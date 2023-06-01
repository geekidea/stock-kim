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

package io.geekidea.framework.enums;

import io.geekidea.framework.common.enums.BaseTypeStateEnum;

/**
 * YES/No状态码 0/1
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2020-02-20
 **/
public enum YesNoEnum implements BaseTypeStateEnum {
    YES(1, "yes"),
    NO(0, "no");

    private Integer key;
    private String value;

    YesNoEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
