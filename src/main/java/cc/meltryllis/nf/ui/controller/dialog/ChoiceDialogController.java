package cc.meltryllis.nf.ui.controller.dialog;

import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * 选择型对话框。让用户选择是或者否。
 *
 * @author Zachary W
 * @date 2025/3/17
 */
public class ChoiceDialogController extends AbstractStageDialogController<Boolean> {

    @FXML
    public VBox   root;
    @FXML
    public Button ok;
    @FXML
    public Button cancel;

    public void setMessages(StringBinding... messages) {
        for (StringBinding message : messages) {
            Label label = new Label();
            label.setWrapText(true);
            label.textProperty().bind(message);
            root.getChildren().add(root.getChildren().size() - 1, label);
        }
    }


    @Override
    protected void initialize() {
        ok.textProperty().bind(I18nUtil.createStringBinding("Common.OK"));
        ok.setOnAction(event -> {
            setResult(true);
            getStageDialog().close();
        });
        cancel.textProperty().bind(I18nUtil.createStringBinding("Common.Cancel"));
        cancel.setOnAction(event -> {
            setResult(false);
            getStageDialog().close();
        });
    }

}
