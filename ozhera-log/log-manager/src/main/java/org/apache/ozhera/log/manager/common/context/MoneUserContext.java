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
package org.apache.ozhera.log.manager.common.context;

import org.apache.ozhera.log.manager.model.convert.UserConvert;
import org.apache.ozhera.log.manager.user.MoneUser;
import com.xiaomi.mone.tpc.login.vo.AuthUserVo;

/**
 * @author wtt
 * @version 1.0
 * @description
 * @date 2021/9/2 15:58
 */
public class MoneUserContext {

    private static ThreadLocal<MoneUser> currentUserHolder = new ThreadLocal<>();

    public static void setCurrentUser(AuthUserVo user, Boolean isAdmin) {
        MoneUser moneUser = UserConvert.INSTANCE.userAdapter(user);
        moneUser.setIsAdmin(isAdmin);
        currentUserHolder.set(moneUser);
    }

    public static MoneUser getCurrentUser() {
        return currentUserHolder.get();
    }

    public static void clear() {
        currentUserHolder.remove();
    }

}