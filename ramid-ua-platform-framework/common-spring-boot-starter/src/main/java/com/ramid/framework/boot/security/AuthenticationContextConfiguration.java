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

package com.ramid.framework.boot.security;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ramid.framework.commons.security.AuthenticationContext;
import com.ramid.framework.commons.security.DataPermission;
import com.ramid.framework.commons.threadlocal.ThreadLocalHolder;
import com.ramid.framework.security.configuration.SecurityExtProperties;
import com.ramid.framework.security.domain.UserInfoDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

/**
 * @author Levin
 */
@Configuration
@RequiredArgsConstructor
public class AuthenticationContextConfiguration {

    private static final String USER_INFO = "USER_INFO_KEY";
    private final SaTokenDao saTokenDao;

    @Bean
    public AuthenticationContext authenticationContext(SecurityExtProperties properties) {
        return new AuthenticationContext() {
            @Override
            public UserInfoDetails getContext() {
                String key = String.format(properties.getServer().getInfoKeyPrefix(), StpUtil.getTokenValue());
                return (UserInfoDetails) ThreadLocalHolder.get(USER_INFO,
                        () -> JSONObject.parseObject((String) saTokenDao.getObject(key), UserInfoDetails.class));
            }

            @Override
            public Long tenantId() {
                return Optional.ofNullable(getContext()).map(UserInfoDetails::getTenantId).orElse(null);
            }

            @Override
            public String tenantName() {
                return Optional.ofNullable(getContext()).map(UserInfoDetails::getTenantName).orElse(null);
            }

            @Override
            public String tenantCode() {
                return Optional.ofNullable(getContext()).map(UserInfoDetails::getTenantCode).orElse(null);
            }

            @Override
            public Long userId() {
                return Optional.ofNullable(getContext()).map(UserInfoDetails::getUserId).orElse(null);
            }

            @Override
            public String nickName() {
                return Optional.ofNullable(getContext()).map(UserInfoDetails::getNickName).orElse(null);
            }

            @Override
            public List<String> funcPermissionList() {
                return (List<String>) getContext().getFuncPermissions();
            }

            @Override
            public List<String> rolePermissionList() {
                return (List<String>) getContext().getRoles();
            }

            @Override
            public DataPermission dataPermission() {
                return getContext().getDataPermission();
            }

            @Override
            public boolean anonymous() {
                try {
                    return !StpUtil.isLogin();
                } catch (Exception e) {
                    return true;
                }
            }
        };
    }

}
