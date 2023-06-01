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

package io.geekidea.stock.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-19
 **/
@Data
@ApiModel("板块K线图查询参数")
public class BkKLineSearchQuery {

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("类型，1：行业，2：概念")
    private Integer type;

    @ApiModelProperty("行业或概念")
    private String name;

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("类型名称，industry concept")
    private String typeName;

    @ApiModelProperty("板块名称")
    private String bkName;

}
