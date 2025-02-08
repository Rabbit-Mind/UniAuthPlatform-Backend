package com.ramid.ua.platform.iam.auth.domain.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Levin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdAuthResp {

    @Schema(description = "授权地址")
    private String authorizeUrl;

    @Schema(description = "状态码")
    private String state;
}
