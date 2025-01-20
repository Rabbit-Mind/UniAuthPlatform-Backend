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
package com.ramid.ua.platform.iam.tenant.domain.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Levin
 */
@Data
@Schema(name = "ProductDefinitionSaveReq")
public class ProductDefinitionSaveReq {
    
    @NotBlank(message = "产品名称不能为空")
    @Length(min = 1, max = 100, message = "产品名称长度 {min} - {max}")
    @Schema(description = "产品名称")
    private String name;
    
    @Schema(description = "产品Logo链接")
    private String logo;
    
    @Length(max = 10086, message = "产品描述超过 {max} 描述,请简化描述")
    @Schema(description = "产品描述")
    private String description;
    
}
