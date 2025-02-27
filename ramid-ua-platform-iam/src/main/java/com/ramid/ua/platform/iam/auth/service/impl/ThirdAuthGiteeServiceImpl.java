package com.ramid.ua.platform.iam.auth.service.impl;

import com.ramid.ua.platform.iam.auth.configuration.ThirdAuthProperties;
import com.ramid.ua.platform.iam.auth.service.ThirdAuthService;
import com.ramid.ua.platform.iam.auth.domain.dto.resp.ThirdAuthResp;
import com.ramid.ua.platform.iam.system.domain.enums.ThirdAuthType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author levin
 * <p>
 * GITEE 对接文档 <a href="https://www.justauth.cn/guide/oauth/gitee/"/>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThirdAuthGiteeServiceImpl implements ThirdAuthService {
    /**
     * 忽略 star 检测的用户
     */
    // TODO: 后面可通过数据库存储以及在平台设置相应功能，设定忽略与项目名称
    private static final List<String> IGNORE_ACCOUNT = List.of("");
    private static final String STAR_PROJECT_NAME = "";

    private final ThirdAuthProperties thirdAuthProperties;

    @Override
    public ThirdAuthType platform() {
        return ThirdAuthType.GITEE;
    }

    @Override
    public ThirdAuthResp authorize() {
        AuthRequest authRequest = authRequest();
        // 随机的state字符串
        String state = AuthStateUtils.createState();
        // 构造授权地址
        String authorizeUrl = authRequest.authorize(state);
        return ThirdAuthResp.builder().authorizeUrl(authorizeUrl).state(state).build();
    }


    @Override
    public void callback(AuthUser user) {
//        String body = null;
//        AuthToken authToken = user.getToken();
//        String accessToken = authToken.getAccessToken();
//        JSONObject rawUserInfo = user.getRawUserInfo();
//        String starredUrl = rawUserInfo.getString("starred_url");
//        try (HttpResponse execute = HttpUtil.createGet(starredUrl).auth(accessToken).execute()) {
//            body = execute.body();
//        } catch (Exception ex) {
//            log.error("异常信息", ex);
//        }
//        if (!IGNORE_ACCOUNT.contains(user.getUsername()) || StrUtil.contains(body, STAR_PROJECT_NAME)) {
//            throw CheckedException.badRequest("请先 Star {0} 项目,所有代码均开源并不会采集信息", STAR_PROJECT_NAME);
//        }
    }

    @Override
    public AuthRequest authRequest() {
        AuthConfig config = thirdAuthProperties.getConfigMap().get(platform());
        return new AuthGiteeRequest(config);
    }

}
