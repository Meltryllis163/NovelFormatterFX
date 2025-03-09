package cc.meltryllis.nf.ui.controller.input;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.FormatFactory;
import cc.meltryllis.nf.entity.property.input.ChapterLengthProperty;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
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
    public Tile maxLengthTile;

    @FXML
    public ComboBox<Integer> maxLengthComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChapterLengthProperty lengthProperty = InputFormatProperty.getInstance().getChapterLengthProperty();

        // Tile
        maxLengthTile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.Input.Chapter.MaxLength.Tile.Desc"));
        // 此处文本在语言变更时会发生错误，该错误由atlantafx产生，而且修复于以下request(https://github.com/mkpaz/atlantafx/pull/81)
        // 目前暂时不提供除简体中文以外的其他语言，因此不会产生问题，等待atlantafx更新
        maxLengthTile.titleProperty().bind(I18nUtil.createStringBinding("App.Input.Chapter.MaxLength.Tile.Title"));

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
