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

package com.ramid.ua.platform.iam.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ramid.framework.commons.annotation.log.AccessLog;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.iam.system.domain.dto.req.RegisteredClientReq;
import com.ramid.ua.platform.iam.system.domain.dto.resp.RegisteredClientResp;
import com.ramid.ua.platform.iam.system.domain.entity.RegisteredClient;
import com.ramid.ua.platform.iam.system.service.RegisteredClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ramid.ua.platform.iam.system.domain.converts.RegisteredClientConverts.REGISTERED_CLIENT_REF_2_RESP_CONVERTS;

/**
 * 应用管理
 *
 * @author Levin
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/registered-client")
@Tag(name = "终端管理", description = "终端管理")
public class RegisteredClientController {
    
    private final RegisteredClientService registeredClientService;
    
    @GetMapping
    @Parameters({
            @Parameter(description = "clientId", name = "clientId", in = ParameterIn.QUERY),
            @Parameter(description = "clientName", name = "clientName", in = ParameterIn.QUERY)
    })
    @Operation(summary = "应用列表 - [Levin] - [DONE]")
    public IPage<RegisteredClientResp> query(@Parameter(description = "当前页") @RequestParam(required = false, defaultValue = "1") Integer current,
                                             @Parameter(description = "条数") @RequestParam(required = false, defaultValue = "20") Integer size,
                                             String clientId, String clientName) {
        return this.registeredClientService.page(new Page<>(current, size),
                Wraps.<RegisteredClient>lbQ().like(RegisteredClient::getClientId, clientId)
                        .like(RegisteredClient::getClientName, clientName))
                .convert(REGISTERED_CLIENT_REF_2_RESP_CONVERTS::convert);
    }
    
    @PostMapping
    @AccessLog(module = "终端管理", description = "保存应用")
    @Operation(summary = "保存应用")
    public void create(@Validated @RequestBody RegisteredClientReq req) {
        this.registeredClientService.create(req);
    }
    
    @PutMapping("/{id}")
    @AccessLog(module = "终端管理", description = "修改应用")
    @Operation(summary = "修改应用")
    public void modify(@PathVariable Long id, @Validated @RequestBody RegisteredClientReq req) {
        this.registeredClientService.modify(id, req);
    }
    
    @DeleteMapping("/{id}")
    @AccessLog(module = "终端管理", description = "删除应用")
    @Operation(summary = "删除应用")
    public void del(@PathVariable String id) {
        this.registeredClientService.deleteById(id);
    }
    
}
