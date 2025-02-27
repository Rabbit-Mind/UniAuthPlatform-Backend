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

package com.ramid.ua.platform.iam.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ramid.framework.commons.entity.SuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 组织
 *
 * @author Levin
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_org")
@EqualsAndHashCode(callSuper = true)
@Schema(name = "Org", description = "组织")
public class Org extends SuperEntity<Long> {
    
    @Schema(description = "标题")
    private String label;
    
    @Schema(description = "树形结构路径")
    private List<Long> treePath;
    
    @Schema(description = "父ID")
    private Long parentId;
    
    @Schema(description = "排序号")
    private Integer sequence;
    
    @Schema(description = "电话")
    private String tel;
    
    @Schema(description = "租户ID")
    private Long tenantId;
    
    @Schema(description = "简称")
    private String alias;
    
    @Schema(description = "状态")
    private Boolean status;
    
    @Schema(description = "描述")
    private String description;
    
}
