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

import io.geekidea.stock.entity.UserStock;
import io.geekidea.stock.service.UserStockService;
import io.geekidea.stock.dto.query.UserStockQuery;
import io.geekidea.stock.dto.vo.UserStockQueryVo;
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
 * 用户持仓信息 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-19
 */
@Slf4j
@RestController
@RequestMapping("/userStock")
@Api("用户持仓信息")
public class UserStockController extends BaseController {

    @Autowired
    private UserStockService userStockService;

    @PostMapping("/addUserStock")
    @ApiOperation("添加UserStock对象")
    public ApiResult<Boolean> addUserStock(@Valid @RequestBody UserStock userStock) throws Exception {
        boolean flag = userStockService.save(userStock);
        return ApiResult.result(flag);
    }

    @PostMapping("/updateUserStock")
    @ApiOperation("修改UserStock对象")
    public ApiResult<Boolean> updateUserStock(@Valid @RequestBody UserStock userStock) throws Exception {
        boolean flag = userStockService.updateById(userStock);
        return ApiResult.result(flag);
    }

    @PostMapping("/deleteUserStock/{id}")
    @ApiOperation("删除UserStock对象")
    public ApiResult<Boolean> deleteUserStock(@PathVariable("id") Long id) throws Exception {
        boolean flag = userStockService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取UserStock对象详情")
    public ApiResult<UserStock> getUserStock(@PathVariable("id") Long id) throws Exception {
        UserStock userStock = userStockService.getUserStockById(id);
        return ApiResult.ok(userStock);
    }

    @PostMapping("/getUserStockList")
    @ApiOperation("获取UserStock列表")
    public ApiResult<List<UserStock>> getUserStockList(@Valid @RequestBody UserStockQuery userStockQuery) throws Exception {
        List<UserStock> list = userStockService.getUserStockList(userStockQuery);
        return ApiResult.ok(list);
    }

}
