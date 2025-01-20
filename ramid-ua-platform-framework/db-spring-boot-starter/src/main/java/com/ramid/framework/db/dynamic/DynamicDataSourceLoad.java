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

package com.ramid.framework.db.dynamic;

import cn.hutool.core.collection.CollUtil;
import com.ramid.framework.db.dynamic.core.DynamicDatasourceEvent;
import com.ramid.framework.db.dynamic.core.EventAction;
import com.ramid.framework.db.dynamic.feign.TenantFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author levin
 */
@Slf4j
@RequiredArgsConstructor
public class DynamicDataSourceLoad {

    private final DynamicDataSourceHandler dynamicDataSourceHandler;
    private final TenantFeignClient tenantFeignClient;

    public void init() {
        log.debug("extend.mybatis-plus.multi-tenant.strategy eq feign , pull dynamic begin...");
        final List<DynamicDatasourceEvent> result = tenantFeignClient.selectAll();
        if (CollUtil.isEmpty(result)) {
            log.warn("feign pull tenantDynamicDataSources is null......");
            return;
        }
        result.forEach(tenantDynamicDataSource -> dynamicDataSourceHandler.handler(EventAction.ADD, tenantDynamicDataSource));
        log.debug("extend.mybatis-plus.multi-tenant.strategy eq feign , pull dynamic end...");
    }

}
