package cc.meltryllis.nf.ui.controller.input;

import cc.meltryllis.nf.entity.FormatFactory;
import cc.meltryllis.nf.entity.property.input.ChapterLengthProperty;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.ui.controls.FormField;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 章节最大长度控制器。
 *
 * @author Zachary W
 * @date 2025/2/21
 */
public class ChapterMaxLengthController implements Initializable {

    @FXML
    private FormField         root;
    @FXML
    private ComboBox<Integer> maxLengthComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChapterLengthProperty lengthProperty = InputFormatProperty.getInstance().getChapterLengthProperty();

        root.titleProperty().bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.MaxLength.Header.Title"));
        root.descriptionProperty().bind(I18nUtil.createStringBinding(
                "App.Formatter.Input.Chapter.MaxLength.Header.Desc"));

        // ComboBox
        maxLengthComboBox.getItems().addAll(FormatFactory.createDefaultChapterMaxLengthList());
        int selectedMaxLength = lengthProperty.getMaxLength();
        lengthProperty.getMaxLengthProperty().bind(maxLengthComboBox.valueProperty());
        if (selectedMaxLength > 0 && !maxLengthComboBox.getItems().contains(selectedMaxLength)) {
            maxLengthComboBox.getItems().add(selectedMaxLength);
            maxLengthComboBox.setValue(selectedMaxLength);
        } else {
            maxLengthComboBox.getSelectionModel().selectFirst();
        }
        maxLengthComboBox.getItems().sort(Integer::compareTo);
    }

}
