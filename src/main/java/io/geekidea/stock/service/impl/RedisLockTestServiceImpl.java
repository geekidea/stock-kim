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

import io.geekidea.framework.redis.RedisLock;
import io.geekidea.stock.service.RedisLockTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/17
 **/
@Slf4j
@Service
public class RedisLockTestServiceImpl implements RedisLockTestService {

    @RedisLock(key = "test_redis_lock_key", min = 10)
    @Override
    public String test() throws Exception {
        System.out.println("执行开始。。。。");
        System.out.println("执行结束。。。。");
        return "ok...";
    }
}
