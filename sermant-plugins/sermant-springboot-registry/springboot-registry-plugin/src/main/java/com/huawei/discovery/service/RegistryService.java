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

package com.huawei.discovery.service;

import com.huawei.discovery.entity.ServiceInstance;

import com.huaweicloud.sermant.core.plugin.service.PluginService;

/**
 * 注册
 *
 * @author zhouss
 * @since 2022-09-27
 */
public interface RegistryService extends PluginService {
    /**
     * 注册
     *
     * @param serviceInstance 实例信息
     */
    void registry(ServiceInstance serviceInstance);

    /**
     * 关闭服务发现, 从注册中心下线
     */
    void shutdown();
}
