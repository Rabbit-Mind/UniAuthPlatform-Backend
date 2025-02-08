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

package com.ramid.ua.platform.iam.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.ramid.framework.commons.BeanUtilPlus;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.iam.system.domain.dto.req.RegisteredClientReq;
import com.ramid.ua.platform.iam.auth.domain.entity.RegisteredClient;
import com.ramid.ua.platform.iam.system.repository.RegisteredClientMapper;
import com.ramid.ua.platform.iam.auth.service.RegisteredClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Levin
 */
@Service
@RequiredArgsConstructor
public class RegisteredClientServiceImpl extends SuperServiceImpl<RegisteredClientMapper, RegisteredClient> implements RegisteredClientService {
    
    @Override
    public void create(RegisteredClientReq req) {
        baseMapper.existsCallback(RegisteredClient::getClientId, req.getClientId(), () -> CheckedException.badRequest("终端已存在,注册失败"));
        var bean = BeanUtilPlus.toBean(req, RegisteredClient.class);

        if (CollUtil.isNotEmpty(req.getGrantTypes())) {
            bean.setGrantTypes(CollUtil.join(req.getGrantTypes(), ","));
        }
        if (Objects.nonNull(req.getClientSettings())) {
            bean.setClientSettings(JSON.toJSONString(req.getClientSettings()));
        }
        if (Objects.nonNull(req.getTokenSettings())) {
            bean.setTokenSettings(JSON.toJSONString(req.getTokenSettings()));
        }
        this.baseMapper.insert(bean);
    }
    
    @Override
    public void modify(Long id, RegisteredClientReq req) {
        Long count = baseMapper.selectCount(Wraps.<RegisteredClient>lbQ()
                .ne(RegisteredClient::getId, id).eq(RegisteredClient::getClientId, req.getClientId()));
        if (count != null && count > 0) {
            throw CheckedException.badRequest("终端已存在,修改失败");
        }
        var bean = BeanUtilPlus.toBean(id, req, RegisteredClient.class);
        if (CollUtil.isNotEmpty(req.getGrantTypes())) {
            bean.setGrantTypes(CollUtil.join(req.getGrantTypes(), ","));
        }
        if (Objects.nonNull(req.getClientSettings())) {
            bean.setClientSettings(JSON.toJSONString(req.getClientSettings()));
        }
        if (Objects.nonNull(req.getTokenSettings())) {
            bean.setTokenSettings(JSON.toJSONString(req.getTokenSettings()));
        }
        this.baseMapper.updateById(bean);
    }
    
    @Override
    public void deleteById(String id) {
        this.baseMapper.removeById(id);
    }
}
