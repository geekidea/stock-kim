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

package io.geekidea.framework.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.geekidea.framework.common.vo.IdName;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义mybatisplus父Mapper
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author jkcxkj
 * @date 2019/12/28
 **/
public interface SuperMapper<T> extends BaseMapper<T> {

    /**
     * 根据条件查询 id,name 列表
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @return
     */
    List<IdName> selectIdName(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

}
