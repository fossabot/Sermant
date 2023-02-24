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

package com.huaweicloud.sermant.router.dubbo.handler;

import com.huaweicloud.sermant.core.service.ServiceManager;
import com.huaweicloud.sermant.router.common.utils.CollectionUtils;
import com.huaweicloud.sermant.router.dubbo.service.LaneContextFilterService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 泳道处理器
 *
 * @author provenceee
 * @since 2023-02-21
 */
public class LaneContextFilterHandler extends AbstractContextFilterHandler {
    private static final int ORDER = 100;

    private final LaneContextFilterService laneContextFilterService;

    /**
     * 构造方法
     */
    public LaneContextFilterHandler() {
        laneContextFilterService = ServiceManager.getService(LaneContextFilterService.class);
    }

    @Override
    public Map<String, List<String>> getRequestTag(String interfaceName, String methodName,
        Map<String, Object> attachments, Object[] args) {
        Set<String> matchTags = configService.getMatchTags();
        if (CollectionUtils.isEmpty(matchTags)) {
            // 染色标记为空，代表没有染色规则，直接return
            return Collections.emptyMap();
        }

        // 上游透传的标记
        Map<String, List<String>> requestTag = getRequestTag(attachments, matchTags);

        // 本次染色标记
        Map<String, List<String>> laneTag = laneContextFilterService
            .getLane(interfaceName, methodName, attachments, args);
        if (CollectionUtils.isEmpty(laneTag)) {
            return requestTag;
        }

        // 如果上游传来的标记中，存在与本次染色相同的标记，以上游传递的为准
        laneTag.forEach(requestTag::putIfAbsent);
        return requestTag;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
