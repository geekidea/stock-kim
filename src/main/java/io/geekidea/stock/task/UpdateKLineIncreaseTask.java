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
import io.geekidea.stock.util.TaskExecuteTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-10
 **/
@Slf4j
@Component
public class UpdateKLineIncreaseTask {

    @Autowired
    private StockKLineService stockKLineService;

    public void updateKLineIncrease() throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("更新K线涨幅数据开始");
        stockKLineService.updateKLineIncrease(null);
        log.info("更新K线涨幅数据结束");
        long endTime = System.currentTimeMillis();
        long diffTime = (endTime - startTime) / 1000;
        log.info("耗时：{}秒", diffTime);

    }


    public void updateRecentKLineIncrease() throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("更新最近的K线涨幅数据开始");
        stockKLineService.updateRecentKLineIncrease();
        log.info("更新最近的K线涨幅数据结束");
        long endTime = System.currentTimeMillis();
        long diffTime = (endTime - startTime) / 1000;
        log.info("耗时：{}秒", diffTime);
    }

    public void updateKLineDayIncrease() throws Exception {
        if (TaskExecuteTimeUtil.marketCloseExecute()) {
            long startTime = System.currentTimeMillis();
            log.info("更新K线N日涨幅数据开始");
            stockKLineService.updateKLineDayIncrease();
            log.info("更新K线N日涨幅数据结束");
            long endTime = System.currentTimeMillis();
            long diffTime = (endTime - startTime) / 1000;
            log.info("耗时：{}秒", diffTime);
        }
    }

    public void updateKLineMA() throws Exception {
//        if (TaskExecuteTimeUtil.marketCloseExecute()) {
            long startTime = System.currentTimeMillis();
            log.info("更新K线N日均线数据开始");
            stockKLineService.updateKLineMA();
            log.info("更新K线N日均线数据结束");
            long endTime = System.currentTimeMillis();
            long diffTime = (endTime - startTime) / 1000;
            log.info("耗时：{}秒", diffTime);
//        }
    }

    public void updateLineDateKLineDayIncrease() throws Exception {
        long start = System.currentTimeMillis();
        String lineDate = "2021-11-10";
        stockKLineService.updateTodayKLineDayIncrease(lineDate);
        long end = System.currentTimeMillis();
        System.out.println("计算K线每日涨幅耗时 = " + (end - start) / 1000);
    }
}
