package cc.meltryllis.nf.ui.controller;

import cc.meltryllis.nf.entity.property.input.InputFormatProperty;
import javafx.fxml.Initializable;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * {@link javafx.scene.control.TabPane} 下的「格式化工具」 {@code Tab}。
 *
 * @author Zachary W
 * @date 2025/3/1
 */
public class FormatterPaneController implements Initializable {

    public StackPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

}
