package cc.meltryllis.nf.ui.controller;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.config.ChapterInputFormat;
import cc.meltryllis.nf.config.InputFormat;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
    public Tile chapterLimitTile;

    @FXML
    public ComboBox<Integer> chapterLimitComboBox;

    @FXML
    public Tile chapterRegexTile;

    private void initChapterLimitConfig(ChapterInputFormat config) {
        chapterLimitTile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.ChapterConfig.LengthLimitTile.Desc"));
        // 此处文本在语言变更时会发生错误，该错误由atlantafx产生，而且修复于以下request(https://github.com/mkpaz/atlantafx/pull/81)
        // 目前暂时不提供除简体中文以外的其他语言，因此不会产生问题，等待atlantafx更新
        chapterLimitTile.titleProperty().bind(I18nUtil.createStringBinding("App.ChapterConfig.LengthLimitTile.Title"));
        for (int limit = 30; limit <= 100; limit += 10) {
            chapterLimitComboBox.getItems().add(limit);
        }
        chapterLimitComboBox.setValue(config.getMaxLimitLength());
        chapterLimitComboBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> config.setMaxLimitLength(newValue));
    }

    private void initChapterRegex() {
        chapterRegexTile.titleProperty().bind(I18nUtil.createStringBinding("App.ChapterConfig.ChapterRegexTile.Title"));
        chapterRegexTile.descriptionProperty()
                .bind(I18nUtil.createStringBinding("App.ChapterConfig.ChapterRegexTile.Desc"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChapterInputFormat chapterConfig = InputFormat.getInstance().getChapterFormat();
        chapterConfigLabel.textProperty().bind(I18nUtil.createStringBinding("App.ChapterConfig"));
        initChapterLimitConfig(chapterConfig);
        initChapterRegex();
    }

}
