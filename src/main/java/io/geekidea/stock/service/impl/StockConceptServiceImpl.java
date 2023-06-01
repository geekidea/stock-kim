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
import io.geekidea.stock.entity.StockConcept;
import io.geekidea.stock.entity.StockConceptContent;
import io.geekidea.stock.mapper.StockConceptMapper;
import io.geekidea.stock.service.StockConceptContentService;
import io.geekidea.stock.service.StockConceptService;
import io.geekidea.stock.dto.query.StockConceptQuery;
import io.geekidea.stock.dto.vo.StockConceptQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <pre>
 * 股票概念 服务实现类
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
public class StockConceptServiceImpl extends BaseServiceImpl<StockConceptMapper, StockConcept> implements StockConceptService {

    @Autowired
    private StockConceptMapper stockConceptMapper;

    @Autowired
    private StockConceptContentService stockConceptContentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveStockConcept(StockConcept stockConcept) throws Exception {
        return super.save(stockConcept);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStockConcept(StockConcept stockConcept) throws Exception {
        return super.updateById(stockConcept);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteStockConcept(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public StockConceptQueryVo getStockConceptById(Long id) throws Exception {
        return stockConceptMapper.getStockConceptById(id);
    }

    @Override
    public Paging<StockConceptQueryVo> getStockConceptPageList(StockConceptQuery stockConceptQuery) throws Exception {
        Page page = buildPageQuery(stockConceptQuery, OrderItem.desc("create_time"));
        IPage<StockConceptQueryVo> iPage = stockConceptMapper.getStockConceptPageList(page, stockConceptQuery);
        return new Paging(iPage);
    }

    @Override
    public void saveStockConcept(List<StockConceptContent> stockConceptContents) throws Exception {
        if (CollectionUtils.isEmpty(stockConceptContents)) {
            return;
        }
        // 拆分概念内容，有多少个概念，就组装多少行数据
        for (StockConceptContent stockConceptContent : stockConceptContents) {
            String stockCode = stockConceptContent.getStockCode();
            String stockName = stockConceptContent.getStockName();
            String conceptContent = stockConceptContent.getConceptContent();
            String conceptBatchNo = stockConceptContent.getConceptBatchNo();
            if (StringUtils.isBlank(conceptContent)) {
                log.error("conceptContent为空，stockCode：" + stockCode);
                continue;
            }
            String[] conceptContents = conceptContent.split(";");
            List<StockConcept> stockConcepts = new ArrayList<>();
            for (String concept : conceptContents) {
                StockConcept stockConcept = new StockConcept();
                stockConcept.setStockCode(stockCode);
                stockConcept.setStockName(stockName);
                stockConcept.setConceptBatchNo(conceptBatchNo);
                stockConcept.setConceptName(concept);
                stockConcepts.add(stockConcept);
            }
            this.saveBatch(stockConcepts);
        }
    }

    @Override
    public void cleanRepeatConcept() throws Exception {
        // 获取所有数据
        List<StockConcept> stockConcepts = list();
        // 按概念名称分组
        Map<String, List<StockConcept>> map = stockConcepts.stream().collect(Collectors.groupingBy(StockConcept::getConceptName, LinkedHashMap::new, Collectors.toList()));
        List<Long> repeatIds = new ArrayList<>();
        // 循环每个概念，判断重复的加入到待删除列表
        for (Map.Entry<String, List<StockConcept>> entry : map.entrySet()) {
            String conceptName = entry.getKey();
            Set<String> stockCodeSet = new HashSet<>();
            List<StockConcept> list = entry.getValue();
            for (StockConcept stockConcept : list) {
                Long id = stockConcept.getId();
                String stockCode = stockConcept.getStockCode();
                String stockName = stockConcept.getStockName();
                String concept = stockConcept.getConceptName();
                if (stockCodeSet.contains(stockCode)) {
                    repeatIds.add(stockConcept.getId());
                    System.out.println("重复：" + id + " " + stockCode + " " + stockName + " " + concept);
                } else {
                    stockCodeSet.add(stockCode);
                }
            }
        }
        System.out.println("repeatIds = " + repeatIds);
        if (CollectionUtils.isNotEmpty(repeatIds)) {
            // 批量删除重复的数据
            stockConceptMapper.deleteBatchIds(repeatIds);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void splitConcept() throws Exception {
        // 获取所有概念内容
        List<StockConceptContent> stockConceptContents = stockConceptContentService.list();
        // 保存概念分类映射
        saveStockConcept(stockConceptContents);
        // 清除重复概念
        cleanRepeatConcept();
    }

}
