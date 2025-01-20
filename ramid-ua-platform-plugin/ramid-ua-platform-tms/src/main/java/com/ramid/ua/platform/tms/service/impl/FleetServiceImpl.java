package com.ramid.ua.platform.tms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.tms.domain.entity.Fleet;
import com.ramid.ua.platform.tms.domain.req.FleetPageReq;
import com.ramid.ua.platform.tms.domain.req.FleetSaveReq;
import com.ramid.ua.platform.tms.domain.resp.FleetPageResp;
import com.ramid.ua.platform.tms.mapper.FleetMapper;
import com.ramid.ua.platform.tms.service.FleetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Levin
 */
@Service
@RequiredArgsConstructor
public class FleetServiceImpl extends SuperServiceImpl<FleetMapper, Fleet> implements FleetService {


    @Override
    public IPage<FleetPageResp> pageList(FleetPageReq req) {
        return this.baseMapper.selectPage(req.buildPage(), Wraps.<Fleet>lbQ().like(Fleet::getFleetName, req.getFleetName())
                        .like(Fleet::getLeaderMobile, req.getLeaderMobile())
                        .like(Fleet::getLeaderRealName, req.getLeaderRealName()))
                .convert(x -> BeanUtil.toBean(x, FleetPageResp.class));
    }

    @Override
    public void created(FleetSaveReq req) {
        this.baseMapper.insert(BeanUtil.toBean(req, Fleet.class));
    }

    @Override
    public void edit(Long id, FleetSaveReq req) {
        Optional.ofNullable(this.baseMapper.selectById(id)).orElseThrow(() -> CheckedException.notFound("司机信息不存在"));
        final Fleet bean = BeanUtil.toBean(req, Fleet.class);
        bean.setId(id);
        this.baseMapper.updateById(bean);
    }
}
