package com.ramid.ua.platform.wms.basic.service;

import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.wms.basic.domain.entity.Workbench;
import com.ramid.ua.platform.wms.basic.domain.req.WorkbenchSaveReq;

/**
 * <p>
 * 工作台表 服务类
 * </p>
 *
 * @author ddCat
 * @since 2024-06-17
 */
public interface WorkbenchService extends SuperService<Workbench> {

    /**
     * 添加工作台
     *
     * @param req req
     */
    void create(WorkbenchSaveReq req);

    /**
     * 修改工作台
     *
     * @param id  id
     * @param req req
     */
    void modify(Long id, WorkbenchSaveReq req);

}
