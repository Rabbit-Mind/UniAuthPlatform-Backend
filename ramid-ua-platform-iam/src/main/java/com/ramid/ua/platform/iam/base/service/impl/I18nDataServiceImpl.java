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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.commons.security.AuthenticationContext;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.framework.i18n.I18nMessageProvider;
import com.ramid.framework.i18n.domain.I18nMessage;
import com.ramid.ua.platform.iam.base.domain.dto.req.I18nDataSaveReq;
import com.ramid.ua.platform.iam.base.domain.dto.req.I18nPageReq;
import com.ramid.ua.platform.iam.base.domain.dto.resp.I18nDataPageResp;
import com.ramid.ua.platform.iam.base.domain.entity.I18nData;
import com.ramid.ua.platform.iam.base.domain.entity.I18nLocaleMessage;
import com.ramid.ua.platform.iam.base.repository.I18nDataMapper;
import com.ramid.ua.platform.iam.base.repository.I18nLocaleMessageMapper;
import com.ramid.ua.platform.iam.base.service.I18nDataService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author Levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class I18nDataServiceImpl extends SuperServiceImpl<I18nDataMapper, I18nData> implements I18nDataService {
    
    private final I18nDataMapper i18nDataMapper;
    private final I18nLocaleMessageMapper i18nLocaleMessageMapper;
    private final I18nMessageProvider i18nMessageProvider;
    private final AuthenticationContext context;
    
    @PostConstruct
    public void init() {
        List<I18nMessage> messages = i18nDataMapper.loadI18nMessage();
        log.debug("从数据库加载国际化数据 - {}", JSON.toJSONString(messages));
        i18nMessageProvider.loadI18nMessage(messages);
    }
    
    @Override
    public IPage<I18nDataPageResp> pageList(I18nPageReq req) {
        final IPage<I18nDataPageResp> page = this.baseMapper.selectPage(req.buildPage(),
                Wraps.<I18nData>lbQ().like(I18nData::getCode, req.getCode()))
                .convert(x -> BeanUtil.toBean(x, I18nDataPageResp.class));
        final List<Long> i18nIdList = page.getRecords().stream().map(I18nDataPageResp::getId).toList();
        if (CollUtil.isEmpty(i18nIdList)) {
            return page;
        }
        final Map<Long, List<I18nLocaleMessage>> map = this.i18nLocaleMessageMapper.selectList(Wraps.<I18nLocaleMessage>lbQ().in(I18nLocaleMessage::getParentId, i18nIdList))
                .stream().collect(groupingBy(I18nLocaleMessage::getParentId, toList()));
        for (I18nDataPageResp record : page.getRecords()) {
            record.setLanguages(map.get(record.getId()));
        }
        return page;
    }
    
    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void add(I18nDataSaveReq req) {
        final I18nData i18nData = I18nData.builder().code(req.getCode()).remark(req.getRemark()).tenantId(context.tenantId()).build();
        this.baseMapper.insert(i18nData);
        final List<?> list = req.getLanguages()
                .stream()
                .map(lang -> I18nLocaleMessage.builder()
                        .parentId(i18nData.getId()).locale(lang.getLocale()).message(lang.getMessage()).build())
                .toList();
        this.i18nLocaleMessageMapper.insertBatchSomeColumn(list);
    }
    
    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void edit(Long id, I18nDataSaveReq req) {
        this.i18nLocaleMessageMapper.delete(Wraps.<I18nLocaleMessage>lbQ().eq(I18nLocaleMessage::getParentId, id));
        this.baseMapper.updateById(I18nData.builder().id(id).code(req.getCode()).remark(req.getRemark()).build());
        final List<?> list = req.getLanguages()
                .stream()
                .map(lang -> I18nLocaleMessage.builder()
                        .parentId(id).locale(lang.getLocale()).message(lang.getMessage()).build())
                .toList();
        this.i18nLocaleMessageMapper.insertBatchSomeColumn(list);
    }
    
    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        this.i18nLocaleMessageMapper.delete(Wraps.<I18nLocaleMessage>lbQ().eq(I18nLocaleMessage::getParentId, id));
        return this.baseMapper.deleteById(id) > 0;
    }
}
