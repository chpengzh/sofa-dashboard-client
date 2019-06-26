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
package com.alipay.sofa.dashboard.client.config;

import com.alipay.sofa.dashboard.client.properties.SofaDashboardMySQLProperties;
import com.alipay.sofa.dashboard.client.properties.SofaDashboardProperties;
import com.alipay.sofa.dashboard.client.store.DashboardMySQLStoreConfig;
import com.alipay.sofa.dashboard.client.store.DimensionStore;
import com.alipay.sofa.dashboard.client.store.MySQLDimensionStore;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({ SofaDashboardProperties.class })
@ConditionalOnProperty(prefix = "com.alipay.sofa.dashboard.client", value = "enable", matchIfMissing = true)
public class DimensionStoreConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "com.alipay.sofa.dashboard.client.store", value = "mysql", matchIfMissing = true)
    @ConditionalOnClass({ MySQLDimensionStore.class, SqlSessionFactory.class })
    public DimensionStore<?> createMySQLDimensionStore(SofaDashboardProperties prop) {
        SofaDashboardMySQLProperties mysqlProps = prop.getMysql();
        return new MySQLDimensionStore(DashboardMySQLStoreConfig.newBuilder()
            .driver(mysqlProps.getDriver()).url(mysqlProps.getUrl())
            .username(mysqlProps.getUsername()).password(mysqlProps.getPassword())
            .maximumActiveConnections(mysqlProps.getMaximumActiveConnections()).build());
    }

}
