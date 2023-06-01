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
 * 行业概念排行类型枚举 industry：行业，concept：概念
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-27
 **/
public enum IndustryConceptTypeEnum {
    INDUSTRY("industry", "行业"),
    CONCEPT("concept", "概念");

    private String code;
    private String desc;

    IndustryConceptTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static IndustryConceptTypeEnum getByCode(String code) {
        for (IndustryConceptTypeEnum StockSearchTypeEnum : values()) {
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
