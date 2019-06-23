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
package com.alipay.sofa.dashboard.client.dimension;

import com.alipay.sofa.dashboard.client.model.info.InfoDescriptor;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public class ActuatorInfoDimension implements ApplicationDimension<InfoDescriptor> {

    private final InfoEndpoint endpoint;

    public ActuatorInfoDimension(InfoEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getName() {
        return DimensionName.INFO;
    }

    @Override
    public Class<InfoDescriptor> getType() {
        return InfoDescriptor.class;
    }

    @Override
    public InfoDescriptor currentValue() {
        Map<String, Object> info = endpoint.info();
        Map<String, Object> flatMapInfo = toFlatMap(null, info);

        //
        // Using a model to wrap map will make it easier to understand
        //
        InfoDescriptor descriptor = new InfoDescriptor();
        descriptor.getInfo().putAll(flatMapInfo);
        return descriptor;
    }

    /**
     * Transform a complex dictionary into flat map
     * Sample input:
     * <pre>
     * {
     *     "a": {
     *         "b": {
     *             "c": "d"
     *         },
     *         "e": "f"
     *     }
     * }
     * </pre>
     * Sample output:
     * <pre>
     * {
     *     "a.b.c": "d",
     *     "a.e": "f"
     * }
     * </pre>
     *
     * @param origin origin map
     * @return flat map
     */
    private Map<String, Object> toFlatMap(String prefix, Map<String, Object> origin) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : origin.entrySet()) {
            String nextKey = StringUtils.isEmpty(prefix) ? entry.getKey() : prefix + "."
                                                                            + entry.getKey();
            if (entry.getValue() instanceof Map) {
                //noinspection unchecked
                Map<String, Object> innerResult = toFlatMap(nextKey,
                    (Map<String, Object>) entry.getValue());
                result.putAll(innerResult);
            } else {
                result.put(nextKey, entry.getValue());
            }
        }
        return result;
    }
}
