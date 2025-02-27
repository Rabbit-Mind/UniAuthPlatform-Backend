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

package com.ramid.framework.feign.plugin.mock;

import cn.hutool.core.io.IoUtil;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.loadbalancer.LoadBalancerFeignRequestTransformer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Levin
 */
@Slf4j
public class MockLoadBalancerFeignClient extends FeignBlockingLoadBalancerClient {

    private static final String REQUEST_ORIGIN_KEY = "request-origion";
    private static final String REQUEST_ORIGIN_VALUE = "SwaggerBootstrapUi";
    private final MockProperties mockProperties;

    public MockLoadBalancerFeignClient(Default delegate, LoadBalancerClient loadBalancerClient,
                                       LoadBalancerClientFactory loadBalancerClientFactory,
                                       List<LoadBalancerFeignRequestTransformer> transformers,
                                       MockProperties mockProperties) {

        super(delegate, loadBalancerClient, loadBalancerClientFactory, transformers);
        this.mockProperties = mockProperties;
        log.info("mock feign 负载均衡器初始化");
    }

    /**
     * 2. 请求的服务在mock服务列表中,则请求走mock服务器
     */
    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        String url = request.url();
        URI uri = URI.create(url);
        final String host = uri.getHost();
        final Map<String, MockProperties.Server> servicesMap = mockProperties.getServerMap();
        if (servicesMap.containsKey(host)) {
            final MockProperties.Server server = servicesMap.get(host);
            final Map<String, Collection<String>> feignHeaders = new LinkedHashMap<>(request.headers());
            final Collection<String> requestOriginList = feignHeaders.get(REQUEST_ORIGIN_KEY);
            if (!CollectionUtils.isEmpty(requestOriginList) && requestOriginList.contains(REQUEST_ORIGIN_VALUE)) {
                feignHeaders.remove("content-type");
                feignHeaders.put(HttpHeaders.CONTENT_TYPE, Collections.singleton("application/json"));
                log.debug("doc.html origin,remove content-type");
            }
            Request newRequest = Request.create(request.httpMethod(), url.replace(host, server.getServerUrl()), feignHeaders,
                    request.body(), request.charset(), null);
            return getResponse(newRequest, server);
        }
        return super.execute(request, options);
    }

    /**
     * 请求响应
     */
    private Response getResponse(Request request, MockProperties.Server server) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
        final Map<String, Collection<String>> requestHeaders = request.headers();
        for (Map.Entry<String, Collection<String>> entry : requestHeaders.entrySet()) {
            headers.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        RestTemplate template = new RestTemplate();
        HttpEntity<byte[]> entity = new HttpEntity<>(request.body(), headers);
        final HttpMethod httpMethod = Objects.requireNonNull(HttpMethod.valueOf(request.httpMethod().name()));
        final ResponseEntity<String> exchange = template.exchange(URLDecoder.decode(request.url(), StandardCharsets.UTF_8), httpMethod, entity, String.class);
        final String body = exchange.getBody();
        InputStream inputStream = null;
        if (Objects.nonNull(body)) {
            log.debug("响应结果 - {}", body);
            inputStream = IoUtil.toUtf8Stream(body);
        }
        return Response.builder().request(request)
                .body(inputStream == null ? new byte[0] : IoUtil.readBytes(inputStream))
                .headers(request.headers())
                .status(exchange.getStatusCode().value()).build();
    }

}