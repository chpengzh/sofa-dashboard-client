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
package com.alipay.sofa.dashboard.client.io;

import com.alipay.sofa.dashboard.client.model.io.StoreRecord;

import java.util.List;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public interface RecordExporter<CFG> {

    /**
     * Get store's configuration.
     *
     * @return configuration model
     */
    CFG getStoreConfig();

    /**
     * Query latest records
     *
     * @param instanceId    instanceId
     * @param dimensionName dimensionName
     * @param offset        query offset
     * @param limit         query size limit
     */
    List<StoreRecord> getLatestRecords(String instanceId, String dimensionName, int offset,
                                       int limit);
}
