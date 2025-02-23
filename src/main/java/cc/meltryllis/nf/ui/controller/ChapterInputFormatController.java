package cc.meltryllis.nf.ui.controller;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/11
 */
public class ChapterInputFormatController implements Initializable {
    @FXML
    Label chapterConfigLabel;


    @FXML
    public Tile chapterRegexTile;

    private void initChapterRegex() {
        chapterRegexTile.titleProperty().bind(I18nUtil.createStringBinding("App.ChapterInput.ChapterRegexTile.Title"));
        chapterRegexTile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.ChapterInput.ChapterRegexTile.Desc"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chapterConfigLabel.textProperty().bind(I18nUtil.createStringBinding("App.ChapterInput"));
        initChapterRegex();
    }

}
