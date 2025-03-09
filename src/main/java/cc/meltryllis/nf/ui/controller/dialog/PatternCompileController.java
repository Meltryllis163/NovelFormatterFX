package cc.meltryllis.nf.ui.controller.dialog;

import cc.meltryllis.nf.ui.common.StateLabel;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * 正则表达式输入控制器。
 *
 * @author Zachary W
 * @date 2025/3/9
 */
@Slf4j
public class PatternCompileController extends StageDialogController<String> implements Initializable {

    @FXML
    public Label      patternLabel;
    @FXML
    public TextField  patternField;
    @FXML
    public StateLabel compileResultLabel;
    @FXML
    public Label      testTextLabel;
    @FXML
    public TextField  testTextField;
    @FXML
    public StateLabel matchResultLabel;
    @FXML
    public Button     applyButton;

    private final SimpleObjectProperty<Pattern>   patternProperty   = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Exception> exceptionProperty = new SimpleObjectProperty<>();


    private void initLabels() {
        patternLabel.textProperty().bind(I18nUtil.createStringBinding("Dialog.PatternCompile.Pattern.Label.Text"));
        testTextLabel.textProperty().bind(I18nUtil.createStringBinding("Dialog.PatternCompile.TestText.Label.Text"));
        patternField.requestFocus();
    }

    private void initPatternField() {
        patternField.promptTextProperty().bind(I18nUtil.createStringBinding(
                "App.Settings.FormatterConfig.Input.ChapterRegex.PatternField.Prompt"));
        patternField.textProperty().addListener((observable, oldValue, newValue) -> {
            exceptionProperty.setValue(null);
            patternProperty.setValue(null);
            if (!StrUtil.isEmpty(newValue)) {
                try {
                    Pattern pattern = Pattern.compile(newValue);
                    patternProperty.setValue(pattern);
                } catch (Exception e) {
                    exceptionProperty.setValue(e);
                }
            }
            updateMatchResultLabel();
            updateCompileResultLabel();
        });
    }

    private void initTestTextField() {
        testTextField.promptTextProperty()
                .bind(I18nUtil.createStringBinding("Dialog.PatternCompile.TestText.Label.Prompt"));
        testTextField.textProperty().addListener((observable, oldValue, newValue) -> updateMatchResultLabel());
    }

    public void initApplyButton() {
        applyButton.textProperty().bind(I18nUtil.createStringBinding("Common.Apply"));
        applyButton.disableProperty().bind(patternProperty.isNull());
        applyButton.setOnAction(event -> {
            setResult(patternProperty.getValue().toString());
            getStageDialog().close();
        });
    }

    private void updateMatchResultLabel() {
        String testText = testTextField.getText();
        Pattern pattern = patternProperty.getValue();
        if (pattern == null || StrUtil.isEmpty(testText)) {
            matchResultLabel.setState(StateLabel.EMPTY, (String) null);
        } else if (pattern.matcher(testText).matches()) {
            matchResultLabel.setState(StateLabel.SUCCESS,
                    I18nUtil.createStringBinding("Dialog.PatternCompile.MatchResultLabel.Success"));
        } else {
            matchResultLabel.setState(StateLabel.WARN,
                    I18nUtil.createStringBinding("Dialog.PatternCompile.MatchResultLabel.Fail"));
        }
    }

    private void updateCompileResultLabel() {
        Pattern pattern = patternProperty.getValue();
        Exception e = exceptionProperty.getValue();
        if (pattern == null && e == null) {
            // 空正则
            compileResultLabel.setState(StateLabel.EMPTY, (String) null);
        } else if (pattern != null) {
            // 正则有效
            compileResultLabel.setState(StateLabel.SUCCESS,
                    I18nUtil.createStringBinding("Dialog.PatternCompile.Pattern.CompileResult.Label.Success"));
        } else {
            // 正则错误
            String message = e.getMessage();
            int index = message.indexOf(System.lineSeparator());
            if (index >= 0) {
                message = message.substring(0, index) + ".";
            }
            compileResultLabel.setState(StateLabel.WARN, message);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLabels();
        initPatternField();
        initTestTextField();
        initApplyButton();
    }

    @Override
    protected void setInitialResult(String initialValue) {
        super.setResult(initialValue);
        patternField.setText(initialValue);
    }
}
