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
 * 板块K线
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
@ApiModel("BkKLine对象")
public class BkKLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建日期")
    @NotBlank(message = "创建日期不能为空")
    private String lineDate;

    @ApiModelProperty(value = "代码")
    @NotBlank(message = "代码不能为空")
    private String lineCode;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String lineName;

    @ApiModelProperty(value = "开盘价")
    @NotNull(message = "开盘价不能为空")
    private BigDecimal openPrice;

    @ApiModelProperty(value = "收盘价")
    @NotNull(message = "收盘价不能为空")
    private BigDecimal closePrice;

    @ApiModelProperty(value = "最低价")
    @NotNull(message = "最低价不能为空")
    private BigDecimal lowPrice;

    @ApiModelProperty(value = "最高价")
    @NotNull(message = "最高价不能为空")
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

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "5日涨幅%")
    private BigDecimal i5;

    @ApiModelProperty(value = "10日涨幅%")
    private BigDecimal i10;

    @ApiModelProperty(value = "20日涨幅%")
    private BigDecimal i20;

    @ApiModelProperty(value = "30日涨幅%")
    private BigDecimal i30;

    @ApiModelProperty(value = "60日涨幅%")
    private BigDecimal i60;

    @ApiModelProperty(value = "90日涨幅%")
    private BigDecimal i90;

    @ApiModelProperty(value = "120日涨幅%")
    private BigDecimal i120;

    @ApiModelProperty(value = "250日涨幅%")
    private BigDecimal i250;

    @ApiModelProperty(value = "300日涨幅%")
    private BigDecimal i300;

    @ApiModelProperty(value = "5日均线")
    private BigDecimal ma5;

    @ApiModelProperty(value = "10日均线")
    private BigDecimal ma10;

    @ApiModelProperty(value = "20日均线")
    private BigDecimal ma20;

    @ApiModelProperty(value = "30日均线")
    private BigDecimal ma30;

    @ApiModelProperty(value = "60日均线")
    private BigDecimal ma60;

    @ApiModelProperty(value = "90日均线")
    private BigDecimal ma90;

    @ApiModelProperty(value = "120日均线")
    private BigDecimal ma120;

    @ApiModelProperty(value = "250日均线")
    private BigDecimal ma250;

    @ApiModelProperty(value = "300日均线")
    private BigDecimal ma300;

    @ApiModelProperty("是否自选，false：否，true：是")
    private boolean optionalYn;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
