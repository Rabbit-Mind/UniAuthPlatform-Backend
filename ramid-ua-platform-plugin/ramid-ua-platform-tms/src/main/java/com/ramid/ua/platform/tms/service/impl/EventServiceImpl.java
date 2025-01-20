package com.ramid.ua.platform.tms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ramid.framework.commons.security.AuthenticationContext;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.tms.domain.entity.TmsOrder;
import com.ramid.ua.platform.tms.domain.entity.TmsOrderEvent;
import com.ramid.ua.platform.tms.domain.entity.TmsOrderEventFile;
import com.ramid.ua.platform.tms.domain.req.OrderEventFileSaveReq;
import com.ramid.ua.platform.tms.domain.req.OrderEventSaveReq;
import com.ramid.ua.platform.tms.domain.resp.OrderEventResp;
import com.ramid.ua.platform.tms.mapper.OrderMapper;
import com.ramid.ua.platform.tms.mapper.TmsOrderEventFileMapper;
import com.ramid.ua.platform.tms.mapper.TmsOrderEventMapper;
import com.ramid.ua.platform.tms.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 节点
 *
 * @author Levin
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl extends SuperServiceImpl<TmsOrderEventMapper, TmsOrderEvent> implements EventService {

    private final AuthenticationContext authenticationContext;
    private final OrderMapper orderMapper;
    private final TmsOrderEventFileMapper eventFileMapper;

    @Override
    public List<OrderEventResp> listEvent(Long orderId) {
        return this.baseMapper.selectList(Wraps.<TmsOrderEvent>lbQ()
                .eq(TmsOrderEvent::getOrderId, orderId)
                .eq(TmsOrderEvent::getLocked,false)
        ).stream().map( event -> {
                    final OrderEventResp eventResp = BeanUtil.toBean(event, OrderEventResp.class);
                    return eventResp;
        }).collect(Collectors.toList());
    }

    @Override
    public OrderEventResp createEvent(OrderEventSaveReq req) {
        Long orderId = req.getOrderId();
        TmsOrder order = orderMapper.selectById(orderId);
        if (order==null){
            return null;
        }
        final TmsOrderEvent event = BeanUtil.toBean(req, TmsOrderEvent.class);
        event.setId(null);
        event.setOrderNo(order.getOrderNo());
        event.setOperatorId(authenticationContext.userId());
        event.setLocked(false);
        this.baseMapper.insert(event);

        List<OrderEventFileSaveReq> files = req.getFiles();
        if (CollUtil.isNotEmpty(files)) {
            final List<TmsOrderEventFile> eventFileList = files.stream().map( file -> {
                final TmsOrderEventFile eventFile = TmsOrderEventFile.builder()
                        .orderId(event.getOrderId())
                        .eventId(event.getId())
                        .fileId(file.getFileId())
                        .sourceId(event.getSourceId())
                        .locked(false)
                        .build();
                return eventFile;
            }).collect(Collectors.toList());
            this.eventFileMapper.insertBatchSomeColumn(eventFileList);
        }

        OrderEventResp eventResp = new OrderEventResp();
        eventResp.setId(event.getId());
        return eventResp;
    }

    @Override
    public OrderEventResp cancelEvent(Long id) {
        TmsOrderEvent event = this.baseMapper.selectById(id);
        if (event==null){
            return null;
        }

        event.setLocked(true);
        this.baseMapper.updateById(event);

        OrderEventResp resp = new OrderEventResp();
        resp.setId(id);
        return resp;
    }

    @Override
    public OrderEventResp updateEvent(Long id, OrderEventSaveReq req) {
        TmsOrderEvent oldEvent = this.baseMapper.selectById(id);
        if (oldEvent==null){
            return null;
        }

        final TmsOrderEvent newEvent = BeanUtil.toBean(req, TmsOrderEvent.class);
        newEvent.setId(id);
        newEvent.setOrderNo(null);
        this.baseMapper.updateById(newEvent);

        TmsOrderEventFile oldFile = TmsOrderEventFile.builder().locked(true).build();
        this.eventFileMapper.update(oldFile, Wrappers.<TmsOrderEventFile>lambdaUpdate()
                .eq(TmsOrderEventFile::getEventId,id)
                .eq(TmsOrderEventFile::getLocked,false));
        List<OrderEventFileSaveReq> files = req.getFiles();
        if (CollUtil.isNotEmpty(files)) {
            final List<TmsOrderEventFile> eventFileList = files.stream().map( file -> {
                final TmsOrderEventFile eventFile = TmsOrderEventFile.builder()
                        .orderId(newEvent.getOrderId())
                        .eventId(id)
                        .fileId(file.getFileId())
                        .sourceId(newEvent.getSourceId())
                        .locked(false)
                        .build();
                return eventFile;
            }).collect(Collectors.toList());
            this.eventFileMapper.insertBatchSomeColumn(eventFileList);
        }

        OrderEventResp resp = new OrderEventResp();
        resp.setId(id);
        return resp;
    }

}
