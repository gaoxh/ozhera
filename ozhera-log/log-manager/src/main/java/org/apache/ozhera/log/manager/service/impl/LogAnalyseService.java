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
package org.apache.ozhera.log.manager.service.impl;

import org.apache.ozhera.log.common.Result;
import org.apache.ozhera.log.exception.CommonError;
import org.apache.ozhera.log.manager.common.context.MoneUserContext;
import org.apache.ozhera.log.manager.dao.MilogLogstoreDao;
import org.apache.ozhera.log.manager.domain.AnalyseLog;
import org.apache.ozhera.log.manager.mapper.MilogAnalyseDashboardGraphRefMapper;
import org.apache.ozhera.log.manager.mapper.MilogAnalyseDashboardMapper;
import org.apache.ozhera.log.manager.mapper.MilogAnalyseGraphMapper;
import org.apache.ozhera.log.manager.mapper.MilogAnalyseGraphTypeMapper;
import org.apache.ozhera.log.manager.model.convert.DGRefConvert;
import org.apache.ozhera.log.manager.model.convert.DashboardConvert;
import org.apache.ozhera.log.manager.model.convert.GraphConvert;
import org.apache.ozhera.log.manager.model.convert.GraphTypeConvert;
import org.apache.ozhera.log.manager.model.dto.*;
import org.apache.ozhera.log.manager.model.pojo.*;
import org.apache.ozhera.log.manager.model.vo.*;
import com.xiaomi.youpin.docean.anno.Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.ozhera.log.manager.model.dto.*;
import org.apache.ozhera.log.manager.model.pojo.*;
import org.apache.ozhera.log.manager.model.vo.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LogAnalyseService {
    @Resource
    private MilogAnalyseDashboardGraphRefMapper dgRefMapper;

    @Resource
    private MilogAnalyseDashboardMapper dashboardMapper;

    @Resource
    private MilogAnalyseGraphMapper graphMapper;

    @Resource
    private MilogAnalyseGraphTypeMapper graphTypeMapper;

    @Resource
    private AnalyseLog analyseLog;

    @Resource
    private MilogLogstoreDao logstoreDao;

    private static final List<String> ignoreKeyList = new ArrayList<>();

    {
        ignoreKeyList.add("message");
        ignoreKeyList.add("logsource");
        ignoreKeyList.add("timestamp");
        ignoreKeyList.add("traceId");
        ignoreKeyList.add("line");
    }

    public Result<DashboardDTO> getDashboardGraph(LogAnalyseQuery logAnalyseQuery) {
        List<MilogAnalyseDashboardDO> dashboardList = dashboardMapper.getByStoreId(logAnalyseQuery.getStoreId());
        if (dashboardList == null || dashboardList.isEmpty()) {
            return Result.success();
        }
        MilogAnalyseDashboardDO dashboard = dashboardList.get(0);
        List<DashboardGraphDTO> dashboardGraphList = graphMapper.getDashboardGraph(dashboard.getId());
        return Result.success(DashboardConvert.INSTANCE.fromDO(dashboard, dashboardGraphList));
    }

    public Result<Long> createGraph(CreateGraphCmd cmd) {
        MilogAnalyseGraphDO graphDO = GraphConvert.INSTANCE.toDO(cmd);
        graphDO.setCreator(MoneUserContext.getCurrentUser().getUser());
        graphDO.setUpdater(MoneUserContext.getCurrentUser().getUser());
        long thisTime = System.currentTimeMillis();
        graphDO.setCreateTime(thisTime);
        graphDO.setUpdateTime(thisTime);
        graphMapper.insert(graphDO);
        return Result.success(graphDO.getId());
    }

    public Result<Boolean> updateGraph(UpdateGraphCmd cmd) {
        MilogAnalyseGraphDO graphDO = GraphConvert.INSTANCE.toDO(cmd);
        graphDO.setUpdater(MoneUserContext.getCurrentUser().getUser());
        graphDO.setUpdateTime(System.currentTimeMillis());
        int res = graphMapper.updateById(graphDO);
        return res == 1 ? Result.success(true) : Result.fail(CommonError.ParamsError);
    }

    public Result<Boolean> deleteGraph(Long graphId) {
        graphMapper.deleteById(graphId);
        dgRefMapper.deleteGraphRef(graphId);
        return Result.success(true);
    }

    public Result<List<GraphDTO>> searchGraph(GraphQuery query) {
        List<GraphDTO> graphList = graphMapper.search(query);
        return Result.success(graphList);
    }

    public Result<Boolean> ref(DGRefCmd cmd) {
        Long refed = dgRefMapper.isRefed(cmd.getDashboardId(), cmd.getGraphId());
        if (refed == null || refed == 0) {
            int res = dgRefMapper.insert(DGRefConvert.INSTANCE.toDo(cmd));
            return res == 1 ? Result.success(true) : Result.fail(CommonError.ParamsError);
        }
        return Result.fail(CommonError.ParamsError.getCode(), "The chart already exists for this dashboard");
    }

    public Result<Boolean> delRef(DGRefDelCmd cmd) {
        int res = dgRefMapper.delRef(cmd.getDashboardId(), cmd.getGraphId());
        return res == 1 ? Result.success(true) : Result.fail(CommonError.ParamsError);
    }

    public Result<Boolean> updateRef(DGRefUpdateCmd cmd) {
        if (cmd.getDashboardId() == null || cmd.getGraphList() == null || cmd.getGraphList().isEmpty()) {
            return Result.fail(CommonError.ParamsError);
        }
        for (DGRefUpdateCmd.DGRefDetailUpdateCmd graph : cmd.getGraphList()) {
            MilogAnalyseDashboardGraphRefDO refOld = dgRefMapper.getRef(cmd.getDashboardId(), graph.getGraphId());
            if (refOld == null) {
                return Result.fail(CommonError.ParamsError);
            }
            MilogAnalyseDashboardGraphRefDO refNew = DGRefConvert.INSTANCE.toDo(graph);
            refNew.setId(refOld.getId());
            dgRefMapper.updateById(refNew);
        }
        return Result.success(true);
    }

    public Result<LogAnalyseDataDTO> data(LogAnalyseDataQuery query) throws IOException {
        LogAnalyseDataDTO dto = analyseLog.getData(query);
        return Result.success(dto);
    }

    public Result<LogAnalyseDataDTO> dataPre(LogAnalyseDataPreQuery query) throws IOException {
        LogAnalyseDataDTO dto = analyseLog.getData(query);
        return Result.success(dto);
    }

    public Result<Long> createDashboard(CreateDashboardCmd cmd) {
        MilogAnalyseDashboardDO dashboardDO = DashboardConvert.INSTANCE.toDO(cmd);
        dashboardDO.setCreateTime(System.currentTimeMillis());
        dashboardDO.setCreator(MoneUserContext.getCurrentUser().getUser());

        // Set the default name, and change it later if you need multiple dashboards
        MilogLogStoreDO logstore = logstoreDao.queryById(cmd.getStoreId());
        if (logstore == null) {
            return Result.failParam("Create failed, logstore does not exist");
        }
        dashboardDO.setName(logstore.getLogstoreName() + "dashboard");

        dashboardMapper.insert(dashboardDO);
        return Result.success(dashboardDO.getId());
    }

    public Result<List<GraphTypeDTO>> type() {
        List<MilogAnalyseGraphTypeDO> gtaphTypeDOList = graphTypeMapper.selectList(null);
        List<GraphTypeDTO> graphTypeDTOList = GraphTypeConvert.INSTANCE.toDTOList(gtaphTypeDOList);
        return Result.success(graphTypeDTOList);
    }


    public Result<List<String>> supportKey(Long storeId) {
        if (storeId == null) {
            return Result.fail(CommonError.ParamsError);
        }
        MilogLogStoreDO logStore = logstoreDao.queryById(storeId);
        if (logStore == null) {
            return Result.fail(CommonError.ParamsError);
        }
        String[] keyDescripArray = logStore.getKeyList().split(",");
        List<String> keyList = new ArrayList<>();
        String key;
        for (String keyDes : keyDescripArray) {
            key = keyDes.split(":")[0];
            if (ignoreKeyList.contains(key)) {
                continue;
            }
            keyList.add(key);
        }
        return Result.success(keyList);
    }

}