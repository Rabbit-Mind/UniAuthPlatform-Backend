package com.ramid.ua.platform.wms.basic.service;

import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.wms.basic.domain.entity.StorageArea;
import com.ramid.ua.platform.wms.basic.domain.req.StorageAreaSaveReq;

/**
 * <p>
 * 库区表 服务类
 * </p>
 *
 * @author ddCat
 * @since 2024-06-17
 */
public interface StorageAreaService extends SuperService<StorageArea> {


    /**
     * 添加库区信息
     *
     * @param req req
     */
    void create(StorageAreaSaveReq req);

    /**
     * 修改库区信息
     *
     * @param id  库区id
     * @param req 库区信息
     */
    void modify(Long id, StorageAreaSaveReq req);

}
