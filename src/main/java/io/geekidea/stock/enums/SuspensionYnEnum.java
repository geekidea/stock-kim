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
 * 是否停牌
 * 是否停牌，0：未停牌，1：停牌
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-27
 **/
public enum SuspensionYnEnum {
    NO(0, "未停牌"),
    YES(1, "停牌");

    private int code;
    private String desc;

    SuspensionYnEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SuspensionYnEnum getByCode(int code) {
        for (SuspensionYnEnum StockSearchTypeEnum : values()) {
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
