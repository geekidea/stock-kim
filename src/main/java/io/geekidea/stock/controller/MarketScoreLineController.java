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
import io.geekidea.stock.dto.vo.LineNamveValueVo;
import io.geekidea.stock.service.MarketScoreLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * <pre>
 * 市场分数 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-21
 */
@Slf4j
@RestController
@RequestMapping("/marketScoreLine")
@Api("市场分数")
public class MarketScoreLineController extends BaseController {

    @Autowired
    private MarketScoreLineService marketScoreLineService;

    @PostMapping("/getMarketScoreLineList")
    @ApiOperation("获取MarketScoreLine列表")
    public ApiResult<List<LineNamveValueVo>> getMarketScoreLineList() throws Exception {
        List<LineNamveValueVo> list = marketScoreLineService.getMarketScoreLineList();
        return ApiResult.ok(list);
    }

    @PostMapping("/calcMarketScore")
    @ApiOperation("计算市场分数")
    public ApiResult calcMarketScore() throws Exception {
        marketScoreLineService.calcMarketScore();
        return ApiResult.ok();
    }

}
