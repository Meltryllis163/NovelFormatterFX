package cc.meltryllis.nf.ui.controller.input;

import cc.meltryllis.nf.constants.FileCons;
import cc.meltryllis.nf.entity.property.InputFormatProperty;
import cc.meltryllis.nf.utils.FileUtil;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 小说文档导入控制器。
 *
 * @author Zachary W
 * @date 2025/2/21
 */
public class NovelSelectController implements Initializable {

    public static HBox box;
    @FXML
    public HBox root;
    @FXML
    public TextField filePathField;
    @FXML
    public Button browseButton;

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.titleProperty().bind(I18nUtil.createStringBinding("FileChooser.Extension.TXT.Title"));
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter(
                I18nUtil.get("FileChooser.Extension.TXT.Desc"), "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        setFile(file);
    }

    private boolean setFile(File file) {
        if (file != null && file.isFile() && FileCons.TXT_SUFFIX.equalsIgnoreCase(FileUtil.getSuffix(file))) {
            filePathField.setText(file.getAbsolutePath());
            InputFormatProperty.getInstance().getFileProperty().set(file);
            return true;
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        box = root;
        filePathField.promptTextProperty().bind(I18nUtil.createStringBinding(
                "App.Input.SelectNovel.Field.Prompt"));
        filePathField.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        filePathField.setOnDragDropped(event -> {
            for (File file : event.getDragboard().getFiles()) {
                if (setFile(file)) {
                    break;
                }
            }
            event.consume();
        });
    }
}
