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

package com.ramid.ua.platform.iam.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.iam.system.domain.dto.resp.UserRoleResp;
import com.ramid.ua.platform.iam.system.domain.entity.User;
import com.ramid.ua.platform.iam.system.domain.entity.UserRole;
import com.ramid.ua.platform.iam.system.repository.UserMapper;
import com.ramid.ua.platform.iam.system.repository.UserRoleMapper;
import com.ramid.ua.platform.iam.system.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 角色分配
 * 账号角色绑定
 * </p>
 *
 * @author Levin
 * @since 2019-07-03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends SuperServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    
    private final UserMapper userMapper;
    
    @Override
    public UserRoleResp findUserByRoleId(Long roleId) {
        final List<Long> userIdList = super.list(Wraps.<UserRole>lbQ().eq(UserRole::getRoleId, roleId))
                .stream().map(UserRole::getUserId).distinct().collect(Collectors.toList());
        final List<User> users = userMapper.selectList(Wraps.lbQ());
        if (CollectionUtil.isEmpty(users)) {
            return null;
        }
        final List<UserRoleResp.UserRoleDetail> userRoleDetails = users.stream().map(user -> UserRoleResp.UserRoleDetail.builder()
                .id(user.getId()).nickName(user.getNickName()).username(user.getUsername()).build()).collect(Collectors.toList());
        return UserRoleResp.builder().userRoleDetails(userRoleDetails).originTargetKeys(userIdList).build();
    }
}
