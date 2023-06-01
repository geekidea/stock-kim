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
import io.geekidea.stock.entity.MarketScoreLine;
import io.geekidea.stock.dto.query.MarketScoreLineQuery;
import io.geekidea.stock.dto.vo.MarketScoreLineQueryVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 * 市场分数 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-21
 */
public interface MarketScoreLineService extends BaseService<MarketScoreLine> {

    /**
     * 保存
     *
     * @param marketScoreLine
     * @return
     * @throws Exception
     */
    boolean saveMarketScoreLine(MarketScoreLine marketScoreLine) throws Exception;

    /**
     * 修改
     *
     * @param marketScoreLine
     * @return
     * @throws Exception
     */
    boolean updateMarketScoreLine(MarketScoreLine marketScoreLine) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteMarketScoreLine(Long id) throws Exception;


    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    MarketScoreLineQueryVo getMarketScoreLineById(Long id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param marketScoreLineQuery
     * @return
     * @throws Exception
     */
    Paging<MarketScoreLineQueryVo> getMarketScoreLinePageList(MarketScoreLineQuery marketScoreLineQuery) throws Exception;

    void calcMarketScore() throws Exception;

    void calcMarketScore(String lineDate) throws Exception;

    void calcMarketScore(List<String> lineDates) throws Exception;

    BigDecimal getCalcMarketScore(String lineDate) throws Exception;

    List<LineNamveValueVo> getMarketScoreLineList() throws Exception;

    void calcRecentMarketScore() throws Exception;

    void calcTodayMarketScore() throws Exception;
}
