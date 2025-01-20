package com.ramid.ua.platform.wms.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ramid.framework.commons.BeanUtilPlus;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.wms.basic.domain.entity.Location;
import com.ramid.ua.platform.wms.basic.domain.req.LocationSaveReq;
import com.ramid.ua.platform.wms.basic.mapper.LocationMapper;
import com.ramid.ua.platform.wms.basic.service.LocationService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 储位表 服务实现类
 * </p>
 *
 * @author ddCat
 * @since 2024-06-17
 */
@Service
public class LocationServiceImpl extends SuperServiceImpl<LocationMapper, Location> implements LocationService {

    @Override
    public void create(LocationSaveReq req) {
        Long count = this.baseMapper.selectCount(Location::getCode, req.getCode());
        if (count != null && count > 0) {
            throw CheckedException.badRequest("该储位编号已存在");
        }
        this.baseMapper.insert(BeanUtil.toBean(req, Location.class));
    }

    @Override
    public void modify(Long id, LocationSaveReq req) {
        Long count = this.baseMapper.selectCount(Wraps.<Location>lbQ().ne(Location::getId, id).eq(Location::getCode, req.getCode()));
        if (count != null && count > 0) {
            throw CheckedException.badRequest("该储位编号已存在");
        }
        var bean = BeanUtilPlus.toBean(id, req, Location.class);
        this.baseMapper.updateById(bean);
    }
}
