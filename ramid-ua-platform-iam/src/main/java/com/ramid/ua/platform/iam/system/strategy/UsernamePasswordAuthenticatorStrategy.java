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

package com.ramid.ua.platform.iam.system.strategy;

import cn.dev33.satoken.stp.StpUtil;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.db.utils.TenantHelper;
import com.ramid.framework.security.configuration.server.support.AuthenticationPrincipal;
import com.ramid.framework.security.configuration.server.support.AuthenticatorStrategy;
import com.ramid.framework.security.utils.PasswordEncoderHelper;
import com.ramid.ua.platform.iam.system.domain.entity.User;
import com.ramid.ua.platform.iam.system.repository.UserMapper;
import com.ramid.ua.platform.iam.tenant.domain.entity.Tenant;
import com.ramid.ua.platform.iam.tenant.repository.TenantMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 默认登录处理.
 *
 * @author Levin
 **/
@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticatorStrategy implements AuthenticatorStrategy {
    
    @Resource
    private UserMapper userMapper;
    @Resource
    private TenantMapper tenantMapper;
    
    @Override
    public void prepare(final AuthenticationPrincipal principal) {
    }
    
    @Override
    public void authenticate(final AuthenticationPrincipal principal) {
        String username = principal.getUsername();
        String password = principal.getPassword();
        String tenantCode = principal.getTenantCode();
        Tenant tenant = Optional.ofNullable(TenantHelper.executeWithMaster(() -> tenantMapper.selectOne(Tenant::getCode, tenantCode)))
                .orElseThrow(() -> CheckedException.notFound("{0}租户不存在", tenantCode));
        if (!tenant.getStatus()) {
            throw CheckedException.badRequest("租户已被禁用,请联系管理员");
        }
        User user = Optional.ofNullable(TenantHelper.executeWithTenantDb(tenantCode, () -> userMapper.selectUserByTenantId(username, tenant.getId())))
                .orElseThrow(() -> CheckedException.notFound("账户不存在"));
        if (!PasswordEncoderHelper.matches(password, user.getPassword())) {
            throw CheckedException.badRequest("用户名或密码错误");
        }
        StpUtil.login(user.getId(), principal.getClientId());
    }
}
