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
 * 同步K线错误数据 查询结果对象
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-11-09
 */
@Data
@Accessors(chain = true)
@ApiModel("SyncKLineErrorDataQueryVo对象")
public class SyncKLineErrorDataQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

private Long id;

    @ApiModelProperty(value = "代码")
    private String lineCode;

    @ApiModelProperty(value = "名称")
    private String lineName;

    @ApiModelProperty(value = "序号")
    private Integer serialNumber;

    @ApiModelProperty(value = "天数")
    private Integer syncDay;

    @ApiModelProperty(value = "从那开始")
    private Date beforeEndDate;

    @ApiModelProperty(value = "是否更新")
    private Boolean isUpdate;

    @ApiModelProperty(value = "错误内容")
    private String errorMsg;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}