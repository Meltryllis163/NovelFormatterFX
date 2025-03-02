package cc.meltryllis.nf.ui.controller.settings;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.Regex;
import cc.meltryllis.nf.entity.property.InputFormatProperty;
import cc.meltryllis.nf.entity.property.UniqueObservableList;
import cc.meltryllis.nf.utils.I18nUtil;
import cc.meltryllis.nf.utils.StrUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/26
 */
public class ChapterInputRegexEditor implements Initializable {

    @FXML
    public Tile             tile;
    @FXML
    public Button           deleteButton;
    @FXML
    public TextField        descriptionField;
    @FXML
    public TextField        patternField;
    @FXML
    public TableView<Regex> tableView;

    private UniqueObservableList<Regex> regexUniqueObservableList;

    @FXML
    private void selectAll() {
        tableView.getSelectionModel().selectAll();
    }

    @FXML
    private void delete() {
        List<Regex> selectedRegexItems = tableView.getSelectionModel().getSelectedItems().stream().toList();
        for (Regex selectedItem : selectedRegexItems) {
            regexUniqueObservableList.remove(selectedItem);
        }
    }

    @FXML
    private void addRegex() {
        String pattern = patternField.getText();
        if (StrUtil.isEmpty(pattern)) {
            return;
        }
        Regex regex = new Regex(descriptionField.getText(), pattern);
        regexUniqueObservableList.add(regex);
    }

    private void initTile() {
        tile.titleProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Input.ChapterRegex.Tile.Title"));
        tile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Input.ChapterRegex.Tile.Desc"));
    }

    private void initDeleteButton() {
        deleteButton.disableProperty().bind(regexUniqueObservableList.getSizeProperty().lessThan(2));
    }

    private void initFields() {
        descriptionField.promptTextProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Input.ChapterRegex.DescField.Prompt"));
        patternField.promptTextProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Input.ChapterRegex.PatternField.Prompt"));
    }

    private void initTableView() {
        this.regexUniqueObservableList = InputFormatProperty.getInstance().getRegexUniqueObservableList();
        regexUniqueObservableList.asItems(tableView);
        tableView.setEditable(false);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<Regex, String> descColumn = new TableColumn<>();
        descColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Input.ChapterInput.Regex.TableView.DescColumn.Title"));
        descColumn.setCellValueFactory(regexStringCellDataFeatures -> new SimpleStringProperty(
                regexStringCellDataFeatures.getValue().getDescription()));

        TableColumn<Regex, String> patternColumn = new TableColumn<>();
        patternColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Input.ChapterInput.Regex.TableView.PatternColumn.Title"));
        patternColumn.setCellValueFactory(regexPatternCellDataFeatures -> new SimpleStringProperty(
                regexPatternCellDataFeatures.getValue().getPattern().toString()));

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
