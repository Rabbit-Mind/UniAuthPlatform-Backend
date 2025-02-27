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

package com.ramid.ua.platform.iam.tenant.service;

import com.ramid.framework.db.dynamic.core.EventAction;
import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.iam.tenant.domain.dto.req.DbSettingSaveReq;
import com.ramid.ua.platform.iam.tenant.domain.dto.resp.DbSettingPageResp;
import com.ramid.ua.platform.iam.tenant.domain.entity.DbSetting;

import java.util.List;

/**
 * @author Levvin
 */
public interface DbSettingService extends SuperService<DbSetting> {
    
    /**
     * 查询所有可用的动态数据源
     *
     * @return 查询结果
     */
    List<DbSettingPageResp> selectTenantDynamicDatasource();
    
    /**
     * ping 数据源
     *
     * @param id id
     */
    void ping(Long id);
    
    /**
     * 添加或者保存动态数据源信息
     *
     * @param req req
     */
    void created(DbSettingSaveReq req);
    
    /**
     * 添加或者保存动态数据源信息
     *
     * @param id  id
     * @param req req
     */
    void edit(Long id, DbSettingSaveReq req);
    
    /**
     * 删除数据源
     *
     * @param id id
     */
    void delete(Long id);
    
    /**
     * 发布数据源事件
     *
     * @param action   动作
     * @param tenantId 租户ID
     */
    void publishEvent(EventAction action, Long tenantId);
}
