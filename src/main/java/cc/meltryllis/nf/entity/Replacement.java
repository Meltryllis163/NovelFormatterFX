package cc.meltryllis.nf.entity;

import cc.meltryllis.nf.utils.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 导出时文本替换规则。
 * <p>
 * 为了保证相同文本规则在导出时仅被替换一次，重写 {@code equals(Object)} 和 {@code hashCode()} 方法，
 * 设计为仅考虑{@code target} 和 {@code isRegexEnabled}的异同。
 *
 * @author Zachary W
 * @date 2025/2/18
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Replacement {

    private String oldText;
    private boolean regexMode;
    private String newText;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Replacement) {
            return isRegexMode() == ((Replacement) obj).isRegexMode() && StrUtil.equals(getOldText(),
                    ((Replacement) obj).getOldText());
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int res = 1;
        res = prime * res + (getOldText() == null ? 0 : getOldText().hashCode());
        res = prime * res + Boolean.hashCode(regexMode);
        return res;
    }
}
