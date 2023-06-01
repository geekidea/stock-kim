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

import io.geekidea.stock.entity.UserStockBill;
import io.geekidea.stock.service.UserStockBillService;
import io.geekidea.stock.dto.query.UserStockBillQuery;
import io.geekidea.stock.dto.vo.UserStockBillQueryVo;
import io.geekidea.framework.common.api.ApiResult;
import io.geekidea.framework.common.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import io.geekidea.framework.common.vo.Paging;

/**
 * <pre>
 * 用户持仓流水 前端控制器
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
@RequestMapping("/userStockBill")
@Api("用户持仓流水")
public class UserStockBillController extends BaseController {

    @Autowired
    private UserStockBillService userStockBillService;

    @PostMapping("/addUserStockBill")
    @ApiOperation("添加UserStockBill对象")
    public ApiResult<Boolean> addUserStockBill(@Valid @RequestBody UserStockBill userStockBill) throws Exception {
        boolean flag = userStockBillService.save(userStockBill);
        return ApiResult.result(flag);
    }

    @PostMapping("/updateUserStockBill")
    @ApiOperation("修改UserStockBill对象")
    public ApiResult<Boolean> updateUserStockBill(@Valid @RequestBody UserStockBill userStockBill) throws Exception {
        boolean flag = userStockBillService.updateById(userStockBill);
        return ApiResult.result(flag);
    }

    @PostMapping("/deleteUserStockBill/{id}")
    @ApiOperation("删除UserStockBill对象")
    public ApiResult<Boolean> deleteUserStockBill(@PathVariable("id") Long id) throws Exception {
        boolean flag = userStockBillService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取UserStockBill对象详情")
    public ApiResult<UserStockBill> getUserStockBill(@PathVariable("id") Long id) throws Exception {
        UserStockBill userStockBill = userStockBillService.getUserStockBillById(id);
        return ApiResult.ok(userStockBill);
    }

    @PostMapping("/getUserStockBillPageList")
    @ApiOperation("获取UserStockBill分页列表")
    public ApiResult<Paging<UserStockBill>> getUserStockBillPageList(@Valid @RequestBody UserStockBillQuery userStockBillQuery) throws Exception {
        Paging<UserStockBill> paging = userStockBillService.getUserStockBillPageList(userStockBillQuery);
        return ApiResult.ok(paging);
    }

}
