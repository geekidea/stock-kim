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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * 抽象父类参数
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author jkcxkj
 * @date 2020/1/15
 **/
@Data
@ApiModel("抽象父类参数")
public abstract class BaseParam implements Serializable, ValidateParam, ConvertParam {

    private static final long serialVersionUID = 4193146871006919245L;

    /**
     * 是否是管理员，后台使用，前端不用传
     */
    @Null(message = "非法参数")
    private Boolean queryAllData;

    @ApiModelProperty("登陆用户ID")
    private Long loginUserId;

    @ApiModelProperty("是否是管理员")
    private boolean admin;

    @ApiModelProperty("角色ID")
    private Long roleId;

}
