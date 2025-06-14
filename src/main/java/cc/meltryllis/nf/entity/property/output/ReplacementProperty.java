package cc.meltryllis.nf.entity.property.output;

import cc.meltryllis.nf.ui.controls.ICopyableTexts;
import cc.meltryllis.nf.utils.common.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 「文本替换」响应式属性。
 * <p>
 * 为了保证相同文本规则在导出时仅被替换一次，重写 {@code equals(Object)} 和 {@code hashCode()} 方法，
 * 设计为仅考虑{@code oldText} 和 {@code regexMode}的异同。
 *
 * @author Zachary W
 * @date 2025/2/18
 */
@Getter
@Setter
public class ReplacementProperty implements ICopyableTexts {

    @JsonIgnore
    private SimpleStringProperty  oldTextProperty;
    @JsonIgnore
    private SimpleBooleanProperty regexModeProperty;
    @JsonIgnore
    private SimpleStringProperty  newTextProperty;

    @JsonCreator
    public ReplacementProperty(@JsonProperty("oldText") String oldText, @JsonProperty("regexMode") boolean regexMode,
                               @JsonProperty("newText") String newText) {
        this.oldTextProperty = new SimpleStringProperty(oldText);
        this.regexModeProperty = new SimpleBooleanProperty(regexMode);
        this.newTextProperty = new SimpleStringProperty(newText);
    }

    @JsonGetter
    public String getOldText() {
        return getOldTextProperty().getValue();
    }

    public void setOldText(String oldText) {
        getOldTextProperty().setValue(oldText);
    }

    @JsonGetter
    public boolean isRegexMode() {
        return getRegexModeProperty().getValue();
    }

    public void setRegexMode(boolean enabled) {
        getRegexModeProperty().setValue(enabled);
    }

    @JsonGetter
    public String getNewText() {
        return getNewTextProperty().getValue();
    }

    public void setNewText(String newText) {
        getNewTextProperty().setValue(newText);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof ReplacementProperty replacementProperty) {
            return isRegexMode() == replacementProperty.isRegexMode() && StrUtil.equals(getOldText(),
                    replacementProperty.getOldText());
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int res = 1;
        res = prime * res + (getOldText() == null ? 0 : getOldText().hashCode());
        res = prime * res + Boolean.hashCode(isRegexMode());
        return res;
    }

    @Override
    public String[] getCopyableTexts() {
        return new String[] {
                getOldText(), getNewText()
        };
    }
}
