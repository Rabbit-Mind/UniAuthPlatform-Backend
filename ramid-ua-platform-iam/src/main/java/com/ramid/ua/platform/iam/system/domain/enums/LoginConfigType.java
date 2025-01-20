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
 * 登录配置类型
 *
 * @author Levin
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "登录配置类型")
@JsonFormat
public enum LoginConfigType implements DictEnum<String> {
    
    /**
     * PLATFORM
     */
    PLATFORM("platform", "PLATFORM"),
    /**
     * SAAS
     */
    SAAS("saas", "SAAS"),
    ;
    
    @EnumValue
    @JsonValue
    private String type;
    
    @Schema(description = "描述")
    private String desc;
    
    @JsonCreator
    public static LoginConfigType of(String type) {
        if (type == null) {
            return null;
        }
        for (LoginConfigType info : values()) {
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
        return String.valueOf(type);
    }
    
}
