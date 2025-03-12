package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/11
 */
public class ChapterInputFormatController implements Initializable {
    @FXML
    Label chapterInputFormatTitleLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chapterInputFormatTitleLabel.textProperty().bind(I18nUtil.createStringBinding("App.Input.Chapter.Label.Text"));
    }

}
