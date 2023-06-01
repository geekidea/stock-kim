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

package io.geekidea.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.framework.util.BatchNoUtil;
import io.geekidea.stock.dto.query.MarketScoreLineQuery;
import io.geekidea.stock.dto.vo.LineNamveValueVo;
import io.geekidea.stock.dto.vo.MarketScoreLineQueryVo;
import io.geekidea.stock.dto.vo.NameValuePercentageVo;
import io.geekidea.stock.entity.MarketScoreLine;
import io.geekidea.stock.mapper.StockKLineMapper;
import io.geekidea.stock.service.MarketScoreLineService;
import io.geekidea.stock.service.StockService;
import io.geekidea.stock.mapper.MarketScoreLineMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <pre>
 * 市场分数 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-21
 */
@Slf4j
@Service
public class MarketScoreLineServiceImpl extends BaseServiceImpl<MarketScoreLineMapper, MarketScoreLine> implements MarketScoreLineService {

    @Autowired
    private MarketScoreLineMapper marketScoreLineMapper;

    @Autowired
    private StockKLineMapper stockKLineMapper;

    @Autowired
    private StockService stockService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveMarketScoreLine(MarketScoreLine marketScoreLine) throws Exception {
        return super.save(marketScoreLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMarketScoreLine(MarketScoreLine marketScoreLine) throws Exception {
        return super.updateById(marketScoreLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteMarketScoreLine(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public MarketScoreLineQueryVo getMarketScoreLineById(Long id) throws Exception {
        return marketScoreLineMapper.getMarketScoreLineById(id);
    }

    @Override
    public Paging<MarketScoreLineQueryVo> getMarketScoreLinePageList(MarketScoreLineQuery marketScoreLineQuery) throws Exception {
        Page page = buildPageQuery(marketScoreLineQuery, OrderItem.desc("create_time"));
        IPage<MarketScoreLineQueryVo> iPage = marketScoreLineMapper.getMarketScoreLinePageList(page, marketScoreLineQuery);
        return new Paging(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void calcMarketScore() throws Exception {
        LambdaUpdateWrapper<MarketScoreLine> lambdaUpdateWrapper = new LambdaUpdateWrapper();
        lambdaUpdateWrapper.isNotNull(MarketScoreLine::getLineDate);
        marketScoreLineMapper.delete(lambdaUpdateWrapper);
        // 获取天数集合，循环，按天统计
        String startDate = "2020-01-01";
        List<String> lineDates = stockKLineMapper.getLineDates(startDate);
        if (CollectionUtils.isEmpty(lineDates)) {
            return;
        }
        calcMarketScore(lineDates);
    }

    @Override
    public void calcMarketScore(String lineDate) throws Exception {
        calcMarketScore(Arrays.asList(lineDate));
    }

    @Override
    public void calcMarketScore(List<String> lineDates) throws Exception {
        if (CollectionUtils.isEmpty(lineDates)) {
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        List<MarketScoreLine> marketScoreLines = new ArrayList<>();
        for (String lineDate : lineDates) {
            MarketScoreLine marketScoreLine = new MarketScoreLine();
            BigDecimal marketScore = getCalcMarketScore(lineDate);
            marketScoreLine.setLineDate(lineDate);
            marketScoreLine.setScore(marketScore);
            marketScoreLine.setLineBatchNo(batchNo);
            marketScoreLines.add(marketScoreLine);
            log.info("{}:{}", lineDate, marketScore);
        }
        saveBatch(marketScoreLines);
    }

    @Override
    public BigDecimal getCalcMarketScore(String lineDate) throws Exception {
        // 1. 获取涨跌家数百分比
        List<NameValuePercentageVo> stockMarketOverviews = getStockMarketOverview(lineDate);
        // 2. 获取行业涨跌百分比
        List<NameValuePercentageVo> industryMarketOverviews = getIndustryMarketOverview(lineDate);
        // 3. 获取概念涨跌百分比
        List<NameValuePercentageVo> conceptMarketOverviews = getConceptMarketOverview(lineDate);
        // 4. 获取涨跌分布百分比
        List<NameValuePercentageVo> increaseDistributions = getIncreaseDistribution(lineDate);
        return stockService.calcMarketScore(stockMarketOverviews, industryMarketOverviews, conceptMarketOverviews, increaseDistributions);
    }

    private List<NameValuePercentageVo> getStockMarketOverview(String lineDate) throws Exception {
        return marketScoreLineMapper.getStockMarketOverview(lineDate);
    }

    private List<NameValuePercentageVo> getIndustryMarketOverview(String lineDate) throws Exception {
        return marketScoreLineMapper.getIndustryMarketOverview(lineDate);
    }

    private List<NameValuePercentageVo> getConceptMarketOverview(String lineDate) throws Exception {
        return marketScoreLineMapper.getConceptMarketOverview(lineDate);
    }

    private List<NameValuePercentageVo> getIncreaseDistribution(String lineDate) throws Exception {
        return marketScoreLineMapper.getIncreaseDistribution(lineDate);
    }

    @Override
    public List<LineNamveValueVo> getMarketScoreLineList() throws Exception {
        return marketScoreLineMapper.getMarketScoreLineList();
    }

    @Override
    public void calcRecentMarketScore() throws Exception {

    }

    @Override
    public void calcTodayMarketScore() throws Exception {

    }

}
