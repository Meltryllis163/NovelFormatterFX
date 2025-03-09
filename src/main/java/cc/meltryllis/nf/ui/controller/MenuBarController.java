package cc.meltryllis.nf.ui.controller;

import atlantafx.base.theme.Styles;
import cc.meltryllis.nf.constants.DataCons;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 菜单栏控制器。
 *
 * @author Zachary W
 * @date 2025/2/10
 */
@Slf4j
public class MenuBarController implements Initializable {

    @FXML
    MenuBar menuBar;
    @FXML
    public Menu helpMenu;
    @FXML
    MenuItem exitItem;
    @FXML
    public MenuItem aboutItem;
    @FXML
    Menu fileMenu;

    public void exit() {
        if (menuBar != null) {
            Window window = menuBar.getScene().getWindow();
            if (window instanceof Stage stage) {
                stage.close();
            }
        }
    }

    public void showAboutDialog() {
        // 外部容器
        HBox box = new HBox(60);

        // 版本
        Label versionKey = new Label(null, new FontIcon("fth-alert-circle"));
        versionKey.textProperty().bind(I18nUtil.createStringBinding("Dialog.About.Version.Key"));
        HBox.setHgrow(versionKey, Priority.ALWAYS);
        Label versionValue = new Label(DataCons.VERSION);
        // Github
        Label githubKey = new Label(null, new FontIcon("fth-github"));
        githubKey.textProperty().bind(I18nUtil.createStringBinding("Dialog.About.Github.Key"));
        HBox.setHgrow(versionKey, Priority.ALWAYS);
        Label githubValue = new Label();
        githubValue.textProperty().bind(I18nUtil.createStringBinding("Dialog.About.Github.Value"));
        githubValue.getStyleClass().addAll(Styles.ACCENT, Styles.TEXT_UNDERLINED);
        githubValue.setCursor(Cursor.HAND);
        githubValue.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(URI.create(DataCons.GITHUB));
            } catch (IOException e) {
                log.debug("Browse website failed.", e);
            }
        });
        // 添加Key标签
        VBox keyBox = new VBox(UICons.BIG_SPACING);
        keyBox.setAlignment(Pos.CENTER_LEFT);
        keyBox.getChildren().addAll(versionKey, githubKey);
        // 添加Value标签
        VBox valueBox = new VBox(UICons.BIG_SPACING);
        valueBox.getChildren().addAll(versionValue, githubValue);
        valueBox.setAlignment(Pos.CENTER_RIGHT);

        box.getChildren().addAll(keyBox, valueBox);
        DialogUtil.show(I18nUtil.createStringBinding("App.Title"), box);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileMenu.textProperty().bind(I18nUtil.createStringBinding("Common.File"));
        exitItem.textProperty().bind(I18nUtil.createStringBinding("Common.Exit"));
        helpMenu.textProperty().bind(I18nUtil.createStringBinding("Common.Help"));
        aboutItem.textProperty().bind(I18nUtil.createStringBinding("Common.About"));
    }
}