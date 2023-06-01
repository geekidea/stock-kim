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

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 板块信息
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel("BkInfo对象")
public class BkInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "板块编码")
    @TableId(value = "bk_code", type = IdType.INPUT)
    private String bkCode;

    @ApiModelProperty(value = "板块名称")
    private String bkName;

    @ApiModelProperty(value = "板块别名")
    private String bkAliasName;

    @ApiModelProperty(value = "板块首字母")
    private String bkLetter;

    @ApiModelProperty(value = "板块排序")
    private Integer sort;

    @ApiModelProperty(value = "板块类型")
    private Integer bkType;

    @ApiModelProperty(value = "板块类型名称")
    private String bkTypeName;

    @ApiModelProperty(value = "板块价格")
    private BigDecimal bkPrice;

    @ApiModelProperty(value = "板块涨幅")
    private BigDecimal bkIncrease;

    @ApiModelProperty(value = "个股数量")
    private Integer bkCount;

    @ApiModelProperty(value = "上涨数量")
    private Integer riseCount;

    @ApiModelProperty(value = "下跌数量")
    private Integer fallCount;

    @ApiModelProperty(value = "平盘数量")
    private Integer flatCount;

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

    @ApiModelProperty(value = "3-60日平均涨幅%")
    private BigDecimal iAvg;

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

    @ApiModelProperty(value = "查询SQL")
    private String bkQuerySql;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "是否是机会板块，0：不是，1：是")
    private Integer opportunityYn;

    @ApiModelProperty("是否自选，false：否，true：是")
    private boolean optionalYn;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建日期")
    private Date createDate;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "最大涨幅的股票信息")
    private String maxStockInfo;

}
