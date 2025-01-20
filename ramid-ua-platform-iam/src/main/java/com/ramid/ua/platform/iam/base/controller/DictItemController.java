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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.iam.base.domain.dto.req.DictItemPageReq;
import com.ramid.ua.platform.iam.base.domain.dto.req.DictItemSaveReq;
import com.ramid.ua.platform.iam.base.domain.entity.SysDictItem;
import com.ramid.ua.platform.iam.base.service.DictItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 字典项
 *
 * @author Levin
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "系统字典", description = "系统字典")
@RequestMapping("/dict/{dict_id}/items")
public class DictItemController {
    
    private final DictItemService dictItemService;
    
    @GetMapping
    @Operation(summary = "查询字典子项", description = "查询字典子项 - [DONE] - [Levin]")
    @Parameters({
            @Parameter(name = "dict_id", description = "字典ID", in = ParameterIn.PATH),
            @Parameter(name = "label", description = "名称", in = ParameterIn.QUERY)
    })
    public Page<SysDictItem> pageList(@PathVariable("dict_id") Long dictId, DictItemPageReq req) {
        return this.dictItemService.page(req.buildPage(), Wraps.<SysDictItem>lbQ().eq(SysDictItem::getDictId, dictId)
                .eq(SysDictItem::getStatus, req.getStatus()).like(SysDictItem::getLabel, req.getLabel()));
    }
    
    @PostMapping
    @Operation(summary = "添加字典子项", description = "添加字典子项 - [DONE] - [Levin]")
    @Parameter(name = "dict_id", description = "字典ID", in = ParameterIn.PATH)
    public void create(@PathVariable("dict_id") Long dictId, @Validated @RequestBody DictItemSaveReq req) {
        this.dictItemService.create(dictId, req);
    }
    
    @PutMapping("/{item_id}")
    @Operation(summary = "编辑字典子项 - [DONE] - [Levin]", description = "编辑字典子项 - [DONE] - [Levin]")
    @Parameter(name = "dict_id", description = "字典ID", in = ParameterIn.PATH)
    public void modify(@PathVariable("dict_id") Long dictId, @PathVariable("item_id") Long itemId, @Validated @RequestBody DictItemSaveReq req) {
        this.dictItemService.modify(dictId, itemId, req);
        
    }
    
    @DeleteMapping("/{item_id}")
    @Operation(summary = "删除字典子项 - [DONE] - [Levin]", description = "删除字典子项 - [DONE] - [Levin]")
    @Parameter(name = "dict_id", description = "字典ID", in = ParameterIn.PATH)
    public void del(@PathVariable("dict_id") Long dictId, @PathVariable("item_id") Long itemId) {
        this.dictItemService.removeById(itemId);
    }
    
}
