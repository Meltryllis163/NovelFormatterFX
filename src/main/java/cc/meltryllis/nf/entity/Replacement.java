package cc.meltryllis.nf.entity;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
@AllArgsConstructor
@Getter
@Setter
public class Replacement {

    @JsonIgnore
    private SimpleBooleanProperty selectedProperty;

    private String oldText;
    private boolean regexMode;
    private String newText;

    public Replacement(String oldText, boolean regexMode, String newText) {
        this(new SimpleBooleanProperty(false), oldText, regexMode, newText);
    }

    public boolean isSelected() {
        return getSelectedProperty().getValue();
    }

    public void setSelected(boolean value) {
        getSelectedProperty().setValue(value);
    }

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
