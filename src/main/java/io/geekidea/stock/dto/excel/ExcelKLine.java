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

package io.geekidea.stock.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <pre>
 *
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-11
 */
@Data
@ApiModel("Excel KLine对象")
public class ExcelKLine {

    @ExcelProperty(value = "时间")
    private String lineDate;

    @ExcelProperty(value = "开盘")
    private BigDecimal openPrice;

    @ExcelProperty(value = "最高")
    private BigDecimal highPrice;

    @ExcelProperty(value = "最低")
    private BigDecimal lowPrice;

    @ExcelProperty(value = "收盘")
    private BigDecimal closePrice;

}
