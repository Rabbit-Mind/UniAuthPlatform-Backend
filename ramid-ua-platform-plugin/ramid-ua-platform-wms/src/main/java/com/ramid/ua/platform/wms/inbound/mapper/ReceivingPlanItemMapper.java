package com.ramid.ua.platform.wms.inbound.mapper;

import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.ua.platform.wms.inbound.domain.entity.ReceivingPlanItem;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 收货计划 Mapper 接口
 * </p>
 *
 * @author ddCat
 * @since 2024-06-24
 */
@Repository
public interface ReceivingPlanItemMapper extends SuperMapper<ReceivingPlanItem> {

    int removeByPlanId(Long planId);

}
