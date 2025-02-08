package com.ramid.ua.platform.iam.auth.strategy;

import com.alibaba.fastjson.JSON;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.db.utils.TenantHelper;
import com.ramid.ua.platform.iam.auth.support.AuthenticationPrincipal;
import com.ramid.ua.platform.iam.auth.support.AuthenticatorStrategy;
import com.ramid.ua.platform.iam.auth.support.domain.UserTenantAuthentication;
import com.ramid.ua.platform.iam.system.domain.entity.User;
import com.ramid.ua.platform.iam.auth.domain.entity.UserThirdAccount;
import com.ramid.ua.platform.iam.system.domain.enums.ThirdAuthType;
import com.ramid.ua.platform.iam.system.repository.ThirdAccountMapper;
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
 * GITEE 登录处理.
 **/
@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class GiteeAuthenticatorStrategy implements AuthenticatorStrategy {

    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ThirdAccountMapper thirdAccountMapper;

    @Override
    public void prepare(final AuthenticationPrincipal principal) {
    }

    @Override
    public UserTenantAuthentication authenticate(final AuthenticationPrincipal principal) {
        log.warn("暂未实现授权绑定逻辑,比如授权后默认第一种授权类型生成 t_user 记录");
        String username = principal.getUsername();
        String tenantCode = principal.getTenantCode();
        Tenant tenant = Optional.ofNullable(TenantHelper.executeWithMaster(() -> tenantMapper.selectOne(Tenant::getCode, tenantCode)))
                .orElseThrow(() -> CheckedException.notFound("{0}租户不存在", tenantCode));
        if (!tenant.getStatus()) {
            throw CheckedException.badRequest("租户已被禁用,请联系管理员");
        }
        ThirdAuthType type = ThirdAuthType.of(principal.getLoginType());
        UserThirdAccount thirdAccount = Optional.ofNullable(thirdAccountMapper.selectOne(UserThirdAccount::getAccountId, username, UserThirdAccount::getType, type))
                .orElseThrow(() -> CheckedException.notFound("用户未授权"));
        log.debug("third-account => {}", JSON.toJSONString(thirdAccount));
        User user = Optional.ofNullable(TenantHelper.executeWithTenantDb(tenantCode, () -> userMapper.selectUserByTenantId("admin", tenant.getId())))
                .orElseThrow(() -> CheckedException.notFound("账户不存在"));
        return UserTenantAuthentication.builder().user(user).tenant(tenant).build();
    }

    @Override
    public String loginType() {
        return ThirdAuthType.GITEE.getType();
    }
}
