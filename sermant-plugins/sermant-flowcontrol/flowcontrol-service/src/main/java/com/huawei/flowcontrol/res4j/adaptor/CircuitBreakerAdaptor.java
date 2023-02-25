/*
 * Copyright (C) 2022-2022 Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huawei.flowcontrol.res4j.adaptor;

import com.huawei.flowcontrol.common.core.rule.CircuitBreakerRule;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.vavr.collection.Map;

import java.util.concurrent.TimeUnit;

/**
 * 适配接口io.github.resilience4j.circuitbreaker.CircuitBreaker, 增加新的字段功能
 *
 * @author zhouss
 * @since 2022-08-09
 */
public class CircuitBreakerAdaptor implements CircuitBreaker {
    private final CircuitBreaker oldCircuitBreaker;

    /**
     * 强制关闭熔断
     */
    private boolean forceClosed = false;

    /**
     * 强制开启熔断
     */
    private boolean forceOpen = false;

    /**
     * 构造器
     *
     * @param oldCircuitBreaker rest4j 原生熔断器
     * @param rule 熔断规则
     */
    public CircuitBreakerAdaptor(CircuitBreaker oldCircuitBreaker, CircuitBreakerRule rule) {
        this.oldCircuitBreaker = oldCircuitBreaker;
        this.forceClosed = rule.isForceClosed();
        this.forceOpen = rule.isForceOpen();
    }

    @Override
    public boolean tryAcquirePermission() {
        return oldCircuitBreaker.tryAcquirePermission();
    }

    @Override
    public void releasePermission() {
        oldCircuitBreaker.releasePermission();
    }

    @Override
    public void acquirePermission() {
        oldCircuitBreaker.acquirePermission();
    }

    @Override
    public void onError(long duration, TimeUnit durationUnit, Throwable throwable) {
        oldCircuitBreaker.onError(duration, durationUnit, throwable);
    }

    @Override
    public void onSuccess(long duration, TimeUnit durationUnit) {
        oldCircuitBreaker.onSuccess(duration, durationUnit);
    }

    @Override
    public void onResult(long duration, TimeUnit durationUnit, Object result) {
        oldCircuitBreaker.onResult(duration, durationUnit, result);
    }

    @Override
    public void reset() {
        oldCircuitBreaker.reset();
    }

    @Override
    public void transitionToClosedState() {
        oldCircuitBreaker.transitionToClosedState();
    }

    @Override
    public void transitionToOpenState() {
        oldCircuitBreaker.transitionToOpenState();
    }

    @Override
    public void transitionToHalfOpenState() {
        oldCircuitBreaker.transitionToHalfOpenState();
    }

    @Override
    public void transitionToDisabledState() {
        oldCircuitBreaker.transitionToDisabledState();
    }

    @Override
    public void transitionToMetricsOnlyState() {
        oldCircuitBreaker.transitionToMetricsOnlyState();
    }

    @Override
    public void transitionToForcedOpenState() {
        oldCircuitBreaker.transitionToForcedOpenState();
    }

    @Override
    public String getName() {
        return oldCircuitBreaker.getName();
    }

    @Override
    public State getState() {
        return oldCircuitBreaker.getState();
    }

    @Override
    public CircuitBreakerConfig getCircuitBreakerConfig() {
        return oldCircuitBreaker.getCircuitBreakerConfig();
    }

    @Override
    public Metrics getMetrics() {
        return oldCircuitBreaker.getMetrics();
    }

    @Override
    public Map<String, String> getTags() {
        return oldCircuitBreaker.getTags();
    }

    @Override
    public EventPublisher getEventPublisher() {
        return oldCircuitBreaker.getEventPublisher();
    }

    @Override
    public long getCurrentTimestamp() {
        return oldCircuitBreaker.getCurrentTimestamp();
    }

    @Override
    public TimeUnit getTimestampUnit() {
        return oldCircuitBreaker.getTimestampUnit();
    }

    public boolean isForceClosed() {
        return forceClosed;
    }

    public void setForceClosed(boolean forceClosed) {
        this.forceClosed = forceClosed;
    }

    public boolean isForceOpen() {
        return forceOpen;
    }

    public void setForceOpen(boolean forceOpen) {
        this.forceOpen = forceOpen;
    }
}
