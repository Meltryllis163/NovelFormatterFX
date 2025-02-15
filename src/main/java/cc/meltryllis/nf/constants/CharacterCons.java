package cc.meltryllis.nf.constants;

/**
 * 小说中存在的特殊字符常量，例如空格、标点等等。
 *
 * @author Zachary W
 * @date 2025/2/14
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
public interface CharacterCons {

    interface CHINESE {
        /** 全角空格，在txt中，全角空格长度等于四个 {@link CharacterCons.ENGLISH#SPACE} */
        char SPACE = '\u3000';
    }

    interface ENGLISH {
        /** 空格 */
        char SPACE = '\u0020';
    }
}
