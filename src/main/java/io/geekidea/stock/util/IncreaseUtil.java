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

import io.geekidea.framework.util.BigDecimalUtil;

import java.math.BigDecimal;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-10
 **/
public class IncreaseUtil {

    /**
     * <pre>
     * 计算涨幅百分比
     * 核心算法
     *  (end -start) / start * 100
     *  (11 - 10) / 10 * 100 = 10%
     *  (10 - 9) / 10 * 100 = -10%
     * </pre>
     *
     * @param start
     * @param end
     * @return
     */
    public static BigDecimal getIncrease(BigDecimal start, BigDecimal end) {
        if (start == null || end == null) {
            return null;
        }
        BigDecimal diff = BigDecimalUtil.sub(end, start);
        BigDecimal increase = BigDecimalUtil.percentage(diff, start);
        return increase;
    }

    /**
     * 获取涨幅类型
     *
     * @param increase
     * @return
     */
    public static Integer getIncreaseType(BigDecimal increase) {
        if (increase == null) {
            return null;
        }
        Integer type = null;
        if (BigDecimalUtil.ge(increase, 9.9)) {
            type = 4;
        } else if (BigDecimalUtil.ge(increase, 5.0) && BigDecimalUtil.lt(increase, 9.9)) {
            type = 3;
        } else if (BigDecimalUtil.ge(increase, 1.0) && BigDecimalUtil.lt(increase, 5.0)) {
            type = 2;
        } else if (BigDecimalUtil.gt(increase, 0) && BigDecimalUtil.lt(increase, 1)) {
            type = 1;
        } else if (BigDecimalUtil.equalsZero(increase)) {
            type = 0;
        } else if (BigDecimalUtil.gt(increase, -1.0) && BigDecimalUtil.lt(increase, 0)) {
            type = -1;
        } else if (BigDecimalUtil.gt(increase, -5.0) && BigDecimalUtil.le(increase, -1)) {
            type = -2;
        } else if (BigDecimalUtil.gt(increase, -9.9) && BigDecimalUtil.le(increase, -5.0)) {
            type = -3;
        } else if (BigDecimalUtil.le(increase, -9.9)) {
            type = -4;
        }
        return type;
    }

    /**
     * 获取涨幅状态
     *
     * @param increase
     * @return
     */
    public static Integer getIncreaseState(BigDecimal increase) {
        if (increase == null) {
            return null;
        }
        if (BigDecimalUtil.gt(increase, 0)) {
            return 1;
        } else if (BigDecimalUtil.equalsZero(increase)) {
            return 0;
        } else {
            return -1;
        }
    }

    public static BigDecimal getAmplitude(BigDecimal close, BigDecimal max, BigDecimal min) {
        if (close == null || max == null || min == null) {
            return null;
        }
        // 振幅：(最高-最低)/上一个收盘价 (11-9)/10*100
        BigDecimal diff = BigDecimalUtil.sub(max, min);
        BigDecimal amplitude = BigDecimalUtil.percentage(diff, close);
        return amplitude;
    }

    public static void main(String[] args) {
        // 昨日收盘价 10 今日收盘价 11(10%) 10.6(6%) 10.35(3.5%)  9(跌停) 9.4(-6%) 9.65(-3.5%)
//        System.out.println(getIncrease(new BigDecimal(10), new BigDecimal(11)));
//        System.out.println(getIncrease(new BigDecimal(10), new BigDecimal(10.6)));
//        System.out.println(getIncrease(new BigDecimal(10), new BigDecimal(10.35)));
//        System.out.println(getIncrease(new BigDecimal(10), new BigDecimal(9)));
//        System.out.println(getIncrease(new BigDecimal(10), new BigDecimal(9.4)));
//        System.out.println(getIncrease(new BigDecimal(10), new BigDecimal(9.65)));
//        System.out.println(getIncrease(new BigDecimal(1830), new BigDecimal(1839.60)));
//        System.out.println(getIncrease(new BigDecimal(302.02), new BigDecimal(306)));

//        double[] nums = new double[]{20, 10, 9.9, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0.5, 0, -20, -10, -9.9, -9, -8, -7, -6, -5, -4, -3, -2, -1, -0.5};
//        for (double num : nums) {
//            System.out.println(num + " : " + getIncreaseType(new BigDecimal(num)));
//        }
//        System.out.println(getIncreaseState(new BigDecimal(10)));
//        System.out.println(getIncreaseState(new BigDecimal(0)));
//        System.out.println(getIncreaseState(new BigDecimal(-10)));

        System.out.println(getAmplitude(new BigDecimal(10), new BigDecimal(11), new BigDecimal(9)));

    }
}
