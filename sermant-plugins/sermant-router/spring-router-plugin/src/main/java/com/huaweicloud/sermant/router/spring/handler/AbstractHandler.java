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

import com.huaweicloud.sermant.core.service.ServiceManager;
import com.huaweicloud.sermant.router.common.handler.Handler;
import com.huaweicloud.sermant.router.spring.service.SpringConfigService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 抽象处理器
 *
 * @author provenceee
 * @since 2023-02-21
 */
public abstract class AbstractHandler implements Handler {
    protected final SpringConfigService configService;

    /**
     * 构造方法
     */
    public AbstractHandler() {
        configService = ServiceManager.getService(SpringConfigService.class);
    }

    /**
     * 从headers中，获取需要透传的请求标记
     *
     * @param headers http请求太
     * @param keys 需要获取的标记的key
     * @return 请求标记
     */
    protected Map<String, List<String>> getRequestTag(Map<String, List<String>> headers, Set<String> keys) {
        Map<String, List<String>> map = new HashMap<>();
        for (String headerKey : keys) {
            if (headers.containsKey(headerKey)) {
                map.put(headerKey, headers.get(headerKey));
            }
        }
        return map;
    }
}
