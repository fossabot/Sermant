/*
 * Copyright (C) 2022-2022 Huawei Technologies Co., Ltd. All rights reserved.
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

package com.huaweicloud.sermant.router.spring.service;

import com.huaweicloud.sermant.core.plugin.service.PluginService;
import com.huaweicloud.sermant.router.common.request.RequestData;

import java.util.List;

/**
 * BaseLoadBalancerInterceptor服务
 *
 * @author provenceee
 * @since 2022-07-20
 */
public interface LoadBalancerService extends PluginService {
    /**
     * 获取目标实例
     *
     * @param targetName  目标服务
     * @param instances   实例
     * @param requestData 请求数据
     * @return 目标实例
     */
    List<Object> getTargetInstances(String targetName, List<Object> instances, RequestData requestData);
}
