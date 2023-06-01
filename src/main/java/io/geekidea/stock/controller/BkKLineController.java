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

import io.geekidea.stock.dto.query.BkKLineSearchQuery;
import io.geekidea.framework.util.DateUtil;
import io.geekidea.stock.dto.query.SyncBkKLineQuery;
import io.geekidea.stock.dto.vo.BkKLineVo;
import io.geekidea.stock.entity.BkKLine;
import io.geekidea.stock.service.BkKLineService;
import io.geekidea.stock.dto.query.BkKLineQuery;
import io.geekidea.stock.dto.vo.BkKLineQueryVo;
import io.geekidea.framework.common.api.ApiResult;
import io.geekidea.framework.common.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import io.geekidea.framework.common.vo.Paging;

/**
 * <pre>
 * 板块K线 前端控制器
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
@RequestMapping("/bkKLine")
@Api("板块K线")
public class BkKLineController extends BaseController {

    @Autowired
    private BkKLineService bkKLineService;

    @PostMapping("/addBkKLine")
    @ApiOperation("添加BkKLine对象")
    public ApiResult<Boolean> addBkKLine(@Valid @RequestBody BkKLine bkKLine) throws Exception {
        boolean flag = bkKLineService.save(bkKLine);
        return ApiResult.result(flag);
    }

    @PostMapping("/updateBkKLine")
    @ApiOperation("修改BkKLine对象")
    public ApiResult<Boolean> updateBkKLine(@Valid @RequestBody BkKLine bkKLine) throws Exception {
        boolean flag = bkKLineService.updateById(bkKLine);
        return ApiResult.result(flag);
    }

    @PostMapping("/deleteBkKLine/{id}")
    @ApiOperation("删除BkKLine对象")
    public ApiResult<Boolean> deleteBkKLine(@PathVariable("id") Long id) throws Exception {
        boolean flag = bkKLineService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取BkKLine对象详情")
    public ApiResult<BkKLineQueryVo> getBkKLine(@PathVariable("id") Long id) throws Exception {
        BkKLineQueryVo bkKLineQueryVo = bkKLineService.getBkKLineById(id);
        return ApiResult.ok(bkKLineQueryVo);
    }

    @PostMapping("/getBkKLinePageList")
    @ApiOperation("获取BkKLine分页列表")
    public ApiResult<Paging<BkKLineQueryVo>> getBkKLinePageList(@Valid @RequestBody BkKLineQuery bkKLineQuery) throws Exception {
        Paging<BkKLineQueryVo> paging = bkKLineService.getBkKLinePageList(bkKLineQuery);
        return ApiResult.ok(paging);
    }

    @PostMapping("/getBkKLineList")
    @ApiOperation("获取板块K线列表")
    public ApiResult<BkKLineVo> getBkKLineList(@Valid @RequestBody BkKLineSearchQuery bkKLineSearchQuery) throws Exception {
        BkKLineVo vo = bkKLineService.getBkKLineList(bkKLineSearchQuery);
        return ApiResult.ok(vo);
    }

    @PostMapping("/syncTodayAllBkKLineData")
    @ApiOperation("同步当日板块K线数据")
    public ApiResult<Object> syncTodayAllBkKLineData() throws Exception {
        String today = DateUtil.getTodayDate();
        bkKLineService.syncBkKLineData(today);
        return ApiResult.ok();
    }

    @PostMapping("/syncBkKLineData")
    @ApiOperation("同步板块K线数据")
    public ApiResult<Object> syncBkKLineData(@Valid @RequestBody SyncBkKLineQuery syncBkKLineQuery) throws Exception {
        bkKLineService.syncBkKLineData(syncBkKLineQuery);
        return ApiResult.ok();
    }

}
