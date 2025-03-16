package cc.meltryllis.nf.ui.controller.settings;

import atlantafx.base.controls.Card;
import cc.meltryllis.nf.ui.common.outline.OutlinePane;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/3/16
 */
public class SettingsPaneController implements Initializable {

    @FXML
    public OutlinePane root;
    @FXML
    public Label       formatterSettingsLabel;
    @FXML
    public Card        chapterRegexEditor;
    @FXML
    public Card        chapterTemplateEditor;


    private void registerOutline() {
        root.addOutlineEntry(formatterSettingsLabel, 1);
        root.addOutlineEntry(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Input.ChapterRegex.Tile.Title"),
                chapterRegexEditor, 2);
        root.addOutlineEntry(
                I18nUtil.createStringBinding("App.Settings.FormatterConfig.Output.ChapterTemplate.Tile.Title"),
                chapterTemplateEditor, 2);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formatterSettingsLabel.textProperty()
                .bind(I18nUtil.createStringBinding("App.Settings.FormatterConfig.Label.Text"));
        registerOutline();
    }

}
