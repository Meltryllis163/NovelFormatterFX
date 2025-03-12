package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.constants.MyStyles;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.output.ReplacementProperty;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.ui.common.CustomTableView;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.TooltipUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * 导出文档时替换文本栏控制器。
 *
 * @author Zachary W
 * @date 2025/2/18
 */
@Slf4j
public class ReplaceOutputController implements Initializable {

    @FXML
    public Tile                                 replaceTile;
    @FXML
    public ToggleButton                         regexButton;
    @FXML
    public TextField                            targetField;
    @FXML
    public Label                                rightToLabel;
    @FXML
    public TextField                            replacementField;
    @FXML
    public CustomTableView<ReplacementProperty> replacementTableView;

    public void addListItem() {
        if (StrUtil.isEmpty(targetField.getText())) {
            return;
        }
        ReplacementProperty replacementProperty = new ReplacementProperty(targetField.getText(),
                regexButton.isSelected(), replacementField.getText());
        replacementTableView.getItems().add(replacementProperty);
    }

    public void selectAllListItems() {
        replacementTableView.getSelectionModel().selectAll();
    }

    public void deleteSelectedItems() {
        List<ReplacementProperty> selectedItems = replacementTableView.getSelectionModel().getSelectedItems().stream()
                .toList();
        for (ReplacementProperty selectedItem : selectedItems) {
            replacementTableView.getItems().remove(selectedItem);
        }
    }

    private void initReplaceTile() {
        replaceTile.titleProperty().bind(I18nUtil.createStringBinding("App.Output.Replace.Tile.Title"));
        replaceTile.descriptionProperty().bind(I18nUtil.createStringBinding("App.Output.Replace.Tile.Desc"));
    }

    private void initButtons() {
        regexButton.setCursor(Cursor.HAND);
        regexButton.setGraphic(new ImageView(
                new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/icons/regex.png")))));
        regexButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                targetField.setEditable(false);
                targetField.setCursor(Cursor.HAND);
                targetField.setOnMouseClicked(event -> {
                    DialogUtil.FXMLBuilder<String> builder = new DialogUtil.FXMLBuilder<>(
                            "/fxml/dialog/pattern-compile.fxml");
                    String pattern = builder.setTitle(I18nUtil.createStringBinding("Dialog.PatternCompile.Title"))
                            .setInitialValue(targetField.getText()).show();
                    targetField.setText(pattern);
                });
                targetField.getStyleClass().add(MyStyles.TEXT_FIELD_LIGHT_BLUE);
            } else {
                targetField.setEditable(true);
                targetField.setCursor(Cursor.DEFAULT);
                targetField.onMouseClickedProperty().setValue(null);
                targetField.getStyleClass().remove(MyStyles.TEXT_FIELD_LIGHT_BLUE);
            }
            TooltipUtil.show(regexButton, newValue ? "Dialog.Regex.Enabled" : "Dialog.Regex.Disabled",
                    TooltipUtil.Pos.BOTTOM);
        });
    }

    private void initTableView() {
        replacementTableView.setItems(OutputFormatProperty.getInstance()
                .getReplacementPropertyUniqueObservableList());

        // 无Items时提示
        Label placeholderLabel = new Label();
        placeholderLabel.textProperty()
                .bind(I18nUtil.createStringBinding("App.Output.Replace.TableView.PlaceholderLabel.Text"));
        replacementTableView.placeholderProperty().setValue(placeholderLabel);

        // 正则模式列
        TableColumn<ReplacementProperty, String> regexModeColumn = createRegexModeColumn();

        // 原文本列
        TableColumn<ReplacementProperty, String> oldTextColumn = createOldTextColumn();

        // 替换后文本
        TableColumn<ReplacementProperty, String> newTextColumn = new TableColumn<>();
        newTextColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Output.Replace.TableView.NewTextColumn.Title"));
        newTextColumn.setCellValueFactory(replacementStringCellDataFeatures -> new SimpleStringProperty(
                replacementStringCellDataFeatures.getValue().getNewText()));

        replacementTableView.getColumns().addAll(Arrays.asList(regexModeColumn, oldTextColumn, newTextColumn));

    }

    private TableColumn<ReplacementProperty, String> createRegexModeColumn() {
        TableColumn<ReplacementProperty, String> regexModeColumn = new TableColumn<>();
        regexModeColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Output.Replace.TableView.RegexModeColumn.Title"));
        regexModeColumn.setCellValueFactory(replacementStringCellDataFeatures -> Bindings.when(
                        replacementStringCellDataFeatures.getValue().getRegexModeProperty())
                .then(I18nUtil.createStringBinding("Common.Enabled"))
                .otherwise(I18nUtil.createStringBinding("Common.Disabled")));
        return regexModeColumn;
    }

    private TableColumn<ReplacementProperty, String> createOldTextColumn() {
        TableColumn<ReplacementProperty, String> oldTextColumn = new TableColumn<>();
        oldTextColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Output.Replace.TableView.OldTextColumn.Title"));
        oldTextColumn.setCellValueFactory(
                replacementStringCellDataFeatures -> replacementStringCellDataFeatures.getValue().getOldTextProperty());
        oldTextColumn.setCellFactory(new Callback<>() {
            Label label;

            @Override
            public TableCell<ReplacementProperty, String> call(
                    TableColumn<ReplacementProperty, String> replacementStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            label = new Label(item);
                            ReplacementProperty replacementProperty = getTableView().getItems().get(getIndex());
                            if (replacementProperty.isRegexMode()) {
                                label.setTextFill(UICons.DODER_BLUE);
                            }
                            setGraphic(label);
                        }
                    }
                };
            }
        });
        return oldTextColumn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initReplaceTile();
        initTableView();
        initButtons();
    }

}
