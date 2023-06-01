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

package io.geekidea.stock.service;

import io.geekidea.framework.common.service.BaseService;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.stock.entity.BkInfo;
import io.geekidea.stock.dto.query.BkInfoQuery;
import io.geekidea.stock.dto.vo.BkInfoQueryVo;
import io.geekidea.stock.entity.Stock;

import java.math.BigDecimal;

/**
 * <pre>
 * 板块信息 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-06
 */
public interface BkInfoService extends BaseService<BkInfo> {
    
    /**
     * 保存
     *
     * @param bkInfo
     * @return
     * @throws Exception
     */
    boolean saveBkInfo(BkInfo bkInfo) throws Exception;
        
    /**
     * 修改
     *
     * @param bkInfo
     * @return
     * @throws Exception
     */
    boolean updateBkInfo(BkInfo bkInfo) throws Exception;
        
    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteBkInfo(Long id) throws Exception;
    
    
    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    BkInfoQueryVo getBkInfoById(Long id) throws Exception;
        
    /**
     * 获取分页对象
     *
     * @param bkInfoQuery
     * @return
     * @throws Exception
     */
    Paging<BkInfoQueryVo> getBkInfoPageList(BkInfoQuery bkInfoQuery) throws Exception;

    void syncBkInfo(String bkCode, BigDecimal bkIncrease) throws Exception;

    void syncBkInfo(BkInfo bkInfo, BigDecimal bkIncrease) throws Exception;

    void syncRealBkInfo(String bkCode) throws Exception;

    Integer getMaxNo(Integer type) throws Exception;

    BkInfo getBkInfoByBkCode(String bkCode) throws Exception;

    void refreshBkInfo(String bkCode) throws Exception;

    void updateBkRealData(String batchNo) throws Exception;

    void calcBKDayIncreaseMA() throws Exception;

    void calcBKDayIncreaseMA(boolean optionalYn) throws Exception;

    void calcBkDayIncreaseMA(BkInfo bkInfo) throws Exception;
}
