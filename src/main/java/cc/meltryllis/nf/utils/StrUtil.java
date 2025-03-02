package cc.meltryllis.nf.utils;

import java.text.MessageFormat;

/**
 * 字符串工具。
 *
 * @author Zachary W
 * @date 2025/2/25
 */
public class StrUtil {

    public static int TRIM_LEFT  = 1;
    public static int TRIM_RIGHT = 1 << 1;
    public static int TRIM_ALL   = TRIM_LEFT | TRIM_RIGHT;

    public static String format(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!CharUtil.isBlankChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String trim(String str) {
        return str == null ? null : trim(str, TRIM_ALL);
    }

    public static String trim(String str, int mode) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        int start = 0;
        int end = length;
        // 清除左侧空白字符
        if (BitUtil.stateContainsAll(mode, TRIM_LEFT)) {
            while (start < end && CharUtil.isBlankChar(str.charAt(start))) {
                start++;
            }
        }
        // 清除右侧空白字符
        if (BitUtil.stateContainsAll(mode, TRIM_RIGHT)) {
            while (end > start && CharUtil.isBlankChar(str.charAt(end - 1))) {
                end--;
            }
        }
        return str.substring(start, end);
    }

    public static boolean equals(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        }
        return s1.equals(s2);
    }

    /**
     * 重复数次字符并返回重复后的字符串。
     *
     * @param chr   需要重复的字符。
     * @param count 重复次数。如果重复次数为0则返回空字符串。重复次数为1则返回本身。
     *
     * @return 重复完成后的字符串。
     */
    public static String repeat(char chr, int count) {
        String res = String.valueOf(chr);
        return res.repeat(count);
    }
}
