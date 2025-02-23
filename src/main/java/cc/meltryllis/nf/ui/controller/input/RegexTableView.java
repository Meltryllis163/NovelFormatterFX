package cc.meltryllis.nf.ui.controller.input;

import cc.meltryllis.nf.entity.Regex;
import cc.meltryllis.nf.entity.config.InputFormat;
import cc.meltryllis.nf.entity.property.RegexProperty;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 使用与编辑 {@link Regex} 的表格。
 *
 * @author Zachary W
 * @date 2025/2/12
 */
public class RegexTableView extends TableView<RegexProperty> {
    @SuppressWarnings("unchecked")
    public RegexTableView() {
        loadData();
        setEditable(true);

        TableColumn<RegexProperty, Boolean> enabledColumn = createEnabledTableColumn();

        TableColumn<RegexProperty, String> descColumn = new TableColumn<>();
        descColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.ChapterInput.RegexTableView.DescColumn.Title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<RegexProperty, Pattern> patternColumn = new TableColumn<>();
        patternColumn.textProperty().bind(I18nUtil.createStringBinding(
                "App.ChapterInput.RegexTableView.PatternColumn.Title"));
        patternColumn.setCellValueFactory(new PropertyValueFactory<>("pattern"));

        /*unchecked*/
        getColumns().setAll(enabledColumn, descColumn, patternColumn);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        getItems().getFirst().enabledProperty().set(true);

    }

    private @NotNull TableColumn<RegexProperty, Boolean> createEnabledTableColumn() {
        TableColumn<RegexProperty, Boolean> enabledColumn = new TableColumn<>();
        CheckBox enableAll = new CheckBox();
        enableAll.setOnAction(event -> getItems().forEach(item -> item.enabledProperty().set(enableAll.isSelected())));
        enabledColumn.setGraphic(enableAll);
        enabledColumn.setEditable(true);
        enabledColumn.setCellValueFactory(new PropertyValueFactory<>("enabled"));
        enabledColumn.setCellFactory(param -> {
            CheckBox checkBox = new CheckBox();
            TableCell<RegexProperty, Boolean> cell = new TableCell<>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    setGraphic(checkBox);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        checkBox.setSelected(item);
                        setGraphic(checkBox);
                    }
                }
            };
            checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) ->
                    cell.getTableRow().getItem().enabledProperty().set(isSelected));
            cell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return cell;
        });
        return enabledColumn;
    }

    private void loadData() {
        List<RegexProperty> regexPropertyList = new ArrayList<>();
        for (Regex regex : InputFormat.getInstance().getChapterRegexList()) {
            regexPropertyList.add(new RegexProperty(regex));
        }
        ObservableList<RegexProperty> data = FXCollections.observableList(regexPropertyList);
        setItems(data);
    }
}
