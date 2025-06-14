package cc.meltryllis.nf.ui.controller.input;

import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.ui.controls.MTextField;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 小说文档导入控制器。
 *
 * @author Zachary W
 * @date 2025/2/21
 */
@Slf4j
public class NovelSelectController implements Initializable {

    @FXML
    public VBox       root;
    @FXML
    public Label      selectFileLabel;
    @FXML
    public MTextField filePathField;
    @FXML
    public Button     browseButton;

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.titleProperty().bind(I18nUtil.createStringBinding("FileChooser.Extension.TXT.Title"));
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter(
                I18nUtil.get("FileChooser.Extension.TXT.Desc"), "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        InputFormatProperty.getInstance().setFile(file);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectFileLabel.textProperty().bind(I18nUtil.createStringBinding("App.Formatter.Input.SelectNovel.Label.Text"));
        filePathField.promptTextProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Input.SelectNovel.Field.Prompt"));
        SimpleObjectProperty<File> fileProperty = InputFormatProperty.getInstance().getFileProperty();
        filePathField.textProperty().bind(Bindings.createStringBinding(() -> {
            File file = fileProperty.getValue();
            return file == null ? null : file.getAbsolutePath();
        }, fileProperty));
        filePathField.setCursor(Cursor.DEFAULT);
    }

}
