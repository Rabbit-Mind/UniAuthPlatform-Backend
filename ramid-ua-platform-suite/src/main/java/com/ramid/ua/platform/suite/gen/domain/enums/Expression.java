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
package com.ramid.ua.platform.suite.gen.domain.enums;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ramid.framework.commons.entity.DictEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Levin
 */
@Getter
@RequiredArgsConstructor
@JsonFormat
public enum Expression implements DictEnum<String> {

    /**
     * eq
     */
    EQ(StringPool.EQUALS),
    NE("<>"),
    GT(StringPool.RIGHT_CHEV),
    GE(">="),
    LT(StringPool.LEFT_CHEV),
    LE("<="),
    IS_NULL("IS NULL"),
    LIKE("LIKE"),
    IS_NOT_NULL("IS NOT NULL"),
    ;

    public final String value;

    @JsonCreator
    public static Expression of(String name) {
        for (Expression expression : values()) {
            if (StringUtils.equalsIgnoreCase(expression.name(), name)) {
                return expression;
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String getDesc() {
        return value;
    }
}
