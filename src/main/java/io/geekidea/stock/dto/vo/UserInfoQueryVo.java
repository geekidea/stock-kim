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
 * 用户信息 查询结果对象
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-11-19
 */
@Data
@Accessors(chain = true)
@ApiModel("UserInfoQueryVo对象")
public class UserInfoQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "密码")
    private String pwd;

    @ApiModelProperty(value = "总资产")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "总市值")
    private BigDecimal totalMarketValue;

    @ApiModelProperty(value = "持仓总盈亏")
    private BigDecimal totalHoldAmount;

    @ApiModelProperty(value = "持仓总数量")
    private Integer totalHoldCount;

    @ApiModelProperty(value = "当日盈亏")
    private BigDecimal totalCurrentAmount;

    @ApiModelProperty(value = "可用金额")
    private BigDecimal useAmount;

    @ApiModelProperty(value = "当日涨幅")
    private BigDecimal increase;

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