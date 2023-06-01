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

import io.geekidea.stock.dto.query.StockSearchQuery;
import io.geekidea.stock.dto.vo.StockQueryVo;
import io.geekidea.stock.dto.vo.StockSearchVo;
import io.geekidea.stock.mapper.BkStockMapper;
import io.geekidea.stock.mapper.StockMapper;
import io.geekidea.stock.service.StockSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/23
 **/
@Slf4j
@Service
public class StockSearchServiceImpl implements StockSearchService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private BkStockMapper bkStockMapper;

    @Override
    public List<StockSearchVo> search(StockSearchQuery stockSearchQuery) {
        String keyword = stockSearchQuery.getKeyword();
        String bkCode = stockSearchQuery.getBkCode();
        if (StringUtils.isBlank(keyword)) {
            return null;
        }
        if (StringUtils.isBlank(bkCode)) {
            return null;
        }
        // 通过keyword在stock表中搜索
        List<StockSearchVo> list = stockMapper.getStockByKeyword(keyword);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<String> stockCodes = new ArrayList<>();
        for (StockSearchVo stockSearchVo : list) {
            stockCodes.add(stockSearchVo.getStockCode());
        }
        // 判断keyword在bkCode中是否存在
        List<String> existsStockCodes = bkStockMapper.getBkStockCodesByStockCodes(bkCode, stockCodes);
        for (StockSearchVo stockSearchVo : list) {
            String stockCode = stockSearchVo.getStockCode();
            if (existsStockCodes.contains(stockCode)) {
                stockSearchVo.setExists(true);
            }
        }
        return list;
    }

}
