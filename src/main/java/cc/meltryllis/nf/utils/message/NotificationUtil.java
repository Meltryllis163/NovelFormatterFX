package cc.meltryllis.nf.utils.message;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.utils.I18nUtil;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
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

    /** 通知样式：ACCENT */
    public static final int ACCENT  = 0;
    /** 通知样式：SUCCESS */
    public static final int SUCCESS = 1;
    /** 通知样式：WARNING */
    public static final int WARNING = 2;
    /** 通知样式：DANGER */
    public static final int DANGER  = 3;
    /** 在格式化工具界面通知 */
    public static final int FORMATTER_PANE = 0;
    /** 在设置界面通知 */
    public static final int SETTINGS_PANE  = 1;
    private static final FontIcon FONT_ICON = new FontIcon("fth-help-circle");
    private static final Notification[] LAST_NOTIFICATIONS = new Notification[2];

    /** 格式化工具面板 */
    private static StackPane formatterPane;
    /** 设置面板 */
    private static StackPane settingsPane;


    public static void registerFormatterPane(@NotNull StackPane pane) {
        formatterPane = pane;
    }

    public static void registerSettingsPane(@NotNull StackPane pane) {
        settingsPane = pane;
    }

    @Nullable
    private static StackPane getStackPane(int type) {
        if (type == FORMATTER_PANE) {
            return formatterPane;
        } else if (type == SETTINGS_PANE) {
            return settingsPane;
        } else {
            return null;
        }
    }

    public static void show(int paneType, final int notificationType, @NotNull final String i18nKey,
                            @NotNull final Pos position) {
        // 前置检查
        StackPane stackPane = getStackPane(paneType);
        if (stackPane == null) {
            return;
        }
        removeLastNotification(stackPane, paneType);
        // 样式设置
        Notification notification = createNotification(notificationType, i18nKey);
        StackPane.setMargin(notification, UICons.NOTIFICATION_INSETS);
        StackPane.setAlignment(notification, position);
        // 动画
        Timeline in = createSlideInAnimations(notification, position);
        stackPane.getChildren().add(notification);
        setLastNotification(paneType, notification);
        if (in != null) {
            in.playFromStart();
        }
        notification.setOnClose(event -> {
            Timeline out = createSlideOutAnimations(notification, position);
            if (out != null) {
                out.setOnFinished(actionEvent -> stackPane.getChildren().remove(notification));
                out.playFromStart();
            }
        });
    }

    private static Notification createNotification(int notificationType, String i18nKey) {
        Notification notification = new Notification(null, FONT_ICON);
        notification.messageProperty().bind(I18nUtil.createStringBinding(i18nKey));
        notification.getStyleClass().add(Styles.ELEVATED_1);
        switch (notificationType) {
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

    private static void removeLastNotification(@NotNull StackPane pane, int paneType) {
        Notification lastNotification = getLastNotification(paneType);
        pane.getChildren().remove(lastNotification);
        LAST_NOTIFICATIONS[paneType] = null;
    }

    private static Notification getLastNotification(int paneType) {
        if (paneType >= FORMATTER_PANE && paneType <= SETTINGS_PANE) {
            return LAST_NOTIFICATIONS[paneType];
        }
        return null;
    }

    private static void setLastNotification(int paneType, Notification notification) {
        if (paneType >= FORMATTER_PANE && paneType <= SETTINGS_PANE) {
            LAST_NOTIFICATIONS[paneType] = notification;
        }
    }

    private static Timeline createSlideInAnimations(Notification notification, Pos position) {
        Timeline in = null;
        Duration millis = Duration.millis(250);
        if (Pos.BOTTOM_CENTER.equals(position)) {
            in = Animations.slideInUp(notification, millis);
        } else if (Pos.TOP_CENTER.equals(position)) {
            in = Animations.slideInDown(notification, millis);
        }
        return in;
    }

    private static Timeline createSlideOutAnimations(Notification notification, Pos position) {
        Timeline out = null;
        Duration millis = Duration.millis(250);
        if (Pos.BOTTOM_CENTER.equals(position)) {
            out = Animations.slideOutDown(notification, millis);
        } else if (Pos.TOP_CENTER.equals(position)) {
            out = Animations.slideOutUp(notification, millis);
        }
        return out;
    }
}
