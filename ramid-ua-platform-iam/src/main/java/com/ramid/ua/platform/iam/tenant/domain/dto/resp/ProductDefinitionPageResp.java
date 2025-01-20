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
package com.ramid.ua.platform.iam.tenant.domain.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

/**
 * @author Levin
 */
@Data
public class ProductDefinitionPageResp {
    
    @Schema(description = "ID")
    private Long id;
    
    @Schema(description = "产品编码")
    private String code;
    
    @Schema(description = "产品名称")
    private String name;
    
    @Schema(description = "产品Logo链接")
    private String logo;
    
    @Schema(description = "产品详情")
    private String description;
    
    @Schema(description = "启用状态")
    private Boolean status;
    
    @Schema(description = "创建人")
    private String createdName;
    
    @Schema(description = "创建时间")
    private Instant createdTime;
    
}
