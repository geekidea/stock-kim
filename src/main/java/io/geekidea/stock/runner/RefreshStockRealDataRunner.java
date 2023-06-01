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

package io.geekidea.stock.runner;

import io.geekidea.stock.service.StockRealDataService;
import io.geekidea.stock.util.TaskExecuteTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/23
 **/
@Slf4j
@Component
public class RefreshStockRealDataRunner implements ApplicationRunner {

    @Autowired
    private StockRealDataService stockRealDataService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (TaskExecuteTimeUtil.isNotExecute()) {
            return;
        }
        log.info("项目启动刷新股票实时数据开始");
        stockRealDataService.syncStockRealData(true);
        log.info("项目启动刷新股票实时数据结束");
    }

}
