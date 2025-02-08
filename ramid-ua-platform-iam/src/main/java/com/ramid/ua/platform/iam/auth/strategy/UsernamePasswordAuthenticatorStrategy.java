package com.ramid.ua.platform.iam.auth.strategy;

import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.db.utils.TenantHelper;
import com.ramid.framework.security.utils.PasswordEncoderHelper;
import com.ramid.ua.platform.iam.auth.support.AuthenticationPrincipal;
import com.ramid.ua.platform.iam.auth.support.AuthenticatorStrategy;
import com.ramid.ua.platform.iam.auth.support.domain.UserTenantAuthentication;
import com.ramid.ua.platform.iam.system.domain.entity.User;
import com.ramid.ua.platform.iam.system.repository.UserMapper;
import com.ramid.ua.platform.iam.tenant.domain.entity.Tenant;
import com.ramid.ua.platform.iam.tenant.repository.TenantMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 默认登录处理.
 **/
@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticatorStrategy implements AuthenticatorStrategy {
    
    @Resource
    private UserMapper userMapper;
    @Resource
    private TenantMapper tenantMapper;
    
    @Override
    public void prepare(final AuthenticationPrincipal principal) {
    }
    
    @Override
    public UserTenantAuthentication authenticate(final AuthenticationPrincipal principal) {
        String username = principal.getUsername();
        String password = principal.getPassword();
        String tenantCode = principal.getTenantCode();
        Tenant tenant = Optional.ofNullable(TenantHelper.executeWithMaster(() -> tenantMapper.selectOne(Tenant::getCode, tenantCode)))
                .orElseThrow(() -> CheckedException.notFound("{0}租户不存在", tenantCode));
        if (!tenant.getStatus()) {
            throw CheckedException.badRequest("租户已被禁用,请联系管理员");
        }
        User user = Optional.ofNullable(TenantHelper.executeWithTenantDb(tenantCode, () -> userMapper.selectUserByTenantId(username, tenant.getId())))
                .orElseThrow(() -> CheckedException.notFound("账户不存在"));
        if (!PasswordEncoderHelper.matches(password, user.getPassword())) {
            throw CheckedException.badRequest("用户名或密码错误");
        }
        return UserTenantAuthentication.builder().user(user).tenant(tenant).build();
    }
}
