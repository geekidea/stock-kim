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

import io.geekidea.stock.dto.query.*;
import io.geekidea.stock.dto.vo.*;
import io.geekidea.stock.entity.Stock;
import io.geekidea.framework.common.service.BaseService;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.stock.entity.StockKLine;
import io.geekidea.stock.entity.StockRealData;

import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 *  服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-11
 */
public interface StockService extends BaseService<Stock> {

    /**
     * 保存
     *
     * @param stockList
     * @throws Exception
     */
    void importStock(List<Stock> stockList) throws Exception;

    /**
     * 保存
     *
     * @param stock
     * @return
     * @throws Exception
     */
    boolean saveStock(Stock stock) throws Exception;

    /**
     * 修改
     *
     * @param stock
     * @return
     * @throws Exception
     */
    boolean updateStock(Stock stock) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteStock(Long id) throws Exception;


    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    StockQueryVo getStockById(Long id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param stockQuery
     * @return
     * @throws Exception
     */
    Paging<StockQueryVo> getStockPageList(StockQuery stockQuery) throws Exception;

    /**
     * 获取股票列表
     *
     * @param stockQuery
     * @return
     * @throws Exception
     */
    List<StockBasicVo> getStockBasicList(StockQuery stockQuery) throws Exception;

    /**
     * 更新实时数据
     *
     * @param stockRealData
     * @throws Exception
     */
    void updateStockRealData(List<StockRealData> stockRealData) throws Exception;

    List<IndustryConceptAvgVo> getRiseIndustryAvg() throws Exception;

    List<IndustryConceptAvgVo> getFallIndustryAvg(boolean asc ) throws Exception;

    List<IndustryConceptAvgVo> getIndustryAvg() throws Exception;

    List<IndustryConceptAvgVo> getRiseConceptAvg() throws Exception;

    List<IndustryConceptAvgVo> getFallConceptAvg(boolean asc) throws Exception;

    List<IndustryConceptAvgVo> getConceptAvg() throws Exception;

    List<NameValuePercentageVo> getIndustryRiseFallCount() throws Exception;

    List<NameValuePercentageVo> getConceptRiseFallCount() throws Exception;

    List<IndustryConceptAvgVo> getIndustryConceptAvg(IndustryConceptAvgQuery industryConceptAvgQuery) throws Exception;

    List<IndustryConceptAvgVo> getRiseIndustryConceptAvg(IndustryConceptAvgQuery industryConceptAvgQuery) throws Exception;

    List<IndustryConceptAvgVo> getFallIndustryConceptAvg(IndustryConceptAvgQuery industryConceptAvgQuery) throws Exception;

    List<NameValuePercentageVo> getMarketOverview(QuotationPieQuery industryConceptAvgQuery) throws Exception;

    List<NameValuePercentageVo> getStockMarketOverview() throws Exception;

    List<NameValuePercentageVo> getIndustryMarketOverview() throws Exception;

    List<NameValuePercentageVo> getConceptMarketOverview() throws Exception;

    List<NameValuePercentageVo> getRiseFallLimit() throws Exception;

    List<NameValuePercentageVo> getIncreaseDistribution() throws Exception;

    MarketOverviewDistributionVo getMarketOverviewDistribution() throws Exception;

    BigDecimal getMarketOverviewScore() throws Exception;

    BigDecimal calcMarketScore(List<NameValuePercentageVo> stockMarketOverviews,
                               List<NameValuePercentageVo> industryMarketOverviews,
                               List<NameValuePercentageVo> conceptMarketOverviews,
                               List<NameValuePercentageVo> increaseDistributions) throws Exception;

    MarketOverviewInfoVo getMarketOverviewInfo() throws Exception;

    /**
     * 更新每日涨幅和均线
     *
     * @throws Exception
     */
    void updateStockDayIncreaseAndMA() throws Exception;

    StockDynamicQueryVo getStockDynamicList(StockDynamicQuery stockDynamicQuery) throws Exception;

    void calcPercentage(List<NameValuePercentageVo> list) throws Exception;

    List<Stock> getStockListByStockCodes(List<String> stockCodes);

    void updateStockIncreaseMA() throws Exception;

    void updateRealDayIncrease(Stock stock, List<StockKLine> stockKLines) throws Exception;

    void updateRealMA(Stock stock, List<StockKLine> stockKLines) throws Exception;

    Paging<IndustryConceptIndexVo> getIndustryConceptPageList(IndustryConceptIndexQuery industryConceptIndexQuery) throws Exception;

    void updateAddStockInfo() throws Exception;

    List<String> getStockCodeList() throws Exception;

    List<String> getStockCodeList(boolean optionalYn) throws Exception;

    String getLastTradeDate() throws Exception;

    /**
     * 计算阶段涨幅和均线
     *
     * @throws Exception
     */
    void calcDayIncreaseMA() throws Exception;

    void calcDayIncreaseMA(boolean optionalYn) throws Exception;

    /**
     * 根据股票信息计算阶段涨幅和均线
     *
     * @param stock
     * @throws Exception
     */
    void calcDayIncreaseMA(Stock stock) throws Exception;

    /**
     * 导入同花顺股票
     *
     * @param stockList
     * @throws Exception
     */
    void importThsStock(List<Stock> stockList) throws Exception;

    List<String> getNotOptionalStockCodeList() throws Exception;
}
