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

import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.framework.util.BatchNoUtil;
import io.geekidea.framework.util.BigDecimalUtil;
import io.geekidea.stock.dto.vo.LineNamveValueVo;
import io.geekidea.stock.entity.StockRiseLine;
import io.geekidea.stock.mapper.StockRiseLineMapper;
import io.geekidea.stock.service.StockRiseLineService;
import io.geekidea.stock.dto.query.StockRiseLineQuery;
import io.geekidea.stock.dto.vo.StockRiseLineQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * <pre>
 * 上涨家数数据 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-11
 */
@Slf4j
@Service
public class StockRiseLineServiceImpl extends BaseServiceImpl<StockRiseLineMapper, StockRiseLine> implements StockRiseLineService {

    @Autowired
    private StockRiseLineMapper stockRiseLineMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveStockRiseLine(StockRiseLine stockRiseLine) throws Exception {
        return super.save(stockRiseLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStockRiseLine(StockRiseLine stockRiseLine) throws Exception {
        return super.updateById(stockRiseLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteStockRiseLine(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public StockRiseLineQueryVo getStockRiseLineById(Long id) throws Exception {
        return stockRiseLineMapper.getStockRiseLineById(id);
    }

    @Override
    public Paging<StockRiseLineQueryVo> getStockRiseLinePageList(StockRiseLineQuery stockRiseLineQuery) throws Exception {
        Page page = buildPageQuery(stockRiseLineQuery, OrderItem.desc("create_time"));
        IPage<StockRiseLineQueryVo> iPage = stockRiseLineMapper.getStockRiseLinePageList(page, stockRiseLineQuery);
        return new Paging(iPage);
    }

    @Override
    public void calcRiseCount() throws Exception {
        // 从K线数据获取每天的上涨家数
        List<StockRiseLine> stockRiseLines = stockRiseLineMapper.getRiseCountList();
        if (CollectionUtils.isEmpty(stockRiseLines)) {
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        stockRiseLines.forEach(line -> {
            line.setLineBatchNo(batchNo);
        });
        // 保存数据
        saveBatch(stockRiseLines);
    }

    @Override
    public void updateTodayRiseCount(boolean optionalYn) throws Exception {
        // 从K线数据获取每天的上涨家数
        StockRiseLine stockRiseLine = stockRiseLineMapper.getTodayRiseCount(optionalYn);
        if (stockRiseLine == null) {
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        stockRiseLine.setLineBatchNo(batchNo);
        String lineDate = stockRiseLine.getLineDate();
        Integer totalCount = stockRiseLine.getTotalCount();
        Integer riseCount = stockRiseLine.getRiseCount();
        BigDecimal risePercentage = BigDecimalUtil.percentage(riseCount, totalCount);
        stockRiseLine.setRisePercentage(risePercentage);
        // 判断当天的数据是否存在
        Integer count = stockRiseLineMapper.getCount(lineDate);
        if (count > 0) {
            // 更新数据
            stockRiseLine.setUpdateTime(new Date());
            updateById(stockRiseLine);
        } else {
            // 保存数据
            stockRiseLine.setCreateTime(new Date());
            save(stockRiseLine);
        }
    }

    @Override
    public List<LineNamveValueVo> getStockRiseLineList() throws Exception {
        return stockRiseLineMapper.getStockRiseLineList();
    }

}
