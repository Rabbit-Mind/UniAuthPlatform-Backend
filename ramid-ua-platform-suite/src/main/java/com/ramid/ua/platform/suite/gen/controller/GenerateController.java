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
package com.ramid.ua.platform.suite.gen.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ramid.framework.commons.exception.CheckedException;
import com.ramid.framework.db.mybatisplus.page.PageRequest;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.suite.gen.domain.dto.req.GenerateReq;
import com.ramid.ua.platform.suite.gen.domain.dto.resp.GenerateTableResp;
import com.ramid.ua.platform.suite.gen.domain.entity.GenerateEntity;
import com.ramid.ua.platform.suite.gen.service.GenerateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Levin
 */
@Slf4j
@RestController
@RequestMapping("/generates")
@RequiredArgsConstructor
@Tag(name = "代码生成", description = "代码生成")
@Validated
public class GenerateController {

    private final GenerateService generateService;

    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping
    public Page<GenerateEntity> page(PageRequest pageRequest, String author) {
        return generateService.page(pageRequest.buildPage(),
                Wraps.<GenerateEntity>lbQ().eq(GenerateEntity::getAuthor, author));
    }

    @Operation(summary = "获取所有的表", description = "获取所有的表")
    @GetMapping("/tables")
    public List<GenerateTableResp> tables() {
        return generateService.loadTables();
    }

    @Operation(summary = "代码生成")
    @PostMapping("/{id}/download")
    public void add(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        final GenerateEntity generateEntity = this.generateService.getById(id);
        final String path = generateService.generate(generateEntity);
        final File file = ZipUtil.zip(path);
        if (!file.exists()) {
            throw CheckedException.badRequest("文件不存在");
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            IoUtil.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            log.error("[文件下载失败]", e);
            throw CheckedException.badRequest("文件下载失败");
        } finally {
            try {
                Files.deleteIfExists(Paths.get(file.getPath()));
                log.info("[删除成功] - [目录: {}]", file.getPath());
            } catch (IOException e) {
                log.warn("[文件删除失败] - [{}]", e.getLocalizedMessage());
            }
        }
    }

    @Operation(summary = "添加代码生成")
    @PostMapping
    public void add(@Validated @RequestBody GenerateReq req) {
        generateService.save(BeanUtil.toBean(req, GenerateEntity.class));
    }

    @Operation(summary = "编辑代码生成")
    @PutMapping("/{id}")
    public void edit(@PathVariable Long id, @Validated @RequestBody GenerateReq req) {
        final GenerateEntity request = BeanUtil.toBean(req, GenerateEntity.class);
        request.setId(id);
        generateService.updateById(request);
    }

    @Operation(summary = "删除代码生成")
    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        generateService.removeById(id);
    }
}
