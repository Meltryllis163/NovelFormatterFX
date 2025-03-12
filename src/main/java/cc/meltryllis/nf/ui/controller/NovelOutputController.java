package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import cc.meltryllis.nf.parser.ParseProcessor;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.ui.controller.dialog.FormatFailController;
import cc.meltryllis.nf.ui.controller.dialog.FormatSuccessController;
import cc.meltryllis.nf.utils.FXUtil;
import cc.meltryllis.nf.utils.common.StrUtil;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zachary W
 * @date 2025/2/12
 */
@Slf4j
public class NovelOutputController implements Initializable {

    @FXML
    public VBox   root;
    @FXML
    public Label  othersLabel;
    @FXML
    public Label  exportConfigLabel;
    @FXML
    public Button exportButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exportConfigLabel.textProperty().bind(I18nUtil.createStringBinding("App.Output.Label.Text"));
        exportButton.textProperty().bind(I18nUtil.createStringBinding("Common.Export"));
        othersLabel.textProperty().bind(I18nUtil.createStringBinding("Common.Others"));
    }

    private VBox createFormatSuccessDialogContent(File sourceFile) {
        FXMLLoader fxmlLoader = FXUtil.newFXMLLoader("/fxml/dialog/format-success.fxml");
        try {
            VBox contentBox = fxmlLoader.load();
            FormatSuccessController formatSuccessController = fxmlLoader.getController();
            formatSuccessController.setFormatFile(sourceFile);
            return contentBox;
        } catch (IOException e) {
            log.debug("Load format-success.fxml failed.", e);
            return null;
        }
    }

    private VBox createFormatFailDialogContent(IOException exception) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/dialog/format-fail.fxml"));
        try {
            VBox contentBox = fxmlLoader.load();
            Object controller = fxmlLoader.getController();
            if (controller instanceof FormatFailController formatFailController) {
                formatFailController.setFailException(exception.getMessage());
            }
            return contentBox;
        } catch (IOException e) {
            log.debug("Load format-fail.fxml failed.", e);
            return null;
        }
    }

    public void export() {
        long start = System.currentTimeMillis();
        File formatFile = InputFormatProperty.getInstance().getFile();
        try {
            ParseProcessor parseProcessor = new ParseProcessor(formatFile);
            if (parseProcessor.format()) {
                VBox exportSuccessDialog = createFormatSuccessDialogContent(formatFile);
                if (exportSuccessDialog != null) {
                    DialogUtil.show(I18nUtil.createStringBinding("Dialog.FormatSuccess.Title"), exportSuccessDialog,
                            DialogUtil.Type.SUCCESS);
                }
            }
        } catch (IOException e) {
            VBox exportSuccessDialog = createFormatFailDialogContent(e);
            if (exportSuccessDialog != null) {
                DialogUtil.show(I18nUtil.createStringBinding("Dialog.FormatFail.Title"), exportSuccessDialog,
                        DialogUtil.Type.WARNING);
            }
        }
        log.info(StrUtil.format("Format file success. Time: {0}ms.", System.currentTimeMillis() - start));
    }
}
