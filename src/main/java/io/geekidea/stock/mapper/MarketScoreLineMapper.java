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
import io.geekidea.stock.dto.query.MarketScoreLineQuery;
import io.geekidea.stock.dto.vo.LineNamveValueVo;
import io.geekidea.stock.dto.vo.MarketScoreLineQueryVo;
import io.geekidea.stock.dto.vo.NameValuePercentageVo;
import io.geekidea.stock.entity.MarketScoreLine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * 市场分数 Mapper 接口
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-21
 */
@Repository
public interface MarketScoreLineMapper extends BaseMapper<MarketScoreLine> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    MarketScoreLineQueryVo getMarketScoreLineById(Long id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param marketScoreLineQueryParam
     * @return
     */
    IPage<MarketScoreLineQueryVo> getMarketScoreLinePageList(@Param("page" ) Page page, @Param("query" ) MarketScoreLineQuery marketScoreLineQuery);

    List<LineNamveValueVo> getMarketScoreLineList();

    List<NameValuePercentageVo> getStockMarketOverview(@Param("lineDate" ) String lineDate);

    List<NameValuePercentageVo> getIndustryMarketOverview(@Param("lineDate" ) String lineDate);

    List<NameValuePercentageVo> getConceptMarketOverview(@Param("lineDate" ) String lineDate);

    List<NameValuePercentageVo> getIncreaseDistribution(@Param("lineDate" ) String lineDate);

}
