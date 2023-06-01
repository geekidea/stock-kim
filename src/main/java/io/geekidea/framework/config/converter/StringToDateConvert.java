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

package io.geekidea.framework.config.converter;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <code>
 * <pre>
 * 日期转换器,将请求参数的日期字符串转换成java.util.Date类型
 * 日期格式顺序:
 * 	1.yyyy-MM-dd HH:mm:ss:S
 * 	2.yyyy-MM-dd HH:mm:ss
 * 	3.yyyy-MM-dd HH:mm
 * 	4.yyyy-MM-dd HH
 * 	5.yyyy-MM-dd
 * </pre>
 * </code>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-9-11
 */
public class StringToDateConvert {

    /**
     * <code>
     * <pre>
     * 1.如果日期字符串为空,则直接返回空
     * 2.使用格式化组进行格式化,如果解析成功,则直接返回
     * 4.否则,抛出非法参数异常
     * @param source 请求的日期参数
     * @return 解析后的日期类型:java.util.Date
     * @exception IllegalArgumentException 非法参数异常
     * </pre>
     * </code>
     */
    public Date convert(String source) {
        // 日期格式化数组
        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd"));
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S"));
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH"));
        dateFormats.add(new SimpleDateFormat("yyyy-MM"));

        if (StringUtils.isBlank(source)) {
            return null;
        }
        source = source.trim();
        Date date = null;
        boolean flag = false;
        for (DateFormat dateFormat : dateFormats) {
            try {
                date = dateFormat.parse(source);
                flag = true;
                break;
            } catch (ParseException e) {
                // e.printStackTrace();
            }
        }

        if (!flag) {
            try {
                int timeLength = source.length();
                Long time = Long.parseLong(source);
                if (timeLength == 10) {
                    time = time * 1000;
                }
                date = new Date(time);
                flag = true;
            } catch (Exception e) {

            }
        }

        if (flag) {
            return date;
        } else {
            throw new IllegalArgumentException("不能解析日期:" + source);
        }

    }
}
