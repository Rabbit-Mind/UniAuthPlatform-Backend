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

package com.ramid.ua.platform.iam.system.domain.dto.req;

import cn.hutool.core.lang.RegexPool;
import com.ramid.ua.platform.iam.system.domain.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author Levin
 */
@Data
@Schema(name = "UserUpdateReq", description = "用户编辑DTO")
public class UserUpdateReq implements Serializable {
    
    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 50, message = "姓名长度不能超过 {max}")
    private String nickName;
    
    @Schema(description = "组织ID")
    private Long orgId;
    
    @Schema(description = "岗位ID")
    private Long positionId;
    
    @Schema(description = "邮箱")
    @Length(max = 100, message = "邮箱长度不能超过 {max}")
    @Pattern(regexp = RegexPool.EMAIL, message = "邮箱格式错误")
    private String email;
    
    @Schema(description = "手机")
    @NotBlank(message = "手机号不能为空")
    @Length(max = 11, message = "手机长度不能超过20")
    @Pattern(regexp = RegexPool.MOBILE, message = "手机号格式错误")
    private String mobile;
    
    @Schema(description = "性别")
    private Sex sex;
    
    @Schema(description = "状态")
    private Boolean status;
    
    @Schema(description = "头像")
    @Length(max = 255, message = "头像长度不能超过 {max}")
    private String avatar;
    
    @Schema(description = "民族")
    @Length(max = 20, message = "民族长度不能超过20")
    private String nation;
    
    @Schema(description = "学历")
    @Length(max = 20, message = "学历长度不能超过20")
    private String education;
    
    @Schema(description = "职位状态")
    @Length(max = 20, message = "职位状态长度不能超过{max}")
    private String positionStatus;
    
    @Schema(description = "工作描述")
    @Length(max = 255, message = "描述长度不能超过{max}")
    private String description;
}
