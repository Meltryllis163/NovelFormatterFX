package cc.meltryllis.nf.ui.controller.input;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.ui.controller.TabsController;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 章节正则解析表格控制器。
 *
 * @author Zachary W
 * @date 2025/2/26
 */
public class ChapterRegexController implements Initializable {

    @FXML
    public Tile           chapterRegexTile;
    @FXML
    public RegexTableView tableView;

    private void initChapterRegex() {
        chapterRegexTile.titleProperty().bind(I18nUtil.createStringBinding(
                "App.Input.ChapterInput.Regex.Tile.Title"));
        chapterRegexTile.descriptionProperty().bind(I18nUtil.createStringBinding(
                "App.Input.ChapterInput.Regex.Tile.Desc"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initChapterRegex();
    }

    public void editTableView() {
        TabsController.getTabPane().getSelectionModel().selectLast();
    }
}
