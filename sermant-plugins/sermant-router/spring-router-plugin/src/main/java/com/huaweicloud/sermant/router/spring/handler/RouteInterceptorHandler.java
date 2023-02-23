/*
 * Copyright (C) 2023-2023 Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huaweicloud.sermant.router.spring.handler;

import com.huaweicloud.sermant.router.common.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 路由web拦截器处理器
 *
 * @author provenceee
 * @since 2023-02-21
 */
public class RouteInterceptorHandler extends AbstractInterceptorHandler {
    private static final int ORDER = 200;

    /**
     * 获取透传的标记
     *
     * @return 透传的标记
     */
    @Override
    public Map<String, List<String>> getRequestTag(String path, String methodName, Map<String, List<String>> headers,
        Map<String, String[]> parameters) {
        Set<String> matchKeys = configService.getMatchKeys();
        if (CollectionUtils.isEmpty(matchKeys)) {
            return Collections.emptyMap();
        }
        return getRequestTag(headers, matchKeys);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}