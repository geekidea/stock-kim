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
import io.geekidea.stock.entity.StockRecord;
import io.geekidea.stock.mapper.StockRecordMapper;
import io.geekidea.stock.service.StockRecordService;
import io.geekidea.stock.dto.query.StockRecordQuery;
import io.geekidea.stock.dto.vo.StockRecordQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <pre>
 *  服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-12
 */
@Slf4j
@Service
public class StockRecordServiceImpl extends BaseServiceImpl<StockRecordMapper, StockRecord> implements StockRecordService {

    @Autowired
    private StockRecordMapper stockRecordMapper;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveStockRecord(StockRecord stockRecord) throws Exception {
        return super.save(stockRecord);
    }
        
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStockRecord(StockRecord stockRecord) throws Exception {
        return super.updateById(stockRecord);
    }
        
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteStockRecord(Long id) throws Exception {
        return super.removeById(id);
    }
        
    @Override
    public StockRecordQueryVo getStockRecordById(Long id) throws Exception {
        return stockRecordMapper.getStockRecordById(id);
    }
        
    @Override
    public Paging<StockRecordQueryVo> getStockRecordPageList(StockRecordQuery stockRecordQuery) throws Exception {
        Page page = buildPageQuery(stockRecordQuery, OrderItem.desc("create_time"));
        IPage<StockRecordQueryVo> iPage = stockRecordMapper.getStockRecordPageList(page, stockRecordQuery);
        return new Paging(iPage);
    }
    
}
