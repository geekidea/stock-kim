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

package io.geekidea.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.geekidea.framework.common.exception.BusinessException;
import io.geekidea.framework.common.query.OrderItemMapping;
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.framework.thread.ThreadExecutor;
import io.geekidea.framework.thread.ThreadExecutorCallback;
import io.geekidea.framework.util.BatchNoUtil;
import io.geekidea.framework.util.BigDecimalUtil;
import io.geekidea.framework.util.SetUtil;
import io.geekidea.stock.api.SinaStockApi;
import io.geekidea.stock.dto.vo.IncreaseTypeVo;
import io.geekidea.stock.dto.vo.NameValueCount;
import io.geekidea.stock.entity.*;
import io.geekidea.stock.enums.BkTypeEnum;
import io.geekidea.stock.enums.IncreaseTypeEnum;
import io.geekidea.stock.mapper.BkInfoMapper;
import io.geekidea.stock.mapper.BkKLineMapper;
import io.geekidea.stock.service.BkInfoService;
import io.geekidea.stock.dto.query.BkInfoQuery;
import io.geekidea.stock.dto.vo.BkInfoQueryVo;
import io.geekidea.stock.service.BkKLineService;
import io.geekidea.stock.service.StockService;
import io.geekidea.stock.util.IncreaseUtil;
import io.geekidea.stock.util.OptionalYnUtil;
import io.geekidea.stock.util.TaskExecuteTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <pre>
 * 板块信息 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-06
 */
@Slf4j
@Service
public class BkInfoServiceImpl extends BaseServiceImpl<BkInfoMapper, BkInfo> implements BkInfoService {

    @Autowired
    private BkInfoMapper bkInfoMapper;

    @Autowired
    private StockService stockService;

    @Autowired
    private BkKLineService bkKLineService;

    @Autowired
    private BkKLineMapper bkKLineMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBkInfo(BkInfo bkInfo) throws Exception {
        return super.save(bkInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBkInfo(BkInfo bkInfo) throws Exception {
        return super.updateById(bkInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBkInfo(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public BkInfoQueryVo getBkInfoById(Long id) throws Exception {
        return bkInfoMapper.getBkInfoById(id);
    }

    @Override
    public Paging<BkInfoQueryVo> getBkInfoPageList(BkInfoQuery bkInfoQuery) throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        bkInfoQuery.setOptionalYn(optionalYn);
        List<OrderItem> defaultOrderItems = new ArrayList<>();
        String bkStrategy = bkInfoQuery.getBkStrategy();
        if (StringUtils.isNotBlank(bkStrategy)) {
            defaultOrderItems.add(OrderItem.desc("i_avg"));
        } else {
            defaultOrderItems.add(OrderItem.asc("sort"));
            defaultOrderItems.add(OrderItem.asc("bk_code"));
        }
        Integer bkCount = bkInfoQuery.getBkCount();
        if (bkCount == null) {
            bkInfoQuery.setBkCount(3);
        }
        OrderItemMapping orderItemMapping = OrderItemMapping.getInstance().put("iavg", "i_avg");
        Page page = buildPageQuery(bkInfoQuery, defaultOrderItems, orderItemMapping);
        IPage<BkInfoQueryVo> iPage = null;

        Integer searchType = bkInfoQuery.getSearchType();
        // 默认按板块搜索
        if (searchType == null) {
            searchType = 1;
        }
        iPage = bkInfoMapper.getBkInfoPageList(page, bkInfoQuery);
        return new Paging(iPage);
    }

    @Override
    public void syncBkInfo(String bkCode, BigDecimal bkIncrease) throws Exception {
        BkInfo bkInfo = bkInfoMapper.getBkInfoByBkCode(bkCode);
        syncBkInfo(bkInfo, bkIncrease);
    }

    @Override
    public void syncBkInfo(BkInfo bkInfo, BigDecimal bkIncrease) throws Exception {
        if (bkInfo == null) {
            throw new BusinessException("板块信息不能为空");
        }
        bkInfo.setBkIncrease(bkIncrease);
        String bkCode = bkInfo.getBkCode();
        // 同步板块最大涨幅信息
        BkInfo maxIncreaseBkInfo = bkInfoMapper.getMaxIncreaseBkInfo(bkCode);
        if (maxIncreaseBkInfo != null) {
            bkInfo.setMaxStockCode(maxIncreaseBkInfo.getMaxStockCode());
            bkInfo.setMaxStockName(maxIncreaseBkInfo.getMaxStockName());
            bkInfo.setMaxIncrease(maxIncreaseBkInfo.getMaxIncrease());
            bkInfo.setMaxIncreasePrice(maxIncreaseBkInfo.getMaxIncreasePrice());
        }
        // 获取总市值、总交易金额等
        BkInfo totalAmountBkInfo = bkInfoMapper.getTotalAmountBkInfo(bkCode);
        if (totalAmountBkInfo != null) {
            bkInfo.setTotalMarketValue(totalAmountBkInfo.getTotalMarketValue());
            bkInfo.setTotalCirculationValue(totalAmountBkInfo.getTotalCirculationValue());
            bkInfo.setTotalTradeAmount(totalAmountBkInfo.getTotalTradeAmount());
            bkInfo.setTotalTradeNumber(totalAmountBkInfo.getTotalTradeNumber());
        }
        // 更新板块信息
        bkInfo.setUpdateTime(new Date());
        updateById(bkInfo);
    }

    /**
     * 计算涨跌数量
     *
     * @param bkCode
     * @param bkInfo
     * @throws Exception
     */
    private void calcIncreaCount(String bkCode, BkInfo bkInfo) throws Exception {
        if (StringUtils.isBlank(bkCode)) {
            return;
        }
        if (bkInfo == null) {
            return;
        }
        // 获取涨幅类型个数
        List<IncreaseTypeVo> increaseTypeVos = bkInfoMapper.getIncreaseTypeBkInfo(bkCode);
        if (CollectionUtils.isNotEmpty(increaseTypeVos)) {
            Integer bkCount = 0;
            Integer riseCount = 0;
            Integer fallCount = 0;
            Integer flatCount = 0;
            for (IncreaseTypeVo increaseTypeVo : increaseTypeVos) {
                Integer increaseType = increaseTypeVo.getIncreaseType();
                Integer count = increaseTypeVo.getCount();
                bkCount += count;
                if (increaseType.equals(1)) {
                    riseCount = count;
                } else if (increaseType.equals(-1)) {
                    fallCount = count;
                } else if (increaseType.equals(0)) {
                    flatCount = count;
                }
            }
            bkInfo.setBkCount(bkCount);
            bkInfo.setRiseCount(riseCount);
            bkInfo.setFallCount(fallCount);
            bkInfo.setFlatCount(flatCount);
        }
    }

    @Override
    public void syncRealBkInfo(String bkCode) throws Exception {
        if (StringUtils.isBlank(bkCode)) {
            throw new BusinessException("板块代码不能为空");
        }
        BkInfo bkInfo = bkInfoMapper.getBkInfoByBkCode(bkCode);
        if (bkInfo == null) {
            throw new BusinessException("板块信息不能为空");
        }
        BigDecimal bkIncrease = bkInfoMapper.getBkIncrease(bkCode);
        bkInfo.setBkIncrease(bkIncrease);
        calcIncreaCount(bkCode, bkInfo);
        // 更新板块信息
        bkInfo.setUpdateTime(new Date());
        updateById(bkInfo);
    }

    @Override
    public Integer getMaxNo(Integer type) throws Exception {
        if (type == null) {
            return 0;
        }
        String bkCode = bkInfoMapper.getMaxNo(type);
        if (bkCode == null) {
            return 0;
        }
        String cleanBkCode = null;
        if (BkTypeEnum.INDUSTRY.getCode() == type.intValue()) {
            cleanBkCode = bkCode.substring(1);
        } else if (BkTypeEnum.CONCEPT.getCode() == type.intValue()) {
            cleanBkCode = bkCode.substring(1);
        }
        Integer max = Integer.parseInt(cleanBkCode);
        return max;
    }

    @Override
    public BkInfo getBkInfoByBkCode(String bkCode) throws Exception {
        LambdaQueryWrapper<BkInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BkInfo::getBkCode, bkCode);
        return bkInfoMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public void refreshBkInfo(String bkCode) throws Exception {
        if (TaskExecuteTimeUtil.isNotExecute()) {
            return;
        }
        if (StringUtils.isBlank(bkCode)) {
            throw new BusinessException("板块代码不能为空");
        }
        // 获取到板块的股票列表
        List<String> stockCodes = bkInfoMapper.getStocksByBkCode(bkCode);
        if (CollectionUtils.isEmpty(stockCodes)) {
            throw new BusinessException("板块下数据");
        }
        // 调用新浪实时数据接口获取股票数据
        String batchNo = BatchNoUtil.getBatchNo();
        List<StockRealData> stockRealDatas = SinaStockApi.getRealData(batchNo, stockCodes);
        // 更新股票实时数据
        stockService.updateStockRealData(stockRealDatas);
        // 计算板块下个股的汇总数据
        syncRealBkInfo(bkCode);
    }

    @Override
    public void updateBkRealData(String batchNo) throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        // 更新板块涨幅
        LambdaQueryWrapper<BkInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (optionalYn) {
            lambdaQueryWrapper.eq(BkInfo::isOptionalYn, optionalYn);
        }
        List<BkInfo> dbBkInfos = bkInfoMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(dbBkInfos)) {
            return;
        }
        // 获取板块汇总信息
        List<BkInfo> list = bkInfoMapper.getBkStatistics(optionalYn);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(vo -> {
            String maxStockInfo = vo.getMaxStockInfo();
            if (StringUtils.isNotBlank(maxStockInfo)) {
                String[] strings = maxStockInfo.split(",");
                vo.setMaxStockCode(strings[0]);
                vo.setMaxStockName(strings[1]);
                vo.setMaxIncreasePrice(new BigDecimal(strings[2]));
            }
            vo.setBatchNo(batchNo);
            vo.setUpdateTime(new Date());
        });
        Map<String, BkInfo> map = list.stream().collect(Collectors.toMap(BkInfo::getBkCode, BkInfo -> BkInfo));
        // 更新涨跌家数
        List<NameValueCount> nameValueCounts = bkInfoMapper.getBkIncreaseTypeCount(optionalYn);
        if (CollectionUtils.isNotEmpty(nameValueCounts)) {
            for (NameValueCount nameValueCount : nameValueCounts) {
                String bkCode = nameValueCount.getName();
                String increaseType = nameValueCount.getValue();
                Integer count = nameValueCount.getCount();
                BkInfo bkInfo = map.get(bkCode);
                if (bkInfo == null) {
                    continue;
                }
                if ("1".equals(increaseType)) {
                    bkInfo.setRiseCount(count);
                } else if ("-1".equals(increaseType)) {
                    bkInfo.setFallCount(count);
                } else {
                    bkInfo.setFlatCount(count);
                }
            }
        }
        // 批量更新板块信息
        List<BkInfo> updateDbBkInfos = new ArrayList<>();
        for (BkInfo dbBkInfo : dbBkInfos) {
            // 更新板块信息
            String bkCode = dbBkInfo.getBkCode();
            BkInfo bkInfo = map.get(bkCode);
            if (bkInfo != null) {
                updateDbBkInfoField(dbBkInfo, bkInfo, batchNo);
                updateDbBkInfos.add(dbBkInfo);
            }
        }
        updateBatchById(updateDbBkInfos);
        // 更新板块K线
        // 生成板块K线数据
        List<BkKLine> bkKLines = bkKLineMapper.getRealBkKLines(optionalYn);
        if (CollectionUtils.isEmpty(bkKLines)) {
            return;
        }
        String lineDate = bkKLines.get(0).getLineDate();
        int bkKLineSize = bkKLines.size();
        List<String> bkCodes = new ArrayList<>();
        bkKLines.forEach(item -> {
            bkCodes.add(item.getLineCode());
        });
        // 查询已经存在的板块K线数据，修改
        List<BkKLine> existsBkKLines = bkKLineMapper.getExistsBkKLine(bkCodes, lineDate, optionalYn);
        List<BkKLine> addBkKLines = new ArrayList<>();
        List<BkKLine> updateBkKLines = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(existsBkKLines)) {
            Map<String, BkKLine> existsBkKLineMap = existsBkKLines.stream().collect(Collectors.toMap(BkKLine::getLineCode, BkKLine -> BkKLine));
            bkKLines.forEach(item -> {
                String lineCode = item.getLineCode();
                item.setOptionalYn(optionalYn);
                if (existsBkKLineMap.containsKey(lineCode)) {
                    BkKLine dbBkKLine = existsBkKLineMap.get(lineCode);
                    updateBkKLineField(dbBkKLine, item);
                    updateBkKLines.add(dbBkKLine);
                } else {
                    addBkKLines.add(item);
                }
            });
        }

        if (CollectionUtils.isNotEmpty(addBkKLines)) {
            bkKLineService.saveBatch(addBkKLines);
        }
        if (CollectionUtils.isNotEmpty(updateBkKLines)) {
            bkKLineService.updateBatchById(updateBkKLines);
        }
    }

    private void updateBkKLineField(BkKLine dbBkKLine, BkKLine item) {
        if (dbBkKLine == null || item == null) {
            return;
        }
        dbBkKLine.setOpenPrice(item.getOpenPrice());
        dbBkKLine.setClosePrice(item.getClosePrice());
        dbBkKLine.setHighPrice(item.getHighPrice());
        dbBkKLine.setLowPrice(item.getLowPrice());
        dbBkKLine.setTradeAmount(item.getTradeAmount());
        dbBkKLine.setTradeNumber(item.getTradeNumber());
        dbBkKLine.setTurnoverRate(item.getTurnoverRate());
        dbBkKLine.setAmplitude(item.getAmplitude());
        dbBkKLine.setIncrease(item.getIncrease());
        dbBkKLine.setBatchNo(item.getBatchNo());
        dbBkKLine.setUpdateTime(new Date());
    }

    private void updateDbBkInfoField(BkInfo dbBkInfo, BkInfo bkInfo, String batchNo) {
        if (dbBkInfo == null) {
            return;
        }
        if (bkInfo == null) {
            return;
        }
        dbBkInfo.setBkPrice(bkInfo.getBkPrice());
        dbBkInfo.setBkIncrease(bkInfo.getBkIncrease());
        dbBkInfo.setTotalMarketValue(bkInfo.getTotalMarketValue());
        dbBkInfo.setTotalCirculationValue(bkInfo.getTotalCirculationValue());
        dbBkInfo.setTotalTradeAmount(bkInfo.getTotalTradeAmount());
        dbBkInfo.setTotalTradeNumber(bkInfo.getTotalTradeNumber());
        dbBkInfo.setBkCount(bkInfo.getBkCount());
        dbBkInfo.setMaxIncrease(bkInfo.getMaxIncrease());
        dbBkInfo.setMaxStockCode(bkInfo.getMaxStockCode());
        dbBkInfo.setMaxStockName(bkInfo.getMaxStockName());
        dbBkInfo.setRiseCount(bkInfo.getRiseCount());
        dbBkInfo.setFallCount(bkInfo.getFallCount());
        dbBkInfo.setFlatCount(bkInfo.getFlatCount());
        dbBkInfo.setBatchNo(batchNo);
        dbBkInfo.setUpdateTime(new Date());
    }

    @Override
    public void calcBKDayIncreaseMA() throws Exception {
        boolean optionalYn = OptionalYnUtil.getOptionalYn();
        calcBKDayIncreaseMA(optionalYn);
    }

    @Override
    public void calcBKDayIncreaseMA(boolean optionalYn) throws Exception {
        LambdaQueryWrapper<BkInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (optionalYn) {
            lambdaQueryWrapper.eq(BkInfo::isOptionalYn, optionalYn);
        }
        List<BkInfo> bkInfos = bkInfoMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(bkInfos)) {
            return;
        }
        Map<String, BkInfo> bkInfoMap = bkInfos.stream().collect(Collectors.toMap(BkInfo::getBkCode, BkInfo -> BkInfo));
        List<String> bkCodes = new ArrayList<>();
        for (BkInfo bkInfo : bkInfos) {
            String bkCode = bkInfo.getBkCode();
            bkCodes.add(bkCode);
        }
        // 多线程计算
        ThreadExecutor.execute("计算板块阶段涨幅和均线", bkCodes, 100, new ThreadExecutorCallback<String>() {
            @Override
            public void execute(int index, List<String> subList) throws Exception {
                List<BkInfo> updateBkInfos = new ArrayList<>();
                for (int i = 0; i < subList.size(); i++) {
                    String bkCode = subList.get(i);
                    BkInfo bkInfo = bkInfoMap.get(bkCode);
                    // 计算阶段涨幅和均线
                    calcBkDayIncreaseMA(bkInfo);
                    updateBkInfos.add(bkInfo);
                }
                // 批量更新
                updateBatchById(updateBkInfos);
            }

            @Override
            public void finish() throws Exception {

            }
        });
    }

    @Override
    public void calcBkDayIncreaseMA(BkInfo bkInfo) throws Exception {
        if (bkInfo == null) {
            return;
        }
        String bkCode = bkInfo.getBkCode();
        List<BkKLine> bkKLines = bkKLineMapper.getTop61BkKLineListByBkCode(bkCode);
        if (CollectionUtils.isEmpty(bkKLines)) {
            return;
        }
        BkKLine currentBkKLine = bkKLines.get(0);
        BigDecimal currentClosePrice = currentBkKLine.getClosePrice();
        BigDecimal iAvg = BigDecimal.ZERO;
        int afterIndex = 0;
        BigDecimal increase = null;
        // 5日涨幅:（当前收盘价 - 6天前的收盘价 ）/ 6天前的收盘价
        afterIndex = 3;
        increase = getTodayDayIncreaseValue(bkKLines, currentClosePrice, afterIndex);
        iAvg = BigDecimalUtil.add(iAvg, increase);
        bkInfo.setI3(increase);
        afterIndex = 5;
        increase = getTodayDayIncreaseValue(bkKLines, currentClosePrice, afterIndex);
        iAvg = BigDecimalUtil.add(iAvg, increase);
        bkInfo.setI5(increase);
        // 10日涨幅
        afterIndex = 10;
        increase = getTodayDayIncreaseValue(bkKLines, currentClosePrice, afterIndex);
        iAvg = BigDecimalUtil.add(iAvg, increase);
        bkInfo.setI10(increase);
        // 20日涨幅
        afterIndex = 20;
        increase = getTodayDayIncreaseValue(bkKLines, currentClosePrice, afterIndex);
        iAvg = BigDecimalUtil.add(iAvg, increase);
        bkInfo.setI20(increase);
        // 30日涨幅
        afterIndex = 30;
        increase = getTodayDayIncreaseValue(bkKLines, currentClosePrice, afterIndex);
        iAvg = BigDecimalUtil.add(iAvg, increase);
        bkInfo.setI30(increase);
        // 60日涨幅
        afterIndex = 60;
        increase = getTodayDayIncreaseValue(bkKLines, currentClosePrice, afterIndex);
        iAvg = BigDecimalUtil.add(iAvg, increase);
        bkInfo.setI60(increase);

        iAvg = BigDecimalUtil.divide(iAvg, 6, 2);
        bkInfo.setIAvg(iAvg);

        // 计算均线
        BigDecimal maValue = null;
        // 5日均线：(最近5天的收盘价之和) / 5
        afterIndex = 3;
        maValue = getMAValue(bkKLines, afterIndex);
        bkInfo.setMa3(maValue);
        afterIndex = 5;
        maValue = getMAValue(bkKLines, afterIndex);
        bkInfo.setMa5(maValue);
        // 10日均线
        afterIndex = 10;
        maValue = getMAValue(bkKLines, afterIndex);
        bkInfo.setMa10(maValue);
        // 20日均线
        afterIndex = 20;
        maValue = getMAValue(bkKLines, afterIndex);
        bkInfo.setMa20(maValue);
        // 30日均线
        afterIndex = 30;
        maValue = getMAValue(bkKLines, afterIndex);
        bkInfo.setMa30(maValue);
        // 60日均线
        afterIndex = 60;
        maValue = getMAValue(bkKLines, afterIndex);
        bkInfo.setMa60(maValue);

        currentBkKLine.setUpdateTime(new Date());
    }

    private BigDecimal getTodayDayIncreaseValue(List<BkKLine> bkKLines, BigDecimal currentClosePrice,
                                                int afterIndex) {
        int listSize = bkKLines.size();
        if (afterIndex >= listSize) {
            return null;
        }
        BkKLine beforeBkKLine = bkKLines.get(afterIndex);
        BigDecimal beforeClosePrice = beforeBkKLine.getClosePrice();
        return IncreaseUtil.getIncrease(beforeClosePrice, currentClosePrice);
    }

    private BigDecimal getMAValue(List<BkKLine> bkKLines, int afterIndex) {
        int listSize = bkKLines.size();
        if (afterIndex > (listSize - 1)) {
            return null;
        }
        List<BkKLine> subList = bkKLines.subList(0, afterIndex);
        BigDecimal sum = subList.stream().map(BkKLine::getClosePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        int num = subList.size();
        BigDecimal maValue = BigDecimalUtil.divide(sum, num, 2);
        return maValue;
    }

}
