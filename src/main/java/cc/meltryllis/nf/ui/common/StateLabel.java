package cc.meltryllis.nf.ui.common;

import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import lombok.AccessLevel;
import lombok.Setter;

/**
 * {@link AbstractLabelState} 上下文控制。
 *
 * @author Zachary W
 * @date 2025/3/10
 */
@Setter(AccessLevel.PROTECTED)
public class StateLabel extends Label {

    public static final SuccessLabelState SUCCESS = new SuccessLabelState();
    public static final WarnLabelState    WARN    = new WarnLabelState();
    public static final EmptyLabelState   EMPTY   = new EmptyLabelState();

    private AbstractLabelState state;

    public void setState(AbstractLabelState state, String text) {
        this.state = state;
        this.state.handle(this, text);
    }

    public void setState(AbstractLabelState state, StringBinding text) {
        this.state = state;
        this.state.handle(this, text);
    }
}
