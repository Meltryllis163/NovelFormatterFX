package cc.meltryllis.nf.formatter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * 文本解析器基类。
 *
 * @author Zachary W
 * @date 2025/2/5
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class AbstractParser {

    protected String trimmingText;

    /**
     * 解析传入的 {@code trimmingText}。
     *
     * @param trimmingText 需要解析的文本，已经处理过前后空白字符。
     *
     * @return 如果文本满足当前解析器要求，则返回 {@code true}，否则返回 {@code false}。
     */
    protected abstract boolean parse(@NotNull String trimmingText);

}
