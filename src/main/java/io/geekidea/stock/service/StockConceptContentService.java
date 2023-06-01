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
import io.geekidea.stock.entity.StockConceptContent;
import io.geekidea.stock.dto.query.StockConceptContentQuery;
import io.geekidea.stock.dto.vo.StockConceptContentQueryVo;

import java.util.List;

/**
 * <pre>
 * 股票概念内容 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-24
 */
public interface StockConceptContentService extends BaseService<StockConceptContent> {

    /**
     * 保存
     *
     * @param stockConceptContent
     * @return
     * @throws Exception
     */
    boolean saveStockConceptContent(StockConceptContent stockConceptContent) throws Exception;

    /**
     * 修改
     *
     * @param stockConceptContent
     * @return
     * @throws Exception
     */
    boolean updateStockConceptContent(StockConceptContent stockConceptContent) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteStockConceptContent(Long id) throws Exception;


    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    StockConceptContentQueryVo getStockConceptContentById(Long id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param stockConceptContentQuery
     * @return
     * @throws Exception
     */
    Paging<StockConceptContentQueryVo> getStockConceptContentPageList(StockConceptContentQuery stockConceptContentQuery) throws Exception;

    void saveStockConceptContent(List<StockConceptContent> stockConceptContents) throws Exception;
}
