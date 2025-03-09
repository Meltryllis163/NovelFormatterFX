package cc.meltryllis.nf.utils.message.dialog;

import cc.meltryllis.nf.ui.controller.dialog.StageDialogController;
import cc.meltryllis.nf.utils.FXUtil;
import cc.meltryllis.nf.utils.common.StrUtil;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * {@link javafx.scene.Scene} 弹窗通知工具。
 *
 * @author Zachary W
 * @date 2025/3/2
 */
@Slf4j
public class DialogUtil {

    public static Window owner;

    public static void registerOwner(Window window) {
        DialogUtil.owner = window;
    }

    public static void show(@Nullable StringBinding title, Pane contentPane) {
        show(title, contentPane, Type.NONE);
    }

    public static void show(@Nullable StringBinding title, @NotNull Pane contentPane, Type type) {
        StageDialog stage = StageDialog.builder(contentPane).setType(type).setTitle(title).build();
        stage.showAndWait();
    }

    public static void showMessage(@Nullable StringBinding title, @NotNull StringBinding message,
                                   @NotNull DialogUtil.Type type) {
        MessagesVBox messagesVBox = new MessagesVBox(List.of(message));
        StageDialog stageDialog = StageDialog.builder(messagesVBox).setTitle(title).setType(type).build();
        stageDialog.showAndWait();
    }

    @Nullable
    private static <T> T showFXML(@Nullable StringBinding title, @NotNull Type type, @NotNull String fxml,
                                  @Nullable T initialValue) {
        FXMLLoader loader = FXUtil.newFXMLLoader(fxml);
        try {
            Pane contentPane = loader.load();
            StageDialogController<T> stageDialogController = loader.getController();
            StageDialog stageDialog = StageDialog.builder(contentPane).setType(type).setTitle(title).build();
            return stageDialogController.registerStageDialog(initialValue, stageDialog);
        } catch (IOException e) {
            log.warn(StrUtil.format("Load fxml({0}) failed. Return null.", fxml), e);
            return null;
        }
    }

    public enum Type {
        NONE, ACCENT, SUCCESS, WARNING, DANGER
    }

    public static class FXMLBuilder<T> {

        @NotNull
        private final String fxml;

        @Nullable
        private StringBinding title;
        @Nullable
        private T             initialValue;

        @NotNull
        private Type type = Type.NONE;

        public FXMLBuilder(@NotNull String fxml) {
            this.fxml = fxml;
        }

        public FXMLBuilder<T> setTitle(@Nullable StringBinding title) {
            this.title = title;
            return this;
        }

        public FXMLBuilder<T> setType(@NotNull Type type) {
            this.type = type;
            return this;
        }

        public FXMLBuilder<T> setInitialValue(@Nullable T initialValue) {
            this.initialValue = initialValue;
            return this;
        }

        public T show() {
            return showFXML(title, type, fxml, initialValue);
        }

    }

}
