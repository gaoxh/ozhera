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
package org.apache.ozhera.log.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Corresponds to the logtail configuration, and one logtail corresponds to an RMQ topic
 */
@Data
@EqualsAndHashCode
public class LogtailConfig {
    private Long logtailId;

    private String ak;
    private String sk;
    private String clusterInfo;
    @EqualsAndHashCode.Exclude
    private String consumerGroup;
    private String topic;
    private String tag;
    private String type;
    private Integer appType;

    private String deploySpace;

    private Integer parseType;

    @EqualsAndHashCode.Exclude
    private String tail;
    /**
     * Log delimiter
     */
    private String parseScript;
    private String valueList;

}
