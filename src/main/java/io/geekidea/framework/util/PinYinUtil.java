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

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.apache.commons.lang3.StringUtils;

/**
 * http://stock.kim
 * doc.stock.kim
 *
 * @author geekidea
 * @date 2021/12/13
 **/
public class PinYinUtil {

    private static HanyuPinyinOutputFormat UPPERCASE_FORMAT = new HanyuPinyinOutputFormat();

    static {
        // 输出为大写
        UPPERCASE_FORMAT.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        // 没有音标
        UPPERCASE_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    /**
     * 获取中文首字母
     *
     * @param chinese
     * @return
     * @throws Exception
     */
    public static String getFirstLetterPinYin(String chinese) throws Exception {
        if (StringUtils.isBlank(chinese)) {
            return null;
        }
        String string = null;
        try {
            char[] chars = chinese.trim().toCharArray();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                char word = chars[i];
                //匹配是否是汉字
                if (Character.toString(word).matches("[\\u4E00-\\u9FA5]+")) {
                    // 处理多音字
                    if ("长".charAt(0) == word) {
                        stringBuilder.append("C");
                    } else {
                        //如果是多音字，返回多个拼音，这里只取第一个
                        String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(word, UPPERCASE_FORMAT);
                        stringBuilder.append(pinyins[0].charAt(0));
                    }
                } else {
                    stringBuilder.append(word);
                }
            }
            string = stringBuilder.toString().replaceAll(" ", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    public static void main(String[] args) throws Exception {
        String letter = getFirstLetterPinYin("贵州茅台");
        System.out.println(letter);
    }
}
