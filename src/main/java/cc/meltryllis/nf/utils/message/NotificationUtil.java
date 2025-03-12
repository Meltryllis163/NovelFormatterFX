package cc.meltryllis.nf.utils.message;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.ui.controller.TabsController;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import cc.meltryllis.nf.utils.message.dialog.DialogUtil;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * {@link Notification} 通知工具。
 *
 * @author Zachary W
 * @date 2025/3/1
 */
public class NotificationUtil {

    private static final FontIcon     FONT_ICON = new FontIcon("fth-help-circle");
    private static       Notification LAST_NOTIFICATION;

    @Nullable
    private static StackPane getStackPane() {
        TabPane tabPane = TabsController.getTabPane();
        if (tabPane != null) {
            Node content = tabPane.getSelectionModel().getSelectedItem().getContent();
            if (content instanceof StackPane stackPane) {
                return stackPane;
            }
        }
        return null;
    }

    /**
     * 显示通知。
     *
     * @param notificationType 通知类型。
     * @param i18nKey          通知内容的国际化Key。
     * @param position         通知位置，目前仅支持 {@code BOTTOM_CENTER} 和 {@code TOP_CENTER}。
     */
    public static void show(final DialogUtil.Type notificationType, @NotNull final String i18nKey, @NotNull final Pos position) {
        // 前置检查
        StackPane stackPane = getStackPane();
        if (stackPane == null) {
            return;
        }
        removeLastNotification(stackPane);
        // 样式设置
        Notification notification = createNotification(notificationType, i18nKey);
        StackPane.setMargin(notification, UICons.NOTIFICATION_INSETS);
        StackPane.setAlignment(notification, position);
        // 动画
        Timeline in = createSlideInAnimations(notification, position);
        stackPane.getChildren().add(notification);
        LAST_NOTIFICATION = notification;
        if (in != null) {
            in.playFromStart();
        } else {
            stackPane.getChildren().add(notification);
        }
        notification.setOnClose(event -> {
            Timeline out = createSlideOutAnimations(notification, position);
            if (out != null) {
                out.setOnFinished(actionEvent -> stackPane.getChildren().remove(notification));
                out.playFromStart();
            } else {
                stackPane.getChildren().remove(notification);
            }
        });
    }

    private static Notification createNotification(DialogUtil.Type type, String i18nKey) {
        Notification notification = new Notification(null, FONT_ICON);
        notification.messageProperty().bind(I18nUtil.createStringBinding(i18nKey));
        notification.getStyleClass().add(Styles.ELEVATED_1);
        switch (type) {
            case ACCENT:
                notification.getStyleClass().add(Styles.ACCENT);
                break;
            case SUCCESS:
                notification.getStyleClass().add(Styles.SUCCESS);
                break;
            case WARNING:
                notification.getStyleClass().add(Styles.WARNING);
                break;
            case DANGER:
                notification.getStyleClass().add(Styles.DANGER);
                break;
            default:
                break;
        }
        notification.setPrefWidth(Region.USE_COMPUTED_SIZE);
        notification.setMaxHeight(Region.BASELINE_OFFSET_SAME_AS_HEIGHT);
        return notification;
    }

    private static void removeLastNotification(@NotNull StackPane stackPane) {
        if (LAST_NOTIFICATION != null) {
            stackPane.getChildren().remove(LAST_NOTIFICATION);
            LAST_NOTIFICATION = null;
        }
    }

    private static Timeline createSlideInAnimations(Notification notification, Pos position) {
        Timeline in = null;
        Duration millis = Duration.millis(250);
        if (VPos.BOTTOM.equals(position.getVpos())) {
            in = Animations.slideInUp(notification, millis);
        } else if (VPos.TOP.equals(position.getVpos())) {
            in = Animations.slideInDown(notification, millis);
        }
        return in;
    }

    private static Timeline createSlideOutAnimations(Notification notification, Pos position) {
        Timeline out = null;
        Duration millis = Duration.millis(250);
        if (VPos.BOTTOM.equals(position.getVpos())) {
            out = Animations.slideOutDown(notification, millis);
        } else if (VPos.TOP.equals(position.getVpos())) {
            out = Animations.slideOutUp(notification, millis);
        }
        return out;
    }
}
