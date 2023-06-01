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
import io.geekidea.stock.entity.StockRecord;
import io.geekidea.stock.service.StockRecordService;
import io.geekidea.stock.dto.query.StockRecordQuery;
import io.geekidea.stock.dto.vo.StockRecordQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

/**
 * <pre>
 *  前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-12
 */
@Slf4j
@RestController
@RequestMapping("/stockRecord")
@Api("股票记录")
public class StockRecordController extends BaseController {

    @Autowired
    private StockRecordService stockRecordService;

    @PostMapping("/add")
    @ApiOperation("添加StockRecord对象")
    public ApiResult<Boolean> addStockRecord(@Valid @RequestBody StockRecord stockRecord) throws Exception {
        boolean flag = stockRecordService.save(stockRecord);
        return ApiResult.result(flag);
    }

    @PostMapping("/update")
    @ApiOperation("修改StockRecord对象")
    public ApiResult<Boolean> updateStockRecord(@Valid @RequestBody StockRecord stockRecord) throws Exception {
        boolean flag = stockRecordService.updateById(stockRecord);
        return ApiResult.result(flag);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除StockRecord对象")
    public ApiResult<Boolean> deleteStockRecord(@PathVariable("id") Long id) throws Exception {
        boolean flag = stockRecordService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取StockRecord对象详情")
    public ApiResult<StockRecordQueryVo> getStockRecord(@PathVariable("id") Long id) throws Exception {
        StockRecordQueryVo stockRecordQueryVo = stockRecordService.getStockRecordById(id);
        return ApiResult.ok(stockRecordQueryVo);
    }

    @PostMapping("/getPageList")
    @ApiOperation("获取StockRecord分页列表")
    public ApiResult<Paging<StockRecordQueryVo>> getStockRecordPageList(@Valid @RequestBody StockRecordQuery stockRecordQuery) throws Exception {
        Paging<StockRecordQueryVo> paging = stockRecordService.getStockRecordPageList(stockRecordQuery);
        return ApiResult.ok(paging);
    }

}
