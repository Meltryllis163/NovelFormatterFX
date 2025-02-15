package cc.meltryllis.nf.parser;

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

}
