/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.geekidea.stock.service;

import io.geekidea.stock.entity.UserInfo;
import io.geekidea.framework.common.service.BaseService;
import io.geekidea.stock.dto.query.UserInfoQuery;
import io.geekidea.stock.dto.vo.UserInfoQueryVo;
import io.geekidea.framework.common.vo.Paging;

import java.io.Serializable;

/**
 * <pre>
 * 用户信息 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-19
 */
public interface UserInfoService extends BaseService<UserInfo> {
    
    /**
     * 保存
     *
     * @param userInfo
     * @return
     * @throws Exception
     */
    boolean saveUserInfo(UserInfo userInfo) throws Exception;
        
    /**
     * 修改
     *
     * @param userInfo
     * @return
     * @throws Exception
     */
    boolean updateUserInfo(UserInfo userInfo) throws Exception;
        
    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteUserInfo(Long id) throws Exception;
    
    
    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    UserInfo getUserInfoById(Long id) throws Exception;
        
    /**
     * 获取分页对象
     *
     * @param userInfoQuery
     * @return
     * @throws Exception
     */
    Paging<UserInfo> getUserInfoPageList(UserInfoQuery userInfoQuery) throws Exception;
    
}
