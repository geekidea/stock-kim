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
 * 涨停 涨停～5% 5%～1% 1%～0% 平盘 0～-1% -1～-5 -5～跌停 跌停
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-4
 **/
public enum DistributionTypeEnum {
    rise4("4", "涨停"),
    rise3("3", "涨停～5%"),
    rise2("2", "5%～1%"),
    rise1("1", "1%～0%"),
    zero("0", "平盘"),
    fall1("-1", "0～-1%"),
    fall2("-2", "-1～-5"),
    fall3("-3", "-5～跌停"),
    fall4("-4", "跌停");

    private String code;
    private String desc;

    DistributionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DistributionTypeEnum getByCode(String code) {
        for (DistributionTypeEnum StockSearchTypeEnum : values()) {
            if (StockSearchTypeEnum.getCode().equals(code)) {
                return StockSearchTypeEnum;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
