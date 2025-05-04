package cc.meltryllis.nf.ui.controller.dialog;

import cc.meltryllis.nf.constants.UICons;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.util.List;

/**
 * 消息对话框控制器。
 * 允许用户执行最简单的是/否选择。
 *
 * @author Zachary W
 * @date 2025/3/28
 */
public class MessagesController extends AbstractStageDialogController<Boolean> {

    @FXML
    public  VBox                root;
    /** 消息文本列表 */
    @Setter
    private List<StringBinding> messages;

    @Override
    protected void stageDialogRegistered() {
        super.stageDialogRegistered();
        root.setAlignment(Pos.CENTER_LEFT);
        root.setSpacing(UICons.DEFAULT_SPACING);
        initMessageLabels();
        getStageDialog().setOnOKAction(event -> {
            setResult(true);
            getStageDialog().close();
        });
        getStageDialog().setOnCancelAction(event -> {
            setResult(false);
            getStageDialog().close();
        });
    }

    private void initMessageLabels() {
        for (StringBinding message : messages) {
            if (message == null) {
                continue;
            }
            Label l = new Label();
            l.setWrapText(true);
            l.textProperty().bind(message);
            root.getChildren().add(l);
        }
    }

}
