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
import java.util.ArrayList;
import java.util.List;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 11/4/21
 **/
public class SkdjUtil {

    public static BigDecimal getSkdj() {
        Integer n = 9;
        Integer m = 3;
        BigDecimal closePrice = new BigDecimal(25.88);
//        BigDecimal lowPrice = new BigDecimal(21.51);
        BigDecimal lowPrice = new BigDecimal(22.14);
//        BigDecimal highPrice = new BigDecimal(26.78);
        BigDecimal highPrice = new BigDecimal(26);
//        (closePrice-lowPrice)/(highPrice-lowPrice)*100
        BigDecimal molecule = BigDecimalUtil.sub(closePrice, lowPrice);
        BigDecimal denominator = BigDecimalUtil.sub(highPrice, lowPrice);
        BigDecimal rsv = BigDecimalUtil.percentage(molecule, denominator);
        System.out.println("rsv = " + rsv);
        List<Double> list  =new ArrayList<>();
        list.add(25.95);
        list.add(26.00);
        list.add(25.38);
        list.add(23.11);
        list.add(22.14);
        list.add(23.59);
        list.add(24.05);
        list.add(24.10);
        list.add(25.88);
        Double d = EMAUtil.getEMA(list,3);
        System.out.println("d = " + d);
//        BigDecimal k = BigDecimalUtil.divide(rsv, 3, 2);
//        System.out.println("k = " + k);

        Double k = 33.33 +0.333*rsv.doubleValue();
        System.out.println("k = " + k);

        return null;
    }

    public static void main(String[] args) {
//        getSkdj();
    }
}
