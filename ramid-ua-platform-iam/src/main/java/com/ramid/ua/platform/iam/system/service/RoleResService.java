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

package com.ramid.ua.platform.iam.system.service;

import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.iam.system.domain.dto.req.RoleResSaveReq;
import com.ramid.ua.platform.iam.system.domain.dto.req.UserRoleSaveReq;
import com.ramid.ua.platform.iam.system.domain.entity.RoleRes;

/**
 * <p>
 * 业务接口
 * 角色的资源
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
public interface RoleResService extends SuperService<RoleRes> {
    
    /**
     * 给用户分配角色
     *
     * @param req req
     * @return 保存结果
     */
    boolean assignUser(UserRoleSaveReq req);
    
    /**
     * 给角色重新分配 权限（资源/菜单）
     *
     * @param req req
     */
    void assignResource(RoleResSaveReq req);
}
