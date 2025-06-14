package cc.meltryllis.nf.ui;

import atlantafx.base.theme.PrimerLight;
import cc.meltryllis.nf.ui.stage.MainStage;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 程序主入口。
 *
 * @author Zachary W
 * @date 2025/2/10
 */
@Slf4j
public class MainApplication extends Application {

    public static final String INDEX_CSS = Objects.requireNonNull(MainApplication.class.getResource("/css/index.css"))
            .toExternalForm();

    public static Window OWNER;

    @Override
    public void start(Stage ignored) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        MainStage stage = new MainStage();
        OWNER = stage;
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
