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
import io.geekidea.stock.entity.RiseLimitLine;
import io.geekidea.stock.service.RiseLimitLineService;
import io.geekidea.stock.dto.query.RiseLimitLineQuery;
import io.geekidea.stock.dto.vo.RiseLimitLineQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import java.util.List;

/**
 * <pre>
 * 涨停家数 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-22
 */
@Slf4j
@RestController
@RequestMapping("/riseLimitLine")
@Api("涨停家数")
public class RiseLimitLineController extends BaseController {

    @Autowired
    private RiseLimitLineService riseLimitLineService;

    @PostMapping("/addRiseLimitLine")
    @ApiOperation("添加RiseLimitLine对象")
    public ApiResult<Boolean> addRiseLimitLine(@Valid @RequestBody RiseLimitLine riseLimitLine) throws Exception {
        boolean flag = riseLimitLineService.save(riseLimitLine);
        return ApiResult.result(flag);
    }

    @PostMapping("/updateRiseLimitLine")
    @ApiOperation("修改RiseLimitLine对象")
    public ApiResult<Boolean> updateRiseLimitLine(@Valid @RequestBody RiseLimitLine riseLimitLine) throws Exception {
        boolean flag = riseLimitLineService.updateById(riseLimitLine);
        return ApiResult.result(flag);
    }

    @PostMapping("/deleteRiseLimitLine/{id}")
    @ApiOperation("删除RiseLimitLine对象")
    public ApiResult<Boolean> deleteRiseLimitLine(@PathVariable("id") Long id) throws Exception {
        boolean flag = riseLimitLineService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取RiseLimitLine对象详情")
    public ApiResult<RiseLimitLineQueryVo> getRiseLimitLine(@PathVariable("id") Long id) throws Exception {
        RiseLimitLineQueryVo riseLimitLineQueryVo = riseLimitLineService.getRiseLimitLineById(id);
        return ApiResult.ok(riseLimitLineQueryVo);
    }

    @PostMapping("/getRiseLimitLinePageList")
    @ApiOperation("获取RiseLimitLine分页列表")
    public ApiResult<Paging<RiseLimitLineQueryVo>> getRiseLimitLinePageList(@Valid @RequestBody RiseLimitLineQuery riseLimitLineQuery) throws Exception {
        Paging<RiseLimitLineQueryVo> paging = riseLimitLineService.getRiseLimitLinePageList(riseLimitLineQuery);
        return ApiResult.ok(paging);
    }

    @PostMapping("/getRiseLimitLineList")
    @ApiOperation("获取RiseLimitLine列表")
    public ApiResult<List<LineNamveValueVo>> getRiseLimitLineList() throws Exception {
        List<LineNamveValueVo> list = riseLimitLineService.getRiseLimitLineList();
        return ApiResult.ok(list);
    }

    @PostMapping("/calcRiseLimitCount")
    @ApiOperation("计算涨停家数")
    public ApiResult calcRiseLimitCount() throws Exception {
        riseLimitLineService.calcRiseLimitCount();
        return ApiResult.ok();
    }

}
