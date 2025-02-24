package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.constants.MyStyles;
import cc.meltryllis.nf.entity.Replacement;
import cc.meltryllis.nf.entity.property.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.UniqueListProperty;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.ui.common.FXMLListCell;
import cc.meltryllis.nf.utils.I18nUtil;
import cn.hutool.core.util.StrUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * 导出文档时替换文本栏控制器。
 *
 * @author Zachary W
 * @date 2025/2/18
 */
public class ReplaceOutputController implements Initializable {

    @FXML
    public Tile replaceTile;
    @FXML
    public ToggleButton regexButton;
    @FXML
    public TextField targetField;
    @FXML
    public Label rightToLabel;
    @FXML
    public TextField replacementField;
    @FXML
    public ListView<Replacement> listView;

    public void addListItem() {
        if (StrUtil.isEmpty(targetField.getText())) {
            return;
        }
        Replacement replacement = new Replacement(targetField.getText(), regexButton.isSelected(),
                replacementField.getText());
        OutputFormatProperty.getInstance().getReplacementUniqueListProperty().add(replacement);
    }

    public void selectAllListItems() {
        UniqueListProperty<Replacement> uniqueListProperty = OutputFormatProperty.getInstance()
                .getReplacementUniqueListProperty();
        for (Replacement replacement : uniqueListProperty.getValue()) {
            replacement.setSelected(true);
        }
    }

    public void deleteSelectedItems() {
        UniqueListProperty<Replacement> uniqueListProperty = OutputFormatProperty.getInstance()
                .getReplacementUniqueListProperty();
        Iterator<Replacement> iterator = uniqueListProperty.iterator();
        while (iterator.hasNext()) {
            Replacement replacement = iterator.next();
            if (replacement.isSelected()) {
                iterator.remove();
            }
        }
    }

    private void initReplaceTile() {
        replaceTile.titleProperty().bind(I18nUtil.createStringBinding("App.OutputConfiguration.Replace.Tile.Title"));
        replaceTile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.OutputConfiguration.Replace.Tile.Desc"));
    }

    private void initReplacementList() {
        regexButton.setCursor(Cursor.HAND);
        regexButton.setGraphic(new ImageView(new Image(
                Objects.requireNonNull(MainApplication.class.getResourceAsStream("/icons/regex.png")))));
        regexButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                targetField.getStyleClass().add(MyStyles.TEXT_FIELD_LIGHT_BLUE);
            } else {
                targetField.getStyleClass().remove(MyStyles.TEXT_FIELD_LIGHT_BLUE);
            }
        });
        Label placeholderLabel = new Label();
        placeholderLabel.textProperty()
                .bind(I18nUtil.createStringBinding("App.OutputConfiguration.Replace.ListView.PlaceholderLabel.Text"));
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.placeholderProperty().setValue(placeholderLabel);
        listView.itemsProperty().bind(OutputFormatProperty.getInstance().getReplacementUniqueListProperty());
        listView.setCellFactory(param -> new FXMLListCell<>("/fxml/output/replace-list-cell.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initReplaceTile();
        initReplacementList();
    }
}
