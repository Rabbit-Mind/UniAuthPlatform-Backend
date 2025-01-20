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

package com.ramid.ua.platform.iam.system.domain.dto.req;

import com.ramid.framework.db.mybatisplus.page.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位分页查询
 *
 * @author Levin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "PositionPageReq", description = "岗位分页查询")
public class PositionPageReq extends PageRequest {
    
    @Schema(description = "名称")
    private String title;
    
    @Schema(description = "组织ID")
    private Long orgId;
    
    @Schema(description = "类型")
    private Integer type;
    
    @Schema(description = "状态")
    private Boolean status;
    
}
