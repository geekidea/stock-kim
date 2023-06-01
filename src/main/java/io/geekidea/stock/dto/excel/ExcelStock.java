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
@ApiModel("Excel Stock对象")
public class ExcelStock {

    @ExcelProperty(value = "代码")
    private String stockCode;

    @ExcelProperty(value = "名称")
    private String stockName;

    @ExcelProperty(value = "最新")
    private BigDecimal latestPrice;

    @ExcelProperty(value = "涨幅%")
    private BigDecimal increase;

    @ExcelProperty(value = "昨收")
    private BigDecimal yesterdayClosingPrice;

    @ExcelProperty(value = "总市值")
    private String totalMarketValue;

    @ExcelProperty(value = "流通市值")
    private String circulationMarketValue;

    @ExcelProperty(value = "金额")
    private String tradeAmount;

    @ExcelProperty(value = "所属行业")
    private String industry;

    @ExcelProperty(value = "换手%")
    private BigDecimal turnoverRate;

    @ExcelProperty(value = "量比")
    private BigDecimal volumeRatio;

    @ExcelProperty(value = "振幅%")
    private BigDecimal amplitude;

    @ExcelProperty(value = "5日涨幅%")
    private BigDecimal fiveDayIncrease;

    @ExcelProperty(value = "10日涨幅%")
    private BigDecimal tenDayIncrease;

    @ExcelProperty(value = "20日涨幅%")
    private BigDecimal twentyDayIncrease;

    @ExcelProperty(value = "本月涨幅%")
    private BigDecimal currentMonthIncrease;

    @ExcelProperty(value = "近一月涨幅%")
    private BigDecimal latelyMonthIncrease;

    @ExcelProperty(value = "今年涨幅%")
    private BigDecimal currentYearIncrease;

    @ExcelProperty(value = "近一年涨幅%")
    private BigDecimal latelyYearIncrease;

    @ExcelProperty(value = "连涨天数")
    private String continuousRisingDays;

    @ExcelProperty(value = "上市日期")
    private String listingDate;

    @ExcelProperty(value = "自选时间")
    private String optionalDate;

}
