package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 菜单栏控制器。
 *
 * @author Zachary W
 * @date 2025/2/10
 */
public class MenuBarController implements Initializable {

    @FXML
    MenuBar menuBar;

    @FXML
    Menu fileMenu;

    @FXML
    Menu languageMenu;

    @FXML
    MenuItem exitItem;

    public void exit() {
        if (menuBar != null) {
            Window window = menuBar.getScene().getWindow();
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileMenu.textProperty().bind(I18nUtil.createStringBinding("Common.File"));
        languageMenu.textProperty().bind(I18nUtil.createStringBinding("Common.Language"));
        for (Locale supportedLocale : I18nUtil.getSupportedLocales()) {
            MenuItem item = new MenuItem(supportedLocale.getDisplayName(supportedLocale));
            item.setOnAction(actionEvent -> I18nUtil.setLocale(supportedLocale));
            languageMenu.getItems().add(item);
        }
        exitItem.textProperty().bind(I18nUtil.createStringBinding("Common.Exit"));
    }

}
