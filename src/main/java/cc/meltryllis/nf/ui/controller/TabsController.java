package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.constants.DataCons;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.entity.property.HistoryProperty;
import cc.meltryllis.nf.entity.property.UniqueObservableList;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.ui.controls.Message;
import cc.meltryllis.nf.ui.controls.PopupTip;
import cc.meltryllis.nf.ui.controls.outline.OutlinePane;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/5/9
 */
@Slf4j
public class TabsController implements Initializable {

    @FXML
    private StackPane    root;
    @FXML
    private VBox         navigationPane;
    @FXML
    private ToggleButton formatterButton;
    @FXML
    private ToggleButton settingsButton;
    @FXML
    private MenuButton   historyMenuButton;
    @FXML
    private OutlinePane  settingsPane;
    @FXML
    private VBox         formatterPane;
    @FXML
    private Label        versionLabel;

    private void initNavigationPane() {
        navigationPane.setMinWidth(UICons.NAVIGATION_PANE_WIDTH);
        navigationPane.setMaxWidth(UICons.NAVIGATION_PANE_WIDTH);
    }

    private void initNavigationButtons() {
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(formatterButton, settingsButton);
        // 至少有一个菜单项被选中
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                group.selectToggle(oldValue);
            }
        });
        formatterButton.setSelected(true);
        PopupTip.builder(formatterButton, I18nUtil.createStringBinding("App.Navigation.FormatterButton.Tooltip"))
                .position(Pos.CENTER_RIGHT).showDelay(800).install();
        PopupTip.builder(settingsButton, I18nUtil.createStringBinding("App.Navigation.SettingsButton.Tooltip"))
                .position(Pos.CENTER_RIGHT).showDelay(800).install();
    }

    private void initBottomTools() {
        historyMenuButton.setPopupSide(Side.RIGHT);
        PopupTip.builder(historyMenuButton, I18nUtil.createStringBinding("App.Navigation.History.Tooltip"))
                .position(Pos.CENTER_RIGHT).showDelay(800).install();

        UniqueObservableList<File> inputFileHistoryList = HistoryProperty.getInstance().getInputFileHistoryList();
        updateHistoryMenuButtonItems();
        inputFileHistoryList.addListener((ListChangeListener<File>) c -> updateHistoryMenuButtonItems());

        versionLabel.setText("v" + DataCons.VERSION);
        versionLabel.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(URI.create(DataCons.GITHUB));
            } catch (IOException e) {
                log.debug("Browse website failed.", e);
            }
        });
    }

    private void updateHistoryMenuButtonItems() {
        historyMenuButton.getItems().clear();
        UniqueObservableList<File> inputFileHistoryList = HistoryProperty.getInstance().getInputFileHistoryList();
        for (File file : inputFileHistoryList) {
            MenuItem item = createHistoryMenuItem(file);
            historyMenuButton.getItems().add(item);
        }
        historyMenuButton.getItems().add(new SeparatorMenuItem());
        MenuItem clearAllItem = new MenuItem();
        clearAllItem.textProperty().bind(I18nUtil.createStringBinding("App.Navigation.History.ClearAll"));
        clearAllItem.setOnAction(event -> inputFileHistoryList.clear());
        historyMenuButton.getItems().add(clearAllItem);
    }

    private MenuItem createHistoryMenuItem(@NotNull File file) {
        MenuItem item = new MenuItem(file.getPath());
        item.setOnAction(event -> {
            if (!InputFormatProperty.getInstance().setFile(file)) {
                DialogUtil.showMessage(MainApplication.OWNER, Message.Type.WARNING,
                        I18nUtil.createStringBinding("Dialog.FileNotFound.Title"),
                        I18nUtil.createStringBinding("Dialog.FileNotFound.Desc"));
                historyMenuButton.getItems().remove(item);
            }
        });
        return item;
    }

    private void initPanes() {
        settingsPane.visibleProperty().bind(settingsButton.selectedProperty());
        formatterPane.visibleProperty().bind(formatterButton.selectedProperty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initNavigationPane();
        initNavigationButtons();
        initBottomTools();
        initPanes();
    }

}
