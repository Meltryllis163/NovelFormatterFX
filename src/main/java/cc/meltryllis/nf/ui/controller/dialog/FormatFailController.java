package cc.meltryllis.nf.ui.controller.dialog;

import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 格式化失败对话框控制器。
 *
 * @author Zachary W
 * @date 2025/3/8
 */
public class FormatFailController extends AbstractStageDialogController<Exception> implements Initializable {

    @FXML
    public Label    failTitle;
    @FXML
    public Label    failMessage;
    @FXML
    public TextArea exceptionArea;

    public void setFailException(Exception e) {
        failTitle.textProperty().bind(I18nUtil.createStringBinding("Dialog.FormatFail.Title"));
        failMessage.textProperty().bind(I18nUtil.createStringBinding("Dialog.FormatFail.Message"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        exceptionArea.setText(stringWriter.toString());
    }

    @Override
    protected void setInitialResult(Exception initialResult) {
        super.setInitialResult(initialResult);
        setFailException(initialResult);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exceptionArea.setEditable(false);
        exceptionArea.setWrapText(false);
    }

}
