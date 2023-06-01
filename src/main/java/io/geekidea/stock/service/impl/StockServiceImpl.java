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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.geekidea.framework.common.exception.BusinessException;
import io.geekidea.framework.thread.ThreadExecutor;
import io.geekidea.framework.thread.ThreadExecutorCallback;
import io.geekidea.framework.util.*;
import io.geekidea.stock.callback.SyncKLineCallback;
import io.geekidea.stock.dto.query.*;
import io.geekidea.stock.dto.vo.*;
import io.geekidea.stock.entity.*;
import io.geekidea.stock.mapper.*;
import io.geekidea.stock.service.*;
import io.geekidea.stock.enums.IndustryConceptTypeEnum;
import io.geekidea.stock.enums.QuotationPieTypeEnum;
import io.geekidea.stock.enums.StockSearchTypeEnum;
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.stock.util.IncreaseUtil;
import io.geekidea.stock.util.OptionalYnUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-11
 */
@Slf4j
@Service
public class StockServiceImpl extends BaseServiceImpl<StockMapper, Stock> implements StockService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockRecordService stockRecordService;

    @Autowired
    private StockKLineMapper stockKLineMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StockKLineService stockKLineService;

    @Autowired
    private IndustryIndexService industryIndexService;

    @Autowired
    private ConceptIndexService conceptIndexService;

    @Autowired
    private BkInfoMapper bkInfoMapper;

    @Autowired
    private BkStockMapper bkStockMapper;

    @Autowired
    private BkKLineMapper bkKLineMapper;

    @Autowired
    private BkInfoService bkInfoService;

    @Autowired
    private BkIndustryConceptService bkIndustryConceptService;

    @Autowired
    private BkKLineService bkKLineService;

    @Autowired
    private StockRealDataService stockRealDataService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importStock(List<Stock> stockList) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String random = RandomStringUtils.randomNumeric(3);
        String currentTime = dateFormat.format(new Date());
        // 批次号
        String batchNo = BatchNoUtil.getBatchNo();
        List<String> stockCodes = new ArrayList<>();
        // 设置批次号和自动排序值
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = stockList.get(i);
            stock.setBatchNo(batchNo);
            // 自动排序
            DecimalFormat decimalFormat = new DecimalFormat("0000");
            String index = decimalFormat.format((i + 1));
            String autoSort = currentTime + "0" + index;
            stock.setAutoSort(autoSort);
            stockCodes.add(stock.getStockCode());

            // 拼音
            String stockName = stock.getStockName();
            String pinYin = PinYinUtil.getFirstLetterPinYin(stockName);
            stock.setStockLetter(pinYin);
        }

        // 获取所有的股票数据，对比
        int dbStockCount = count();
        if (dbStockCount == 0) {
            saveBatch(stockList);
        } else {
            LambdaQueryWrapper<Stock> listWrapper = new LambdaQueryWrapper<>();
            listWrapper.in(Stock::getStockCode, stockCodes);
            List<Stock> dbStockList = list(listWrapper);
            // 转换成map
            Map<String, Stock> dbStockMap = dbStockList.stream().collect(Collectors.toMap(Stock::getStockCode, Stock -> Stock));
            // 数据库中的股票代码集合
            List<String> dbStocks = new ArrayList<>();
            dbStockList.forEach(stock -> {
                dbStocks.add(stock.getStockCode());
            });
            // 区分股票添加/修改
            SetUtil.SetDiff<String> setDiff = SetUtil.diff(dbStocks, stockCodes);
            if (setDiff.isHasAdd()) {
                Set<String> addSet = setDiff.getAddSet();
                List<Stock> addStocks = new ArrayList<>();
                for (Stock stock : stockList) {
                    String stockCode = stock.getStockCode();
                    if (addSet.contains(stockCode)) {
                        addStocks.add(stock);
                    }
                }

                saveBatch(addStocks);
            }
            if (setDiff.isHasUpdate()) {
                Set<String> updateSet = setDiff.getUpdateSet();
                List<Stock> updateStocks = new ArrayList<>();
                for (Stock stock : stockList) {
                    String stockCode = stock.getStockCode();
                    if (updateSet.contains(stockCode)) {
                        Stock dbStock = dbStockMap.get(stockCode);
                        updateStockField(dbStock, stock);
                        updateStocks.add(dbStock);
                    }
                }
                updateBatchById(updateStocks);
            }
            // 更新K线/板块数据
            updateAddStockInfo();
            // 刷新实时数据
            stockRealDataService.syncStockRealData(true);
        }
        // 保存每次导入的所有数据记录
        List<StockRecord> stockRecordList = convertStockListToStockRecordList(stockList);
        if (CollectionUtils.isNotEmpty(stockRecordList)) {
            stockRecordService.saveBatch(stockRecordList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveStock(Stock stock) throws Exception {
        return super.save(stock);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStock(Stock stock) throws Exception {
        return super.updateById(stock);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteStock(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public StockQueryVo getStockById(Long id) throws Exception {
        return stockMapper.getStockById(id);
    }

    @Override
    public Paging<StockQueryVo> getStockPageList(StockQuery stockQuery) throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        stockQuery.setOptionalYn(optionalYn);
        String keyword = stockQuery.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            if (keyword.contains(",")) {
                String[] stockCodes = keyword.split(",");
                stockQuery.setStockCodes(stockCodes);
            }
        }
        if (StringUtils.isNotBlank(keyword)) {
            if (keyword.startsWith("BK")) {
                stockQuery.setType(StockSearchTypeEnum.BK.getCode());
            }
        }
        Integer type = stockQuery.getType();
        StockSearchTypeEnum stockSearchTypeEnum = StockSearchTypeEnum.getByCode(type);
        IPage<StockQueryVo> iPage = null;
        if (StockSearchTypeEnum.ALL == stockSearchTypeEnum) {
            Page page = buildPageQuery(stockQuery, OrderItem.desc("increase"));
            iPage = stockMapper.getStockPageList(page, stockQuery);
            // TODO 需要前端标记是否分页 sibarStockList使用新的接口
//            OrderItem orderItem = stockQuery.getOrder();
//            if (orderItem == null){
//                orderItem = OrderItem.desc("increase");
//            }
//            List<StockQueryVo> stocks = stockMapper.getStockList(orderItem);
//            if (CollectionUtils.isNotEmpty(stocks)){
//                iPage = new Page<>();
//                iPage.setRecords(stocks);
//                iPage.setTotal(stocks.size());
//            }
        } else if (StockSearchTypeEnum.OPTIONAL == stockSearchTypeEnum) {
            Page page = buildPageQuery(stockQuery, OrderItem.asc("auto_sort"));
            iPage = stockMapper.getStockPageList(page, stockQuery);
        } else if (StockSearchTypeEnum.INDUSTRY == stockSearchTypeEnum) {
            Page page = buildPageQuery(stockQuery, OrderItem.desc("increase"));
            iPage = stockMapper.getStockPageList(page, stockQuery);
        } else if (StockSearchTypeEnum.CONCEPT == stockSearchTypeEnum) {
            Page page = buildPageQuery(stockQuery, OrderItem.desc("increase"));
            iPage = stockMapper.getStockPageListByConcept(page, stockQuery);
        } else if (StockSearchTypeEnum.DISTRIBUTION == stockSearchTypeEnum) {
            Integer distributionType = stockQuery.getDistributionType();
            OrderItem orderItem = OrderItem.desc("increase");
            if (distributionType < 0) {
                orderItem = OrderItem.asc("increase");
            }
            Page page = buildPageQuery(stockQuery, orderItem);
            iPage = stockMapper.getStockPageList(page, stockQuery);

        } else if (StockSearchTypeEnum.BK == stockSearchTypeEnum) {
            Page page = buildPageQuery(stockQuery, OrderItem.desc("increase"));
            String industry = stockQuery.getIndustry();
            String concept = stockQuery.getConcept();
            if (StringUtils.isNotBlank(industry)) {
                iPage = stockMapper.getStockPageListByBkCode(page, stockQuery);
            } else if (StringUtils.isNotBlank(concept)) {
                iPage = stockMapper.getStockPageListByBkCodeAndConcept(page, stockQuery);
            } else {
                iPage = stockMapper.getStockPageListByBkCode(page, stockQuery);
            }
        }
        return new Paging(iPage);
    }

    @Override
    public List<StockBasicVo> getStockBasicList(StockQuery stockQuery) throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        stockQuery.setOptionalYn(optionalYn);
        Integer type = stockQuery.getType();
        OrderItem orderItem = handleOrderItem(stockQuery.getOrder(), OrderItem.desc("increase"));
        stockQuery.setOrder(orderItem);

        StockSearchTypeEnum stockSearchTypeEnum = StockSearchTypeEnum.getByCode(type);
        if (StockSearchTypeEnum.ALL == stockSearchTypeEnum) {
            List<StockBasicVo> stocks = stockMapper.getStockBasicList(stockQuery);
            return stocks;
        }
        return null;
    }

    @Override
    public void updateStockRealData(List<StockRealData> stockRealDataRecords) throws Exception {
        if (CollectionUtils.isEmpty(stockRealDataRecords)) {
            return;
        }
        StockRealData firstStockRealData = stockRealDataRecords.get(0);
        String realDate = DateUtil.formatYYYYMMDD(firstStockRealData.getRealDate());
        Map<String, StockRealData> stockRealDataMap = stockRealDataRecords.stream().collect(Collectors.toMap(StockRealData::getStockCode, StockRealDataRecord -> StockRealDataRecord));
        List<String> stockCodes = new ArrayList<>();
        stockRealDataRecords.forEach(stockRealData -> {
            stockCodes.add(stockRealData.getStockCode());
        });
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Stock::getStockCode, stockCodes);
        List<Stock> dbStocks = stockMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(dbStocks)) {
            return;
        }
        for (Stock dbStock : dbStocks) {
            String stockCode = dbStock.getStockCode();
            StockRealData stockRealData = stockRealDataMap.get(stockCode);
            updateStockField(dbStock, stockRealData);
        }
        // 更新股票信息
        updateBatchById(dbStocks);
    }


    @Override
    public List<IndustryConceptAvgVo> getRiseIndustryAvg() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return stockMapper.getRiseIndustryAvg(optionalYn);
    }

    @Override
    public List<IndustryConceptAvgVo> getFallIndustryAvg(boolean asc) throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return stockMapper.getFallIndustryAvg(asc, optionalYn);
    }

    @Override
    public List<IndustryConceptAvgVo> getIndustryAvg() throws Exception {
        List<IndustryConceptAvgVo> riseVos = getRiseIndustryAvg();
        List<IndustryConceptAvgVo> fallVos = getFallIndustryAvg(false);
        return mergeIndustryConceptAvgVoList(riseVos, fallVos);
    }

    @Override
    public List<IndustryConceptAvgVo> getRiseConceptAvg() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return stockMapper.getRiseConceptAvg(optionalYn);
    }

    @Override
    public List<IndustryConceptAvgVo> getFallConceptAvg(boolean asc) throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return stockMapper.getFallConceptAvg(asc, optionalYn);
    }

    @Override
    public List<IndustryConceptAvgVo> getConceptAvg() throws Exception {
        List<IndustryConceptAvgVo> riseVos = getRiseConceptAvg();
        List<IndustryConceptAvgVo> fallVos = getFallConceptAvg(false);
        return mergeIndustryConceptAvgVoList(riseVos, fallVos);
    }

    @Override
    public List<NameValuePercentageVo> getIndustryRiseFallCount() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return stockMapper.getIndustryRiseFallCount(optionalYn);
    }

    @Override
    public List<NameValuePercentageVo> getConceptRiseFallCount() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return stockMapper.getConceptRiseFallCount(optionalYn);
    }

    @Override
    public List<IndustryConceptAvgVo> getIndustryConceptAvg(IndustryConceptAvgQuery industryConceptAvgQuery) throws Exception {
        String typeName = industryConceptAvgQuery.getTypeName();
        IndustryConceptTypeEnum industryConceptTypeEnum = IndustryConceptTypeEnum.getByCode(typeName);
        List<IndustryConceptAvgVo> list = null;
        if (IndustryConceptTypeEnum.INDUSTRY == industryConceptTypeEnum) {
            list = getIndustryAvg();
        } else if (IndustryConceptTypeEnum.CONCEPT == industryConceptTypeEnum) {
            list = getConceptAvg();
        }
        return list;
    }

    @Override
    public List<IndustryConceptAvgVo> getRiseIndustryConceptAvg(IndustryConceptAvgQuery industryConceptAvgQuery) throws Exception {
        String typeName = industryConceptAvgQuery.getTypeName();
        IndustryConceptTypeEnum industryConceptTypeEnum = IndustryConceptTypeEnum.getByCode(typeName);
        List<IndustryConceptAvgVo> list = null;
        if (IndustryConceptTypeEnum.INDUSTRY == industryConceptTypeEnum) {
            list = getRiseIndustryAvg();
        } else if (IndustryConceptTypeEnum.CONCEPT == industryConceptTypeEnum) {
            list = getRiseConceptAvg();
        }
        return list;
    }

    @Override
    public List<IndustryConceptAvgVo> getFallIndustryConceptAvg(IndustryConceptAvgQuery industryConceptAvgQuery) throws Exception {
        String typeName = industryConceptAvgQuery.getTypeName();
        IndustryConceptTypeEnum industryConceptTypeEnum = IndustryConceptTypeEnum.getByCode(typeName);
        List<IndustryConceptAvgVo> list = null;
        boolean asc = industryConceptAvgQuery.isAsc();
        if (IndustryConceptTypeEnum.INDUSTRY == industryConceptTypeEnum) {
            list = getFallIndustryAvg(false);
        } else if (IndustryConceptTypeEnum.CONCEPT == industryConceptTypeEnum) {
            list = getFallConceptAvg(asc);
        }
        return list;
    }

    @Override
    public List<NameValuePercentageVo> getMarketOverview(QuotationPieQuery industryConceptAvgQuery) throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        String typeName = industryConceptAvgQuery.getTypeName();
        QuotationPieTypeEnum quotationPieTypeEnum = QuotationPieTypeEnum.getByCode(typeName);
        List<NameValuePercentageVo> list = null;
        if (QuotationPieTypeEnum.STOCK == quotationPieTypeEnum) {
            list = getStockMarketOverview();
        } else if (QuotationPieTypeEnum.INDUSTRY == quotationPieTypeEnum) {
            list = getIndustryMarketOverview();
        } else if (QuotationPieTypeEnum.CONCEPT == quotationPieTypeEnum) {
            list = getConceptMarketOverview();
        }
        return list;
    }

    @Override
    public void calcPercentage(List<NameValuePercentageVo> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            BigDecimal sum = list.stream().map(NameValuePercentageVo::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            list.forEach(vo -> {
                BigDecimal percentage = BigDecimalUtil.percentage(vo.getValue(), sum);
                vo.setPercentage(percentage);
            });
        }
    }

    @Override
    public List<Stock> getStockListByStockCodes(List<String> stockCodes) {
        if (CollectionUtils.isEmpty(stockCodes)) {
            return null;
        }
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Stock::getStockCode, stockCodes);
        return stockMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void updateStockIncreaseMA() throws Exception {


    }

    @Override
    public void updateRealDayIncrease(Stock stock, List<StockKLine> stockKLines) throws Exception {
        if (stock == null) {
            return;
        }
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }
        StockKLine currentStockKLine = stockKLines.get(0);
        BigDecimal currentClosePrice = currentStockKLine.getClosePrice();
        Integer beforeIndex = null;
        BigDecimal increase = null;
        // 5日涨幅:（当前收盘价 - 6天前的收盘价 ）/ 6天前的收盘价
        // 5日均线：(最近5天的收盘价之和) / 5
        beforeIndex = 5;
        increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
        stock.setI5(increase);
        // 10日涨幅
        beforeIndex = 10;
        increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
        stock.setI10(increase);
        // 20日涨幅
        beforeIndex = 20;
        increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
        stock.setI20(increase);
        // 30日涨幅
        beforeIndex = 30;
        increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
        stock.setI30(increase);
        // 60日涨幅
        beforeIndex = 60;
        increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
        stock.setI60(increase);
        // 90日涨幅
        beforeIndex = 90;
        increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
        stock.setI90(increase);
        // 120日涨幅
        beforeIndex = 120;
        increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
        stock.setI120(increase);
        // 250日涨幅
        beforeIndex = 250;
        increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
        stock.setI250(increase);
        // 300日涨幅
        beforeIndex = 300;
        increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
        stock.setI300(increase);
    }

    public BigDecimal getDayIncreaseValue(List<StockKLine> stockKLines, BigDecimal currentClosePrice,
                                          int beforeIndex) {
        StockKLine beforeStockKLine = stockKLines.get(beforeIndex);
        BigDecimal beforeClosePrice = beforeStockKLine.getClosePrice();
        return IncreaseUtil.getIncrease(beforeClosePrice, currentClosePrice);
    }

    @Override
    public void updateRealMA(Stock stock, List<StockKLine> stockKLines) throws Exception {
        if (stock == null) {
            return;
        }
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }

        StockKLine currentStockKLine = stockKLines.get(0);
        BigDecimal currentClosePrice = currentStockKLine.getClosePrice();
        BigDecimal maValue = null;
        Integer beforeIndex = null;
        // 5日均线：(最近5天的收盘价之和) / 5
        beforeIndex = 4;
        maValue = getMAValue(stockKLines, beforeIndex);
        stock.setMa5(maValue);
        // 10日均线
        beforeIndex = 9;
        maValue = getMAValue(stockKLines, beforeIndex);
        stock.setMa10(maValue);
        // 20日均线
        beforeIndex = 19;
        maValue = getMAValue(stockKLines, beforeIndex);
        stock.setMa20(maValue);
        // 30日均线
        beforeIndex = 29;
        maValue = getMAValue(stockKLines, beforeIndex);
        stock.setMa30(maValue);
        // 60日均线
        beforeIndex = 59;
        maValue = getMAValue(stockKLines, beforeIndex);
        stock.setMa60(maValue);
        // 90日均线
        beforeIndex = 89;
        maValue = getMAValue(stockKLines, beforeIndex);
        stock.setMa90(maValue);
        // 120日均线
        beforeIndex = 119;
        maValue = getMAValue(stockKLines, beforeIndex);
        stock.setMa120(maValue);
        // 250日均线
        beforeIndex = 249;
        maValue = getMAValue(stockKLines, beforeIndex);
        stock.setMa250(maValue);
        // 300日均线
        beforeIndex = 299;
        maValue = getMAValue(stockKLines, beforeIndex);
        stock.setMa300(maValue);
    }

    @Override
    public Paging<IndustryConceptIndexVo> getIndustryConceptPageList(IndustryConceptIndexQuery industryConceptIndexQuery) throws Exception {
        String typeName = industryConceptIndexQuery.getTypeName();
        IndustryConceptTypeEnum industryConceptTypeEnum = IndustryConceptTypeEnum.getByCode(typeName);
        Paging<IndustryConceptIndexVo> paging = null;
        if (IndustryConceptTypeEnum.INDUSTRY == industryConceptTypeEnum) {
            paging = industryIndexService.getIndustryIndexPageList(industryConceptIndexQuery);
        } else if (IndustryConceptTypeEnum.CONCEPT == industryConceptTypeEnum) {
            paging = conceptIndexService.getConceptIndexPageList(industryConceptIndexQuery);
        }
        return paging;
    }

    @Override
    public void updateAddStockInfo() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        // 获取到删除的股票信息
        List<String> deleteStockCodes = stockMapper.getDeleteStockCodes();
        // 获取到新增的股票信息
        List<Stock> addStocks = stockMapper.getAddStock();
        List<Stock> addFixStocks = stockMapper.getFixAddStock();
        if (CollectionUtils.isNotEmpty(addFixStocks)) {
            addStocks.addAll(addFixStocks);
            // 删除当日K线数据
            List<String> deleteFixStocks = new ArrayList<>();
            addFixStocks.forEach(item -> {
                deleteFixStocks.add(item.getStockCode());
            });
            stockKLineMapper.deleteByStockCodes(deleteFixStocks);
        }
        if (CollectionUtils.isEmpty(deleteStockCodes) && CollectionUtils.isEmpty(addStocks)) {
            log.info("无数据更新");
            return;
        }
        if (CollectionUtils.isNotEmpty(deleteStockCodes)) {
            // 删除K线数据
            stockKLineMapper.deleteByStockCodes(deleteStockCodes);
            // 删除板块K线数据
            List<String> deleteBkCodes = bkStockMapper.getBkCodesByStockCodes(deleteStockCodes);
            if (CollectionUtils.isNotEmpty(deleteBkCodes)) {
                bkKLineMapper.deleteByBkCodes(deleteBkCodes);
            }
            // 删除板块股票数据
            bkStockMapper.deleteByStockCodes(deleteStockCodes);
        }
        if (CollectionUtils.isNotEmpty(addStocks)) {
            // 调用接口获取新增的股票K线数据
            stockKLineService.syncKLineData(addStocks, 500, null, false, 20, new SyncKLineCallback() {
                @Override
                public void finish() throws Exception {
                    // 新增板块信息
                    Set<String> addIndustrys = new HashSet<>();
                    for (Stock addStock : addStocks) {
                        String industry = addStock.getIndustry();
                        addIndustrys.add(industry);
                    }
                    List<String> existsIndustrys = bkInfoMapper.getExistsBkInfoListByIndustryBkNames(addIndustrys);
                    if (CollectionUtils.isNotEmpty(existsIndustrys)) {
                        addIndustrys.removeAll(existsIndustrys);
                    }
                    if (CollectionUtils.isNotEmpty(addIndustrys)) {
                        List<Stock> addBkStocks = new ArrayList<>();
                        List<String> addBkStockCodes = new ArrayList<>();
                        for (Stock addStock : addStocks) {
                            String industry = addStock.getIndustry();
                            if (addIndustrys.contains(industry)) {
                                addBkStocks.add(addStock);
                                addBkStockCodes.add(addStock.getStockCode());
                            }
                        }
                        if (CollectionUtils.isNotEmpty(addBkStocks)) {
                            // 添加板块信息
                            bkIndustryConceptService.generateIndustryBkInfo(optionalYn,addBkStocks);
                            // 删除板块K线数据
                            List<String> deleteBkCodes = bkStockMapper.getBkCodesByStockCodes(addBkStockCodes);
                            if (CollectionUtils.isNotEmpty(deleteBkCodes)) {
                                bkKLineMapper.deleteByBkCodes(deleteBkCodes);
                            }
                        }
                    }

                    // 删除板块个股为空的板块信息
                    bkInfoMapper.deleteNonSubStockBkInfo();
                    // 更新所有板块的K线数据
                    bkKLineService.initBkKLineData();
                }
            });
        }
    }

    @Override
    public List<String> getStockCodeList() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return getStockCodeList(optionalYn);
    }

    @Override
    public List<String> getStockCodeList(boolean optionalYn) throws Exception {
        return stockMapper.getStockCodeList(optionalYn);
    }

    @Override
    public String getLastTradeDate() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return stockMapper.getLastTradeDate(optionalYn);
    }

    @Override
    public void calcDayIncreaseMA() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        calcDayIncreaseMA(optionalYn);
    }


    @Override
    public void calcDayIncreaseMA(boolean optionalYn) throws Exception {
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (optionalYn) {
            lambdaQueryWrapper.eq(Stock::isOptionalYn, optionalYn);
        }
        List<Stock> stocks = stockMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stocks)) {
            return;
        }
        Map<String, Stock> stockMap = stocks.stream().collect(Collectors.toMap(Stock::getStockCode, Stock -> Stock));
        List<String> stockCodes = new ArrayList<>();
        for (Stock stock : stocks) {
            String stockCode = stock.getStockCode();
            stockCodes.add(stockCode);
        }
        // 多线程计算
        ThreadExecutor.execute("计算阶段涨幅和均线", stockCodes, 100, new ThreadExecutorCallback<String>() {
            @Override
            public void execute(int index, List<String> subList) throws Exception {
                List<Stock> updateStocks = new ArrayList<>();
                for (int i = 0; i < subList.size(); i++) {
                    String stockCode = subList.get(i);
                    Stock stock = stockMap.get(stockCode);
                    // 计算阶段涨幅和均线
                    calcDayIncreaseMA(stock);
                    updateStocks.add(stock);
                }
                // 批量更新
                updateBatchById(updateStocks);
            }

            @Override
            public void finish() throws Exception {
                log.info("计算阶段涨幅和均线完成");
            }
        });
    }


    @Override
    public void calcDayIncreaseMA(Stock stock) throws Exception {
        if (stock == null) {
            return;
        }
        String stockCode = stock.getStockCode();
        List<StockKLine> stockKLines = stockKLineMapper.getTop301StockKLineListByStockCode(stockCode, null);
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }
        StockKLine currentStockKLine = stockKLines.get(0);
        BigDecimal currentClosePrice = currentStockKLine.getClosePrice();
        int afterIndex = 0;
        BigDecimal increase = null;
        // 5日涨幅:（当前收盘价 - 6天前的收盘价 ）/ 6天前的收盘价
        afterIndex = 3;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI3(increase);
        afterIndex = 5;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI5(increase);
        // 10日涨幅
        afterIndex = 10;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI10(increase);
        // 20日涨幅
        afterIndex = 20;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI20(increase);
        // 30日涨幅
        afterIndex = 30;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI30(increase);
        // 60日涨幅
        afterIndex = 60;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI60(increase);
        // 90日涨幅
        afterIndex = 90;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI90(increase);
        // 120日涨幅
        afterIndex = 120;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI120(increase);
        // 250日涨幅
        afterIndex = 250;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI250(increase);
        // 300日涨幅
        afterIndex = 300;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, afterIndex);
        stock.setI300(increase);

        // 计算均线
        BigDecimal maValue = null;
        // 5日均线：(最近5天的收盘价之和) / 5
        afterIndex = 3;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa3(maValue);
        afterIndex = 5;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa5(maValue);
        // 10日均线
        afterIndex = 10;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa10(maValue);
        // 20日均线
        afterIndex = 20;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa20(maValue);
        // 30日均线
        afterIndex = 30;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa30(maValue);
        // 60日均线
        afterIndex = 60;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa60(maValue);
        // 90日均线
        afterIndex = 90;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa90(maValue);
        // 120日均线
        afterIndex = 120;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa120(maValue);
        // 250日均线
        afterIndex = 250;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa250(maValue);
        // 300日均线
        afterIndex = 300;
        maValue = getMAValue(stockKLines, afterIndex);
        stock.setMa300(maValue);

        currentStockKLine.setUpdateTime(new Date());

    }

    @Override
    public void importThsStock(List<Stock> stockList) throws Exception {
        // 有，就修改市值、网站、上市日期，同花顺行业
        // 没有，则添加
        List<String> stockCodes = new ArrayList<>();
        stockList.forEach(item -> {
            stockCodes.add(item.getStockCode());
        });
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Stock::getStockCode, stockCodes);
        List<Stock> dbStocks = stockMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(dbStocks)) {
            return;
        }
        Map<String, Stock> dbStockMap = dbStocks.stream().collect(Collectors.toMap(Stock::getStockCode, Stock -> Stock));
        List<Stock> addStocks = new ArrayList<>();
        List<Stock> updateStocks = new ArrayList<>();
        String addBatchNo = BatchNoUtil.getBatchNo();
        String updateBatchNo = BatchNoUtil.getBatchNo();
        for (Stock stock : stockList) {
            String stockCode = stock.getStockCode();
            if (dbStockMap.containsKey(stockCode)) {
                Stock dbStock = dbStockMap.get(stockCode);
                // 修改字段
                dbStock.setTotalMarketValue(stock.getTotalMarketValue());
                dbStock.setCirculationMarketValue(stock.getCirculationMarketValue());
                dbStock.setWebsite(stock.getWebsite());
                dbStock.setListingDate(stock.getListingDate());
                dbStock.setThsIndustry(stock.getThsIndustry());
                dbStock.setUpdateTime(new Date());
                dbStock.setBatchNo(updateBatchNo);
                updateStocks.add(dbStock);
            } else {
                stock.setBatchNo(addBatchNo);
                addStocks.add(stock);
            }
        }
        if (CollectionUtils.isNotEmpty(addStocks)) {
            saveBatch(addStocks, 200);
        }
        if (CollectionUtils.isNotEmpty(updateStocks)) {
            updateBatchById(updateStocks, 200);
        }
    }

    @Override
    public List<String> getNotOptionalStockCodeList() throws Exception {
        return stockMapper.getNotOptionalStockCodeList();
    }

    public BigDecimal getTodayDayIncreaseValue(List<StockKLine> stockKLines, BigDecimal currentClosePrice,
                                               int afterIndex) {
        int listSize = stockKLines.size();
        if (afterIndex >= listSize) {
            return null;
        }
        System.out.println("listSize = " + listSize);
        System.out.println("afterIndex = " + afterIndex);
        StockKLine beforeStockKLine = stockKLines.get(afterIndex);
        BigDecimal beforeClosePrice = beforeStockKLine.getClosePrice();
        return IncreaseUtil.getIncrease(beforeClosePrice, currentClosePrice);
    }


    public BigDecimal getMAValue(List<StockKLine> stockKLines, int afterIndex) {
        int listSize = stockKLines.size();
        if (afterIndex > (listSize - 1)) {
            return null;
        }
        List<StockKLine> subList = stockKLines.subList(0, afterIndex);
        BigDecimal sum = subList.stream().map(StockKLine::getClosePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        int num = subList.size();
        BigDecimal maValue = BigDecimalUtil.divide(sum, num, 2);
        return maValue;
    }


    @Override
    public List<NameValuePercentageVo> getStockMarketOverview() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        List<NameValuePercentageVo> list = stockMapper.getStockMarketOverview(optionalYn);
        calcPercentage(list);
        return list;
    }

    @Override
    public List<NameValuePercentageVo> getIndustryMarketOverview() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        List<NameValuePercentageVo> list = stockMapper.getIndustryMarketOverview(optionalYn);
        calcPercentage(list);
        return list;
    }

    @Override
    public List<NameValuePercentageVo> getConceptMarketOverview() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        List<NameValuePercentageVo> list = stockMapper.getConceptMarketOverview(optionalYn);
        calcPercentage(list);
        return list;
    }

    @Override
    public List<NameValuePercentageVo> getRiseFallLimit() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return stockMapper.getRiseFallLimit(optionalYn);
    }

    @Override
    public List<NameValuePercentageVo> getIncreaseDistribution() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        return stockMapper.getIncreaseDistribution(optionalYn);
    }

    @Override
    public MarketOverviewDistributionVo getMarketOverviewDistribution() throws Exception {
        // 上涨下跌家数
        List<NameValuePercentageVo> stockMarketOverviews = getStockMarketOverview();
        // 涨跌停家数
        List<NameValuePercentageVo> riseFallLimits = getRiseFallLimit();
        // 涨幅分布
        List<NameValuePercentageVo> increaseDistributions = getIncreaseDistribution();
        MarketOverviewDistributionVo vo = new MarketOverviewDistributionVo();
        vo.setStockMarketOverviews(stockMarketOverviews);
        vo.setRiseFallLimits(riseFallLimits);
        vo.setIncreaseDistributions(increaseDistributions);
        return vo;
    }

    @Override
    public BigDecimal getMarketOverviewScore() throws Exception {
        // 1. 获取涨跌家数百分比
        List<NameValuePercentageVo> stockMarketOverviews = getStockMarketOverview();
        // 2. 获取行业涨跌百分比
        List<NameValuePercentageVo> industryMarketOverviews = getIndustryMarketOverview();
        // 3. 获取概念涨跌百分比
        List<NameValuePercentageVo> conceptMarketOverviews = getConceptMarketOverview();
        // 4. 获取涨跌分布百分比
        List<NameValuePercentageVo> increaseDistributions = getIncreaseDistribution();
        return calcMarketScore(stockMarketOverviews, industryMarketOverviews, conceptMarketOverviews, increaseDistributions);
    }

    @Override
    public BigDecimal calcMarketScore(List<NameValuePercentageVo> stockMarketOverviews, List<NameValuePercentageVo> industryMarketOverviews, List<NameValuePercentageVo> conceptMarketOverviews, List<NameValuePercentageVo> increaseDistributions) throws Exception {
        BigDecimal scoreSum = BigDecimal.ZERO;
        int count = 0;
        // 1. 获取涨跌家数百分比
        if (CollectionUtils.isNotEmpty(stockMarketOverviews)) {
            // 求和
            // 求上涨百分比
            BigDecimal sum = stockMarketOverviews.stream().map(NameValuePercentageVo::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, BigDecimal> map = stockMarketOverviews.stream().collect(Collectors.toMap(NameValuePercentageVo::getName, NameValuePercentageVo::getValue));
            BigDecimal num = map.get("1");
            BigDecimal score = BigDecimalUtil.percentage(num, sum);
            scoreSum = BigDecimalUtil.add(scoreSum, score);
            count++;
        }
        // 2. 获取行业涨跌百分比
        if (CollectionUtils.isNotEmpty(industryMarketOverviews)) {
            BigDecimal sum = industryMarketOverviews.stream().map(NameValuePercentageVo::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, BigDecimal> map = industryMarketOverviews.stream().collect(Collectors.toMap(NameValuePercentageVo::getName, NameValuePercentageVo::getValue));
            BigDecimal num = map.get("1");
            BigDecimal score = BigDecimalUtil.percentage(num, sum);
            scoreSum = BigDecimalUtil.add(scoreSum, score);
            count++;
        }
        // 3. 获取概念涨跌百分比
        if (CollectionUtils.isNotEmpty(conceptMarketOverviews)) {
            BigDecimal sum = conceptMarketOverviews.stream().map(NameValuePercentageVo::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, BigDecimal> map = conceptMarketOverviews.stream().collect(Collectors.toMap(NameValuePercentageVo::getName, NameValuePercentageVo::getValue));
            BigDecimal num = map.get("1");
            BigDecimal score = BigDecimalUtil.percentage(num, sum);
            scoreSum = BigDecimalUtil.add(scoreSum, score);
            count++;
        }
        // 4. 计算涨跌分布百分比
        if (CollectionUtils.isNotEmpty(increaseDistributions)) {
            Map<String, BigDecimal> map = increaseDistributions.stream().collect(Collectors.toMap(NameValuePercentageVo::getName, NameValuePercentageVo::getValue));
            BigDecimal x2 = map.get("2");
            BigDecimal y2 = map.get("-2");
            BigDecimal xy2 = BigDecimalUtil.add(x2, y2);
            BigDecimal score = BigDecimalUtil.percentage(x2, xy2);
            scoreSum = BigDecimalUtil.add(scoreSum, score);
            count++;
        }
        // 计算平均分数
        BigDecimal temperature = BigDecimalUtil.divide(scoreSum, count, 2);
        log.info("temperature：" + temperature);
        return temperature;
    }

    @Override
    public MarketOverviewInfoVo getMarketOverviewInfo() throws Exception {
        MarketOverviewInfoVo marketOverviewInfoVo = new MarketOverviewInfoVo();
        // 获取个股涨跌情况
        List<NameValuePercentageVo> stockMarketOverviews = getStockMarketOverview();
        if (CollectionUtils.isNotEmpty(stockMarketOverviews)) {
            stockMarketOverviews.forEach(item -> {
                if ("1".equals(item.getName())) {
                    marketOverviewInfoVo.setStockRiseCount(item.getValue());
                    marketOverviewInfoVo.setStockRisePercentage(item.getPercentage());
                } else if ("-1".equals(item.getName())) {
                    marketOverviewInfoVo.setStockFallCount(item.getValue());
                    marketOverviewInfoVo.setStockFallPercentage(item.getPercentage());
                }
            });
        }
        // 获取行业涨跌情况
        List<NameValuePercentageVo> industryMarketOverviews = getIndustryMarketOverview();
        if (CollectionUtils.isNotEmpty(industryMarketOverviews)) {
            industryMarketOverviews.forEach(item -> {
                if ("1".equals(item.getName())) {
                    marketOverviewInfoVo.setIndustryRiseCount(item.getValue());
                    marketOverviewInfoVo.setIndustryRisePercentage(item.getPercentage());
                } else if ("-1".equals(item.getName())) {
                    marketOverviewInfoVo.setIndustryFallCount(item.getValue());
                    marketOverviewInfoVo.setIndustryFallPercentage(item.getPercentage());
                }
            });
        }
        // 获取概念涨跌情况
        List<NameValuePercentageVo> conceptMarketOverviews = getConceptMarketOverview();
        if (CollectionUtils.isNotEmpty(conceptMarketOverviews)) {
            conceptMarketOverviews.forEach(item -> {
                if ("1".equals(item.getName())) {
                    marketOverviewInfoVo.setConceptRiseCount(item.getValue());
                    marketOverviewInfoVo.setConceptRisePercentage(item.getPercentage());
                } else if ("-1".equals(item.getName())) {
                    marketOverviewInfoVo.setConceptFallCount(item.getValue());
                    marketOverviewInfoVo.setConceptFallPercentage(item.getPercentage());
                }
            });
        }

        List<NameValuePercentageVo> increaseDistributions = getIncreaseDistribution();
        if (CollectionUtils.isNotEmpty(increaseDistributions)) {
            Map<String, BigDecimal> map = increaseDistributions.stream().collect(Collectors.toMap(NameValuePercentageVo::getName, NameValuePercentageVo::getValue));
            // 获取涨停百分比
            BigDecimal x = null;
            BigDecimal y = null;
            x = map.get("4");
            y = map.get("-4");
            marketOverviewInfoVo.setRiseLimitCount(x);
            marketOverviewInfoVo.setFallLimitCount(y);
            if (x != null && y != null) {
                BigDecimal sum = BigDecimalUtil.add(x, y);
                BigDecimal risePercentage = BigDecimalUtil.percentage(x, sum);
                BigDecimal fallPercentage = BigDecimalUtil.percentage(y, sum);
                marketOverviewInfoVo.setRiseLimitPercentage(risePercentage);
                marketOverviewInfoVo.setFallLimitPercentage(fallPercentage);
            } else if (x == null) {
                // 涨停家数为0
                marketOverviewInfoVo.setRiseLimitPercentage(BigDecimal.ZERO);
                marketOverviewInfoVo.setFallLimitPercentage(new BigDecimal(100));
            } else if (y == null) {
                // 跌停家数为0
                marketOverviewInfoVo.setRiseLimitPercentage(new BigDecimal(100));
                marketOverviewInfoVo.setFallLimitPercentage(BigDecimal.ZERO);
            }
            // 获取大涨百分比
            x = map.get("3");
            y = map.get("-3");
            marketOverviewInfoVo.setBigRiseCount(x);
            marketOverviewInfoVo.setBigFallCount(y);
            if (x != null && y != null) {
                BigDecimal sum = BigDecimalUtil.add(x, y);
                BigDecimal risePercentage = BigDecimalUtil.percentage(x, sum);
                BigDecimal fallPercentage = BigDecimalUtil.percentage(y, sum);
                marketOverviewInfoVo.setBigRisePercentage(risePercentage);
                marketOverviewInfoVo.setBigFallPercentage(fallPercentage);
            } else if (x == null) {
                // 大涨家数为0
                marketOverviewInfoVo.setBigRisePercentage(BigDecimal.ZERO);
                marketOverviewInfoVo.setBigFallPercentage(new BigDecimal(100));
            } else if (y == null) {
                // 大跌家数为0
                marketOverviewInfoVo.setRiseLimitPercentage(new BigDecimal(100));
                marketOverviewInfoVo.setFallLimitPercentage(BigDecimal.ZERO);
            }
        }
        return marketOverviewInfoVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStockDayIncreaseAndMA() throws Exception {
        String lineDate = stockKLineMapper.getMaxLineDate();
        if (StringUtils.isBlank(lineDate)) {
            return;
        }
        LambdaQueryWrapper<StockKLine> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StockKLine::getLineDate, lineDate);
        List<StockKLine> stockKLines = stockKLineMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }
        Map<String, StockKLine> map = stockKLines.stream().collect(Collectors.toMap(StockKLine::getStockCode, StockKLine -> StockKLine));
        List<Stock> stocks = stockMapper.selectList(null);
        if (CollectionUtils.isEmpty(stocks)) {
            return;
        }
        List<Stock> updateStocks = new ArrayList<>();
        String batchNo = BatchNoUtil.getBatchNo();
        for (Stock stock : stocks) {
            String stockCode = stock.getStockCode();
            StockKLine stockKLine = map.get(stockCode);
            if (stockKLine == null) {
                continue;
            }
            stock.setBatchNo(batchNo);
            stock.setUpdateTime(new Date());
            updateStocks.add(stock);
        }
        // 批量更新
        updateBatchById(updateStocks);
    }

    @Override
    public StockDynamicQueryVo getStockDynamicList(StockDynamicQuery stockDynamicQuery) throws Exception {
        String sql = stockDynamicQuery.getSql();
        sql = SqlParserUtil.cleanSql(sql);
        if (StringUtils.isBlank(sql)) {
            throw new BusinessException("SQL不能为空");
        }
        if (!sql.contains("select")) {
            throw new BusinessException("无效的查询SQL");
        }
        // 查询
        StockDynamicQueryVo stockDynamicQueryVo = jdbcTemplate.query(sql, new ResultSetExtractor<StockDynamicQueryVo>() {
            @Override
            public StockDynamicQueryVo extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                List<String> columns = new ArrayList<>();
                List<String> types = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    columns.add(columnName);
                    String columnTypeName = metaData.getColumnTypeName((i + 1));
                    types.add(columnTypeName);
                }
                StockDynamicQueryVo stockDynamicQueryVo = new StockDynamicQueryVo();
                stockDynamicQueryVo.setColumns(columns);
                stockDynamicQueryVo.setTypes(types);
                List<Map<String, Object>> list = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 0; i < count; i++) {
                        String key = columns.get(i);
                        Object value = resultSet.getObject(i + 1);
                        row.put(key, value);
                    }
                    list.add(row);
                }
                stockDynamicQueryVo.setData(list);
                if (CollectionUtils.isNotEmpty(list)) {
                    stockDynamicQueryVo.setTotal(list.size());
                }
                return stockDynamicQueryVo;
            }
        });
        return stockDynamicQueryVo;
    }

    private List<ColumnAliasName> getStockDynamicColumnVoList(String sql) {
        return null;
    }

    private List<IndustryConceptAvgVo> mergeIndustryConceptAvgVoList(List<IndustryConceptAvgVo> riseVos, List<IndustryConceptAvgVo> fallVos) {
        if (CollectionUtils.isEmpty(riseVos)) {
            riseVos = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(fallVos)) {
            fallVos = new ArrayList<>();
        }
        int total = 24;
        int half = total / 2;
        int riseSize = riseVos.size();
        int fallSize = fallVos.size();
        if (riseSize >= half && fallSize >= half) {
            riseVos = riseVos.subList(0, half);
            fallVos = fallVos.subList(0, half);
        } else if (riseSize >= half && fallSize < half) {
            int diff = total - fallSize;
            riseSize = riseSize >= diff ? diff : riseSize;
            riseVos = riseVos.subList(0, riseSize);
            fallVos = fallVos.subList(0, fallSize);
        } else if (riseSize < half && fallSize >= half) {
            int diff = total - riseSize;
            fallSize = fallSize >= diff ? diff : fallSize;
            riseVos = riseVos.subList(0, riseSize);
            fallVos = fallVos.subList(0, fallSize);
        } else {
            riseVos = riseVos.subList(0, (riseSize / 2));
            fallVos = fallVos.subList(0, (fallSize / 2));
        }
        fallVos = fallVos.stream().sorted(Comparator.comparing(IndustryConceptAvgVo::getAvg).reversed()).collect(Collectors.toList());
        List<IndustryConceptAvgVo> allVos = new ArrayList<>();
        allVos.addAll(riseVos);
        allVos.addAll(fallVos);
        return allVos;
    }

    private void updateStockField(Stock dbStock, StockRealData stockRealData) {
        if (dbStock == null) {
            return;
        }
        if (stockRealData == null) {
            return;
        }
        // 最新价格
        dbStock.setPrice(stockRealData.getCurrentPrice());
        // 最新涨幅
        dbStock.setIncrease(stockRealData.getCurrentIncrease());
        // 今日开盘价
        dbStock.setOpenPrice(stockRealData.getOpeningPrice());
        // 昨收
        dbStock.setYesterdayClosingPrice(stockRealData.getYesterdayClosingPrice());
        // 今日最高价
        dbStock.setHighPrice(stockRealData.getHighPrice());
        // 今日最低价
        dbStock.setLowPrice(stockRealData.getLowPrice());
        // 交易金额
        dbStock.setTradeAmount(stockRealData.getTradeAmount());
        // 交易量
        dbStock.setTradeNumber(stockRealData.getTradeNumber());
        // 实时数据更新日期
        dbStock.setRealDate(stockRealData.getRealDate());
        // 实时数据更新时间
        dbStock.setRealTime(stockRealData.getRealTime());
        // 实时数据更新批次号
        dbStock.setBatchNo(stockRealData.getBatchNo());
        // 修改时间
        dbStock.setUpdateTime(new Date());
        // 是否停牌
        dbStock.setSuspensionYn(stockRealData.getSuspensionYn());
    }

    private void updateStockField(Stock dbStock, Stock stock) {
        if (dbStock == null) {
            return;
        }
        if (stock == null) {
            return;
        }
        dbStock.setTotalMarketValue(stock.getTotalMarketValue());
        dbStock.setCirculationMarketValue(stock.getCirculationMarketValue());
        dbStock.setBatchNo(stock.getBatchNo());
        dbStock.setStockFullCode(stock.getStockFullCode());
        dbStock.setMarketType(stock.getMarketType());
        dbStock.setMarketTypeName(stock.getMarketTypeName());
        dbStock.setOptionalDate(stock.getOptionalDate());
        dbStock.setUpdateTime(new Date());
    }

    private List<StockRecord> convertStockListToStockRecordList(List<Stock> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<StockRecord> stockRecordList = new ArrayList<>();
            for (Stock stock : list) {
                StockRecord stockRecord = new StockRecord();
                BeanUtils.copyProperties(stock, stockRecord);
                stockRecordList.add(stockRecord);
            }
            return stockRecordList;
        }
        return null;
    }

}
