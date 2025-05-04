package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.constants.DataCons;
import cc.meltryllis.nf.constants.MyStyles;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.entity.property.HistoryProperty;
import cc.meltryllis.nf.entity.property.UniqueObservableList;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.utils.FXUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 菜单栏控制器。
 *
 * @author Zachary W
 * @date 2025/2/10
 */
@Slf4j
public class MenuBarController implements Initializable {

    /**
     * 一个将文件路径字符串与 {@code MenuItem} 关联在一起的哈希表。
     * 主要用来查询某个文件是否在历史记录中。
     */
    private final HashMap<String, MenuItem> filePathMap = new HashMap<>();
    @FXML
    public MenuBar  menuBar;
    @FXML
    public Menu     fileMenu;
    @FXML
    public Menu     historyMenu;
    @FXML
    public MenuItem clearAllItem;
    @FXML
    public MenuItem exitItem;
    @FXML
    public MenuItem aboutItem;
    @FXML
    public Menu     helpMenu;

    public void exit() {
        Window window = FXUtil.getWindow(menuBar);
        if (window instanceof Stage stage) {
            stage.close();
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
        githubValue.getStyleClass().add(MyStyles.HYPER_LINK);
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

    private void initHistoryMenu() {
        historyMenu.textProperty().bind(I18nUtil.createStringBinding("App.Menu.File.History"));
        clearAllItem.textProperty().bind(I18nUtil.createStringBinding("App.Menu.File.History.ClearAll"));

        UniqueObservableList<File> inputFileHistoryList = HistoryProperty.getInstance().getInputFileHistoryList();
        inputFileHistoryList.addListener((ListChangeListener<File>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    System.out.println(c.wasAdded());
                    int from = c.getFrom();
                    int to = c.getTo();
                    for (int i = from; i < to; i++) {
                        System.out.println(i);
                    }
                    removeHistory(c.getFrom());
                }
                if (c.wasAdded()) {
                    List<? extends File> addedSubList = c.getAddedSubList();
                    for (int i = 0; i < addedSubList.size(); i++) {
                        addHistory(c.getFrom() + i, addedSubList.get(i));
                    }
                }
            }
        });
        for (File file : inputFileHistoryList) {
            addHistory(historyMenu.getItems().size() - 2, file);
        }
    }

    private void addHistory(int index, File file) {
        if (file == null) {
            return;
        }
        String path = file.getPath();
        MenuItem item;
        if (filePathMap.containsKey(path)) {
            item = filePathMap.get(path);
        } else {
            item = new MenuItem(path);
            item.setOnAction(event -> {
                if (!InputFormatProperty.getInstance().setFile(file)) {
                    DialogUtil.showMessage(I18nUtil.createStringBinding("Dialog.FileNotFound.Title"),
                            I18nUtil.createStringBinding("Dialog.FileNotFound.Desc"), DialogUtil.Type.WARNING);
                    historyMenu.getItems().remove(item);
                }
            });
            filePathMap.put(path, item);
        }
        historyMenu.getItems().add(index, item);
    }

    private void removeHistory(int index) {
        historyMenu.getItems().remove(index);
    }

    @FXML
    private void clearHistory() {
        HistoryProperty.getInstance().getInputFileHistoryList().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileMenu.textProperty().bind(I18nUtil.createStringBinding("App.Menu.File"));
        initHistoryMenu();
        exitItem.textProperty().bind(I18nUtil.createStringBinding("App.Menu.File.Exit"));
        helpMenu.textProperty().bind(I18nUtil.createStringBinding("App.Menu.Help"));
        aboutItem.textProperty().bind(I18nUtil.createStringBinding("App.Menu.Help.About"));
    }

}