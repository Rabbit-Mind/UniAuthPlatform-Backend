package com.ramid.ua.platform.suite.file.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.suite.file.domain.dto.req.FileStoragePageReq;
import com.ramid.ua.platform.suite.file.domain.dto.resp.FileStoragePageResp;
import com.ramid.ua.platform.suite.file.domain.entity.FileStorage;
import com.ramid.ua.platform.suite.file.domain.entity.FileStorageSetting;
import com.ramid.ua.platform.suite.file.domain.enums.MineType;
import com.ramid.ua.platform.suite.file.event.StorageSettingTemplate;
import com.ramid.ua.platform.suite.file.repository.FileStorageMapper;
import com.ramid.ua.platform.suite.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.hash.HashInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

/**
 * @author xiao1
 * @since 2024-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl extends SuperServiceImpl<FileStorageMapper, FileStorage> implements FileStorageService {


    private final org.dromara.x.file.storage.core.FileStorageService fileStorageService;
    private final StorageSettingTemplate storageSettingTemplate;

    private static final long KB = 1024;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;
    private static final long TB = GB * 1024;


    @Override
    public FileStorage upload(MultipartFile file) {
        FileStorageSetting setting = storageSettingTemplate.getDefaultStorageSetting();
        String platform = setting.getPlatform();
        if (fileStorageService.getFileStorage(platform) == null) {
            throw CheckedException.badRequest("未找到对应的存储平台，请检查配置");
        }
        FileInfo info = fileStorageService.of(file).setPlatform(platform).upload();
        FileStorage storage = toFileInfoRecord(info);
        storage.setCategory(MineType.ofName(info.getContentType()));
        storage.setPlatform(platform);
        this.baseMapper.insert(storage);
        return storage;
    }

    @Override
    public FileStorage uploadImage(MultipartFile file) {
        FileInfo info = fileStorageService
                .of(file)
//                .setThumbnailSuffix() //指定缩略图后缀，必须是 thumbnailator 支持的图片格式，默认使用全局的
//                .setSaveThFilename() //指定缩略图的保存文件名，注意此文件名不含后缀，默认自动生成
                // 将图片大小调整到 1000*1000
                .image(img -> img.size(1000, 1000))
                // 再生成一张 200*200 的缩略图
                .thumbnail(th -> th.size(200, 200))
                .upload();
        return toFileInfoRecord(info);
    }

    @Override
    public void delete(Long id) {
        FileStorage storage = Optional.ofNullable(this.baseMapper.selectById(id)).orElseThrow(() -> CheckedException.notFound("文件不存在"));
        org.dromara.x.file.storage.core.platform.FileStorage fileStorage = fileStorageService.getFileStorage(storage.getPlatform());
        if (fileStorage == null) {
            throw CheckedException.badRequest("未找到对应的存储平台或对应平台未开启");
        }
        FileInfo fileInfo = toFileInfo(storage);
        if (fileStorageService.delete(fileInfo)) {
            this.baseMapper.deleteById(id);
        }
    }

    @Override
    public void rename(Long id, String originName) {
        this.baseMapper.updateById(FileStorage.builder().id(id).originalFilename(originName).build());
    }

    @Override
    public IPage<FileStoragePageResp> pageList(FileStoragePageReq req) {
        return this.baseMapper.selectPage(req.buildPage(), Wraps.<FileStorage>lbQ()
                        .eq(FileStorage::getCategory, req.getCategory())
                        .like(FileStorage::getOriginalFilename, req.getOriginalFilename())
                        .like(FileStorage::getCreatedName, req.getCreatedName()))
                .convert(x -> BeanUtil.toBean(x, FileStoragePageResp.class));
    }

    /**
     * 将 FileInfo 转为 FileInfoRecord
     */
    public FileStorage toFileInfoRecord(FileInfo info) {
        FileStorage detail = BeanUtil.copyProperties(
                info, FileStorage.class, "metadata", "userMetadata", "thMetadata", "thUserMetadata", "attr", "hashInfo");
        detail.setMetadata(JSON.toJSONString(info.getMetadata()));
        detail.setUserMetadata(JSON.toJSONString(info.getUserMetadata()));
        detail.setThMetadata(JSON.toJSONString(info.getThMetadata()));
        detail.setThUserMetadata(JSON.toJSONString(info.getThUserMetadata()));
        detail.setAttr(JSON.toJSONString(info.getAttr()));
        detail.setHashInfo(JSON.toJSONString(info.getHashInfo()));
        detail.setFormatSize(formatFileSize(info.getSize()));
        return detail;
    }

    /**
     * 将 FileInfoRecord 转为 FileInfo
     */
    public FileInfo toFileInfo(FileStorage detail) {
        FileInfo info = BeanUtil.copyProperties(detail, FileInfo.class, "metadata", "userMetadata", "thMetadata", "thUserMetadata", "attr", "hashInfo");
        // 这里手动获取数据库中的 json 字符串 并转成 元数据，方便使用
        info.setMetadata(jsonToMetadata(detail.getMetadata()));
        info.setUserMetadata(jsonToMetadata(detail.getUserMetadata()));
        info.setThMetadata(jsonToMetadata(detail.getThMetadata()));
        info.setThUserMetadata(jsonToMetadata(detail.getThUserMetadata()));
        // 这里手动获取数据库中的 json 字符串 并转成 附加属性字典，方便使用
        info.setAttr(JSON.parseObject(detail.getAttr(), Dict.class));
        // 这里手动获取数据库中的 json 字符串 并转成 哈希信息，方便使用
        info.setHashInfo(JSON.parseObject(detail.getHashInfo(), HashInfo.class));
        return info;
    }

    /**
     * 将 json 字符串转换成元数据对象
     */
    public Map<String, String> jsonToMetadata(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, new TypeReference<>() {
        });
    }


    public static String formatFileSize(long bytes) {
        if (bytes >= TB) {
            return String.format("%.2f TB", bytes / (double) TB);
        } else if (bytes >= GB) {
            return String.format("%.2f GB", bytes / (double) GB);
        } else if (bytes >= MB) {
            return String.format("%.2f MB", bytes / (double) MB);
        } else if (bytes >= KB) {
            return String.format("%.2f KB", bytes / (double) KB);
        } else {
            return String.format("%d B", bytes);
        }
    }

}
