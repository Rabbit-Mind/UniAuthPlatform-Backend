package com.ramid.ua.platform.tms.controller;

import com.ramid.ua.platform.tms.domain.req.OrderEventSaveReq;
import com.ramid.ua.platform.tms.domain.resp.OrderEventResp;
import com.ramid.ua.platform.tms.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Levin
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
@Tag(name = "节点模块",description = "节点模块")
public class EventController {

    private final EventService eventService;

    @PostMapping("/list")
    @Operation(summary = "节点列表 - [Levin] - [DONE]")
    public List<OrderEventResp> pageList(@RequestBody OrderEventSaveReq req) {
        return this.eventService.listEvent(req.getOrderId());
    }

    @PostMapping("/create")
    @Operation(summary = "创建节点 - [Levin] - [DONE]")
    public OrderEventResp created(@RequestBody OrderEventSaveReq req) {
        return this.eventService.createEvent(req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "取消节点 - [Levin] - [DONE]")
    public OrderEventResp cancel(@PathVariable Long id) {
        return this.eventService.cancelEvent(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑节点 - [Levin] - [DONE]")
    public OrderEventResp update(@PathVariable Long id, @RequestBody OrderEventSaveReq req) {
        return this.eventService.updateEvent(id, req);
    }
}
