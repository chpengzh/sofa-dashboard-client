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

import java.io.Serializable;
import java.util.Properties;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public class DashboardMySQLStoreConfig implements Serializable {

    private static final int serialVersionUID         = 0x11;

    private String           driver                   = "com.mysql.jdbc.Driver";

    private String           url                      = "jdbc:mysql://127.0.0.1:3306/sofa_dashboard_client";

    private String           username                 = "root";

    private String           password                 = "";

    private int              maximumActiveConnections = 10;

    public DashboardMySQLStoreConfig() {
    }

    private DashboardMySQLStoreConfig(Builder builder) {
        setDriver(builder.driver);
        setUrl(builder.url);
        setUsername(builder.username);
        setPassword(builder.password);
        setMaximumActiveConnections(builder.maximumActiveConnections);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Properties toProperties() {
        Properties properties = new Properties();
        properties.put("driver", driver);
        properties.put("url", url);
        properties.put("username", username);
        properties.put("password", password);
        properties.put("maximumActiveConnections", maximumActiveConnections);
        return properties;
    }

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

    public static final class Builder {

        private String driver                   = "com.mysql.jdbc.Driver";

        private String url                      = "jdbc:mysql://127.0.0.1:3306/sofa_dashboard_client";

        private String username                 = "root";

        private String password                 = "";

        private int    maximumActiveConnections = 10;

        private Builder() {
        }

        public Builder driver(String driver) {
            this.driver = driver;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder maximumActiveConnections(int maximumActiveConnections) {
            this.maximumActiveConnections = maximumActiveConnections;
            return this;
        }

        public DashboardMySQLStoreConfig build() {
            return new DashboardMySQLStoreConfig(this);
        }
    }
}
