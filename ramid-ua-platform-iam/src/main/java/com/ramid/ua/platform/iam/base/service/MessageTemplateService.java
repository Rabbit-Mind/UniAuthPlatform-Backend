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

package com.ramid.ua.platform.iam.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ramid.ua.platform.iam.base.domain.dto.req.MessageTemplatePageReq;
import com.ramid.ua.platform.iam.base.domain.dto.req.MessageTemplateSaveReq;
import com.ramid.ua.platform.iam.base.domain.dto.resp.MessageTemplateDetailResp;
import com.ramid.ua.platform.iam.base.domain.dto.resp.MessageTemplatePageResp;
import com.ramid.ua.platform.iam.base.domain.entity.MessageTemplate;

/**
 * @author Levin
 */
public interface MessageTemplateService extends IService<MessageTemplate> {
    
    /**
     * 分页列表
     *
     * @param req req
     * @return 查询结果
     */
    IPage<MessageTemplatePageResp> pageList(MessageTemplatePageReq req);
    
    /**
     * 创建模板
     *
     * @param req req
     */
    void create(MessageTemplateSaveReq req);
    
    /**
     * 修改模板
     *
     * @param id  id
     * @param req req
     */
    void modify(Long id, MessageTemplateSaveReq req);
    
    /**
     * 模板详情
     *
     * @param id id
     * @return 查询结果
     */
    MessageTemplateDetailResp detail(Long id);
}
