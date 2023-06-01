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

package io.geekidea.stock.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.geekidea.stock.dto.vo.NameValueVo;
import io.geekidea.stock.entity.BkStock;
import io.geekidea.stock.dto.query.BkStockQuery;
import io.geekidea.stock.dto.vo.BkStockQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * 板块股票 Mapper 接口
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-06
 */
@Repository
public interface BkStockMapper extends BaseMapper<BkStock> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    BkStockQueryVo getBkStockById(Long id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param bkStockQueryParam
     * @return
     */
    IPage<BkStockQueryVo> getBkStockPageList(@Param("page") Page page, @Param("query") BkStockQuery bkStockQuery);

    List<BkStock> getBkStocksByBkCode(String bkCode);

    List<NameValueVo> getBkIndustryCount(String bkCode);

    List<NameValueVo> getBkConceptCount(String bkCode);

    List<String> getBkStockCodesByBkCodeAndIndustry(@Param("bkCode") String bkCode, @Param("industry") String industry);

    List<String> getBkStockCodesByBkCodeAndConcept(@Param("bkCode") String bkCode, @Param("concept") String concept);

    void deleteIndustryConceptBkStock();

    void deleteByStockCodes(@Param("deleteStockCodes") List<String> deleteStockCodes);

    List<String> getBkCodesByStockCodes(@Param("deleteStockCodes") List<String> deleteStockCodes);

    List<String> getBkStockCodesByStockCodes(@Param("bkCode") String bkCode, @Param("stockCodes") List<String> stockCodes);
}
