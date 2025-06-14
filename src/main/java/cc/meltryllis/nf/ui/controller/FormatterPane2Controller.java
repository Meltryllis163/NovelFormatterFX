package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.formatter.FormatProcessor;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/5/27
 */
@Slf4j
public class FormatterPane2Controller implements Initializable {

    @FXML
    private VBox   root;
    @FXML
    private ToggleButton inputButton, outputButton;
    @FXML
    private HBox   inputBox, outputBox;
    @FXML
    private Button exportButton;

    public void export() {
        long start = System.currentTimeMillis();
        File formatFile = InputFormatProperty.getInstance().getFile();
        try {
            FormatProcessor formatProcessor = new FormatProcessor(formatFile);
            if (formatProcessor.format()) {
                log.info(StrUtil.format("Format file success. Time: {0}ms.", System.currentTimeMillis() - start));
                DialogUtil.DialogBuilder<File> builder = new DialogUtil.DialogBuilder<>(root.getScene().getWindow(),
                        "/fxml/dialog/format-success.fxml");
                builder.title(I18nUtil.createStringBinding("Dialog.FormatSuccess.Title")).initialValue(formatFile)
                        .show();
            }
        } catch (IOException e) {
            DialogUtil.DialogBuilder<Exception> failExceptionBuilder = new DialogUtil.DialogBuilder<>(
                    root.getScene().getWindow(), "/fxml/dialog/format-fail.fxml");
            failExceptionBuilder.title(I18nUtil.createStringBinding("Dialog.FormatFail.Title")).initialValue(e).show();
        }
        // DialogUtil.DialogBuilder<File> builder = new DialogUtil.DialogBuilder<>(root.getScene().getWindow(),
        //         "/fxml/dialog/format-success.fxml");
        // builder.title(I18nUtil.createStringBinding("Dialog.FormatSuccess.Title"))
        //         .initialValue(new File("C:\\Users\\Meltryllis\\Desktop\\万相之王.txt"))
        //         .type(DialogUtil.DialogType.SUCCESS).okButton(true).cancelButton(true).show();
    }

    private void initDragFileEvent() {
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

    private void initToggles() {
        inputButton.textProperty().bind(I18nUtil.createStringBinding("App.Formatter.Input.Label.Text"));
        outputButton.textProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.Label.Text"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // inputLabel.textProperty().bind(I18nUtil.createStringBinding("App.Formatter.Input.Label.Text"));
        // outputLabel.textProperty().bind(I18nUtil.createStringBinding("App.Formatter.Output.Label.Text"));
        initToggles();
        exportButton.textProperty().bind(I18nUtil.createStringBinding("Common.Export"));
        initDragFileEvent();

        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(inputButton, outputButton);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == null) {
                    group.selectToggle(oldValue);
                }
            }
        });
        inputBox.visibleProperty().bind(inputButton.selectedProperty());
        outputBox.visibleProperty().bind(outputButton.selectedProperty());
        inputButton.setSelected(true);
    }

}
