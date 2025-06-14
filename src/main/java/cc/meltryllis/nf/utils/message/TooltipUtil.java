package cc.meltryllis.nf.utils.message;

import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.animation.PauseTransition;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/**
 * {@link Tooltip} 通知工具。
 *
 * @author Zachary W
 * @date 2025/3/1
 */
public class TooltipUtil {

    public static final int OFFSET = 5;

    public static void show(Control control, String i18nKey, Pos pos) {
        show(control, i18nKey, pos, 1000);
    }

    public static void show(Control control, String i18nKey, Pos pos, double hideDelay) {
        Point2D p = control.localToScene(0, 0);
        final Tooltip customTooltip = new Tooltip();
        customTooltip.textProperty().bind(I18nUtil.createStringBinding(i18nKey));
        double anchorX = p.getX() + control.getScene().getX() + control.getScene().getWindow().getX();
        if (pos == Pos.RIGHT) {
            anchorX += control.getWidth() + OFFSET;
        }
        double anchorY = p.getY() + control.getScene().getY() + control.getScene().getWindow().getY();
        if (pos == Pos.BOTTOM) {
            anchorY += control.getHeight() + OFFSET;
        }
        customTooltip.setAutoHide(true);
        customTooltip.show(control.getScene().getWindow(), anchorX, anchorY);
        // 定时关闭
        PauseTransition pauseTransition = new PauseTransition(Duration.millis(hideDelay));
        pauseTransition.setOnFinished(event -> customTooltip.hide());
        pauseTransition.play();
    }

    public enum Pos {
        BOTTOM, RIGHT
    }

}
