package cc.meltryllis.nf.ui.common.outline;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * 拥有目录跳转功能的 {@code StackPane}
 *
 * @author Zachary W
 * @date 2025/3/13
 */
@Slf4j
@Getter
public class OutlinePane extends StackPane {

    public static final int        OUTLINE_WIDTH = 200;
    private final       ScrollPane contentScrollPane;
    private final       StackPane  contentStackPane;
    // private final        ScrollPane outlineScrollPane;
    private final       Outline    outline;
    private             VBox       content;

    // TODO 现在的滚动面板有个致命缺陷，那就是只能在contentScrollPane内才能滚动
    public OutlinePane() {

        contentStackPane = new StackPane();
        contentScrollPane = new ScrollPane(contentStackPane);
        contentScrollPane.setFitToWidth(true);
        contentScrollPane.vvalueProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                outline.selectEntry(getFirstVisibleScrollTarget());
            }
        });

        outline = new Outline(createScrollHandler());
        StackPane.setAlignment(outline, Pos.TOP_RIGHT);
        StackPane.setMargin(outline, new Insets(0, 20, 0, 0));

        getChildren().setAll(contentScrollPane, outline);
    }

    public void setContent(VBox content) {
        this.content = content;
        StackPane.setMargin(content, new Insets(20, OUTLINE_WIDTH + 50, 20, 50));
        this.contentStackPane.getChildren().setAll(content);
    }

    public void addOutlineEntry(@NotNull Label l, int level) {
        addOutlineEntry(l.textProperty(), l, level);
    }

    public void addOutlineEntry(@NotNull ObservableValue<? extends String> title, Node scrollTarget, int level) {
        outline.getChildren()
                .add(outline.getChildren().size() - 2, outline.createOutlineEntry(title, scrollTarget, level));
    }

    private Consumer<Node> createScrollHandler() {
        return node -> {
            if (node == null) {
                contentScrollPane.setVvalue(0);
                return;
            }
            Bounds boundsInParent = node.getBoundsInParent();
            double y = boundsInParent.getMinY();
            double height = contentScrollPane.getContent().getBoundsInLocal().getHeight();
            double viewHeight = contentScrollPane.getViewportBounds().getHeight();
            contentScrollPane.setVvalue(y / (height - viewHeight));
        };
    }

    private Node getFirstVisibleScrollTarget() {
        double viewMinY = contentScrollPane.getVvalue() * (contentScrollPane.getContent().getBoundsInLocal()
                .getHeight() - contentScrollPane.getViewportBounds().getHeight());
        for (int i = content.getChildren().size() - 1; i >= 0; i--) {
            Node child = content.getChildren().get(i);
            if (child.getProperties().containsKey(OutlineEntry.IS_SCROLL_TARGET)) {
                double targetMinY = child.getBoundsInParent().getMinY();
                if (viewMinY >= targetMinY - 50) {
                    return child;
                }
            }
        }
        return null;
    }

}
