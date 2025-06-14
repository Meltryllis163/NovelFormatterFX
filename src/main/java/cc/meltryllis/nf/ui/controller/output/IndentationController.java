package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.util.IntegerStringConverter;
import cc.meltryllis.nf.entity.property.output.IndentationProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.ui.controls.FormField;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * 段落缩进控制器。
 *
 * @author Zachary W
 * @date 2025/3/3
 */
public class IndentationController implements Initializable {

    @FXML
    public  ComboBox<IndentationProperty.Space> spaceComboBox;
    @FXML
    public  Spinner<Integer>                    spinner;
    @FXML
    public  ToggleButton                        indentationForChapterButton;
    @FXML
    private FormField                           root;

    private void initHeader() {
        root.titleProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Output.Paragraph.Indentation.Header.Title"));
        root.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Output.Paragraph.Indentation.Header.Desc"));
    }

    private void initSpinner() {
        IntegerStringConverter.createFor(spinner);
        OutputFormatProperty.getInstance().getParagraphProperty().getIndentationProperty()
                .setSpaceCount(spinner.getValue());
        spinner.setTooltip(null);
        spinner.valueProperty().addListener(
                (observable, oldValue, newValue) -> OutputFormatProperty.getInstance().getParagraphProperty()
                        .getIndentationProperty().setSpaceCount(newValue));
    }

    private void initComboBox() {

        spaceComboBox.setCellFactory(param -> new TranslationCell());
        spaceComboBox.setButtonCell(new TranslationCell());

        IndentationProperty indentationProperty = OutputFormatProperty.getInstance().getParagraphProperty()
                .getIndentationProperty();
        spaceComboBox.setItems(FXCollections.observableList(Arrays.asList(IndentationProperty.Space.values())));
        spaceComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> indentationProperty.setSpace(newValue));
        spaceComboBox.getSelectionModel().select(indentationProperty.getSpace());
    }

    private void initIndentationForChapterButton() {
        IndentationProperty indentationProperty = OutputFormatProperty.getInstance().getParagraphProperty()
                .getIndentationProperty();
        indentationForChapterButton.setCursor(Cursor.HAND);
        indentationForChapterButton.textProperty().bind(I18nUtil.createStringBinding(
                "App.Formatter.Output.Paragraph.Indentation.EffectiveForChapter.Button.Text"));
        indentationForChapterButton.setSelected(indentationProperty.isEffectiveForChapter());
        indentationForChapterButton.selectedProperty()
                .addListener((observable, oldValue, newValue) -> indentationProperty.setEffectiveForChapter(newValue));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initHeader();
        initSpinner();
        initComboBox();
        initIndentationForChapterButton();
    }

    private class TranslationCell extends ListCell<IndentationProperty.Space> {

        @Override
        protected void updateItem(IndentationProperty.Space item, boolean empty) {
            super.updateItem(item, empty);
            textProperty().unbind();
            if (empty || item == null) {
                setText(StrUtil.EMPTY);
            } else {
                textProperty().bind(item.getNameProperty());
            }
        }
    }

}
