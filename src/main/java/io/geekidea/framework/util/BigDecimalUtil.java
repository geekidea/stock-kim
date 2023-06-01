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

import java.math.BigDecimal;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-27
 **/
public class BigDecimalUtil {

    public static boolean equalsZero(BigDecimal value) {
        return BigDecimal.ZERO.compareTo(value) == 0;
    }


    /**
     * 相加
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal add(BigDecimal x, BigDecimal y) {
        if (x == null) {
            x = BigDecimal.ZERO;
        }
        if (y == null) {
            y = BigDecimal.ZERO;
        }

        return x.add(y);
    }

    /**
     * 相加
     *
     * @param x
     * @param y
     * @return
     */
    public static Integer add(Integer x, Integer y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("参数不能为空，x：" + x + "，y：" + y);
        }

        return add(new BigDecimal(x), new BigDecimal(y)).intValue();
    }


    /**
     * 相加
     *
     * @param x
     * @param y
     * @return
     */
    public static Integer addIgnoreNull(Integer x, Integer y) {
        if (x == null) {
            x = 0;
        }
        if (y == null) {
            y = 0;
        }
        return add(new BigDecimal(x), new BigDecimal(y)).intValue();
    }

    /**
     * 相乘
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal mult(BigDecimal x, Integer y) {
        if (x == null || y == null) {
            return BigDecimal.ZERO;
        }
        return x.multiply(new BigDecimal(y));
    }

    /**
     * 相减
     *
     * @param x
     * @param y
     * @return
     */
    public static BigDecimal sub(BigDecimal x, BigDecimal y) {
        if (x == null || y == null) {
            return null;
        }
        return x.subtract(y);
    }

    /**
     * 相减
     *
     * @param x
     * @param y
     * @return
     */
    public static Integer sub(Integer x, Integer y) {
        if (x == null || y == null) {
            return null;
        }
        return sub(new BigDecimal(x), new BigDecimal(y)).intValue();
    }

    /**
     * 判断是否相等
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean equals(BigDecimal x, BigDecimal y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("参数不能为空，x：" + x + "，y：" + y);
        }
        return x.compareTo(y) == 0;
    }


    /**
     * 大于等于
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean ge(BigDecimal x, Double y) {
        return ge(x, new BigDecimal(y));
    }

    /**
     * 大于等于
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean ge(BigDecimal x, BigDecimal y) {
        return x.compareTo(y) >= 0;
    }

    /**
     * 大于
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean gt(BigDecimal x, Integer y) {
        return gt(x, new BigDecimal(y));
    }

    public static boolean gt(BigDecimal x, Double y) {
        return gt(x, new BigDecimal(y));
    }

    /**
     * 大于
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean gt(BigDecimal x, BigDecimal y) {
        return x.compareTo(y) > 0;
    }

    /**
     * 小于
     * x < y
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean lt(BigDecimal x, BigDecimal y) {
        if (x == null || y == null) {
            return false;
        }
        return x.compareTo(y) < 0;
    }

    public static boolean lt(BigDecimal x, double y) {
        return lt(x, new BigDecimal(y));
    }

    /**
     * 小于
     * x < y
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean lt(BigDecimal x, Integer y) {
        if (y == null) {
            return false;
        }
        return lt(x, new BigDecimal(y));
    }

    /**
     * 小于
     * x < y
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean lt(Integer x, Integer y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("参数不能为空，x：" + x + "，y：" + y);
        }
        return lt(new BigDecimal(x), new BigDecimal(y));
    }

    /**
     * 除法
     *
     * @param x 除数
     * @param y 被出数
     * @return
     */
    public static BigDecimal divide(Integer x, Integer y) {
        return divide(x, y, 2);
    }

    /**
     * 除法
     *
     * @param x     除数
     * @param y     被出数
     * @param scale 精度
     * @return
     */
    public static BigDecimal divide(Integer x, Integer y, Integer scale) {
        if (x == null || y == null) {
            return null;
        }
        if (y == 0) {
            return null;
        }
        if (scale == null || scale <= 0) {
            scale = 2;
        }
        BigDecimal num1 = new BigDecimal(x);
        BigDecimal num2 = new BigDecimal(y);
        return num1.divide(num2, scale, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 除法
     *
     * @param x     除数
     * @param y     被出数
     * @param scale 精度
     * @return
     */
    public static BigDecimal divide(Double x, Double y, Integer scale) {
        if (x == null || y == null) {
            return null;
        }
        if (y == 0) {
            return null;
        }
        if (scale == null || scale <= 0) {
            scale = 2;
        }
        BigDecimal num1 = new BigDecimal(x);
        BigDecimal num2 = new BigDecimal(y);
        return num1.divide(num2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除法
     *
     * @param x     除数
     * @param y     被出数
     * @param scale 精度
     * @return
     */
    public static BigDecimal divide(BigDecimal x, BigDecimal y, Integer scale) {
        if (x == null || y == null) {
            return null;
        }
        if (equalsZero(y)) {
            return null;
        }
        if (scale == null || scale <= 0) {
            scale = 2;
        }
        return x.divide(y, scale, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal divide(BigDecimal x, Integer y, Integer scale) {
        BigDecimal bigDecimal = new BigDecimal(y);
        return divide(x, bigDecimal, scale);
    }


    /**
     * 百分比
     *
     * @param x     除数
     * @param y     被出数
     * @param scale 精度
     * @return
     */
    public static BigDecimal percentage(Integer x, Integer y) {
        BigDecimal percentage = divide(x, y, 4);
        percentage = mult(percentage, 100);
        return percentage.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 百分比
     *
     * @param x     除数
     * @param y     被出数
     * @param scale 精度
     * @return
     */
    public static BigDecimal percentage(Double x, Double y) {
        BigDecimal percentage = divide(x, y, 4);
        percentage = mult(percentage, 100);
        return percentage.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 百分比
     *
     * @param x     除数
     * @param y     被出数
     * @param scale 精度
     * @return
     */
    public static BigDecimal percentage(BigDecimal x, BigDecimal y) {
        BigDecimal percentage = divide(x, y, 4);
        percentage = mult(percentage, 100);
        return percentage.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static boolean range(BigDecimal x, double s, double e) {
        if (x == null) {
            return false;
        }
        BigDecimal start = new BigDecimal(s);
        BigDecimal end = new BigDecimal(e);
        if (ge(x, start) && lt(x, end)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(range(new BigDecimal(5.1), 6, 9.9));
    }

    public static boolean le(BigDecimal x, int y) {
        if (x == null) {
            return false;
        }
        return x.compareTo(new BigDecimal(y)) <= 0;
    }

    public static boolean le(BigDecimal x, double y) {
        if (x == null) {
            return false;
        }
        return x.compareTo(new BigDecimal(y)) <= 0;
    }

    public static BigDecimal divide(BigDecimal molecule, BigDecimal denominator) {
        if (denominator == null || BigDecimal.ZERO.equals(denominator)) {
            return null;
        }
        return molecule.divide(denominator, 2);
    }
}
