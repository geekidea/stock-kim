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

import io.geekidea.stock.dto.query.StockKLineSearchQuery;
import io.geekidea.stock.dto.vo.StockBasicKLineVo;
import io.geekidea.stock.dto.vo.StockKLineVo;
import io.geekidea.stock.service.StockKLineService;
import io.geekidea.framework.common.api.ApiResult;
import io.geekidea.framework.common.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

/**
 * <pre>
 * K线数据 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-18
 */
@Slf4j
@RestController
@RequestMapping("/stockKLine")
@Api("K线数据")
public class StockKLineController extends BaseController {

    @Autowired
    private StockKLineService stockKLineService;

    @PostMapping("/getKLineList")
    @ApiOperation("获取K线列表")
    public ApiResult<StockKLineVo> getStockKLineList(@Valid @RequestBody StockKLineSearchQuery stockKLineSearchQuery) throws Exception {
        StockKLineVo vo = stockKLineService.getStockKLineList(stockKLineSearchQuery);
        return ApiResult.ok(vo);
    }

    @PostMapping("/getBasicList")
    @ApiOperation("获取基本K线列表")
    public ApiResult<StockBasicKLineVo> getStockBasicKLineList(@Valid @RequestBody StockKLineSearchQuery stockKLineSearchQuery) throws Exception {
        StockBasicKLineVo vo = stockKLineService.getStockBasicKLineList(stockKLineSearchQuery);
        return ApiResult.ok(vo);
    }

    @PostMapping("/syncRecentKLineData")
    @ApiOperation("同步最近K线数据")
    public ApiResult<Object> syncRecentKLineData() throws Exception {
        stockKLineService.asyncRecentKLineData();
        return ApiResult.ok();
    }

    @PostMapping("/updateKLineIncrease")
    @ApiOperation("计算K线当日涨幅")
    public ApiResult<Object> updateKLineIncrease() throws Exception {
        String startDate = "2021-11-08";
        stockKLineService.updateKLineIncrease(startDate);
        return ApiResult.ok();
    }

    @PostMapping("/updateTodayKLineDayIncrease")
    @ApiOperation("计算K线每日涨幅")
    public ApiResult<Object> updateTodayKLineDayIncrease() throws Exception {
        long start = System.currentTimeMillis();
        stockKLineService.updateTodayKLineDayIncrease(null);
        long end = System.currentTimeMillis();
        System.out.println("计算K线每日涨幅耗时 = " + (end - start) / 1000);
        return ApiResult.ok();
    }


}
