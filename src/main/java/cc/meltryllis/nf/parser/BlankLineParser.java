package cc.meltryllis.nf.parser;

import org.jetbrains.annotations.NotNull;

/**
 * 空行解析器。
 *
 * @author Zachary W
 * @date 2025/2/13
 */
public class BlankLineParser extends AbstractParser {

    public BlankLineParser(String text) {
        super(text);
    }

    @Override
    protected boolean parse(@NotNull String trimmingText) {
        return false;
    }
}
