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
package com.alipay.sofa.dashboard.client.store;

import com.alipay.sofa.dashboard.client.model.dimension.DimensionName;
import com.alipay.sofa.dashboard.client.model.dimension.StoreRecord;
import com.alipay.sofa.dashboard.client.model.memory.MemoryDescriptor;
import com.alipay.sofa.dashboard.client.model.thread.ThreadSummaryDescriptor;
import com.alipay.sofa.dashboard.client.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public class MySQLDimensionStoreTest {

    @Test
    public void basicTest() throws SQLException {
        DimensionStore<?> store = new MySQLDimensionStore(DashboardMySQLStoreConfig.newBuilder()
            .url(generateDB()).build());

        // Create schemes
        Set<String> schemes = new HashSet<String>() {
            {
                add(DimensionName.THREAD_SUMMARY);
                add(DimensionName.MEMORY);
            }
        };
        store.createTablesIfNotExists(schemes);

        // Add a new records
        StoreRecord record = StoreRecord.newBuilder().schemeName(DimensionName.THREAD_SUMMARY)
            .timestamp(System.currentTimeMillis())
            .value(JsonUtils.toJsonString(new ThreadSummaryDescriptor())).build();
        store.addRecords(true, new ArrayList<StoreRecord>() {
            {
                add(record);
            }
        });

        // Query records
        List<StoreRecord> query = store.getLatestRecords(DimensionName.THREAD_SUMMARY, 0, 10);
        Assert.assertEquals(query.size(), 1);
        Assert.assertEquals(query.get(0), record);
    }

    private String generateDB() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Object refObject = new Object() {
            {
            }
        };
        String currentClassName = refObject.getClass().getEnclosingClass().getName();
        String currentMethodName = refObject.getClass().getEnclosingMethod().getName();

        String invokeMethodName = null;
        for (int i = 0; i < stackTraceElements.length - 1; i++) {
            if (Objects.equals(stackTraceElements[i].getMethodName(), currentMethodName)) {
                invokeMethodName = stackTraceElements[i + 1].getMethodName();
                break;
            }
        }

        String suffix = UUID.randomUUID().toString().substring(0, 6);
        return String.format("jdbc:h2:./target/test_db/%s_%s_%s", currentClassName,
            invokeMethodName, suffix);
    }
}
