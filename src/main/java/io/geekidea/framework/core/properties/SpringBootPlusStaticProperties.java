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

package io.geekidea.framework.core.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 配置文件属性映射为静态属性
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2019-10-11
 * @since 1.3.1.RELEASE
 **/
@Slf4j
@Data
@Configuration
public class SpringBootPlusStaticProperties {

    public static String INFO_PROJECT_VERSION = "";


    @PostConstruct
    public void init() {

    }

}
