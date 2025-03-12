package cc.meltryllis.nf.ui.controller.output;

import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 「段落」相关配置项总控制器。
 *
 * @author Zachary W
 * @date 2025/3/4
 */
public class ParagraphController implements Initializable {

    public Label paragraphLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paragraphLabel.textProperty().bind(I18nUtil.createStringBinding("App.Output.Paragraph.Label.Text"));
    }
}
