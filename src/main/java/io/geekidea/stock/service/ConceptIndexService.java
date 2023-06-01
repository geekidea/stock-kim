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
import io.geekidea.stock.entity.ConceptIndex;
import io.geekidea.stock.dto.vo.ConceptIndexQueryVo;

/**
 * <pre>
 * 概念指数 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-04
 */
public interface ConceptIndexService extends BaseService<ConceptIndex> {
    
    /**
     * 保存
     *
     * @param conceptIndex
     * @return
     * @throws Exception
     */
    boolean saveConceptIndex(ConceptIndex conceptIndex) throws Exception;
        
    /**
     * 修改
     *
     * @param conceptIndex
     * @return
     * @throws Exception
     */
    boolean updateConceptIndex(ConceptIndex conceptIndex) throws Exception;
        
    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteConceptIndex(Long id) throws Exception;
    
    
    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    ConceptIndexQueryVo getConceptIndexById(Long id) throws Exception;
        
    /**
     * 获取分页对象
     *
     * @param conceptIndexQuery
     * @return
     * @throws Exception
     */
    Paging<IndustryConceptIndexVo> getConceptIndexPageList(IndustryConceptIndexQuery industryConceptIndexQuery) throws Exception;

    void updateConceptIndex(String batchNo) throws Exception;
}
