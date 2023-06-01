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

package io.geekidea.framework.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-9-11
 */
@ApiModel("主键状态VO")
public class IdStatusParam implements Serializable {
    private static final long serialVersionUID = -7581307955242965701L;

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("状态,1:启用 0:禁用")
    private Integer status;

}
