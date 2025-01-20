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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.framework.security.domain.UserInfoDetails;
import com.ramid.ua.platform.iam.base.domain.dto.req.ChangeUserInfoReq;
import com.ramid.ua.platform.iam.system.domain.dto.req.UserOnlinePageReq;
import com.ramid.ua.platform.iam.system.domain.dto.req.UserPageReq;
import com.ramid.ua.platform.iam.system.domain.dto.req.UserSaveReq;
import com.ramid.ua.platform.iam.system.domain.dto.req.UserUpdateReq;
import com.ramid.ua.platform.iam.system.domain.dto.resp.UserResp;
import com.ramid.ua.platform.iam.system.domain.entity.User;

/**
 * <p>
 * 业务接口
 * 账号
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
public interface UserService extends SuperService<User> {
    
    /**
     * 添加用户
     *
     * @param req 用户信息
     */
    void create(UserSaveReq req);
    
    /**
     * 修改用户信息
     *
     * @param id  id
     * @param req req
     */
    void modify(Long id, UserUpdateReq req);
    
    /**
     * 数据权限 分页
     *
     * @param req req
     * @return 查询结果
     */
    IPage<UserResp> pageList(UserPageReq req);
    
    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param orgPassword 原始密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String orgPassword, String newPassword);
    
    /**
     * 根据ID删除用户
     *
     * @param id id
     */
    void deleteById(Long id);
    
    /**
     * 修改用户信息
     *
     * @param req req
     */
    void changeInfo(ChangeUserInfoReq req);
    
    /**
     * 重置密码
     *
     * @param id ID
     */
    void resetPassword(Long id);
    
    /**
     * 用户信息
     *
     * @param userId 用户信息
     * @return 用户信息
     */
    UserInfoDetails userinfo(Long userId);
    
    /**
     * 查询在线用户列表
     *
     * @param req req
     * @return 查询结果
     */
    IPage<Object> userOnlineList(UserOnlinePageReq req);
    
}
