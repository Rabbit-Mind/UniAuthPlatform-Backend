package com.ramid.ua.platform.tms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.tms.domain.entity.Accident;
import com.ramid.ua.platform.tms.domain.req.AccidentPageReq;
import com.ramid.ua.platform.tms.domain.req.AccidentSaveReq;
import com.ramid.ua.platform.tms.domain.resp.AccidentPageResp;

/**
 * @author Levin
 */
public interface AccidentService extends SuperService<Accident> {



    /**
     * 分页查询
     *
     * @param req req
     * @return 查询结果
     */
    IPage<AccidentPageResp> pageList(AccidentPageReq req);

    /**
     * 创建车辆事故信息
     *
     * @param req req
     */
    void created(AccidentSaveReq req);

    /**
     * 编辑车辆事故信息
     *
     * @param id  id
     * @param req req
     */
    void edit(Long id, AccidentSaveReq req);


}
