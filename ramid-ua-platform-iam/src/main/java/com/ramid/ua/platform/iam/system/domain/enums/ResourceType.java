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

package com.ramid.ua.platform.iam.system.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ramid.framework.commons.entity.DictEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 资源类型-枚举
 *
 * @author Levin
 */
@Getter
@JsonFormat
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "资源类型-枚举")
public enum ResourceType implements DictEnum<String> {
    
    DIRECTORY("directory", "目录"),
    MENU("menu", "菜单"),
    IFRAME("iframe", "内嵌"),
    LINK("link", "外链"),
    BUTTON("button", "按钮"),
    ;
    
    @EnumValue
    @JsonValue
    @Schema(description = "资源类型")
    private String type;
    
    @Schema(description = "描述")
    private String desc;
    
    @JsonCreator
    public static ResourceType of(String type) {
        if (type == null) {
            return null;
        }
        for (ResourceType info : values()) {
            if (info.type.equals(type)) {
                return info;
            }
        }
        return null;
    }
    
    @Override
    public String getValue() {
        return this.type;
    }
    
    @Override
    public String toString() {
        return type;
    }
    
}
