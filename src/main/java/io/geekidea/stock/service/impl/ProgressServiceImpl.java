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

import io.geekidea.stock.constant.RedisKey;
import io.geekidea.stock.dto.vo.ProgressVo;
import io.geekidea.stock.service.ProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/9
 **/
@Slf4j
@Service
public class ProgressServiceImpl implements ProgressService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ProgressVo getProgressInfo(String type) throws Exception {
        ProgressVo progressVo = null;
        if ("syncRecentKLineData".equals(type)) {
            progressVo = (ProgressVo) redisTemplate.opsForValue().get(RedisKey.SYNC_K_LINE_DATA_PROGRESS);
        }
        return progressVo;
    }
}
