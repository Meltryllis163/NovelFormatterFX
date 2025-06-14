package cc.meltryllis.nf.ui.controller.output;

import atlantafx.base.util.IntegerStringConverter;
import cc.meltryllis.nf.entity.property.output.OutputFormatProperty;
import cc.meltryllis.nf.entity.property.output.ParagraphProperty;
import cc.meltryllis.nf.ui.controls.FormField;
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
    private FormField        root;
    @FXML
    private Spinner<Integer> blankLineSpinner;
    @FXML
    private ToggleButton     resegmentToggleButton;

    private void initBlackLine() {
        root.titleProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.BlackLine.Header.Title"));
        root.descriptionProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.BlackLine.Header.Desc"));
        IntegerStringConverter.createFor(blankLineSpinner);
        blankLineSpinner.setTooltip(null);
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
