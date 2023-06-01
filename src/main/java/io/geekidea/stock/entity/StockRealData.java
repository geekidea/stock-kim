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

package io.geekidea.stock.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.geekidea.framework.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * <pre>
 * 股票实时数据
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel("StockRealData对象")
public class StockRealData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "批次号")
    @NotBlank(message = "批次号不能为空")
    private String batchNo;

    @ApiModelProperty(value = "股票代码")
    @NotBlank(message = "股票代码不能为空")
    private String stockCode;

    @ApiModelProperty(value = "股票名称")
    private String stockName;

    @ApiModelProperty(value = "最新价格")
    private BigDecimal currentPrice;

    @ApiModelProperty(value = "最新涨幅%")
    private BigDecimal currentIncrease;

    @ApiModelProperty(value = "今日开盘价")
    private BigDecimal openingPrice;

    @ApiModelProperty(value = "昨日收盘价")
    private BigDecimal yesterdayClosingPrice;

    @ApiModelProperty(value = "今日最高价")
    private BigDecimal highPrice;

    @ApiModelProperty(value = "今日最低价")
    private BigDecimal lowPrice;

    @ApiModelProperty(value = "成交数量，单位万")
    private BigDecimal tradeNumber;

    @ApiModelProperty(value = "成交金额，单位亿")
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "买一价")
    private BigDecimal buyOnePrice;

    @ApiModelProperty(value = "卖一价")
    private BigDecimal sellOnePrice;

    @ApiModelProperty(value = "买一量")
    private BigDecimal buyOneNumber;

    @ApiModelProperty(value = "卖一量")
    private BigDecimal sellOneNumber;

    @ApiModelProperty(value = "买二量")
    private BigDecimal buyTwoNumber;

    @ApiModelProperty(value = "买二价")
    private BigDecimal buyTwoPrice;

    @ApiModelProperty(value = "买三量")
    private BigDecimal buyThreeNumber;

    @ApiModelProperty(value = "买三价")
    private BigDecimal buyThreePrice;

    @ApiModelProperty(value = "买四量")
    private BigDecimal buyFourNumber;

    @ApiModelProperty(value = "买四价")
    private BigDecimal buyFourPrice;

    @ApiModelProperty(value = "买五量")
    private BigDecimal buyFiveNumber;

    @ApiModelProperty(value = "买五价")
    private BigDecimal buyFivePrice;

    @ApiModelProperty(value = "卖二量")
    private BigDecimal sellTwoNumber;

    @ApiModelProperty(value = "卖二价")
    private BigDecimal sellTwoPrice;

    @ApiModelProperty(value = "卖三量")
    private BigDecimal sellThreeNumber;

    @ApiModelProperty(value = "卖三价")
    private BigDecimal sellThreePrice;

    @ApiModelProperty(value = "卖四量")
    private BigDecimal sellFourNumber;

    @ApiModelProperty(value = "卖四价")
    private BigDecimal sellFourPrice;

    @ApiModelProperty(value = "卖五量")
    private BigDecimal sellFiveNumber;

    @ApiModelProperty(value = "卖五价")
    private BigDecimal sellFivePrice;

    @ApiModelProperty(value = "是否停牌，0：未停牌，1：停牌")
    private Integer suspensionYn;

    @ApiModelProperty(value = "当前日期")
    private Date realDate;

    @ApiModelProperty(value = "当前时间")
    private Date realTime;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
