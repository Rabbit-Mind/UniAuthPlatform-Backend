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

package com.ramid.framework.db.mybatisplus.datascope.service;

import com.ramid.framework.commons.security.DataPermission;

/**
 * @author Levin
 */
public interface DataScopeService {

    /**
     * 根据用户编号获取数据权限
     *
     * @param userId 用户ID
     * @return 查询结果
     */
    DataPermission getDataScopeById(Long userId);

    /**
     * 根据用户编号获取数据权限
     *
     * @param userId 用户ID
     * @param orgId  机构ID
     * @return 查询结果
     */
    default DataPermission getDataScopeById(Long userId, Long orgId) {
        throw new RuntimeException("暂未实现");
    }
}
