package com.ramid.ua.platform.tms.domain.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Levin
 */
@Data
@Schema(name = "OrderEventResp")
public class OrderEventResp {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "平台单号")
    private String systemNo;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区县")
    private String district;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "记录时间")
    private LocalDateTime recordTime;

    @Schema(description = "节点状态ID")
    private Integer eventType;

    @Schema(description = "操作人员ID")
    private Integer operatorId;

    @Schema(description = "节点备注")
    private String remark;

    @Schema(description = "总数量")
    private Integer quantity;

    @Schema(description = "毛重")
    private BigDecimal grossWeight;

    @Schema(description = "体积")
    private BigDecimal volume;

    @Schema(description = "订单货物ID")
    private Long skuId;

    @Schema(description = "站点ID")
    private Integer stationId;

    @Schema(description = "任务ID")
    private Long taskId;

    @Schema(description = "司机ID")
    private Integer driverId;

    @Schema(description = "司机姓名")
    private String driverName;

    @Schema(description = "司机手机号")
    private String driverMobile;

    @Schema(description = "车辆id")
    private Integer truckId;

    @Schema(description = "车牌号")
    private String plateNo;

    @Schema(description = "数据来源")
    private Integer sourceId;

}