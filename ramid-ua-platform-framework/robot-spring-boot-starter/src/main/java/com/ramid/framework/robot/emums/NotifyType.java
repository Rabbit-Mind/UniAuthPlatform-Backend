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

package com.ramid.framework.robot.emums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * NotifyType
 *
 * @author Levin
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "枚举")
@JsonFormat
public enum NotifyType {

    /**
     * 钉钉
     */
    DING_TALK("ding-talk", "钉钉"),
    /**
     * 微信
     */
    WECHAT("wechat", "微信"),
    FEI_SHU("fei-shu", "飞书"),
    EMAIL("email", "邮箱"),
    ;
    @EnumValue
    @JsonValue
    private String type;

    @Schema(description = "描述")
    private String desc;

    @JsonCreator
    public static NotifyType of(String type) {
        if (type == null) {
            return null;
        }
        for (NotifyType info : values()) {
            if (info.type.equals(type)) {
                return info;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(type);
    }

}