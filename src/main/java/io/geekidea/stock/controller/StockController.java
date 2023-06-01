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

package io.geekidea.stock.controller;

import io.geekidea.stock.dto.query.*;
import io.geekidea.stock.dto.vo.*;
import io.geekidea.stock.dto.query.*;
import io.geekidea.stock.dto.vo.*;
import io.geekidea.stock.entity.Stock;
import io.geekidea.stock.service.StockService;
import io.geekidea.framework.common.api.ApiResult;
import io.geekidea.framework.common.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import io.geekidea.framework.common.vo.Paging;

import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 *  前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-11
 */
@Slf4j
@RestController
@RequestMapping("/stock")
@Api("股票")
public class StockController extends BaseController {

    @Autowired
    private StockService stockService;

    @PostMapping("/add")
    @ApiOperation("添加Stock对象")
    public ApiResult<Boolean> addStock(@Valid @RequestBody Stock stock) throws Exception {
        boolean flag = stockService.save(stock);
        return ApiResult.result(flag);
    }

    @PostMapping("/update")
    @ApiOperation("修改Stock对象")
    public ApiResult<Boolean> updateStock(@Valid @RequestBody Stock stock) throws Exception {
        boolean flag = stockService.updateById(stock);
        return ApiResult.result(flag);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除Stock对象")
    public ApiResult<Boolean> deleteStock(@PathVariable("id") Long id) throws Exception {
        boolean flag = stockService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取Stock对象详情")
    public ApiResult<StockQueryVo> getStock(@PathVariable("id") Long id) throws Exception {
        StockQueryVo stockQueryVo = stockService.getStockById(id);
        return ApiResult.ok(stockQueryVo);
    }

    @PostMapping("/getStockPageList")
    @ApiOperation("获取Stock分页列表")
    public ApiResult<Paging<StockQueryVo>> getStockPageList(@Valid @RequestBody StockQuery stockQuery) throws Exception {
        Paging<StockQueryVo> paging = stockService.getStockPageList(stockQuery);
        return ApiResult.ok(paging);
    }

    @PostMapping("/getStockBasicList")
    @ApiOperation("获取Stock基本字段列表")
    public ApiResult<List<StockBasicVo>> getStockBasicList(@Valid @RequestBody StockQuery stockQuery) throws Exception {
        List<StockBasicVo> list = stockService.getStockBasicList(stockQuery);
        return ApiResult.ok(list);
    }

    @PostMapping("/getStockDynamicList")
    @ApiOperation("获取Stock动态数据列表")
    public ApiResult<StockDynamicQueryVo> getStockDynamicList(@Valid @RequestBody StockDynamicQuery stockDynamicQuery) throws Exception {
        StockDynamicQueryVo vo = stockService.getStockDynamicList(stockDynamicQuery);
        return ApiResult.ok(vo);
    }


    @PostMapping("/getIndustryConceptAvg")
    @ApiOperation("获取涨跌幅排前的行业概念指数")
    public ApiResult<List<IndustryConceptAvgVo>> getIndustryConceptAvg(@RequestBody IndustryConceptAvgQuery industryConceptAvgQuery) throws Exception {
        List<IndustryConceptAvgVo> list = stockService.getIndustryConceptAvg(industryConceptAvgQuery);
        return ApiResult.ok(list);
    }

    @PostMapping("/getRiseIndustryConceptAvg")
    @ApiOperation("获取涨幅排前的行业概念指数")
    public ApiResult<List<IndustryConceptAvgVo>> getRiseIndustryConceptAvg(@RequestBody IndustryConceptAvgQuery industryConceptAvgQuery) throws Exception {
        List<IndustryConceptAvgVo> list = stockService.getRiseIndustryConceptAvg(industryConceptAvgQuery);
        return ApiResult.ok(list);
    }

    @PostMapping("/getFallIndustryConceptAvg")
    @ApiOperation("获取跌幅排前的行业概念指数")
    public ApiResult<List<IndustryConceptAvgVo>> getFallIndustryConceptAvg(@RequestBody IndustryConceptAvgQuery industryConceptAvgQuery) throws Exception {
        List<IndustryConceptAvgVo> list = stockService.getFallIndustryConceptAvg(industryConceptAvgQuery);
        return ApiResult.ok(list);
    }


    @PostMapping("/getIndustryRiseFallCount")
    @ApiOperation("获取行业涨跌跌数量")
    public ApiResult<List<NameValuePercentageVo>> getIndustryRiseFallCount() throws Exception {
        List<NameValuePercentageVo> list = stockService.getIndustryRiseFallCount();
        return ApiResult.ok(list);
    }

    @PostMapping("/getConceptRiseFallCount")
    @ApiOperation("获取概念涨跌跌数量")
    public ApiResult<List<NameValuePercentageVo>> getConceptRiseFallCount() throws Exception {
        List<NameValuePercentageVo> list = stockService.getConceptRiseFallCount();
        return ApiResult.ok(list);
    }

    @PostMapping("/getMarketOverview")
    @ApiOperation("获取市场概况行情数据")
    public ApiResult<List<NameValuePercentageVo>> getMarketOverview(@RequestBody QuotationPieQuery quotationPieQuery) throws Exception {
        List<NameValuePercentageVo> list = stockService.getMarketOverview(quotationPieQuery);
        return ApiResult.ok(list);
    }

    @PostMapping("/getMarketOverviewDistribution")
    @ApiOperation("获取市场涨幅分布")
    public ApiResult<MarketOverviewDistributionVo> getMarketOverviewDistribution() throws Exception {
        MarketOverviewDistributionVo vo = stockService.getMarketOverviewDistribution();
        return ApiResult.ok(vo);
    }

    @PostMapping("/getMarketOverviewScore")
    @ApiOperation("获取市场评级分数")
    public ApiResult<BigDecimal> getMarketOverviewScore() throws Exception {
        BigDecimal temperature = stockService.getMarketOverviewScore();
        return ApiResult.ok(temperature);
    }

    @PostMapping("/getMarketOverviewInfo")
    @ApiOperation("获取市场概览信息")
    public ApiResult<MarketOverviewInfoVo> getMarketOverviewInfo() throws Exception {
        MarketOverviewInfoVo marketOverviewInfo = stockService.getMarketOverviewInfo();
        return ApiResult.ok(marketOverviewInfo);
    }

    @PostMapping("/getIndustryConceptPageList")
    @ApiOperation("获取行业概念分页列表")
    public ApiResult<Paging<IndustryConceptIndexVo>> getIndustryConceptPageList(@RequestBody IndustryConceptIndexQuery industryConceptIndexQuery) throws Exception {
        Paging<IndustryConceptIndexVo> paging = stockService.getIndustryConceptPageList(industryConceptIndexQuery);
        return ApiResult.ok(paging);
    }

    @PostMapping("/updateAddStockInfo")
    @ApiOperation("更新新增股票信息")
    public ApiResult<Paging<IndustryConceptIndexVo>> updateAddStockInfo() throws Exception {
        stockService.updateAddStockInfo();
        return ApiResult.ok();
    }

}
