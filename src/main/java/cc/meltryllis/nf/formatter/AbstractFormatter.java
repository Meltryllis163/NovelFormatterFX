package cc.meltryllis.nf.formatter;

import cc.meltryllis.nf.utils.common.StrUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;

/**
 * 文本格式化器基类。
 *
 * @author Zachary W
 * @date 2025/3/19
 */
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractFormatter<T extends AbstractParser> {

    @NotNull
    protected T              parser;
    @NotNull
    protected BufferedWriter writer;

    public AbstractFormatter(@NotNull T parser, @NotNull BufferedWriter writer) {
        this.parser = parser;
        this.writer = writer;
    }

    public boolean parse(@NotNull String text) {
        String trimmingText = StrUtil.trim(text);
        if (parser.parse(trimmingText)) {
            parser.setTrimmingText(trimmingText);
            return true;
        }
        parser.setTrimmingText(null);
        return false;
    }

    /**
     * 格式化文本。
     *
     * @return 成功输出文本到指定位置则返回 {@code true}，否则返回 {@code false}。
     */
    protected boolean format() {
        if (parser.getTrimmingText() == null) {
            throw new UnsupportedOperationException("Please parse text first.");
        }
        return false;
    }

}
