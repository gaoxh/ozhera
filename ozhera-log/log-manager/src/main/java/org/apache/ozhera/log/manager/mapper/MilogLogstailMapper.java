/*
 * Copyright (C) 2020 Xiaomi Corporation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.ozhera.log.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ozhera.log.manager.model.pojo.MilogLogstailDO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper
 * </p>
 *
 * @author wanghaoyang
 * @since 2022-01-10
 */
public interface MilogLogstailMapper extends BaseMapper<MilogLogstailDO> {
    /**
     * Obtain the tail used to count the number of log entries
     * @return
     */
    List<Map<String, Object>> getAllTailForCount();
}