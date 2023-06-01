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

package io.geekidea.stock.constant;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/9
 **/
public interface RedisKey {

    /**
     * 最近K线数据锁
     */
    String SYNC_STOCK_REAL_DATA_LOCK = "sync_stock_real_data_lock";

    /**
     * 最近K线数据锁
     */
    String SYNC_RECENT_K_LINE_DATA_LOCK = "sync_recent_k_line_data_lock";
    /**
     * 同步K线数据进度
     */
    String SYNC_K_LINE_DATA_PROGRESS = "sync_k_line_data_progress";
    /**
     * 同步K线数据网络超时处理结果
     */
    String SYNC_K_LINE_DATA_TIMEOUT_RESULT = "sync_k_line_data_timeout_result";
}
