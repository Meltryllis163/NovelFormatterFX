package cc.meltryllis.nf.utils;

import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 数字转换工具。
 *
 * @author Zachary W
 * @date 2025/2/8
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
public class NumberUtil {

    /** Unicode中的最小中文字符 */
    public static final char MIN_CHINESE_CHARACTER = '\u4E00';
    /** Unicode中的最大中文字符 */
    public static final char MAX_CHINESE_CHARACTER = '\u9FA5';
    private static final char[] DIGITS = new char[]{'零', '一', '壹', '二', '贰', '三', '叁', '四', '肆', '五', '伍', '六', '陆', '七', '柒', '八', '捌', '九', '玖'};
    /** 数量单位 */
    private static final char[] QUANTITY_UNIT = new char[]{'十', '百', '千', '万', '亿'};

    public static int chineseToNumber(String chinese) throws IllegalArgumentException {
        try {
            return NumberChineseFormatter.chineseToNumber(chinese);
        } catch (IllegalArgumentException e) {
            // 尝试解析三零九九这类非规范中文数字
            int res = 0;
            for (char c : chinese.toCharArray()) {
                res = res * 10 + chineseToNumber(c);
            }
            return res;
        }
    }

    public static int parseNumber(String str) throws IllegalArgumentException {
        str = StrUtil.trim(str);
        if (StrUtil.isBlank(str)) {
            throw new IllegalArgumentException(StrUtil.format("\"{}\" is blank.", str));
        }
        try {
            return chineseToNumber(str);
        } catch (IllegalArgumentException e) {
            return cn.hutool.core.util.NumberUtil.parseInt(str);
        }
    }

    /**
     * 抄自 {@code cn.hutool.core.convert.coNumberChineseFormatter#chineseToNumber(char)}。
     */
    private static int chineseToNumber(char chinese) throws IllegalArgumentException {
        // '两'(20004) -> '二'(20108)
        if (20004 == chinese) {
            chinese = 20108;
        }
        int i = ArrayUtil.indexOf(DIGITS, chinese);
        if (i < 0) {
            throw new IllegalArgumentException(StrUtil.format("{} is not a chinese number character.", chinese));
        }
        return (i + 1) / 2;
    }

}
