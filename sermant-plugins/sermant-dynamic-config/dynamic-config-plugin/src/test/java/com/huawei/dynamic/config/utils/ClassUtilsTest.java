/*
 * Copyright (C) 2022-2022 Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huawei.dynamic.config.utils;

import com.huawei.dynamic.config.sources.TestConfigSources;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * ClassUtils测试
 *
 * @author zhouss
 * @since 2022-04-16
 */
public class ClassUtilsTest {
    @Test
    public void defineClass() {
        final Optional<Class<?>> aClass = ClassUtils
            .defineClass(TestConfigSources.class.getName(), Thread.currentThread()
                .getContextClassLoader());
        Assert.assertTrue(aClass.isPresent());
        Assert.assertFalse(ClassUtils.defineClass(null, null).isPresent());
    }
}
