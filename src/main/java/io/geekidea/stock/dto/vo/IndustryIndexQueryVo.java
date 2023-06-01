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
import lombok.experimental.Accessors;
import java.io.Serializable;

import java.util.Date;
import java.math.BigDecimal;

/**
 * <pre>
 * 行业指数 查询结果对象
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-04
 */
@Data
@Accessors(chain = true)
@ApiModel("IndustryIndexQueryVo对象")
public class IndustryIndexQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "行业名称")
    private String industryName;

    @ApiModelProperty(value = "行业指数")
    private BigDecimal industryIndex;

    @ApiModelProperty(value = "个股数量")
    private Integer industryCount;

    @ApiModelProperty(value = "最大涨幅代码")
    private String maxStockCode;

    @ApiModelProperty(value = "最大涨幅名称")
    private String maxStockName;

    @ApiModelProperty(value = "最大涨幅")
    private BigDecimal maxIncrease;

    @ApiModelProperty(value = "最大涨幅价格")
    private BigDecimal maxIncreasePrice;

    @ApiModelProperty(value = "总市值")
    private BigDecimal totalMarketValue;

    @ApiModelProperty(value = "流通市值")
    private BigDecimal totalCirculationValue;

    @ApiModelProperty(value = "成交金额")
    private BigDecimal totalTradeAmount;

    @ApiModelProperty(value = "成交数量")
    private BigDecimal totalTradeNumber;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}