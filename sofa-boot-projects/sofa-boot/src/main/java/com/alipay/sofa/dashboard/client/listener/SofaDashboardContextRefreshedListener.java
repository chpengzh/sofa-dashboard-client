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
package com.alipay.sofa.dashboard.client.listener;

import com.alipay.sofa.dashboard.client.event.SofaDashboardAppAfterStartedEvent;
import com.alipay.sofa.dashboard.client.event.SofaDashboardAppStartEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
@Component
public class SofaDashboardContextRefreshedListener implements
                                                  ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();

        applicationContext.publishEvent(new SofaDashboardAppStartEvent(applicationContext));
        applicationContext.publishEvent(new SofaDashboardAppAfterStartedEvent(applicationContext));
    }

}