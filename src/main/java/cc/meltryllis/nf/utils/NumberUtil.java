package cc.meltryllis.nf.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * 数字转换工具。
 * <p>
 * 主要用于解析章节编号。因此暂时只考虑正整数，即 {@code [1, Integer.MAX_VALUE]} 范围内的正整数。
 *
 * @author Zachary W
 * @date 2025/2/8
 */
@Slf4j
@SuppressWarnings("UnnecessaryUnicodeEscape")
public class NumberUtil {

    private static final int BIT_ONE             = 0;
    /** 十位索引 */
    private static final int BIT_TEN             = 1;
    /** 百位索引 */
    private static final int BIT_HUNDRED         = 2;
    /** 千位索引 */
    private static final int BIT_THOUSAND        = 3;
    /** 万位索引 */
    private static final int BIT_TEN_THOUSAND    = 4;
    /** 亿位索引 */
    private static final int BIT_HUNDRED_MILLION = 8;

    private static final char S_TEN             = '十';
    private static final char T_TEN             = '拾';
    private static final char S_HUNDRED         = '百';
    private static final char T_HUNDRED         = '佰';
    private static final char S_THOUSAND        = '千';
    private static final char T_THOUSAND        = '仟';
    private static final char S_TEN_THOUSAND    = '万';
    private static final char T_TEN_THOUSAND    = '萬';
    private static final char S_HUNDRED_MILLION = '亿';
    private static final char T_HUNDRED_MILLION = '亿';

    private static final char   ZERO_CHAR = '零';
    private static final String ZERO_STR  = "零";

    private static final char[]                          DIGITS        = new char[]{'零', '一', '壹', '二', '贰', '三', '叁', '四', '肆', '五', '伍', '六', '陆', '七', '柒', '八', '捌', '九', '玖'};
    /** 中文权位 */
    private static final HashMap<Character, ChineseUnit> CHINESE_UNITS = new HashMap<>();
    private static final HashMap<String, String> COLLOQUIALS = new HashMap<>();

    static {
        CHINESE_UNITS.put(S_TEN, new ChineseUnit(S_TEN, 10, false));
        CHINESE_UNITS.put(T_TEN, new ChineseUnit(T_TEN, 10, false));
        CHINESE_UNITS.put(S_HUNDRED, new ChineseUnit(S_HUNDRED, 100, false));
        CHINESE_UNITS.put(T_HUNDRED, new ChineseUnit(T_HUNDRED, 100, false));
        CHINESE_UNITS.put(S_THOUSAND, new ChineseUnit(S_THOUSAND, 1000, false));
        CHINESE_UNITS.put(T_THOUSAND, new ChineseUnit(T_THOUSAND, 1000, false));
        CHINESE_UNITS.put(S_TEN_THOUSAND, new ChineseUnit(S_TEN_THOUSAND, 10000, true));
        CHINESE_UNITS.put(T_TEN_THOUSAND, new ChineseUnit(T_TEN_THOUSAND, 10000, true));
        CHINESE_UNITS.put(S_HUNDRED_MILLION, new ChineseUnit(S_HUNDRED_MILLION, 100000000, true));
    }

    static {
        COLLOQUIALS.put("一十", "十");
        COLLOQUIALS.put("一拾", "拾");
    }

    /**
     * 将数字格式化为中文数字。
     *
     * @param number        需要格式化的数字，该数字仅用于章节编号，因此范围仅限定为 {@code [1, Integer.MAX_VALUE(2147483647)]}
     * @param isTraditional 是否使用繁体中文
     *
     * @return 格式化完成的中文数字字符串。
     *
     * @see #format(int, boolean, boolean)
     */
    public static String format(int number, boolean isTraditional) {
        return format(number, isTraditional, true);
    }

    /**
     * 将数字格式化为中文数字。
     *
     * @param number        需要格式化的数字，该数字仅用于章节编号，因此范围仅限定为 {@code [1, Integer.MAX_VALUE(2147483647)]}
     * @param isTraditional 是否使用繁体中文
     * @param isColloquial  是否口语化，例如「一十万」写成「十万」。
     *
     * @return 格式化完成的中文数字字符串。
     */
    public static String format(int number, boolean isTraditional, boolean isColloquial) {
        StringBuilder builder = new StringBuilder();
        // 千四位
        addPart(builder, BIT_ONE, number % 10000, isTraditional);
        // 万四位
        number /= 10000;
        addPart(builder, BIT_TEN_THOUSAND, number % 10000, isTraditional);
        // 亿四位
        number /= 10000;
        addPart(builder, BIT_HUNDRED_MILLION, number % 10000, isTraditional);
        String trimNumber = trimZero(builder);
        if (isColloquial) {
            for (String s : COLLOQUIALS.keySet()) {
                if (trimNumber.startsWith(s)) {
                    trimNumber = trimNumber.replaceFirst(s, COLLOQUIALS.get(s));
                }
            }
        }
        return trimNumber;
    }

    /**
     * 将当前四位数部分添加至总字符串中。
     *
     * @param builder       字符串构造器
     * @param bit           当前四位数所在节，用来获取当前节的节权位名。可以参考 {@link #getUnitName(int, boolean)} 中的 {@code bit}。
     * @param num           当前节的四位数数值。
     * @param isTraditional 是否使用繁体中文
     */
    private static void addPart(StringBuilder builder, int bit, int num, boolean isTraditional) {
        if (num > 0) {
            if (num % 10 == 0) {
                // 个位为0，需要补零，例如三十万「零」四百
                insertHeaderZero(builder);
            }
            builder.insert(0, getUnitName(bit, isTraditional)).insert(0, thousandToChineseNumber(num, isTraditional));
            if (num < 1000) {
                // 最高位不满，需要补零，例如一万「零」三百七十五
                insertHeaderZero(builder);
            }
        } else {
            // 当前节为0，如果高位还存在数值，则需要补零
            // 此处直接补零，最后再trim过滤
            insertHeaderZero(builder);
        }
    }

    private static void insertHeaderZero(StringBuilder builder) {
        if (!builder.isEmpty() && builder.charAt(0) != ZERO_CHAR) {
            builder.insert(0, ZERO_STR);
        }
    }

    private static String trimZero(StringBuilder builder) {
        if (builder == null) {
            return "";
        }
        if (builder.length() <= 1) {
            return builder.toString();
        }
        char zero = ZERO_STR.charAt(0);
        int start = 0;
        int end = builder.length();
        if (builder.charAt(start) == zero) {
            start++;
        }
        if (builder.charAt(end - 1) == zero) {
            end--;
        }
        return builder.substring(start, end);
    }

    /**
     * 将 {@code [0, 9999]} 之间的整数转换为中文数字。
     *
     * @param number        {@code [0, 9999]} 之间的整数
     * @param isTraditional 是否使用繁体中文
     *
     * @return 转换完成的中文数字，0则返回""用于拼接。
     */
    private static String thousandToChineseNumber(int number, boolean isTraditional) {
        // if (number < 0 || number > 9999) {
        //     throw new IllegalArgumentException(StrUtil.format("{0} is not a number between 0 and 9999.", number));
        // }
        if (number == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int bit = 0;
        int lastNum = 0;
        while (number != 0) {
            int num = number % 10;
            number /= 10;
            if (num == 0) {
                if (lastNum != 0) {
                    builder.insert(0, ZERO_STR);
                }
            } else {
                char chineseChar = numberToChineseNumber(num, isTraditional);
                builder.insert(0, getUnitName(bit, isTraditional)).insert(0, chineseChar);
            }
            bit++;
            lastNum = num;
        }
        return builder.toString();
    }

    /**
     * 将 {@code [0, 9]} 之间的整数转换为中文数字。
     *
     * @param number        {@code [0, 9]} 之间的整数
     * @param isTraditional 是否使用繁体中文
     *
     * @return 转换完成的中文数字。
     */
    private static char numberToChineseNumber(int number, boolean isTraditional) {
        // if (number < 0 || number > 9) {
        //     throw new IllegalArgumentException(StrUtil.format("{0} is not a number between 0 and 9.", number));
        // }
        if (number == 0) {
            return DIGITS[0];
        }
        return DIGITS[number * 2 - (isTraditional ? 0 : 1)];
    }

    /**
     * 得到当前位数对应的权位名。
     *
     * @param bit           数字位数，0为个位，1为十位……以此类推。
     * @param isTraditional 是否使用繁体中文
     *
     * @return 权威名。
     */
    private static String getUnitName(int bit, boolean isTraditional) {
        return switch (bit) {
            case BIT_TEN -> String.valueOf(isTraditional ? T_TEN : S_TEN);
            case BIT_HUNDRED -> String.valueOf(isTraditional ? T_HUNDRED : S_HUNDRED);
            case BIT_THOUSAND -> String.valueOf(isTraditional ? T_THOUSAND : S_THOUSAND);
            case BIT_TEN_THOUSAND -> String.valueOf(isTraditional ? T_TEN_THOUSAND : S_TEN_THOUSAND);
            case BIT_HUNDRED_MILLION -> String.valueOf(S_HUNDRED_MILLION);
            default -> "";
        };
    }

    /**
     * 中文字符串转数字。
     * 支持解析两种中文数字：
     * <ul>
     *     <li>标准类型：例如一千零二十四</li>
     *     <li>非标准类型：如一零二四</li>
     * </ul>
     *
     * @param chinese 传入的字符串。
     *
     * @return 字符串解析成的 {@code int} 数字。
     *
     * @throws IllegalArgumentException 如果传入的字符串不满足中文字符格式，则抛出此异常。
     */
    public static int chineseToNumber(String chinese) throws IllegalArgumentException {
        if (StrUtil.isBlank(chinese)) {
            throw new IllegalArgumentException("Blank string.");
        }
        try {
            return standardChineseToNumber(chinese);
        } catch (IllegalArgumentException e) {
            // log.debug("Parse as standard chinese number failed. Try to parse as non-standard chinese number.");
            // 尝试解析三零九九这类非规范中文数字
            int res = 0;
            for (char c : chinese.toCharArray()) {
                // 异常则抛出
                res = res * 10 + characterToNumber(c);
            }
            return res;
        }
    }

    /**
     * 总结中文数字的规则如下：
     * 1.权位前面可能是权位、非零数字
     * 2.非零数字的前面可能是零或者权位
     * 3.零的前面必然是权位
     * <p>
     * 关于权位：
     * 权位满足以下规则：
     * 节权位会越来越大，比如「五千万亿」，后面的「亿」节权位比前面的「万」节权位更大。
     * 非节权位会越来越小，比如「三千六百八十一」
     *
     * @param str 解析的字符串对象
     *
     * @return 解析成功的数字。
     *
     * @throws IllegalArgumentException 传入对象无法正确解析为数字。
     */
    public static int standardChineseToNumber(String str) throws IllegalArgumentException {
        if (StrUtil.isEmpty(str)) {
            throw new IllegalArgumentException(StrUtil.format("{0} is not a str number string.", str));
        }
        int length = str.length();
        int res = 0;
        int lastNum = 0;
        for (int i = 0; i < length; i++) {
            char chr = str.charAt(i);
            try {
                int num = characterToNumber(chr);
                if (lastNum > 0) {
                    throw new IllegalArgumentException(StrUtil.format("Invalid Number {0}.", str));
                }
                lastNum = num;
            } catch (IllegalArgumentException e) {
                // 如果不是数字，可能是权位
                // log.debug("Not chinese number(0-9). Try to parse as chinese number unit.");
                // 如果不是权位则会抛出异常
                ChineseUnit unit = characterToChineseUnit(chr);
                if (unit.isSectionUnit()) {
                    res += lastNum;
                    res *= unit.value();
                } else {
                    res += (lastNum == 0 ? 1 : lastNum) * unit.value();
                }
                lastNum = 0;
            }
        }
        res += lastNum;
        return res;
    }

    /**
     * 抄自 {@code cn.hutool.core.convert.coNumberChineseFormatter#chineseToNumber(char)}。
     *
     * @throws IllegalArgumentException 字符不是中文数字字符时抛出该异常。
     */
    private static int characterToNumber(char chr) throws IllegalArgumentException {
        // 「两」(20004) -> 「二」(20108)
        if (20004 == chr) {
            chr = 20108;
        }
        int i = ArrayUtil.indexOf(DIGITS, chr);
        if (i < 0) {
            throw new IllegalArgumentException(StrUtil.format("{0} is not a chinese number character.", chr));
        }
        return (i + 1) / 2;
    }

    private static ChineseUnit characterToChineseUnit(char chr) throws IllegalArgumentException {
        if (CHINESE_UNITS.containsKey(chr)) {
            return CHINESE_UNITS.get(chr);
        }
        throw new IllegalArgumentException(StrUtil.format("{0} is not a chinese number unit.", chr));
    }

    /**
     * 尝试解析字符串为数字。
     *
     * @param str 字符串。
     *
     * @return 数字。
     *
     * @throws IllegalArgumentException 无法解析为数字时抛出该异常。
     */
    public static int parseNumber(String str) throws IllegalArgumentException {
        String trimmingStr = StrUtil.trim(str);
        if (StrUtil.isBlank(trimmingStr)) {
            throw new IllegalArgumentException(StrUtil.format("String is blank.", trimmingStr));
        }
        try {
            return chineseToNumber(trimmingStr);
        } catch (IllegalArgumentException e) {
            log.debug(StrUtil.format("{0} is not a chinese number string, try to parse as integer.", str));
            return Integer.parseInt(trimmingStr);
        }
    }

    /**
     * 中文数字权位。
     *
     * @param name
     * @param value
     * @param isSectionUnit 是否为节权位。节权位即整个小节的数字权位，例如对于数字「一千零二十四万」来说，「万」是「一千零三十四」这整个数字节的权位。
     */
    private record ChineseUnit(char name, int value, boolean isSectionUnit) {

    }

}
