package com.ramid.ua.platform.iam.system.repository;

import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.ua.platform.iam.auth.domain.entity.UserThirdAccount;
import org.springframework.stereotype.Repository;

/**
 * @author levin
 */
@Repository
public interface ThirdAccountMapper extends SuperMapper<UserThirdAccount> {
}
