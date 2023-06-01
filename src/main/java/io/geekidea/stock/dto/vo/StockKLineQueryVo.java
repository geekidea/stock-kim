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
 * 股票K线数据 查询结果对象
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-11-21
 */
@Data
@Accessors(chain = true)
@ApiModel("StockKLineQueryVo对象")
public class StockKLineQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "创建日期")
    private String lineDate;

    @ApiModelProperty(value = "代码")
    private String stockCode;

    @ApiModelProperty(value = "名称")
    private String stockName;

    @ApiModelProperty(value = "开盘价")
    private BigDecimal openPrice;

    @ApiModelProperty(value = "收盘价")
    private BigDecimal closePrice;

    @ApiModelProperty(value = "最低价")
    private BigDecimal lowPrice;

    @ApiModelProperty(value = "最高价")
    private BigDecimal highPrice;

    @ApiModelProperty(value = "成交数量，单位万")
    private BigDecimal tradeNumber;

    @ApiModelProperty(value = "换手率%")
    private BigDecimal turnoverRate;

    @ApiModelProperty(value = "成交金额(单位万)")
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "振幅%")
    private BigDecimal amplitude;

    @ApiModelProperty(value = "涨幅%")
    private BigDecimal increase;

    @ApiModelProperty(value = "涨幅类型 4：涨停，3：5%～涨停，2：1%～5%，1：0%～1%，0：平盘，-1：0～-1%，-2：-1%～-5%，-3：-5%～-10%,-4:跌停")
    private Integer increaseType;

    @ApiModelProperty(value = "涨幅状态 1：上涨，0：平盘，-1：下跌")
    private Integer increaseState;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}