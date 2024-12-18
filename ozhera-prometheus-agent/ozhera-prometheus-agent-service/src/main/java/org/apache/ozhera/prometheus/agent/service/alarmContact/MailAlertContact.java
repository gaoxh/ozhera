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

package org.apache.ozhera.prometheus.agent.service.alarmContact;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.apache.ozhera.prometheus.agent.Impl.RuleAlertDao;
import org.apache.ozhera.prometheus.agent.entity.RuleAlertEntity;
import org.apache.ozhera.prometheus.agent.result.alertManager.AlertManagerFireResult;
import org.apache.ozhera.prometheus.agent.result.alertManager.Alerts;
import org.apache.ozhera.prometheus.agent.result.alertManager.CommonLabels;
import org.apache.ozhera.prometheus.agent.result.alertManager.GroupLabels;
import org.apache.ozhera.prometheus.agent.util.FreeMarkerUtil;
import org.apache.ozhera.prometheus.agent.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxiaowei6
 */
@Slf4j
@Component
public class MailAlertContact extends BaseAlertContact {
    @Autowired
    RuleAlertDao dao;

    @Autowired
    private MailUtil mailUtil;

    @NacosValue(value = "${hera.alertmanager.url}", autoRefreshed = true)
    private String silenceUrl;

    @Override
    public void Reach(AlertManagerFireResult fireResult) {
        List<Alerts> alerts = fireResult.getAlerts();
        GroupLabels groupLabels = fireResult.getGroupLabels();
        String alertName = groupLabels.getAlertname();
        log.info("SendAlert mailReach begin send AlertName :{}", alertName);

        fireResult.getAlerts().stream().forEach(alert -> {
            try {
                String[] principals = dao.GetRuleAlertAtPeople(alertName);
                if (principals == null) {
                    log.info("SendAlert principals null alertName:{}", alertName);
                    return;
                }
                RuleAlertEntity ruleAlertEntity = dao.GetRuleAlertByAlertName(alert.getLabels().getAlertname());
                int priority = ruleAlertEntity.getPriority();
                Map<String, Object> map = new HashMap<>();
                String priorityStr = "P" + String.valueOf(priority);
                map.put("priority", priorityStr);
                map.put("title", fireResult.getCommonAnnotations().getTitle());
                String alertOp = alert.getLabels().getAlert_op();
                String alertValue = alert.getLabels().getAlert_value();
                if (alertOp == null || alertOp.isEmpty()) {
                    alertOp = "";
                    alertValue = "";
                }
                map.put("alert_op", alertOp);
                map.put("alert_value", alertValue);
                map.put("application", alert.getLabels().getApplication());
                map.put("silence_url", silenceUrl);
                CommonLabels commonLabels = fireResult.getCommonLabels();
                Class clazz = commonLabels.getClass();
                Field[] fields = clazz.getDeclaredFields();
                StringBuilder sb = new StringBuilder();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object fieldValue = null;
                    try {
                        fieldValue = field.get(commonLabels);
                        if (fieldValue == null) {
                            continue;
                        }
                        map.put(fieldName, field.get(commonLabels));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                Map<String, Object> finalMap = transferNames(map);
                filterName(finalMap);
                finalMap.forEach(
                        (k, v) -> {
                            sb.append("<p><strong>").append(k).append("</strong>: ").append(v).append("</p>");
                        });
                String content = sb.toString();
                map.put("content", content);
                String freeMarkerRes = FreeMarkerUtil.getContentExceptJson("/mail", "mailCart.ftl", finalMap);
                String title = String.format("[%s][Hera]  %s %s %s", priorityStr, fireResult.getCommonAnnotations().getTitle(), alert.getLabels().getAlert_op(), alert.getLabels().getAlert_value());
                //send mail
                mailUtil.sendMailToUserArray(new ArrayList<>(Arrays.asList(principals)), title, freeMarkerRes);
            } catch (Exception e) {
                log.error("SendAlert.mailReach error:{}", e);
            }
        });
    }
}