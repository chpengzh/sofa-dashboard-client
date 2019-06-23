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

import com.alipay.sofa.dashboard.client.model.common.Application;
import com.alipay.sofa.dashboard.client.utils.JsonUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public class ZookeeperAppPublisher extends ZookeeperRegistryBase
                                                                implements
                                                                AppPublisher<ZookeeperRegistryConfig> {

    private static final String  SOFA_BOOT_CLIENT_ROOT     = "apps";

    private static final String  SOFA_BOOT_CLIENT_INSTANCE = "instance";

    private static final Logger  LOGGER                    = LoggerFactory
                                                               .getLogger(ZookeeperAppPublisher.class);

    private volatile Application cachedInstance;

    @Override
    public synchronized void register(Application instance) throws Exception {
        this.cachedInstance = instance;

        Stat stat = getCuratorClient().checkExists().forPath(SOFA_BOOT_CLIENT_ROOT);
        if (stat == null) {
            getCuratorClient().create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT).forPath(SOFA_BOOT_CLIENT_ROOT);
        }
        byte[] bytes = JsonUtils.toJsonBytes(instance);
        String sessionNode = toSessionNode(instance);
        getCuratorClient().create().creatingParentContainersIfNeeded()
            .withMode(CreateMode.EPHEMERAL).forPath(sessionNode, bytes);
    }

    @Override
    public synchronized void unRegister(Application instance) {
        if (Objects.equals(cachedInstance, instance)) {
            cachedInstance = null;
        }

        String sessionNode = toSessionNode(instance);
        try {
            getCuratorClient().delete().forPath(sessionNode);
        } catch (Exception e) {
            LOGGER.error("Failed to deregister application to zookeeper.", e);
        }
    }

    /**
     * Convert an instance definition into session node name
     *
     * @param instance application instance
     * @return session node name
     */
    private String toSessionNode(Application instance) {
        String appId = String.format("%s:%d?startTime=%d&lastRecover=%d&state=%s",
            instance.getHostName(), instance.getPort(), instance.getStartTime(),
            instance.getLastRecover(), instance.getAppState());
        String appName = instance.getAppName();
        return String.format("/%s/%s/%s/%s", SOFA_BOOT_CLIENT_ROOT, SOFA_BOOT_CLIENT_INSTANCE,
            appName, appId);
    }

    @Override
    void onReconnected() {
        Application instance = this.cachedInstance;
        if (instance != null) {
            LOGGER.info("Try to recover session node");
            try {
                register(instance);
            } catch (Exception e) {
                LOGGER.error("Recover session error", e);
            }
        }
    }
}
