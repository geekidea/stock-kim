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
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-9-11
 */
public class DateUtil {

    public static final String formatStr_yyyyMMddHHmmssS_ = "yyyyMMddHHmmss";
    public static final String formatStr_yyyyMMddHHmmssS = "yyyy-MM-dd HH:mm:ss.S";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String formatStr_yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
    public static final String formatStr_yyyyMMddHH = "yyyy-MM-dd HH";
    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String formatStr_yyyy = "yyyy";
    public static final String formatStr_yyyy_MM_dd = "yyyyMMdd";
    public static final String formatStr_yyyyMMddDelimiter = "-";

    public static String getYYYYMMDDHHMMSS(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String formatYYYYMMDD(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYYMMDD);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String getTodayDate() {
        return new SimpleDateFormat(YYYYMMDD).format(new Date());
    }

    public static String getTodayDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static Integer diffDay(String maxDate, String minDate) {
        if (StringUtils.isBlank(maxDate) || StringUtils.isBlank(minDate)) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(YYYYMMDD);
        try {
            Date max = dateFormat.parse(maxDate);
            Date min = dateFormat.parse(minDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(max);
            long maxTime = cal.getTimeInMillis();
            cal.setTime(min);
            long minTime = cal.getTimeInMillis();
            long diffDay = (minTime - maxTime) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(diffDay));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseYYYYMMDD(String string) throws ParseException {
        if (StringUtils.isBlank(string)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYYMMDD);
        Date date = simpleDateFormat.parse(string);
        return date;
    }

    public static String now() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(yyyyMMddHHmmss);
        String dateString = simpleDateFormat.format(new Date());
        return dateString;

    }
}
