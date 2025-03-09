package cc.meltryllis.nf.utils.message.dialog;

import cc.meltryllis.nf.constants.UICons;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * 包含一个或多个 {@link javafx.scene.control.Label} 的 {@code VBox} 容器。
 *
 * @author Zachary W
 * @date 2025/3/10
 */
public class MessagesVBox extends VBox {

    /** 消息文本列表 */
    private final List<StringBinding> messages;

    public MessagesVBox(List<StringBinding> messages) {
        this(messages, Pos.CENTER_LEFT);
    }

    public MessagesVBox(List<StringBinding> messages, Pos alignment) {
        setAlignment(alignment);
        setSpacing(UICons.DEFAULT_SPACING);
        this.messages = messages;
        initMessageLabels();
    }

    private void initMessageLabels() {
        for (StringBinding message : messages) {
            if (message == null) {
                continue;
            }
            Label l = new Label();
            l.setWrapText(true);
            l.textProperty().bind(message);
            getChildren().add(l);
        }
    }
}
