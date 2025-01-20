package com.ramid.ua.platform.wms.matedata.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ramid.framework.commons.BeanUtilPlus;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.commons.security.AuthenticationContext;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.framework.redis.plus.sequence.RedisSequenceHelper;
import com.ramid.ua.platform.wms.inbound.domain.enums.WmsSequence;
import com.ramid.ua.platform.wms.matedata.domain.entity.Brand;
import com.ramid.ua.platform.wms.matedata.domain.req.BrandSaveReq;
import com.ramid.ua.platform.wms.matedata.mapper.BrandMapper;
import com.ramid.ua.platform.wms.matedata.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 品牌 服务实现类
 * </p>
 *
 * @author ddCat
 * @since 2024-06-18
 */
@Service
@RequiredArgsConstructor
public class BrandServiceImpl extends SuperServiceImpl<BrandMapper, Brand> implements BrandService {

    private final RedisSequenceHelper sequenceHelper;
    private final AuthenticationContext context;

    @Override
    public void create(BrandSaveReq req) {
        Long count = this.baseMapper.selectCount(Brand::getName, req.getName());
        if (count != null && count > 0) {
            throw CheckedException.badRequest("品牌已存在");
        }
        var bean = BeanUtil.toBean(req, Brand.class);
        String code = sequenceHelper.generate(WmsSequence.BRAND_NO, context.tenantId());
        bean.setCode(code);
        this.baseMapper.insert(bean);
    }

    @Override
    public void modify(Long id, BrandSaveReq req) {
        Long count = this.baseMapper.selectCount(Wraps.<Brand>lbQ().ne(Brand::getId, id).eq(Brand::getName, req.getName()));
        if (count != null && count > 0) {
            throw CheckedException.badRequest("该储位编号已存在");
        }
        var bean = BeanUtilPlus.toBean(id, req, Brand.class);
        this.baseMapper.updateById(bean);
    }

}
