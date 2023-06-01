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

package io.geekidea.stock.util;

import io.geekidea.framework.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/10
 **/
@Slf4j
public class TaskExecuteTimeUtil {

    private static final Set<String> HOLIDAY_LIST = new LinkedHashSet<>();

    static {
        // 一、元旦：2022年1月1日至3日放假，共3天。
        List<String> holidayList = null;
        holidayList = Arrays.asList("2022-01-01", "2022-01-02", "2022-01-03");
        HOLIDAY_LIST.addAll(holidayList);
        // 二、春节：1月31日至2月6日放假调休，共7天。1月29日（星期六）、1月30日（星期日）上班。
        holidayList = Arrays.asList("2022-01-31", "2022-02-01", "2022-02-02", "2022-02-03", "2022-02-04", "2022-02-05", "2022-02-06", "2022-02-07");
        HOLIDAY_LIST.addAll(holidayList);
        // 三、清明节：4月3日至5日放假调休，共3天。4月2日（星期六）上班。
        holidayList = Arrays.asList("2022-04-03", "2022-04-04", "2022-04-05");
        HOLIDAY_LIST.addAll(holidayList);
        // 四、劳动节：4月30日至5月4日放假调休，共5天。4月24日（星期日）、5月7日（星期六）上班。
        holidayList = Arrays.asList("2022-04-30", "2022-05-01", "2022-05-02", "2022-05-03", "2022-05-04");
        HOLIDAY_LIST.addAll(holidayList);
        // 五、端午节：6月3日至5日放假，共3天。
        holidayList = Arrays.asList("2022-06-03", "2022-06-04", "2022-06-05");
        HOLIDAY_LIST.addAll(holidayList);
        // 六、中秋节：9月10日至12日放假，共3天。
        holidayList = Arrays.asList("2022-09-10", "2022-09-11", "2022-09-12");
        HOLIDAY_LIST.addAll(holidayList);
        // 七、国庆节：10月1日至7日放假调休，共7天。10月8日（星期六）、10月9日（星期日）上班。
        holidayList = Arrays.asList("2022-10-01", "2022-10-02", "2022-10-03", "2022-10-04", "2022-10-05", "2022-10-06", "2022-10-07");
        HOLIDAY_LIST.addAll(holidayList);
    }

    public static boolean isNotExecute() {
        return !isExecute();
    }

    public static boolean isExecute() {
        String todayDate = DateUtil.getTodayDate();
        // 排除节假日
        if (HOLIDAY_LIST.contains(todayDate)) {
            return false;
        }
        LocalDate localDate = LocalDate.now();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int weekDay = dayOfWeek.getValue();
        // 星期六和星期天不执行
        if (weekDay >= 6) {
            return false;
        }
        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();

        String currentTimeString = hour + "";
        if (minute < 10) {
            currentTimeString += ("0" + minute);
        } else {
            currentTimeString += minute;
        }
        int currentTime = Integer.parseInt(currentTimeString);
        if ((currentTime >= 926 && currentTime <= 1140) || (currentTime >= 1300 && currentTime <= 1510)) {
            log.info("currentTime = " + currentTime);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        isExecute();
    }


    /**
     * 收盘后执行
     * 11点35 15:05 执行
     *
     * @return
     */
    public static boolean marketCloseExecute() {
        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        String currentTimeString = hour + "" + minute;
        int currentTime = Integer.parseInt(currentTimeString);
        if ((currentTime >= 1130 && currentTime <= 1300) || (currentTime >= 1500)) {
            log.info("currentTime = " + currentTime);
            return true;
        }
        return false;
    }

}
