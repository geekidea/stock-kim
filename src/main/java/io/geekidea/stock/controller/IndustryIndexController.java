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
import io.geekidea.stock.dto.query.IndustryConceptIndexQuery;
import io.geekidea.stock.dto.vo.IndustryConceptIndexVo;
import io.geekidea.stock.entity.IndustryIndex;
import io.geekidea.stock.service.IndustryIndexService;
import io.geekidea.stock.dto.vo.IndustryIndexQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

/**
 * <pre>
 * 行业指数 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-04
 */
@Slf4j
@RestController
@RequestMapping("/industryIndex")
@Api("行业指数")
public class IndustryIndexController extends BaseController {

    @Autowired
    private IndustryIndexService industryIndexService;

    @PostMapping("/add")
    @ApiOperation("添加IndustryIndex对象")
    public ApiResult<Boolean> addIndustryIndex(@Valid @RequestBody IndustryIndex industryIndex) throws Exception {
        boolean flag = industryIndexService.save(industryIndex);
        return ApiResult.result(flag);
    }

    @PostMapping("/update")
    @ApiOperation("修改IndustryIndex对象")
    public ApiResult<Boolean> updateIndustryIndex(@Valid @RequestBody IndustryIndex industryIndex) throws Exception {
        boolean flag = industryIndexService.updateById(industryIndex);
        return ApiResult.result(flag);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除IndustryIndex对象")
    public ApiResult<Boolean> deleteIndustryIndex(@PathVariable("id") Long id) throws Exception {
        boolean flag = industryIndexService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取IndustryIndex对象详情")
    public ApiResult<IndustryIndexQueryVo> getIndustryIndex(@PathVariable("id") Long id) throws Exception {
        IndustryIndexQueryVo industryIndexQueryVo = industryIndexService.getIndustryIndexById(id);
        return ApiResult.ok(industryIndexQueryVo);
    }

    @PostMapping("/getPageList")
    @ApiOperation("获取IndustryIndex分页列表")
    public ApiResult<Paging<IndustryConceptIndexVo>> getIndustryIndexPageList(@Valid @RequestBody IndustryConceptIndexQuery industryConceptIndexQuery) throws Exception {
        Paging<IndustryConceptIndexVo> paging = industryIndexService.getIndustryIndexPageList(industryConceptIndexQuery);
        return ApiResult.ok(paging);
    }

}
