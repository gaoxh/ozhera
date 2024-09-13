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
package org.apache.ozhera.monitor.service.model.prometheus;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaoxihui
 * @date 2021/9/3 10:31 上午
 */
@Data
public class MiLogQuery implements Serializable {

    private Long projectId;
    private String serverIp;
    private String traceId;
    private String generationTime;
    private String level;

}