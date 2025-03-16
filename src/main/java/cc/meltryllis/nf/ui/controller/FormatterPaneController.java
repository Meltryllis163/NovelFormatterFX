package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.parser.ParseProcessor;
import cc.meltryllis.nf.ui.common.outline.OutlinePane;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * {@link javafx.scene.control.TabPane} 下的「格式化工具」 {@code Tab}。
 *
 * @author Zachary W
 * @date 2025/3/1
 */
@Slf4j
public class FormatterPaneController implements Initializable {

    @FXML
    public OutlinePane root;
    @FXML
    public Label       inputFormatLabel;
    @FXML
    public Label       chapterTemplateLabel;
    @FXML
    public Label       chapterInputFormatTitleLabel;
    @FXML
    public Label       exportConfigLabel;
    @FXML
    public Label       paragraphLabel;
    @FXML
    public Label       othersLabel;
    @FXML
    public Button      exportButton;

    private void initTexts() {
        inputFormatLabel.textProperty().bind(I18nUtil.createStringBinding("App.Formatter.Input.Label.Text"));
        chapterInputFormatTitleLabel.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Input.Chapter.Label.Text"));
        exportConfigLabel.textProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.Label.Text"));
        paragraphLabel.textProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.Paragraph.Label.Text"));
        chapterTemplateLabel.textProperty()
                .bind(I18nUtil.createStringBinding("App.Formatter.Output.ChapterFormat.Title"));
        exportButton.textProperty().bind(I18nUtil.createStringBinding("Common.Export"));
        othersLabel.textProperty().bind(I18nUtil.createStringBinding("Common.Others"));
    }

    private void initOutline() {
        root.addOutlineEntry(inputFormatLabel, 1);
        root.addOutlineEntry(chapterInputFormatTitleLabel, 2);
        root.addOutlineEntry(exportConfigLabel, 1);
        root.addOutlineEntry(paragraphLabel, 2);
        root.addOutlineEntry(chapterTemplateLabel, 2);
        root.addOutlineEntry(othersLabel, 2);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTexts();
        initOutline();
        root.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        root.setOnDragDropped(event -> {
            for (File file : event.getDragboard().getFiles()) {
                if (InputFormatProperty.getInstance().setFile(file)) {
                    break;
                }
            }
            event.consume();
        });
    }

    public void export() {
        long start = System.currentTimeMillis();
        File formatFile = InputFormatProperty.getInstance().getFile();
        try {
            ParseProcessor parseProcessor = new ParseProcessor(formatFile);
            if (parseProcessor.format()) {
                DialogUtil.FXMLBuilder<File> builder = new DialogUtil.FXMLBuilder<>("/fxml/dialog/format-success.fxml");
                builder.setTitle(I18nUtil.createStringBinding("Dialog.FormatSuccess.Title")).setInitialValue(formatFile)
                        .show();
            }
        } catch (IOException e) {
            DialogUtil.FXMLBuilder<String> failMessageBuilder = new DialogUtil.FXMLBuilder<>(
                    "/fxml/dialog/format-fail.fxml");
            failMessageBuilder.setTitle(I18nUtil.createStringBinding("Dialog.FormatFail.Title"))
                    .setInitialValue(e.getMessage()).show();
        }
        log.info(StrUtil.format("Format file success. Time: {0}ms.", System.currentTimeMillis() - start));
    }

}
