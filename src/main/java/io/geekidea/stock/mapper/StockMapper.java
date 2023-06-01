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

package io.geekidea.stock.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.geekidea.stock.dto.query.StockQuery;
import io.geekidea.stock.dto.vo.*;
import io.geekidea.stock.dto.vo.*;
import io.geekidea.stock.entity.ConceptIndex;
import io.geekidea.stock.entity.IndustryIndex;
import io.geekidea.stock.entity.Stock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *  Mapper 接口
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-11
 */
@Repository
public interface StockMapper extends BaseMapper<Stock> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    StockQueryVo getStockById(Long id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param stockQueryParam
     * @return
     */
    IPage<StockQueryVo> getStockPageList(@Param("page") Page page, @Param("query") StockQuery stockQuery);

    IPage<StockQueryVo> getStockPageListByConcept(@Param("page") Page page, @Param("query") StockQuery stockQuery);

    List<IndustryConceptAvgVo> getRiseIndustryAvg(boolean optionalYn);

    List<IndustryConceptAvgVo> getFallIndustryAvg(boolean asc, boolean optionalYn);

    List<IndustryConceptAvgVo> getRiseConceptAvg(boolean optionalYn);

    List<IndustryConceptAvgVo> getFallConceptAvg(boolean asc, boolean optionalYn);

    List<NameValuePercentageVo> getIndustryRiseFallCount(boolean optionalYn);

    List<NameValuePercentageVo> getConceptRiseFallCount(boolean optionalYn);

    List<NameValuePercentageVo> getStockMarketOverview(boolean optionalYn);

    List<NameValuePercentageVo> getIndustryMarketOverview(boolean optionalYn);

    List<NameValuePercentageVo> getConceptMarketOverview(boolean optionalYn);

    List<NameValuePercentageVo> getRiseFallLimit(boolean optionalYn);

    List<NameValuePercentageVo> getIncreaseDistribution(boolean optionalYn);

    List<IndustryIndex> getIndustryStatistics();

    List<ConceptIndex> getConceptStatistics();

    List<ConceptNameInfoVo> getConceptNameInfoList();

    List<Stock> getNonKLineDataStocks();

    Integer getCountBybeforeEndDate(@Param("lineCode") String lineCode, @Param("beforeEndDate") Date beforeEndDate);

    List<Stock> getRecentDiffData(@Param("beforeEndDate") Date beforeEndDate);

    List<Stock> getTodayDiffData();

    IPage<StockQueryVo> getStockPageListByBkCode(@Param("page") Page page, @Param("query") StockQuery stockQuery);

    IPage<StockQueryVo> getStockPageListByBkCodeAndConcept(@Param("page") Page page, @Param("query") StockQuery stockQuery);

    List<StockBasicVo> getStockBasicList(StockQuery stockQuery);

    List<String> getDeleteStockCodes();

    List<Stock> getAddStock();

    List<Stock> getFixAddStock();

    List<Stock> getStockListByStockCodes(@Param("addStockCodes") List<String> addStockCodes);

    List<String> getStockCodeList(boolean optionalYn);

    String getLastTradeDate(boolean optionalYn);

    List<StockSearchVo> getStockByKeyword(@Param("keyword") String keyword);

    List<String> getNotOptionalStockCodeList();

}
