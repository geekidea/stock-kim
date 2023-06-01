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

package io.geekidea.framework.common.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.geekidea.framework.common.query.BaseOrderPageQuery;
import io.geekidea.framework.common.query.BasePageQuery;
import io.geekidea.framework.common.query.OrderItemMapping;
import io.geekidea.framework.common.service.BaseService;
import io.geekidea.framework.util.CaseFormatUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021-08-02
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    protected Page buildPageQuery(BasePageQuery pageQuery) {
        return buildPageQuery(pageQuery, new ArrayList<>());
    }

    protected Page buildPageQuery(BasePageQuery pageQuery, OrderItem defaultOrder) {
        return buildPageQuery(pageQuery, Arrays.asList(defaultOrder));
    }

    protected Page buildPageQuery(BasePageQuery pageQuery, List<OrderItem> defaultOrders) {
        return buildPageQuery(pageQuery, defaultOrders, null);
    }

    protected Page buildPageQuery(BasePageQuery pageQuery, OrderItem defaultOrder, OrderItemMapping orderItemMapping) {
        return buildPageQuery(pageQuery, Arrays.asList(defaultOrder), orderItemMapping);
    }


    protected OrderItem handleOrderItem(OrderItem orderItem) {
        return handleOrderItem(orderItem, null);
    }

    protected OrderItem handleOrderItem(OrderItem orderItem, OrderItem defaultOrder) {
        return handleOrderItem(orderItem, defaultOrder, null);
    }

    protected OrderItem handleOrderItem(OrderItem orderItem, OrderItem defaultOrder, OrderItemMapping orderItemMapping) {
        List<OrderItem> orderItems = handleOrderItem(orderItem, Arrays.asList(defaultOrder), orderItemMapping);
        if (CollectionUtils.isNotEmpty(orderItems)) {
            return orderItems.get(0);
        }
        return null;
    }

    protected List<OrderItem> handleOrderItem(OrderItem orderItem, List<OrderItem> defaultOrders, OrderItemMapping orderItemMapping) {
        return handleOrderItem(Arrays.asList(orderItem), defaultOrders, orderItemMapping);
    }

    protected List<OrderItem> handleOrderItem(List<OrderItem> orderItems, List<OrderItem> defaultOrders, OrderItemMapping orderItemMapping) {
        /**
         * 如果是pageQuery是OrderpageQuery，并且不为空，则使用前端排序
         * 否则使用默认排序
         */
        List<OrderItem> resultOrderItems = defaultOrders;
        if (CollectionUtils.isNotEmpty(orderItems)) {
            boolean hasColumn = false;
            for (OrderItem orderItem : orderItems) {
                if (orderItem == null) {
                    continue;
                }
                if (StringUtils.isNotBlank(orderItem.getColumn())) {
                    hasColumn = true;
                }
            }
            if (hasColumn) {
                resultOrderItems = orderItems;
            }
        }

        if (CollectionUtils.isNotEmpty(resultOrderItems)) {
            // 处理排序字段映射
            Map<String, String> mapMapping = null;
            if (orderItemMapping != null) {
                mapMapping = orderItemMapping.getMap();
            }
            for (OrderItem resultOrderItem : resultOrderItems) {
                if (resultOrderItem == null) {
                    continue;
                }
                boolean flag = false;
                if (MapUtils.isNotEmpty(mapMapping)) {
                    String column = resultOrderItem.getColumn();
                    String mappingName = mapMapping.get(column);
                    if (StringUtils.isNotBlank(mappingName)) {
                        resultOrderItem.setColumn(mappingName);
                        flag = true;
                    }
                }
                if (!flag) {
                    // 默认驼峰转下划线
                    String underscoreColumn = CaseFormatUtil.camelToUnderscore(resultOrderItem.getColumn());
                    resultOrderItem.setColumn(underscoreColumn);
                }
            }
        }
        return resultOrderItems;
    }

    protected Page buildPageQuery(BasePageQuery pageQuery, List<OrderItem> defaultOrders, OrderItemMapping orderItemMapping) {
        Page page = new Page();
        // 设置当前页码
        page.setCurrent(pageQuery.getPageIndex());
        // 设置页大小
        page.setSize(pageQuery.getPageSize());
        /**
         * 如果是pageQuery是OrderpageQuery，并且不为空，则使用前端参数排序
         * 否则使用默认排序
         */
        if (pageQuery instanceof BaseOrderPageQuery) {
            BaseOrderPageQuery baseOrderPageQuery = (BaseOrderPageQuery) pageQuery;
            OrderItem orderItem = baseOrderPageQuery.getOrder();
            List<OrderItem> orderItems = handleOrderItem(orderItem, defaultOrders, orderItemMapping);
            page.setOrders(orderItems);
        }
        return page;
    }


}
