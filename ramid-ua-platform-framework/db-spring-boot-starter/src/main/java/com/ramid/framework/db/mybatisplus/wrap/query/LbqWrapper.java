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

package com.ramid.framework.db.mybatisplus.wrap.query;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 查询构造器
 * 类似 LambdaQueryWrapper 的增强 Wrapper
 * <p>
 * 相比 LambdaQueryWrapper 的增强如下：
 * 1，new QueryWrapper(T entity)时， 对entity 中的string字段 %和_ 符号进行转义，便于模糊查询
 * 2，对nested、eq、ne、gt、ge、lt、le、in、*like*、 等方法 进行条件判断，null 或 "" 字段不加入查询
 * 3，对*like*相关方法的参数 %和_ 符号进行转义，便于模糊查询
 * 4，增加 leFooter 方法， 将日期参数值，强制转换成当天 23：59：59
 * 5，增加 geHeader 方法， 将日期参数值，强制转换成当天 00：00：00
 *
 * @author Levin
 * @since 2020-04-03
 */
public class LbqWrapper<T> extends AbstractLambdaWrapper<T, LbqWrapper<T>>
        implements
        Query<LbqWrapper<T>, T, SFunction<T, ?>> {

    /**
     * 查询字段
     */
    private SharedString sqlSelect = new SharedString();

    /**
     * 是否跳过空值（Levin项目扩展）
     */
    private boolean skipEmpty = true;

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public LbqWrapper() {
        this((T) null);
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public LbqWrapper(T entity) {
        super.setEntity(entity);
        super.initNeed();
        // 覆盖之前的entity
        if (entity != null) {
            super.setEntity(Wraps.replace(BeanUtil.toBean(entity, getEntityClass())));
        }
    }

    /**
     * 不建议直接 new 该实例，使用 Wraps.lbQ(entity)
     */
    public LbqWrapper(Class<T> entityClass) {
        super.setEntityClass(entityClass);
        super.initNeed();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
     */
    LbqWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
               Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
               SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    /**
     * SELECT 部分 SQL 设置
     *
     * @param columns 查询字段
     */
    @SafeVarargs
    @Override
    public final LbqWrapper<T> select(SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(columnsToString(false, columns));
        }
        return this.typedThis;
    }

    @Override
    public LbqWrapper<T> select(boolean condition, List<SFunction<T, ?>> columns) {
        if (Objects.nonNull(columns)) {
            this.sqlSelect.setStringValue(columnsToString(false, columns));
        }
        return this.typedThis;
    }

    /**
     * 过滤查询的字段信息(主键除外!)
     * <p>例1: 只要 java 字段名以 "test" 开头的             -> select(i -&gt; i.getProperty().startsWith("test"))</p>
     * <p>例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)</p>
     * <p>例3: 只要 java 字段没有填充策略的                 -> select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)</p>
     * <p>例4: 要全部字段                                   -> select(i -&gt; true)</p>
     * <p>例5: 只要主键字段                                 -> select(i -&gt; false)</p>
     *
     * @param predicate 过滤方式
     * @return this
     */
    @Override
    public LbqWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        super.setEntityClass(entityClass);
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getEntityClass()).chooseSelect(predicate));
        return this.typedThis;
    }

    @Override
    public String getSqlSelect() {
        return this.sqlSelect.getStringValue();
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect 不向下传递</p>
     */
    @Override
    protected LbqWrapper<T> instance() {
        return new LbqWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
    }

    @Override
    public LbqWrapper<T> nested(Consumer<LbqWrapper<T>> consumer) {
        return super.nested(consumer);
    }

    @Override
    public LbqWrapper<T> eq(SFunction<T, ?> column, Object val) {
        return super.eq(this.checkCondition(val), column, val);
    }

    @Override
    public LbqWrapper<T> ne(SFunction<T, ?> column, Object val) {
        return super.ne(this.checkCondition(val), column, val);
    }

    @Override
    public LbqWrapper<T> gt(SFunction<T, ?> column, Object val) {
        return super.gt(this.checkCondition(val), column, val);
    }

    @Override
    public LbqWrapper<T> ge(SFunction<T, ?> column, Object val) {
        return super.ge(this.checkCondition(val), column, val);
    }

    public LbqWrapper<T> geHeader(SFunction<T, ?> column, LocalDateTime val) {
        if (val != null) {
            val = LocalDateTime.of(val.toLocalDate(), LocalTime.MIN);
        }
        return super.ge(this.checkCondition(val), column, val);
    }

    public LbqWrapper<T> geHeader(SFunction<T, ?> column, LocalDate val) {
        LocalDateTime dateTime = null;
        if (val != null) {
            dateTime = LocalDateTime.of(val, LocalTime.MIN);
        }
        return super.ge(this.checkCondition(val), column, dateTime);
    }

    @Override
    public LbqWrapper<T> lt(SFunction<T, ?> column, Object val) {
        return super.lt(this.checkCondition(val), column, val);
    }

    @Override
    public LbqWrapper<T> le(SFunction<T, ?> column, Object val) {
        return super.le(this.checkCondition(val), column, val);
    }

    public LbqWrapper<T> leFooter(SFunction<T, ?> column, LocalDateTime val) {
        if (val != null) {
            val = LocalDateTime.of(val.toLocalDate(), LocalTime.MAX);
        }
        return super.le(this.checkCondition(val), column, val);
    }

    public LbqWrapper<T> leFooter(SFunction<T, ?> column, LocalDate val) {
        LocalDateTime dateTime = null;
        if (val != null) {
            dateTime = LocalDateTime.of(val, LocalTime.MAX);
        }
        return super.le(this.checkCondition(val), column, dateTime);
    }

    @Override
    public LbqWrapper<T> in(SFunction<T, ?> column, Collection<?> coll) {
        return super.in(coll != null && !coll.isEmpty(), column, coll);
    }

    @Override
    public LbqWrapper<T> in(SFunction<T, ?> column, Object... values) {
        return super.in(values != null && values.length > 0, column, values);
    }

    @Override
    public LbqWrapper<T> like(SFunction<T, ?> column, Object val) {
        return super.like(this.checkCondition(val), column, val);
    }

    @Override
    public LbqWrapper<T> likeLeft(SFunction<T, ?> column, Object val) {
        return super.likeLeft(this.checkCondition(val), column, val);
    }

    @Override
    public LbqWrapper<T> likeRight(SFunction<T, ?> column, Object val) {
        return super.likeRight(this.checkCondition(val), column, val);
    }

    /**
     * 忽略实体中的某些字段，实体中的字段默认是会除了null以外的全部进行等值匹配
     * 再次可以进行忽略
     *
     * @param column column
     * @param val    val
     * @return LbqWrapper
     */

    @Override
    public LbqWrapper<T> notLike(SFunction<T, ?> column, Object val) {
        return super.notLike(this.checkCondition(val), column, val);
    }

    // ----------------以下为自定义方法---------

    /**
     * 取消跳过空的字符串  不允许跳过空的字符串
     *
     * @return
     */
    public LbqWrapper<T> cancelSkipEmpty() {
        this.skipEmpty = false;
        return this;
    }

    /**
     * 空值校验
     * 传入空字符串("")时， 视为： 字段名 = ""
     *
     * @param val 参数值
     */
    private boolean checkCondition(Object val) {
        if (!this.skipEmpty) {
            return val != null;
        }
        if (val instanceof String v) {
            return StringUtils.isNotBlank(v);
        }
        if (val instanceof Collection<?> v) {
            return CollectionUtils.isNotEmpty(v);
        }
        return Objects.nonNull(val);
    }

    /**
     * 忽略实体(entity)中的某些字段，实体中的字段默认是会除了null以外的全部进行等值匹配
     * 再次可以进行忽略
     *
     * @param <A>       这个是传入的待忽略字段的set方法
     * @param setColumn setColumn
     * @return LbqWrapper
     */
    public <A extends Object> LbqWrapper<T> ignore(BiFunction<T, A, ?> setColumn) {
        setColumn.apply(this.getEntity(), null);
        return this;
    }

}
