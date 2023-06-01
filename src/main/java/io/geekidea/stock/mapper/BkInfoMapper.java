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
import io.geekidea.stock.dto.vo.IncreaseTypeVo;
import io.geekidea.stock.dto.vo.NameValueCount;
import io.geekidea.stock.entity.BkInfo;
import io.geekidea.stock.dto.query.BkInfoQuery;
import io.geekidea.stock.dto.vo.BkInfoQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 * 板块信息 Mapper 接口
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-11-06
 */
@Repository
public interface BkInfoMapper extends BaseMapper<BkInfo> {
    
    /**
     * 根据ID获取查询对象
     *
     * @param id
     * @return
     */
    BkInfoQueryVo getBkInfoById(Long id);
        
    /**
     * 获取分页对象
     *
     * @param page
     * @param bkInfoQueryParam
     * @return
     */
    IPage<BkInfoQueryVo> getBkInfoPageList(@Param("page") Page page, @Param("query") BkInfoQuery bkInfoQuery);

    BkInfo getBkInfoByBkCode(@Param("bkCode") String bkCode);

    List<String> getBkCodes();

    String getBkCode(Integer bkType, String bkName);

    BkInfo getMaxIncreaseBkInfo(String bkCode);

    BkInfo getTotalAmountBkInfo(String bkCode);

    List<IncreaseTypeVo> getIncreaseTypeBkInfo(String bkCode);

    List<String> getExistsBkInfoListByIndustryBkNames(@Param("industrys") Set<String> industrys);

    String getMaxNo(Integer type);

    void deleteNonSubStockBkInfo();

    List<BkInfo> getAddBkInfos();

    List<String> getStocksByBkCode(String bkCode);

    BigDecimal getBkIncrease(String bkCode);

    List<BkInfo> getBkStatistics(boolean optionalYn);

    List<NameValueCount> getBkIncreaseTypeCount(boolean optionalYn);

}
