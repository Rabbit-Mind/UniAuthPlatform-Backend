package com.ramid.ua.platform.tms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.commons.security.AuthenticationContext;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.framework.redis.plus.sequence.RedisSequenceHelper;
import com.ramid.ua.platform.tms.domain.entity.PartPurchase;
import com.ramid.ua.platform.tms.domain.enums.TmsSequence;
import com.ramid.ua.platform.tms.domain.req.PartPurchasePageReq;
import com.ramid.ua.platform.tms.domain.req.PartPurchaseSaveReq;
import com.ramid.ua.platform.tms.domain.resp.PartPurchasePageResp;
import com.ramid.ua.platform.tms.mapper.PartPurchaseMapper;
import com.ramid.ua.platform.tms.service.PartPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Levin
 */
@Service
@RequiredArgsConstructor
public class PartPurchaseServiceImpl extends SuperServiceImpl<PartPurchaseMapper, PartPurchase> implements PartPurchaseService {


    private final RedisSequenceHelper sequenceHelper;
    private final AuthenticationContext authenticationContext;

    @Override
    public IPage<PartPurchasePageResp> pageList(PartPurchasePageReq req) {
        return this.baseMapper.selectPage(req.buildPage(), Wraps.<PartPurchase>lbQ()
                        .like(PartPurchase::getPurchaseNo, req.getPurchaseNo())
                        .like(PartPurchase::getPartName, req.getPartName()))
                .convert(x -> BeanUtil.toBean(x, PartPurchasePageResp.class));
    }

    @Override
    public void created(PartPurchaseSaveReq req) {
        final PartPurchase bean = BeanUtil.toBean(req, PartPurchase.class);
        bean.setPurchaseNo(sequenceHelper.generate(TmsSequence.PURCHASE_NO, authenticationContext.tenantId()));
        this.baseMapper.insert(bean);
    }

    @Override
    public void edit(Long id, PartPurchaseSaveReq req) {
        Optional.ofNullable(this.baseMapper.selectById(id)).orElseThrow(() -> CheckedException.notFound("配件采购单信息不存在"));
        final PartPurchase bean = BeanUtil.toBean(req, PartPurchase.class);
        bean.setId(id);
        this.baseMapper.updateById(bean);
    }
}
