package cc.meltryllis.nf.ui.controller.settings;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.entity.property.input.RegexProperty;
import cc.meltryllis.nf.ui.controls.MTableView;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * @author Zachary W
 * @date 2025/2/26
 */
public class ChapterRegexEditor implements Initializable {

    @FXML
    public Tile                      tile;
    @FXML
    public Button                    deleteButton;
    @FXML
    public TextField                 descriptionField;
    @FXML
    public TextField                 patternField;
    @FXML
    public MTableView<RegexProperty> tableView;

    @FXML
    private void selectAll() {
        tableView.getSelectionModel().selectAll();
    }

    @FXML
    private void deleteSelectedRegexItems() {
        List<RegexProperty> selectedRegexItems = tableView.getSelectionModel().getSelectedItems().stream().toList();
        for (RegexProperty selectedItem : selectedRegexItems) {
            if (tableView.getItems().size() > 1) {
                tableView.getItems().remove(selectedItem);
            }
        }
    }

    @FXML
    private void addRegex() {
        String pattern = patternField.getText();
        if (StrUtil.isEmpty(pattern)) {
            return;
        }
        RegexProperty regex = new RegexProperty(descriptionField.getText(), pattern);
        tableView.getItems().add(regex);
    }

    private void initTile() {
        tile.titleProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Input.ChapterRegex.Tile.Title"));
        tile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Input.ChapterRegex.Tile.Desc"));
    }

    private void initDeleteButton() {
        deleteButton.disableProperty()
                .bind(InputFormatProperty.getInstance().getChapterRegexProperty().getRegexPropertyUniqueObservableList()
                        .getSizeProperty().lessThan(2));
    }

    private void initFields() {
        descriptionField.promptTextProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Input.ChapterRegex.DescField.Prompt"));

        patternField.promptTextProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Input.ChapterRegex.PatternField.Prompt"));

        patternField.setEditable(false);
        patternField.setCursor(Cursor.HAND);
        patternField.setOnMouseClicked(event -> {
            DialogUtil.DialogBuilder<String> builder = new DialogUtil.DialogBuilder<>(tile.getScene().getWindow(),
                    "/fxml/dialog/pattern-compile.fxml");
            String pattern = builder.title(I18nUtil.createStringBinding("Dialog.PatternCompile.Title"))
                    .initialValue(patternField.getText()).okButton(true).show();
            patternField.setText(pattern);
        });
    }

    private void initTableView() {
        tableView.setItems(
                InputFormatProperty.getInstance().getChapterRegexProperty().getRegexPropertyUniqueObservableList());
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<RegexProperty, String> descColumn = new TableColumn<>();
        descColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.Regex.TableView.DescColumn.Title"));
        descColumn.setCellValueFactory(
                regexStringCellDataFeatures -> regexStringCellDataFeatures.getValue().getDescriptionProperty());

        TableColumn<RegexProperty, Pattern> patternColumn = new TableColumn<>();
        patternColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.Regex.TableView.PatternColumn.Title"));
        patternColumn.setCellValueFactory(
                regexPatternCellDataFeatures -> regexPatternCellDataFeatures.getValue().getPatternProperty());

        tableView.getColumns().setAll(Arrays.asList(descColumn, patternColumn));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTile();
        initTableView();
        initDeleteButton();
        initFields();
    }

}
