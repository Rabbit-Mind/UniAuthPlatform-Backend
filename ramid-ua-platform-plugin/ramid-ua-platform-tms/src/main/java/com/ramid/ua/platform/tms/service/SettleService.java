package com.ramid.ua.platform.tms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.ua.platform.tms.domain.entity.BmsSettleDetail;
import com.ramid.ua.platform.tms.domain.req.BmsDetailCalcReq;
import com.ramid.ua.platform.tms.domain.req.SettleDetailPageReq;
import com.ramid.ua.platform.tms.domain.req.SettleDetailSaveReq;
import com.ramid.ua.platform.tms.domain.resp.SettleDetailPageResp;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 费用信息
 *
 * </p>
 *
 * @author Levin
 * @since 2023-09-02
 */
public interface SettleService extends SuperService<BmsSettleDetail> {

    /**
     * 费用列表
     *
     * @param req req
     * @return 查询结果
     */
    IPage<SettleDetailPageResp> pageList(SettleDetailPageReq req);

    /**
     * 创建费用
     *
     * @param req req
     * @return id
     */
    SettleDetailPageResp createFee(SettleDetailSaveReq req);

    /**
     * 取消费用
     *
     * @param id id
     * @return id
     */
    SettleDetailPageResp cancelFee(Long id);

    /**
     * 修改费用
     *
     * @param id id
     * @param req req
     * @return id
     */
    SettleDetailPageResp updateFee(Long id, SettleDetailSaveReq req);

    /**
     * 计算费用
     *
     * @param req req
     * @return ids
     */
    List<Long> calc(BmsDetailCalcReq req);
}
