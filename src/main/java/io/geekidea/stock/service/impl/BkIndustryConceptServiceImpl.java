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

package io.geekidea.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.geekidea.framework.util.DecimalUtil;
import io.geekidea.framework.util.BatchNoUtil;
import io.geekidea.framework.util.MapUtil;
import io.geekidea.framework.util.PinYinUtil;
import io.geekidea.stock.entity.BkInfo;
import io.geekidea.stock.entity.BkStock;
import io.geekidea.stock.entity.Stock;
import io.geekidea.stock.entity.StockConcept;
import io.geekidea.stock.mapper.StockConceptMapper;
import io.geekidea.stock.mapper.StockMapper;
import io.geekidea.stock.service.BkIndustryConceptService;
import io.geekidea.stock.service.BkInfoService;
import io.geekidea.stock.service.BkStockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/14
 **/
@Slf4j
@Service
public class BkIndustryConceptServiceImpl implements BkIndustryConceptService {

    @Autowired
    private BkInfoService bkInfoService;

    @Autowired
    private BkStockService bkStockService;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockConceptMapper stockConceptMapper;

    private static final List<String> EXCLUDE_NUMBER_LIST = Arrays.asList("4", "38", "213", "250", "438", "748");

    private Integer index = 0;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generateIndustryBkInfo(boolean optionalYn, List<Stock> stocks) throws Exception {
        if (CollectionUtils.isEmpty(stocks)) {
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        Map<String, List<Stock>> map = stocks.stream().collect(Collectors.groupingBy(Stock::getIndustry, LinkedHashMap::new, Collectors.toList()));
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<Stock>> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue().size();
            countMap.put(key, value);
        }
        Map<String, Integer> sortCountMap = MapUtil.sortByValue(countMap, true);
        System.out.println("sortCountMap = " + sortCountMap);
        for (Map.Entry<String, Integer> entry : sortCountMap.entrySet()) {
            String industry = entry.getKey();
            List<Stock> list = map.get(industry);
            // 保存到bk_info
            BkInfo bkInfo = new BkInfo();
            bkInfo.setBatchNo(batchNo);
            Integer max = bkInfoService.getMaxNo(1);
            Integer no = getNo(max);
            String bkCode = "I" + DecimalUtil.formatThreeLength(no);
            bkInfo.setBkCode(bkCode);
            bkInfo.setBkName(industry);
            bkInfo.setBkType(1);
            bkInfo.setBkTypeName("行业");
            bkInfo.setSort(1000);
            String pinYin = PinYinUtil.getFirstLetterPinYin(industry);
            bkInfo.setBkLetter(pinYin);
            bkInfoService.save(bkInfo);
            List<BkStock> bkStocks = new ArrayList<>();
            for (Stock stock : list) {
                // 循环保存到bk_stock
                BkStock bkStock = new BkStock();
                bkStock.setBkCode(bkCode);
                bkStock.setIndustry(industry);
                bkStock.setStockCode(stock.getStockCode());
                bkStock.setStockName(stock.getStockName());
                bkStocks.add(bkStock);
            }
            bkStockService.saveBatch(bkStocks);
        }
    }

    @Override
    public void generateIndustryBkInfo(boolean optionalYn) throws Exception {
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Stock::getIncrease);
        List<Stock> stocks = stockMapper.selectList(lambdaQueryWrapper);
        generateIndustryBkInfo(optionalYn, stocks);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generateConceptBkInfo(boolean optionalYn) throws Exception {
        List<StockConcept> stockConcepts = stockConceptMapper.selectList(null);
        String batchNo = BatchNoUtil.getBatchNo();
        Map<String, List<StockConcept>> map = stockConcepts.stream().collect(Collectors.groupingBy(StockConcept::getConceptName, LinkedHashMap::new, Collectors.toList()));
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<StockConcept>> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue().size();
            countMap.put(key, value);
        }
        Map<String, Integer> sortCountMap = MapUtil.sortByValue(countMap, true);
        System.out.println("sortCountMap = " + sortCountMap);
        for (Map.Entry<String, Integer> entry : sortCountMap.entrySet()) {
            String concept = entry.getKey();
            Set<StockConcept> list = new HashSet<>(map.get(concept));
            // 保存到bk_info
            BkInfo bkInfo = new BkInfo();
            bkInfo.setBatchNo(batchNo);
            Integer no = getNo();
            String bkCode = "C" + DecimalUtil.formatThreeLength(no);
            bkInfo.setBkCode(bkCode);
            bkInfo.setBkName(concept);
            bkInfo.setBkType(2);
            bkInfo.setBkTypeName("概念");
            bkInfoService.save(bkInfo);
            List<BkStock> bkStocks = new ArrayList<>();
            for (StockConcept stockConcept : list) {
                // 循环保存到bk_stock
                BkStock bkStock = new BkStock();
                bkStock.setBkCode(bkCode);
                bkStock.setStockCode(stockConcept.getStockCode());
                bkStock.setStockName(stockConcept.getStockName());
                bkStocks.add(bkStock);
            }
            bkStockService.saveBatch(bkStocks);
        }
    }


    public Integer getNo() {
        for (int i = 0; i < 10; i++) {
            index++;
            String string = String.valueOf(index);
            boolean exclude = isExclude(string);
            if (!exclude) {
                return index;
            }
        }
        return index;
    }

    public Integer getNo(Integer max) {
        for (int i = 0; i < 10; i++) {
            max++;
            String string = String.valueOf(max);
            boolean exclude = isExclude(string);
            if (!exclude) {
                return max;
            }
        }
        return max;
    }

    public boolean isExclude(String string) {
        for (int i = 0; i < EXCLUDE_NUMBER_LIST.size(); i++) {
            String excludeNumber = EXCLUDE_NUMBER_LIST.get(i);
            if (string.contains(excludeNumber)) {
                return true;
            }
        }
        return false;
    }

}
