package com.home.homepage.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class PinyinUtil {

    public static String generate(String chinese) throws Exception {
        if (chinese == null || chinese.isEmpty()) {
            return "";
        }
        // 默认移除掉所有的标点后，再转拼音处理
        chinese = clear(chinese);

        StringBuilder pinyin = new StringBuilder();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        char[] chars = chinese.toCharArray();
        for (char c : chars) {
            if (c > 128) {
                pinyin.append(PinyinHelper.toHanyuPinyinStringArray(c, format)[0]);
            } else {
                pinyin.append(c);
            }
        }
        return pinyin.toString();
    }

    // 清楚字符串中的所有中英文标点符号
    public static String clear(String chinese) {
        chinese = chinese.replaceAll("\\p{Punct}", "");
        chinese = chinese.replaceAll("\\pP", "");
        chinese = chinese.replaceAll("\\p{P}", "");
        chinese = chinese.replaceAll("\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
        chinese = chinese.replaceAll("\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
        return chinese;
    }
}
