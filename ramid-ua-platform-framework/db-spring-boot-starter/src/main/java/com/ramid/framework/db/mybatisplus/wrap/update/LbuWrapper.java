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

package com.ramid.framework.db.mybatisplus.wrap.update;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 修改构造器
 *
 * @author Levin
 */
public class LbuWrapper<T> extends AbstractLambdaWrapper<T, LbuWrapper<T>>
        implements
        Update<LbuWrapper<T>, SFunction<T, ?>> {

    /**
     * SQL 更新字段内容，例如：name='1', age=2
     */
    private final List<String> sqlSet;

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate()
     */
    public LbuWrapper() {
        // 如果无参构造函数，请注意实体 NULL 情况 SET 必须有否则 SQL 异常
        this(null);
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate(entity)
     */
    public LbuWrapper(T entity) {
        super.setEntity(entity);
        super.initNeed();
        this.sqlSet = new ArrayList<>();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaUpdate(...)
     */
    private LbuWrapper(T entity, List<String> sqlSet, AtomicInteger paramNameSeq,
                       Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                       SharedString lastSql, SharedString sqlComment) {
        super.setEntity(entity);
        this.sqlSet = sqlSet;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
    }

    /**
     * 空值校验
     * 传入空字符串("")时， 视为： 字段名 = ""
     *
     * @param val 参数值
     */
    private static boolean checkCondition(Object val) {
        return val != null;
    }

    @Override
    public LbuWrapper<T> set(boolean condition, SFunction<T, ?> column, Object val, String mapping) {
        if (condition) {
            String sql = formatParam(mapping, val);
            sqlSet.add(column + Constants.EQUALS + sql);
        }
        return this.typedThis;
    }

    @Override
    public LbuWrapper<T> setSql(boolean condition, String setSql, Object... params) {
        if (condition && StringUtils.isNotBlank(setSql)) {
            this.sqlSet.add(setSql);
        }
        return this.typedThis;
    }

    @Override
    public LbuWrapper<T> setIncrBy(boolean condition, SFunction<T, ?> column, Number val) {
        return maybeDo(condition, () -> {
            String realColumn = columnToString(column);
            sqlSet.add(String.format("%s=%s + %s", realColumn, realColumn, val instanceof BigDecimal ? ((BigDecimal) val).toPlainString() : val));
        });
    }

    @Override
    public LbuWrapper<T> setDecrBy(boolean condition, SFunction<T, ?> column, Number val) {
        return maybeDo(condition, () -> {
            String realColumn = columnToString(column);
            sqlSet.add(String.format("%s=%s - %s", realColumn, realColumn, val instanceof BigDecimal ? ((BigDecimal) val).toPlainString() : val));
        });
    }

    @Override
    public String getSqlSet() {
        if (CollectionUtils.isEmpty(this.sqlSet)) {
            return null;
        }
        return String.join(StringPool.COMMA, this.sqlSet);
    }

    @Override
    protected LbuWrapper<T> instance() {
        return new LbuWrapper<>(this.getEntity(), this.sqlSet, this.paramNameSeq, this.paramNameValuePairs, new MergeSegments(),
                SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public LbuWrapper<T> nested(Consumer<LbuWrapper<T>> consumer) {
        return super.nested(consumer);
    }

    @Override
    public LbuWrapper<T> eq(SFunction<T, ?> column, Object val) {
        return super.eq(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> ne(SFunction<T, ?> column, Object val) {
        return super.ne(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> gt(SFunction<T, ?> column, Object val) {
        return super.gt(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> ge(SFunction<T, ?> column, Object val) {
        return super.ge(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> lt(SFunction<T, ?> column, Object val) {
        return super.lt(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> le(SFunction<T, ?> column, Object val) {
        return super.le(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> like(SFunction<T, ?> column, Object val) {
        return super.like(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> notLike(SFunction<T, ?> column, Object val) {
        return super.notLike(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> likeLeft(SFunction<T, ?> column, Object val) {
        return super.likeLeft(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> likeRight(SFunction<T, ?> column, Object val) {
        return super.likeRight(checkCondition(val), column, val);
    }

    @Override
    public LbuWrapper<T> in(SFunction<T, ?> column, Collection<?> coll) {
        return super.in(coll != null && !coll.isEmpty(), column, coll);
    }

    @Override
    public LbuWrapper<T> in(SFunction<T, ?> column, Object... values) {
        return super.in(values != null && values.length > 0, column, values);
    }

}
