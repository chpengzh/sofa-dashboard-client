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
@ConfigurationProperties(prefix = "com.alipay.sofa.dashboard.mysql")
public class SofaDashboardMySQLProperties {

    /**
     * JDBC Driver
     */
    private String driver                   = "com.mysql.jdbc.Driver";

    /**
     * MySQL数据库连接地址
     */
    private String url                      = "jdbc:mysql://127.0.0.1:3306/sofa_dashboard_client";

    /**
     * MySQL连接用户名
     */
    private String username                 = "root";

    /**
     * MySQL连接用户秘钥
     */
    private String password                 = "";

    /**
     * 最大保持连接数
     */
    private int    maximumActiveConnections = 10;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaximumActiveConnections() {
        return maximumActiveConnections;
    }

    public void setMaximumActiveConnections(int maximumActiveConnections) {
        this.maximumActiveConnections = maximumActiveConnections;
    }

    @Override
    public String toString() {
        return "SofaDashboardMySQLProperties{" + "driver='" + driver + '\'' + ", url='" + url
               + '\'' + ", username='" + username + '\'' + ", password='" + password + '\''
               + ", maximumActiveConnections=" + maximumActiveConnections + '}';
    }
}
