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

package com.ramid.framework.db.dynamic.core.redis;

import com.alibaba.fastjson2.JSON;
import com.ramid.framework.db.dynamic.core.DynamicDatasourceEvent;
import com.ramid.framework.db.dynamic.core.DynamicDatasourceEventPublish;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Levin
 */
@RequiredArgsConstructor
public class RedisDynamicDatasourcePublish implements DynamicDatasourceEventPublish {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void publish(DynamicDatasourceEvent message) {
        log.info("redis publish - {}", message);
        redisTemplate.convertAndSend(DEFAULT_EVENT_TOPIC, JSON.toJSONString(message));
    }

}
