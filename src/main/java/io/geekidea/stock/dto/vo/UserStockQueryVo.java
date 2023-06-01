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
 * 用户持仓信息 查询结果对象
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-11-27
 */
@Data
@Accessors(chain = true)
@ApiModel("UserStockQueryVo对象")
public class UserStockQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "持仓股票代码")
    private String stockCode;

    @ApiModelProperty(value = "持仓股票名称")
    private String stockName;

    @ApiModelProperty(value = "持仓市值")
    private BigDecimal holdMarketValue;

    @ApiModelProperty(value = "持仓数量")
    private Integer holdCount;

    @ApiModelProperty(value = "可用数量")
    private Integer useCount;

    @ApiModelProperty(value = "现价")
    private BigDecimal currentPrice;

    @ApiModelProperty(value = "成本价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "持仓盈亏")
    private BigDecimal holdAmount;

    @ApiModelProperty(value = "持仓涨幅")
    private BigDecimal holdIncrease;

    @ApiModelProperty(value = "当日盈亏")
    private BigDecimal todayHoldAmount;

    @ApiModelProperty(value = "个股仓位")
    private BigDecimal percentage;

    @ApiModelProperty(value = "买入日期")
    private Date buyDate;

    @ApiModelProperty(value = "买入价格")
    private BigDecimal buyPrice;

    @ApiModelProperty(value = "买入涨幅")
    private BigDecimal buyIncrease;

    @ApiModelProperty(value = "持有天数")
    private Integer holdDay;

    @ApiModelProperty(value = "市场类型名称")
    private String marketTypeName;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}