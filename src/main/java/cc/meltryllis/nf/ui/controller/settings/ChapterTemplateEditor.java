package cc.meltryllis.nf.ui.controller.settings;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.property.output.ChapterTemplateProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.ui.common.CustomTableView;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/24
 */
public class ChapterTemplateEditor implements Initializable {

    @FXML
    public Tile                                     tile;
    @FXML
    public Button                                   deleteButton;
    @FXML
    public TextField                                templateField;
    @FXML
    public TextField                                exampleField;
    @FXML
    public CustomTableView<ChapterTemplateProperty> tableView;

    @FXML
    private void selectAll() {
        tableView.getSelectionModel().selectAll();
    }

    @FXML
    private void deleteSelectedItems() {
        List<ChapterTemplateProperty> selectItems = tableView.getSelectionModel().getSelectedItems().stream().toList();
        for (ChapterTemplateProperty selectedItem : selectItems) {
            if (tableView.getItems().size() > 1) {
                tableView.getItems().remove(selectedItem);
            }
        }
    }

    @FXML
    private void addChapterFormatItem() {
        String template = templateField.getText();
        if (StrUtil.isEmpty(template)) {
            return;
        }
        ChapterTemplateProperty chapterTemplateProperty = new ChapterTemplateProperty(template);
        tableView.getItems().add(chapterTemplateProperty);
    }

    private void initTile() {
        tile.titleProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Output.ChapterTemplate.Tile.Title"));
        tile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Output.ChapterTemplate.Tile.Desc"));
    }

    private void initFields() {
        templateField.promptTextProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Output.ChapterTemplate.TemplateField.Prompt"));
        templateField.textProperty()
                .addListener((observableValue, string, t1) -> exampleField.setText(Chapter.exampleText(t1)));
        exampleField.promptTextProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Output.ChapterTemplate.ExampleField.Prompt"));
    }

    private void initTableView() {
        tableView.setItems(OutputFormatProperty.getInstance().getChapterTemplateUniqueList());

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<ChapterTemplateProperty, String> exampleColumn = new TableColumn<>();
        exampleColumn.textProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Output.ChapterTemplate.TableView.ExampleColumn.Title"));
        exampleColumn.setCellValueFactory(
                param -> new SimpleStringProperty(Chapter.exampleText(param.getValue().getTemplateText())));

        TableColumn<ChapterTemplateProperty, String> templateColumn = new TableColumn<>();
        templateColumn.textProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Output.ChapterTemplate.TableView.FormatColumn.Title"));
        templateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTemplateText()));

        tableView.getColumns().addAll(Arrays.asList(templateColumn, exampleColumn));
    }

    private void initButtons() {
        this.deleteButton.disableProperty()
                .bind(OutputFormatProperty.getInstance().getChapterTemplateUniqueList().getSizeProperty().lessThan(2));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTile();
        initFields();
        initTableView();
        initButtons();
    }

}
