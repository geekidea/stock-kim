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
 * 股票概念
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-11
 */
@Data
@ApiModel("ConceptExcelStock对象")
public class ConceptExcelStock {

    @ExcelProperty(value = "股票代码")
    private String stockCode;

    @ExcelProperty(value = "股票简称")
    private String stockName;

    @ExcelProperty(value = "所属概念")
    private String conceptContent;

    @ExcelProperty(value = "所属概念数量(个)")
    private String conceptCount;

}
