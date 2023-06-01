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
import io.geekidea.stock.service.ProgressService;
import io.geekidea.stock.dto.vo.ProgressVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/9
 **/
@Slf4j
@RestController
@RequestMapping("/progress")
@Api("同步数据进度")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/info")
    @ApiOperation("进度信息")
    public ApiResult<ProgressVo> getProgressInfo(String type) throws Exception {
        ProgressVo progressVo = progressService.getProgressInfo(type);
        return ApiResult.ok(progressVo);
    }

}
