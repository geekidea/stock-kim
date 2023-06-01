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

import io.geekidea.stock.mapper.StockKLineMapper;
import io.geekidea.stock.mapper.StockMapper;
import io.geekidea.stock.entity.Stock;
import io.geekidea.stock.entity.StockKLine;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/10
 **/
public class StockKLineTest extends BaseTest {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockKLineMapper stockKLineMapper;

    @Test
    public void test() {
        List<Stock> stocks = stockMapper.selectList(null);
        for (Stock stock : stocks) {
            String stockCode = stock.getStockCode();
            List<StockKLine> stockKLines = stockKLineMapper.getStockKLineListByStockCode(stockCode);
            if (CollectionUtils.isEmpty(stockKLines)) {
                continue;
            }
            int size = stockKLines.size();
            if (size > 415) {
                List<StockKLine> deleteStockKLines = stockKLines.subList(415, size);
                List<Long> deleteStockKLineIds = new ArrayList<>();
                deleteStockKLines.forEach(item -> {
                    deleteStockKLineIds.add(item.getId());
                });
                stockKLineMapper.deleteBatchIds(deleteStockKLineIds);
            }
            System.out.println();
        }
    }

}
