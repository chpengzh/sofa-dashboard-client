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
import com.alipay.sofa.dashboard.client.model.common.RegistryConfig;

import java.util.List;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public interface AppSubscriber<CFG extends RegistryConfig> {

    /**
     * Startup registry.
     *
     * @return return {@code false} if it is already started
     */
    boolean start(CFG config);

    /**
     * Shutdown registry.
     */
    void shutdown();

    /**
     * String name
     *
     * @return application instance list
     */
    List<Application> getByName(String appName);

}
