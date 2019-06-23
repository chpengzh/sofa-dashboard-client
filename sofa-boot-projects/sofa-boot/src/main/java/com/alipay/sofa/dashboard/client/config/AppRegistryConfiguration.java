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

import com.alipay.sofa.dashboard.client.listener.SofaDashboardContextClosedListener;
import com.alipay.sofa.dashboard.client.listener.SofaDashboardAppStartListener;
import com.alipay.sofa.dashboard.client.model.common.Application;
import com.alipay.sofa.dashboard.client.properties.SofaDashboardProperties;
import com.alipay.sofa.dashboard.client.registry.AppPublisher;
import com.alipay.sofa.dashboard.client.registry.ZookeeperAppPublisher;
import com.alipay.sofa.dashboard.client.registry.ZookeeperRegistryConfig;
import com.alipay.sofa.dashboard.client.utils.NetworkAddressUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({ SofaDashboardProperties.class })
@ConditionalOnProperty(prefix = "com.alipay.sofa.dashboard.client", value = "enable", matchIfMissing = true)
public class AppRegistryConfiguration {

    private static final String KEY_SPRING_APP_NAME = "spring.application.name";

    private static final String KEY_SERVER_PORT     = "server.port";

    private static final String DEFAULT_SERVER_PORT = "8080";

    @Bean
    @ConditionalOnMissingBean
    public Application getApplicationInstance(Environment env, SofaDashboardProperties prop) {
        long current = System.currentTimeMillis();

        Application app = new Application();
        app.setAppName(env.getRequiredProperty(KEY_SPRING_APP_NAME));
        app.setHostName(getLocalIp(prop));
        app.setPort(Integer.parseInt(env.getProperty(KEY_SERVER_PORT, DEFAULT_SERVER_PORT)));
        app.setStartTime(current);
        app.setLastRecover(current);
        return app;
    }

    @Bean
    @ConditionalOnMissingBean
    public AppPublisher getAppInstanceRegistry(SofaDashboardProperties prop) {
        ZookeeperRegistryConfig config = new ZookeeperRegistryConfig();
        config.setAddress(prop.getZookeeper().getAddress());
        config.setBaseSleepTimeMs(prop.getZookeeper().getBaseSleepTimeMs());
        config.setMaxRetries(prop.getZookeeper().getMaxRetries());
        config.setSessionTimeoutMs(prop.getZookeeper().getSessionTimeoutMs());
        config.setConnectionTimeoutMs(prop.getZookeeper().getConnectionTimeoutMs());

        ZookeeperAppPublisher registry = new ZookeeperAppPublisher();
        registry.start(config);
        return registry;
    }

    @Bean
    @ConditionalOnMissingBean
    public SofaDashboardContextClosedListener getContextCloseListener() {
        return new SofaDashboardContextClosedListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public SofaDashboardAppStartListener getContextRefreshedListener() {
        return new SofaDashboardAppStartListener();
    }

    private String getLocalIp(SofaDashboardProperties properties) {
        NetworkAddressUtils.calculate(null, null);
        if (StringUtils.isEmpty(properties.getClient().getInstanceIp())) {
            return NetworkAddressUtils.getLocalIP();
        } else {
            return properties.getClient().getInstanceIp();
        }
    }
}
