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

import io.geekidea.stock.mapper.InitMapper;
import io.geekidea.stock.service.InitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-10-26
 **/
@Slf4j
@Service
public class InitServiceImpl implements InitService {

    @Autowired
    private InitMapper initMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    @Override
    public void updateMaxConnection() {
        String sql = "set global max_connections=1056";
        jdbcTemplate.update(sql);
    }

}
