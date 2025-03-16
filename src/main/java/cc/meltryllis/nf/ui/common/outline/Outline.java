package cc.meltryllis.nf.ui.common.outline;

import atlantafx.base.theme.Styles;
import cc.meltryllis.nf.constants.MyStyles;
import cc.meltryllis.nf.constants.UICons;
import cc.meltryllis.nf.utils.i18n.I18nUtil;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

/**
 * 用于跳转的程序目录。
 *
 * @author Zachary W
 * @date 2025/3/16
 */
@Getter
public class Outline extends VBox {

    private final Consumer<Node> scrollHandler;
    PseudoClass SELECTED = PseudoClass.getPseudoClass(MyStyles.Outline.SELECTED);
    private Label selectedEntry;

    public Outline(Consumer<Node> scrollHandler) {
        super(UICons.DEFAULT_SPACING);
        getStyleClass().add(MyStyles.Outline.OUTLINE);
        this.scrollHandler = scrollHandler;
        Separator separator = new Separator();
        separator.getStyleClass().add(Styles.SMALL);
        Label topLabel = createOutlineEntry(I18nUtil.createStringBinding("App.Formatter.Outline.Top"), null, 2);
        topLabel.setGraphic(new FontIcon(Feather.CHEVRON_UP));

        getChildren().addAll(separator, topLabel);
        setMinWidth(OutlinePane.OUTLINE_WIDTH);
        setMaxWidth(OutlinePane.OUTLINE_WIDTH);

        // 允许滚动事件
        setPickOnBounds(false);
    }

    protected OutlineEntry createOutlineEntry(@NotNull ObservableValue<? extends String> titleObservableValue,
                                              Node scrollTarget, int level) {
        OutlineEntry entry = new OutlineEntry(this, titleObservableValue, scrollTarget, level);
        if (getChildren().size() == 2) {
            entry.pseudoClassStateChanged(SELECTED, true);
            selectedEntry = entry;
        }
        return entry;
    }

    protected void selectEntry(Node scrollTarget) {
        if (scrollTarget == null) {
            return;
        }
        if (selectedEntry != null) {
            selectedEntry.pseudoClassStateChanged(SELECTED, false);
        }
        for (Node child : getChildren()) {
            if (child instanceof OutlineEntry entry && entry.getScrollTarget() == scrollTarget) {
                selectedEntry = entry;
                entry.pseudoClassStateChanged(SELECTED, true);
                break;
            }
        }
    }

}
