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

import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.iam.base.domain.dto.req.DictItemSaveReq;
import com.ramid.ua.platform.iam.tenant.domain.entity.TenantDictItem;

/**
 * <p>
 * 业务接口
 * 字典项
 * </p>
 *
 * @author Levin
 */
public interface TenantDictItemService extends SuperService<TenantDictItem> {
    
    /**
     * 添加字典项
     *
     * @param req req
     */
    void create(DictItemSaveReq req);
    
    /**
     * 修改字典项
     *
     * @param itemId 字典项ID
     * @param req    req
     */
    void modify(Long itemId, DictItemSaveReq req);
    
    /**
     * 删除字典
     *
     * @param id id
     */
    void delete(Long id);
}
