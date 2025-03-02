package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.constants.MyStyles;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.entity.Replacement;
import cc.meltryllis.nf.entity.property.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.UniqueObservableList;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.utils.I18nUtil;
import cc.meltryllis.nf.utils.StrUtil;
import cc.meltryllis.nf.utils.message.TooltipUtil;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

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
public class ReplaceOutputController implements Initializable {

    @FXML
    public Tile                   replaceTile;
    @FXML
    public Button deleteButton;
    @FXML
    public ToggleButton           regexButton;
    @FXML
    public TextField              targetField;
    @FXML
    public Label                  rightToLabel;
    @FXML
    public TextField              replacementField;
    @FXML
    public TableView<Replacement> tableView;

    private UniqueObservableList<Replacement> replacementUniqueObservableList;

    public void addListItem() {
        if (StrUtil.isEmpty(targetField.getText())) {
            return;
        }
        Replacement replacement = new Replacement(targetField.getText(), regexButton.isSelected(),
                replacementField.getText());
        OutputFormatProperty.getInstance().getReplacementUniqueObservableList().add(replacement);
    }

    public void selectAllListItems() {
        tableView.getSelectionModel().selectAll();
    }

    public void deleteSelectedItems() {
        List<Replacement> selectedItems = tableView.getSelectionModel().getSelectedItems().stream().toList();
        for (Replacement selectedItem : selectedItems) {
            replacementUniqueObservableList.remove(selectedItem);
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
                targetField.getStyleClass().add(MyStyles.TEXT_FIELD_LIGHT_BLUE);
            } else {
                targetField.getStyleClass().remove(MyStyles.TEXT_FIELD_LIGHT_BLUE);
            }
            TooltipUtil.show(regexButton, newValue ? "Message.RegexEnabled" : "Message.RegexDisabled",
                    TooltipUtil.Pos.BOTTOM);
        });
    }

    private void initTableView() {
        replacementUniqueObservableList = OutputFormatProperty.getInstance().getReplacementUniqueObservableList();
        replacementUniqueObservableList.asItems(tableView);

        // 表格基本设置
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // 无Items时提示
        Label placeholderLabel = new Label();
        placeholderLabel.textProperty()
                .bind(I18nUtil.createStringBinding("App.Output.Replace.TableView.PlaceholderLabel.Text"));
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.placeholderProperty().setValue(placeholderLabel);

        // 正则模式列
        TableColumn<Replacement, String> regexModeColumn = createRegexModeColumn();

        // 原文本列
        TableColumn<Replacement, String> oldTextColumn = createOldTextColumn();

        // 替换后文本
        TableColumn<Replacement, String> newTextColumn = new TableColumn<>();
        newTextColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Output.Replace.TableView.NewTextColumn.Title"));
        newTextColumn.setCellValueFactory(replacementStringCellDataFeatures -> new SimpleStringProperty(
                replacementStringCellDataFeatures.getValue().getNewText()));

        tableView.getColumns().addAll(Arrays.asList(regexModeColumn, oldTextColumn, newTextColumn));

    }

    private TableColumn<Replacement, String> createRegexModeColumn() {
        TableColumn<Replacement, String> regexModeColumn = new TableColumn<>();
        regexModeColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Output.Replace.TableView.RegexModeColumn.Title"));
        regexModeColumn.setCellValueFactory(replacementStringCellDataFeatures -> {
            StringBinding binding = replacementStringCellDataFeatures.getValue()
                    .isRegexMode() ? I18nUtil.createStringBinding("Common.Enabled") : I18nUtil.createStringBinding(
                    "Common.Disabled");
            SimpleStringProperty property = new SimpleStringProperty();
            property.bind(binding);
            return property;
        });
        return regexModeColumn;
    }

    private TableColumn<Replacement, String> createOldTextColumn() {
        TableColumn<Replacement, String> oldTextColumn = new TableColumn<>();
        oldTextColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Output.Replace.TableView.OldTextColumn.Title"));
        oldTextColumn.setCellValueFactory(replacementStringCellDataFeatures -> new SimpleStringProperty(
                replacementStringCellDataFeatures.getValue().getOldText()));
        oldTextColumn.setCellFactory(new Callback<>() {

            Label label;

            @Override
            public TableCell<Replacement, String> call(TableColumn<Replacement, String> replacementStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            label = new Label(item);
                            Replacement replacementItem = getTableView().getItems().get(getIndex());
                            if (replacementItem.isRegexMode()) {
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
