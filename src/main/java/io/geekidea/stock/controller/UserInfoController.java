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

import io.geekidea.stock.entity.UserInfo;
import io.geekidea.stock.service.UserInfoService;
import io.geekidea.stock.dto.query.UserInfoQuery;
import io.geekidea.stock.dto.vo.UserInfoQueryVo;
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
 * 用户信息 前端控制器
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
@RequestMapping("/userInfo")
@Api("用户信息")
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/addUserInfo")
    @ApiOperation("添加UserInfo对象")
    public ApiResult<Boolean> addUserInfo(@Valid @RequestBody UserInfo userInfo) throws Exception {
        boolean flag = userInfoService.save(userInfo);
        return ApiResult.result(flag);
    }

    @PostMapping("/updateUserInfo")
    @ApiOperation("修改UserInfo对象")
    public ApiResult<Boolean> updateUserInfo(@Valid @RequestBody UserInfo userInfo) throws Exception {
        boolean flag = userInfoService.updateById(userInfo);
        return ApiResult.result(flag);
    }

    @PostMapping("/deleteUserInfo/{id}")
    @ApiOperation("删除UserInfo对象")
    public ApiResult<Boolean> deleteUserInfo(@PathVariable("id") Long id) throws Exception {
        boolean flag = userInfoService.removeById(id);
        return ApiResult.result(flag);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取UserInfo对象详情")
    public ApiResult<UserInfo> getUserInfo(@PathVariable("id") Long id) throws Exception {
        UserInfo userInfo = userInfoService.getUserInfoById(id);
        return ApiResult.ok(userInfo);
    }

    @PostMapping("/getUserInfoPageList")
    @ApiOperation("获取UserInfo分页列表")
    public ApiResult<Paging<UserInfo>> getUserInfoPageList(@Valid @RequestBody UserInfoQuery userInfoQuery) throws Exception {
        Paging<UserInfo> paging = userInfoService.getUserInfoPageList(userInfoQuery);
        return ApiResult.ok(paging);
    }

}
