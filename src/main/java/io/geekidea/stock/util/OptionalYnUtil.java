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

package io.geekidea.stock.util;

import io.geekidea.framework.util.HttpServletRequestUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/12/14
 **/
public class OptionalYnUtil {
    private static final String REQUEST_HEAD = "OptionalYn";

    public static Boolean optionalYn;

    public static boolean getOptionalYn() {
        if (optionalYn != null) {
            return optionalYn;
        }
//        String optionalYn = HttpServletRequestUtil.getRequest().getHeader(REQUEST_HEAD);
        String optionalYn = "true";
        if (StringUtils.isBlank(optionalYn)) {
            return true;
        } else {
            if (optionalYn.equals("true")) {
                return true;
            }
        }
        return false;
    }


}
