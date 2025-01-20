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

package com.ramid.framework.boot.log;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Levin
 */
@Data
@ConfigurationProperties(AccessLogProperties.PREFIX)
public class AccessLogProperties {

    public static final String PREFIX = "extend.boot.log.access";

    private boolean enabled = true;

    /**
     * 令牌Key
     */
    private String token = "Authorization";

    /**
     * traceId
     */
    private String trace = "traceId";
    /**
     * 日志存储策略
     * 如果不是 authority 服务 请配置 feign 的策略
     */
    private StoreStrategy strategy = StoreStrategy.local;

    /**
     * 存储策略
     */
    public enum StoreStrategy {

        /**
         * feign = feign 请求
         * local = 本地
         */
        feign, local

    }

}
