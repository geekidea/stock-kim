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

import io.geekidea.framework.util.BigDecimalUtil;
import io.geekidea.stock.entity.StockKLine;
import io.geekidea.stock.entity.UserStock;
import io.geekidea.stock.mapper.UserStockMapper;
import io.geekidea.stock.service.UserStockService;
import io.geekidea.stock.dto.query.UserStockQuery;
import io.geekidea.stock.dto.vo.UserStockQueryVo;
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * <pre>
 * 用户持仓信息 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-19
 */
@Slf4j
@Service
public class UserStockServiceImpl extends BaseServiceImpl<UserStockMapper, UserStock> implements UserStockService {

    @Autowired
    private UserStockMapper userStockMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveUserStock(UserStock userStock) throws Exception {
        return save(userStock);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserStock(UserStock userStock) throws Exception {
        return updateById(userStock);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUserStock(Long id) throws Exception {
        return removeById(id);
    }

    @Override
    public UserStock getUserStockById(Long id) throws Exception {
        return getById(id);
    }

    @Override
    public List<UserStock> getUserStockList(UserStockQuery userStockQuery) throws Exception {
        OrderItem orderItem = handleOrderItem(userStockQuery.getOrder(), OrderItem.desc("create_time"));
        userStockQuery.setOrder(orderItem);
        Long userId = userStockQuery.getUserId();
        if (userId == null) {
            userStockQuery.setUserId(1L);
        }
        List<UserStock> list = userStockMapper.getUserStockList(userStockQuery);
        int listSize = list.size();
        if (CollectionUtils.isNotEmpty(list)) {
            // 持仓市值
            BigDecimal totalHoldMarketValue = list.stream().map(UserStock::getHoldMarketValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 持仓盈亏
            BigDecimal totalHoldAmount = list.stream().map(UserStock::getHoldAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 持仓涨幅
            BigDecimal totalHoldIncrease = list.stream().map(UserStock::getHoldIncrease).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal avgHoldIncrease = BigDecimalUtil.divide(totalHoldIncrease, listSize, 2);
            UserStock userStock = new UserStock();
            userStock.setHoldMarketValue(totalHoldMarketValue);
            userStock.setHoldAmount(totalHoldAmount);
            userStock.setHoldIncrease(avgHoldIncrease);
            list.add(userStock);
        }
        return list;
    }

}
