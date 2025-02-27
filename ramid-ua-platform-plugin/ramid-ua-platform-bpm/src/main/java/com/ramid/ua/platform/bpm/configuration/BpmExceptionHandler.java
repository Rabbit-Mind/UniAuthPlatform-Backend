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

package com.ramid.ua.platform.bpm.configuration;

import cn.dev33.satoken.context.SaHolder;
import com.ramid.framework.commons.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * BPMN 异常处理
 *
 * @author Levin
 */
@Slf4j
@RestControllerAdvice
public class BpmExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ParseException.class)
    public Result<ResponseEntity<Void>> handlerException(ParseException e) {
        String message = "BPMN 异常[" + e.getLocalizedMessage() + "]";
        log.error("bpm-parse => http request uri => {},message => {}", SaHolder.getRequest().getUrl(), e.getLocalizedMessage());
        return Result.fail(HttpStatus.BAD_REQUEST.value(), message);
    }

}