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

package com.huawei.dubbo.registry.constants;

/**
 * 常量
 *
 * @author provenceee
 * @since 2022-01-27
 */
public class Constant {
    /**
     * sc注册协议名
     */
    public static final String SC_REGISTRY_PROTOCOL = "sc";

    /**
     * nacos注册协议名
     */
    public static final String NACOS_REGISTRY_PROTOCOL = "nacos";

    /**
     * sc注册ip
     */
    public static final String SC_REGISTRY_IP = "localhost:30100";

    /**
     * 协议分隔符
     */
    public static final String PROTOCOL_SEPARATION = "://";

    /**
     * sc初始化迁移规则
     */
    public static final String SC_INIT_MIGRATION_RULE = "scInit";

    /**
     * sc注册地址
     */
    public static final String SC_REGISTRY_ADDRESS = SC_REGISTRY_PROTOCOL + PROTOCOL_SEPARATION + SC_REGISTRY_IP;

    /**
     * 设置协议方法名
     */
    public static final String SET_PROTOCOL_METHOD_NAME = "setProtocol";

    private Constant() {
    }
}