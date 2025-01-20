package com.ramid.ua.platform.wms.matedata.service;

import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.wms.matedata.domain.entity.Brand;
import com.ramid.ua.platform.wms.matedata.domain.req.BrandSaveReq;

/**
 * <p>
 * 品牌 服务类
 * </p>
 *
 * @author ddCat
 * @since 2024-06-18
 */
public interface BrandService extends SuperService<Brand> {


    /**
     * 添加品牌
     *
     * @param req 品牌信息
     */
    void create(BrandSaveReq req);

    /**
     * 修改品牌
     *
     * @param id  品牌ID
     * @param req 品牌信息
     */
    void modify(Long id, BrandSaveReq req);
}
