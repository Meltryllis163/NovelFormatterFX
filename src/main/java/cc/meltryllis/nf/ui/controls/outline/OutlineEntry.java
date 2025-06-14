package cc.meltryllis.nf.ui.controls.outline;

import cc.meltryllis.nf.constants.MyStyles;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 适用于 {@link Outline 的元素。
 * <p>
 *
 * @author Zachary W
 * @date 2025/3/16
 */
@Getter(AccessLevel.PROTECTED)
public class OutlineEntry extends Label {

    public static final String IS_SCROLL_TARGET = "isScrollTarget";
    private final       Node   scrollTarget;

    public OutlineEntry(@NotNull Outline outline, ObservableValue<? extends String> title, Node scrollTarget,
                        int level) {
        this.scrollTarget = scrollTarget;
        textProperty().bind(title);
        getStyleClass().add(MyStyles.Outline.PREFIX_TITLE + level);
        if (scrollTarget != null) {
            scrollTarget.getProperties().put(IS_SCROLL_TARGET, true);
        }
        setOnMouseClicked(event -> outline.getScrollHandler().accept(scrollTarget));
    }

}
