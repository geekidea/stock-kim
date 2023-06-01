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

package io.geekidea.stock.api;

import cn.hutool.http.HttpRequest;
import io.geekidea.framework.util.BigDecimalUtil;
import io.geekidea.stock.entity.StockRealData;
import io.geekidea.stock.enums.SuspensionYnEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-13
 **/
@Slf4j
public class SinaStockApi {

    // 连接超时时间：30s
    private static int CONNECTION_TIMEOUT = 30 * 1000;
    // 超时时间：30s
    private static int TIMEOUT = 30 * 1000;

    // 6开头的是上证
    // 0和3开头的是深证
    private static final String SIX = "6";
    private static final String SH = "sh";
    private static final String SZ = "sz";
    private static final String BASIC_URL = "https://hq.sinajs.cn/list=";


    public static StockRealData getRealData(String stockCode) throws Exception {
        return getRealData(null, stockCode);
    }

    /**
     * https://hq.sinajs.cn/list=sz300418
     * var hq_str_sys_auth="FAILED";
     */
    public static StockRealData getRealData(String batchNo, String stockCode) throws Exception {
        if (StringUtils.isBlank(stockCode)) {
            return null;
        }
        List<String> stockCodes = new ArrayList<>();
        stockCodes.add(stockCode);
        List<StockRealData> list = getRealData(batchNo, stockCodes);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        StockRealData stockRealData = list.get(0);
        return stockRealData;
    }

    /**
     * https://hq.sinajs.cn/list=sz300418,sz002624
     * <pre>
     *     var hq_str_sz300418="昆仑万维,18.550,18.540,19.930,20.500,18.210,19.930,19.940,69455878,1365979484.530,62875,19.930,61500,19.920,29800,19.910,100200,19.900,23400,19.890,204680,19.940,187500,19.950,94000,19.960,59000,19.970,54523,19.980,2021-09-13,15:41:00,00,D|700|13951.000";
     *     var hq_str_sz002624="完美世界,15.870,15.950,16.920,17.300,15.620,16.920,16.930,63753450,1060737203.700,72109,16.920,97300,16.910,50400,16.900,6100,16.890,8600,16.880,227250,16.930,103600,16.940,107500,16.950,33900,16.960,33700,16.970,2021-09-13,15:00:03,00";
     * </pre>
     *
     * @param stockCodes
     * @param realBatchNo
     * @return
     * @throws Exception
     */
    public static List<StockRealData> getRealData(String batchNo, List<String> stockCodes) throws Exception {
        if (CollectionUtils.isEmpty(stockCodes)) {
            return null;
        }
        String fullStockCode = "";
        for (int i = 0; i < stockCodes.size(); i++) {
            String stockCode = stockCodes.get(i);
            if (stockCode.startsWith(SIX)) {
                fullStockCode += SH + stockCode;
            } else {
                fullStockCode += SZ + stockCode;
            }

            if (i < stockCodes.size() - 1) {
                fullStockCode += ",";
            }
        }
        String url = BASIC_URL + fullStockCode;
        String result = HttpRequest.get(url)
                .setConnectionTimeout(CONNECTION_TIMEOUT)
                .timeout(TIMEOUT)
                .execute().body();
        if (StringUtils.isBlank(result)) {
            log.error("调用实时数据接口结果返回为空");
            return null;
        }
        // 判断是否有分号，有分号表示有多个数据
        String[] dataArray = null;
        int semicolonIndex = result.indexOf(";");
        if (semicolonIndex == -1) {
            // 单个
            dataArray = new String[]{result};
        } else {
            // 按照分号拆分
            dataArray = result.split(";");
        }
        if (ArrayUtils.isEmpty(dataArray)) {
            return null;
        }
        List<StockRealData> list = new ArrayList<>();
        for (String dataResult : dataArray) {
            // 处理数据
            StockRealData stockRealData = getStockRealDataRecord(batchNo, dataResult);
            if (stockRealData != null) {
                list.add(stockRealData);
            }
        }
        return list;
    }

    private static StockRealData getStockRealDataRecord(String batchNo, String result) {
        if (StringUtils.isBlank(result)) {
            return null;
        }
        result = result.trim();
        int index = result.indexOf("=");
        if (index == -1) {
            return null;
        }
        // 获取股票代码
        // var hq_str_sz300418=
        String stockCode = result.substring((index - 6), index);
        // 获取数据内容部分
        String content = result.substring(index + 2, result.length() - 2);
        if (StringUtils.isBlank(content)) {
            return null;
        }
        String[] arrays = content.split(",");
        if (ArrayUtils.isEmpty(arrays)) {
            return null;
        }
        // 字段映射
        StockRealData stockRealData = getStockRealDataByArrays(arrays);
        if (stockRealData == null) {
            return null;
        }
        // 股票代码
        stockRealData.setStockCode(stockCode);
        // 批次号
        stockRealData.setBatchNo(batchNo);
        return stockRealData;
    }


    /**
     * 计算最新涨幅
     *
     * @param yesterdayClosingPrice 昨日收盘价
     * @param currentPrice          当前价格
     * @return
     */
    private static BigDecimal calcCurrentIncrease(BigDecimal yesterdayClosingPrice, BigDecimal currentPrice) {
        BigDecimal zero = new BigDecimal(0);
        if (yesterdayClosingPrice == null || BigDecimalUtil.equalsZero(yesterdayClosingPrice)) {
            return zero;
        }
        if (currentPrice == null || BigDecimalUtil.equalsZero(currentPrice)) {
            return zero;
        }
        if (yesterdayClosingPrice.equals(currentPrice)) {
            return zero;
        }
        // (19.98-18.54) / 18.54 * 100 = 7.766 => 7.77
        // (currentPrice - yesterdayClosingPrice) / yesterdayClosingPrice * 100
        BigDecimal currentIncrease = zero;
        try {
            currentIncrease = currentPrice.subtract(yesterdayClosingPrice).divide(yesterdayClosingPrice, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentIncrease;
    }

    private static StockRealData getStockRealDataByArrays(String[] arrays) {
        if (ArrayUtils.isEmpty(arrays)) {
            return null;
        }
        StockRealData stockRealData = new StockRealData();
        // 股票名称
        stockRealData.setStockName(arrays[0]);
        // 今日开盘价
        stockRealData.setOpeningPrice(new BigDecimal(arrays[1]));
        // 昨日收盘价
        stockRealData.setYesterdayClosingPrice(new BigDecimal(arrays[2]));
        // 当前价格
        stockRealData.setCurrentPrice(new BigDecimal(arrays[3]));
        // 今日最高价
        stockRealData.setHighPrice(new BigDecimal(arrays[4]));
        // 今日最低价
        stockRealData.setLowPrice(new BigDecimal(arrays[5]));
        // 买一价
        stockRealData.setBuyOnePrice(new BigDecimal(arrays[6]));
        // 卖一价
        stockRealData.setSellOnePrice(new BigDecimal(arrays[7]));
        // 总手
        stockRealData.setTradeNumber(division(arrays[8], 1000000));
        // 金额
        stockRealData.setTradeAmount(division(arrays[9], 100000000));
        // 买一量
        stockRealData.setBuyOneNumber(getDivisionNumber(arrays[10]));
        // 买二量
        stockRealData.setBuyTwoNumber(getDivisionNumber(arrays[12]));
        // 买二价
        stockRealData.setBuyTwoPrice(new BigDecimal(arrays[13]));
        // 买三量
        stockRealData.setBuyThreeNumber(getDivisionNumber(arrays[14]));
        // 买三价
        stockRealData.setBuyThreePrice(new BigDecimal(arrays[15]));
        // 买四量
        stockRealData.setBuyFourNumber(getDivisionNumber(arrays[16]));
        // 买四价
        stockRealData.setBuyFourPrice(new BigDecimal(arrays[17]));
        // 买五量
        stockRealData.setBuyFiveNumber(getDivisionNumber(arrays[18]));
        // 买五价
        stockRealData.setBuyFivePrice(new BigDecimal(arrays[19]));
        // 卖一量
        stockRealData.setSellOneNumber(getDivisionNumber(arrays[20]));
        // 卖二量
        stockRealData.setSellTwoNumber(getDivisionNumber(arrays[22]));
        // 卖二价
        stockRealData.setSellTwoPrice(new BigDecimal(arrays[23]));
        // 卖三量
        stockRealData.setSellThreeNumber(getDivisionNumber(arrays[24]));
        // 卖三价
        stockRealData.setSellThreePrice(new BigDecimal(arrays[25]));
        // 卖四量
        stockRealData.setSellFourNumber(getDivisionNumber(arrays[26]));
        // 卖四价
        stockRealData.setSellFourPrice(new BigDecimal(arrays[27]));
        // 卖五量
        stockRealData.setSellFiveNumber(getDivisionNumber(arrays[28]));
        // 卖五价
        stockRealData.setSellFivePrice(new BigDecimal(arrays[29]));
        // 当前年月日
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            stockRealData.setRealDate(dateFormat.parse(arrays[30]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            stockRealData.setRealTime(timeFormat.parse(arrays[31]));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 判断是否是停牌
        // 今日开盘价为0，当前价为0，昨日收盘价不为0
        if (BigDecimalUtil.equalsZero(stockRealData.getOpeningPrice()) &&
                BigDecimalUtil.equalsZero(stockRealData.getCurrentPrice()) &&
                !BigDecimalUtil.equalsZero(stockRealData.getYesterdayClosingPrice())
        ) {
            // 停牌
            stockRealData.setCurrentPrice(stockRealData.getYesterdayClosingPrice());
            stockRealData.setCurrentIncrease(BigDecimal.ZERO);
            stockRealData.setSuspensionYn(SuspensionYnEnum.YES.getCode());
        } else {
            // 最新涨幅
            BigDecimal currentIncrease = calcCurrentIncrease(stockRealData.getYesterdayClosingPrice(), stockRealData.getCurrentPrice());
            stockRealData.setCurrentIncrease(currentIncrease);
            stockRealData.setSuspensionYn(SuspensionYnEnum.NO.getCode());
        }
        return stockRealData;
    }

    private static BigDecimal division(String value, Integer x) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        if (x <= 0) {
            return null;
        }
        BigDecimal valueBigDecimal = new BigDecimal(value);
        BigDecimal xBigDecimal = new BigDecimal(x);
        BigDecimal result = valueBigDecimal.divide(xBigDecimal, 2, BigDecimal.ROUND_HALF_UP);
        return result;
    }


    private static BigDecimal getDivisionNumber(String value) {
        return division(value, 100);
    }

}
