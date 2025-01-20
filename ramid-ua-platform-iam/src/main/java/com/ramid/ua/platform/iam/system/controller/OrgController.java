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

package com.ramid.ua.platform.iam.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.google.common.collect.Maps;
import com.ramid.framework.commons.BeanUtilPlus;
import com.ramid.framework.commons.annotation.log.AccessLog;
import com.ramid.framework.db.mybatisplus.wrap.Wraps;
import com.ramid.ua.platform.iam.system.domain.dto.req.OrgSaveReq;
import com.ramid.ua.platform.iam.system.domain.entity.Org;
import com.ramid.ua.platform.iam.system.service.OrgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组织架构
 *
 * @author Levin
 */
@Slf4j
@RestController
@RequestMapping("/org")
@RequiredArgsConstructor
@Tag(name = "组织架构", description = "组织架构")
public class OrgController {
    
    private final OrgService orgService;
    
    @GetMapping("/trees")
    @Operation(summary = "查询系统所有的组织树", description = "查询系统所有的组织树")
    public List<Tree<Long>> trees(String name, Boolean status) {
        List<Org> list = this.orgService.list(Wraps.<Org>lbQ().like(Org::getLabel, name).eq(Org::getStatus, status).orderByAsc(Org::getSequence));
        final List<TreeNode<Long>> nodes = list.stream().map(org -> {
            final TreeNode<Long> treeNode = new TreeNode<>(org.getId(), org.getParentId(), org.getLabel(), org.getSequence());
            Map<String, Object> extra = Maps.newLinkedHashMap();
            extra.put("label", org.getLabel());
            extra.put("alias", org.getAlias());
            extra.put("status", org.getStatus());
            extra.put("sequence", org.getSequence());
            extra.put("tel", org.getTel());
            extra.put("description", org.getDescription());
            treeNode.setExtra(extra);
            return treeNode;
        }).collect(Collectors.toList());
        return TreeUtil.build(nodes, 0L);
    }
    
    @PostMapping("/create")
    @AccessLog(module = "组织架构", description = "创建组织架构")
    @Operation(summary = "创建组织架构")
    @SaCheckPermission(value = {"sys:org:add"})
    public void create(@Validated @RequestBody OrgSaveReq req) {
        this.orgService.create(req);
    }
    
    @PutMapping("/{id}/modify")
    @AccessLog(module = "组织架构", description = "编辑组织架构")
    @Operation(summary = "编辑组织架构")
    @SaCheckPermission(value = {"sys:org:edit"})
    public void modify(@PathVariable Long id, @Validated @RequestBody OrgSaveReq req) {
        orgService.updateById(BeanUtilPlus.toBean(id, req, Org.class));
    }
    
    @DeleteMapping("/{id}")
    @AccessLog(module = "组织架构", description = "删除组织架构")
    @Operation(summary = "删除组织架构")
    @SaCheckPermission(value = {"sys:org:remove"})
    public void del(@PathVariable Long id) {
        orgService.remove(id);
    }
    
}
