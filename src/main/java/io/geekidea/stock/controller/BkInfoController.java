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

import io.geekidea.stock.entity.BkInfo;
import io.geekidea.stock.service.BkInfoService;
import io.geekidea.stock.dto.query.BkInfoQuery;
import io.geekidea.stock.dto.vo.BkInfoQueryVo;
import io.geekidea.framework.common.api.ApiResult;
import io.geekidea.framework.common.controller.BaseController;
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
 * 板块信息 前端控制器
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
@RequestMapping("/bkInfo")
@Api("板块信息")
public class BkInfoController extends BaseController {

    @Autowired
    private BkInfoService bkInfoService;

    @PostMapping("/addBkInfo")
    @ApiOperation("添加BkInfo对象")
    public ApiResult<Boolean> addBkInfo(@Valid @RequestBody BkInfo bkInfo) throws Exception {
        boolean flag = bkInfoService.save(bkInfo);
        return ApiResult.result(flag);
    }

    @PostMapping("/updateBkInfo")
    @ApiOperation("修改BkInfo对象")
    public ApiResult<Boolean> updateBkInfo(@Valid @RequestBody BkInfo bkInfo) throws Exception {
        boolean flag = bkInfoService.updateById(bkInfo);
        return ApiResult.result(flag);
    }

    @PostMapping("/deleteBkInfo/{id}")
    @ApiOperation("删除BkInfo对象")
    public ApiResult<Boolean> deleteBkInfo(@PathVariable("id") Long id) throws Exception {
        boolean flag = bkInfoService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取BkInfo对象详情")
    public ApiResult<BkInfoQueryVo> getBkInfo(@PathVariable("id") Long id) throws Exception {
        BkInfoQueryVo bkInfoQueryVo = bkInfoService.getBkInfoById(id);
        return ApiResult.ok(bkInfoQueryVo);
    }

    @GetMapping("/getBkInfo")
    @ApiOperation("获取BkInfo对象详情")
    public ApiResult<BkInfo> getBkInfoByBkCode(String bkCode) throws Exception {
        BkInfo bkInfo = bkInfoService.getBkInfoByBkCode(bkCode);
        return ApiResult.ok(bkInfo);
    }

    @PostMapping("/getBkInfoPageList")
    @ApiOperation("获取BkInfo分页列表")
    public ApiResult<Paging<BkInfoQueryVo>> getBkInfoPageList(@Valid @RequestBody BkInfoQuery bkInfoQuery) throws Exception {
        Paging<BkInfoQueryVo> paging = bkInfoService.getBkInfoPageList(bkInfoQuery);
        return ApiResult.ok(paging);
    }

    @PostMapping("/getBkInfoList")
    @ApiOperation("获取BkInfo列表")
    public ApiResult<List<BkInfo>> getBkInfoList() throws Exception {
        List<BkInfo> list = bkInfoService.list();
        return ApiResult.ok(list);
    }

    @PostMapping("/refreshBkInfo")
    @ApiOperation("刷新板块信息")
    public ApiResult<Boolean> refreshBkInfo(String bkCode) throws Exception {
        bkInfoService.refreshBkInfo(bkCode);
        return ApiResult.ok();
    }

}
