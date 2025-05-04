package cc.meltryllis.nf.formatter;

import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.utils.common.ArrayUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 正文解析器。
 *
 * @author Zachary W
 * @date 2025/2/4
 */
public class ContentParser extends AbstractParser {

    /** 可以表示一句话结束的标点符号集合 */
    public static final char[] END_MARKS = new char[] {
            /* 中文标点 */ CharacterCons.CHINESE.PERIOD, CharacterCons.CHINESE.QUESTION, CharacterCons.CHINESE.EXCLAMATION, CharacterCons.CHINESE.RIGHT_DOUBLE_QUOTE, CharacterCons.CHINESE.RIGHT_CORNER_BRACKET, CharacterCons.CHINESE.RIGHT_WHITE_CORNER_BRACKET,
            /* 英文标点 */ CharacterCons.ENGLISH.PERIOD, CharacterCons.ENGLISH.QUESTION, CharacterCons.ENGLISH.EXCLAMATION, CharacterCons.ENGLISH.QUOTE };

    @Override
    protected boolean parse(@NotNull String trimmingText) {
        return true;
    }

    /**
     * 判断当前文本是否为完整语句。
     *
     * @return 完整则返回 {@code true}，否则返回 {@code false}。
     */
    protected boolean isComplete() {
        char[] textChars = getTrimmingText().toCharArray();
        int textLength = textChars.length;

        char lastChr = textChars[textLength - 1];
        if (ArrayUtil.indexOf(END_MARKS, lastChr) == -1) {
            return false;
        }
        return true;
    }

}
