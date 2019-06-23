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
package com.alipay.sofa.dashboard.client.health;

import com.alipay.sofa.dashboard.client.event.SofaDashboardAppAfterStartedEvent;
import com.alipay.sofa.dashboard.client.event.SofaDashboardAppStartEvent;
import com.alipay.sofa.healthcheck.startup.ReadinessCheckCallback;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.ApplicationContext;
import org.springframework.core.PriorityOrdered;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public class DashboardAfterHealthCheckCallback implements ReadinessCheckCallback, PriorityOrdered {

    @Override
    public Health onHealthy(ApplicationContext context) {
        Health.Builder builder = new Health.Builder();

        context.publishEvent(new SofaDashboardAppStartEvent(context));
        context.publishEvent(new SofaDashboardAppAfterStartedEvent(context));

        return builder.status(Status.UP).build();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
