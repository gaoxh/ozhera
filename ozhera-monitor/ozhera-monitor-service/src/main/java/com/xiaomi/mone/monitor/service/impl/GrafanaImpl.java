/*
 *  Copyright (C) 2020 Xiaomi Corporation
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.xiaomi.mone.monitor.service.impl;

import com.xiaomi.mone.monitor.service.Grafana;
import com.xiaomi.mone.monitor.service.GrafanaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wodiwudi
 */
@Slf4j
@Service(registry = "registryConfig",interfaceClass = Grafana.class, retries = 0,group = "${dubbo.group}")
public class GrafanaImpl implements Grafana {

    @Autowired
    GrafanaService grafanaService;

    /**
     * 调用grafana接口
     */
    @Override
    public String  requestGrafana(String group,String title,String area) {
        return grafanaService.requestGrafana(group,title,area);
    }
}
