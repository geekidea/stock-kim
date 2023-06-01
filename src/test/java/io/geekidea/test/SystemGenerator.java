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

package io.geekidea.test;

/**
 * 代码生成器入口类
 *
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-08-02
 **/
public class SystemGenerator {

    public static void main(String[] args) {
        CodeGenerator codeGenerator = new CodeGenerator();
        // 公共配置
        // 数据库配置
        codeGenerator
                .setUserName("root")
                .setPassword("root")
                .setDriverName("com.mysql.jdbc.Driver")
                .setDriverUrl("jdbc:mysql://localhost:3306/db_stock?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true");

        // 包信息
        codeGenerator
                .setProjectPackagePath("com/test")
                .setParentPackage("com.test");

        // 组件作者等配置
        codeGenerator
                .setModuleName("stock")
                .setAuthor("geekidea")
                .setPkIdColumnName("id");

        // 生成策略
        codeGenerator.setPageListOrder(true);
        codeGenerator.setParamValidation(true);

        // 生成实体映射相关代码,可用于数据库字段更新
        // 当数据库字段更新时，可自定义自动生成哪些那文件
        codeGenerator.setGeneratorEntity(true);
        codeGenerator.setGeneratorQueryParam(true);
        codeGenerator.setGeneratorQueryVo(true);

        // 生成业务相关代码
        codeGenerator.setGeneratorController(true);
        codeGenerator.setGeneratorService(true);
        codeGenerator.setGeneratorServiceImpl(true);
        codeGenerator.setGeneratorMapper(true);
        codeGenerator.setGeneratorMapperXml(true);
        codeGenerator.setBaseResultMap(false);

        // 指定需要生成的方法，默认生成增删改查分页方法
        codeGenerator.setGeneratorAddMethod(true);
        codeGenerator.setGeneratorUpdateMethod(true);
        codeGenerator.setGeneratorDeleteMethod(true);
        codeGenerator.setGeneratorInfoMethod(true);
        codeGenerator.setGeneratorPageMethod(true);

        codeGenerator.onlyUpdateEntityAndVo(true);

        // 是否覆盖已有文件
        codeGenerator.setFileOverride(true);

        // 初始化公共变量
        codeGenerator.init();

        // 需要生成的表数组
        // xxx,yyy,zzz为需要生成代码的表名称
        String[] tables = {
                "bk_info",
        };

        // 循环生成
        for (String table : tables) {
            // 设置需要生成的表名称
            codeGenerator.setTableName(table);
            // 生成代码
            codeGenerator.generator();
        }

    }

}
