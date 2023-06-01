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
 *  查询结果对象
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
@ApiModel("StockRecordQueryVo对象")
public class StockRecordQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "序号")
    private Integer serialNumber;

    @ApiModelProperty(value = "股票代码")
    private String stockCode;

    @ApiModelProperty(value = "股票名称")
    private String stockName;

    @ApiModelProperty(value = "最新价格")
    private BigDecimal price;

    @ApiModelProperty(value = "涨幅%")
    private BigDecimal increase;

    @ApiModelProperty(value = "昨日收盘价")
    private BigDecimal yesterdayClosingPrice;

    @ApiModelProperty(value = "总市值")
    private BigDecimal totalMarketValue;

    @ApiModelProperty(value = "流通市值")
    private BigDecimal circulationMarketValue;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "所属行业")
    private String industry;

    @ApiModelProperty(value = "换手率%")
    private BigDecimal turnoverRate;

    @ApiModelProperty(value = "量比")
    private BigDecimal volumeRatio;

    @ApiModelProperty(value = "振幅%")
    private BigDecimal amplitude;

    @ApiModelProperty(value = "5日涨幅%")
    private BigDecimal fiveDayIncrease;

    @ApiModelProperty(value = "10日涨幅%")
    private BigDecimal tenDayIncrease;

    @ApiModelProperty(value = "20日涨幅%")
    private BigDecimal twentyDayIncrease;

    @ApiModelProperty(value = "本月涨幅%")
    private BigDecimal currentMonthIncrease;

    @ApiModelProperty(value = "最近一个月涨幅%")
    private BigDecimal latelyMonthIncrease;

    @ApiModelProperty(value = "今年涨幅%")
    private BigDecimal currentYearIncrease;

    @ApiModelProperty(value = "最近一年涨幅%")
    private BigDecimal latelyYearIncrease;

    @ApiModelProperty(value = "连涨天数")
    private Integer continuousRisingDays;

    @ApiModelProperty(value = "上市日期")
    private Date listingDate;

    @ApiModelProperty(value = "自选日期")
    private Date optionalDate;

    @ApiModelProperty(value = "类型：0：股票，2：板块，3：ETF")
    private Integer type;

    @ApiModelProperty(value = "自动排序")
    private String autoSort;

    @ApiModelProperty(value = "删除，0：未删除，1：已删除")
    private Integer deleted;

    @ApiModelProperty(value = "是否停牌，0：未停牌，1：停牌")
    private Integer suspensionYn;

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