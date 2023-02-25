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

package com.huawei.discovery.service.lb.stats;

import com.huawei.discovery.config.LbConfig;
import com.huawei.discovery.entity.Recorder;

import com.huaweicloud.sermant.core.plugin.config.PluginConfigManager;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 记录当前实例的指标数据
 *
 * @author zhouss
 * @since 2022-09-26
 */
public class InstanceStats implements Recorder {
    /**
     * 正在请求的数量
     */
    private final AtomicLong activeRequests = new AtomicLong();

    /**
     * 所有的请求数统计
     */
    private final AtomicLong allRequestCount = new AtomicLong();

    /**
     * 所有请求调用的消耗时间
     */
    private final AtomicLong allRequestConsumeTime = new AtomicLong();

    /**
     * 请求失败数
     */
    private final AtomicLong failRequestCount = new AtomicLong();

    /**
     * 时间窗口
     */
    private final long instanceStateTimeWindowMs;

    /**
     * 上一次时间窗口更新时间, 作为时间窗口的左边界
     */
    private volatile long lastLeftWindowTime;

    /**
     * 时间窗口内的平均响应时间
     */
    private volatile double responseAvgTime;

    /**
     * 构造器
     */
    public InstanceStats() {
        this.instanceStateTimeWindowMs =
                PluginConfigManager.getPluginConfig(LbConfig.class).getInstanceStatTimeWindowMs();
        lastLeftWindowTime = System.currentTimeMillis();
    }

    /**
     * 调用前请求
     */
    @Override
    public void beforeRequest() {
        activeRequests.incrementAndGet();
        allRequestCount.incrementAndGet();
    }

    /**
     * 异常调用统计
     *
     * @param ex 异常类型
     */
    @Override
    public void errorRequest(Throwable ex, long consumeTimeMs) {
        baseStats(consumeTimeMs);
        failRequestCount.incrementAndGet();
    }

    /**
     * 结果调用
     */
    @Override
    public void afterRequest(long consumeTimeMs) {
        baseStats(consumeTimeMs);
    }

    private void calculateResponseAvgTime() {
        final long allConsumeTime = allRequestConsumeTime.get();
        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastLeftWindowTime >= this.instanceStateTimeWindowMs) {
            lastLeftWindowTime = currentTimeMillis;
            allRequestCount.set(0);
            allRequestConsumeTime.set(0);
        }
        this.responseAvgTime = allRequestCount.get() == 0 ? 0 : (allConsumeTime * 1d / allRequestCount.get());
    }

    private void baseStats(long consumeTimeMs) {
        final long request = activeRequests.decrementAndGet();
        if (request < 0) {
            activeRequests.set(0);
        }
        allRequestConsumeTime.addAndGet(consumeTimeMs);
        this.calculateResponseAvgTime();
    }

    /**
     * 结束请求
     */
    @Override
    public void completeRequest() {
    }

    public AtomicLong getAllRequestCount() {
        return allRequestCount;
    }

    public AtomicLong getAllRequestConsumeTime() {
        return allRequestConsumeTime;
    }

    /**
     * 获取并发数
     *
     * @return 并发数
     */
    public long getActiveRequests() {
        final long activeCount = activeRequests.get();
        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastLeftWindowTime >= this.instanceStateTimeWindowMs) {
            lastLeftWindowTime = currentTimeMillis;
            this.activeRequests.set(0);
            return 0;
        }
        return activeCount;
    }

    public AtomicLong getFailRequestCount() {
        return failRequestCount;
    }

    /**
     * 获取平均响应时间
     *
     * @return responseAvgTime
     */
    public double getResponseAvgTime() {
        return responseAvgTime;
    }
}
