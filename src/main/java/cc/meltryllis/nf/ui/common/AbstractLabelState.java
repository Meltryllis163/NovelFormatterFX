package cc.meltryllis.nf.ui.common;

import javafx.beans.binding.StringBinding;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code Label} 状态模式。
 * 用于控制 {@code Label} 的图标、文本和颜色。
 *
 * @author Zachary W
 * @date 2025/3/10
 */
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public abstract class AbstractLabelState {

    protected final void handle(StateLabel label, StringBinding text) {
        handle(label);
        label.textProperty().bind(text);
    }

    protected final void handle(StateLabel label, String text) {
        handle(label);
        if (label.textProperty().isBound()) {
            label.textProperty().unbind();
        }
        label.setText(text);
    }

    protected abstract void handle(StateLabel label);
}
