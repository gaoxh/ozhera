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
package org.apache.ozhera.log.manager.domain.analyse;

import org.apache.ozhera.log.manager.model.bo.CalcuAggrParam;
import org.apache.ozhera.log.manager.model.dto.LogAnalyseDataDTO;
import org.apache.ozhera.log.manager.model.dto.LogAnalyseDataFieldDTO;
import com.xiaomi.youpin.docean.anno.Service;
import com.xiaomi.youpin.docean.common.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.*;

import static org.apache.ozhera.log.common.Constant.GSON;

@Service
public class FieldStrategy implements AggrCalcuStrategy {

    @Override
    public AggregationBuilder getAggr(CalcuAggrParam param) {
        if (StringUtils.isEmpty(param.getBead())) {
            return null;
        }

        AggregationBuilder groupAggBuilder = AggregationBuilders.terms("group_aggs")
                .size(Integer.MAX_VALUE)
                .field(param.getBead())
                // If you know that the number of aggregation results in this layer is very small, set it to map to improve performance.
                .executionHint("map");

        return groupAggBuilder;
    }

    @Override
    public LogAnalyseDataDTO formatRes(SearchResponse response) {
        List<Map<String, Object>> dataArray = new ArrayList<>();
        Map<String, Object> ferray;
        if (Objects.nonNull(response) && Objects.equals(response.status(), RestStatus.OK)
                && Objects.nonNull(response.getAggregations()) && Objects.nonNull(response.getAggregations().get("group_aggs"))) {
            Terms groupResult = response.getAggregations().get("group_aggs");
            for (Terms.Bucket groupBucket : groupResult.getBuckets()) {
                ferray = new HashMap<>();
                Object key = groupBucket.getKey();
                long docCount = groupBucket.getDocCount();
                ferray.put("field", key);
                ferray.put("count", docCount);
                dataArray.add(ferray);
            }
        }
        return new LogAnalyseDataFieldDTO(GSON.toJson(dataArray));
    }
}
