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

package com.ramid.framework.i18n.aspect;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.ramid.framework.commons.i18n.Language;
import com.ramid.framework.i18n.annotation.I18nField;
import com.ramid.framework.i18n.core.I18nMessageResource;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 动态国际化处理
 *
 * @author Levin
 */
@Aspect
@RequiredArgsConstructor
public class I18nAspect {

    private final I18nMessageResource messageSource;

    /***
     * 定义controller切入点拦截规则：拦截标记AccessLog注解和指定包下的方法
     * execution(public * com.ramid.base.controller.*.*(..)) 解释：
     * 第一个* 任意返回类型
     * 第三个* 类下的所有方法
     * ()中间的.. 任意参数
     * annotation(com.ramid.framework.commons.annotation.log.AccessLog) 解释：
     * 标记了@AccessLog 注解的方法
     */
    @Pointcut("execution(public * com.ramid..*.*(..)) && @annotation(com.ramid.framework.i18n.annotation.I18nMethod)")
    public void i18nAspect() {

    }

    /**
     * 返回通知
     *
     * @param obj       obj
     * @param joinPoint joinPoint
     */
    @AfterReturning(returning = "obj", pointcut = "i18nAspect()")
    public void doAfterReturning(JoinPoint joinPoint, Object obj) {
        parse(obj);
    }

    private void parseList(Collection<?> list) {
        list.forEach(this::parse);
    }

    /**
     * 遍历字段，解析出那些字段上标记了指定注解的字段
     *
     * @param obj 对象
     */
    private void parse(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Collection<?> list) {
            parseList(list);
            return;
        }
        // 解析方法上的注解，计算出obj对象中所有需要查询的数据
        Field[] fields = ReflectUtil.getFields(obj.getClass());
        for (Field field : fields) {
            I18nField annotation = field.getAnnotation(I18nField.class);
            if (annotation == null) {
                continue;
            }
            final Object fieldValue = ReflectUtil.getFieldValue(obj, field);
            if (fieldValue == null) {
                continue;
            }
            if (fieldValue instanceof Language item) {
                ReflectUtil.setFieldValue(obj, annotation.target(), messageSource.getMessage(item.getLanguage(), fieldValue));
            } else {
                if (StrUtil.isNotBlank(annotation.target())) {
                    ReflectUtil.setFieldValue(obj, annotation.target(), messageSource.getMessage(annotation.code(), fieldValue));
                } else {
                    ReflectUtil.setFieldValue(obj, field, messageSource.getMessage(fieldValue.toString()));
                }
            }
        }
    }
}
