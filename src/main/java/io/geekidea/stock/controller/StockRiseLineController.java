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

import io.geekidea.framework.common.api.ApiResult;
import io.geekidea.framework.common.controller.BaseController;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.stock.dto.vo.LineNamveValueVo;
import io.geekidea.stock.entity.StockRiseLine;
import io.geekidea.stock.service.StockRiseLineService;
import io.geekidea.stock.dto.query.StockRiseLineQuery;
import io.geekidea.stock.dto.vo.StockRiseLineQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import java.util.List;

/**
 * <pre>
 * 上涨家数数据 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-11
 */
@Slf4j
@RestController
@RequestMapping("/stockRiseLine")
@Api("上涨家数数据")
public class StockRiseLineController extends BaseController {

    @Autowired
    private StockRiseLineService stockRiseLineService;

    @PostMapping("/add")
    @ApiOperation("添加StockRiseLine对象")
    public ApiResult<Boolean> addStockRiseLine(@Valid @RequestBody StockRiseLine stockRiseLine) throws Exception {
        boolean flag = stockRiseLineService.save(stockRiseLine);
        return ApiResult.result(flag);
    }

    @PostMapping("/update")
    @ApiOperation("修改StockRiseLine对象")
    public ApiResult<Boolean> updateStockRiseLine(@Valid @RequestBody StockRiseLine stockRiseLine) throws Exception {
        boolean flag = stockRiseLineService.updateById(stockRiseLine);
        return ApiResult.result(flag);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除StockRiseLine对象")
    public ApiResult<Boolean> deleteStockRiseLine(@PathVariable("id") Long id) throws Exception {
        boolean flag = stockRiseLineService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取StockRiseLine对象详情")
    public ApiResult<StockRiseLineQueryVo> getStockRiseLine(@PathVariable("id") Long id) throws Exception {
        StockRiseLineQueryVo stockRiseLineQueryVo = stockRiseLineService.getStockRiseLineById(id);
        return ApiResult.ok(stockRiseLineQueryVo);
    }

    @PostMapping("/getPageList")
    @ApiOperation("获取StockRiseLine分页列表")
    public ApiResult<Paging<StockRiseLineQueryVo>> getStockRiseLinePageList(@Valid @RequestBody StockRiseLineQuery stockRiseLineQuery) throws Exception {
        Paging<StockRiseLineQueryVo> paging = stockRiseLineService.getStockRiseLinePageList(stockRiseLineQuery);
        return ApiResult.ok(paging);
    }

    @PostMapping("/getRiseLineList")
    @ApiOperation("获取StockRiseLine列表")
    public ApiResult<List<LineNamveValueVo>> getStockRiseLineList() throws Exception {
        List<LineNamveValueVo> list = stockRiseLineService.getStockRiseLineList();
        return ApiResult.ok(list);
    }

    @PostMapping("/calcRiseCount")
    @ApiOperation("计算上涨家数")
    public ApiResult calcRiseCount() throws Exception {
        stockRiseLineService.calcRiseCount();
        return ApiResult.ok();
    }

}
