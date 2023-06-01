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

package io.geekidea.stock.task;

import io.geekidea.framework.util.DateUtil;
import io.geekidea.stock.service.StockRealDataService;
import io.geekidea.stock.util.TaskExecuteTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-09-13
 **/
@Slf4j
@Component
public class SinaStockRealDataTask {

    @Autowired
    private StockRealDataService stockRealDataService;

    /**
     * 3分钟同步一次实时数据
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0/3 9,10,11,13,14,15 ? * MON-FRI")
    public void autoSyncRealData() throws Exception {
        if (TaskExecuteTimeUtil.isNotExecute()) {
            return;
        }
        log.info("定时刷新股票实时数据开始");
        stockRealDataService.syncStockRealData(false);
        log.info("定时刷新股票实时数据结束");
    }

    /**
     * 12点过5分，15点过5分，同步所有市场的数据
     *
     * @throws Exception
     */
//    @Scheduled(cron = "0 5 12,15 ? * MON-FRI")
    public void autoSyncMarketRealData() throws Exception {
        if (TaskExecuteTimeUtil.isNotExecute()) {
            return;
        }
        log.info("定时刷新所有股票实时数据开始");
        stockRealDataService.syncMarketStockRealData();
        log.info("定时刷新所有股票实时数据结束");
    }

    /**
     * 每隔5分钟，同步板块实时数据
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0/5 9,10,11,13,14,15 ? * MON-FRI")
    public void autoSyncBkRealData() throws Exception {
        if (TaskExecuteTimeUtil.isNotExecute()) {
            return;
        }
        log.info("定时刷新板块实时数据开始");
        stockRealDataService.syncBkStockRealData();
        log.info("定时板块股票实时数据结束");
    }

    public void getRealData() throws Exception {
        log.info("刷新股票实时数据开始");
        stockRealDataService.syncStockRealData(true);
        log.info("刷新股票实时数据结束");
    }


}
