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

package com.ramid.ua.platform.iam.system.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.ua.platform.iam.system.domain.dto.resp.PositionPageResp;
import com.ramid.ua.platform.iam.system.domain.entity.Position;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 岗位
 *
 * @author Levin
 */

@Repository
public interface PositionMapper extends SuperMapper<Position> {
    
    /**
     * 分页查询岗位信息（含角色）
     *
     * @param page    page
     * @param wrapper wrapper
     * @return 查询结果
     */
    IPage<PositionPageResp> findStationPage(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<Position> wrapper);
    
}
