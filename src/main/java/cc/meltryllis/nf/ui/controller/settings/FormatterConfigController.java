package cc.meltryllis.nf.ui.controller.settings;

import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 「设置-格式化工具」控制器。
 *
 * @author Zachary W
 * @date 2025/3/11
 */
public class FormatterConfigController implements Initializable {

    @FXML
    public Label formatterConfigLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formatterConfigLabel.textProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Label.Text"));
    }
}
