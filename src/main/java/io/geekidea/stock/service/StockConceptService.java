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
import io.geekidea.stock.entity.StockConcept;
import io.geekidea.stock.dto.query.StockConceptQuery;
import io.geekidea.stock.dto.vo.StockConceptQueryVo;
import io.geekidea.stock.entity.StockConceptContent;

import java.util.List;

/**
 * <pre>
 * 股票概念 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-24
 */
public interface StockConceptService extends BaseService<StockConcept> {

    /**
     * 保存
     *
     * @param stockConcept
     * @return
     * @throws Exception
     */
    boolean saveStockConcept(StockConcept stockConcept) throws Exception;

    /**
     * 修改
     *
     * @param stockConcept
     * @return
     * @throws Exception
     */
    boolean updateStockConcept(StockConcept stockConcept) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteStockConcept(Long id) throws Exception;


    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    StockConceptQueryVo getStockConceptById(Long id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param stockConceptQuery
     * @return
     * @throws Exception
     */
    Paging<StockConceptQueryVo> getStockConceptPageList(StockConceptQuery stockConceptQuery) throws Exception;

    void saveStockConcept(List<StockConceptContent> stockConceptContents) throws Exception;

    void cleanRepeatConcept() throws Exception;

    void splitConcept() throws Exception;
}
