package com.ramid.ua.platform.tms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.commons.entity.Dict;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.tms.domain.entity.Fleet;
import com.ramid.ua.platform.tms.domain.req.FleetPageReq;
import com.ramid.ua.platform.tms.domain.req.FleetSaveReq;
import com.ramid.ua.platform.tms.domain.resp.FleetPageResp;
import com.ramid.ua.platform.tms.service.FleetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Levin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/fleets")
@Tag(name = "车队管理", description = "车队管理")
public class FleetController {


    private final FleetService fleetService;

    @Operation(summary = "车队列表 - [Levin] - [DONE]", description = "车队列表")
    @GetMapping("/dict_list")
    public List<Dict<?>> list(Boolean enabled) {
        return this.fleetService.list(Wraps.<Fleet>lbQ().eq(Fleet::getEnabled, enabled))
                .stream()
                .map(x -> Dict.builder().value(x.getId()).label(x.getFleetName()).build())
                .collect(Collectors.toList());
    }

    @PostMapping("/page")
    @Operation(summary = "车队列表信息 - [Levin] - [DONE]")
    public IPage<FleetPageResp> pageList(@RequestBody FleetPageReq req) {
        return this.fleetService.pageList(req);
    }

    @PostMapping("/create")
    @Operation(summary = "删除车队 - [Levin] - [DONE]")
    public void created(@Validated @RequestBody FleetSaveReq req) {
        this.fleetService.created(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑车队 - [Levin] - [DONE]")
    public void edit(@PathVariable Long id, @Validated @RequestBody FleetSaveReq req) {
        this.fleetService.edit(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除车队 - [Levin] - [DONE]")
    public void remove(@PathVariable Long id) {
        this.fleetService.removeById(id);
    }


}
