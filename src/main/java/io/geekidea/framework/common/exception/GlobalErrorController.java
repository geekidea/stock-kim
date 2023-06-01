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

package io.geekidea.framework.common.exception;

import io.geekidea.framework.common.api.ApiCode;
import io.geekidea.framework.common.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 全局Error/404处理
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-9-11
 */
@ApiIgnore
@RestController
@Slf4j
public class GlobalErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public ApiResult handleError(HttpServletRequest request, HttpServletResponse response){
        int status = response.getStatus();
        log.info("response status = " + status);
        switch (status){
            case HttpServletResponse.SC_UNAUTHORIZED:
                log.error("Unauthorized");
                return ApiResult.fail(ApiCode.UNAUTHORIZED);
            case HttpServletResponse.SC_FORBIDDEN:
                log.error("Permission denied");
                return ApiResult.fail(ApiCode.NOT_PERMISSION);
            case HttpServletResponse.SC_NOT_FOUND:
                log.error("404 NOT FOUND");
                return ApiResult.fail(ApiCode.NOT_FOUND);
            default:
                log.error("ERROR...");
                break;
        }
        return ApiResult.fail(ApiCode.FAIL);
    }

    @Override
    public String getErrorPath() {
        log.error("errorPath....");
        return ERROR_PATH;
    }
}
