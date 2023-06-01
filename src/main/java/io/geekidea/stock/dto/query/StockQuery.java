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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import io.geekidea.framework.common.query.BaseOrderPageQuery;

/**
 * <pre>
 *  查询参数对象
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel("Stock查询参数")
public class StockQuery extends BaseOrderPageQuery {

    @ApiModelProperty("类型 0：全部，1：自选，2：行业，3：概念")
    private Integer type;

    @ApiModelProperty("行业名称")
    private String industry;

    @ApiModelProperty("概念名称")
    private String concept;

    @ApiModelProperty("涨跌范围分布类型")
    private Integer distributionType;

    @ApiModelProperty("5-10-20涨幅是否大于0")
    private boolean day51020Increase;

    @ApiModelProperty("股票列表")
    private String[] stockCodes;

    private boolean optionalYn;

}
