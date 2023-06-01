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

import io.geekidea.stock.entity.UserStock;
import io.geekidea.framework.common.service.BaseService;
import io.geekidea.stock.dto.query.UserStockQuery;
import io.geekidea.stock.dto.vo.UserStockQueryVo;
import io.geekidea.framework.common.vo.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 用户持仓信息 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-19
 */
public interface UserStockService extends BaseService<UserStock> {
    
    /**
     * 保存
     *
     * @param userStock
     * @return
     * @throws Exception
     */
    boolean saveUserStock(UserStock userStock) throws Exception;
        
    /**
     * 修改
     *
     * @param userStock
     * @return
     * @throws Exception
     */
    boolean updateUserStock(UserStock userStock) throws Exception;
        
    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteUserStock(Long id) throws Exception;
    
    
    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    UserStock getUserStockById(Long id) throws Exception;
        
    /**
     * 获取列表
     *
     * @param userStockQuery
     * @return
     * @throws Exception
     */
    List<UserStock> getUserStockList(UserStockQuery userStockQuery) throws Exception;
    
}
