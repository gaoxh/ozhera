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
package org.apache.ozhera.log.manager.service;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.ozhera.log.manager.model.dto.DictionaryDTO;
import org.apache.ozhera.log.manager.model.pojo.MilogAppMiddlewareRel;
import org.apache.ozhera.log.utils.PinYin4jUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.ozhera.log.common.Constant.COMMON_MQ_PREFIX;
import static org.apache.ozhera.log.common.Constant.COMMON_MQ_SUFFIX;

/**
 * @author wtt
 * @version 1.0
 * @description
 * @date 2021/9/23 11:31
 */
public interface MqConfigService {

    default List<String> generateCommonTagTopicName(String orgId) {
        return IntStream.range(0, COMMON_MQ_SUFFIX.size()).mapToObj(value -> {
            String suffix = COMMON_MQ_SUFFIX.get(value);
            if (StringUtils.isNotBlank(orgId)) {
                return String.format("%s_%s_%s", COMMON_MQ_PREFIX, orgId, suffix);
            }
            return String.format("%s_%s", COMMON_MQ_PREFIX, suffix);
        }).collect(Collectors.toList());
    }

    default String generateSimpleTopicName(Long appId, String appName, String source, Long tailId) {
        if (StringUtils.isNotEmpty(appName)) {
            // Chinese characters are converted to pinyin
            appName = PinYin4jUtils.getAllPinyin(appName);
        }
        // Handle special characters
        List<String> collect = ReUtil.RE_KEYS.stream()
                .map(character -> character.toString()).collect(Collectors.toList());
        String topicName = String.format("%s_%s_%s_%s", appId, appName, tailId, source);
        topicName = StrUtil.removeAny(topicName, collect.toArray(new String[0]));
        return topicName;
    }

    default String generateSimpleTopicName(Long id, String name) {
        if (StringUtils.isNotEmpty(name)) {
            // Chinese characters are converted to pinyin
            name = PinYin4jUtils.getAllPinyin(name);
        }
        // Handle special characters
        List<String> collect = ReUtil.RE_KEYS.stream().map(Object::toString).toList();
        String topicName = String.format("%s_%s", id, name);
        topicName = StrUtil.removeAny(topicName, collect.toArray(new String[0]));
        return topicName;
    }

    MilogAppMiddlewareRel.Config generateConfig(String ak, String sk, String nameServer, String serviceUrl,
                                                String authorization, String orgId, String teamId, Long exceedId,
                                                String name, String source, Long id);

    List<DictionaryDTO> queryExistsTopic(String ak, String sk, String nameServer, String serviceUrl,
                                         String authorization, String orgId, String teamId);

    /**
     * Create several public topics and enable tag filtering
     *
     * @return
     */
    List<String> createCommonTagTopic(String ak, String sk, String nameServer, String serviceUrl,
                                      String authorization, String orgId, String teamId);


    boolean CreateGroup(String ak, String sk, String nameServer);
}
