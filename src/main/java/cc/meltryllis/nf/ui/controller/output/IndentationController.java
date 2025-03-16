package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.controls.Tile;
import atlantafx.base.util.IntegerStringConverter;
import cc.meltryllis.nf.entity.property.output.IndentationProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.util.StringConverter;

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
    public Tile                                tile;
    @FXML
    public ComboBox<IndentationProperty.Space> spaceComboBox;
    @FXML
    public Spinner<Integer>                    spinner;
    @FXML
    public ToggleButton                        indentationForChapterButton;

    private void initTile() {
        tile.titleProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.Paragraph.Indentation.Tile.Title"));
        tile.descriptionProperty().bind(I18nUtil.createStringBinding(
                "App.Formatter.Output.Paragraph.Indentation.Tile.Desc"));
    }

    private void initSpinner() {
        IntegerStringConverter.createFor(spinner);
        OutputFormatProperty.getInstance().getParagraphProperty().getIndentationProperty()
                .setSpaceCount(spinner.getValue());
        spinner.valueProperty().addListener(
                (observable, oldValue, newValue) -> OutputFormatProperty.getInstance().getParagraphProperty()
                        .getIndentationProperty().setSpaceCount(newValue));
    }

    private void initComboBox() {
        spaceComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(IndentationProperty.Space space) {
                if (space == null) {
                    return null;
                }
                return space.getName();
            }

            @Override
            public IndentationProperty.Space fromString(String string) {
                return null;
            }
        });

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
        indentationForChapterButton.textProperty()
                .bind(I18nUtil.createStringBinding(
                        "App.Formatter.Output.Paragraph.Indentation.EffectiveForChapter.Button.Text"));
        indentationForChapterButton.setSelected(indentationProperty.isEffectiveForChapter());
        indentationForChapterButton.selectedProperty()
                .addListener((observable, oldValue, newValue) -> indentationProperty.setEffectiveForChapter(newValue));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTile();
        initSpinner();
        initComboBox();
        initIndentationForChapterButton();
    }

}
