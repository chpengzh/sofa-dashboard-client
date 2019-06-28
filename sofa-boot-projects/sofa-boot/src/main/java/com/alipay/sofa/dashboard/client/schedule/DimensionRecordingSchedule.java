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
        executors.schedule(new StoreTimerTask(), initDelayExp, TimeUnit.SECONDS);
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
                executors.schedule(this, flushPeriodExp, TimeUnit.SECONDS);

            }
        }
    }
}
