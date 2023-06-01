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

import io.geekidea.stock.service.BkKLineService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 11/6/21
 **/
public class BkKLineTest extends BaseTest {

    @Autowired
    private BkKLineService bkKLineService;

    @Test
    public void syncBkKLine() throws Exception {
        String startDate = "2021-08-01";
        String bkCode = "BK002";
        bkKLineService.syncBkKLine(startDate, bkCode);
    }

    @Test
    public void syncAllBkKLineData() throws Exception {
        String startDate = "2020-08-01";
        bkKLineService.syncBkKLineData(startDate);
    }

    @Test
    public void syncCustomBkKLineData() throws Exception {
        String startDate = "2020-08-01";
        bkKLineService.syncCustomBkKLineData(startDate);
    }

}
