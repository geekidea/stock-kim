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

package io.geekidea.stock.enums;

/**
 * 股票搜索类型
 * 类型 0：全部，1：自选，2：行业，3：概念
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-25
 **/
public enum StockSearchTypeEnum {
    ALL(0, "全部"),
    OPTIONAL(1, "自选"),
    INDUSTRY(2, "行业"),
    CONCEPT(3, "概念"),
    DISTRIBUTION(4, "涨跌范围分布"),
    BK(5, "板块搜索"),
    ;

    private int code;
    private String desc;

    StockSearchTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StockSearchTypeEnum getByCode(int code) {
        for (StockSearchTypeEnum StockSearchTypeEnum : values()) {
            if (code == StockSearchTypeEnum.getCode()) {
                return StockSearchTypeEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
