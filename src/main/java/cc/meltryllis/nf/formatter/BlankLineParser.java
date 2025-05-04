package cc.meltryllis.nf.formatter;

import cc.meltryllis.nf.utils.common.StrUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 空行解析器。
 *
 * @author Zachary W
 * @date 2025/2/13
 */
public class BlankLineParser extends AbstractParser {

    @Override
    protected boolean parse(@NotNull String trimmingText) {
        return StrUtil.isEmpty(trimmingText);
    }

}
