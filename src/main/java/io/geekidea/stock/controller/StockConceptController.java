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
import io.geekidea.stock.service.StockConceptService;
import io.geekidea.stock.dto.query.StockConceptQuery;
import io.geekidea.stock.dto.vo.StockConceptQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

/**
 * <pre>
 * 股票概念 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-24
 */
@Slf4j
@RestController
@RequestMapping("/stockConcept")
@Api("股票概念")
public class StockConceptController extends BaseController {

    @Autowired
    private StockConceptService stockConceptService;

    @PostMapping("/getPageList")
    @ApiOperation("获取StockConcept分页列表")
    public ApiResult<Paging<StockConceptQueryVo>> getStockConceptPageList(@Valid @RequestBody StockConceptQuery stockConceptQuery) throws Exception {
        Paging<StockConceptQueryVo> paging = stockConceptService.getStockConceptPageList(stockConceptQuery);
        return ApiResult.ok(paging);
    }


}
