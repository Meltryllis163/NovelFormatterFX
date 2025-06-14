package cc.meltryllis.nf.ui.controller.input;

import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.input.RegexProperty;
import cc.meltryllis.nf.ui.controls.FormField;
import cc.meltryllis.nf.ui.controls.MTableView;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 章节正则解析表格控制器。
 *
 * @author Zachary W
 * @date 2025/2/26
 */
@Slf4j
public class ChapterRegexController implements Initializable {

    @FXML
    public  Button    selectAllButton, addButton, editButton, deleteButton;
    @FXML
    private FormField root;
    @FXML
    private MTableView<RegexProperty> chapterRegexTableView;

    private void initHeader() {
        root.titleProperty().bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.Regex.Header.Title"));
        root.descriptionProperty().bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.Regex.Header.Desc"));
    }

    private void initTableView() {
        chapterRegexTableView.setItems(
                InputFormatProperty.getInstance().getChapterRegexProperty().getRegexPropertyUniqueObservableList());
        chapterRegexTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // 「启用正则」列
        TableColumn<RegexProperty, Boolean> enableColumn = createEnableColumn();
        // 「描述」列
        TableColumn<RegexProperty, String> descColumn = createDescColumn();

        chapterRegexTableView.getColumns().setAll(Arrays.asList(enableColumn, descColumn));
    }

    private @NotNull TableColumn<RegexProperty, Boolean> createEnableColumn() {
        TableColumn<RegexProperty, Boolean> enabledColumn = new TableColumn<>();
        enabledColumn.setMinWidth(100);
        enabledColumn.setMaxWidth(100);
        enabledColumn.setResizable(false);
        // 列头
        enabledColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.Regex.TableView.EnabledColumn.Title"));
        CheckBox enableAll = new CheckBox();
        enableAll.setOnAction(event -> {
            for (RegexProperty item : chapterRegexTableView.getItems()) {
                item.setEnabled(enableAll.isSelected());
            }
        });
        enabledColumn.setGraphic(enableAll);
        // 列值
        enabledColumn.setCellValueFactory(
                regexBooleanCellDataFeatures -> regexBooleanCellDataFeatures.getValue().getEnabledProperty());
        enabledColumn.setCellFactory(CheckBoxTableCell.forTableColumn(enabledColumn));
        return enabledColumn;
    }

    private @NotNull TableColumn<RegexProperty, String> createDescColumn() {
        TableColumn<RegexProperty, String> descColumn = new TableColumn<>();
        descColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.Regex.TableView.DescColumn.Title"));
        descColumn.setCellValueFactory(
                regexStringCellDataFeatures -> regexStringCellDataFeatures.getValue().getDescriptionProperty());
        return descColumn;
    }

    @FXML
    private void selectAll() {
        chapterRegexTableView.getSelectionModel().selectAll();
    }

    @FXML
    private void deleteSelectedRegexItems() {
        List<RegexProperty> selectedRegexItems = chapterRegexTableView.getSelectionModel().getSelectedItems().stream()
                .toList();
        for (RegexProperty selectedItem : selectedRegexItems) {
            if (chapterRegexTableView.getItems().size() > 1) {
                chapterRegexTableView.getItems().remove(selectedItem);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initHeader();
        initTableView();
    }

}
