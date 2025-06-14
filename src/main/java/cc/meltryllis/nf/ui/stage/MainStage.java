package cc.meltryllis.nf.ui.stage;

import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.entity.property.HistoryProperty;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.ui.controller.TitleBarController;
import cc.meltryllis.nf.utils.FXUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;

import java.io.IOException;
import java.util.Locale;

/**
 * 程序主窗口。
 *
 * @author Zachary W
 * @date 2025/6/9
 */
public class MainStage extends CustomStage {

    public MainStage() {
        initialize();
    }

    public void initialize() {
        try {
            FXMLLoader tabsLoader = FXUtil.newFXMLLoader("/fxml/tabs.fxml");
            initialize(tabsLoader.load(), UICons.PREF_WIDTH, UICons.PREF_HEIGHT);
            getScene().getRoot().getStyleClass().add("main-stage");
            setMinWidth(UICons.MIN_WIDTH);
            setMinHeight(UICons.MIN_HEIGHT);

            initTitleBar();
            initLanguageMnemonic();

            // 退出事件
            // stage.setOnCloseRequest(windowEvent -> {
            // 上方事件在通过stage.close()关闭程序时不会触发。参考https://stackoverflow.com/questions/48689985/javafx-stage-close-not-calling-my-oncloserequest-handler。
            setOnHidden(event -> {
                InputFormatProperty.store();
                OutputFormatProperty.store();
                HistoryProperty.store();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initTitleBar() {
        TitleBarController controller = getTitleBarController();
        // controller.setLogoSlotWidth(UICons.NAVIGATION_PANE_WIDTH);
        controller.getTitle().textProperty().bind(I18nUtil.createStringBinding("App.Title"));
        controller.getTitleSlot().setAlignment(Pos.CENTER);
    }

    /**
     * Debug.
     * 初始化切换语言快捷键。
     */
    private void initLanguageMnemonic() {
        KeyCombination changeLanguage = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN,
                KeyCombination.SHIFT_DOWN, KeyCombination.ALT_DOWN);
        Button changeLanguageButton = new Button();
        changeLanguageButton.setOnAction(event -> {
            if (I18nUtil.getLocale().equals(Locale.SIMPLIFIED_CHINESE)) {
                I18nUtil.setLocale(Locale.ENGLISH);
            } else {
                I18nUtil.setLocale(Locale.SIMPLIFIED_CHINESE);
            }
        });
        Mnemonic changeLanguageMnemonic = new Mnemonic(changeLanguageButton, changeLanguage);
        getScene().addMnemonic(changeLanguageMnemonic);
    }

}
