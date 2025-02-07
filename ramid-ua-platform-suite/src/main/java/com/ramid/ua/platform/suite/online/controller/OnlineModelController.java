package com.ramid.ua.platform.suite.online.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.suite.online.dialect.FastCrudDialect;
import com.ramid.ua.platform.suite.online.domain.entity.OnlineModel;
import com.ramid.ua.platform.suite.online.domain.req.OnlineFormDesignSaveReq;
import com.ramid.ua.platform.suite.online.domain.req.OnlineFormDesignerPageReq;
import com.ramid.ua.platform.suite.online.domain.req.OnlineFormDesignerSaveReq;
import com.ramid.ua.platform.suite.online.domain.resp.OnlineFormDesignerDetailResp;
import com.ramid.ua.platform.suite.online.domain.resp.OnlineFormDesignerPageResp;
import com.ramid.ua.platform.suite.online.service.OnlineModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/online-model")
@RequiredArgsConstructor
@Tag(name = "表单设计", description = "表单设计")
public class OnlineModelController {
    
    private final OnlineModelService onlineModelService;
    
    @Operation(summary = "分页查询", description = "分页查询")
    @PostMapping("/page")
    public IPage<OnlineFormDesignerPageResp> pageList(@RequestBody OnlineFormDesignerPageReq req) {
        return onlineModelService.pageList(req);
    }
    
    @PostMapping("/create")
    @Operation(summary = "新增表单 - [Levin] - [DONE]")
    public void created(@Validated @RequestBody OnlineFormDesignerSaveReq req) {
        this.onlineModelService.created(req);
    }
    
    @GetMapping("/{id}/detail")
    @Operation(summary = "查看详情 - [Levin] - [DONE]")
    public OnlineFormDesignerDetailResp detail(@PathVariable Long id) {
        return this.onlineModelService.detail(id);
    }
    
    @PostMapping("/{id}/form-design")
    @Operation(summary = "表单设计 - [Levin] - [DONE]")
    public void formDesign(@PathVariable Long id, @Validated @RequestBody OnlineFormDesignSaveReq req) {
        this.onlineModelService.formDesign(id, req);
    }
    
    @PutMapping("/{id}/modify")
    @Operation(summary = "修改表单 - [Levin] - [DONE]")
    public void modify(@PathVariable Long id, @Validated @RequestBody OnlineFormDesignerSaveReq req) {
        this.onlineModelService.modify(id, req);
    }
    
    @GetMapping("/fast-crud")
    @Operation(summary = "fast-crud模板 - [Levin] - [DONE]")
    public JSONObject fastCrud(@RequestParam String definitionKey) {
        OnlineModel model = this.onlineModelService.getOne(Wraps.<OnlineModel>lbQ().eq(OnlineModel::getDefinitionKey, definitionKey));
        if (model == null) {
            return new JSONObject() {
                
                {
                    put("columns", null);
                }
            };
        }
        JSONObject fastCrud = FastCrudDialect.toFastCrud(model.getFormSchemas());
        return new JSONObject() {
            
            {
                put("columns", fastCrud);
            }
        };
    }
    
    @GetMapping("/detail")
    @Operation(summary = "模型详情- [Levin] - [DONE]", description = "根据参数获取详情")
    public OnlineFormDesignerDetailResp detail(@RequestParam String definitionKey) {
        return this.onlineModelService.detail(definitionKey);
    }
}
