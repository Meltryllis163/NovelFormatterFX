package cc.meltryllis.nf.utils;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

/**
 * JavaFX工具类。
 *
 * @author Zachary W
 * @date 2025/2/18
 */
public class FXUtil {

    public static void scrollVerticalToVisible(Node node) {
        Node container = node;
        // for (
        //         container = node;
        //         container != null && !(container instanceof ScrollPane);
        //         container = container.getParent()
        // );
        while (container != null && !(container instanceof ScrollPane)) {
            container = container.getParent();
        }
        if (container == null) {
            return;
        }
        ScrollPane scrollPane = (ScrollPane) container;

        scrollPane.applyCss();
        scrollPane.layout();

        Bounds bounds = scrollPane.getContent().sceneToLocal(node.localToScene(node.getBoundsInLocal()));

        double height = scrollPane.getContent().getBoundsInLocal().getHeight();
        double viewHeight = scrollPane.getViewportBounds().getHeight();

        if (viewHeight >= height) {
            return;
        }

        double yMin = bounds.getMinY();
        double yMax = bounds.getMaxY();
        double v = (scrollPane.getVvalue() - scrollPane.getVmin()) / (scrollPane.getVmax() - scrollPane.getVmin());

        double viewportMinInContent = v * (height - viewHeight);
        double viewportMaxInContent = viewportMinInContent + viewHeight;
        if (yMin < viewportMinInContent) {
            double newV = yMin / (height - viewHeight);
            scrollPane.setVvalue(scrollPane.getVmin() + newV * (scrollPane.getVmax() - scrollPane.getVmin()));
        } else if (yMax > viewportMaxInContent) {
            double newV = (yMax - viewHeight) / (height - viewHeight);
            scrollPane.setVvalue(scrollPane.getVmin() + newV * (scrollPane.getVmax() - scrollPane.getVmin()));
        }
    }
}
