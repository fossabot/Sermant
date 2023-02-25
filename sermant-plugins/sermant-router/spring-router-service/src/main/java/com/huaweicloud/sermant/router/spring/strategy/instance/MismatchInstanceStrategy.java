/*
 * Copyright (C) 2021-2022 Huawei Technologies Co., Ltd. All rights reserved.
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

package com.huaweicloud.sermant.router.spring.strategy.instance;

import com.huaweicloud.sermant.router.config.strategy.AbstractInstanceStrategy;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

/**
 * 匹配不在mismatch中的实例
 *
 * @param <I> 实例泛型
 * @author provenceee
 * @since 2021-12-08
 */
public class MismatchInstanceStrategy<I> extends AbstractInstanceStrategy<I, List<Map<String, String>>> {
    /**
     * 匹配不在mismatch中的实例
     *
     * @param instance 实例
     * @param tags 没有匹配上的标签
     * @param mapper 获取metadata的方法
     * @return 是否匹配
     */
    @Override
    public boolean isMatch(I instance, List<Map<String, String>> tags, Function<I, Map<String, String>> mapper) {
        // 由于mismatch里面的标签已经匹配过了且没有匹配上，所以要剔除掉，不能参与负载均衡，否则会导致流量比例不正确（会偏高）
        Map<String, String> metadata = getMetadata(instance, mapper);
        for (Map<String, String> mismatchTag : tags) {
            for (Entry<String, String> entry : mismatchTag.entrySet()) {
                String value = entry.getValue();
                String key = entry.getKey();

                // value为null时，要把含有该标签的全都过滤掉
                if (value == null) {
                    if (metadata.containsKey(key)) {
                        return false;
                    } else {
                        continue;
                    }
                }
                if (Objects.equals(metadata.get(key), value)) {
                    return false;
                }
            }
        }
        return true;
    }
}