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
package org.apache.ozhera.log.manager.controller;

import org.apache.ozhera.log.api.model.vo.AgentLogProcessDTO;
import org.apache.ozhera.log.common.Result;
import org.apache.ozhera.log.manager.model.bo.MilogAgentIpParam;
import org.apache.ozhera.log.manager.service.extension.agent.MilogAgentService;
import org.apache.ozhera.log.manager.service.extension.agent.MilogAgentServiceFactory;
import com.xiaomi.youpin.docean.anno.Controller;
import com.xiaomi.youpin.docean.anno.RequestMapping;
import com.xiaomi.youpin.docean.anno.RequestParam;

import java.util.List;

@Controller
public class MIlogAgentManageController {

    private MilogAgentService milogAgentService;

    public void init() {
        milogAgentService = MilogAgentServiceFactory.getAgentExtensionService();
    }

    /**
     * Log Collection Progress
     *
     * @param ip
     * @return
     */
    @RequestMapping(path = "/milog/meta/process", method = "get")
    public Result<List<AgentLogProcessDTO>> process(@RequestParam(value = "ip") String ip) {
        return milogAgentService.process(ip);
    }

    /**
     * Agent distribution configuration - full distribution
     *
     * @param agentId
     * @param agentIp
     * @param agentMachine
     * @return
     */
    @RequestMapping(path = "/milog/agent/config/issue", method = "get")
    public Result<String> configIssueAgent(@RequestParam(value = "agentId") String agentId,
                                           @RequestParam(value = "agentIp") String agentIp,
                                           @RequestParam("agentMachine") String agentMachine) {
        return milogAgentService.configIssueAgent(agentId, agentIp, agentMachine);
    }

    /**
     * @param agentIpParam
     * @return
     */
    @RequestMapping(path = "/milog/agent/deployee")
    public Result<String> agentDeploy(@RequestParam("ips") MilogAgentIpParam agentIpParam) {
        return null;
    }

    /**
     * @param agentIpParam
     * @return
     */
    @RequestMapping(path = "/milog/agent/offline/batch")
    public Result<String> agentOfflineBatch(@RequestParam("param") MilogAgentIpParam agentIpParam) {
        return milogAgentService.agentOfflineBatch(agentIpParam);
    }

    /**
     * @param ip
     * @return
     */
    @RequestMapping(path = "/milog/agent/upgrade")
    public Result<String> agentUpgrade(@RequestParam("ip") String ip) {
        return null;
    }
}
