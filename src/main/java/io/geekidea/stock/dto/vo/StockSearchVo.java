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

package io.geekidea.stock.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/23
 **/
@Data
@ApiModel("股票搜索结果")
public class StockSearchVo {

    @ApiModelProperty("代码")
    private String stockCode;

    @ApiModelProperty("名称")
    private String stockName;

    @ApiModelProperty("市场类型,1:上证，2：深证，3：创业板")
    private Integer marketType;

    @ApiModelProperty("市场类型名称")
    private String marketTypeName;

    @ApiModelProperty("当前板块中是否已存在该股票")
    private boolean exists;


}
