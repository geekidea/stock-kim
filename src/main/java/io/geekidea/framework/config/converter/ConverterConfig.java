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

package io.geekidea.framework.config.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 转换器配置
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-9-11
 */
@Configuration
public class ConverterConfig {

    @Bean
    public Converter<String, Date> stringToDateConverter() {
        return new StringToDateConverter();
    }

    @Bean
    public Converter<String, Integer> stringToIntegerConverter() {
        return new StringToIntegerConverter();
    }

    @Bean
    public Converter<String, Double> stringToDoubleConverter() {
        return new StringToDoubleConverter();
    }

}
