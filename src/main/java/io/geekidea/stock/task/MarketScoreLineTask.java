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

import io.geekidea.stock.service.MarketScoreLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-11
 **/
@Slf4j
@Component
public class MarketScoreLineTask {

    @Autowired
    private MarketScoreLineService marketScoreLineService;

    public void calcMarketScore() throws Exception {
        marketScoreLineService.calcMarketScore();
    }

    public void calcRecentMarketScore() throws Exception {
        marketScoreLineService.calcRecentMarketScore();
    }

    public void calcTodayMarketScore() throws Exception {
        marketScoreLineService.calcTodayMarketScore();
    }
}
