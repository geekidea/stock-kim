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
import io.geekidea.framework.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <pre>
 * 自定义K线
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel("CustomKLine对象")
public class CustomKLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "类型,1:超跌反弹")
    private Integer type;

    @ApiModelProperty(value = "开始日期")
    private String startDate;

    @ApiModelProperty(value = "股票代码")
    @NotBlank(message = "股票代码不能为空")
    private String stockCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "K线代码")
    private String lineCode;

    @ApiModelProperty(value = "股票名称")
    private String stockName;

    @ApiModelProperty(value = "范围跌幅")
    private BigDecimal rangeIncrease;

    @ApiModelProperty(value = "所属行业")
    private String industry;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "涨幅%")
    private BigDecimal increase;

    @ApiModelProperty(value = "最高价日期")
    private String maxDate;

    @ApiModelProperty(value = "阶段最高价")
    private BigDecimal max;

    @ApiModelProperty(value = "最低价日期")
    private String minDate;

    @ApiModelProperty(value = "阶段最低价")
    private BigDecimal min;

    @ApiModelProperty(value = "相差的天数")
    private Integer diffDay;

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

    @ApiModelProperty(value = "批次")
    private String batchNo;

    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
