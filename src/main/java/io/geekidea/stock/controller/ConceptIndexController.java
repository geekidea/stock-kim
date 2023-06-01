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
import io.geekidea.stock.entity.ConceptIndex;
import io.geekidea.stock.service.ConceptIndexService;
import io.geekidea.stock.dto.vo.ConceptIndexQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

/**
 * <pre>
 * 概念指数 前端控制器
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
@RequestMapping("/conceptIndex")
@Api("概念指数")
public class ConceptIndexController extends BaseController {

    @Autowired
    private ConceptIndexService conceptIndexService;

    @PostMapping("/add")
    @ApiOperation("添加ConceptIndex对象")
    public ApiResult<Boolean> addConceptIndex(@Valid @RequestBody ConceptIndex conceptIndex) throws Exception {
        boolean flag = conceptIndexService.save(conceptIndex);
        return ApiResult.result(flag);
    }

    @PostMapping("/update")
    @ApiOperation("修改ConceptIndex对象")
    public ApiResult<Boolean> updateConceptIndex(@Valid @RequestBody ConceptIndex conceptIndex) throws Exception {
        boolean flag = conceptIndexService.updateById(conceptIndex);
        return ApiResult.result(flag);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除ConceptIndex对象")
    public ApiResult<Boolean> deleteConceptIndex(@PathVariable("id") Long id) throws Exception {
        boolean flag = conceptIndexService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取ConceptIndex对象详情")
    public ApiResult<ConceptIndexQueryVo> getConceptIndex(@PathVariable("id") Long id) throws Exception {
        ConceptIndexQueryVo conceptIndexQueryVo = conceptIndexService.getConceptIndexById(id);
        return ApiResult.ok(conceptIndexQueryVo);
    }

    @PostMapping("/getPageList")
    @ApiOperation("获取ConceptIndex分页列表")
    public ApiResult<Paging<IndustryConceptIndexVo>> getConceptIndexPageList(@Valid @RequestBody IndustryConceptIndexQuery industryConceptIndexQuery) throws Exception {
        Paging<IndustryConceptIndexVo> paging = conceptIndexService.getConceptIndexPageList(industryConceptIndexQuery);
        return ApiResult.ok(paging);
    }

}
