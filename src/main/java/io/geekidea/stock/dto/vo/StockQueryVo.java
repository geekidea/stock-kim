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
 * 股票信息 查询结果对象
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
@ApiModel("StockQueryVo对象")
public class StockQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "股票代码")
    private String stockCode;

    @ApiModelProperty(value = "股票名称")
    private String stockName;

    @ApiModelProperty(value = "股票代码全称")
    private String stockFullCode;

    @ApiModelProperty(value = "名称首字母")
    private String stockLetter;

    @ApiModelProperty(value = "所属行业")
    private String industry;

    @ApiModelProperty(value = "最新价格")
    private BigDecimal price;

    @ApiModelProperty(value = "涨幅%")
    private BigDecimal increase;

    @ApiModelProperty(value = "3日涨幅%")
    private BigDecimal i3;

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

    @ApiModelProperty(value = "3日均线")
    private BigDecimal ma3;

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

    @ApiModelProperty(value = "昨日收盘价")
    private BigDecimal yesterdayClosingPrice;

    @ApiModelProperty(value = "总市值")
    private BigDecimal totalMarketValue;

    @ApiModelProperty(value = "流通市值")
    private BigDecimal circulationMarketValue;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "换手率%")
    private BigDecimal turnoverRate;

    @ApiModelProperty(value = "量比")
    private BigDecimal volumeRatio;

    @ApiModelProperty(value = "振幅%")
    private BigDecimal amplitude;

    @ApiModelProperty(value = "类型：0：所有，1：自选")
    private Integer type;

    @ApiModelProperty(value = "今日开盘价")
    private BigDecimal openPrice;

    @ApiModelProperty(value = "今日最高价")
    private BigDecimal highPrice;

    @ApiModelProperty(value = "今日最低价")
    private BigDecimal lowPrice;

    @ApiModelProperty(value = "成交数量，单位万")
    private BigDecimal tradeNumber;

    @ApiModelProperty(value = "市场类型 1:上证主板 2：深证主板 3：创业板")
    private Integer marketType;

    @ApiModelProperty(value = "市场类型名称")
    private String marketTypeName;

    @ApiModelProperty(value = "是否停牌，0：未停牌，1：停牌")
    private Integer suspensionYn;

    @ApiModelProperty(value = "是否是锂电池概念")
    private Integer lithiumYn;

    @ApiModelProperty(value = "锂电池概念名称")
    private String lithiumConceptName;

    @ApiModelProperty(value = "是否是光伏概念")
    private Integer tanYn;

    @ApiModelProperty(value = "光伏概念名称")
    private String tanConceptName;

    @ApiModelProperty(value = "是否是医疗概念")
    private Integer medicalYn;

    @ApiModelProperty(value = "医疗概念名称")
    private String medicalConceptName;

    @ApiModelProperty(value = "是否是化工概念")
    private Integer chemicalYn;

    @ApiModelProperty(value = "化工概念名称")
    private String chemicalConceptName;

    @ApiModelProperty(value = "是否是汽车概念")
    private Integer carYn;

    @ApiModelProperty(value = "汽车概念名称")
    private String carConceptName;

    @ApiModelProperty(value = "是否是有色金属概念")
    private Integer metalsYn;

    @ApiModelProperty(value = "有色金属概念名称")
    private String metalsConceptName;

    @ApiModelProperty(value = "是否是酒概念")
    private Integer alcoholYn;

    @ApiModelProperty(value = "酒概念名称")
    private String alcoholConceptName;

    @ApiModelProperty(value = "是否是半导体芯片概念")
    private Integer soxYn;

    @ApiModelProperty(value = "半导体芯片概念名称")
    private String soxConceptName;

    @ApiModelProperty(value = "是否是军工概念")
    private Integer militaryYn;

    @ApiModelProperty(value = "军工概念名称")
    private String militaryConceptName;

    @ApiModelProperty(value = "是否是新能源概念")
    private Integer newEnergyYn;

    @ApiModelProperty(value = "新能源概念名称")
    private String newEnergyConceptName;

    @ApiModelProperty(value = "是否是传统能源概念")
    private Integer energyYn;

    @ApiModelProperty(value = "传统能源概念名称")
    private String energyConceptName;

    @ApiModelProperty(value = "自动排序")
    private String autoSort;

    @ApiModelProperty(value = "实时数据更新日期")
    private Date realDate;

    @ApiModelProperty(value = "实时数据更新时间")
    private Date realTime;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "上市日期")
    private Date listingDate;

    @ApiModelProperty(value = "自选日期")
    private Date optionalDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "0:正常，1：删除")
    private Integer deleted;

    @ApiModelProperty(value = "删除原因")
    private String deletedRemark;

}