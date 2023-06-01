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
import io.geekidea.stock.dto.query.CustomKLineQuery;
import io.geekidea.stock.dto.vo.CustomKLineQueryVo;
import io.geekidea.stock.entity.CustomKLine;

/**
 * <pre>
 * 自定义K线 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-24
 */
public interface CustomKLineService extends BaseService<CustomKLine> {

    /**
     * 保存
     *
     * @param customKLine
     * @return
     * @throws Exception
     */
    boolean saveCustomKLine(CustomKLine customKLine) throws Exception;

    /**
     * 修改
     *
     * @param customKLine
     * @return
     * @throws Exception
     */
    boolean updateCustomKLine(CustomKLine customKLine) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteCustomKLine(Long id) throws Exception;


    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    CustomKLineQueryVo getCustomKLineById(Long id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param customKLineQuery
     * @return
     * @throws Exception
     */
    Paging<CustomKLineQueryVo> getCustomKLinePageList(CustomKLineQuery customKLineQuery) throws Exception;

    /**
     * 获取指定范围的跌幅
     * @param startDate
     * @param limit
     * @throws Exception
     */
    void getRangeFall(String startDate, int limit) throws Exception;

    /**
     * 获取指定范围的涨幅
     * @param startDate
     * @param limit
     * @throws Exception
     */
    void getRangeRise(String startDate, int limit) throws Exception;
}
