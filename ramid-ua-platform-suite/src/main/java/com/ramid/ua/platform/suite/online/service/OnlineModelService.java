package com.ramid.ua.platform.suite.online.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.suite.online.domain.entity.OnlineModel;
import com.ramid.ua.platform.suite.online.domain.req.OnlineFormDesignSaveReq;
import com.ramid.ua.platform.suite.online.domain.req.OnlineFormDesignerPageReq;
import com.ramid.ua.platform.suite.online.domain.req.OnlineFormDesignerSaveReq;
import com.ramid.ua.platform.suite.online.domain.resp.OnlineFormDesignerDetailResp;
import com.ramid.ua.platform.suite.online.domain.resp.OnlineFormDesignerPageResp;

public interface OnlineModelService extends SuperService<OnlineModel> {
    
    /**
     * 分页查询
     *
     * @param req req
     * @return 查询结果
     */
    IPage<OnlineFormDesignerPageResp> pageList(OnlineFormDesignerPageReq req);
    
    /**
     * 创建
     *
     * @param req req
     */
    void created(OnlineFormDesignerSaveReq req);
    
    /**
     * 修改
     *
     * @param id  id
     * @param req req
     */
    void modify(Long id, OnlineFormDesignerSaveReq req);
    
    /**
     * 详情
     *
     * @param id id
     * @return 查询结果
     */
    OnlineFormDesignerDetailResp detail(Long id);
    
    /**
     * 详情
     *
     * @param definitionKey definitionKey
     * @return 查询结果
     */
    OnlineFormDesignerDetailResp detail(String definitionKey);
    
    /**
     * 表单设计
     *
     * @param id  id
     * @param req req
     */
    void formDesign(Long id, OnlineFormDesignSaveReq req);
    
}
