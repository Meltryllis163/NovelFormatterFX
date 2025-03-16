package cc.meltryllis.nf.ui.controller.dialog;

import cc.meltryllis.nf.constants.FileCons;
import cc.meltryllis.nf.utils.common.FileUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.File;

/**
 * 格式化成功对话框控制器。
 *
 * @author Zachary W
 * @date 2025/3/4
 */
public class FormatSuccessController extends StageDialogController<File> {

    @FXML
    public Label successLabel;
    @FXML
    public Text  pathText;
    @FXML
    public Label openFileLabel;
    @FXML
    public Label openDirLabel;

    public void setFormatFile(File formatFile) {
        File outputFile = new File(formatFile.getParent(), FileUtil.getPrefix(formatFile) + FileCons.TXT_OUTPUT_SUFFIX);
        // 成功消息
        successLabel.textProperty().bind(I18nUtil.createStringBinding("Dialog.FormatSuccess.Success"));
        // 文件路径
        pathText.setText(outputFile.getAbsolutePath());
        // 打开文件
        openFileLabel.textProperty().bind(I18nUtil.createStringBinding("Dialog.FormatSuccess.OpenFile"));
        openFileLabel.setCursor(Cursor.HAND);
        openFileLabel.setOnMouseClicked(event -> {
            FileUtil.open(outputFile);
            getStageDialog().close();
        });
        // 打开目录
        openDirLabel.textProperty().bind(I18nUtil.createStringBinding("Dialog.FormatSuccess.OpenDir"));
        openDirLabel.setCursor(Cursor.HAND);
        openDirLabel.setOnMouseClicked(event -> {
            FileUtil.openParentDirectory(outputFile);
            getStageDialog().close();
        });
    }

    @Override
    protected void setInitialResult(File initialResult) {
        super.setInitialResult(initialResult);
        setFormatFile(initialResult);
    }

}
