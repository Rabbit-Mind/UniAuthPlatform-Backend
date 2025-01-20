package com.ramid.ua.platform.tms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.tms.domain.entity.Driver;
import com.ramid.ua.platform.tms.domain.req.DriverApprovalReq;
import com.ramid.ua.platform.tms.domain.req.DriverPageReq;
import com.ramid.ua.platform.tms.domain.req.DriverSaveReq;
import com.ramid.ua.platform.tms.domain.resp.DriverPageResp;

/**
 * @author Levin
 */
public interface DriverService extends SuperService<Driver> {

    /**
     * 司机列表
     *
     * @param req req
     * @return 查询结果
     */
    IPage<DriverPageResp> pageList(DriverPageReq req);

    /**
     * 创建司机
     *
     * @param req req
     */
    void created(DriverSaveReq req);

    /**
     * 编辑司机信息
     *
     * @param id  id
     * @param req req
     */
    void edit(Long id, DriverSaveReq req);

    /**
     * 审批
     *
     * @param id  id
     * @param req 审批信息
     */
    void approval(Long id, DriverApprovalReq req);


    /**
     * 启用/停用
     *
     * @param id      id
     * @param enabled 启用/停用状态
     */
    void enabled(Long id, Boolean enabled);

}
