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

package com.ramid.framework.boot.remote.configuration;

import com.ramid.framework.boot.remote.RemoteService;
import com.ramid.framework.boot.remote.dict.DictLoadService;
import com.ramid.framework.boot.remote.properties.RemoteProperties;
import com.ramid.framework.commons.remote.LoadService;
import com.ramid.framework.i18n.I18nMessageProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * 关联字段数据注入工具 自动配置类
 *
 * @author Levin
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(RemoteProperties.class)
@ConditionalOnProperty(prefix = RemoteProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class RemoteAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RemoteResultAspect getRemoteResultAspect(RemoteService remoteService) {
        return new RemoteResultAspect(remoteService);
    }

    @Bean
    @ConditionalOnMissingBean
    public DictLoadService dictLoadService(RedisTemplate<String, Object> redisTemplate, I18nMessageProvider i18nMessageProvider) {
        return new DictLoadService(redisTemplate, i18nMessageProvider);
    }

    /**
     * 回显服务
     *
     * @param strategyMap 回显查询实例
     * @return RemoteService
     */
    @Bean
    @ConditionalOnMissingBean
    public RemoteService getRemoteService(RemoteProperties remoteProperties, Map<String, LoadService> strategyMap) {
        return new RemoteService(remoteProperties, strategyMap);
    }
}
