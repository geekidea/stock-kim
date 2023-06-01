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
import io.geekidea.stock.entity.BkKLine;
import io.geekidea.stock.dto.query.BkKLineQuery;
import io.geekidea.stock.dto.vo.BkKLineQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * 板块K线 Mapper 接口
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-06
 */
@Repository
public interface BkKLineMapper extends BaseMapper<BkKLine> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    BkKLineQueryVo getBkKLineById(Long id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param bkKLineQueryParam
     * @return
     */
    IPage<BkKLineQueryVo> getBkKLinePageList(@Param("page") Page page, @Param("query") BkKLineQuery bkKLineQuery);

    List<BkKLine> getBkKLines(@Param("startDate") String startDate, @Param("bkCode") String bkCode);

    List<BkKLine> getBkKLinesByKeyword(@Param("keyword") String keyword, @Param("startDate") String startDate);

    void deleteByBkCode(String startDate, String bkCode);

    List<BkKLine> getBkKLineByStockCodes(@Param("stockCodes") List<String> stockCodes, @Param("startDate") String startDate);

    List<BkKLine> getBkKLinesByBkCode(@Param("bkCode") String bkCode, @Param("startDate") String startDate);

    String getMaxLineDate();

    List<BkKLine> getBkKLinesByStock(@Param("bkCode") String bkCode);

    void deleteIndustryConceptBkKLine();

    void deleteByBkCodes(@Param("deleteBkCodes") List<String> deleteBkCodes);

    void deleteAllByBkCode(@Param("bkCode") String bkCode);

    List<BkKLine> getRealBkKLines(boolean optionalYn);

    List<BkKLine> getExistsBkKLine(@Param("bkCodes") List<String> bkCodes, @Param("lineDate") String lineDate,@Param("optionalYn") boolean optionalYn);

    List<BkKLine> getTop61BkKLineListByBkCode(String bkCode);
}
