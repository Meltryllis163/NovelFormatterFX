package cc.meltryllis.nf.ui;

import atlantafx.base.theme.PrimerLight;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
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
        stage.titleProperty().bind(I18nUtil.createStringBinding("App.Title"));
        Image icon = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/icons/icon.png")));
        stage.getIcons().add(icon);
        stage.setMinWidth(UICons.STAGE_MIN_WIDTH);
        stage.setMinHeight(UICons.STAGE_MIN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
