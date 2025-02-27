/*
 * Copyright (c) 2023 RAMID-UNI-AUTH-PLATFORM Authors. All Rights Reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ramid.framework.robot.message.push;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.ramid.framework.robot.RobotProperties;
import com.ramid.framework.robot.emums.NotifyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 钉钉机器人
 *
 * @author Levin
 */
@Slf4j
@RequiredArgsConstructor
public class DingTalkRobotMessageHandler implements RobotMessageHandler {

    private final RobotProperties robotProperties;

    @Override
    public String notify(String message) {
        Map<String, Object> body = Map.ofEntries(
                Map.entry("msgtype", "text"),
                Map.entry("at", Map.of("isAtAll", true)),
                Map.entry("text", Map.of("content", message)));
        String response = this.request(body);
        log.info("ding talk notify response - {}", response);
        return response;
    }

    @Override
    public String notify(String message, Map<?, ?> map, boolean ignoreNull) {
        return notify(StrFormatter.format(message, map, ignoreNull));
    }

    @Override
    public String getUrl() {
        RobotProperties.DingTalk dingTalk = robotProperties.getDingTalk();
        StringBuilder url = new StringBuilder();
        url.append("https://oapi.dingtalk.com/robot/send?access_token=");
        url.append(dingTalk.getAccessToken());
        String secret = dingTalk.getSecret();
        if (StrUtil.isBlank(secret)) {
            return url.toString();
        }
        Long timestamp = System.currentTimeMillis();
        url.append("&timestamp=").append(timestamp);
        String sign = SecureUtil.hmacSha256(secret).digestHex(timestamp + "\n" + secret);
        url.append("&sign=").append(URLEncoder.encode(sign, StandardCharsets.UTF_8));
        return url.toString();
    }

    @Override
    public NotifyType notifyType() {
        return NotifyType.DING_TALK;
    }
}
