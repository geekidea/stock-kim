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
import io.geekidea.framework.thread.ThreadExecutor;
import io.geekidea.framework.thread.ThreadExecutorCallback;
import io.geekidea.stock.api.TencentStockApi;
import io.geekidea.stock.callback.SyncKLineCallback;
import io.geekidea.stock.dto.vo.*;
import io.geekidea.framework.common.exception.BusinessException;
import io.geekidea.framework.util.BatchNoUtil;
import io.geekidea.framework.util.BigDecimalUtil;
import io.geekidea.framework.util.DateUtil;
import io.geekidea.framework.util.PatternUtil;
import io.geekidea.stock.mapper.StockMapper;
import io.geekidea.stock.constant.RedisKey;
import io.geekidea.stock.dto.query.StockKLineQuery;
import io.geekidea.stock.dto.query.StockKLineSearchQuery;
import io.geekidea.stock.entity.Stock;
import io.geekidea.stock.entity.StockKLine;
import io.geekidea.stock.entity.StockRealData;
import io.geekidea.stock.entity.SyncKLineErrorData;
import io.geekidea.stock.mapper.StockKLineMapper;
import io.geekidea.stock.mapper.SyncKLineErrorDataMapper;
import io.geekidea.stock.service.StockKLineService;
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.stock.service.SyncKLineErrorDataService;
import io.geekidea.stock.util.IncreaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * <pre>
 * K线数据 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-18
 */
@Slf4j
@Service
public class StockKLineServiceImpl extends BaseServiceImpl<StockKLineMapper, StockKLine> implements StockKLineService {

    @Autowired
    private StockKLineMapper stockKLineMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SyncKLineErrorDataService syncKLineErrorDataService;

    @Autowired
    private SyncKLineErrorDataMapper syncKLineErrorDataMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveStockKLine(StockKLine stockKLine) throws Exception {
        return super.save(stockKLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStockKLine(StockKLine stockKLine) throws Exception {
        return super.updateById(stockKLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteStockKLine(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public StockKLineQueryVo getStockKLineById(Long id) throws Exception {
        return stockKLineMapper.getStockKLineById(id);
    }

    @Override
    public Paging<StockKLineQueryVo> getStockKLinePageList(StockKLineQuery stockKLineQuery) throws Exception {
        Page page = buildPageQuery(stockKLineQuery, OrderItem.desc("create_time"));
        IPage<StockKLineQueryVo> iPage = stockKLineMapper.getStockKLinePageList(page, stockKLineQuery);
        return new Paging(iPage);
    }

    @Override
    public StockKLineVo getStockKLineList(StockKLineSearchQuery stockKLineSearchQuery) throws Exception {
        String keyword = stockKLineSearchQuery.getKeyword();
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException("参数不能为空");
        }
        String startDate = stockKLineSearchQuery.getStartDate();
        if (StringUtils.isBlank(startDate)) {
            startDate = "2020-03-02";
        }
        if (PatternUtil.isNumber(keyword)) {
            stockKLineSearchQuery.setLineCode(keyword);
        } else {
            stockKLineSearchQuery.setLineName(keyword);
        }
        List<StockKLineBasicVo> stockKLineBasicVos = stockKLineMapper.getStockKLineBasicList(stockKLineSearchQuery);
        if (CollectionUtils.isEmpty(stockKLineBasicVos)) {
            return null;
        }
        List<Object[]> data = new ArrayList<>();
        for (StockKLineBasicVo stockKLineBasicVo : stockKLineBasicVos) {
            Object[] objects = new Object[10];
            // 日期，开盘(open)，收盘(close)，最低(lowest)，最高(highest)
            objects[0] = stockKLineBasicVo.getLineDate();
            objects[1] = stockKLineBasicVo.getOpenPrice();
            objects[2] = stockKLineBasicVo.getClosePrice();
            objects[3] = stockKLineBasicVo.getLowPrice();
            objects[4] = stockKLineBasicVo.getHighPrice();
            objects[5] = stockKLineBasicVo.getIncrease();
            objects[6] = 2.1;
            objects[7] = stockKLineBasicVo.getTradeNumber();
            objects[8] = stockKLineBasicVo.getTradeAmount();
            objects[9] = stockKLineBasicVo.getTurnoverRate();
            data.add(objects);
        }
        StockKLineBasicVo firstStockKLineBasicVo = stockKLineBasicVos.get(0);
        String stockCode = firstStockKLineBasicVo.getStockCode();
        Stock stock = stockMapper.selectById(stockCode);
        StockKLineVo stockKLineVo = new StockKLineVo();
        stockKLineVo.setStock(stock);
        stockKLineVo.setData(data);
        return stockKLineVo;
    }

    @Override
    public StockBasicKLineVo getStockBasicKLineList(StockKLineSearchQuery stockKLineSearchQuery) throws Exception {
        String keyword = stockKLineSearchQuery.getKeyword();
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException("参数不能为空");
        }
        LambdaQueryWrapper<StockKLine> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StockKLine::getStockCode, keyword)
                .or()
                .eq(StockKLine::getStockName, keyword);
        lambdaQueryWrapper.orderByAsc(StockKLine::getLineDate);
        List<StockKLine> stockKLines = stockKLineMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockKLines)) {
            return null;
        }
        List<StockKLine> filterList = stockKLines.stream().filter(new Predicate<StockKLine>() {
            @Override
            public boolean test(StockKLine stockKLine) {
                boolean result = !stockKLine.getLineDate().endsWith("日");
                return result;
            }
        }).collect(Collectors.toList());
        List<String> xAxisData = new ArrayList<>();
        List<Object[]> seriesData = new ArrayList<>();
        for (StockKLine stockKLine : filterList) {
            Object[] objects = new Object[4];
            // 日期，开盘(open)，收盘(close)，最低(lowest)，最高(highest)
            xAxisData.add(stockKLine.getLineDate());
            objects[0] = stockKLine.getOpenPrice();
            objects[1] = stockKLine.getClosePrice();
            objects[2] = stockKLine.getLowPrice();
            objects[3] = stockKLine.getHighPrice();
            seriesData.add(objects);
        }
        StockBasicKLineVo stockBasicKLineVo = new StockBasicKLineVo();
        stockBasicKLineVo.setXData(xAxisData);
        stockBasicKLineVo.setSData(seriesData);
        return stockBasicKLineVo;
    }

    @Override
    public void syncKLineData() throws Exception {
        try {
            List<Stock> stocks = stockMapper.getNonKLineDataStocks();
            if (CollectionUtils.isEmpty(stocks)) {
                return;
            }
            boolean isUpdate = false;
            syncKLineData(stocks, 425, null, isUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void syncKLineData(List<Stock> stocks, int day, Date beforeEndDate, boolean isUpdate, Integer splitSize, SyncKLineCallback syncKLineCallback) throws Exception {
        redisTemplate.delete(RedisKey.SYNC_K_LINE_DATA_PROGRESS);
        redisTemplate.delete(RedisKey.SYNC_K_LINE_DATA_TIMEOUT_RESULT);
        try {
            int total = stocks.size();
            int successCount = 0;
            int failCount = 0;

            ConcurrentMap<String, Stock> stockMap = new ConcurrentHashMap<>();
            List<String> stockCodes = new ArrayList<>();
            for (Stock stock : stocks) {
                String stockCode = stock.getStockCode();
                stockCodes.add(stockCode);
                stockMap.put(stockCode, stock);
            }

            if (splitSize == null) {
                splitSize = 100;
            }

            ThreadExecutor.execute(stockCodes, splitSize, new ThreadExecutorCallback<String>() {

                @Override
                public void execute(int index, List<String> subList) throws Exception {
                    for (int j = 0; j < subList.size(); j++) {
                        String stockCode = subList.get(j);
                        Stock stock = stockMap.get(stockCode);
                        try {
                            syncKLineData(stock, j, day, beforeEndDate, isUpdate);
//                                    successCount++;
                        } catch (Exception e) {
//                                    failCount++;
                            e.printStackTrace();
                            // 记录异常数据到数据库
                            String stockName = stock.getStockName();
                            String errorMsg = e.getMessage();
                            SyncKLineErrorData syncKLineErrorData = new SyncKLineErrorData();
                            syncKLineErrorData.setLineCode(stockCode);
                            syncKLineErrorData.setLineName(stockName);
                            syncKLineErrorData.setErrorMsg(errorMsg);
                            syncKLineErrorData.setSerialNumber(j);
                            syncKLineErrorData.setSyncDay(day);
                            syncKLineErrorData.setBeforeEndDate(beforeEndDate);
                            syncKLineErrorData.setIsUpdate(isUpdate);
                            syncKLineErrorDataService.save(syncKLineErrorData);
                        } finally {
                            // 当前进度
//                            Integer current = (j + 1);
//                            ProgressVo progressVo = new ProgressVo();
//                            progressVo.setTotal(total);
//                            progressVo.setCurrent(current);
//                            progressVo.setSuccess(successCount);
//                            progressVo.setFail(failCount);
//                            BigDecimal progress = BigDecimalUtil.percentage(current, total);
//                            progressVo.setProgress(progress);
//                            redisTemplate.opsForValue().set(RedisKey.SYNC_K_LINE_DATA_PROGRESS, progressVo, 1, TimeUnit.HOURS);
                        }
                    }
                }

                @Override
                public void finish() throws Exception {
                    // 更新涨幅、均线
                    // 同步涨幅类型
//                    updateKLineIncrease(stocks, null);
                    // 同步每日涨幅
//                    updateKLineDayIncreaseByStocks(stocks);
                    // 同步均线
//                    updateKLineMAByStocks(stocks);
                    log.info("多线程同步K线数据完成");
                    if (syncKLineCallback != null) {
                        syncKLineCallback.finish();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void syncKLineData(List<Stock> stocks, int day, Date beforeEndDate, boolean isUpdate) throws Exception {
        syncKLineData(stocks, day, beforeEndDate, isUpdate, null, null);
    }

    @Override
    public void syncKLineData(Stock stock, int index, int day, Date beforeEndDate, boolean isUpdate) throws Exception {
        String stockCode = stock.getStockCode();
        String stockName = stock.getStockName();
        try {
            // 调用API接口
            List<StockKLine> stockKLines = null;
            boolean isTimeoutException = false;
            boolean isSuccess = false;
            Integer successIndex = null;
            for (int i = 0; i < 3; i++) {
                try {
                    stockKLines = TencentStockApi.getStockKLine(stockCode, stockName, day);
                    isSuccess = true;
                    successIndex = i;
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    // 再查询2次，如果还是异常，则抛出异常
                    if (e instanceof SocketTimeoutException) {
                        isTimeoutException = true;
                    } else {
                        throw new BusinessException(e.getMessage());
                    }
                }
            }
            if (isSuccess) {
                // 涨幅振幅计算
                calcKLineIncreaseAmplitude(stockKLines);
            }
            // 有异常时，记录最终结果
            if (isTimeoutException) {
                SocketTimeoutVo socketTimeoutVo = new SocketTimeoutVo();
                socketTimeoutVo.setResult(isSuccess);
                socketTimeoutVo.setSuccessIndex(successIndex);
                socketTimeoutVo.setStockCode(stockCode);
                socketTimeoutVo.setStockName(stockName);
                socketTimeoutVo.setDay(day);
                redisTemplate.opsForHash().put(RedisKey.SYNC_K_LINE_DATA_TIMEOUT_RESULT, stockCode, socketTimeoutVo);
                redisTemplate.expire(RedisKey.SYNC_K_LINE_DATA_TIMEOUT_RESULT, 1, TimeUnit.HOURS);
            }

            if (CollectionUtils.isEmpty(stockKLines)) {
                log.error(stockCode + "," + stockName + " 数据为空");
                throw new BusinessException(stockCode + "," + stockName + " 数据为空");
            }

            List<StockKLine> addStockKLineList = new ArrayList<>();
            List<StockKLine> updateStockKLineList = new ArrayList<>();
            if (isUpdate) {
                // 需要更新
                List<String> lineDates = new ArrayList<>();
                stockKLines.forEach(item -> {
                    lineDates.add(item.getLineDate());
                });
                List<StockKLine> dbStockKLines = stockKLineMapper.getStockKLineListByLineDates(stockCode, lineDates);
                if (CollectionUtils.isNotEmpty(dbStockKLines)) {
                    Map<String, StockKLine> dbStockKLineMap = dbStockKLines.stream().collect(Collectors.toMap(StockKLine::getLineDate, StockKLine -> StockKLine));
                    for (StockKLine stockKLine : stockKLines) {
                        String lineDate = stockKLine.getLineDate();
                        StockKLine dbStockKLine = dbStockKLineMap.get(lineDate);
                        if (dbStockKLine == null) {
                            // 添加数据
                            addStockKLineList.add(stockKLine);
                        } else {
                            // 更新数据
                            dbStockKLine.setOpenPrice(stockKLine.getOpenPrice());
                            dbStockKLine.setClosePrice(stockKLine.getClosePrice());
                            dbStockKLine.setHighPrice(stockKLine.getHighPrice());
                            dbStockKLine.setLowPrice(stockKLine.getLowPrice());
                            dbStockKLine.setTradeNumber(stockKLine.getTradeNumber());
                            dbStockKLine.setTradeAmount(stockKLine.getTradeAmount());
                            dbStockKLine.setTurnoverRate(stockKLine.getTurnoverRate());
                            dbStockKLine.setUpdateTime(new Date());
                            updateStockKLineList.add(dbStockKLine);
                        }
                    }
                } else {
                    addStockKLineList.addAll(stockKLines);
                }
            } else {
                addStockKLineList.addAll(stockKLines);
            }
            if (CollectionUtils.isNotEmpty(addStockKLineList)) {
                saveBatch(addStockKLineList);
                log.info("保存{},{},{}K线数据成功", index, stockCode, stockName);
            }
            if (CollectionUtils.isNotEmpty(updateStockKLineList)) {
                updateBatchById(updateStockKLineList);
                log.info("更新{},{},{}K线数据成功", index, stockCode, stockName);
            }
        } catch (Exception e) {
            log.error("同步{},{},{}K线数据异常", index, stockCode, stockName);
            e.printStackTrace();
            throw new BusinessException("同步 " + stockCode + " " + stockName + " K线数据异常," + e.getMessage());
        }
    }

    @Override
    public void calcKLineIncreaseAmplitude(List<StockKLine> stockKLines) throws Exception {
        // 获取所有股票，循环
        // 获取所有K线数据，循环上一个收盘价（start）当前收盘价（end）
        // 振幅：(最高-最低)/上一个收盘价 (11-9)/10*100
        if (CollectionUtils.isEmpty(stockKLines)) {
            log.error("K线数据为空");
            return;
        }
        StockKLine firstStockKLine = stockKLines.get(0);
        log.info("计算K线当日涨幅：" + firstStockKLine.getStockCode() + "," + firstStockKLine.getStockName());
        for (int i = 0; i < stockKLines.size(); i++) {
            if (i == 0) {
                continue;
            }
            StockKLine preStockKLine = stockKLines.get(i - 1);
            StockKLine stockKLine = stockKLines.get(i);
            BigDecimal yesterdayClosePrice = preStockKLine.getClosePrice();
            BigDecimal closePrice = stockKLine.getClosePrice();
            BigDecimal maxPrice = stockKLine.getHighPrice();
            BigDecimal minPrice = stockKLine.getLowPrice();
            // 获取当日涨幅
            BigDecimal increase = IncreaseUtil.getIncrease(yesterdayClosePrice, closePrice);
            stockKLine.setIncrease(increase);
            // 获取当日振幅
            BigDecimal amplitude = IncreaseUtil.getAmplitude(yesterdayClosePrice, maxPrice, minPrice);
            stockKLine.setAmplitude(amplitude);
        }

    }

    @Override
    public void updateKLineIncrease(List<Stock> stocks, String startDate) throws Exception {
        // 获取所有股票，循环
        // 获取所有K线数据，循环上一个收盘价（start）当前收盘价（end）
        // 振幅：(最高-最低)/上一个收盘价 (11-9)/10*100
        if (CollectionUtils.isEmpty(stocks)) {
            log.error("股票数据为空");
            return;
        }

        String batchNo = BatchNoUtil.getBatchNo();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            String stockCode = stock.getStockCode();
            log.info("K线：" + (i + 1) + "，" + stockCode + "," + stock.getStockName());
            List<StockKLine> stockKLines = getStockKLineListByStockCode(stockCode, startDate);
            if (CollectionUtils.isEmpty(stockKLines)) {
                continue;
            }
            for (int k = 0; k < stockKLines.size(); k++) {
                if (k == 0) {
                    continue;
                }
                StockKLine preStockKLine = stockKLines.get(k - 1);
                StockKLine stockKLine = stockKLines.get(k);
                BigDecimal yesterdayClosePrice = preStockKLine.getClosePrice();
                BigDecimal closePrice = stockKLine.getClosePrice();
                BigDecimal maxPrice = stockKLine.getHighPrice();
                BigDecimal minPrice = stockKLine.getLowPrice();
                // 获取当日涨幅
                BigDecimal increase = IncreaseUtil.getIncrease(yesterdayClosePrice, closePrice);
                stockKLine.setIncrease(increase);
                // 获取当日振幅
                BigDecimal amplitude = IncreaseUtil.getAmplitude(yesterdayClosePrice, maxPrice, minPrice);
                stockKLine.setAmplitude(amplitude);
                // 设置批次号
                stockKLine.setBatchNo(batchNo);
                stockKLine.setUpdateTime(new Date());
            }
            // 批量更新K线数据
            updateBatchById(stockKLines);
        }
    }

    @Override
    public void updateKLineIncrease(String startDate) throws Exception {
        List<Stock> stocks = stockMapper.selectList(null);
        updateKLineIncrease(stocks, startDate);
    }

    @Override
    public List<StockKLine> getStockKLineListByStockCode(String stockCode, String startDate) throws Exception {
        LambdaQueryWrapper<StockKLine> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StockKLine::getStockCode, stockCode);
        if (StringUtils.isNotBlank(startDate)) {
            lambdaQueryWrapper.ge(StockKLine::getLineDate, startDate);
        }
        lambdaQueryWrapper.orderByAsc(StockKLine::getLineDate);
        List<StockKLine> stockKLines = stockKLineMapper.selectList(lambdaQueryWrapper);
        return stockKLines;
    }

    @PostConstruct
    public void initSyncRecentKLineDataRedisKey() {
        redisTemplate.delete(RedisKey.SYNC_RECENT_K_LINE_DATA_LOCK);
    }

    @Async
    @Override
    public void asyncRecentKLineData() throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("更新最近的K线涨幅数据开始");
        syncRecentKLineData();
        log.info("更新最近的K线涨幅数据结束");
        long endTime = System.currentTimeMillis();
        long diffTime = (endTime - startTime) / 1000;
        log.info("耗时：{}秒", diffTime);
    }

    @Override
    public void syncRecentKLineData() throws Exception {
        String startDate = null;
        try {
            if (redisTemplate.hasKey(RedisKey.SYNC_RECENT_K_LINE_DATA_LOCK)) {
                throw new BusinessException("同步最近K线数据任务正在执行，请勿重复操作");
            }
            String now = DateUtil.now();
            redisTemplate.opsForValue().set(RedisKey.SYNC_RECENT_K_LINE_DATA_LOCK, now, 50, TimeUnit.MINUTES);
            do {
                try {
                    StockKLineDateDiffVo diffVo = getStockKLineDateDiffVo();
                    if (diffVo == null) {
                        throw new BusinessException("diffVo为空");
                    }
                    Integer diffDay = diffVo.getDiffDay();
                    if (diffDay == 0) {
                        log.info("目前已是最新数据，无需更新");
                        return;
                    }
                    Date beforeEndDate = diffVo.getLineDate();
                    startDate = DateUtil.formatYYYYMMDD(beforeEndDate);
                    List<Stock> stocks = stockMapper.getRecentDiffData(beforeEndDate);
                    if (CollectionUtils.isEmpty(stocks)) {
                        return;
                    }
                    boolean isUpdate = true;
                    // 同步K线数据
                    syncKLineData(stocks, diffDay, beforeEndDate, isUpdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 如果同步时间超过50分钟，跳出循环
                if (!redisTemplate.hasKey(RedisKey.SYNC_RECENT_K_LINE_DATA_LOCK)) {
                    log.error("同步K线数据超时，跳出循环");
                    break;
                }
            } while (true);
            // 同步涨幅类型
            updateKLineIncrease(startDate);
            // 同步每日涨幅
            updateKLineDayIncrease();
            // 同步均线
            updateKLineMA();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisTemplate.delete(RedisKey.SYNC_RECENT_K_LINE_DATA_LOCK);
        }
    }

    @Override
    public void updateRecentKLineIncrease() throws Exception {
        String startDate = stockKLineMapper.getUpdateKLineIncreaseStartDate();
        updateKLineIncrease(startDate);
    }

    @Override
    public void updateKLineDayIncreaseByStocks(List<Stock> stocks) throws Exception {
        if (CollectionUtils.isEmpty(stocks)) {
            log.error("股票数据为空");
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            String stockCode = stock.getStockCode();
            log.info("K线：" + (i + 1) + "，" + stockCode + "," + stock.getStockName());
            try {
                updateKLineDayIncreaseByStockCode(batchNo, stockCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateKLineDayIncrease() throws Exception {
        List<Stock> stocks = stockMapper.selectList(null);
        updateKLineDayIncreaseByStocks(stocks);
    }

    @Override
    public void updateTodayKLineDayIncrease(String lineDate) throws Exception {
        List<Stock> stocks = stockMapper.selectList(null);
        if (CollectionUtils.isEmpty(stocks)) {
            log.error("股票数据为空");
            return;
        }
        List<String> stockCodes = new ArrayList<>();
        stocks.forEach(item -> {
            stockCodes.add(item.getStockCode());
        });
        List<List<String>> lists = ListUtils.partition(stockCodes, 200);
        int listSize = lists.size();
        CountDownLatch countDownLatch = new CountDownLatch(listSize);
        ExecutorService executorService = Executors.newFixedThreadPool(listSize);
        String batchNo = BatchNoUtil.getBatchNo();
        for (int i = 0; i < listSize; i++) {
            List<String> list = lists.get(i);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < list.size(); i++) {
                            String stockCode = list.get(i);
                            log.info("K线：" + (i + 1) + "，" + stockCode);
                            try {
                                updateTodayKLineDayIncreaseByStockCode(batchNo, stockCode, lineDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        try {
            countDownLatch.await();
            log.info("多线程计算每日涨幅数据完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void calcKLineDayIncrease(List<StockKLine> stockKLines) throws Exception {
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }
        for (int i = 0; i < stockKLines.size(); i++) {
            if (i == 0) {
                continue;
            }
            StockKLine currentStockKLine = stockKLines.get(i);
            BigDecimal currentClosePrice = currentStockKLine.getClosePrice();
            // 5日涨幅:（当前收盘价 - 6天前的收盘价 ）/ 6天前的收盘价
            // 5日均线：(最近5天的收盘价之和) / 5
            if (i >= 5) {
                int beforeIndex = i - 5;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI5(increase);
            }
            // 10日涨幅
            if (i >= 10) {
                int beforeIndex = i - 10;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI10(increase);
            }
            // 20日涨幅
            if (i >= 20) {
                int beforeIndex = i - 20;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI20(increase);
            }
            // 30日涨幅
            if (i >= 30) {
                int beforeIndex = i - 30;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI30(increase);
            }
            // 60日涨幅
            if (i >= 60) {
                int beforeIndex = i - 60;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI60(increase);
            }
            // 90日涨幅
            if (i >= 90) {
                int beforeIndex = i - 90;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI90(increase);
            }
            // 120日涨幅
            if (i >= 120) {
                int beforeIndex = i - 120;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI120(increase);
            }
            // 250日涨幅
            if (i >= 250) {
                int beforeIndex = i - 250;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI250(increase);
            }
            // 300日涨幅
            if (i >= 300) {
                int beforeIndex = i - 300;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI300(increase);
            }


        }


        String batchNo = BatchNoUtil.getBatchNo();
        for (int i = 0; i < stockKLines.size(); i++) {
            StockKLine stockKLine = stockKLines.get(i);
            String stockCode = stockKLine.getStockCode();
            String stockName = stockKLine.getStockName();
            log.info("K线：" + (i + 1) + "，" + stockCode + "," + stockName);
            try {
                updateKLineDayIncreaseByStockCode(batchNo, stockCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void updateKLineDayIncrease(List<StockKLine> stockKLines) throws Exception {
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        for (int i = 0; i < stockKLines.size(); i++) {
            StockKLine stockKLine = stockKLines.get(i);
            String stockCode = stockKLine.getStockCode();
            String stockName = stockKLine.getStockName();
            log.info("K线：" + (i + 1) + "，" + stockCode + "," + stockName);
            try {
                updateKLineDayIncreaseByStockCode(batchNo, stockCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateKLineDayIncreaseByStockCode(String batchNo, String stockCode) throws Exception {
        List<StockKLine> stockKLines = getStockKLineListByStockCode(stockCode, null);
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }
        List<StockKLine> updateStockKLines = new ArrayList<>();
        for (int k = 0; k < stockKLines.size(); k++) {
            if (k == 0) {
                continue;
            }
            StockKLine currentStockKLine = stockKLines.get(k);
            BigDecimal currentClosePrice = currentStockKLine.getClosePrice();
            boolean isUpdate = false;
            // 5日涨幅:（当前收盘价 - 6天前的收盘价 ）/ 6天前的收盘价
            // 5日均线：(最近5天的收盘价之和) / 5
            if (k >= 5) {
                isUpdate = true;
                int beforeIndex = k - 5;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI5(increase);
            }
            // 10日涨幅
            if (k >= 10) {
                isUpdate = true;
                int beforeIndex = k - 10;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI10(increase);
            }
            // 20日涨幅
            if (k >= 20) {
                isUpdate = true;
                int beforeIndex = k - 20;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI20(increase);
            }
            // 30日涨幅
            if (k >= 30) {
                isUpdate = true;
                int beforeIndex = k - 30;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI30(increase);
            }
            // 60日涨幅
            if (k >= 60) {
                isUpdate = true;
                int beforeIndex = k - 60;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI60(increase);
            }
            // 90日涨幅
            if (k >= 90) {
                isUpdate = true;
                int beforeIndex = k - 90;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI90(increase);
            }
            // 120日涨幅
            if (k >= 120) {
                isUpdate = true;
                int beforeIndex = k - 120;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI120(increase);
            }
            // 250日涨幅
            if (k >= 250) {
                isUpdate = true;
                int beforeIndex = k - 250;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI250(increase);
            }
            // 300日涨幅
            if (k >= 300) {
                isUpdate = true;
                int beforeIndex = k - 300;
                BigDecimal increase = getDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//                currentStockKLine.setI300(increase);
            }

            if (isUpdate) {
                // 设置批次号
                currentStockKLine.setBatchNo(batchNo);
                currentStockKLine.setUpdateTime(new Date());
                updateStockKLines.add(currentStockKLine);
            }


        }
        // 批量更新K线数据
        if (CollectionUtils.isNotEmpty(updateStockKLines)) {
            updateBatchById(updateStockKLines);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTodayKLineDayIncreaseByStockCode(String batchNo, String stockCode, String lineDate) throws Exception {
        List<StockKLine> stockKLines = stockKLineMapper.getTop301StockKLineListByStockCode(stockCode, lineDate);
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }
        int k = 0;
        StockKLine currentStockKLine = stockKLines.get(k);
        BigDecimal currentClosePrice = currentStockKLine.getClosePrice();
        int beforeIndex = 0;
        BigDecimal increase = null;
        // 5日涨幅:（当前收盘价 - 6天前的收盘价 ）/ 6天前的收盘价
        // 5日均线：(最近5天的收盘价之和) / 5
        beforeIndex = k + 5;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//        currentStockKLine.setI5(increase);
        // 10日涨幅
        beforeIndex = k + 10;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//        currentStockKLine.setI10(increase);
        // 20日涨幅
        beforeIndex = k + 20;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//        currentStockKLine.setI20(increase);
        // 30日涨幅
        beforeIndex = k + 30;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//        currentStockKLine.setI30(increase);
        // 60日涨幅
        beforeIndex = k + 60;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//        currentStockKLine.setI60(increase);
        // 90日涨幅
        beforeIndex = k + 90;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//        currentStockKLine.setI90(increase);
        // 120日涨幅
        beforeIndex = k + 120;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//        currentStockKLine.setI120(increase);
        // 250日涨幅
        beforeIndex = k + 250;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//        currentStockKLine.setI250(increase);
        // 300日涨幅
        beforeIndex = k + 300;
        increase = getTodayDayIncreaseValue(stockKLines, currentClosePrice, beforeIndex);
//        currentStockKLine.setI300(increase);
        // 设置批次号
        currentStockKLine.setBatchNo(batchNo);
        currentStockKLine.setUpdateTime(new Date());
        // 更新K线数据
        updateById(currentStockKLine);

    }

    @Override
    public void updateKLineMAByStocks(List<Stock> stocks) throws Exception {
        if (CollectionUtils.isEmpty(stocks)) {
            log.error("股票数据为空");
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        List<String> stockCodes = new ArrayList<>();
        stocks.forEach(item -> {
            stockCodes.add(item.getStockCode());
        });
        List<List<String>> lists = ListUtils.partition(stockCodes, 200);
        int listSize = lists.size();
        CountDownLatch countDownLatch = new CountDownLatch(listSize);
        ExecutorService executorService = Executors.newFixedThreadPool(listSize);
        for (int i = 0; i < listSize; i++) {
            List<String> list = lists.get(i);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < list.size(); i++) {
                            String stockCode = list.get(i);
                            log.info("K线：" + (i + 1) + "，" + stockCode);
                            try {
                                updateKLineMAByStockCode(batchNo, stockCode);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        try {
            countDownLatch.await();
            log.info("多线程计算每日均线数据完成");
            // 更新
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getDayIncreaseValue(List<StockKLine> stockKLines, BigDecimal currentClosePrice,
                                          int beforeIndex) {
        StockKLine beforeStockKLine = stockKLines.get(beforeIndex);
        BigDecimal beforeClosePrice = beforeStockKLine.getClosePrice();
        return IncreaseUtil.getIncrease(beforeClosePrice, currentClosePrice);
    }

    public BigDecimal getTodayDayIncreaseValue(List<StockKLine> stockKLines, BigDecimal currentClosePrice,
                                               int beforeIndex) {
        int listSize = stockKLines.size();
        if (beforeIndex > (listSize - 1)) {
            return null;
        }
        StockKLine beforeStockKLine = stockKLines.get(beforeIndex);
        BigDecimal beforeClosePrice = beforeStockKLine.getClosePrice();
        return IncreaseUtil.getIncrease(beforeClosePrice, currentClosePrice);
    }


    @Override
    public void updateKLineMA() throws Exception {
        List<Stock> stocks = stockMapper.selectList(null);
        updateKLineMAByStocks(stocks);
    }

    @Override
    public void updateKLineMA(List<StockKLine> stockKLines) throws Exception {
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        for (int i = 0; i < stockKLines.size(); i++) {
            StockKLine stock = stockKLines.get(i);
            String stockCode = stock.getStockCode();
            String stockName = stock.getStockName();
            log.info("K线：" + (i + 1) + "，" + stockCode + "," + stockName);
            try {
                updateKLineMAByStockCode(batchNo, stockCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateKLineMAByStockCode(String batchNo, String stockCode) throws Exception {
        List<StockKLine> stockKLines = getStockKLineListByStockCode(stockCode, null);
        if (CollectionUtils.isEmpty(stockKLines)) {
            return;
        }
        List<StockKLine> updateStockKLines = new ArrayList<>();
        for (int k = 0; k < stockKLines.size(); k++) {
            if (k == 0) {
                continue;
            }
            StockKLine currentStockKLine = stockKLines.get(k);
            BigDecimal currentClosePrice = currentStockKLine.getClosePrice();
            boolean isUpdate = false;
            // 5日均线：(最近5天的收盘价之和) / 5
            if (k >= 4) {
                isUpdate = true;
                int beforeIndex = k - 4;
                BigDecimal maValue = getMAValue(stockKLines, k, beforeIndex);
//                currentStockKLine.setMa5(maValue);
            }
            // 10日均线
            if (k >= 9) {
                isUpdate = true;
                int beforeIndex = k - 9;
                BigDecimal maValue = getMAValue(stockKLines, k, beforeIndex);
//                currentStockKLine.setMa10(maValue);
            }
            // 20日均线
            if (k >= 19) {
                isUpdate = true;
                int beforeIndex = k - 19;
                BigDecimal maValue = getMAValue(stockKLines, k, beforeIndex);
//                currentStockKLine.setMa20(maValue);
            }
            // 30日均线
            if (k >= 29) {
                isUpdate = true;
                int beforeIndex = k - 29;
                BigDecimal maValue = getMAValue(stockKLines, k, beforeIndex);
//                currentStockKLine.setMa30(maValue);
            }
            // 60日均线
            if (k >= 59) {
                isUpdate = true;
                int beforeIndex = k - 59;
                BigDecimal maValue = getMAValue(stockKLines, k, beforeIndex);
//                currentStockKLine.setMa60(maValue);
            }
            // 90日均线
            if (k >= 89) {
                isUpdate = true;
                int beforeIndex = k - 89;
                BigDecimal maValue = getMAValue(stockKLines, k, beforeIndex);
//                currentStockKLine.setMa90(maValue);
            }
            // 120日均线
            if (k >= 119) {
                isUpdate = true;
                int beforeIndex = k - 119;
                BigDecimal maValue = getMAValue(stockKLines, k, beforeIndex);
//                currentStockKLine.setMa120(maValue);
            }
            // 250日均线
            if (k >= 249) {
                isUpdate = true;
                int beforeIndex = k - 249;
                BigDecimal maValue = getMAValue(stockKLines, k, beforeIndex);
//                currentStockKLine.setMa250(maValue);
            }
            // 300日均线
            if (k >= 299) {
                isUpdate = true;
                int beforeIndex = k - 299;
                BigDecimal maValue = getMAValue(stockKLines, k, beforeIndex);
//                currentStockKLine.setMa300(maValue);
            }

            if (isUpdate) {
                // 设置批次号
                currentStockKLine.setBatchNo(batchNo);
                currentStockKLine.setUpdateTime(new Date());
                updateStockKLines.add(currentStockKLine);
            }


        }
        // 批量更新K线数据
        if (CollectionUtils.isNotEmpty(updateStockKLines)) {
            updateBatchById(updateStockKLines);
        }
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRealKLineData(List<StockRealData> stockRealDatas) throws Exception {
        if (CollectionUtils.isEmpty(stockRealDatas)) {
            return;
        }
        Date realDate = stockRealDatas.get(0).getRealDate();
        String lineDate = DateUtil.formatYYYYMMDD(realDate);
        // 删除最后一个交易日的数据
        String lastTradeDate = stockMapper.getLastTradeDate(true);
        stockKLineMapper.deleteStockKLineByLineDate(lastTradeDate);
        List<StockKLine> stockKLines = new ArrayList<>();
        for (StockRealData stockRealData : stockRealDatas) {
            StockKLine stockKLine = new StockKLine();
            updateStockKLineField(stockKLine, stockRealData, lineDate);
            String stockCode = stockRealData.getStockCode();
            stockKLines.add(stockKLine);
        }
        saveBatch(stockKLines);
    }

    @Override
    public void syncTodayKLineData() {
        try {
            List<Stock> stocks = stockMapper.selectList(null);
            if (CollectionUtils.isEmpty(stocks)) {
                return;
            }
            Integer diffDay = 1;
            boolean isUpdate = true;
            syncKLineData(stocks, diffDay, null, isUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StockKLineDateDiffVo getStockKLineDateDiffVo() throws Exception {
        StockKLineDateDiffVo diffVo = stockKLineMapper.getStockKLineDateDiffVo();
        if (diffVo == null) {
            throw new BusinessException("diffVo为空");
        }
        Integer diffDay = diffVo.getDiffDay();
        if (diffDay == 0) {
            return diffVo;
        }
        Date lineDate = diffVo.getLineDate();
        // 通过天数获取上证指数的最近数据的第一个日期
        // diffDay = latestDate - startDate + 1
        // lineDate = startDate
        List<StockKLine> stockKLines = TencentStockApi.getShanghaiStockIndex(diffDay);
        if (CollectionUtils.isEmpty(stockKLines)) {
            log.error("上证指数数据为空");
            return null;
        }
        String maxLineDateString = stockKLineMapper.getMaxLineDate();
        Date maxLineDate = DateUtil.parseYYYYMMDD(maxLineDateString);
        Long maxLineDateTime = maxLineDate.getTime();
        String startDateString = null;
        Date startDate = null;
        for (StockKLine stockKLine : stockKLines) {
            String itemLineDateString = stockKLine.getLineDate();
            Date itemLineDate = DateUtil.parseYYYYMMDD(itemLineDateString);
            Long itemLineDateTime = itemLineDate.getTime();
            if (itemLineDateTime > maxLineDateTime) {
                startDateString = itemLineDateString;
                startDate = itemLineDate;
                break;
            }
        }
        log.info("startDate: " + startDateString);
        Date latestDate = diffVo.getLatestDate();
        String latestDateString = DateUtil.formatYYYYMMDD(latestDate);
        Integer newDiffDay = DateUtil.diffDay(startDateString, latestDateString);
        newDiffDay = newDiffDay + 1;
        diffVo.setDiffDay(newDiffDay);
        diffVo.setLineDate(startDate);
        return diffVo;
    }

    @Override
    public void syncTimeoutKLineData() throws Exception {
        try {
            List<Stock> stocks = syncKLineErrorDataMapper.getSyncErrorStockList();
            if (CollectionUtils.isEmpty(stocks)) {
                return;
            }
            boolean isUpdate = false;
            syncKLineData(stocks, 500, null, isUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProgressVo syncRecentKLineDataProgress() throws Exception {
        ProgressVo progressVo = (ProgressVo) redisTemplate.opsForValue().get(RedisKey.SYNC_K_LINE_DATA_PROGRESS);
        return progressVo;
    }

    @Override
    public void updateRealKLineData() throws Exception {
        // 删除最后一个交易日的数据
        String lastTradeDate = stockMapper.getLastTradeDate(true);
        stockKLineMapper.deleteStockKLineByLineDate(lastTradeDate);
        List<Stock> stocks = stockMapper.selectList(null);
        List<StockKLine> stockKLines = new ArrayList<>();
        for (Stock stock : stocks) {
            StockKLine stockKLine = new StockKLine();
            updateStockKLineField(stockKLine, stock);
            stockKLines.add(stockKLine);
        }
        saveBatch(stockKLines);
    }

    @Override
    public void updateStockKLineRealData(List<StockRealData> stockRealDataRecords) throws Exception {
        if (CollectionUtils.isEmpty(stockRealDataRecords)) {
            return;
        }
        StockRealData firstStockRealData = stockRealDataRecords.get(0);
        String realDate = DateUtil.formatYYYYMMDD(firstStockRealData.getRealDate());
        List<String> stockCodes = new ArrayList<>();
        stockRealDataRecords.forEach(stockRealData -> {
            stockCodes.add(stockRealData.getStockCode());
        });
        // 更新K线实时数据
        List<StockKLine> dbStockkLines = stockKLineMapper.getStockKLineListByStockCodesAndDate(stockCodes, realDate);
        Map<String, StockKLine> dbStockKLineMap = dbStockkLines.stream().collect(Collectors.toMap(StockKLine::getStockCode, StockKLine -> StockKLine));
        List<String> dbStockKLineStockCodes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dbStockkLines)) {
            for (StockKLine dbStockkLine : dbStockkLines) {
                dbStockKLineStockCodes.add(dbStockkLine.getStockCode());
            }
        }
        List<StockKLine> addStockKLines = new ArrayList<>();
        List<StockKLine> updateStockKLines = new ArrayList<>();
        for (StockRealData stockRealDataRecord : stockRealDataRecords) {
            String stockCode = stockRealDataRecord.getStockCode();
            StockKLine stockKLine = new StockKLine();
            if (dbStockKLineStockCodes.contains(stockCode)) {
                stockKLine = dbStockKLineMap.get(stockCode);
                updateStockKLines.add(stockKLine);
            } else {
                stockKLine = new StockKLine();
                addStockKLines.add(stockKLine);
            }
            updateStockKLineField(stockKLine, stockRealDataRecord);
        }
        if (CollectionUtils.isNotEmpty(updateStockKLines)) {
            updateBatchById(updateStockKLines);
        }
        if (CollectionUtils.isNotEmpty(addStockKLines)) {
            saveBatch(addStockKLines);
        }
    }

    private void updateStockKLineField(StockKLine stockKLine, StockRealData stockRealDataRecord) {
        if (stockKLine == null) {
            return;
        }
        if (stockRealDataRecord == null) {
            return;
        }
        String lineDate = DateUtil.formatYYYYMMDD(stockRealDataRecord.getRealDate());
        stockKLine.setLineDate(lineDate);
        stockKLine.setStockCode(stockRealDataRecord.getStockCode());
        stockKLine.setStockName(stockRealDataRecord.getStockName());
        stockKLine.setOpenPrice(stockRealDataRecord.getOpeningPrice());
        stockKLine.setClosePrice(stockRealDataRecord.getCurrentPrice());
        stockKLine.setHighPrice(stockRealDataRecord.getHighPrice());
        stockKLine.setLowPrice(stockRealDataRecord.getLowPrice());
        stockKLine.setTradeAmount(stockRealDataRecord.getTradeAmount());
        stockKLine.setTradeNumber(stockRealDataRecord.getTradeNumber());
        stockKLine.setIncrease(stockRealDataRecord.getCurrentIncrease());
        BigDecimal amplitude = IncreaseUtil.getAmplitude(stockKLine.getClosePrice(), stockKLine.getHighPrice(), stockKLine.getLowPrice());
        stockKLine.setAmplitude(amplitude);
        stockKLine.setBatchNo(stockRealDataRecord.getBatchNo());
        stockKLine.setUpdateTime(new Date());
    }

    private void updateStockKLineField(StockKLine stockKLine, Stock stock) {
        String lineDate = DateUtil.formatYYYYMMDD(stock.getRealDate());
        stockKLine.setLineDate(lineDate);
        stockKLine.setStockCode(stock.getStockCode());
        stockKLine.setStockName(stock.getStockName());
        stockKLine.setOpenPrice(stock.getOpenPrice());
        stockKLine.setClosePrice(stock.getPrice());
        stockKLine.setLowPrice(stock.getLowPrice());
        stockKLine.setHighPrice(stock.getHighPrice());
        stockKLine.setTradeNumber(stock.getTradeNumber());
        stockKLine.setTradeAmount(stock.getTradeAmount());
        stockKLine.setAmplitude(stock.getAmplitude());
        stockKLine.setIncrease(stock.getIncrease());
        stockKLine.setBatchNo(stock.getBatchNo());
    }

    private void updateStockKLineField(StockKLine dbStockKLine, StockRealData stockRealData, String lineDate) {
        if (dbStockKLine == null) {
            return;
        }
        if (stockRealData == null) {
            return;
        }
        // 日期
        dbStockKLine.setLineDate(lineDate);
        // 股票代码
        dbStockKLine.setStockCode(stockRealData.getStockCode());
        // 股票名称
        dbStockKLine.setStockName(stockRealData.getStockName());
        // TODO 换手率 新浪资金流入流出接口获取换手率
        dbStockKLine.setTurnoverRate(null);
        // 振幅
        BigDecimal closePrice = stockRealData.getYesterdayClosingPrice();
        BigDecimal maxPrice = stockRealData.getHighPrice();
        BigDecimal minPrice = stockRealData.getLowPrice();
        dbStockKLine.setAmplitude(IncreaseUtil.getAmplitude(closePrice, maxPrice, minPrice));
        // 最新价格
        dbStockKLine.setClosePrice(stockRealData.getCurrentPrice());
        // 最新涨幅
        BigDecimal increase = stockRealData.getCurrentIncrease();
        dbStockKLine.setIncrease(increase);
        // 今日开盘价
        dbStockKLine.setOpenPrice(stockRealData.getOpeningPrice());
        // 今日最高价
        dbStockKLine.setHighPrice(stockRealData.getHighPrice());
        // 今日最低价
        dbStockKLine.setLowPrice(stockRealData.getLowPrice());
        // 交易金额
        dbStockKLine.setTradeAmount(stockRealData.getTradeAmount());
        // 交易量
        dbStockKLine.setTradeNumber(stockRealData.getTradeNumber());
        // 批次号
        dbStockKLine.setBatchNo(stockRealData.getBatchNo());
        // 修改时间
        dbStockKLine.setUpdateTime(new Date());
    }

    public BigDecimal getMAValue(List<StockKLine> stockKLines, int k, int beforeIndex) {
        List<StockKLine> subList = stockKLines.subList(beforeIndex, k + 1);
        BigDecimal sum = subList.stream().map(StockKLine::getClosePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        int num = subList.size();
        BigDecimal maValue = BigDecimalUtil.divide(sum, num, 2);
        return maValue;
    }

}
