package cc.meltryllis.nf.utils.message.dialog;

import cc.meltryllis.nf.ui.controller.dialog.AbstractStageDialogController;
import cc.meltryllis.nf.ui.controller.dialog.MessagesController;
import cc.meltryllis.nf.ui.controls.Message;
import cc.meltryllis.nf.ui.controls.StageDialog;
import cc.meltryllis.nf.utils.FXUtil;
import cc.meltryllis.nf.utils.common.StrUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * {@link javafx.scene.Scene} 弹窗通知工具。
 *
 * @author Zachary W
 * @date 2025/3/2
 */
@Slf4j
public class DialogUtil {


    public static boolean showChoice(@NotNull Window owner, Message.Type dialogType, ObservableValue<String> title,
                                     boolean initialValue, ObservableValue<String>... messages) {
        FXMLLoader loader = FXUtil.newFXMLLoader("/fxml/dialog/messages.fxml");
        try {
            Node content = loader.load();
            MessagesController stageDialogController = loader.getController();
            stageDialogController.setMessages(messages);
            StageDialog stageDialog = StageDialog.builder(owner, content).type(dialogType).title(title).build();
            return stageDialogController.registerStageDialog(initialValue, stageDialog);
        } catch (IOException e) {
            log.warn(StrUtil.format("Load fxml({0}) failed. Return null."), e);
            return initialValue;
        }
    }

    public static void showMessage(@NotNull Window owner, Message.Type dialogType, ObservableValue<String> title,
                                   ObservableValue<String>... messages) {
        showChoice(owner, dialogType, title, false, messages);
    }


    @Nullable
    private static <T> T showFXML(@NotNull Window owner, @Nullable ObservableValue<String> title,
                                  @NotNull Message.Type dialogType, @NotNull String fxml,
                                  @Nullable T initialValue, boolean okButton, boolean cancelButton) {
        FXMLLoader loader = FXUtil.newFXMLLoader(fxml);
        try {
            Node content = loader.load();
            AbstractStageDialogController<T> stageDialogController = loader.getController();
            StageDialog stageDialog = StageDialog.builder(owner, content).type(dialogType).title(title)
                    .okButton(okButton).cancelButton(cancelButton).build();
            return stageDialogController.registerStageDialog(initialValue, stageDialog);
        } catch (IOException e) {
            log.warn(StrUtil.format("Load fxml({0}) failed. Return null.", fxml), e);
            return null;
        }
    }

    public static class DialogBuilder<T> {

        @NotNull
        private final Window owner;
        @NotNull
        private final String fxml;

        private final StringProperty titleProperty = new SimpleStringProperty();

        @Nullable
        private T initialValue;

        @NotNull
        private Message.Type dialogType = Message.Type.NONE;
        private boolean      okButton   = false;
        private boolean               cancelButton = false;

        public DialogBuilder(@NotNull Window owner, @NotNull String fxml) {
            this.owner = owner;
            this.fxml = fxml;
        }

        public DialogBuilder<T> title(String title) {
            this.titleProperty.setValue(title);
            return this;
        }

        public DialogBuilder<T> title(ObservableValue<String> titleProperty) {
            this.titleProperty.bind(titleProperty);
            return this;
        }

        public DialogBuilder<T> type(@NotNull Message.Type dialogType) {
            this.dialogType = dialogType;
            return this;
        }

        public DialogBuilder<T> initialValue(@Nullable T initialValue) {
            this.initialValue = initialValue;
            return this;
        }

        public DialogBuilder<T> okButton(boolean okButton) {
            this.okButton = okButton;
            return this;
        }

        public DialogBuilder<T> cancelButton(boolean cancelButton) {
            this.cancelButton = cancelButton;
            return this;
        }

        public T show() {
            return showFXML(owner, titleProperty, dialogType, fxml, initialValue, okButton, cancelButton);
        }

    }

}
