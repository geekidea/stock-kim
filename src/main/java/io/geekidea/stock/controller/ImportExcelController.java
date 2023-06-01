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
import io.geekidea.stock.service.ImportExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>
 *  Excel 前端控制器
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-11
 */
@Slf4j
@RestController
@RequestMapping("/excel")
@Api("导入Excel")
public class ImportExcelController extends BaseController {

    @Autowired
    private ImportExcelService importExcelService;

    @PostMapping("/initImportStock")
    @ApiOperation("初始化导入股票")
    public ApiResult<Boolean> initImportStock(@RequestParam("file") MultipartFile multipartFile, Integer marketType) throws Exception {
        importExcelService.initImportStock(multipartFile, marketType);
        return ApiResult.ok();
    }

    @PostMapping("/importStockExcel")
    @ApiOperation("导入自选股")
    public ApiResult<Boolean> importStockExcel(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        importExcelService.importStockExcel(multipartFile);
        return ApiResult.ok();
    }

    @PostMapping("/importConceptData")
    @ApiOperation("导入概念数据")
    public ApiResult<Boolean> importConceptData(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        importExcelService.importConceptData(multipartFile);
        return ApiResult.ok();
    }

    @PostMapping("/importThsStockExcel")
    @ApiOperation("导入同花顺股票")
    public ApiResult<Boolean> importThsStockExcel(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        importExcelService.importThsStockExcel(multipartFile);
        return ApiResult.ok();
    }

}
