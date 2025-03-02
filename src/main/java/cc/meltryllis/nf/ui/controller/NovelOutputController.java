package cc.meltryllis.nf.ui.controller;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.entity.config.InputFormat;
import cc.meltryllis.nf.entity.config.OutputFormat;
import cc.meltryllis.nf.parser.ParseProcessor;
import cc.meltryllis.nf.utils.I18nUtil;
import cc.meltryllis.nf.utils.StrUtil;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/12
 */
@Slf4j
public class NovelOutputController implements Initializable {


    @FXML
    public VBox root;

    @FXML
    public Label exportConfigLabel;

    @FXML
    public Button exportButton;

    @FXML
    public Tile paragraphIndentationTile;

    @FXML
    public Spinner<Integer> paragraphIndentationSpinner;

    @FXML
    public ComboBox<Pair<StringProperty, Character>> paragraphIndentationComboBox;

    @FXML
    public ToggleButton indentationForChapterButton;


    private void initParagraphIndentation() {
        paragraphIndentationTile.titleProperty()
                .bind(I18nUtil.createStringBinding("App.Output.ParagraphIndentation.Tile.Title"));
        paragraphIndentationTile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.Output.ParagraphIndentation.Tile.Desc"));
        OutputFormat.getInstance().setSpaceCount(paragraphIndentationSpinner.getValue());
        paragraphIndentationSpinner.valueProperty()
                .addListener((observable, oldValue, newValue) -> OutputFormat.getInstance().setSpaceCount(newValue));
        paragraphIndentationComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> OutputFormat.getInstance().setSpace(newValue.getValue()));
        paragraphIndentationComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Pair<StringProperty, Character> object) {
                if (object == null) {
                    return null;
                }
                return object.getKey().getValue();
            }

            @Override
            public Pair<StringProperty, Character> fromString(String string) {
                return null;
            }
        });
        StringBinding[] bindings = new StringBinding[]{I18nUtil.createStringBinding(
                "Common.Space"), I18nUtil.createStringBinding("Common.FullWidthSpace")};
        char[] spaces = new char[]{CharacterCons.ENGLISH.SPACE, CharacterCons.CHINESE.SPACE};
        ObservableList<Pair<StringProperty, Character>> observableList = FXCollections.observableArrayList(
                param -> new Observable[]{param.getKey()});
        for (int i = 0; i < bindings.length; i++) {
            SimpleStringProperty property = new SimpleStringProperty();
            property.bind(bindings[i]);
            observableList.add(new Pair<>(property, spaces[i]));
        }
        paragraphIndentationComboBox.setItems(observableList);
        paragraphIndentationComboBox.getSelectionModel().selectLast();

        indentationForChapterButton.textProperty()
                .bind(I18nUtil.createStringBinding("App.Output.ParagraphIndentation.ChapterButton.Text"));
        indentationForChapterButton.selectedProperty().addListener(
                (observable, oldValue, newValue) -> OutputFormat.getInstance().setIndentationForChapter(newValue));
        indentationForChapterButton.setSelected(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exportConfigLabel.textProperty().bind(I18nUtil.createStringBinding("App.Output.Label.Text"));
        initParagraphIndentation();
        exportButton.textProperty().bind(I18nUtil.createStringBinding("Common.Export"));
    }

    public void export() {
        long start = System.currentTimeMillis();
        ParseProcessor.format(InputFormat.getInstance().getFile());
        log.info(StrUtil.format("Format file success. Time: {0}ms.", System.currentTimeMillis() - start));
    }
}
