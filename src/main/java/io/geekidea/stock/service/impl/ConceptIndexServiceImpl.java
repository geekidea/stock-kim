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
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.stock.mapper.StockMapper;
import io.geekidea.stock.dto.query.IndustryConceptIndexQuery;
import io.geekidea.stock.dto.vo.IndustryConceptIndexVo;
import io.geekidea.stock.entity.ConceptIndex;
import io.geekidea.stock.mapper.ConceptIndexMapper;
import io.geekidea.stock.service.ConceptIndexService;
import io.geekidea.stock.dto.vo.ConceptIndexQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
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
 * 概念指数 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-04
 */
@Slf4j
@Service
public class ConceptIndexServiceImpl extends BaseServiceImpl<ConceptIndexMapper, ConceptIndex> implements ConceptIndexService {

    @Autowired
    private ConceptIndexMapper conceptIndexMapper;

    @Autowired
    private StockMapper stockMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveConceptIndex(ConceptIndex conceptIndex) throws Exception {
        return super.save(conceptIndex);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateConceptIndex(ConceptIndex conceptIndex) throws Exception {
        return super.updateById(conceptIndex);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteConceptIndex(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public ConceptIndexQueryVo getConceptIndexById(Long id) throws Exception {
        return conceptIndexMapper.getConceptIndexById(id);
    }

    @Override
    public Paging<IndustryConceptIndexVo> getConceptIndexPageList(IndustryConceptIndexQuery industryConceptIndexQuery) throws Exception {
        Page page = buildPageQuery(industryConceptIndexQuery, OrderItem.desc("increase"));
        IPage<IndustryConceptIndexVo> iPage = conceptIndexMapper.getConceptIndexPageList(page, industryConceptIndexQuery);
        return new Paging(iPage);
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateConceptIndex(String batchNo) throws Exception {
        // 获取行业汇总信息
        List<ConceptIndex> list = stockMapper.getConceptStatistics();
        // 更新行业信息
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(vo -> {
            String stockInfo = vo.getStockInfo();
            if (StringUtils.isNotBlank(stockInfo)) {
                String[] strings = stockInfo.split(",");
                vo.setMaxStockCode(strings[0]);
                vo.setMaxStockName(strings[1]);
                vo.setMaxIncreasePrice(new BigDecimal(strings[2]));
                vo.setIndustryName(strings[3]);
            }
            vo.setBatchNo(batchNo);
            vo.setCreateDate(new Date());
            vo.setCreateTime(new Date());
        });
        LambdaUpdateWrapper<ConceptIndex> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.isNotNull(ConceptIndex::getId);
        conceptIndexMapper.delete(lambdaUpdateWrapper);
        saveBatch(list);

    }

}
