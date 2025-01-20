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

package com.ramid.framework.db.mybatisplus.handler.type;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 数组字符串互转
 *
 * @author Levin
 */
@MappedTypes(value = {List.class})
public class LongListTypeHandler extends BaseTypeHandler<List<?>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<?> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, StrUtil.join(StrUtil.COMMA, parameter));
    }

    @Override
    @SneakyThrows
    public List<?> getNullableResult(ResultSet rs, String columnName) {
        String reString = rs.getString(columnName);
        return Convert.toList(Object.class, reString);
    }

    @Override
    @SneakyThrows
    public List<?> getNullableResult(ResultSet rs, int columnIndex) {
        String reString = rs.getString(columnIndex);
        return Convert.toList(Object.class, reString);
    }

    @Override
    @SneakyThrows
    public List<?> getNullableResult(CallableStatement cs, int columnIndex) {
        String reString = cs.getString(columnIndex);
        return Convert.toList(Object.class, reString);
    }

}
