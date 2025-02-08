package com.ramid.ua.platform.iam.auth.strategy;

import com.ramid.framework.commons.entity.Result;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.ua.platform.iam.auth.support.AuthenticationPrincipal;
import com.ramid.ua.platform.iam.system.service.CaptchaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 集成验证码认证.
 **/
@Component
public class VerificationCodeAuthenticatorStrategy extends UsernamePasswordAuthenticatorStrategy {
    
    private static final String VERIFICATION_CODE_AUTH_TYPE = "vc";
    
    @Resource
    private CaptchaService captchaService;
    
    @Override
    public void prepare(final AuthenticationPrincipal principal) {
        String vcToken = principal.getParameter("vc_token");
        String vcCode = principal.getParameter("vc_code");
        // 验证验证码
        final Result<Boolean> result = captchaService.valid(vcToken, vcCode);
        if (!result.isSuccessful()) {
            throw CheckedException.badRequest(result.getMessage());
        }
        super.prepare(principal);
    }
    
    @Override
    public String loginType() {
        return VERIFICATION_CODE_AUTH_TYPE;
    }
}
