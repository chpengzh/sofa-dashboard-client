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
package com.alipay.sofa.dashboard.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
@Component
@ConfigurationProperties(prefix = "com.alipay.sofa.dashboard")
public class SofaDashboardProperties {

    private SofaDashboardClientProperties    client    = new SofaDashboardClientProperties();

    private SofaDashboardZookeeperProperties zookeeper = new SofaDashboardZookeeperProperties();

    private String                           store     = "mysql";

    private SofaDashboardMySQLProperties     mysql     = new SofaDashboardMySQLProperties();

    public SofaDashboardClientProperties getClient() {
        return client;
    }

    public void setClient(SofaDashboardClientProperties client) {
        this.client = client;
    }

    public SofaDashboardZookeeperProperties getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(SofaDashboardZookeeperProperties zookeeper) {
        this.zookeeper = zookeeper;
    }

    public SofaDashboardMySQLProperties getMysql() {
        return mysql;
    }

    public void setMysql(SofaDashboardMySQLProperties mysql) {
        this.mysql = mysql;
    }
}
