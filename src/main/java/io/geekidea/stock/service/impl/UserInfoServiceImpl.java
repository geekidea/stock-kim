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

import io.geekidea.stock.entity.UserInfo;
import io.geekidea.stock.mapper.UserInfoMapper;
import io.geekidea.stock.service.UserInfoService;
import io.geekidea.stock.dto.query.UserInfoQuery;
import io.geekidea.stock.dto.vo.UserInfoQueryVo;
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
 * 用户信息 服务实现类
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
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveUserInfo(UserInfo userInfo) throws Exception {
        return save(userInfo);
    }
        
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserInfo(UserInfo userInfo) throws Exception {
        return updateById(userInfo);
    }
        
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUserInfo(Long id) throws Exception {
        return removeById(id);
    }
        
    @Override
    public UserInfo getUserInfoById(Long id) throws Exception {
        return getById(id);
    }
        
    @Override
    public Paging<UserInfo> getUserInfoPageList(UserInfoQuery userInfoQuery) throws Exception {
        Page page = buildPageQuery(userInfoQuery, OrderItem.desc("create_time"));
        IPage<UserInfo> iPage = userInfoMapper.getUserInfoPageList(page, userInfoQuery);
        return new Paging(iPage);
    }
    
}
