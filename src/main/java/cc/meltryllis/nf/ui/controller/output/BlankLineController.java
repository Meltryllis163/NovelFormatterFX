package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.controls.Tile;
import atlantafx.base.util.IntegerStringConverter;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.output.ParagraphProperty;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;

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
    @FXML
    public ToggleButton     resegmentToggleButton;

    private void initBlackLine() {
        blankLineTile.titleProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.BlackLine.Tile.Title"));
        blankLineTile.descriptionProperty().bind(I18nUtil.createStringBinding(
                "App.Formatter.Output.BlackLine.Tile.Desc"));
        IntegerStringConverter.createFor(blankLineSpinner);
        ParagraphProperty paragraphProperty = OutputFormatProperty.getInstance().getParagraphProperty();
        paragraphProperty.getBlankLineCountProperty().bind(blankLineSpinner.valueProperty());
    }

    private void initResegmentButton() {
        ParagraphProperty paragraphProperty = OutputFormatProperty.getInstance().getParagraphProperty();
        resegmentToggleButton.setCursor(Cursor.HAND);
        resegmentToggleButton.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Output.BlackLine.Resegment.ToggleButton.Text"));
        resegmentToggleButton.setSelected(paragraphProperty.isResegment());
        resegmentToggleButton.selectedProperty()
                .addListener((observable, oldValue, newValue) -> paragraphProperty.setResegment(newValue));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initBlackLine();
        initResegmentButton();
    }

}
