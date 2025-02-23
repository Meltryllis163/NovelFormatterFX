package cc.meltryllis.nf.entity.property;

import cc.meltryllis.nf.entity.Replacement;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Replacement} 的响应式装饰属。
 * 新增 {@link #selectedProperty} 用于记录当前属性在 {@code ListView} 或者 {@code TableView} 等数据显示组件中是否被选中。
 *
 * @author Zachary W
 * @date 2025/2/22
 */
@Getter
public class ReplacementProperty {

    @NotNull
    private final Replacement replacement;

    private final SimpleBooleanProperty selectedProperty;

    public ReplacementProperty(@NotNull Replacement replacement) {
        this.replacement = replacement;
        this.selectedProperty = new SimpleBooleanProperty(replacement.isSelected());
        getSelectedProperty().addListener((observable, oldValue, newValue) -> replacement.setSelected(newValue));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof ReplacementProperty) {
            return getReplacement().equals(((ReplacementProperty) obj).getReplacement());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getReplacement().hashCode();
    }
}
