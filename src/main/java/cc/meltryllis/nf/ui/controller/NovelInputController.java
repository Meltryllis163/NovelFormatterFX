package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.config.InputFormat;
import cc.meltryllis.nf.constants.FileCons;
import cc.meltryllis.nf.utils.I18nUtil;
import cn.hutool.core.io.FileUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/10
 */
public class NovelInputController implements Initializable {

    @FXML
    public VBox root;
    @FXML
    public Label filePathLabel;
    @FXML
    public TextField filePathField;
    @FXML
    public Label importConfigLabel;
    @FXML
    Button browseButton;

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18nUtil.get("FileChooser.Extension.TXT.Title"));
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter(
                I18nUtil.get("FileChooser.Extension.TXT.Desc"), "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null && file.isFile() && FileCons.TXT_SUFFIX.equalsIgnoreCase(FileUtil.getSuffix(file))) {
            filePathField.setText(file.getAbsolutePath());
            InputFormat.getInstance().setFile(file);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        importConfigLabel.textProperty().bind(I18nUtil.createStringBinding("App.NovelImportConfiguration"));
//        filePathLabel.textProperty().bind(I18nUtil.createStringBinding("App.SelectFile.Label.text"));
        filePathField.promptTextProperty().bind(I18nUtil.createStringBinding("App.SelectFile.Field.Prompt"));
        filePathField.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        filePathField.setOnDragDropped(event -> {
            for (File file : event.getDragboard().getFiles()) {
                if (FileCons.TXT_SUFFIX.equalsIgnoreCase(FileUtil.getSuffix(file))) {
                    filePathField.setText(file.getAbsolutePath());
                    InputFormat.getInstance().setFile(file);
                    break;
                }
            }
            event.consume();
        });
    }

}
