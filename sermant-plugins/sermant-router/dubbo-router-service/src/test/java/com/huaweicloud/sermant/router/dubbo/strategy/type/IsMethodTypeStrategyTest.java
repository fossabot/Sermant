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

package com.huaweicloud.sermant.router.dubbo.strategy.type;

import com.huaweicloud.sermant.router.dubbo.strategy.TypeStrategy;

import org.junit.Assert;
import org.junit.Test;

/**
 * enabled匹配策略测试
 *
 * @author provenceee
 * @since 2021-12-01
 */
public class IsMethodTypeStrategyTest {
    /**
     * 测试enabled策略
     */
    @Test
    public void testValue() {
        TypeStrategy strategy = new IsMethodTypeStrategy();
        Entity entity = new Entity();
        entity.setEnabled(true);
        entity.setExist(true);

        // 正常情况
        Assert.assertEquals(Boolean.TRUE.toString(), strategy.getValue(entity, ".isEnabled()").orElse(null));

        // 测试找不到方法
        Assert.assertEquals(Boolean.FALSE.toString(), strategy.getValue(entity, ".foo()").orElse(null));

        // 测试null
        Assert.assertNull(strategy.getValue(new Entity(), ".isEnabled()").orElse(null));

        // 正常情况
        Assert.assertEquals(Boolean.TRUE.toString(), strategy.getValue(entity, ".isExist()").orElse(null));

        // 测试找不到方法
        Assert.assertEquals(Boolean.FALSE.toString(), strategy.getValue(entity, ".foo()").orElse(null));

        // 测试null
        Assert.assertEquals(Boolean.FALSE.toString(), strategy.getValue(new Entity(), ".isExist()").orElse(null));
    }
}