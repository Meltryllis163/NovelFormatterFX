package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.utils.message.NotificationUtil;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * {@link javafx.scene.control.TabPane} 下的格式化工具 {@code Tab}。
 *
 * @author Zachary W
 * @date 2025/3/1
 */
public class FormatterPaneController implements Initializable {

    public StackPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NotificationUtil.registerFormatterPane(root);
    }

}
