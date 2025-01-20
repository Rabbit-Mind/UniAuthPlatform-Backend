package com.ramid.ua.platform.wms.stock.service.impl;

import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.ua.platform.wms.stock.domain.entity.StockChange;
import com.ramid.ua.platform.wms.stock.mapper.StockChangeMapper;
import com.ramid.ua.platform.wms.stock.service.StockChangeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存余额变动 服务实现类
 * </p>
 *
 * @author ddCat
 * @since 2024-07-08
 */
@Service
public class StockChangeServiceImpl extends SuperServiceImpl<StockChangeMapper, StockChange> implements StockChangeService {

}
