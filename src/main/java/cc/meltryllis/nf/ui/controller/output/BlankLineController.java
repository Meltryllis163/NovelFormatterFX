package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.controls.Tile;
import cc.meltryllis.nf.entity.property.OutputFormatProperty;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 段落空行控制器。
 *
 * @author Zachary W
 * @date 2025/2/28
 */
public class BlankLineController implements Initializable {

    @FXML
    public Tile             blankLineTile;
    @FXML
    public Spinner<Integer> blankLineSpinner;

    private void initBlackLine() {
        blankLineTile.titleProperty().bind(I18nUtil.createStringBinding("App.Output.BlackLine.Tile.Title"));
        blankLineTile.descriptionProperty().bind(I18nUtil.createStringBinding("App.Output.BlackLine.Tile.Desc"));
        blankLineSpinner.setEditable(false);
        OutputFormatProperty.getInstance().getBlankLineCountProperty().bind(blankLineSpinner.valueProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initBlackLine();
    }
}
