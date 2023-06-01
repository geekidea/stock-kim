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

import io.geekidea.framework.common.query.BaseOrderPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <pre>
 * 板块信息 查询参数对象
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-11-06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel("BkInfo查询参数")
public class BkInfoQuery extends BaseOrderPageQuery {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否是机会板块，0：不是，1：是")
    private Integer bkOpportunity;

    @ApiModelProperty(value = "i3-i20平均涨幅，1：涨幅大于0，0：涨幅小于或等于0")
    private Integer bkIAvg;

    @ApiModelProperty(value = "板块策略，1：多头趋势")
    private String bkStrategy;

    @ApiModelProperty(value = "板块类型")
    private Integer bkType;

    @ApiModelProperty(value = "是否过滤")
    private boolean filter;

    @ApiModelProperty(value = "板块数量")
    private Integer bkCount;

    @ApiModelProperty(value = "搜索类型，1：板块，2：个股")
    private Integer searchType;

    private boolean optionalYn;


}
