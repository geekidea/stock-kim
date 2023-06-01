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

import io.geekidea.framework.util.PinYinUtil;
import io.geekidea.stock.entity.BkInfo;
import io.geekidea.stock.entity.Stock;
import io.geekidea.stock.entity.StockConcept;
import io.geekidea.stock.service.BkInfoService;
import io.geekidea.stock.service.StockConceptService;
import io.geekidea.stock.service.StockService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/12/13
 **/
public class FixPinYinTest extends BaseTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private BkInfoService bkInfoService;

    @Autowired
    private StockConceptService stockConceptService;

    @Test
    public void fixStockPinYin() throws Exception {
        List<Stock> stocks = stockService.list();
        if (CollectionUtils.isEmpty(stocks)) {
            return;
        }
        for (Stock stock : stocks) {
            String stockName = stock.getStockName();
            if (StringUtils.isNotBlank(stockName)) {
                String pinYin = PinYinUtil.getFirstLetterPinYin(stockName);
                stock.setStockLetter(pinYin);
            }
        }

        stockService.updateBatchById(stocks, 200);
    }


    @Test
    public void fixBkInfoPinYin() throws Exception {
        List<BkInfo> bkInfos = bkInfoService.list();
        if (CollectionUtils.isEmpty(bkInfos)) {
            return;
        }
        for (BkInfo bkInfo : bkInfos) {
            String bkName = bkInfo.getBkName();
            if (StringUtils.isNotBlank(bkName)) {
                String pinYin = PinYinUtil.getFirstLetterPinYin(bkName);
                bkInfo.setBkLetter(pinYin);
            }
        }

        bkInfoService.updateBatchById(bkInfos, 200);
    }

    @Test
    public void fixStockConceptPinYin() throws Exception {
        List<StockConcept> stockConcepts = stockConceptService.list();
        if (CollectionUtils.isEmpty(stockConcepts)) {
            return;
        }
        for (StockConcept stockConcept : stockConcepts) {
            String conceptName = stockConcept.getConceptName();
            if (StringUtils.isNotBlank(conceptName)) {
                String pinYin = PinYinUtil.getFirstLetterPinYin(conceptName);
                stockConcept.setConceptLetter(pinYin);
            }
        }

        stockConceptService.updateBatchById(stockConcepts, 200);
    }


}
