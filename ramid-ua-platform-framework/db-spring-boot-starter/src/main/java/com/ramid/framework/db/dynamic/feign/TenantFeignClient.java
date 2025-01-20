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

package com.ramid.framework.db.dynamic.feign;

import com.ramid.framework.db.dynamic.core.DynamicDatasourceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Levin
 */
@FeignClient(name = "ramid-ua-platform-authority", dismiss404 = true, fallback = TenantFeignClient.TenantFeignClientFallback.class)
public interface TenantFeignClient {

    /**
     * 查询所有租户数据源
     *
     * @return 查询结果
     */
    @GetMapping(value = "/tenants/databases/active", headers = {"X-Auto-Token=true"})
    List<DynamicDatasourceEvent> selectAll();

    @Component
    @RequiredArgsConstructor
    class TenantFeignClientFallback implements TenantFeignClient {

        @Override
        public List<DynamicDatasourceEvent> selectAll() {
            return null;
        }
    }
}
