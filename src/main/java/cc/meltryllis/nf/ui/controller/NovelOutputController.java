package cc.meltryllis.nf.ui.controller;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.config.InputFormat;
import cc.meltryllis.nf.config.OutputFormat;
import cc.meltryllis.nf.constants.CharacterCons;
import cc.meltryllis.nf.parser.ParseProcessor;
import cc.meltryllis.nf.utils.I18nUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Pair;
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
import javafx.util.StringConverter;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/12
 */
public class NovelOutputController implements Initializable {


    @FXML
    public VBox root;

    @FXML
    public Label exportConfigLabel;

    @FXML
    public Button exportButton;

    @FXML
    public Tile blankLineTile;

    @FXML
    public Spinner<Integer> blankLineSpinner;

    @FXML
    public Tile paragraphIndentationTile;

    @FXML
    public Spinner<Integer> paragraphIndentationSpinner;

    @FXML
    public ComboBox<Pair<StringProperty, Character>> paragraphIndentationComboBox;

    @FXML
    public ToggleButton indentationForChapterButton;

    private void initBlackLine() {
        blankLineTile.titleProperty()
                .bind(I18nUtil.createStringBinding("App.NovelExportConfiguration.BlackLine.Tile.Title"));
        blankLineTile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.NovelExportConfiguration.BlackLine.Tile.Desc"));
        blankLineSpinner.setEditable(false);
        OutputFormat.getInstance().setBlankLineCount(blankLineSpinner.getValue());
        blankLineSpinner.valueProperty().addListener((observable, oldValue, newValue) -> OutputFormat.getInstance()
                .setBlankLineCount(newValue));
    }

    private void initParagraphIndentation() {
        paragraphIndentationTile.titleProperty()
                .bind(I18nUtil.createStringBinding("App.NovelExportConfiguration.ParagraphIndentation.Tile.Title"));
        paragraphIndentationTile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.NovelExportConfiguration.ParagraphIndentation.Tile.Desc"));
        OutputFormat.getInstance().setSpaceCount(paragraphIndentationSpinner.getValue());
        paragraphIndentationSpinner.valueProperty()
                .addListener((observable, oldValue, newValue) -> OutputFormat.getInstance().setSpaceCount(newValue));
        paragraphIndentationComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> OutputFormat.getInstance()
                        .setSpace(newValue.getValue()));
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
                .bind(I18nUtil.createStringBinding(
                        "App.NovelExportConfiguration.ParagraphIndentation.ChapterButton.Text"));
        indentationForChapterButton.selectedProperty()
                .addListener((observable, oldValue, newValue) -> OutputFormat.getInstance()
                        .setIndentationForChapter(newValue));
        indentationForChapterButton.setSelected(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exportConfigLabel.textProperty().bind(I18nUtil.createStringBinding("App.NovelExportConfiguration"));
        initBlackLine();
        initParagraphIndentation();
        exportButton.textProperty().bind(I18nUtil.createStringBinding("Common.Export"));
    }

    public void export() {
        long start = System.currentTimeMillis();
        try {
            ParseProcessor.format(InputFormat.getInstance().getFile());
        } catch (FileNotFoundException e) {

        }
        Console.log("Time:{}", System.currentTimeMillis() - start);
    }
}
