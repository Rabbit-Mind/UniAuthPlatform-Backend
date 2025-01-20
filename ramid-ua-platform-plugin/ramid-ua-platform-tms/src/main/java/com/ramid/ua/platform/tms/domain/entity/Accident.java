package com.ramid.ua.platform.tms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ramid.framework.commons.entity.SuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Levin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName(value = "tms_accident")
@Schema(name = "Accident", description = "车辆事故")
public class Accident extends SuperEntity<Long> {

    @Schema(description = "事故编号")
    private String accidentNo;

    @Schema(description = "卡车ID")
    private Long truckId;

    @Schema(description = "车牌号")
    private String plateNo;

    @Schema(description = "驾驶员ID")
    private Long driverId;

    @Schema(description = "驾驶员姓名")
    private String driverName;

    @Schema(description = "事故地点")
    private String address;

    @Schema(description = "责任方")
    private String responsibleParty;

    @Schema(description = "事故金额")
    private BigDecimal accidentAmount;

    @Schema(description = "事故日期")
    private LocalDate accidentDate;

    @Schema(description = "经办人姓名")
    private String agentName;

    @Schema(description = "备注")
    private String remark;

}