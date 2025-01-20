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

package com.ramid.ua.platform.iam.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ramid.framework.commons.BeanUtilPlus;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.ua.platform.iam.base.domain.dto.req.MessageChannelSaveReq;
import com.ramid.ua.platform.iam.base.domain.dto.resp.MessageChannelDetailResp;
import com.ramid.ua.platform.iam.base.domain.entity.MessageChannel;
import com.ramid.ua.platform.iam.base.repository.MessageChannelMapper;
import com.ramid.ua.platform.iam.base.service.MessageChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageChannelServiceImpl extends SuperServiceImpl<MessageChannelMapper, MessageChannel> implements MessageChannelService {
    
    @Override
    public void setting(MessageChannelSaveReq req) {
        MessageChannel bean = BeanUtilPlus.toBean(req, MessageChannel.class);
        if (req.getId() == null) {
            this.baseMapper.insert(bean);
        } else {
            this.baseMapper.updateById(bean);
        }
    }
    
    @Override
    public MessageChannelDetailResp detail(String type) {
        MessageChannel channel = this.baseMapper.selectOne(MessageChannel::getType, type);
        MessageChannelDetailResp bean = BeanUtilPlus.toBeanIgnoreError(channel, MessageChannelDetailResp.class);
        if (StrUtil.isNotBlank(channel.getSetting())) {
            bean.setSetting(JSONObject.parseObject(channel.getSetting()));
        }
        return bean;
    }
}
