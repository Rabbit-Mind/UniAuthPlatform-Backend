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

package com.ramid.framework.redis.plus.utils;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Levin
 */
public class RedisAopUtils {

    /**
     * parser 解析器
     */
    private static final ExpressionParser parser = new SpelExpressionParser();
    /**
     * 它使用JDK 8的反射功能
     * 用于内省参数名称（基于 -parameters 编译器标志）
     */
    private static final StandardReflectionParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();

    /**
     * 解析spring EL表达式,无参数方法
     *
     * @param key     表达式
     * @param method  方法
     * @param args    方法参数
     * @param point   切面对象
     * @param useArgs 限流是否到接口参数级别
     * @return parse return
     */
    public static String parse(String key, boolean useArgs, Method method, Object[] args, ProceedingJoinPoint point) {
        if (!useArgs) {
            return parseDefaultKey(key, false, method, point);
        }
        String[] params = discoverer.getParameterNames(method);
        // 指定spel表达式，并且有适配参数时
        if (Objects.nonNull(params) && Objects.nonNull(key)) {
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < params.length; i++) {
                context.setVariable(params[i], args[i]);
            }
            return parser.parseExpression(key).getValue(context, String.class);
        } else {
            return parseDefaultKey(key, true, method, point);
        }
    }

    /**
     * 生成key的分三部分，类名+方法名，参数,key
     * 不满足指定SPEL表达式并且有适配参数时，
     * 采用本方法生成最终redis的限流器key
     *
     * @param key    key
     * @param method method
     * @param point  point
     * @return parseDefaultKey
     */
    public static String parseDefaultKey(String key, boolean useArgs, Method method, ProceedingJoinPoint point) {
        // 保证key有序
        Map<String, Object> keyMap = new LinkedHashMap<>();
        // 放入target的名字
        keyMap.put("target", point.getTarget().getClass().toGenericString());
        // 放入method的名字
        keyMap.put("method", method.getName());
        Object[] params = point.getArgs();
        // 把所有参数放进去
        if (useArgs && Objects.nonNull(params)) {
            for (int i = 0; i < params.length; i++) {
                keyMap.put("params-" + i, params[i]);
            }
        }
        // key表达式
        key = StringUtils.isEmpty(key) ? "" : "_" + key;
        // 使用MD5生成位移key
        return MD5.create().digestHex(JSONObject.toJSONString(keyMap) + key).toUpperCase();
    }

}
