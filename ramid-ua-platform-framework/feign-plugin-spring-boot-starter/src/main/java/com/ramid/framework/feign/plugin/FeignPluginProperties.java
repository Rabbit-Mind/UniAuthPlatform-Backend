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

package com.ramid.framework.feign.plugin;

import com.ramid.framework.feign.plugin.mock.MockProperties;
import com.ramid.framework.feign.plugin.token.AutoRefreshTokenProperties;
import feign.Logger;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author levin
 */
@Data
@ConfigurationProperties(prefix = FeignPluginProperties.PLUGIN_PREFIX)
public class FeignPluginProperties {

    public static final String PLUGIN_PREFIX = "extend.feign.plugin";
    private boolean enabled = true;

    /**
     * Feign 日志级别（FULL 意味着会输出详细日志,建议值在非生产环境使用）
     */
    private Logger.Level level = Logger.Level.FULL;
    private MockProperties mock = new MockProperties();
    private AutoRefreshTokenProperties token = new AutoRefreshTokenProperties();

}