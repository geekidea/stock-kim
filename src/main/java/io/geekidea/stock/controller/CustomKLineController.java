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
import io.geekidea.stock.entity.CustomKLine;
import io.geekidea.stock.service.CustomKLineService;
import io.geekidea.stock.dto.query.CustomKLineQuery;
import io.geekidea.stock.dto.vo.CustomKLineQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

/**
 * <pre>
 * 自定义K线 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-24
 */
@Slf4j
@RestController
@RequestMapping("/customKLine")
@Api("自定义K线")
public class CustomKLineController extends BaseController {

    @Autowired
    private CustomKLineService customKLineService;

    @PostMapping("/addCustomKLine")
    @ApiOperation("添加CustomKLine对象")
    public ApiResult<Boolean> addCustomKLine(@Valid @RequestBody CustomKLine customKLine) throws Exception {
        boolean flag = customKLineService.save(customKLine);
        return ApiResult.result(flag);
    }

    @PostMapping("/updateCustomKLine")
    @ApiOperation("修改CustomKLine对象")
    public ApiResult<Boolean> updateCustomKLine(@Valid @RequestBody CustomKLine customKLine) throws Exception {
        boolean flag = customKLineService.updateById(customKLine);
        return ApiResult.result(flag);
    }

    @PostMapping("/deleteCustomKLine/{id}")
    @ApiOperation("删除CustomKLine对象")
    public ApiResult<Boolean> deleteCustomKLine(@PathVariable("id") Long id) throws Exception {
        boolean flag = customKLineService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取CustomKLine对象详情")
    public ApiResult<CustomKLineQueryVo> getCustomKLine(@PathVariable("id") Long id) throws Exception {
        CustomKLineQueryVo customKLineQueryVo = customKLineService.getCustomKLineById(id);
        return ApiResult.ok(customKLineQueryVo);
    }

    @PostMapping("/getCustomKLinePageList")
    @ApiOperation("获取CustomKLine分页列表")
    public ApiResult<Paging<CustomKLineQueryVo>> getCustomKLinePageList(@Valid @RequestBody CustomKLineQuery customKLineQuery) throws Exception {
        Paging<CustomKLineQueryVo> paging = customKLineService.getCustomKLinePageList(customKLineQuery);
        return ApiResult.ok(paging);
    }

}
