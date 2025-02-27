package com.ramid.ua.platform.iam.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.ramid.framework.boot.remote.dict.DictLoadService;
import com.ramid.framework.commons.BeanUtilPlus;
import com.ramid.framework.commons.entity.Dict;
import com.ramid.framework.commons.entity.Entity;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.commons.security.AuthenticationContext;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.framework.db.utils.TenantHelper;
import com.ramid.ua.platform.iam.base.domain.entity.SysDict;
import com.ramid.ua.platform.iam.base.domain.entity.SysDictItem;
import com.ramid.ua.platform.iam.base.repository.SysDictItemMapper;
import com.ramid.ua.platform.iam.base.repository.SysDictMapper;
import com.ramid.ua.platform.iam.tenant.domain.dto.req.TenantDictSaveReq;
import com.ramid.ua.platform.iam.tenant.domain.entity.TenantDict;
import com.ramid.ua.platform.iam.tenant.domain.entity.TenantDictItem;
import com.ramid.ua.platform.iam.tenant.repository.TenantDictItemMapper;
import com.ramid.ua.platform.iam.tenant.repository.TenantDictMapper;
import com.ramid.ua.platform.iam.tenant.service.TenantDictService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 租户业务字典
 *
 * @author Levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantDictServiceImpl extends SuperServiceImpl<TenantDictMapper, TenantDict> implements TenantDictService {
    
    private final AuthenticationContext context;
    private final SysDictMapper dictMapper;
    private final SysDictItemMapper dictItemMapper;
    private final TenantDictItemMapper tenantDictItemMapper;
    private final DictLoadService dictLoadService;
    
    @PostConstruct
    public void init() {
        refresh();
    }
    
    @Override
    public void create(TenantDictSaveReq req) {
        if (req == null) {
            throw CheckedException.notFound("字典内容不能为空");
        }
        final Long count = this.baseMapper.selectCount(TenantDict::getCode, req.getCode());
        if (count != 0 && count > 0) {
            throw CheckedException.badRequest("字典类型编码重复");
        }
        this.baseMapper.insert(BeanUtil.toBean(req, TenantDict.class));
    }
    
    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void modify(Long id, TenantDictSaveReq req) {
        Optional.ofNullable(this.baseMapper.selectById(id)).orElseThrow(() -> CheckedException.notFound("字典不存在"));
        final Long count = this.baseMapper.selectCount(Wraps.<TenantDict>lbQ().ne(TenantDict::getId, id).eq(TenantDict::getCode, req.getCode()));
        if (count != 0 && count > 0) {
            throw CheckedException.badRequest("字典类型编码重复");
        }
        TenantDict bean = BeanUtilPlus.toBean(id, req, TenantDict.class);
        this.baseMapper.updateById(bean);
        this.dictLoadService.refreshCache(getPairMap(List.of(req.getCode())));
    }
    
    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        final TenantDict dict = Optional.ofNullable(this.baseMapper.selectById(id)).orElseThrow(() -> CheckedException.notFound("字典不存在"));
        this.baseMapper.deleteById(id);
        this.tenantDictItemMapper.delete(Wraps.<TenantDictItem>lbQ().eq(TenantDictItem::getDictCode, dict.getCode()));
    }
    
    @Override
    public void refresh() {
        List<TenantDict> list = this.baseMapper.selectList(TenantDict::getStatus, true);
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<String> codeList = list.stream().map(TenantDict::getCode).distinct().toList();
        this.dictLoadService.refreshCache(getPairMap(codeList));
    }
    
    private Map<String, List<Pair<String, String>>> getPairMap(List<String> codeList) {
        return this.tenantDictItemMapper.selectList(Wraps.<TenantDictItem>lbQ()
                .eq(TenantDictItem::getStatus, true))
                .stream()
                .collect(groupingBy(
                        TenantDictItem::getDictCode,
                        Collectors.mapping(item -> Pair.of(item.getValue(), item.getLabel()), Collectors.toList())));
    }
    
    @Override
    public List<Dict<String>> findItemByCode(String code) {
        Map<Object, Object> map = this.dictLoadService.findByIds(code);
        if (CollUtil.isEmpty(map)) {
            return null;
        }
        List<Dict<String>> dictList = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            dictList.add(new Dict<>(entry.getKey() + "", entry.getValue() + ""));
        }
        return dictList;
    }
    
    @Override
    public void incrSyncTenantDict(Long tenantId) {
        // 查询超管 所有字典数据
        // TODO 默认查询租户ID = 1 的 , 后续改造成配置文件读取的 超管租户ID
        List<SysDict> dictList = TenantHelper.executeWithMaster(() -> dictMapper.selectList(SysDict::getType, 1));
        if (CollUtil.isEmpty(dictList)) {
            log.warn("未查询到有效的数据字典");
            return;
        }
        List<TenantDict> dictTypeList = dictList.stream().map(x -> {
            TenantDict dict = BeanUtil.toBean(x, TenantDict.class);
            dict.setId(null);
            dict.setReadonly(true);
            dict.setTenantId(tenantId);
            dict.setCreatedTime(Instant.now());
            dict.setCreatedBy(context.userId());
            dict.setCreatedName(context.nickName());
            dict.setLastModifiedTime(Instant.now());
            dict.setLastModifiedBy(context.userId());
            dict.setLastModifiedName(context.nickName());
            return dict;
        }).toList();
        List<Long> dictIdList = dictList.stream().map(Entity::getId).toList();
        List<TenantDictItem> dictDataList = TenantHelper.executeWithMaster(() -> dictItemMapper.selectList(Wraps.<SysDictItem>lbQ().in(SysDictItem::getDictId, dictIdList)))
                .stream()
                .map(x -> {
                    TenantDictItem item = BeanUtil.toBean(x, TenantDictItem.class);
                    item.setId(null);
                    item.setReadonly(true);
                    item.setTenantId(tenantId);
                    item.setCreatedTime(Instant.now());
                    item.setCreatedBy(context.userId());
                    item.setCreatedName(context.nickName());
                    item.setLastModifiedTime(Instant.now());
                    item.setLastModifiedBy(context.userId());
                    item.setLastModifiedName(context.nickName());
                    return item;
                }).toList();
        // 理论上如果是管理员刷新租户字典那么需要给租户的数据给删除然后重新添加
        this.baseMapper.delete(Wraps.<TenantDict>lbQ().eq(TenantDict::getTenantId, tenantId).eq(TenantDict::getReadonly, true));
        this.tenantDictItemMapper.delete(Wraps.<TenantDictItem>lbQ().eq(TenantDictItem::getTenantId, tenantId).eq(TenantDictItem::getReadonly, true));
        // 将新数据写入到租户字典表中
        this.baseMapper.insertBatchSomeColumn(dictTypeList);
        this.tenantDictItemMapper.insertBatchSomeColumn(dictDataList);
    }
}
