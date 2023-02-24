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

package com.huaweicloud.sermant.router.config.entity;

import com.huaweicloud.sermant.core.utils.ReflectUtils;
import com.huaweicloud.sermant.core.utils.StringUtils;
import com.huaweicloud.sermant.router.common.constants.RouterConstant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 值匹配反序列化器
 *
 * @author provenceee
 * @since 2022-02-18
 */
public class ValueMatchDeserializer implements ObjectDeserializer {
    private static final String DUBBO_ARGS_FIELD_NAME = "args";

    @Override
    public Map<String, List<MatchRule>> deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONObject args = parser.parseObject();

        // LinkedHashMap用为了保持顺序
        LinkedHashMap<String, List<MatchRule>> matchRuleMap = new LinkedHashMap<>();
        for (String key : args.keySet()) {
            matchRuleMap.put(key, getMatchRuleList(args, key, DUBBO_ARGS_FIELD_NAME.equals(fieldName)));
        }
        return matchRuleMap;
    }

    private List<MatchRule> getMatchRuleList(JSONObject args, String key, boolean isDubboArgs) {
        List<MatchRule> matchRuleList = new ArrayList<>();
        List<JSONObject> array = new ArrayList<>();
        try {
            array = args.getObject(key, new JsonObjectTypeReference());
        } catch (JSONException e) {
            array.add(args.getJSONObject(key));
        }
        for (JSONObject matchRule : array) {
            matchRuleList.add(getMatchRule(matchRule, isDubboArgs));
        }
        return matchRuleList;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    private boolean setField(MatchRule matchRule, String fieldName, Object value) {
        return ReflectUtils.setFieldValue(matchRule, fieldName, value);
    }

    private MatchRule getMatchRule(JSONObject json, boolean isDubboArgs) {
        MatchRule matchRule = new MatchRule();
        ValueMatch valueMatch = new ValueMatch();
        for (Entry<String, Object> entry : json.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();
            if (!setField(matchRule, fieldName, value)) {
                setValueMatchField(valueMatch, fieldName, value);
            }
        }

        // 如果不区分大小写，则强制转为小写
        // 因为boolean型转string是小写，所以如果是.isXxx()的dubbo args类型，则强制把值转为小写，即boolean类型时强制对大小写不敏感
        if (!matchRule.isCaseInsensitive() || (isDubboArgs && isIsMethod(matchRule.getType()))) {
            ListIterator<String> listIterator = valueMatch.getValues().listIterator();
            while (listIterator.hasNext()) {
                String next = listIterator.next();
                if (next != null) {
                    listIterator.set(next.toLowerCase(Locale.ROOT));
                }
            }
        }
        matchRule.setValueMatch(valueMatch);
        return matchRule;
    }

    private void setValueMatchField(ValueMatch valueMatch, String fieldName, Object value) {
        MatchStrategy matchStrategy;
        try {
            matchStrategy = MatchStrategy.valueOf(fieldName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            // 不存在该策略，忽略
            return;
        }
        List<String> values = new ArrayList<>();
        if (MatchStrategy.IN.name().equalsIgnoreCase(fieldName)) {
            values.addAll(((JSONArray) value).toJavaList(String.class));
        } else {
            values.add(String.valueOf(value));
        }
        valueMatch.setMatchStrategy(matchStrategy);
        valueMatch.setValues(values);
    }

    private boolean isIsMethod(String type) {
        return StringUtils.isExist(type) && type.startsWith(RouterConstant.IS_METHOD_PREFIX) && type
            .endsWith(RouterConstant.IS_METHOD_SUFFIX);
    }

    /**
     * JSONObject序列化类
     *
     * @since 2022-02-18
     */
    private static class JsonObjectTypeReference extends TypeReference<ArrayList<JSONObject>> {
    }
}