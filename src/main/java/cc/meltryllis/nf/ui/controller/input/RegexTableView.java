package cc.meltryllis.nf.ui.controller.input;

import cc.meltryllis.nf.entity.Regex;
import cc.meltryllis.nf.entity.property.InputFormatProperty;
import cc.meltryllis.nf.entity.property.UniqueObservableList;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * 使用与编辑 {@link Regex} 的表格。
 *
 * @author Zachary W
 * @date 2025/2/12
 */
@Slf4j
@Getter
public class RegexTableView extends TableView<Regex> {

    private UniqueObservableList<Regex> uniqueObservableList;

    public RegexTableView() {
        setItems();
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn<Regex, Boolean> enabledColumn = createEnabledTableColumn();
        enabledColumn.setMinWidth(200);

        TableColumn<Regex, String> descColumn = new TableColumn<>();
        descColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Input.ChapterInput.Regex.TableView.DescColumn.Title"));
        descColumn.setCellValueFactory(regexStringCellDataFeatures -> new SimpleStringProperty(
                regexStringCellDataFeatures.getValue().getDescription()));
        descColumn.setMinWidth(200);

        TableColumn<Regex, String> patternColumn = new TableColumn<>();
        patternColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Input.ChapterInput.Regex.TableView.PatternColumn.Title"));
        patternColumn.setCellValueFactory(regexStringCellDataFeatures -> new SimpleStringProperty(
                regexStringCellDataFeatures.getValue().getPattern().toString()));
        patternColumn.setMinWidth(800);

        getColumns().setAll(Arrays.asList(enabledColumn, descColumn, patternColumn));

        enableFirstRegex();
    }

    private void enableFirstRegex() {
        try {
            Regex firstRegex = getItems().getFirst();
            firstRegex.getEnabledProperty().set(true);
        } catch (NoSuchElementException e) {
            log.debug("Chapter regex items is empty.", e);
        }
    }

    private @NotNull TableColumn<Regex, Boolean> createEnabledTableColumn() {
        TableColumn<Regex, Boolean> enabledColumn = new TableColumn<>();
        enabledColumn.setEditable(true);
        CheckBox enableAll = new CheckBox();
        enableAll.setOnAction(
                event -> getItems().forEach(item -> item.getEnabledProperty().set(enableAll.isSelected())));
        enabledColumn.setGraphic(enableAll);
        enabledColumn.textProperty()
                .bind(I18nUtil.createStringBinding("App.Input.ChapterInput.Regex.TableView.EnabledColumn.Title"));

        enabledColumn.setCellValueFactory(
                regexBooleanCellDataFeatures -> regexBooleanCellDataFeatures.getValue().getEnabledProperty());
        enabledColumn.setCellFactory(param -> {
            CheckBox checkBox = new CheckBox();
            TableCell<Regex, Boolean> cell = new TableCell<>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    setGraphic(checkBox);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        checkBox.setSelected(item);
                        setGraphic(checkBox);
                    }
                }
            };
            checkBox.selectedProperty().addListener(
                    (obs, wasSelected, isSelected) -> cell.getTableRow().getItem().getEnabledProperty()
                            .set(isSelected));
            return cell;
        });
        return enabledColumn;
    }

    private void setItems() {
        uniqueObservableList = InputFormatProperty.getInstance().getRegexUniqueObservableList();
        uniqueObservableList.asItems(this);
    }
}
