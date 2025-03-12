package cc.meltryllis.nf.ui.controller.dialog;

import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * 格式化失败对话框控制器。
 *
 * @author Zachary W
 * @date 2025/3/8
 */
public class FormatFailController {

    @FXML
    public Label failLabel;
    @FXML
    public Label reasonLabel;

    public void setFailException(String message) {
        failLabel.textProperty().bind(I18nUtil.createStringBinding("Dialog.FormatFail.Fail"));
        reasonLabel.setText(message);
    }
}
