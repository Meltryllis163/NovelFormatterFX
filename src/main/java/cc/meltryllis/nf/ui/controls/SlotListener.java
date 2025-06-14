package cc.meltryllis.nf.ui.controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

/**
 * 从 {@link atlantafx.base.controls.SlotListener} 复制。一个用于监听 {@code Pane} 内组件是否变化的监听器。
 * <p>
 * 该监听器实现以下功能：
 * <ul>
 *     <li>当 {@code Pane} 内组件发生变化时触发该监听器。</li>
 *     <li> {@code Pane} 内只允许存在一个组件。</li>
 *     <li> {@code Pane} 内不存在组件时，让面板不可见且不可被布局管理。</li>
 * </ul>
 *
 * @author Zachary W
 * @date 2025/6/4
 */
public class SlotListener implements ChangeListener<Node> {

    private final           Pane                      slot;
    private final @Nullable BiConsumer<Node, Boolean> onContentUpdate;

    /**
     * Creates a new listener and binds it to the specified container.
     *
     * @param slot The container for user-specified node.
     */
    public SlotListener(Pane slot) {
        this(slot, null);
    }

    /**
     * Creates a new listener and binds it to the specified container.
     * Also, it registers the custom callback handler that will be notified
     * upon the container content changed.
     *
     * @param slot            The container for user-specified node.
     * @param onContentUpdate The callback handler to be notified upon
     *                        the container content changing.
     */
    public SlotListener(@NotNull Node slot, @Nullable BiConsumer<Node, Boolean> onContentUpdate) {

        this.onContentUpdate = onContentUpdate;

        if (slot instanceof Pane pane) {
            this.slot = pane;
        } else {
            throw new IllegalArgumentException("Invalid slot type. Pane is required.");
        }
    }

    @Override
    public void changed(ObservableValue<? extends Node> obs, Node old, Node val) {
        if (val != null) {
            slot.getChildren().setAll(val);
        } else {
            slot.getChildren().clear();
        }
        slot.setVisible(val != null);
        slot.setManaged(val != null);

        if (onContentUpdate != null) {
            onContentUpdate.accept(val, val != null);
        }
    }

}
