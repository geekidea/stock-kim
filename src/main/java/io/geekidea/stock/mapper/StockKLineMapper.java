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
import io.geekidea.stock.dto.query.StockKLineSearchQuery;
import io.geekidea.stock.dto.vo.StockKLineBasicVo;
import io.geekidea.stock.dto.vo.StockKLineDateDiffVo;
import io.geekidea.stock.entity.StockKLine;
import io.geekidea.stock.dto.query.StockKLineQuery;
import io.geekidea.stock.dto.vo.StockKLineQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <pre>
 * K线数据 Mapper 接口
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-18
 */
@Repository
public interface StockKLineMapper extends BaseMapper<StockKLine> {

    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    StockKLineQueryVo getStockKLineById(Long id);

    /**
     * 获取分页对象
     *
     * @param page
     * @param stockKLineQueryParam
     * @return
     */
    IPage<StockKLineQueryVo> getStockKLinePageList(@Param("page") Page page, @Param("query") StockKLineQuery stockKLineQuery);

    StockKLineDateDiffVo getStockKLineDateDiffVo();

    String getUpdateKLineIncreaseStartDate();

    List<String> getLineDates(@Param("startDate") String startDate);

    List<StockKLine> getStockKLineListByLineDates(@Param("lineCode") String lineCode, @Param("lineDates") List<String> lineDates);

    List<StockKLine> getStockKLineListByStockCodeAndLimit(@Param("stockCode") String stockCode, @Param("limit") Integer limit) throws Exception;

    String getMaxLineDate();

    void updateRealKLineData();

    List<StockKLine> getStockKLineListByStockCode(@Param("stockCode") String stockCode);

    List<StockKLine> getTop301StockKLineListByStockCode(@Param("stockCode") String stockCode, @Param("lineDate") String lineDate);

    List<StockKLineBasicVo> getStockKLineBasicList(StockKLineSearchQuery stockKLineSearchQuery);

    void deleteByStockCodes(@Param("deleteStockCodes") List<String> deleteStockCodes);

    void deleteStockKLineByLineDate(String lastTradeDate);

    List<StockKLine> getStockKLineListByStockCodesAndDate(@Param("stockCodes") List<String> stockCodes, @Param("lineDate") String lineDate);
}
