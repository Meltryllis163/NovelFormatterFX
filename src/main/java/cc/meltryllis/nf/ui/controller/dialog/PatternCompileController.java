package cc.meltryllis.nf.ui.controller.dialog;

import cc.meltryllis.nf.constants.MyStyles;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
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
public class PatternCompileController extends AbstractStageDialogController<String> implements Initializable {

    public static PseudoClass WARNING_TIP = PseudoClass.getPseudoClass(MyStyles.WARNING_TIP);
    public static PseudoClass SUCCESS_TIP = PseudoClass.getPseudoClass(MyStyles.SUCCESS_TIP);
    public static PseudoClass EMPTY_TIP   = PseudoClass.getPseudoClass(MyStyles.HIDDEN_TIP);

    @FXML
    public Label     patternLabel;
    @FXML
    public TextField patternField;
    @FXML
    public Label     compileResultLabel;
    @FXML
    public Label     testTextLabel;
    @FXML
    public TextField testTextField;
    @FXML
    public Label     matchResultLabel;

    private final SimpleObjectProperty<Pattern>   patternProperty   = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Exception> exceptionProperty = new SimpleObjectProperty<>();

    private PseudoClass matchLabelLastClass;
    private PseudoClass compileLabelLastClass;


    private void initLabels() {
        patternLabel.textProperty().bind(I18nUtil.createStringBinding("Dialog.PatternCompile.Pattern.Label.Text"));
        testTextLabel.textProperty().bind(I18nUtil.createStringBinding("Dialog.PatternCompile.TestText.Label.Text"));
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

    public void initOKButton() {
        Button okButton = getStageDialog().getOkButton();
        if (okButton != null) {
            okButton.disableProperty().bind(patternProperty.isNull());
            okButton.setOnAction(event -> {
                setResult(patternProperty.getValue().toString());
                getStageDialog().close();
            });
        }
    }

    private void updateMatchResultLabel() {
        String testText = testTextField.getText();
        Pattern pattern = patternProperty.getValue();
        if (matchLabelLastClass != null) {
            matchResultLabel.pseudoClassStateChanged(matchLabelLastClass, false);
        }
        if (pattern == null || StrUtil.isEmpty(testText)) {
            matchResultLabel.pseudoClassStateChanged(EMPTY_TIP, true);
            matchLabelLastClass = EMPTY_TIP;
        } else if (pattern.matcher(testText).matches()) {
            matchResultLabel.textProperty()
                    .bind(I18nUtil.createStringBinding("Dialog.PatternCompile.MatchResultLabel.Success"));
            matchResultLabel.pseudoClassStateChanged(SUCCESS_TIP, true);
            matchLabelLastClass = SUCCESS_TIP;
        } else {
            matchResultLabel.textProperty()
                    .bind(I18nUtil.createStringBinding("Dialog.PatternCompile.MatchResultLabel.Fail"));
            matchResultLabel.pseudoClassStateChanged(WARNING_TIP, true);
            matchLabelLastClass = WARNING_TIP;
        }
    }

    private void updateCompileResultLabel() {
        Pattern pattern = patternProperty.getValue();
        Exception e = exceptionProperty.getValue();
        if (compileLabelLastClass != null) {
            compileResultLabel.pseudoClassStateChanged(compileLabelLastClass, false);
        }
        if (pattern == null && e == null) {
            compileResultLabel.pseudoClassStateChanged(EMPTY_TIP, true);
            compileLabelLastClass = EMPTY_TIP;
            // 空正则
        } else if (pattern != null) {
            compileResultLabel.textProperty()
                    .bind(I18nUtil.createStringBinding("Dialog.PatternCompile.Pattern.CompileResult.Label.Success"));
            // 正则有效
            compileResultLabel.pseudoClassStateChanged(SUCCESS_TIP, true);
            compileLabelLastClass = SUCCESS_TIP;
        } else {
            // 正则错误
            String message = e.getMessage();
            int index = message.indexOf(System.lineSeparator());
            if (index >= 0) {
                message = message.substring(0, index) + ".";
            }
            compileResultLabel.textProperty().unbind();
            compileResultLabel.setText(message);
            compileResultLabel.pseudoClassStateChanged(WARNING_TIP, true);
            compileLabelLastClass = WARNING_TIP;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLabels();
        initPatternField();
        initTestTextField();
    }

    @Override
    protected void stageDialogRegistered() {
        super.stageDialogRegistered();
        initOKButton();
    }

    @Override
    protected void setInitialResult(String initialValue) {
        super.setResult(initialValue);
        patternField.setText(initialValue);
    }

}
