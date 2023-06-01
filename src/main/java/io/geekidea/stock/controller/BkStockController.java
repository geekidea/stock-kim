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

import io.geekidea.framework.common.exception.BusinessException;
import io.geekidea.stock.dto.vo.NameValueVo;
import io.geekidea.stock.entity.BkStock;
import io.geekidea.stock.entity.Stock;
import io.geekidea.stock.service.BkStockService;
import io.geekidea.stock.dto.query.BkStockQuery;
import io.geekidea.stock.dto.vo.BkStockQueryVo;
import io.geekidea.framework.common.api.ApiResult;
import io.geekidea.framework.common.controller.BaseController;
import io.geekidea.stock.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import io.geekidea.framework.common.vo.Paging;

import java.util.List;

/**
 * <pre>
 * 板块股票 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-06
 */
@Slf4j
@RestController
@RequestMapping("/bkStock")
@Api("板块股票")
public class BkStockController extends BaseController {

    @Autowired
    private BkStockService bkStockService;

    @PostMapping("/addBkStock")
    @ApiOperation("添加BkStock对象")
    public ApiResult<Boolean> addBkStock(@Valid @RequestBody BkStock bkStock) throws Exception {
        boolean flag = bkStockService.saveBkStock(bkStock);
        return ApiResult.result(flag);
    }

    @PostMapping("/deleteBkStock")
    @ApiOperation("删除BkStock对象")
    public ApiResult<Boolean> deleteBkStock(@Valid @RequestBody BkStock bkStock) throws Exception {
        boolean flag = bkStockService.deleteBkStock(bkStock);
        return ApiResult.result(flag);
    }

    @GetMapping("/getBkIndustryCount")
    @ApiOperation("获取板块行业分布")
    public ApiResult<List<NameValueVo>> getBkIndustryCount(String bkCode) throws Exception {
        List<NameValueVo> list = bkStockService.getBkIndustryCount(bkCode);
        return ApiResult.ok(list);
    }

    @GetMapping("/getBkConceptCount")
    @ApiOperation("获取板块概念分布")
    public ApiResult<List<NameValueVo>> getBkConceptCount(String bkCode) throws Exception {
        List<NameValueVo> list = bkStockService.getBkConceptCount(bkCode);
        return ApiResult.ok(list);
    }

}
