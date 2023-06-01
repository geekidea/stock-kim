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

import io.geekidea.stock.entity.UserStockBill;
import io.geekidea.stock.mapper.UserStockBillMapper;
import io.geekidea.stock.service.UserStockBillService;
import io.geekidea.stock.dto.query.UserStockBillQuery;
import io.geekidea.stock.dto.vo.UserStockBillQueryVo;
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;


/**
 * <pre>
 * 用户持仓流水 服务实现类
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
public class UserStockBillServiceImpl extends BaseServiceImpl<UserStockBillMapper, UserStockBill> implements UserStockBillService {

    @Autowired
    private UserStockBillMapper userStockBillMapper;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveUserStockBill(UserStockBill userStockBill) throws Exception {
        return save(userStockBill);
    }
        
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserStockBill(UserStockBill userStockBill) throws Exception {
        return updateById(userStockBill);
    }
        
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUserStockBill(Long id) throws Exception {
        return removeById(id);
    }
        
    @Override
    public UserStockBill getUserStockBillById(Long id) throws Exception {
        return getById(id);
    }
        
    @Override
    public Paging<UserStockBill> getUserStockBillPageList(UserStockBillQuery userStockBillQuery) throws Exception {
        Page page = buildPageQuery(userStockBillQuery, OrderItem.desc("create_time"));
        IPage<UserStockBill> iPage = userStockBillMapper.getUserStockBillPageList(page, userStockBillQuery);
        return new Paging(iPage);
    }
    
}
