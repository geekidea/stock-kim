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

import io.geekidea.stock.api.SinaStockApi;
import io.geekidea.framework.thread.ThreadExecutor;
import io.geekidea.framework.thread.ThreadExecutorCallback;
import io.geekidea.framework.redis.RedisLock;
import io.geekidea.framework.util.BatchNoUtil;
import io.geekidea.framework.util.DateUtil;
import io.geekidea.framework.redis.RedisLockService;
import io.geekidea.stock.service.*;
import io.geekidea.stock.constant.RedisKey;
import io.geekidea.stock.entity.StockRealData;
import io.geekidea.stock.dto.query.StockRealDataRecordQuery;
import io.geekidea.stock.dto.vo.StockRealDataRecordQueryVo;
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.stock.util.OptionalYnUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


/**
 * <pre>
 * 股票实时数据记录 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-13
 */
@Slf4j
@Service
public class StockRealDataServiceImpl implements StockRealDataService {

    @Autowired
    private StockService stockService;

    @Autowired
    private RedisLockService redisLockService;

    @Autowired
    private IndustryIndexService industryIndexService;

    @Autowired
    private ConceptIndexService conceptIndexService;

    @Autowired
    private StockKLineService stockKLineService;

    @Autowired
    private StockRiseLineService stockRiseLineService;

    @Autowired
    private BkKLineService bkKLineService;

    @Autowired
    private BkInfoService bkInfoService;

    @RedisLock(key = RedisKey.SYNC_STOCK_REAL_DATA_LOCK, min = 10)
    @Override
    public void syncStockRealData(boolean isSyncBkInfo) throws Exception {
        try {
            log.info("刷新股票实时数据开始");
            // 同步批次号
            String batchNo = BatchNoUtil.getBatchNo();
            // 获取股票代码集合
            List<String> stockCodes = stockService.getStockCodeList();
            if (CollectionUtils.isEmpty(stockCodes)) {
                log.error("没有股票信息");
                return;
            }
            log.info("刷新的股票数量：" + stockCodes.size());
            log.info("批次号：{}", batchNo);
            // 多线程执行
            updateStockRealData(batchNo, stockCodes);
            if (isSyncBkInfo) {
                syncBkStockRealData(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("刷新股票实时数据结束");
        }


    }

    @Override
    public void syncMarketStockRealData() throws Exception {
        try {
            log.info("刷新股票实时数据开始");
            // 同步批次号
            String batchNo = BatchNoUtil.getBatchNo();
            // 获取股票代码集合
            boolean optionalYn = false;
            List<String> stockCodes = stockService.getNotOptionalStockCodeList();
            if (CollectionUtils.isEmpty(stockCodes)) {
                log.error("没有股票信息");
                return;
            }
            log.info("刷新的股票数量：" + stockCodes.size());
            log.info("批次号：{}", batchNo);
            // 多线程执行
            updateStockRealData(batchNo, stockCodes);
            // 更新每日涨幅和均线  10分钟更新一次
            syncBkStockRealData(false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("刷新股票实时数据结束");
        }
    }

    @Override
    public void syncBkStockRealData() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        syncBkStockRealData(optionalYn);
    }

    @Override
    public void syncBkStockRealData(boolean optionalYn) throws Exception {
        // 更新每日涨幅和均线  10分钟更新一次
        stockService.calcDayIncreaseMA(optionalYn);
        // 计算板块每日涨幅和均线  10分钟更新一次
        bkInfoService.calcBKDayIncreaseMA(optionalYn);
    }

    /**
     * 多线程更新数据
     *
     * @param batchNo
     * @param lists
     * @throws Exception
     */
    private void updateStockRealData(String batchNo, List<String> list) throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        // 更新最后一个交易日的板块K线
        String lastTradeDay = stockService.getLastTradeDate();
        ThreadExecutor.execute("同步股票实时数据", list, 100, new ThreadExecutorCallback<String>() {
            @Override
            public void execute(int index, List<String> subList) throws Exception {
                List<StockRealData> stockRealDatas = SinaStockApi.getRealData(batchNo, subList);
                // 更新股票实时数据
                stockService.updateStockRealData(stockRealDatas);
                // 更新K线实时数据
                stockKLineService.updateStockKLineRealData(stockRealDatas);
            }

            @Override
            public void finish() throws Exception {
                try {
                    log.info("多线程同步实时基础数据完成");
                    // 更新当日涨跌家数
                    stockRiseLineService.updateTodayRiseCount(optionalYn);
                    // 更新板块实时数据
                    bkInfoService.updateBkRealData(batchNo);
                    log.info("更新实时数据完成");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<List<String>> getSplitList(List<String> stockCodes, int num) {
        if (CollectionUtils.isEmpty(stockCodes)) {
            return null;
        }
        List<List<String>> lists = ListUtils.partition(stockCodes, num);
        return lists;
    }

}
