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
import io.geekidea.framework.thread.ThreadExecutor;
import io.geekidea.framework.thread.ThreadExecutorCallback;
import io.geekidea.stock.dto.query.BkKLineQuery;
import io.geekidea.stock.dto.query.BkKLineSearchQuery;
import io.geekidea.stock.dto.query.SyncBkKLineQuery;
import io.geekidea.stock.dto.vo.BkKLineVo;
import io.geekidea.stock.entity.BkInfo;
import io.geekidea.stock.entity.BkKLine;
import io.geekidea.stock.enums.BkTypeEnum;
import io.geekidea.framework.common.exception.BusinessException;
import io.geekidea.stock.mapper.*;
import io.geekidea.stock.service.BkKLineService;
import io.geekidea.stock.enums.IndustryConceptTypeEnum;
import io.geekidea.stock.service.BkInfoService;
import io.geekidea.stock.dto.vo.BkKLineQueryVo;
import io.geekidea.framework.common.service.impl.BaseServiceImpl;
import io.geekidea.framework.common.vo.Paging;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <pre>
 * 板块K线 服务实现类
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
public class BkKLineServiceImpl extends BaseServiceImpl<BkKLineMapper, BkKLine> implements BkKLineService {

    @Autowired
    private BkKLineMapper bkKLineMapper;

    @Autowired
    private BkStockMapper bkStockMapper;

    @Autowired
    private StockKLineMapper stockKLineMapper;

    @Autowired
    private BkInfoMapper bkInfoMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private BkInfoService bkInfoService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBkKLine(BkKLine bkKLine) throws Exception {
        return super.save(bkKLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBkKLine(BkKLine bkKLine) throws Exception {
        return super.updateById(bkKLine);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBkKLine(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public BkKLineQueryVo getBkKLineById(Long id) throws Exception {
        return bkKLineMapper.getBkKLineById(id);
    }

    @Override
    public Paging<BkKLineQueryVo> getBkKLinePageList(BkKLineQuery bkKLineQuery) throws Exception {
        Page page = buildPageQuery(bkKLineQuery, OrderItem.desc("create_time"));
        IPage<BkKLineQueryVo> iPage = bkKLineMapper.getBkKLinePageList(page, bkKLineQuery);
        return new Paging(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BigDecimal syncBkKLine(String startDate, String bkCode) throws Exception {
        if (StringUtils.isBlank(bkCode)) {
            throw new BusinessException("板块编码不能为空");
        }
        BkInfo bkInfo = bkInfoMapper.getBkInfoByBkCode(bkCode);
        return syncBkKLine(startDate, bkInfo);
    }

    @Override
    public BigDecimal syncBkKLine(String startDate, BkInfo bkInfo, boolean init) throws Exception {
        System.out.println(Thread.currentThread() + " bkCode:" + bkInfo.getBkCode());
        if (bkInfo == null) {
            throw new BusinessException("板块信息不存在");
        }
        if (StringUtils.isBlank(startDate)) {
            startDate = "2021-06-01";
        }
        String bkCode = bkInfo.getBkCode();
        if (StringUtils.isBlank(bkCode)) {
            throw new BusinessException("板块编码不能为空");
        }
        // 删除之前板块K线数据
        if (init) {
            bkKLineMapper.deleteAllByBkCode(bkCode);
        } else {
            bkKLineMapper.deleteByBkCode(startDate, bkCode);
        }
        // 通过板块编码获取该板块下的所有股票信息列表
        // 如果是更新当日板块K线数据，直接从stock表获取数据，否则从stock_k_line获取数据
        List<BkKLine> bkKLines = null;
        String lastTradeDate = stockMapper.getLastTradeDate(true);
        if (lastTradeDate.equals(startDate)) {
            bkKLines = bkKLineMapper.getBkKLinesByStock(bkCode);
        } else {
            bkKLines = bkKLineMapper.getBkKLines(startDate, bkCode);
        }
        if (CollectionUtils.isEmpty(bkKLines)) {
            log.error("K线数据为空" + " bkCode：" + bkCode + "，startDate：" + startDate);
            return null;
        }
        bkKLines.forEach(item -> {
            item.setLineCode(bkCode);
            item.setLineName(bkInfo.getBkName());
        });
        // 保存
        saveBatch(bkKLines);
        // 获取到最后一天的涨幅
        BkKLine firstBkKLine = bkKLines.get(0);
        return firstBkKLine.getIncrease();
    }

    @Override
    public BigDecimal syncBkKLine(String startDate, BkInfo bkInfo) throws Exception {
        return syncBkKLine(startDate, bkInfo, false);
    }

    @Override
    public BkKLineVo getBkKLineList(BkKLineSearchQuery bkKLineSearchQuery) throws Exception {
        String keyword = bkKLineSearchQuery.getKeyword();
        String startDate = bkKLineSearchQuery.getStartDate();
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException("参数不能为空");
        }
        Integer type = bkKLineSearchQuery.getType();
        String name = bkKLineSearchQuery.getName();
        List<String> stockCodes = null;
        List<BkKLine> bkKLines = null;
        if (type == null) {
            bkKLines = bkKLineMapper.getBkKLinesByKeyword(keyword, startDate);
        } else if (type == 1) {
            stockCodes = bkStockMapper.getBkStockCodesByBkCodeAndIndustry(keyword, name);
            bkKLines = bkKLineMapper.getBkKLineByStockCodes(stockCodes, startDate);
        } else if (type == 2) {
            stockCodes = bkStockMapper.getBkStockCodesByBkCodeAndConcept(keyword, name);
            bkKLines = bkKLineMapper.getBkKLineByStockCodes(stockCodes, startDate);
        } else if (type == 3) {
            // 行业/概念点击柱状图获取K线数据
            String typeName = bkKLineSearchQuery.getTypeName();
            String bkName = bkKLineSearchQuery.getBkName();
            Integer bkType = null;
            if (IndustryConceptTypeEnum.INDUSTRY.getCode().equals(typeName)) {
                bkType = BkTypeEnum.INDUSTRY.getCode();
            } else if (IndustryConceptTypeEnum.CONCEPT.getCode().equals(typeName)) {
                bkType = BkTypeEnum.CONCEPT.getCode();
            }
            String bkCode = bkInfoMapper.getBkCode(bkType, bkName);
            bkKLines = bkKLineMapper.getBkKLinesByBkCode(bkCode, startDate);
        }
        if (CollectionUtils.isEmpty(bkKLines)) {
            throw new BusinessException("未找到对应的K线数据");
        }
        List<Object[]> data = new ArrayList<>();
        for (BkKLine bkKLine : bkKLines) {
            Object[] objects = new Object[10];
            // 日期，开盘(open)，收盘(close)，最低(lowest)，最高(highest)
            objects[0] = bkKLine.getLineDate();
            objects[1] = bkKLine.getOpenPrice();
            objects[2] = bkKLine.getClosePrice();
            objects[3] = bkKLine.getLowPrice();
            objects[4] = bkKLine.getHighPrice();
            objects[5] = bkKLine.getIncrease();
            objects[6] = 2.1;
            objects[7] = bkKLine.getTradeNumber();
            objects[8] = bkKLine.getTradeAmount();
            objects[9] = bkKLine.getTurnoverRate();
            data.add(objects);
        }
        BkKLine firstStockLine = bkKLines.get(0);
        String bkCode = firstStockLine.getLineCode();
        BkInfo bkInfo = bkInfoMapper.getBkInfoByBkCode(bkCode);
        BkKLineVo bkKLineVo = new BkKLineVo();
        bkKLineVo.setBkInfo(bkInfo);
        bkKLineVo.setData(data);
        return bkKLineVo;
    }

    @Override
    public void syncBkKLineData(String startDate, List<BkInfo> bkInfos, boolean init) throws Exception {
        if (CollectionUtils.isEmpty(bkInfos)) {
            return;
        }
        Map<String, BkInfo> bkInfoMap = bkInfos.stream().collect(Collectors.toMap(BkInfo::getBkCode, BkInfo -> BkInfo));
        List<String> bkCodes = new ArrayList<>();
        for (BkInfo bkInfo : bkInfos) {
            bkCodes.add(bkInfo.getBkCode());
        }
        ThreadExecutor.execute(bkCodes, 50, new ThreadExecutorCallback<String>() {
            @Override
            public void execute(int index, List<String> subList) throws Exception {
                for (String bkCode : subList) {
                    try {
                        // 获取板块信息
                        BkInfo bkInfo = bkInfoMap.get(bkCode);
                        // 同步板块K线并返回最近一天的K线涨幅
                        BigDecimal bkIncrease = syncBkKLine(startDate, bkInfo, init);
                        if (bkIncrease != null) {
                            // 同步板块信息
                            bkInfoService.syncBkInfo(bkInfo, bkIncrease);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void finish() throws Exception {
                System.out.println("同步板块K线线程执行完成");
            }
        });
    }


    @Override
    public void syncBkKLineData(String startDate, List<BkInfo> bkInfos) throws Exception {
        syncBkKLineData(startDate, bkInfos, false);
    }

    @Override
    public void syncBkKLineData(String startDate) throws Exception {
        LambdaQueryWrapper<BkInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BkInfo::isOptionalYn, true);
        List<BkInfo> bkInfos = bkInfoMapper.selectList(lambdaQueryWrapper);
        syncBkKLineData(startDate, bkInfos);
    }

    @Override
    public void initBkKLineData() throws Exception {
        List<BkInfo> bkInfos = bkInfoMapper.selectList(null);
        syncBkKLineData(null, bkInfos, true);
    }

    @Override
    public void syncBkKLineData(SyncBkKLineQuery syncBkKLineQuery) throws Exception {
        String bkCode = syncBkKLineQuery.getBkCode();
        syncBkKLine(null, bkCode);
    }

    @Override
    public void syncCustomBkKLineData(String startDate) throws Exception {
        LambdaQueryWrapper<BkInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(BkInfo::getBkCode, "CC001");
        List<BkInfo> bkInfos = bkInfoMapper.selectList(lambdaQueryWrapper);
        syncBkKLineData(startDate, bkInfos);
    }

}
