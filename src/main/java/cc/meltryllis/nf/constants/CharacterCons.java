package cc.meltryllis.nf.constants;

/**
 * 小说中存在的特殊字符常量，例如空格、标点等等。
 * 部分命名来自 <a href="https://unicodeplus.com/">Unicode表</a>。
 *
 * @author Zachary W
 * @date 2025/2/14
 */
@SuppressWarnings({"UnnecessaryUnicodeEscape"})
public interface CharacterCons {

    /** 可以表示一句话结束的标点符号集合 */
    char[] END_MARKS = new char[] {
            /* 中文标点 */ CHINESE.PERIOD, CHINESE.QUESTION, CHINESE.EXCLAMATION, CHINESE.RIGHT_DOUBLE_QUOTE, CHINESE.RIGHT_CORNER_BRACKET, CHINESE.RIGHT_WHITE_CORNER_BRACKET,
            /* 英文标点 */ ENGLISH.PERIOD, ENGLISH.QUESTION, ENGLISH.EXCLAMATION, ENGLISH.QUOTE };

    interface CHINESE {

        /** 全角空格，在txt中，全角空格长度等于四个 {@link CharacterCons.ENGLISH#SPACE} */
        char SPACE                      = '\u3000';
        char COMMA                      = ',';
        char PERIOD                     = '。';
        char QUESTION                   = '？';
        char EXCLAMATION                = '！';
        /** 上双引号 */
        char LEFT_DOUBLE_QUOTE          = '“';
        /** 下双引号 */
        char RIGHT_DOUBLE_QUOTE         = '”';
        /** 上直角单引号 */
        char LEFT_CORNER_BRACKET        = '「';
        /** 上直角单引号 */
        char RIGHT_CORNER_BRACKET       = '」';
        /** 下直角双引号 */
        char LEFT_WHITE_CORNER_BRACKET  = '『';
        /** 下直角双引号 */
        char RIGHT_WHITE_CORNER_BRACKET = '』';

    }

    interface ENGLISH {

        /** 空格 */
        char SPACE       = '\u0020';
        char COMMA       = ',';
        char PERIOD      = '.';
        char QUESTION    = '?';
        char EXCLAMATION = '!';
        /** 双引号 */
        char QUOTE       = '"';
    }
}
