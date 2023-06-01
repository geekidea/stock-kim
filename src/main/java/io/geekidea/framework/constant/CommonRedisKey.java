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

package io.geekidea.framework.constant;

/**
 * <p>
 *  redis key 常量
 * </p>
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2019-05-23
 **/
public interface CommonRedisKey {

    /**
     * 登陆用户token信息key
     */
    String LOGIN_TOKEN = "login:token:%s";

    /**
     * 登陆用户信息key
     */
    String LOGIN_USER = "login:user:%s";

    /**
     * 登陆用户盐值信息key
     */
    String LOGIN_SALT= "login:salt:%s";

    /**
     * 登陆用户username token
     */
    String LOGIN_USER_TOKEN = "login:user:token:%s:%s";

    /**
     * 验证码
     */
    String VERIFY_CODE = "verify.code:%s";
    /**
     * 短信验证码
     */
    String SMS_VERIFY_CODE = "sms.code:%s";
}
