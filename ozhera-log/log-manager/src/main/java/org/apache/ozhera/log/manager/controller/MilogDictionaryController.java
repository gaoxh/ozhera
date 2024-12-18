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

import org.apache.ozhera.log.common.Result;
import org.apache.ozhera.log.manager.model.bo.MilogDictionaryParam;
import org.apache.ozhera.log.manager.model.dto.DictionaryDTO;
import org.apache.ozhera.log.manager.service.impl.MilogDictionaryServiceImpl;
import com.xiaomi.youpin.docean.anno.Controller;
import com.xiaomi.youpin.docean.anno.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author wtt
 * @version 1.0
 * @description
 * @date 2021/9/26 15:29
 */
@Controller
public class MilogDictionaryController {

    @Resource
    private MilogDictionaryServiceImpl milogDictionaryService;

    @RequestMapping(path = "/milog/dictionary/list", method = "post")
    public Result<Map<Integer, List<DictionaryDTO<?>>>> queryDictionaryList(MilogDictionaryParam codes) {
        return milogDictionaryService.queryDictionaryList(codes);
    }

}
