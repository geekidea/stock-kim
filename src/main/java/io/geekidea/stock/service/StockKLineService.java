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
import io.geekidea.stock.callback.SyncKLineCallback;
import io.geekidea.stock.dto.query.StockKLineSearchQuery;
import io.geekidea.stock.dto.vo.*;
import io.geekidea.stock.dto.query.StockKLineQuery;
import io.geekidea.stock.entity.Stock;
import io.geekidea.stock.entity.StockKLine;
import io.geekidea.stock.entity.StockRealData;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 * K线数据 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-18
 */
public interface StockKLineService extends BaseService<StockKLine> {

    /**
     * 保存
     *
     * @param stockKLine
     * @return
     * @throws Exception
     */
    boolean saveStockKLine(StockKLine stockKLine) throws Exception;

    /**
     * 修改
     *
     * @param stockKLine
     * @return
     * @throws Exception
     */
    boolean updateStockKLine(StockKLine stockKLine) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteStockKLine(Long id) throws Exception;


    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     * @throws Exception
     */
    StockKLineQueryVo getStockKLineById(Long id) throws Exception;

    /**
     * 获取分页对象
     *
     * @param stockKLineQuery
     * @return
     * @throws Exception
     */
    Paging<StockKLineQueryVo> getStockKLinePageList(StockKLineQuery stockKLineQuery) throws Exception;

    StockKLineVo getStockKLineList(StockKLineSearchQuery lineCode) throws Exception;

    StockBasicKLineVo getStockBasicKLineList(StockKLineSearchQuery lineCode) throws Exception;

    void syncKLineData() throws Exception;

    void syncKLineData(List<Stock> stocks, int day, Date beforeEndDate, boolean isUpdate,Integer splitSize, SyncKLineCallback syncKLineCallback) throws Exception;

    void syncKLineData(List<Stock> stocks, int day, Date beforeEndDate, boolean isUpdate) throws Exception;

    void syncKLineData(Stock stock, int index, int day, Date beforeEndDate, boolean isUpdate) throws Exception;

    /**
     * 更新K线涨幅和振幅
     * @param stockKLines
     * @throws Exception
     */
    void calcKLineIncreaseAmplitude(List<StockKLine> stockKLines) throws Exception;
    
    void updateKLineIncrease(List<Stock> stocks, String startDate) throws Exception;

    void updateKLineIncrease(String startDate) throws Exception;

    List<StockKLine> getStockKLineListByStockCode(String stockCode, String startDate) throws Exception;

    void asyncRecentKLineData() throws Exception;

    void syncRecentKLineData() throws Exception;

    void updateRecentKLineIncrease() throws Exception;

    void updateKLineDayIncreaseByStocks(List<Stock> stocks) throws Exception;

    void updateKLineDayIncrease() throws Exception;

    void updateTodayKLineDayIncrease(String lineDate) throws Exception;

    void calcKLineDayIncrease(List<StockKLine> stockKLines) throws Exception;

    void updateKLineDayIncrease(List<StockKLine> stockKLines) throws Exception;

    void updateKLineDayIncreaseByStockCode(String batchNo, String stockCode) throws Exception;

    void updateTodayKLineDayIncreaseByStockCode(String batchNo, String stockCode, String lineDate) throws Exception;

    void updateKLineMAByStocks(List<Stock> stocks) throws Exception;

    void updateKLineMA() throws Exception;

    void updateKLineMA(List<StockKLine> stockKLines) throws Exception;

    void updateKLineMAByStockCode(String batchNo, String stockCode) throws Exception;

    void updateRealKLineData(List<StockRealData> stockRealData) throws Exception;

    void syncTodayKLineData();

    /**
     * 获取最近需要更新的天数和开始时间
     *
     * @return
     * @throws Exception
     */
    StockKLineDateDiffVo getStockKLineDateDiffVo() throws Exception;

    void syncTimeoutKLineData() throws Exception;

    ProgressVo syncRecentKLineDataProgress() throws Exception;

    void updateRealKLineData() throws Exception;

    void updateStockKLineRealData(List<StockRealData> stockRealDataRecords) throws Exception;
}
