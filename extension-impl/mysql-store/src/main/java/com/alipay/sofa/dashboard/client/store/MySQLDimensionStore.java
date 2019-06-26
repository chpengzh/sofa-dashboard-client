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

import com.alipay.sofa.dashboard.client.model.dimension.StoreRecord;
import com.alipay.sofa.dashboard.client.utils.DashboardSQL;
import com.alipay.sofa.dashboard.client.utils.JsonUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public class MySQLDimensionStore implements DimensionStore<DashboardMySQLStoreConfig> {

    private static final String             MYBATIS_CONFIG_PATH = "META-INF/sofa-dashboard/mybatis-config.xml";

    private static final Logger             LOGGER              = LoggerFactory
                                                                    .getLogger(MySQLDimensionStore.class);

    private final SqlSessionFactory         factory;

    private final DashboardMySQLStoreConfig config;

    public MySQLDimensionStore(DashboardMySQLStoreConfig config) {
        this.config = config;

        ClassLoader classLoader = MySQLDimensionStore.class.getClassLoader();
        try (InputStream input = classLoader.getResourceAsStream(MYBATIS_CONFIG_PATH)) {
            Properties properties = config.toProperties();
            factory = new SqlSessionFactoryBuilder().build(input, properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DashboardMySQLStoreConfig getStoreConfig() {
        return this.config;
    }

    @Override
    public void createTablesIfNotExists(Set<String> dimensionSchemes) {
        try (SqlSession session = factory.openSession(true)) {
            for (String tableName : dimensionSchemes) {
                String script = DashboardSQL.get(DashboardSQL.CREATE_TABLE, tableName);
                try {
                    session.getConnection().prepareStatement(script).executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void addRecords(boolean breakIfError, List<StoreRecord> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        try (SqlSession session = factory.openSession(true)) {
            for (StoreRecord record : records) {
                String script = DashboardSQL.get(DashboardSQL.INSERT_ONE, record.getSchemeName());
                try {
                    PreparedStatement statement = session.getConnection().prepareStatement(script);
                    statement.setLong(1, record.getTimestamp());
                    statement.setString(2, record.getValue());
                    statement.executeUpdate();

                } catch (SQLException e) {
                    if (breakIfError) {
                        throw new RuntimeException(e);
                    }
                    LOGGER.warn("Error while execute addRecords.", e);
                }
            }
        }
    }

    @Override
    public List<StoreRecord> getLatestRecords(String dimensionName, int offset, int limit)
                                                                                          throws SQLException {
        String script = DashboardSQL.get(DashboardSQL.QUERY, dimensionName);
        try (SqlSession session = factory.openSession(true)) {
            PreparedStatement statement = session.getConnection().prepareStatement(script);
            statement.setInt(1, limit);
            statement.setInt(2, offset);

            // Execute query
            List<StoreRecord> result = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (!resultSet.isLast()) {
                    resultSet.next();

                    StoreRecord next = new StoreRecord();
                    next.setSchemeName(dimensionName);
                    next.setTimestamp(resultSet.getLong("timestamp"));
                    next.setValue(resultSet.getString("value"));
                    result.add(next);
                }
            }
            return result;
        }
    }
}
