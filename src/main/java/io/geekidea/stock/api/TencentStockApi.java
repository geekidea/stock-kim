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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.geekidea.framework.common.exception.BusinessException;
import io.geekidea.framework.util.BigDecimalUtil;
import io.geekidea.stock.entity.StockKLine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-06
 **/
@Slf4j
public class TencentStockApi {

    // 连接超时时间：30s
    private static int CONNECTION_TIMEOUT = 30 * 1000;
    // 超时时间：30s
    private static int TIMEOUT = 30 * 1000;

    // 6开头的是上证
    // 0和3开头的是深证
    private static final String SIX = "6";
    private static final String SH = "sh";
    private static final String SZ = "sz";

    private static final int DEFAULT_DAY = 415;

    private static final String BASIC_URL = "https://proxy.finance.qq.com/ifzqgtimg/appstock/app/newfqkline/get?_var=kline_dayqfq&param=%s,day,,,%d,qfq&r=0.05853936387580272";

    public static List<StockKLine> getStockKLine(String stockCode, String stockName) {
        return getStockKLine(stockCode, stockName, null);
    }

    public static List<StockKLine> getStockKLine(String stockCode, String stockName, Integer day) {
        if (StringUtils.isBlank(stockCode)) {
            return null;
        }
        String fullStockCode = null;
        if (stockCode.startsWith(SIX)) {
            fullStockCode = SH + stockCode;
        } else {
            fullStockCode = SZ + stockCode;
        }
        if (day == null) {
            day = DEFAULT_DAY;
        }
        String url = String.format(BASIC_URL, fullStockCode, day);
        log.info("url:" + url);
        String result = HttpRequest.get(url)
                .setConnectionTimeout(CONNECTION_TIMEOUT)
                .timeout(TIMEOUT)
                .execute().body();
        log.info("Tencent Api Result：\n" + result);
        if (StringUtils.isBlank(result)) {
            throw new BusinessException("获取K线历史数据接口结果返回为空");
        }
        try {
            result = result.substring(13);
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getInteger("code") != 0) {
                throw new BusinessException("获取K线历史数据接口异常：" + jsonObject.getString("msg"));
            }
            JSONObject dataJsonObject = jsonObject.getJSONObject("data").getJSONObject(fullStockCode);
            JSONArray jsonArray = dataJsonObject.getJSONArray("qfqday");
            if (jsonArray == null) {
                jsonArray = dataJsonObject.getJSONArray("day");
            }
            List<StockKLine> stockKLines = new ArrayList<>();
            for (Object object : jsonArray) {
                JSONArray array = (JSONArray) object;
                StockKLine stockKLine = new StockKLine();
                stockKLine.setStockCode(stockCode);
                stockName = stockName.replace(" ", "");
                stockKLine.setStockName(stockName);
                stockKLine.setLineDate(array.getString(0));
                stockKLine.setOpenPrice(array.getBigDecimal(1));
                stockKLine.setClosePrice(array.getBigDecimal(2));
                stockKLine.setHighPrice(array.getBigDecimal(3));
                stockKLine.setLowPrice(array.getBigDecimal(4));
                BigDecimal tradeNumber = array.getBigDecimal(5);
                tradeNumber = BigDecimalUtil.divide(tradeNumber, 10000, 2);
                stockKLine.setTradeNumber(tradeNumber);
                stockKLine.setTurnoverRate(array.getBigDecimal(7));
                BigDecimal tradeAmount = array.getBigDecimal(8);
                tradeAmount = BigDecimalUtil.divide(tradeAmount, 10000, 2);
                stockKLine.setTradeAmount(tradeAmount);
                stockKLines.add(stockKLine);
            }
            return stockKLines;
        } catch (Exception e) {
            log.error("获取K线历史数据接口JSON解析异常", e);
            throw new BusinessException("获取K线历史数据接口JSON解析异常," + e.getMessage());
        }
    }

    public static List<StockKLine> getShanghaiStockIndex(Integer day) {
        String fullStockCode = "sh000001";
        if (day == null) {
            day = DEFAULT_DAY;
        }
        String url = String.format(BASIC_URL, fullStockCode, day);
        log.info("url:" + url);
        String result = HttpRequest.get(url)
                .setConnectionTimeout(CONNECTION_TIMEOUT)
                .timeout(TIMEOUT)
                .execute().body();
        log.info("Tencent Api Result：\n" + result);
        if (StringUtils.isBlank(result)) {
            log.error("获取K线历史数据接口结果返回为空");
            return null;
        }
        try {
            result = result.substring(13);
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getInteger("code") != 0) {
                log.error("获取K线历史数据接口异常：" + jsonObject.getString("msg"));
            }
            JSONObject dataJsonObject = jsonObject.getJSONObject("data").getJSONObject(fullStockCode);
            JSONArray jsonArray = dataJsonObject.getJSONArray("qfqday");
            if (jsonArray == null) {
                jsonArray = dataJsonObject.getJSONArray("day");
            }
            List<StockKLine> stockKLines = new ArrayList<>();
            for (Object object : jsonArray) {
                JSONArray array = (JSONArray) object;
                StockKLine stockKLine = new StockKLine();
                stockKLine.setStockCode(fullStockCode);
                stockKLine.setStockName("上证指数");
                stockKLine.setLineDate(array.getString(0));
                stockKLine.setOpenPrice(array.getBigDecimal(1));
                stockKLine.setClosePrice(array.getBigDecimal(2));
                stockKLine.setHighPrice(array.getBigDecimal(3));
                stockKLine.setLowPrice(array.getBigDecimal(4));
                BigDecimal tradeNumber = array.getBigDecimal(5);
                tradeNumber = BigDecimalUtil.divide(tradeNumber, 10000, 2);
                stockKLine.setTradeNumber(tradeNumber);
                stockKLine.setTurnoverRate(array.getBigDecimal(7));
                BigDecimal tradeAmount = array.getBigDecimal(8);
                tradeAmount = BigDecimalUtil.divide(tradeAmount, 10000, 2);
                stockKLine.setTradeAmount(tradeAmount);
                stockKLines.add(stockKLine);
            }
            return stockKLines;
        } catch (Exception e) {
            log.error("获取K线历史数据接口JSON解析异常", e);
            return null;
        }
    }

    public static void main(String[] args) {
        getStockKLine("600519", "贵州茅台", 2);
    }
}
