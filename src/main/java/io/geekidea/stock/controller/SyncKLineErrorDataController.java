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
import io.geekidea.stock.entity.SyncKLineErrorData;
import io.geekidea.stock.service.SyncKLineErrorDataService;
import io.geekidea.stock.dto.query.SyncKLineErrorDataQuery;
import io.geekidea.stock.dto.vo.SyncKLineErrorDataQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

/**
 * <pre>
 * 同步K线错误数据 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-09
 */
@Slf4j
@RestController
@RequestMapping("/syncKLineErrorData")
@Api("同步K线错误数据")
public class SyncKLineErrorDataController extends BaseController {

    @Autowired
    private SyncKLineErrorDataService syncKLineErrorDataService;

    @PostMapping("/addSyncKLineErrorData")
    @ApiOperation("添加SyncKLineErrorData对象")
    public ApiResult<Boolean> addSyncKLineErrorData(@Valid @RequestBody SyncKLineErrorData syncKLineErrorData) throws Exception {
        boolean flag = syncKLineErrorDataService.save(syncKLineErrorData);
        return ApiResult.result(flag);
    }

    @PostMapping("/updateSyncKLineErrorData")
    @ApiOperation("修改SyncKLineErrorData对象")
    public ApiResult<Boolean> updateSyncKLineErrorData(@Valid @RequestBody SyncKLineErrorData syncKLineErrorData) throws Exception {
        boolean flag = syncKLineErrorDataService.updateById(syncKLineErrorData);
        return ApiResult.result(flag);
    }

    @PostMapping("/deleteSyncKLineErrorData/{id}")
    @ApiOperation("删除SyncKLineErrorData对象")
    public ApiResult<Boolean> deleteSyncKLineErrorData(@PathVariable("id") Long id) throws Exception {
        boolean flag = syncKLineErrorDataService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取SyncKLineErrorData对象详情")
    public ApiResult<SyncKLineErrorDataQueryVo> getSyncKLineErrorData(@PathVariable("id") Long id) throws Exception {
        SyncKLineErrorDataQueryVo syncKLineErrorDataQueryVo = syncKLineErrorDataService.getSyncKLineErrorDataById(id);
        return ApiResult.ok(syncKLineErrorDataQueryVo);
    }

    @PostMapping("/getSyncKLineErrorDataPageList")
    @ApiOperation("获取SyncKLineErrorData分页列表")
    public ApiResult<Paging<SyncKLineErrorDataQueryVo>> getSyncKLineErrorDataPageList(@Valid @RequestBody SyncKLineErrorDataQuery syncKLineErrorDataQuery) throws Exception {
        Paging<SyncKLineErrorDataQueryVo> paging = syncKLineErrorDataService.getSyncKLineErrorDataPageList(syncKLineErrorDataQuery);
        return ApiResult.ok(paging);
    }

}
