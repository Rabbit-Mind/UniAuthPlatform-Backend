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

package com.ramid.framework.db.dynamic.core.local;

import com.ramid.framework.db.dynamic.DynamicDataSourceHandler;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;

/**
 * 本地监听
 *
 * @author levin
 */
@Slf4j
@RequiredArgsConstructor
public class DynamicDatasourceLocalListener implements ApplicationListener<DynamicDatasourceEvent> {

    private final DynamicDataSourceHandler dynamicDataSourceHandler;

    @Override
    @EventListener
    public void onApplicationEvent(@Nonnull DynamicDatasourceEvent event) {
        dynamicDataSourceHandler.handler(event.getAction(), event.getDatasource());
    }
}