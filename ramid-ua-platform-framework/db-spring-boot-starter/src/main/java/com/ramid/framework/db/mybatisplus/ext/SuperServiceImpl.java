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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 不含缓存的Service实现
 * <p>
 * <p>
 * 2，removeById：重写 ServiceImpl 类的方法，删除db
 * 3，removeByIds：重写 ServiceImpl 类的方法，删除db
 * 4，updateAllById： 新增的方法： 修改数据（所有字段）
 * 5，updateById：重写 ServiceImpl 类的方法，修改db后
 *
 * @param <M> Mapper
 * @param <T> 实体
 * @author Levin
 * @since 2020年02月27日18:15:17
 */
public class SuperServiceImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> implements SuperService<T> {

    /**
     * 默认批次提交数量
     */
    private static final int DEFAULT_BATCH_SIZE = 500;

    private static final Logger logger = LoggerFactory.getLogger(SuperServiceImpl.class);

    public SuperMapper<T> getSuperMapper() {
        if (baseMapper != null) {
            return baseMapper;
        }
        throw new RuntimeException("Mapper类转换异常");
    }

    @Override
    public boolean insertBatch(List<T> list) {
        if (CollUtil.isEmpty(list)) {
            logger.warn("请求数据为空....");
            return false;
        }
        CollUtil.split(list, DEFAULT_BATCH_SIZE).forEach(baseMapper::insertBatchSomeColumn);
        return true;
    }

    @Override
    public boolean updateBatch(List<T> list) {
        if (CollUtil.isEmpty(list)) {
            logger.warn("请求数据为空....");
            return false;
        }
        CollUtil.split(list, DEFAULT_BATCH_SIZE).forEach(baseMapper::updateBatchSomeColumnById);
        return true;
    }


}
