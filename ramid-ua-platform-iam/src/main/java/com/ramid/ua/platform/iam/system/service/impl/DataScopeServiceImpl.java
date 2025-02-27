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

import com.ramid.framework.commons.entity.Entity;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.commons.security.DataPermission;
import com.ramid.framework.commons.security.DataResourceType;
import com.ramid.framework.db.mybatisplus.datascope.service.DataScopeService;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.iam.system.domain.entity.DataPermissionResource;
import com.ramid.ua.platform.iam.system.domain.entity.Role;
import com.ramid.ua.platform.iam.system.domain.entity.User;
import com.ramid.ua.platform.iam.system.repository.DataPermissionResourceMapper;
import com.ramid.ua.platform.iam.system.repository.RoleMapper;
import com.ramid.ua.platform.iam.system.repository.UserMapper;
import com.ramid.ua.platform.iam.system.service.OrgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.ramid.framework.commons.security.DataScopeType.*;

/**
 * @author levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataScopeServiceImpl implements DataScopeService {
    
    private final RoleMapper roleMapper;
    private final DataPermissionResourceMapper dataPermissionResourceMapper;
    private final UserMapper userMapper;
    private final OrgService orgService;
    
    @Override
    public DataPermission getDataScopeById(Long userId) {
        final User user = Optional.ofNullable(this.userMapper.selectById(userId)).orElseThrow(() -> CheckedException.notFound("用户不存在"));
        return getDataScopeById(userId, user.getOrgId());
    }
    
    /**
     * 开发者可以根据自己企业需求动态扩展数据权限（默认就支撑多维度数据权限，此处以用户维护演示）
     *
     * @param userId 用户ID
     * @param orgId  用户当前机构
     * @return 数据权限
     */
    @Override
    public DataPermission getDataScopeById(Long userId, Long orgId) {
        List<Role> list = roleMapper.findRoleByUserId(userId);
        if (CollectionUtils.isEmpty(list)) {
            return DataPermission.builder().build();
        }
        // 找到 dsType 最大的角色， dsType越大，角色拥有的权限最大
        Role role = list.stream().max(Comparator.comparingInt(item -> item.getScopeType().getType())).get();
        DataPermission permission = DataPermission.builder().scopeType(role.getScopeType()).build();
        List<Long> userIdList = null;
        if (role.getScopeType() == CUSTOMIZE) {
            List<Long> orgIdList = dataPermissionResourceMapper.selectList(Wraps.<DataPermissionResource>lbQ().select(DataPermissionResource::getDataId)
                    .eq(DataPermissionResource::getOwnerId, role.getId())
                    .eq(DataPermissionResource::getOwnerType, DataResourceType.ROLE)
                    .eq(DataPermissionResource::getDataType, DataResourceType.ORG))
                    .stream().map(DataPermissionResource::getDataId).distinct().toList();
            userIdList = this.userMapper.selectList(Wraps.<User>lbQ().select(User::getId).in(User::getOrgId, orgIdList))
                    .stream().map(Entity::getId).toList();
        } else if (role.getScopeType() == THIS_LEVEL) {
            userIdList = this.userMapper.selectList(Wraps.<User>lbQ().select(User::getId).eq(User::getOrgId, orgId))
                    .stream().map(Entity::getId).toList();
        } else if (role.getScopeType() == THIS_LEVEL_CHILDREN) {
            final List<Long> orgIdList = orgService.getFullTreeIdPath(orgId);
            userIdList = this.userMapper.selectList(Wraps.<User>lbQ().select(User::getId).in(User::getOrgId, orgIdList))
                    .stream().map(Entity::getId).toList();
        }
        if (userIdList != null) {
            permission.getDataPermissionMap().put(DataResourceType.USER, userIdList.stream().map(x -> (Object) x).toList());
        }
        // 如果你还有其他维度可以自行扩展
        return permission;
    }
}
