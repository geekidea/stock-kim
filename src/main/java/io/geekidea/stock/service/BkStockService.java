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

import io.geekidea.stock.dto.vo.NameValueVo;
import io.geekidea.stock.entity.BkStock;
import io.geekidea.framework.common.service.BaseService;
import io.geekidea.stock.dto.query.BkStockQuery;
import io.geekidea.stock.dto.vo.BkStockQueryVo;
import io.geekidea.framework.common.vo.Paging;

import java.util.List;

/**
 * <pre>
 * 板块股票 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-06
 */
public interface BkStockService extends BaseService<BkStock> {
    
    /**
     * 保存
     *
     * @param bkStock
     * @return
     * @throws Exception
     */
    boolean saveBkStock(BkStock bkStock) throws Exception;
        
    /**
     * 修改
     *
     * @param bkStock
     * @return
     * @throws Exception
     */
    boolean updateBkStock(BkStock bkStock) throws Exception;
        
    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteBkStock(Long id) throws Exception;
    
    
    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    BkStockQueryVo getBkStockById(Long id) throws Exception;
        
    /**
     * 获取分页对象
     *
     * @param bkStockQuery
     * @return
     * @throws Exception
     */
    Paging<BkStockQueryVo> getBkStockPageList(BkStockQuery bkStockQuery) throws Exception;

    List<NameValueVo> getBkIndustryCount(String bkCode) throws Exception;

    List<NameValueVo> getBkConceptCount(String bkCode) throws Exception;

    boolean deleteBkStock(BkStock bkStock) throws Exception;
}
