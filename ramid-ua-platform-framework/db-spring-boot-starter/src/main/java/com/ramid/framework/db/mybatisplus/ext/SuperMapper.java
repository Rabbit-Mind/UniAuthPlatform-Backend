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

package com.ramid.framework.db.mybatisplus.ext;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * 基于MP的 BaseMapper 新增了2个方法： insertBatchSomeColumn、updateAllById
 *
 * @param <T> 实体
 * @author Levin
 */
@Repository
public interface SuperMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入 仅适用于 最好是 mysql 其他数据库有可能有问题
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    int insertBatchSomeColumn(Collection<?> entityList);

    /**
     * 批量修改 仅适用于 最好是 mysql 其他数据库有可能有问题
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    int updateBatchSomeColumnById(Collection<?> entityList);

    /**
     * 查询单挑数据
     *
     * @param field 字段
     * @param value 值
     * @return 查询结果
     */
    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(Wraps.<T>lbQ().eq(field, value).last(" limit 1"));
    }

    /**
     * 查询单挑数据
     *
     * @param field1 字段1
     * @param value1 值1
     * @param field2 字段2
     * @param value2 值2
     * @return 查询结果
     */
    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectOne(Wraps.<T>lbQ().eq(field1, value1).eq(field2, value2));
    }

    /**
     * 统计数据
     *
     * @return 统计结果
     */
    default Long selectCount() {
        return selectCount(Wraps.lbQ());
    }

    /**
     * 统计数据
     *
     * @param field 字段
     * @param value 值
     * @return 统计结果
     */
    default Long selectCount(SFunction<T, ?> field, Object value) {
        return selectCount(Wraps.<T>lbQ().eq(field, value));
    }

    default long selectCount(SFunction<T, ?> field, Object value, SFunction<T, ?> field2, Object value2) {
        Long count = selectCount(Wraps.<T>lbQ().eq(field, value).eq(field2, value2));
        return count != null ? count : 0;
    }

    default void existsCallback(SFunction<T, ?> field, Object value, Supplier<?> func) {
        Long count = selectCount(Wraps.<T>lbQ().eq(field, value));
        if (count != null && count > 0) {
            func.get();
        }
    }

    default boolean exists(SFunction<T, ?> field, Object value) {
        Long count = selectCount(Wraps.<T>lbQ().eq(field, value));
        return count != null && count > 0;
    }

    /**
     * 查询全部数据
     *
     * @return 查询结果
     */
    default List<T> selectList() {
        return selectList(Wraps.lbQ());
    }

    /**
     * 查询数据
     *
     * @param field 字段
     * @param value 值
     * @return 查询结果
     */
    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(Wraps.<T>lbQ().eq(field, value));
    }

    /**
     * 查询数据
     *
     * @param field1 字段
     * @param value1 值
     * @return 查询结果
     */
    default List<T> selectList(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectList(Wraps.<T>lbQ().eq(field1, value1).eq(field2, value2));
    }


    /**
     * 批量查询
     *
     * @param field  字段
     * @param values 值
     * @return 查询结果
     */
    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(Wraps.<T>lbQ().in(field, values));
    }

    /**
     * 批量插入，适合大量数据插入
     *
     * @param list 数据集合
     */
    default void insertBatch(Collection<T> list) {
        Db.saveBatch(list);
    }

    /**
     * 批量插入，适合大量数据插入
     *
     * @param list 数据集合
     * @param size 插入数量 Db.saveBatch 默认为 1000
     */
    default void insertBatch(Collection<T> list, int size) {
        Db.saveBatch(list, size);
    }

    /**
     * 根据ID批量修改
     *
     * @param list list
     */
    default void updateBatch(Collection<T> list) {
        Db.updateBatchById(list);
    }

    /**
     * 根据ID批量修改
     *
     * @param list list
     * @param size 大小
     */
    default void updateBatch(Collection<T> list, int size) {
        Db.updateBatchById(list, size);
    }

}
