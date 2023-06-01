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

import io.geekidea.stock.dto.query.BkKLineSearchQuery;
import io.geekidea.stock.dto.query.SyncBkKLineQuery;
import io.geekidea.stock.dto.vo.BkKLineVo;
import io.geekidea.stock.entity.BkInfo;
import io.geekidea.stock.entity.BkKLine;
import io.geekidea.framework.common.service.BaseService;
import io.geekidea.stock.dto.query.BkKLineQuery;
import io.geekidea.stock.dto.vo.BkKLineQueryVo;
import io.geekidea.framework.common.vo.Paging;

import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 * 板块K线 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-06
 */
public interface BkKLineService extends BaseService<BkKLine> {

    /**
     * 保存
     *
     * @param bkKLine
     * @return
     * @throws Exception
     */
    boolean saveBkKLine(BkKLine bkKLine) throws Exception;

    /**
     * 修改
     *
     * @param bkKLine
     * @return
     * @throws Exception
     */
    boolean updateBkKLine(BkKLine bkKLine) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteBkKLine(Long id) throws Exception;


    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    BkKLineQueryVo getBkKLineById(Long id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param bkKLineQuery
     * @return
     * @throws Exception
     */
    Paging<BkKLineQueryVo> getBkKLinePageList(BkKLineQuery bkKLineQuery) throws Exception;

    BigDecimal syncBkKLine(String startDate, String bkCode) throws Exception;

    BigDecimal syncBkKLine(String startDate, BkInfo bkInfo, boolean init) throws Exception;

    BigDecimal syncBkKLine(String startDate, BkInfo bkInfo) throws Exception;

    BkKLineVo getBkKLineList(BkKLineSearchQuery bkKLineSearchQuery) throws Exception;

    void syncBkKLineData(String startDate, List<BkInfo> bkInfos, boolean init) throws Exception;

    void syncBkKLineData(String startDate, List<BkInfo> bkInfos) throws Exception;

    void syncBkKLineData(String startDate) throws Exception;

    void initBkKLineData() throws Exception;

    void syncBkKLineData(SyncBkKLineQuery syncBkKLineQuery) throws Exception;

    void syncCustomBkKLineData(String startDate) throws Exception;
}
