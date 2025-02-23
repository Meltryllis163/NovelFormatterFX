package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/21
 */
public class TabsController implements Initializable {

    public Tab mainTab;
    public Tab settingsTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainTab.textProperty().bind(I18nUtil.createStringBinding("App.Tabs.Main"));
        // settingsTab.textProperty().bind(I18nUtil.createStringBinding("App.Tabs.Settings"));
    }
}
