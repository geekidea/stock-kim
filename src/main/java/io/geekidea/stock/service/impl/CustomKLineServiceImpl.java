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

import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
import io.geekidea.framework.util.BatchNoUtil;
import io.geekidea.stock.dto.query.CustomKLineQuery;
import io.geekidea.stock.dto.vo.CustomKLineQueryVo;
import io.geekidea.stock.entity.CustomKLine;
import io.geekidea.stock.entity.Stock;
import io.geekidea.framework.util.DateUtil;
import io.geekidea.stock.mapper.CustomKLineMapper;
import io.geekidea.stock.service.StockService;
import io.geekidea.stock.service.CustomKLineService;
import io.geekidea.stock.util.IncreaseUtil;
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
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * <pre>
 * 自定义K线 服务实现类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-10-24
 */
@Slf4j
@Service
public class CustomKLineServiceImpl extends BaseServiceImpl<CustomKLineMapper, CustomKLine> implements CustomKLineService {

    @Autowired
    private CustomKLineMapper customKLineMapper;

    @Autowired
    private StockService stockService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveCustomKLine(CustomKLine customKLine) throws Exception {
        return super.save(customKLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCustomKLine(CustomKLine customKLine) throws Exception {
        return super.updateById(customKLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteCustomKLine(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public CustomKLineQueryVo getCustomKLineById(Long id) throws Exception {
        return customKLineMapper.getCustomKLineById(id);
    }

    @Override
    public Paging<CustomKLineQueryVo> getCustomKLinePageList(CustomKLineQuery customKLineQuery) throws Exception {
        Page page = buildPageQuery(customKLineQuery, OrderItem.desc("create_time"));
        IPage<CustomKLineQueryVo> iPage = customKLineMapper.getCustomKLinePageList(page, customKLineQuery);
        return new Paging(iPage);
    }

    @Override
    public void getRangeFall(String startDate, int limit) throws Exception {
        long startTime = System.currentTimeMillis();
        if (StringUtils.isBlank(startDate)) {
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        // 获取开始日期到现在的最高价和最高价的日期
        List<CustomKLine> maxPriceList = customKLineMapper.getRangeFallMaxPriceList(startDate);
        if (CollectionUtils.isEmpty(maxPriceList)) {
            log.error("maxPriceList为空");
            return;
        }
        Map<String, CustomKLine> kLineMap = maxPriceList.stream().collect(Collectors.toMap(CustomKLine::getLineCode, CustomKLine -> CustomKLine));
        List<CustomKLine> maxDateList = getRangeFallMaxDateList(startDate, maxPriceList);
        if (CollectionUtils.isEmpty(maxDateList)) {
            log.error("maxDateList为空");
            return;
        }
        for (CustomKLine customKLine : maxDateList) {
            if (customKLine == null) {
                continue;
            }
            String lineCode = customKLine.getLineCode();
            CustomKLine mapCustomKLine = kLineMap.get(lineCode);
            if (mapCustomKLine != null) {
                mapCustomKLine.setMaxDate(customKLine.getMaxDate());
            }
        }
        // 获取最高价格日期后的最低价和最低日期
        List<CustomKLine> minPriceList = getRangeFallMinPriceList(startDate, maxDateList);
        if (CollectionUtils.isEmpty(minPriceList)) {
            log.error("minPriceList为空");
            return;
        }
        for (CustomKLine customKLine : minPriceList) {
            if (customKLine == null) {
                continue;
            }
            String lineCode = customKLine.getLineCode();
            CustomKLine mapCustomKLine = kLineMap.get(lineCode);
            if (mapCustomKLine != null) {
                mapCustomKLine.setMin(customKLine.getMin());
            }
        }
        // 计算最高价到最低价的跌幅排行前500
        List<CustomKLine> minDateList = getRangeFallMinDateList(startDate, kLineMap.values());
        if (CollectionUtils.isEmpty(minDateList)) {
            log.error("minDateList为空");
            return;
        }
        for (CustomKLine customKLine : minDateList) {
            if (customKLine == null) {
                continue;
            }
            String lineCode = customKLine.getLineCode();
            CustomKLine mapCustomKLine = kLineMap.get(lineCode);
            if (mapCustomKLine != null) {
                String maxDate = mapCustomKLine.getMaxDate();
                String minDate = customKLine.getMinDate();
                // 最大值到最小值相差的天数
                Integer diffDay = DateUtil.diffDay(maxDate, minDate);
                mapCustomKLine.setMinDate(minDate);
                mapCustomKLine.setDiffDay(diffDay);
                // 最大值到最小值的跌幅
                BigDecimal fallIncrease = IncreaseUtil.getIncrease(mapCustomKLine.getMax(), mapCustomKLine.getMin());
                mapCustomKLine.setRangeIncrease(fallIncrease);
            }
        }
        // 按照跌幅排序
        List<CustomKLine> customKLineList = new ArrayList<>(kLineMap.values());
        // 筛选跌幅不为空的数据
        customKLineList = customKLineList.stream().filter(new Predicate<CustomKLine>() {
            @Override
            public boolean test(CustomKLine customKLine) {
                if (customKLine == null) {
                    return false;
                }
                if (customKLine.getRangeIncrease() != null) {
                    return true;
                }
                return false;
            }
        }).collect(Collectors.toList());
        // 排序
        List<CustomKLine> sortCustomKLineList = customKLineList.stream().sorted(Comparator.comparing(CustomKLine::getRangeIncrease)).collect(Collectors.toList());
        // 取跌幅前500
        List<CustomKLine> top500List = sortCustomKLineList.subList(0, limit);
        Map<String, CustomKLine> top500Map = top500List.stream().collect(Collectors.toMap(CustomKLine::getLineCode, CustomKLine -> CustomKLine));
        // 查询股票信息
        List<String> lineCodes = new ArrayList<>();
        top500List.forEach(item -> {
            lineCodes.add(item.getLineCode());
        });
        List<Stock> stocks = stockService.getStockListByStockCodes(lineCodes);
        if (CollectionUtils.isEmpty(stocks)) {
            log.error("股票信息为空");
            return;
        }
        for (Stock stock : stocks) {
            String stockCode = stock.getStockCode();
            CustomKLine customKLine = top500Map.get(stockCode);
            if (customKLine != null) {
                customKLine.setStockCode(stockCode);
                customKLine.setStockName(stock.getStockName());
                customKLine.setBatchNo(batchNo);
                customKLine.setI5(stock.getI5());
                customKLine.setI10(stock.getI10());
                customKLine.setI20(stock.getI20());
                customKLine.setI30(stock.getI30());
                customKLine.setI60(stock.getI60());
                customKLine.setI90(stock.getI90());
                customKLine.setI120(stock.getI120());
                customKLine.setI250(stock.getI250());
                customKLine.setI300(stock.getI300());
                customKLine.setIncrease(stock.getIncrease());
                customKLine.setPrice(stock.getPrice());
                customKLine.setIndustry(stock.getIndustry());
                customKLine.setStartDate(startDate);
            }
        }

        System.out.println("top500List = " + top500List.size());
        // 入库
        saveBatch(top500List);

        long endTime = System.currentTimeMillis();
        long diffTime = endTime - startTime;
        System.out.println("diffTime = " + (diffTime / 1000));
    }

    @Override
    public void getRangeRise(String startDate, int limit) throws Exception {
        long startTime = System.currentTimeMillis();
        if (StringUtils.isBlank(startDate)) {
            return;
        }
        String batchNo = BatchNoUtil.getBatchNo();
        // 获取开始日期到现在的最低价和最低价的日期
        List<CustomKLine> minPriceList = customKLineMapper.getRangeRiseMinPriceList(startDate);
        if (CollectionUtils.isEmpty(minPriceList)) {
            log.error("minPriceList为空");
            return;
        }
        Map<String, CustomKLine> kLineMap = minPriceList.stream().collect(Collectors.toMap(CustomKLine::getLineCode, CustomKLine -> CustomKLine));
        List<CustomKLine> minDateList = getRangeRiseMinDateList(startDate, minPriceList);
        if (CollectionUtils.isEmpty(minDateList)) {
            log.error("minDateList为空");
            return;
        }
        for (CustomKLine customKLine : minDateList) {
            if (customKLine == null) {
                continue;
            }
            String lineCode = customKLine.getLineCode();
            CustomKLine mapCustomKLine = kLineMap.get(lineCode);
            if (mapCustomKLine != null) {
                mapCustomKLine.setMinDate(customKLine.getMinDate());
            }
        }
        // 获取最高价格日期后的最高价和最高日期
        List<CustomKLine> maxPriceList = getRangeRiseMaxPriceList(startDate, minDateList);
        if (CollectionUtils.isEmpty(maxPriceList)) {
            log.error("maxPriceList为空");
            return;
        }
        for (CustomKLine customKLine : maxPriceList) {
            if (customKLine == null) {
                continue;
            }
            String lineCode = customKLine.getLineCode();
            CustomKLine mapCustomKLine = kLineMap.get(lineCode);
            if (mapCustomKLine != null) {
                mapCustomKLine.setMax(customKLine.getMax());
            }
        }
        // 计算最低价到最高价的涨幅排行前500
        List<CustomKLine> maxDateList = getRangeRiseMaxDateList(startDate, kLineMap.values());
        if (CollectionUtils.isEmpty(maxDateList)) {
            log.error("maxDateList为空");
            return;
        }
        for (CustomKLine customKLine : maxDateList) {
            if (customKLine == null) {
                continue;
            }
            String lineCode = customKLine.getLineCode();
            CustomKLine mapCustomKLine = kLineMap.get(lineCode);
            if (mapCustomKLine != null) {
                String minDate = mapCustomKLine.getMinDate();
                String maxDate = customKLine.getMaxDate();
                // 最大值到最小值相差的天数
                Integer diffDay = DateUtil.diffDay(maxDate, minDate);
                mapCustomKLine.setMaxDate(maxDate);
                mapCustomKLine.setDiffDay(diffDay);
                // 最大值到最小值的跌幅
                BigDecimal fallIncrease = IncreaseUtil.getIncrease(mapCustomKLine.getMin(), mapCustomKLine.getMax());
                mapCustomKLine.setRangeIncrease(fallIncrease);
            }
        }
        // 按照涨幅排序
        List<CustomKLine> customKLineList = new ArrayList<>(kLineMap.values());
        // 筛选跌幅不为空的数据
        customKLineList = customKLineList.stream().filter(new Predicate<CustomKLine>() {
            @Override
            public boolean test(CustomKLine customKLine) {
                if (customKLine == null) {
                    return false;
                }
                if (customKLine.getRangeIncrease() != null) {
                    return true;
                }
                return false;
            }
        }).collect(Collectors.toList());
        // 排序
        List<CustomKLine> sortCustomKLineList = customKLineList.stream().sorted(Comparator.comparing(CustomKLine::getRangeIncrease).reversed()).collect(Collectors.toList());
        // 取跌幅前500
        List<CustomKLine> top500List = sortCustomKLineList.subList(0, limit);
        Map<String, CustomKLine> top500Map = top500List.stream().collect(Collectors.toMap(CustomKLine::getLineCode, CustomKLine -> CustomKLine));
        // 查询股票信息
        List<String> lineCodes = new ArrayList<>();
        top500List.forEach(item -> {
            lineCodes.add(item.getLineCode());
        });
        List<Stock> stocks = stockService.getStockListByStockCodes(lineCodes);
        if (CollectionUtils.isEmpty(stocks)) {
            log.error("股票信息为空");
            return;
        }
        for (Stock stock : stocks) {
            String stockCode = stock.getStockCode();
            CustomKLine customKLine = top500Map.get(stockCode);
            if (customKLine != null) {
                customKLine.setStockCode(stockCode);
                customKLine.setStockName(stock.getStockName());
                customKLine.setBatchNo(batchNo);
                customKLine.setI5(stock.getI5());
                customKLine.setI10(stock.getI10());
                customKLine.setI20(stock.getI20());
                customKLine.setI30(stock.getI30());
                customKLine.setI60(stock.getI60());
                customKLine.setI90(stock.getI90());
                customKLine.setI120(stock.getI120());
                customKLine.setI250(stock.getI250());
                customKLine.setI300(stock.getI300());
                customKLine.setIncrease(stock.getIncrease());
                customKLine.setPrice(stock.getPrice());
                customKLine.setIndustry(stock.getIndustry());
                customKLine.setStartDate(startDate);
                customKLine.setType(9);
            }
        }

        System.out.println("top500List = " + top500List.size());
        // 入库
        saveBatch(top500List);

        long endTime = System.currentTimeMillis();
        long diffTime = endTime - startTime;
        System.out.println("diffTime = " + (diffTime / 1000));
    }

    private List<CustomKLine> getRangeRiseMaxDateList(String startDate, Collection<CustomKLine> values) {
        List<CustomKLine> list = new ArrayList<>();
        for (CustomKLine customKLine : values) {
            CustomKLine data = customKLineMapper.getRangeRiseMaxDateList(startDate, customKLine);
            list.add(data);
        }
        return list;
    }

    private List<CustomKLine> getRangeRiseMaxPriceList(String startDate, List<CustomKLine> minDateList) {
        List<CustomKLine> list = new ArrayList<>();
        for (CustomKLine customKLine : minDateList) {
            CustomKLine data = customKLineMapper.getRangeRiseMaxPriceList(startDate, customKLine);
            list.add(data);
        }
        return list;
    }

    private List<CustomKLine> getRangeRiseMinDateList(String startDate, List<CustomKLine> minPriceList) {
        List<CustomKLine> list = new ArrayList<>();
        for (CustomKLine customKLine : minPriceList) {
            CustomKLine data = customKLineMapper.getRangeRiseMinDateList(startDate, customKLine);
            list.add(data);
        }
        return list;
    }

    private List<CustomKLine> getRangeFallMaxDateList(String startDate, List<CustomKLine> maxPriceList) throws Exception {
        List<CustomKLine> list = new ArrayList<>();
        for (CustomKLine customKLine : maxPriceList) {
            CustomKLine data = customKLineMapper.getRangeFallMaxDateList(startDate, customKLine);
            list.add(data);
        }
        return list;
    }

    private List<CustomKLine> getRangeFallMinPriceList(String startDate, List<CustomKLine> maxDateList) throws Exception {
        List<CustomKLine> list = new ArrayList<>();
        for (CustomKLine customKLine : maxDateList) {
            CustomKLine data = customKLineMapper.getRangeFallMinPriceList(startDate, customKLine);
            list.add(data);
        }
        return list;
    }

    private List<CustomKLine> getRangeFallMinDateList(String startDate, Collection<CustomKLine> values) throws Exception {
        List<CustomKLine> list = new ArrayList<>();
        for (CustomKLine customKLine : values) {
            CustomKLine data = customKLineMapper.getRangeFallMinDateList(startDate, customKLine);
            list.add(data);
        }
        return list;
    }

}
