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

package io.geekidea.framework.redis;

import io.geekidea.framework.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/17
 **/
@Slf4j
@Aspect
@Component
public class RedisLockAop {

    @Autowired
    private RedisLockService redisLockService;

    @Around("@annotation(redisLock)")
    public Object doAround(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String redisKey = null;
        boolean autoRelease = false;
        try {
            redisKey = redisLock.key();
            int min = redisLock.min();
            int max = redisLock.max();
            String date = DateUtil.getYYYYMMDDHHMMSS(new Date());
            int time = 0;
            if (min > 0) {
                time = min;
                autoRelease = true;
            }
            if (max > 0) {
                time = max;
                autoRelease = true;
            }
            // 尝试加锁
            boolean isLock = redisLockService.lock(redisKey, date, time);
            if (!isLock) {
                throw new RedisLockException("资源正在执行中，请稍后在试");
            }
            return joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            if (!autoRelease) {
                redisLockService.releaseLocak(redisKey);
            }
        }
    }

}
