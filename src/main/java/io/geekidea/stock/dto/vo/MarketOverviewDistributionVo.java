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

import java.util.List;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-28
 **/
@Data
@ApiModel("市场概况涨跌幅分布情况")
public class MarketOverviewDistributionVo {

    @ApiModelProperty("上涨下跌家数")
    private List<NameValuePercentageVo> stockMarketOverviews;
    @ApiModelProperty("涨跌停家数")
    private List<NameValuePercentageVo> riseFallLimits;
    @ApiModelProperty("涨幅分布")
    private List<NameValuePercentageVo> increaseDistributions;

}
