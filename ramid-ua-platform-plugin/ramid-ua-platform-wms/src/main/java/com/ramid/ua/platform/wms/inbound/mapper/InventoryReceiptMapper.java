package com.ramid.ua.platform.wms.inbound.mapper;

import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.ua.platform.wms.inbound.domain.entity.InventoryReceipt;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 入库单 Mapper 接口
 * </p>
 *
 * @author ddCat
 * @since 2024-06-27
 */
@Repository
public interface InventoryReceiptMapper extends SuperMapper<InventoryReceipt> {

}
