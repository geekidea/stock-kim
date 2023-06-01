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

package io.geekidea.framework.util;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author jkcxkj
 * @date 2020/1/14
 **/
public class SetUtil {

    public static <E> SetUtil.SetDiff<E> diff(final List<? extends E> beforeList, final List<? extends E> afterList) {
        if (beforeList == null || afterList == null) {
            throw new NullPointerException("List must not be null.");
        }
        if (beforeList.equals(afterList)) {
            return new SetDiff<E>().setChange(false);
        }
        Set<E> beforeSet = new HashSet<>(beforeList);
        Set<E> afterSet = new HashSet<>(afterList);
        SetUtils.SetView updateSet = SetUtils.intersection(afterSet, beforeSet);
        SetUtils.SetView deleteSet = SetUtils.difference(beforeSet, afterSet);
        SetUtils.SetView addSet = SetUtils.difference(afterSet, beforeSet);
        SetDiff<E> setDiff = new SetDiff<>();
        if (CollectionUtil.isNotEmpty(addSet)) {
            setDiff.setHasAdd(true);
            setDiff.setAddSet(addSet);
        }
        if (CollectionUtils.isNotEmpty(updateSet)) {
            setDiff.setHasUpdate(true);
            setDiff.setUpdateSet(updateSet);
        }
        if (CollectionUtils.isNotEmpty(deleteSet)) {
            setDiff.setHasDelete(true);
            setDiff.setDeleteSet(deleteSet);
        }
        setDiff.setChange(true);
        return setDiff;
    }

    @Data
    @Accessors(chain = true)
    public static class SetDiff<E> {
        private Set<E> addSet;
        private Set<E> updateSet;
        private Set<E> deleteSet;
        private boolean isChange;
        private boolean hasAdd;
        private boolean hasUpdate;
        private boolean hasDelete;
    }
}
