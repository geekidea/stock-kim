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
import io.geekidea.stock.dto.query.IndustryConceptIndexQuery;
import io.geekidea.stock.dto.vo.IndustryConceptIndexVo;
import io.geekidea.stock.entity.IndustryIndex;
import io.geekidea.stock.dto.vo.IndustryIndexQueryVo;

/**
 * <pre>
 * 行业指数 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-04
 */
public interface IndustryIndexService extends BaseService<IndustryIndex> {
    
    /**
     * 保存
     *
     * @param industryIndex
     * @return
     * @throws Exception
     */
    boolean saveIndustryIndex(IndustryIndex industryIndex) throws Exception;
        
    /**
     * 修改
     *
     * @param industryIndex
     * @return
     * @throws Exception
     */
    boolean updateIndustryIndex(IndustryIndex industryIndex) throws Exception;
        
    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteIndustryIndex(Long id) throws Exception;
    
    
    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    IndustryIndexQueryVo getIndustryIndexById(Long id) throws Exception;
        
    /**
     * 获取分页对象
     *
     * @param industryIndexQuery
     * @return
     * @throws Exception
     */
    Paging<IndustryConceptIndexVo> getIndustryIndexPageList(IndustryConceptIndexQuery industryConceptIndexQuery) throws Exception;

    void updateIndustryIndex(String batchNo) throws Exception;
}
