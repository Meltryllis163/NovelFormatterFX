package cc.meltryllis.nf.ui.controller.output;

import cc.meltryllis.nf.constants.ColorCons;
import cc.meltryllis.nf.constants.MyStyles;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.output.ReplacementProperty;
import cc.meltryllis.nf.ui.controls.FormField;
import cc.meltryllis.nf.ui.controls.MTableView;
import cc.meltryllis.nf.ui.controls.PopupTip;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
    public  ToggleButton                    regexButton;
    @FXML
    public  TextField                       targetField;
    @FXML
    public  Label                           rightToLabel;
    @FXML
    public  TextField                       replacementField;
    @FXML
    public  MTableView<ReplacementProperty> replacementTableView;
    @FXML
    private FormField                       root;

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

    private void initHeader() {
        root.titleProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.Replace.Header.Title"));
        root.descriptionProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.Replace.Header.Desc"));
    }

    private void initButtons() {
        // regexButton.setCursor(Cursor.HAND);
        // ImageView blackRegex = new ImageView(
        //         new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/icons/regex-black.png"))));
        // ImageView whiteRegex = new ImageView(
        //         new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/icons/regex-white.png"))));
        // regexButton.setGraphic(blackRegex);
        // regexButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
        //     @Override
        //     public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        //
        //     }
        // });
        regexButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            targetField.pseudoClassStateChanged(PseudoClass.getPseudoClass(MyStyles.Replace.PATTERN), newValue);
            if (newValue) {
                targetField.setEditable(false);
                targetField.setOnMouseClicked(event -> {
                    DialogUtil.DialogBuilder<String> builder = new DialogUtil.DialogBuilder<>(
                            root.getScene().getWindow(), "/fxml/dialog/pattern-compile.fxml");
                    String pattern = builder.title(I18nUtil.createStringBinding("Dialog.PatternCompile.Title"))
                            .initialValue(targetField.getText()).okButton(true).show();
                    targetField.setText(pattern);
                });
            } else {
                targetField.setEditable(true);
                targetField.onMouseClickedProperty().setValue(null);
            }
            // TODO show tooltip
            PopupTip regexTip = PopupTip.builder(regexButton, I18nUtil.createStringBinding(
                            newValue ? "Dialog.ToggleRegexMode.Enabled" : "Dialog.ToggleRegexMode.Disabled"))
                    .position(Pos.BOTTOM_CENTER).hideDelay(2000).build();
            regexButton.setOnMouseClicked(event -> regexTip.ownerShow());
        });
    }

    private void initTableView() {
        replacementTableView.setItems(OutputFormatProperty.getInstance().getReplacementPropertyUniqueObservableList());
        replacementTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // 无Items时提示
        Label placeholderLabel = new Label();
        placeholderLabel.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Output.Replace.TableView.PlaceholderLabel.Text"));
        replacementTableView.placeholderProperty().setValue(placeholderLabel);

        // 正则模式列
        TableColumn<ReplacementProperty, String> regexModeColumn = createRegexModeColumn();

        // 原文本列
        TableColumn<ReplacementProperty, String> oldTextColumn = createOldTextColumn();

        // 替换后文本
        TableColumn<ReplacementProperty, String> newTextColumn = new TableColumn<>();
        newTextColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Output.Replace.TableView.NewTextColumn.Title"));
        newTextColumn.setCellValueFactory(replacementStringCellDataFeatures -> new SimpleStringProperty(
                replacementStringCellDataFeatures.getValue().getNewText()));

        replacementTableView.getColumns().addAll(Arrays.asList(regexModeColumn, oldTextColumn, newTextColumn));

    }

    private TableColumn<ReplacementProperty, String> createRegexModeColumn() {
        TableColumn<ReplacementProperty, String> regexModeColumn = new TableColumn<>();
        regexModeColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Output.Replace.TableView.RegexModeColumn.Title"));
        regexModeColumn.setCellValueFactory(replacementStringCellDataFeatures -> Bindings.when(
                        replacementStringCellDataFeatures.getValue().getRegexModeProperty())
                .then(I18nUtil.createStringBinding("Common.Enabled"))
                .otherwise(I18nUtil.createStringBinding("Common.Disabled")));
        return regexModeColumn;
    }

    private TableColumn<ReplacementProperty, String> createOldTextColumn() {
        TableColumn<ReplacementProperty, String> oldTextColumn = new TableColumn<>();
        oldTextColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Output.Replace.TableView.OldTextColumn.Title"));
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
                                label.setTextFill(ColorCons.DODGER_BLUE);
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
        initHeader();
        initTableView();
        initButtons();
    }

}
