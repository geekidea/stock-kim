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

package io.geekidea.framework.common.param;

/**
 * 自定义参数校验接口
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author jkcxkj
 * @date 2020/1/29
 **/
public interface ValidateParam {
    /**
     * 业务检查参数
     *
     * @return
     */
    default void validate() {

    }
}
