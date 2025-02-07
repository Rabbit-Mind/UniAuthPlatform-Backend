package com.ramid.ua.platform.suite.online.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.commons.BeanUtilPlus;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.ua.platform.suite.online.domain.entity.OnlineFormData;
import com.ramid.ua.platform.suite.online.domain.req.OnlineFormDataSaveReq;
import com.ramid.ua.platform.suite.online.domain.req.OnlineFormDesignerPageReq;
import com.ramid.ua.platform.suite.online.repository.OnlineFormDataMapper;
import com.ramid.ua.platform.suite.online.service.OnlineFormDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineFormDataServiceImpl extends SuperServiceImpl<OnlineFormDataMapper, OnlineFormData> implements OnlineFormDataService {
    
    @Override
    public IPage<JSONObject> pageList(OnlineFormDesignerPageReq req) {
        return this.baseMapper.pageList(req.buildPage(), req).convert(x -> new JSONObject() {
            
            {
                put("id", x.getId());
                put("definitionKey", x.getDefinitionKey());
                put("tenantId", x.getTenantId());
                put("createdName", x.getCreatedName());
                put("createdTime", x.getCreatedTime());
                putAll(x.getFormData());
            }
        });
    }
    
    @Override
    public void created(OnlineFormDataSaveReq req) {
        var bean = BeanUtilPlus.toBean(req, OnlineFormData.class);
        this.baseMapper.insert(bean);
    }
    
    @Override
    public void modify(Long id, OnlineFormDataSaveReq req) {
        var bean = BeanUtilPlus.toBean(id, req, OnlineFormData.class);
        this.baseMapper.updateById(bean);
    }
}
