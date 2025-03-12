package cc.meltryllis.nf.utils.common;

/**
 * 字符工具。
 *
 * @author Zachary W
 * @date 2025/2/25
 */
public class CharUtil {

    public static boolean isBlankChar(char chr) {
        return isBlankChar((int) chr);
    }

    public static boolean isBlankChar(int chr) {
        return Character.isWhitespace(chr) || Character.isSpaceChar(chr) || chr == 12288 || chr == 8204 || chr == 8205;
    }
}
