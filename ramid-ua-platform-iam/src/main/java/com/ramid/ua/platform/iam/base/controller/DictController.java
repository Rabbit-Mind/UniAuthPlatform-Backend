/*
 * Copyright (c) 2023 RAMID-UNI-AUTH-PLATFORM Authors. All Rights Reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ramid.ua.platform.iam.base.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ramid.framework.commons.BeanUtilPlus;
import com.ramid.framework.commons.annotation.log.AccessLog;
import com.ramid.framework.commons.entity.Dict;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.iam.base.domain.dto.req.DictSaveReq;
import com.ramid.ua.platform.iam.base.domain.dto.resp.SysDictResp;
import com.ramid.ua.platform.iam.base.domain.entity.SysDict;
import com.ramid.ua.platform.iam.base.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典类型
 *
 * @author Levin
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/dict")
@Tag(name = "系统字典", description = "系统字典")
@RequiredArgsConstructor
public class DictController {
    
    private final DictService dictService;
    
    @GetMapping("/list")
    @Operation(summary = "字典列表 - [DONE] - [Levin]", description = "查询字典列表 - [DONE] - [Levin]")
    @SaCheckPermission(value = {"dict:list"})
    public List<SysDictResp> list() {
        List<SysDict> list = this.dictService.list(Wraps.<SysDict>lbQ().eq(SysDict::getStatus, true).orderByAsc(SysDict::getSequence));
        return BeanUtilPlus.toBeans(list, SysDictResp.class);
    }
    
    @PostMapping("/refresh")
    @AccessLog(module = "系统字典", description = "刷新字典")
    @Operation(summary = "刷新字典 - [DONE] - [Levin]", description = "刷新字典缓存数据 - [DONE] - [Levin]")
    @SaCheckPermission(value = {"dict:refresh"})
    public void refresh() {
        this.dictService.refresh();
    }
    
    @PostMapping("/create")
    @AccessLog(module = "系统字典", description = "字典新增")
    @Operation(summary = "新增字典 - [DONE] - [Levin]", description = "新增字典 - [DONE] - [Levin]")
    @SaCheckPermission(value = {"dict:add"})
    public void create(@Validated @RequestBody DictSaveReq req) {
        this.dictService.create(req);
    }
    
    @PutMapping("/{id}")
    @AccessLog(module = "系统字典", description = "字典编辑")
    @Operation(summary = "编辑字典 - [DONE] - [Levin]", description = "编辑字典 - [DONE] - [Levin]")
    @SaCheckPermission(value = {"dict:edit"})
    public void modify(@PathVariable Long id, @Validated @RequestBody DictSaveReq req) {
        this.dictService.modify(id, req);
    }
    
    @DeleteMapping("/{id}")
    @AccessLog(module = "系统字典", description = "删除指定字典项")
    @Operation(summary = "删除字典 - [DONE] - [Levin]", description = "删除字典 - [DONE] - [Levin]")
    @SaCheckPermission(value = {"dict:remove"})
    public void remove(@PathVariable Long id) {
        this.dictService.deleteById(id);
    }
    
    @GetMapping("/{code}/list")
    @Operation(summary = "查询字典子项 - [DONE] - [Levin]", description = "查询字典子项")
    @Parameters({
            @Parameter(name = "Accept-Language", description = "语言", example = "zh-CN,zh;q=0.9", in = ParameterIn.HEADER),
            @Parameter(name = "code", description = "编码", in = ParameterIn.PATH),
    })
    public List<Dict<String>> list(@PathVariable("code") String code) {
        return dictService.findItemByCode(code);
    }
}
