package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/21
 */
public class TabsController implements Initializable {

    public static final String MAIN_TAB     = "MainTab";
    public static final String SETTINGS_TAB = "SettingsTab";

    @Getter
    public static TabPane instance;

    @FXML
    public TabPane root;
    @FXML
    public Tab     mainTab;
    @FXML
    public Tab     settingsTab;

    public static TabPane getTabPane() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TabsController.instance = root;
        mainTab.textProperty().bind(I18nUtil.createStringBinding("App.Tabs.Main"));
        mainTab.setId(MAIN_TAB);
        settingsTab.textProperty().bind(I18nUtil.createStringBinding("App.Tabs.Settings"));
        settingsTab.setId(SETTINGS_TAB);
    }

}
