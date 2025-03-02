package cc.meltryllis.nf.utils.message;

import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.ui.MainApplication;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

/**
 * {@link javafx.scene.Scene} 弹窗通知工具。
 *
 * @author Zachary W
 * @date 2025/3/2
 */
public class DialogUtil {

    private static Window owner;

    public static void registerOwner(Window window) {
        DialogUtil.owner = window;
    }

    public static void show(@NotNull String i18nKey, @NotNull Type type) {
        show(null, i18nKey, type);
    }

    public static void show(@Nullable String titleI18nKey, @NotNull String msgI18nKey, @NotNull Type type) {
        Label contentLabel = new Label(null);
        contentLabel.setWrapText(true);
        contentLabel.textProperty().bind(I18nUtil.createStringBinding(msgI18nKey));
        show(titleI18nKey, contentLabel, type);
    }

    public static void show(@Nullable String titleI18nKey, Node content, @NotNull Type type) {
        Stage stage = new Stage();

        HBox contentBox = new HBox(UICons.LARGE_SPACING);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        FontIcon dialogIcon = createDialogIcon(type);
        if (dialogIcon != null) {
            contentBox.getChildren().add(dialogIcon);
        }
        contentBox.getChildren().add(content);
        contentBox.setMinHeight(UICons.DIALOG_CONTENT_MIN_HEIGHT);
        VBox vBox = new VBox(contentBox);
        vBox.setPadding(UICons.DIALOG_INSETS);
        Scene scene = new Scene(vBox);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setIconified(false);
        stage.initOwner(owner);
        if (titleI18nKey != null) {
            stage.titleProperty().bind(I18nUtil.createStringBinding(titleI18nKey));
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(UICons.DIALOG_MIN_WIDTH);
        stage.setMaxWidth(UICons.DIALOG_MAX_WIDTH);
        Image icon = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/icons/icon.png")));
        stage.getIcons().add(icon);
        stage.showAndWait();
    }

    @Nullable
    private static FontIcon createDialogIcon(Type type) {
        FontIcon icon = switch (type) {
            case WARNING -> new FontIcon("fth-alert-circle:32:orange");
            case SUCCESS -> new FontIcon("fth-check-circle:32:green");
            case NONE -> null;
        };
        // 清除默认风格，让尺寸和颜色生效
        if (icon != null) {
            icon.getStyleClass().clear();
        }
        return icon;
    }

    private static HBox createButtons(Stage stage) {
        // 初始化Box
        HBox box = new HBox();
        box.setAlignment(Pos.BASELINE_RIGHT);
        // OK
        Button okButton = new Button();
        okButton.textProperty().bind(I18nUtil.createStringBinding("Common.OK"));
        okButton.setOnAction(event -> stage.close());
        // AddAll
        box.getChildren().addAll(okButton);
        return box;
    }

    public enum Type {
        WARNING, SUCCESS, NONE
    }
}
