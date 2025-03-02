package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.ChapterFormat;
import cc.meltryllis.nf.entity.property.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.UniqueObservableList;
import cc.meltryllis.nf.ui.controller.TabsController;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 章节导出格式控制器。
 *
 * @author Zachary W
 * @date 2025/2/16
 */
public class ChapterFormatController implements Initializable {

    @FXML
    public Card                    root;
    @FXML
    public Tile                    tile;
    @FXML
    public ComboBox<ChapterFormat> comboBox;

    private void initTile() {
        tile.titleProperty().bind(I18nUtil.createStringBinding("App.Output.ChapterFormat.Tile.Title"));
        tile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.Output.ChapterFormat.Tile.Desc"));
    }

    private void initComboBox() {
        OutputFormatProperty outputFormatProperty = OutputFormatProperty.getInstance();
        UniqueObservableList<ChapterFormat> uniqueObservableList = outputFormatProperty.getChapterFormatUniqueObservableList();
        uniqueObservableList.asItems(comboBox);
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ChapterFormat template) {
                if (template == null) {
                    return null;
                }
                return template.exampleChapter();
            }

            @Override
            public ChapterFormat fromString(String string) {
                if (string == null) {
                    return null;
                }
                return new ChapterFormat(string);
            }
        });
        outputFormatProperty.getSelectedChapterFormatProperty().bind(comboBox.valueProperty());
        comboBox.getSelectionModel().selectFirst();
    }

    public void editList() {
        TabPane tabPane = TabsController.getTabPane();
        if (tabPane != null) {
            tabPane.getSelectionModel().selectLast();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTile();
        initComboBox();
    }
}
