package com.ramid.ua.platform.iam.auth.support.domain;

import com.ramid.ua.platform.iam.system.domain.entity.User;
import com.ramid.ua.platform.iam.tenant.domain.entity.Tenant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTenantAuthentication {

    /**
     * 用户信息
     */
    private User user;

    /**
     * 租户信息
     */
    private Tenant tenant;
}
