package cc.meltryllis.nf.ui;

import atlantafx.base.theme.PrimerLight;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.AlertUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * 程序主入口。
 *
 * @author Zachary W
 * @date 2025/2/10
 */
public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/main-application.fxml"));
        Parent root = fxmlLoader.load();
        // 嵌入全局css表
        root.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("/fxml/css/common.css"))
                .toExternalForm());
        Scene scene = new Scene(root, UICons.PREF_WIDTH, UICons.PREF_HEIGHT);
        initLanguageMnemonic(scene);
        stage.titleProperty().bind(I18nUtil.createStringBinding("App.Title"));
        Image icon = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/icons/icon.png")));
        stage.getIcons().add(icon);
        stage.setMinWidth(UICons.STAGE_MIN_WIDTH);
        stage.setMinHeight(UICons.STAGE_MIN_HEIGHT);
        stage.setScene(scene);

        AlertUtil.registerOwner(scene.getWindow());
        DialogUtil.registerOwner(scene.getWindow());
        // 退出事件
        stage.setOnCloseRequest(windowEvent -> {
            InputFormatProperty.store();
            OutputFormatProperty.store();
        });
        stage.show();
    }

    private void initLanguageMnemonic(Scene scene) {
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
        scene.addMnemonic(changeLanguageMnemonic);
    }

    public static void main(String[] args) {
        launch();
    }

}
