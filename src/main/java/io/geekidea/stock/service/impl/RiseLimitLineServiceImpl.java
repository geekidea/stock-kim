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
import io.geekidea.framework.util.BatchNoUtil;
import io.geekidea.stock.dto.vo.LineNamveValueVo;
import io.geekidea.stock.entity.RiseLimitLine;
import io.geekidea.stock.mapper.RiseLimitLineMapper;
import io.geekidea.stock.service.RiseLimitLineService;
import io.geekidea.stock.dto.query.RiseLimitLineQuery;
import io.geekidea.stock.dto.vo.RiseLimitLineQueryVo;
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
 * 涨停家数 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-22
 */
@Slf4j
@Service
public class RiseLimitLineServiceImpl extends BaseServiceImpl<RiseLimitLineMapper, RiseLimitLine> implements RiseLimitLineService {

    @Autowired
    private RiseLimitLineMapper riseLimitLineMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRiseLimitLine(RiseLimitLine riseLimitLine) throws Exception {
        return super.save(riseLimitLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRiseLimitLine(RiseLimitLine riseLimitLine) throws Exception {
        return super.updateById(riseLimitLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRiseLimitLine(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public RiseLimitLineQueryVo getRiseLimitLineById(Long id) throws Exception {
        return riseLimitLineMapper.getRiseLimitLineById(id);
    }

    @Override
    public Paging<RiseLimitLineQueryVo> getRiseLimitLinePageList(RiseLimitLineQuery riseLimitLineQuery) throws Exception {
        Page page = buildPageQuery(riseLimitLineQuery, OrderItem.desc("create_time"));
        IPage<RiseLimitLineQueryVo> iPage = riseLimitLineMapper.getRiseLimitLinePageList(page, riseLimitLineQuery);
        return new Paging(iPage);
    }

    @Override
    public void calcRiseLimitCount() throws Exception {
        LambdaUpdateWrapper<RiseLimitLine> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.isNotNull(RiseLimitLine::getLineDate);
        riseLimitLineMapper.delete(lambdaUpdateWrapper);
        // 从K线数据获取每天的上涨家数
        List<RiseLimitLine> riseLimitLines = riseLimitLineMapper.getRiseLimitCountList();
        if (CollectionUtils.isEmpty(riseLimitLines)) {
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        riseLimitLines.forEach(line -> {
            line.setLineBatchNo(batchNo);
        });
        // 保存数据
        saveBatch(riseLimitLines);
    }

    @Override
    public List<LineNamveValueVo> getRiseLimitLineList() throws Exception {
        return riseLimitLineMapper.getRiseLimitLineList();
    }

}
