package cc.meltryllis.nf.ui.controller.input;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.input.RegexProperty;
import cc.meltryllis.nf.ui.common.MTableView;
import cc.meltryllis.nf.ui.controller.TabsController;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * 章节正则解析表格控制器。
 *
 * @author Zachary W
 * @date 2025/2/26
 */
@Slf4j
public class ChapterRegexController implements Initializable {

    @FXML
    public Tile                      chapterRegexTile;
    @FXML
    public MTableView<RegexProperty> chapterRegexTableView;

    private void initChapterRegexTile() {
        chapterRegexTile.titleProperty().bind(I18nUtil.createStringBinding(
                "App.Formatter.Input.Chapter.Regex.Tile.Title"));
        chapterRegexTile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.Regex.Tile.Desc"));
    }

    private void initTableView() {
        chapterRegexTableView.setItems(InputFormatProperty.getInstance().getChapterRegexProperty().getRegexPropertyUniqueObservableList());

        // 「启用正则」列
        TableColumn<RegexProperty, Boolean> enableColumn = createEnableColumn();
        // 「描述」列
        TableColumn<RegexProperty, String> descColumn = createDescColumn();
        // 「正则表达式」列
        TableColumn<RegexProperty, Pattern> patternColumn = createPatternColumn();

        chapterRegexTableView.getColumns().setAll(Arrays.asList(enableColumn, descColumn, patternColumn));
    }

    private @NotNull TableColumn<RegexProperty, Boolean> createEnableColumn() {
        TableColumn<RegexProperty, Boolean> enabledColumn = new TableColumn<>();
        enabledColumn.setMinWidth(120);
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
        descColumn.setCellValueFactory(regexStringCellDataFeatures -> regexStringCellDataFeatures.getValue()
                .getDescriptionProperty());
        return descColumn;
    }

    private @NotNull TableColumn<RegexProperty, Pattern> createPatternColumn() {
        TableColumn<RegexProperty, Pattern> patternColumn = new TableColumn<>();
        patternColumn.setEditable(true);
        patternColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.Regex.TableView.PatternColumn.Title"));
        patternColumn.setCellValueFactory(
                regexStringCellDataFeatures -> regexStringCellDataFeatures.getValue().getPatternProperty());
        return patternColumn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initChapterRegexTile();
        initTableView();
    }

    @FXML
    private void editTableView() {
        TabsController.getTabPane().getSelectionModel().selectLast();
    }
}
