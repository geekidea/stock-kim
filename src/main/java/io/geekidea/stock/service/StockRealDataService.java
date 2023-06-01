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
import io.geekidea.stock.entity.StockRealData;
import io.geekidea.stock.dto.query.StockRealDataRecordQuery;
import io.geekidea.stock.dto.vo.StockRealDataRecordQueryVo;

/**
 * <pre>
 * 股票实时数据记录 服务类
 * </pre>
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @since 2021-09-13
 */
public interface StockRealDataService {

    void syncStockRealData(boolean isSyncBkInfo) throws Exception;

    void syncMarketStockRealData() throws Exception;

    void syncBkStockRealData() throws Exception;

    void syncBkStockRealData(boolean optionalYn) throws Exception;
}
