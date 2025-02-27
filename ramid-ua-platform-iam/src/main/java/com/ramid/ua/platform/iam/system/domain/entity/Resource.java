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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ramid.framework.commons.entity.SuperEntity;
import com.ramid.ua.platform.iam.system.domain.enums.ResourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 菜单
 *
 * @author Levin
 * @since 2019-11-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_resource")
@Schema(name = "Resource", description = "资源")
public class Resource extends SuperEntity<Long> {
    
    @Schema(description = "权限编码")
    @TableField(value = "permission")
    private String permission;
    
    @Schema(description = "名称")
    private String title;
    
    @Schema(description = "菜单ID")
    private Long parentId;
    
    @Schema(description = "类型")
    private ResourceType type;
    
    @Schema(description = "排序")
    private Integer sequence;
    
    @Schema(description = "图标")
    private String icon;
    
    @Schema(description = "路径")
    private String path;
    
    @Schema(description = "组件")
    private String component;
    
    @Schema(description = "面是否开启缓存，开启后页面会缓存，不会重新加载，仅在标签页启用时有效")
    private Boolean keepAlive;

    @Schema(description = "全局菜单")
    private Boolean global;
    
    @Schema(description = "显示/隐藏")
    private Boolean visible;
    
    @Schema(description = "状态")
    private Boolean status;
    
    @Schema(description = "描述")
    private String description;

    @Schema(description = "自定义菜单 meta 属性")
    private String meta;
}
