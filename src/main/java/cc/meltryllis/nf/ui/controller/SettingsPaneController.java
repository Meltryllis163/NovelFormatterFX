package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/26
 */
public class SettingsPaneController implements Initializable {

    @FXML
    public Label appConfigLabel;
    @FXML
    public Label novelConfigLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appConfigLabel.textProperty().bind(I18nUtil.createStringBinding("App.Settings.AppConfig.Label.Text"));
        novelConfigLabel.textProperty().bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Label.Text"));
    }
}
