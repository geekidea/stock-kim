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
import io.geekidea.stock.entity.StockConceptContent;
import io.geekidea.stock.mapper.StockConceptContentMapper;
import io.geekidea.stock.service.StockConceptContentService;
import io.geekidea.stock.dto.query.StockConceptContentQuery;
import io.geekidea.stock.dto.vo.StockConceptContentQueryVo;
import io.geekidea.stock.service.StockConceptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


/**
 * <pre>
 * 股票概念内容 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-24
 */
@Slf4j
@Service
public class StockConceptContentServiceImpl extends BaseServiceImpl<StockConceptContentMapper, StockConceptContent> implements StockConceptContentService {

    @Autowired
    private StockConceptContentMapper stockConceptContentMapper;

    @Autowired
    private StockConceptService stockConceptService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveStockConceptContent(StockConceptContent stockConceptContent) throws Exception {
        return super.save(stockConceptContent);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStockConceptContent(StockConceptContent stockConceptContent) throws Exception {
        return super.updateById(stockConceptContent);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteStockConceptContent(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public StockConceptContentQueryVo getStockConceptContentById(Long id) throws Exception {
        return stockConceptContentMapper.getStockConceptContentById(id);
    }

    @Override
    public Paging<StockConceptContentQueryVo> getStockConceptContentPageList(StockConceptContentQuery stockConceptContentQuery) throws Exception {
        Page page = buildPageQuery(stockConceptContentQuery, OrderItem.desc("create_time"));
        IPage<StockConceptContentQueryVo> iPage = stockConceptContentMapper.getStockConceptContentPageList(page, stockConceptContentQuery);
        return new Paging(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveStockConceptContent(List<StockConceptContent> stockConceptContents) throws Exception {
        if (CollectionUtils.isEmpty(stockConceptContents)) {
            return;
        }
        // 批量保存概念内容
        this.saveBatch(stockConceptContents);
        // 保存概念分类映射
        stockConceptService.saveStockConcept(stockConceptContents);
    }

}
