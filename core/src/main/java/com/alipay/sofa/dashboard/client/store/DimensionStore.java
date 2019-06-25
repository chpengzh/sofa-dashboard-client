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

import com.alipay.sofa.dashboard.client.model.dimension.DimensionRecord;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public interface DimensionStore<CFG> {

    /**
     * Get store's configuration.
     *
     * @return configuration model
     */
    CFG getStoreConfig();

    /**
     * Prepare tables
     *
     * @param dimensionSchemes scheme definitions
     */
    void createTablesIfNotExists(Map<String, Class> dimensionSchemes);

    /**
     * Append a new record
     *
     * @param records dimension record to store
     */
    void addRecords(List<DimensionRecord> records);

    /**
     * Query latest records
     *
     * @param dimensionName dimension name
     * @param offset        query offset
     * @param limit         query size limit
     */
    List<DimensionRecord> getLatestRecords(String dimensionName, int offset, int limit)
                                                                                       throws SQLException;

}
