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

package io.geekidea.framework.thread;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/11/16
 **/
@Slf4j
public class ThreadExecutor {

    /**
     * true:同步，false:异步
     */
    public static boolean sync = true;

    public static void execute(String businessName, List<?> list, int splitNum, ThreadExecutorCallback threadExecutorCallback) {
        if (StringUtils.isBlank(businessName)) {
            businessName = "";
        }
        log.info("sync：{}", sync);
        long startTime = System.currentTimeMillis();
        ThreadExecutor threadExecutor = new ThreadExecutor();
        try {
            log.info(businessName + "开始执行");
            if (sync) {
                threadExecutor.submitSync(businessName, list, splitNum, threadExecutorCallback);
            } else {
                threadExecutor.submit(businessName, list, splitNum, threadExecutorCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            double diff = (endTime - startTime) / 1000.0;
            BigDecimal diffTime = new BigDecimal(diff).setScale(2, BigDecimal.ROUND_HALF_UP);
            log.info(businessName + "完成，耗时：{}秒", diffTime);
        }

    }

    public static void execute(List<?> list, int splitNum, ThreadExecutorCallback threadExecutorCallback) {
        execute(null, list, splitNum, threadExecutorCallback);
    }

    private void submit(String businessName, List<?> list, int splitNum, ThreadExecutorCallback threadExecutorCallback) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 拆分成子集合
        List<? extends List<?>> lists = ListUtils.partition(list, splitNum);
        // 子集合大小
        int listSize = lists.size();
        CountDownLatch countDownLatch = new CountDownLatch(listSize);
        ExecutorService executorService = Executors.newFixedThreadPool(listSize);
        for (int i = 0; i < listSize; i++) {
            List<?> subList = lists.get(i);
            int k = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        threadExecutorCallback.execute(k, subList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        try {
            countDownLatch.await();
            log.info(businessName + "多线程执行完成");
            threadExecutorCallback.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitSync(String businessName, List<?> list, int splitNum, ThreadExecutorCallback threadExecutorCallback) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 拆分成子集合
        List<? extends List<?>> lists = ListUtils.partition(list, splitNum);
        // 子集合大小
        int listSize = lists.size();
        try {
            for (int i = 0; i < listSize; i++) {
                List<?> subList = lists.get(i);
                int k = i;
                threadExecutorCallback.execute(0, subList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                threadExecutorCallback.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
