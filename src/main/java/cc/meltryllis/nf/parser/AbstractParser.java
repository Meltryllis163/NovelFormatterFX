package cc.meltryllis.nf.parser;

import cc.meltryllis.nf.utils.StrUtil;
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
@Setter(AccessLevel.PRIVATE)
public abstract class AbstractParser {

    protected String trimmingText;
    private boolean isSatisfied;

    /**
     * 解析文本。
     *
     * @param trimmingText 需要解析的文本字符串，该文本应该已经经过 {@link StrUtil#trim(String)} 清理空白字符串。
     */
    public AbstractParser(String trimmingText) {
        setTrimmingText(trimmingText);
        if (StrUtil.isBlank(getTrimmingText())) {
            setSatisfied(this.getClass() == BlankLineParser.class);
        } else {
            setSatisfied(parse(getTrimmingText()));
        }
    }

    /**
     * 解析传入的 {@link #trimmingText}。
     *
     * @param trimmingText 需要解析的文本，该文本应该已经经过 {@link StrUtil#trim(String)} 清理空白字符串。
     * @return 满足解析条件则返回 {@code true}, 否则返回 {@code false}。
     */
    protected abstract boolean parse(@NotNull String trimmingText);

}
