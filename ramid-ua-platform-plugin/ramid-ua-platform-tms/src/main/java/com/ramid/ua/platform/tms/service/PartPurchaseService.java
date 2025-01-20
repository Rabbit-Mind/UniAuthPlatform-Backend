package com.ramid.ua.platform.tms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.tms.domain.entity.PartPurchase;
import com.ramid.ua.platform.tms.domain.req.PartPurchasePageReq;
import com.ramid.ua.platform.tms.domain.req.PartPurchaseSaveReq;
import com.ramid.ua.platform.tms.domain.resp.PartPurchasePageResp;

/**
 * @author Levin
 */
public interface PartPurchaseService extends SuperService<PartPurchase> {

    /**
     * 分页查询
     *
     * @param req req
     * @return 查询结果
     */
    IPage<PartPurchasePageResp> pageList(PartPurchasePageReq req);

    /**
     * 创建配件采购信息
     *
     * @param req req
     */
    void created(PartPurchaseSaveReq req);

    /**
     * 编辑配件采购信息
     *
     * @param id  id
     * @param req req
     */
    void edit(Long id, PartPurchaseSaveReq req);
}
