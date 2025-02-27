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
package com.ramid.ua.platform.suite.gen.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.collect.Maps;
import com.ramid.framework.commons.entity.SuperEntity;
import com.ramid.framework.db.mybatisplus.ext.SuperMapper;
import com.ramid.framework.db.mybatisplus.ext.SuperService;
import com.ramid.framework.db.mybatisplus.ext.SuperServiceImpl;
import com.ramid.ua.platform.suite.gen.domain.dto.resp.GenerateTableResp;
import com.ramid.ua.platform.suite.gen.domain.entity.GenerateEntity;
import com.ramid.ua.platform.suite.gen.repository.GenerateMapper;
import com.ramid.ua.platform.suite.gen.service.GenerateService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.generator.config.rules.DateType.TIME_PACK;

/**
 * @author Levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateServiceImpl extends SuperServiceImpl<GenerateMapper, GenerateEntity> implements GenerateService {

    private final DataSource dataSource;

    @SneakyThrows
    @Override
    @DSTransactional
    public String generate(GenerateEntity request) {
        Map<String, Object> customMap = Maps.newHashMap();
        customMap.put("apiUrlPrefix", request.getApiUrlPrefix());
        customMap.put("platformId", request.getPlatformId());
        customMap.put("now", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        Map<String, String> customFiles = Maps.newHashMap();
        customFiles.put("/crud.ts", "/templates/front/crud.ts.ftl");
        customFiles.put("/index.vue", "/templates/front/index.vue.ftl");
        customFiles.put("/api.ts", "/templates/front/api.ts.ftl");
        customFiles.put("_menu.sql", "/templates/sql/resource.sql.ftl");
        final String peek = DynamicDataSourceContextHolder.peek();
        final DataSource ds = ((DynamicRoutingDataSource) dataSource).getDataSource(peek);
        final String rootDir = StrUtil.blankToDefault(request.getRootDir(), System.getProperty("user.dir") + "/.generated/");
        FastAutoGenerator.create(new DataSourceConfig.Builder(ds))
                .globalConfig(builder -> {
                    builder.author(request.getAuthor()).outputDir(rootDir).dateType(TIME_PACK);
                    if (request.getSpringdoc()) {
                        builder.enableSpringdoc();
                    }
                })
                .packageConfig(builder -> builder.parent(request.getParentPackage()).moduleName(request.getModuleName())
                        .entity("domain").mapper("mapper").xml("mapper.xml")
                        .service("service").serviceImpl("service.impl").controller("controller")
                        // 设置mapperXml生成路径
                        .pathInfo(Collections.singletonMap(OutputFile.xml, rootDir)))
                .strategyConfig(builder -> builder.enableCapitalMode()
                        .enableSkipView().disableSqlFilter()
                        .addInclude(request.getTableName())
                        .addTablePrefix(request.getTablePrefix())
                        .entityBuilder()
                        .naming(NamingStrategy.underline_to_camel).columnNaming(NamingStrategy.underline_to_camel)
                        .logicDeleteColumnName(request.getLogicDeleteField())
                        .enableFileOverride()
                        .enableLombok().superClass(SuperEntity.class)
                        .mapperBuilder().superClass(SuperMapper.class)
                        .serviceBuilder().superServiceClass(SuperService.class)
                        .convertServiceFileName(entityName -> entityName + ConstVal.SERVICE)
                        .superServiceImplClass(SuperServiceImpl.class)
                        .controllerBuilder().enableRestStyle())
                .templateConfig(builder -> builder
                        .service("/templates/backend/service.java")
                        .serviceImpl("/templates/backend/serviceImpl.java")
                        .mapper("/templates/backend/mapper.java")
                        .xml("/templates/backend/mapper.xml")
                        .controller("/templates/backend/controller.java")
                        .build())
                .injectionConfig(builder -> builder.beforeOutputFile((tableInfo, objectMap) -> log.debug("tableInfo - {},objectMap - {}", tableInfo.getEntityName(), objectMap))
                        .customMap(customMap).customFile(customFiles).build())
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
        log.info("{}生成完成:{}", request.getTableName(), rootDir);
        return rootDir;
    }

    @Override
    public List<GenerateTableResp> loadTables() {
        final List<String> tables = this.baseMapper.loadTables();
        if (CollectionUtils.isEmpty(tables)) {
            return null;
        }
        return tables.stream().filter(StringUtils::isNotBlank)
                .map(table -> GenerateTableResp.builder().label(table).value(table).build())
                .collect(Collectors.toList());
    }

}
