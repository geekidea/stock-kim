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

import io.geekidea.stock.service.BkIndustryConceptService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/14
 **/
public class BkInfoTest extends BaseTest {

    @Autowired
    private BkIndustryConceptService bkIndustryConceptService;

    @Test
    public void generateIndustryBkTest() throws Exception {
        bkIndustryConceptService.generateIndustryBkInfo(true);
    }

    @Test
    public void generateConceptBkTest() throws Exception {
        bkIndustryConceptService.generateConceptBkInfo(true);
    }




}
