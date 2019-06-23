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
package com.alipay.sofa.dashboard.client.registry;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
abstract class ZookeeperRegistryBase {

    private static final Logger       LOGGER   = LoggerFactory
                                                   .getLogger(ZookeeperRegistryBase.class);

    private final AtomicBoolean       start    = new AtomicBoolean(false);

    private final AtomicBoolean       shutdown = new AtomicBoolean(false);

    private volatile CuratorFramework curatorClient;

    /**
     * On reconeecte
     */
    abstract void onReconnected();

    final CuratorFramework getCuratorClient() {
        return curatorClient;
    }

    public boolean start(ZookeeperRegistryConfig config) {
        if (start.compareAndSet(false, true)) {
            // custom policy
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(
                config.getBaseSleepTimeMs(),
                config.getMaxRetries());
            // to build curatorClient
            curatorClient = CuratorFrameworkFactory.builder()
                .connectString(config.getAddress())
                .sessionTimeoutMs(config.getSessionTimeoutMs())
                .connectionTimeoutMs(config.getConnectionTimeoutMs())
                .retryPolicy(retryPolicy).build();

            // Recreate session while connection recovered
            curatorClient.getConnectionStateListenable().addListener((client, newState) -> {
                if (newState == ConnectionState.RECONNECTED) {
                    onReconnected();
                }
            });

            curatorClient.start();
            return true;
        }
        // Since registry is already started
        return false;
    }

    public void shutdown() {
        if (shutdown.compareAndSet(false, true)) {
            curatorClient.close();
        }
    }
}
