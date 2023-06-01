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

import io.geekidea.stock.service.StockKLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-6
 **/
@Slf4j
@Component
public class TencentStockKLineTask {

    @Autowired
    private StockKLineService stockKLineService;


    //    @Scheduled(cron = "0 0/5 9,10,11,13,14 * * ?")
    @Scheduled(fixedRate = 3 * 60 * 1000)
    public void autoSyncKLineData() throws Exception {
        if (isExecute()) {
            log.info("定时刷新股票实时数据开始");
            stockKLineService.syncKLineData();
            log.info("定时刷新股票实时数据结束");
        }
    }

    public void syncKLineData() throws Exception {
        log.info("刷新股票实时数据开始");
        stockKLineService.syncKLineData();
        log.info("刷新股票实时数据结束");
    }

    private boolean isExecute() {
        LocalTime localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        String currentTimeString = hour + "" + minute;
        int currentTime = Integer.parseInt(currentTimeString);
        if ((currentTime >= 930 && currentTime <= 1130) || (currentTime >= 1300 && currentTime <= 1500)) {
            log.info("currentTime = " + currentTime);
            return true;
        }
        return false;
    }

    public void syncRecentKLineData() throws Exception {
        log.info("刷新最近的股票实时数据开始");
        stockKLineService.syncRecentKLineData();
        log.info("刷新最近的股票实时数据结束");
    }

    public void syncTodayKLineData()  throws Exception{
        log.info("刷新今天的股票实时数据开始");
        stockKLineService.syncTodayKLineData();
        log.info("刷新今天的股票实时数据结束");
    }

    public void syncTimeoutKLineData() throws Exception {
        log.info("刷新超时股票实时数据开始");
        stockKLineService.syncTimeoutKLineData();
        log.info("刷新超时股票实时数据结束");
    }
}
