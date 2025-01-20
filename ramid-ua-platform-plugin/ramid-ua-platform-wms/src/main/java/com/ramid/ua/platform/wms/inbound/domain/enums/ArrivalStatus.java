package com.ramid.ua.platform.wms.inbound.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ramid.framework.commons.entity.DictEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 到货状态(已到货、延迟、未到货)
 *
 * @author ddCat
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonFormat
@Schema(description = "ArrivalStatus")
public enum ArrivalStatus implements DictEnum<String> {

    ARRIVED("ARRIVED", "已到货"),
    DELAY("DELAY", "延迟"),
    NOT_DELIVERED("NOT_DELIVERED", "未到货"),
    ;
    @EnumValue
    @JsonValue
    private String status;

    @Schema(description = "描述")
    private String desc;

    @JsonCreator
    public static ArrivalStatus of(String status) {
        if (status == null) {
            return null;
        }
        for (ArrivalStatus info : values()) {
            if (info.status.equals(status)) {
                return info;
            }
        }
        return null;
    }


    @Override
    public String getValue() {
        return this.status;
    }

    @Override
    public String toString() {
        return String.valueOf(status);
    }
}
