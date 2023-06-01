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

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.geekidea.framework.common.exception.BusinessException;
import io.geekidea.stock.entity.Stock;
import io.geekidea.stock.mapper.BkStockMapper;
import io.geekidea.stock.dto.vo.NameValueVo;
import io.geekidea.stock.entity.BkStock;
import io.geekidea.stock.mapper.StockMapper;
import io.geekidea.stock.service.BkStockService;
import io.geekidea.stock.dto.query.BkStockQuery;
import io.geekidea.stock.dto.vo.BkStockQueryVo;
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


/**
 * <pre>
 * 板块股票 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-06
 */
@Slf4j
@Service
public class BkStockServiceImpl extends BaseServiceImpl<BkStockMapper, BkStock> implements BkStockService {

    @Autowired
    private BkStockMapper bkStockMapper;

    @Autowired
    private StockMapper stockMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBkStock(BkStock bkStock) throws Exception {
        String bkCode = bkStock.getBkCode();
        String stockCode = bkStock.getStockCode();
        if (StringUtils.isBlank(bkCode)) {
            throw new BusinessException("板块代码不能为空");
        }
        if (StringUtils.isBlank(stockCode)) {
            throw new BusinessException("股票代码不能为空");
        }
        Stock stock = stockMapper.selectById(stockCode);
        if (stock == null) {
            throw new BusinessException("股票信息不存在");
        }
        bkStock.setStockName(stock.getStockName());
        bkStock.setIndustry(stock.getIndustry());
        return super.save(bkStock);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBkStock(BkStock bkStock) throws Exception {
        return super.updateById(bkStock);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBkStock(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public BkStockQueryVo getBkStockById(Long id) throws Exception {
        return bkStockMapper.getBkStockById(id);
    }

    @Override
    public Paging<BkStockQueryVo> getBkStockPageList(BkStockQuery bkStockQuery) throws Exception {
        Page page = buildPageQuery(bkStockQuery, OrderItem.desc("create_time"));
        IPage<BkStockQueryVo> iPage = bkStockMapper.getBkStockPageList(page, bkStockQuery);
        return new Paging(iPage);
    }

    @Override
    public List<NameValueVo> getBkIndustryCount(String bkCode) throws Exception {
        return bkStockMapper.getBkIndustryCount(bkCode);
    }

    @Override
    public List<NameValueVo> getBkConceptCount(String bkCode) throws Exception {
        return bkStockMapper.getBkConceptCount(bkCode);
    }

    @Override
    public boolean deleteBkStock(BkStock bkStock) throws Exception {
        String bkCode = bkStock.getBkCode();
        String stockCode = bkStock.getStockCode();
        if (StringUtils.isBlank(bkCode)) {
            throw new BusinessException("板块代码不能为空");
        }
        if (StringUtils.isBlank(stockCode)) {
            throw new BusinessException("股票代码不能为空");
        }
        LambdaUpdateWrapper<BkStock> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(BkStock::getBkCode, bkStock.getBkCode());
        lambdaUpdateWrapper.eq(BkStock::getStockCode, bkStock.getStockCode());
        return bkStockMapper.delete(lambdaUpdateWrapper) > 0;
    }

}
