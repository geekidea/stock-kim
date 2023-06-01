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

import io.geekidea.framework.common.service.BaseService;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.stock.dto.vo.LineNamveValueVo;
import io.geekidea.stock.entity.StockRiseLine;
import io.geekidea.stock.dto.query.StockRiseLineQuery;
import io.geekidea.stock.dto.vo.StockRiseLineQueryVo;

import java.util.List;

/**
 * <pre>
 * 上涨家数数据 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-11
 */
public interface StockRiseLineService extends BaseService<StockRiseLine> {
    
    /**
     * 保存
     *
     * @param stockRiseLine
     * @return
     * @throws Exception
     */
    boolean saveStockRiseLine(StockRiseLine stockRiseLine) throws Exception;
        
    /**
     * 修改
     *
     * @param stockRiseLine
     * @return
     * @throws Exception
     */
    boolean updateStockRiseLine(StockRiseLine stockRiseLine) throws Exception;
        
    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteStockRiseLine(Long id) throws Exception;
    
    
    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    StockRiseLineQueryVo getStockRiseLineById(Long id) throws Exception;
        
    /**
     * 获取分页对象
     *
     * @param stockRiseLineQuery
     * @return
     * @throws Exception
     */
    Paging<StockRiseLineQueryVo> getStockRiseLinePageList(StockRiseLineQuery stockRiseLineQuery) throws Exception;

    void calcRiseCount() throws Exception;

    void updateTodayRiseCount(boolean optionalYn) throws Exception;

    List<LineNamveValueVo> getStockRiseLineList() throws Exception;

}
