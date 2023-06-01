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
import io.geekidea.stock.dto.query.StockSearchQuery;
import io.geekidea.stock.dto.vo.StockSearchVo;
import io.geekidea.stock.service.StockSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/23
 **/
@Slf4j
@RestController
@RequestMapping("/stockSearch")
@Api("股票搜索")
public class StockSearchController {

    @Autowired
    private StockSearchService stockSearchService;

    @PostMapping("")
    @ApiOperation("获取StockRiseLine分页列表")
    public ApiResult<List<StockSearchVo>> search(@Valid @RequestBody StockSearchQuery stockSearchQuery) throws Exception {
        List<StockSearchVo> list = stockSearchService.search(stockSearchQuery);
        return ApiResult.ok(list);
    }

}
