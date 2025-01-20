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
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.iam.base.domain.dto.resp.SiteSettingDetailResp;
import com.ramid.ua.platform.iam.base.service.PlatService;
import com.ramid.ua.platform.iam.tenant.domain.entity.Tenant;
import com.ramid.ua.platform.iam.tenant.domain.entity.TenantSetting;
import com.ramid.ua.platform.iam.tenant.repository.TenantMapper;
import com.ramid.ua.platform.iam.tenant.repository.TenantSettingMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatServiceImpl implements PlatService {
    
    private final TenantMapper tenantMapper;
    private final TenantSettingMapper tenantSettingMapper;
    
    @Override
    public SiteSettingDetailResp siteSetting(HttpServletRequest request) {
        String httpUrl = request.getHeader("origin");
        if (StrUtil.isBlank(httpUrl)) {
            httpUrl = request.getHeader("referer");
        }
        log.debug("http header origin => {}", httpUrl);
        // 该部分配置后续从数据库读取
        SiteSettingDetailResp detail = new SiteSettingDetailResp();
        if (StrUtil.isBlank(httpUrl)) {
            detail.setTitle("Rabbit Mind 管理平台");
            return detail;
        }
        String siteUrl = URLUtil.url(httpUrl).getHost();
        log.debug("site url => {}", siteUrl);
        TenantSetting setting = this.tenantSettingMapper.selectOne(Wraps.<TenantSetting>lbQ()
                .eq(TenantSetting::getSiteUrl, siteUrl).last(" limit 1"));
        if (setting == null) {
            detail.setTitle("Rabbit Mind 租户平台");
            return detail;
        }
        Tenant tenant = this.tenantMapper.selectById(setting.getTenantId());
        if (tenant != null) {
            detail.setTenantCode(tenant.getCode());
        }
        detail.setTitle(setting.getSiteTitle());
        detail.setSubTitle(setting.getSiteSubTitle());
        detail.setLogo(setting.getSiteLogo());
        JSONObject ext = new JSONObject();
        ext.put("showCodeLogin", false);
        ext.put("showForgetPassword", true);
        ext.put("showQrcodeLogin", false);
        ext.put("showRegister", false);
        ext.put("showRememberMe", false);
        ext.put("showThirdPartyLogin", false);
        detail.setExt(ext);
        return detail;
    }
}
