package cc.meltryllis.nf.ui.controller.input;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.property.InputFormatProperty;
import cc.meltryllis.nf.entity.property.UniqueObservableList;
import cc.meltryllis.nf.utils.I18nUtil;
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
    public Tile tile;

    @FXML
    public ComboBox<Integer> comboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InputFormatProperty inputFormatProperty = InputFormatProperty.getInstance();

        // Tile
        tile.descriptionProperty().bind(I18nUtil.createStringBinding(
                "App.Input.ChapterInput.MaxLengthTile.Desc"));
        // 此处文本在语言变更时会发生错误，该错误由atlantafx产生，而且修复于以下request(https://github.com/mkpaz/atlantafx/pull/81)
        // 目前暂时不提供除简体中文以外的其他语言，因此不会产生问题，等待atlantafx更新
        tile.titleProperty().bind(I18nUtil.createStringBinding(
                "App.Input.ChapterInput.MaxLengthTile.Title"));

        // ComboBox
        UniqueObservableList<Integer> itemUniqueObservableList = InputFormatProperty.getInstance()
                .getChapterMaxLengthUniqueObservableList();
        itemUniqueObservableList.asItems(comboBox);
        int selectedMaxLength = inputFormatProperty.getChapterMaxLengthProperty().getValue();
        inputFormatProperty.getChapterMaxLengthProperty().bind(comboBox.valueProperty());
        if (selectedMaxLength > 0) {
            itemUniqueObservableList.add(selectedMaxLength);
            comboBox.setValue(selectedMaxLength);
        } else {
            comboBox.getSelectionModel().selectFirst();
        }
    }
}
