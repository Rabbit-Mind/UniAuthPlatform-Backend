package com.ramid.ua.platform.iam.auth.support;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationPrincipal {
    
    @NotBlank(message = "登录类型不能为空")
    @Schema(description = "登录类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String loginType;
    
    @NotBlank(message = "登录账号不能为空")
    @Schema(description = "登录账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    
    @NotBlank(message = "登录密码不能为空")
    @Schema(description = "登录密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    
    @Schema(description = "租户编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tenantCode;
    
    @Schema(description = "客户端ID", example = "pc-web")
    private String clientId;
    
    @Schema(description = "客户端密钥", example = "pc-web")
    private String clientSecret;
    
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    @Schema(description = "HTTP 请求", requiredMode = Schema.RequiredMode.REQUIRED)
    private HttpServletRequest request;
    
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    public static final String PRINCIPAL = "principal";

    public static final String PRINCIPAL_TYPE = "principalType";

    public static final String AUTHENTICATION = "authentication";
}