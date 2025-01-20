package com.ramid.ua.platform.tms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.ua.platform.tms.domain.req.ExpensePageReq;
import com.ramid.ua.platform.tms.domain.req.ExpenseSaveReq;
import com.ramid.ua.platform.tms.domain.resp.ExpensePageResp;
import com.ramid.ua.platform.tms.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Levin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
@Tag(name = "车辆规费", description = "车辆规费")
public class ExpenseController {

    private final ExpenseService expenseService;
    @PostMapping("/page")
    @Operation(summary = "车辆规费列表信息 - [Levin] - [DONE]")
    public IPage<ExpensePageResp> pageList(@RequestBody ExpensePageReq req) {
        return this.expenseService.pageList(req);
    }

    @PostMapping("/create")
    @Operation(summary = "删除车辆规费 - [Levin] - [DONE]")
    public void created(@Validated @RequestBody ExpenseSaveReq req) {
        this.expenseService.created(req);
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑车辆规费 - [Levin] - [DONE]")
    public void edit(@PathVariable Long id, @Validated @RequestBody ExpenseSaveReq req) {
        this.expenseService.edit(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除车辆规费 - [Levin] - [DONE]")
    public void remove(@PathVariable Long id) {
        this.expenseService.removeById(id);
    }

}
