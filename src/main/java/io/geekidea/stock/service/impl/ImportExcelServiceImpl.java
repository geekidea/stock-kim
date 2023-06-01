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

package io.geekidea.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import io.geekidea.framework.util.BigDecimalUtil;
import io.geekidea.framework.util.PinYinUtil;
import io.geekidea.stock.dto.excel.*;
import io.geekidea.stock.entity.Stock;
import io.geekidea.stock.entity.StockConceptContent;
import io.geekidea.stock.entity.StockKLine;
import io.geekidea.stock.service.*;
import io.geekidea.stock.dto.excel.*;
import io.geekidea.stock.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-11
 **/
@Slf4j
@Service
public class ImportExcelServiceImpl implements ImportExcelService {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRecordService stockRecordService;

    @Autowired
    private StockKLineService stockKLineService;

    @Autowired
    private StockConceptContentService stockConceptContentService;

    @Override
    public void initImportStock(MultipartFile multipartFile, Integer marketType) throws Exception {
        InputStream inputStream = multipartFile.getInputStream();
        Integer type = 0;
        List<Stock> stockList = null;
        if (marketType == 1) {
            // 上海
            List<ShanghaiExcelStock> shanghaiExcelStockList = EasyExcel.read(inputStream).head(ShanghaiExcelStock.class).sheet().doReadSync();
            stockList = convertShanghaiExcelStockListToStockList(shanghaiExcelStockList, type);
        } else if (marketType == 2) {
            // 深圳
            List<ShenzhenExcelStock> shenzhenExcelStocklist = EasyExcel.read(inputStream).head(ShenzhenExcelStock.class).sheet().doReadSync();
            stockList = convertShenzhenExcelStockListToStockList(shenzhenExcelStocklist, type);
        }
        if (CollectionUtils.isEmpty(stockList)) {
            return;
        }
        stockService.importStock(stockList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importStockExcel(MultipartFile multipartFile) throws Exception {
        InputStream inputStream = multipartFile.getInputStream();
        EasyExcel.read(inputStream, ExcelStock.class, new AnalysisEventListener<ExcelStock>() {
            private List<ExcelStock> excelStocks = new ArrayList<>();

            @Override
            public void onException(Exception exception, AnalysisContext context) throws Exception {
                log.error("解析失败，但是继续解析下一行:" + exception.getMessage());
                // 如果是某一个单元格的转换异常 能获取到具体行号
                // 如果要获取头的信息 配合invokeHeadMap使用
                if (exception instanceof ExcelDataConvertException) {
                    ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
                    log.error("第{}行，第{}列解析异常，数据：{}", excelDataConvertException.getRowIndex()
                            , excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData()
                    );
                }
            }

            @Override
            public void invoke(ExcelStock excelStock, AnalysisContext analysisContext) {
                log.info("excelStock = " + excelStock);
                excelStocks.add(excelStock);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                log.info("集合大小：" + excelStocks.size());
                List<Stock> stockList = convertExcelStockListToStockList(excelStocks);
                if (CollectionUtils.isEmpty(stockList)) {
                    return;
                }
                try {
                    stockService.importStock(stockList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).sheet().doRead();
    }


    @Override
    public void importConceptData(MultipartFile multipartFile) throws Exception {
        List<ConceptExcelStock> conceptExcelStocks = EasyExcel.read(multipartFile.getInputStream()).head(ConceptExcelStock.class).sheet().doReadSync();
        String conceptBatchNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        List<StockConceptContent> stockConceptContents = convertConceptExcelStocksToStockConceptContents(conceptExcelStocks, conceptBatchNo);
        stockConceptContentService.saveStockConceptContent(stockConceptContents);
    }

    @Override
    public void importThsStockExcel(MultipartFile multipartFile) throws Exception {
        InputStream inputStream = multipartFile.getInputStream();
        EasyExcel.read(inputStream, ThsExcelStock.class, new AnalysisEventListener<ThsExcelStock>() {
            private List<ThsExcelStock> thsExcelStocks = new ArrayList<>();

            @Override
            public void onException(Exception exception, AnalysisContext context) throws Exception {
                log.error("解析失败，但是继续解析下一行:" + exception.getMessage());
                // 如果是某一个单元格的转换异常 能获取到具体行号
                // 如果要获取头的信息 配合invokeHeadMap使用
                if (exception instanceof ExcelDataConvertException) {
                    ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
                    log.error("第{}行，第{}列解析异常，数据：{}", excelDataConvertException.getRowIndex()
                            , excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData()
                    );
                }
            }

            @Override
            public void invoke(ThsExcelStock thsExcelStock, AnalysisContext analysisContext) {
                log.info("thsExcelStock = " + thsExcelStock);
                thsExcelStocks.add(thsExcelStock);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                log.info("集合大小：" + thsExcelStocks.size());
                try {
                    List<Stock> stockList = convertThsExcelStockListToStockList(thsExcelStocks);
                    if (CollectionUtils.isEmpty(stockList)) {
                        return;
                    }
                    stockService.importThsStock(stockList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).sheet().doRead();
    }

    private List<Stock> convertThsExcelStockListToStockList(List<ThsExcelStock> thsExcelStocks) throws Exception {
        if (CollectionUtils.isEmpty(thsExcelStocks)) {
            return null;
        }
        List<Stock> stockList = new ArrayList<>();
        for (ThsExcelStock thsExcelStock : thsExcelStocks) {
            Stock stock = convertThsExcelStockToStock(thsExcelStock);
            if (stock != null) {
                stockList.add(stock);
            }
        }
        return stockList;
    }

    private Stock convertThsExcelStockToStock(ThsExcelStock thsExcelStock) throws Exception {
        if (thsExcelStock == null) {
            return null;
        }
        Stock stock = new Stock();
        String stockCode = thsExcelStock.getStockCode();
        if (StringUtils.isBlank(stockCode)) {
            return null;
        }
        if (stockCode.endsWith(".SZ")) {
            stockCode = stockCode.replace(".SZ", "");
            stock.setStockFullCode("sz" + stockCode);
        } else if (stockCode.endsWith(".SH")) {
            stockCode = stockCode.replace(".SH", "");
            stock.setStockFullCode("sh" + stockCode);
        }
        // 处理股票代码
        if (stockCode.startsWith("6")) {
            stock.setMarketType(1);
            stock.setMarketTypeName("上证");
        } else if (stockCode.startsWith("0")) {
            stock.setStockFullCode("sz" + stockCode);
            stock.setMarketType(2);
            stock.setMarketTypeName("深证");
        } else if (stockCode.startsWith("3")) {
            stock.setStockFullCode("sz" + stockCode);
            stock.setMarketType(3);
            stock.setMarketTypeName("创业板");
        }
        stock.setStockCode(stockCode);
        String stockName = thsExcelStock.getStockName();
        if (StringUtils.isBlank(stockName)) {
            return null;
        }
        String pinyin = PinYinUtil.getFirstLetterPinYin(stockName);
        stock.setStockName(stockName);
        stock.setStockLetter(pinyin);
        stock.setWebsite(thsExcelStock.getWebsite());
        stock.setTotalMarketValue(yuanToHundredMillion(thsExcelStock.getTotalMarketValue()));
        stock.setCirculationMarketValue(yuanToHundredMillion(thsExcelStock.getCirculationMarketValue()));
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String listingDate = thsExcelStock.getListingDate();
        if (StringUtils.isNotBlank(listingDate)) {
            try {
                stock.setListingDate(dateFormat.parse(listingDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // 设置自选为否
        stock.setOptionalYn(false);
        return stock;
    }

    public BigDecimal yuanToHundredMillion(String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        }
        BigDecimal value = BigDecimalUtil.divide(new BigDecimal(string), new BigDecimal(10000 * 10000));
        return value;
    }

    private List<StockConceptContent> convertConceptExcelStocksToStockConceptContents(List<ConceptExcelStock> conceptExcelStocks, String conceptBatchNo) {
        if (CollectionUtils.isEmpty(conceptExcelStocks)) {
            return null;
        }
        if (StringUtils.isBlank(conceptBatchNo)) {
            return null;
        }
        List<StockConceptContent> list = new ArrayList<>();
        for (ConceptExcelStock conceptExcelStock : conceptExcelStocks) {
            StockConceptContent stockConceptContent = new StockConceptContent();
            String stockCode = conceptExcelStock.getStockCode();
            int index = stockCode.indexOf(".");
            stockCode = stockCode.substring(0, index);
            try {
                Integer.parseInt(stockCode);
            } catch (NumberFormatException e) {
//                e.printStackTrace();
                continue;
            }
            stockConceptContent.setStockCode(stockCode);
            stockConceptContent.setStockName(conceptExcelStock.getStockName());
            stockConceptContent.setConceptContent(conceptExcelStock.getConceptContent());
            try {
                String conceptCountString = conceptExcelStock.getConceptCount();
                Integer conceptCount = Integer.parseInt(conceptCountString);
                stockConceptContent.setConceptCount(conceptCount);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            stockConceptContent.setConceptBatchNo(conceptBatchNo);
            list.add(stockConceptContent);
        }
        return list;
    }


    private List<Stock> convertExcelStockListToStockList(List<ExcelStock> excelStocklist) {
        if (CollectionUtils.isNotEmpty(excelStocklist)) {
            List<Stock> stockList = new ArrayList<>();
            for (ExcelStock excelStock : excelStocklist) {
                Stock stock = convertExcelStockToStock(excelStock);
                stockList.add(stock);
            }
            return stockList;
        }
        return null;
    }

    private Stock convertExcelStockToStock(ExcelStock excelStock) {
        if (excelStock != null) {
            Stock stock = new Stock();
            String stockCode = excelStock.getStockCode();
            stock.setStockCode(stockCode);
            stock.setStockName(excelStock.getStockName());
            stock.setPrice(excelStock.getLatestPrice());
            stock.setIncrease(excelStock.getIncrease());
            stock.setYesterdayClosingPrice(excelStock.getYesterdayClosingPrice());
            stock.setTotalMarketValue(removeUnit(excelStock.getTotalMarketValue()));
            stock.setCirculationMarketValue(removeUnit(excelStock.getCirculationMarketValue()));
            stock.setTradeAmount(removeUnit(excelStock.getTradeAmount()));
            stock.setIndustry(excelStock.getIndustry());
            stock.setTurnoverRate(excelStock.getTurnoverRate());
            stock.setVolumeRatio(excelStock.getVolumeRatio());
            stock.setAmplitude(excelStock.getAmplitude());
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String listingDate = excelStock.getListingDate();
            if (StringUtils.isNotBlank(listingDate)) {
                try {
                    stock.setListingDate(dateFormat.parse(listingDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String optionalDate = excelStock.getOptionalDate();
            if (StringUtils.isNotBlank(optionalDate)) {
                try {
                    stock.setOptionalDate(dateFormat.parse(optionalDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            stock.setType(0);
            if (stockCode.startsWith("6")) {
                stock.setStockFullCode("sh" + stockCode);
                stock.setMarketType(1);
                stock.setMarketTypeName("上证");
            } else if (stockCode.startsWith("0")) {
                stock.setStockFullCode("sz" + stockCode);
                stock.setMarketType(2);
                stock.setMarketTypeName("深证");
            } else if (stockCode.startsWith("3")) {
                stock.setStockFullCode("sz" + stockCode);
                stock.setMarketType(3);
                stock.setMarketTypeName("创业板");
            }
            return stock;
        }
        return null;
    }

    private List<Stock> convertShenzhenExcelStockListToStockList(List<ShenzhenExcelStock> shenzhenExcelStocklist, Integer type) {
        if (CollectionUtils.isEmpty(shenzhenExcelStocklist)) {
            return null;
        }
        List<Stock> list = new ArrayList<>();
        for (ShenzhenExcelStock shenzhenExcelStock : shenzhenExcelStocklist) {
            Stock stock = new Stock();
            stock.setStockCode(shenzhenExcelStock.getStockCode());
            stock.setStockName(shenzhenExcelStock.getStockName());
            Date listingDate = handleListingDate(shenzhenExcelStock.getListingDate());
            stock.setListingDate(listingDate);
            list.add(stock);
        }
        return list;
    }

    private List<Stock> convertShanghaiExcelStockListToStockList(List<ShanghaiExcelStock> shanghaiExcelStockList, Integer type) {
        if (CollectionUtils.isEmpty(shanghaiExcelStockList)) {
            return null;
        }
        List<Stock> list = new ArrayList<>();
        for (ShanghaiExcelStock shanghaiExcelStock : shanghaiExcelStockList) {
            Stock stock = new Stock();
            stock.setStockCode(shanghaiExcelStock.getStockCode());
            stock.setStockName(shanghaiExcelStock.getStockName());
            Date listingDate = handleListingDate(shanghaiExcelStock.getListingDate());
            stock.setListingDate(listingDate);
            list.add(stock);
        }
        return list;
    }

    private Date handleListingDate(String listingDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(listingDate)) {
            try {
                return dateFormat.parse(listingDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private BigDecimal removeUnit(String value) {
        try {
            if (StringUtils.isNotBlank(value)) {
                if (value.endsWith("万亿")) {
                    value = value.substring(0, value.length() - 2);
                } else {
                    value = value.substring(0, value.length() - 1);
                }
                return new BigDecimal(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
