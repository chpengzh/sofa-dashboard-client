/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.dashboard.client.schedule;

import com.alipay.sofa.dashboard.client.dimension.ApplicationDimension;
import com.alipay.sofa.dashboard.client.model.dimension.StoreRecord;
import com.alipay.sofa.dashboard.client.store.DimensionStore;
import com.alipay.sofa.dashboard.client.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public class DimensionRecordingSchedule implements InitializingBean {

    private static final Logger              LOGGER = LoggerFactory
                                                        .getLogger(DimensionRecordingSchedule.class);

    private final List<ApplicationDimension> dimensions;

    private final DimensionStore<?>          store;

    private final ScheduledExecutorService   executors;

    private final long                       initDelayExp;

    private final long                       flushPeriodExp;

    private final Random                     random = new Random();

    public DimensionRecordingSchedule(List<ApplicationDimension> dimensions,
                                      DimensionStore<?> store, long initDelayExp,
                                      long flushPeriodExp) {
        this.dimensions = dimensions;
        this.store = store;
        this.initDelayExp = initDelayExp;
        this.flushPeriodExp = flushPeriodExp;
        this.executors = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
    }

    @Override
    public void afterPropertiesSet() {
        int nextDelay = calculateNextScheduleTime(initDelayExp).intValue();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Schedule next store in {} second", nextDelay);
        }
        executors.schedule(new StoreTimerTask(), nextDelay, TimeUnit.SECONDS);
    }

    /**
     * 计算下一个上报时间.
     *
     * @param exp 期望值
     * @return 下一次上报时间
     */
    private Double calculateNextScheduleTime(double exp) {
        int count = 0;
        double variance = exp / 2;
        double minimal = exp / 3;
        do {
            double result = Math.sqrt(variance) * random.nextGaussian() + exp;
            if (result > minimal) {
                return result;
            }
            ++count;
        } while (count < 10);
        return exp;
    }

    private class StoreTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                long current = System.currentTimeMillis();

                List<StoreRecord> records = new ArrayList<>();
                for (ApplicationDimension dimension : dimensions) {
                    StoreRecord record = new StoreRecord();
                    record.setTimestamp(current);
                    record.setSchemeName(dimension.getName());
                    record.setValue(JsonUtils.toJsonString(dimension.currentValue()));
                    records.add(record);
                }
                store.addRecords(false, records);

            } catch (Throwable err) {
                LOGGER.warn("Unable to flush dimension record", err);

            } finally {
                int nextDelay = calculateNextScheduleTime(flushPeriodExp).intValue();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Schedule next store in {} second", nextDelay);
                }
                executors.schedule(this, nextDelay, TimeUnit.SECONDS);

            }
        }
    }
}
