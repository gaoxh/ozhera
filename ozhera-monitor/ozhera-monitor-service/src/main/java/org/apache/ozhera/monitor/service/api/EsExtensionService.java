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
package org.apache.ozhera.monitor.service.api;

import com.xiaomi.mone.es.EsClient;
import org.apache.ozhera.monitor.result.Result;
import org.apache.ozhera.monitor.service.model.middleware.DbInstanceQuery;
import org.apache.ozhera.monitor.service.model.prometheus.MetricDetailQuery;

import java.io.IOException;

public interface EsExtensionService {

    String getIndex(MetricDetailQuery param);

    EsClient getEsClient(Integer appSource);

    Result queryMiddlewareInstance(DbInstanceQuery param, Integer page, Integer pageSize, Long esQueryTimeout) throws IOException;

    /**
     * The domain of the abnormal trace, is used for querying related trace lists for metrics
     * @param platForm
     * @return
     */
    String getExceptionTraceDomain(Integer platForm);
}