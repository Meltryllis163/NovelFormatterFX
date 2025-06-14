package cc.meltryllis.nf.ui.controller.output;

import cc.meltryllis.nf.entity.Chapter;
import cc.meltryllis.nf.entity.property.UniqueObservableList;
import cc.meltryllis.nf.entity.property.output.ChapterTemplateProperty;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.ui.controls.FormField;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 章节导出格式控制器。
 *
 * @author Zachary W
 * @date 2025/2/16
 */
public class ChapterTemplateController implements Initializable {

    @FXML
    private FormField                         root;
    @FXML
    private ToggleButton                      autoNumberButton;
    @FXML
    private ComboBox<ChapterTemplateProperty> templateComboBox;

    private void initHeader() {
        root.titleProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.ChapterFormat.Header.Title"));
        root.descriptionProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.ChapterFormat.Header.Desc"));
    }

    private void initAutoNumberButton() {
        autoNumberButton.setCursor(Cursor.HAND);
        autoNumberButton.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Output.ChapterFormat.AutoNumber.Button.Text"));
        autoNumberButton.setPrefWidth(Control.USE_COMPUTED_SIZE);
        autoNumberButton.setSelected(OutputFormatProperty.getInstance().isAutoNumberForChapter());
        OutputFormatProperty.getInstance().getAutoNumberForChapterProperty().bind(autoNumberButton.selectedProperty());
    }

    private void initComboBox() {
        OutputFormatProperty outputFormatProperty = OutputFormatProperty.getInstance();
        UniqueObservableList<ChapterTemplateProperty> templateListWrapper = outputFormatProperty.getChapterTemplateUniqueList();
        templateComboBox.setItems(templateListWrapper);
        templateComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ChapterTemplateProperty chapterTemplateProperty) {
                if (chapterTemplateProperty == null) {
                    return null;
                }
                return Chapter.exampleText(chapterTemplateProperty.getTemplateText());
            }

            @Override
            public ChapterTemplateProperty fromString(String string) {
                if (string == null) {
                    return null;
                }
                return new ChapterTemplateProperty(string);
            }
        });
        outputFormatProperty.getSelectedChapterTemplateObjectProperty().bind(templateComboBox.valueProperty());
        templateComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void editList() {
        // TODO TabPane tabPane = TabsController.getTabPane();
        //  if (tabPane != null) {
        //     tabPane.getSelectionModel().selectLast();
        //  }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initHeader();
        initAutoNumberButton();
        initComboBox();
    }

}
