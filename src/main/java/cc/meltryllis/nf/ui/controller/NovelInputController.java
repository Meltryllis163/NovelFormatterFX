package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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
    public Label inputFormatLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputFormatLabel.textProperty().bind(I18nUtil.createStringBinding("App.Input.Label.Text"));
//        filePathLabel.textProperty().bind(I18nUtil.createStringBinding("App.SelectFile.Label.text"));

    }

}
