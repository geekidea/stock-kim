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
import io.geekidea.stock.entity.SyncKLineErrorData;
import io.geekidea.stock.mapper.SyncKLineErrorDataMapper;
import io.geekidea.stock.service.SyncKLineErrorDataService;
import io.geekidea.stock.dto.query.SyncKLineErrorDataQuery;
import io.geekidea.stock.dto.vo.SyncKLineErrorDataQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <pre>
 * 同步K线错误数据 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-09
 */
@Slf4j
@Service
public class SyncKLineErrorDataServiceImpl extends BaseServiceImpl<SyncKLineErrorDataMapper, SyncKLineErrorData> implements SyncKLineErrorDataService {

    @Autowired
    private SyncKLineErrorDataMapper syncKLineErrorDataMapper;
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSyncKLineErrorData(SyncKLineErrorData syncKLineErrorData) throws Exception {
        return super.save(syncKLineErrorData);
    }
        
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSyncKLineErrorData(SyncKLineErrorData syncKLineErrorData) throws Exception {
        return super.updateById(syncKLineErrorData);
    }
        
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSyncKLineErrorData(Long id) throws Exception {
        return super.removeById(id);
    }
        
    @Override
    public SyncKLineErrorDataQueryVo getSyncKLineErrorDataById(Long id) throws Exception {
        return syncKLineErrorDataMapper.getSyncKLineErrorDataById(id);
    }
        
    @Override
    public Paging<SyncKLineErrorDataQueryVo> getSyncKLineErrorDataPageList(SyncKLineErrorDataQuery syncKLineErrorDataQuery) throws Exception {
        Page page = buildPageQuery(syncKLineErrorDataQuery, OrderItem.desc("create_time"));
        IPage<SyncKLineErrorDataQueryVo> iPage = syncKLineErrorDataMapper.getSyncKLineErrorDataPageList(page, syncKLineErrorDataQuery);
        return new Paging(iPage);
    }
    
}
