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

import io.geekidea.stock.service.CustomKLineService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-24
 **/
public class CustomKLineTest extends BaseTest {

    @Autowired
    private CustomKLineService customKLineService;

    @Test
    public void getRangeFallTest() throws Exception {
        String startDate = "2021-08-05";
        int limit = 500;
        customKLineService.getRangeFall(startDate, limit);
    }

    /**
     * 获取指定范围的涨幅前500
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        String startDate = "2021-01-01";
        int limit = 500;
        customKLineService.getRangeRise(startDate, limit);
    }
}
