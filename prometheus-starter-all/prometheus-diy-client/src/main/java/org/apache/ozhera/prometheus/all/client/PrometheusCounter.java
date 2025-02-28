/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ozhera.prometheus.all.client;

import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangxiaowei6
 */
@Slf4j
public class PrometheusCounter implements XmCounter {

    public String[] labelValues;

    public Counter myCounter;

    public String[] labelNames;

    public PrometheusCounter(Counter cb, String[] lns, String[] lvs) {
        this.myCounter = cb;
        this.labelNames = lns;
        this.labelValues = lvs;
    }

    public PrometheusCounter() {

    }

    @Override
    public XmCounter with(String... labelValues) {
        try {
            if (this.labelNames.length != labelValues.length) {
                log.warn("Incorrect numbers of labels : " + myCounter.describe().get(0).name + " labelName: " + this.labelNames.length + " labelValues: " + labelValues.length + "{} {}", Arrays.toString(this.labelNames), Arrays.toString(labelValues));
                return new PrometheusCounter();
            }
            return this;
        } catch (Throwable throwable) {
            log.warn(throwable.getMessage());
            return null;
        }
    }

    @Override
    public void add(double delta, String... labelValues) {

        List<String> mylist = new ArrayList<>(Arrays.asList(labelValues));
        mylist.add(Prometheus.constLabels.get(Metrics.SERVICE));
        // mylist.add(traceId);
        String[] finalValue = mylist.toArray(new String[mylist.size()]);
        try {
            this.myCounter.labels(finalValue).inc(delta);
        } catch (Throwable throwable) {
            log.warn(throwable.getMessage());
        }

    }
}