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

package io.geekidea.test;

import io.geekidea.stock.task.MarketScoreLineTask;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-11
 **/
public class MarketScoreLineTaskTest extends BaseTest {

    @Autowired
    private MarketScoreLineTask marketScoreLineTask;

    @Test
    public void calcMarketScoreTest() throws Exception {
        marketScoreLineTask.calcMarketScore();
    }

    @Test
    public void calcRecentMarketScoreTest() throws Exception {
        marketScoreLineTask.calcRecentMarketScore();
    }

    @Test
    public void calcTodayMarketScoreTest() throws Exception {
        marketScoreLineTask.calcTodayMarketScore();
    }

}
