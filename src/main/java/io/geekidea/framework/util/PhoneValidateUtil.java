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

package io.geekidea.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 手机号码正则表达式校验
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2020/2/20
 **/
public class PhoneValidateUtil {
    private static final String REG_EX = "^1[3,4,5,6,7,8,9]\\d{9}$";
    private static final Pattern PATTERN = Pattern.compile(REG_EX);

    public static boolean validate(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PATTERN.matcher(phone).matches();
    }

}
