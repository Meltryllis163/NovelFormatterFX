package cc.meltryllis.nf.parse;

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

    /**
     * 尝试格式化文本并用 {@link #writer} 写入。
     *
     * @param trimmingText 尝试格式化的文本，已经处理过前后空白字符。
     *
     * @return 返回 {@code parser.parse(String)}。
     */
    public boolean tryFormat(String trimmingText) {
        if (parser.parse(trimmingText)) {
            format();
            return true;
        }
        return false;
    }

    protected abstract void format();

}
