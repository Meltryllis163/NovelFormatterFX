package cc.meltryllis.nf.ui.controller.settings;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.ChapterFormat;
import cc.meltryllis.nf.entity.property.OutputFormatProperty;
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
 * @date 2025/2/24
 */
public class ChapterOutputFormatEditor implements Initializable {

    @FXML
    public Tile                     tile;
    @FXML
    public Button deleteButton;
    @FXML
    public TextField                templateField;
    @FXML
    public TextField                exampleField;
    @FXML
    public TableView<ChapterFormat> tableView;

    private UniqueObservableList<ChapterFormat> chapterFormatUniqueObservableList;

    @FXML
    private void selectAll() {
        tableView.getSelectionModel().selectAll();
    }

    @FXML
    private void deleteSelectedItems() {
        List<ChapterFormat> selectItems = tableView.getSelectionModel().getSelectedItems().stream().toList();
        for (ChapterFormat selectedItem : selectItems) {
            if (tableView.getItems().size() <= 1) {
                break;
            }
            chapterFormatUniqueObservableList.remove(selectedItem);
        }
    }

    @FXML
    private void addChapterFormatItem() {
        String template = templateField.getText();
        if (StrUtil.isEmpty(template)) {
            return;
        }
        ChapterFormat chapterFormat = new ChapterFormat(template);
        chapterFormatUniqueObservableList.add(chapterFormat);
    }

    private void initTile() {
        tile.titleProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Output.ChapterFormat.Tile.Title"));
        tile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Output.ChapterFormat.Tile.Desc"));
    }

    private void initFields() {
        templateField.promptTextProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Output.ChapterFormat.TemplateField.Prompt"));
        templateField.textProperty()
                .addListener((observableValue, string, t1) -> exampleField.setText(Chapter.exampleFormat(t1)));
        exampleField.promptTextProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Output.ChapterFormat.ExampleField.Prompt"));
    }

    private void initTableView() {
        chapterFormatUniqueObservableList = OutputFormatProperty.getInstance().getChapterFormatUniqueObservableList();
        chapterFormatUniqueObservableList.asItems(tableView);

        // tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);

        TableColumn<ChapterFormat, String> exampleColumn = new TableColumn<>();
        exampleColumn.textProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Output.ChapterFormat.TableView.ExampleColumn.Title"));
        exampleColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().exampleChapter()));

        TableColumn<ChapterFormat, String> templateColumn = new TableColumn<>();
        templateColumn.textProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Output.ChapterFormat.TableView.FormatColumn.Title"));
        templateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTemplateText()));

        tableView.getColumns().addAll(Arrays.asList(templateColumn, exampleColumn));
    }

    private void initButtons() {
        this.deleteButton.disableProperty().bind(chapterFormatUniqueObservableList.getSizeProperty().lessThan(2));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTile();
        initFields();
        initTableView();
        initButtons();
    }
}
