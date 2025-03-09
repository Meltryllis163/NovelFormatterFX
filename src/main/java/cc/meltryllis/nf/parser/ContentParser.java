package cc.meltryllis.nf.parser;

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

    public ContentParser(String text) {
        super(text);
    }

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
        char lastChr = getTrimmingText().charAt(getTrimmingText().length() - 1);
        return ArrayUtil.indexOf(CharacterCons.END_MARKS, lastChr) != -1;
    }

}
