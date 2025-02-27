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

package com.ramid.ua.platform.iam.system.repository;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ramid.framework.commons.entity.Entity;
import com.ramid.framework.db.mybatisplus.datascope.annotation.DataColumn;
import com.ramid.framework.db.mybatisplus.datascope.annotation.DataScope;
import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.ua.platform.iam.system.domain.dto.resp.UserResp;
import com.ramid.ua.platform.iam.system.domain.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Levin
 */

@Repository
public interface UserMapper extends SuperMapper<User> {
    
    /**
     * 分页查询用户
     *
     * @param page    page
     * @param wrapper wrapper
     * @return 查询结果
     */
    @DataScope(columns = @DataColumn(name = Entity.CREATE_USER_COLUMN))
    IPage<UserResp> findPage(@Param("page") IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);
    
    /**
     * 查询用户
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return 查询结果
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from t_user where username = #{username} and tenant_id = #{tenantId}")
    User selectUserByTenantId(@Param("username") String username, @Param("tenantId") Long tenantId);
    
    /**
     * 带数据权限用户列表
     *
     * @return 用户
     */
    @DataScope(columns = @DataColumn(name = Entity.CREATE_USER_COLUMN))
    List<User> list();
    
    /**
     * 删除指定租户用户数据
     *
     * @param tenantId 租户ID
     */
    @InterceptorIgnore(tenantLine = "true")
    @Delete("delete from t_user where tenant_id = #{tenantId}")
    void deleteByTenantId(@Param("tenantId") Long tenantId);
    
    /**
     * 查询指定租户用户信息
     *
     * @param tenantId 租户ID
     * @return 查询结果
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from t_user where tenant_id = #{tenantId}")
    List<User> selectByTenantId(@Param("tenantId") Long tenantId);
    
    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 查询结果
     */
    List<Long> selectResByUserId(@Param("userId") Long userId);
}
