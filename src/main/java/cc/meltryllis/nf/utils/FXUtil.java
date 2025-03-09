package cc.meltryllis.nf.utils;

import cc.meltryllis.nf.ui.MainApplication;
import javafx.css.Styleable;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

/**
 * JavaFX工具类。
 *
 * @author Zachary W
 * @date 2025/2/18
 */
public class FXUtil {

    /**
     * 向样式列表中添加样式，不会重复添加。
     *
     * @param styleable 具有样式列表的对象。
     * @param styles    新样式列表。
     */
    public static void addStyleClass(Styleable styleable, String... styles) {
        if (styleable == null) {
            return;
        }
        for (String style : styles) {
            if (!styleable.getStyleClass().contains(style)) {
                styleable.getStyleClass().add(style);
            }
        }
    }

    /**
     * 从样式列表中删除样式，重复样式会全部删除。
     *
     * @param styleable 具有样式列表的对象。
     * @param styles    删除样式列表。
     */
    public static void removeAllStyleClass(Styleable styleable, String... styles) {
        if (styleable == null) {
            return;
        }
        styleable.getStyleClass().removeAll(styles);
    }

    public static FontIcon createFontIcon(String iconCode) {
        return new FontIcon(iconCode);
    }

    public static Image newImage(String path) {
        return new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream(path)));
    }

    public static FXMLLoader newFXMLLoader(String path) {
        return new FXMLLoader(MainApplication.class.getResource(path));
    }

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
