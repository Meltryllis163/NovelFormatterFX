package cc.meltryllis.nf.ui.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * @author Zachary W
 * @date 2025/2/19
 */
@Slf4j
public class FXMLListCell<T> extends ListCell<T> {

    private final String fxml;
    @Nullable
    private Parent root;

    private FXMLListCellController<T> controller;

    /**
     * 构造表上某一列的视图
     *
     * @param fxml 自定义控件的FXML资源定位符，如果为空，则简单展示文本
     */
    public FXMLListCell(@Nullable String fxml) {
        this.fxml = fxml;
    }

    /**
     * 该方法从构造器从转移到 {@link #updateItem(Object, boolean)} 中。
     * 保证每次更新 {@code ListView} 时显示的组件对象都是新的，避免 {@code ListCell} 复用带来的组件对象重复，产生的修改也重复。
     */
    private void loadRoot() {
        if (fxml != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            try {
                this.root = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                log.debug("Read fxml failed. Set root to null.", e);
                this.root = null;
            }
        }
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            // 空行
            setText(null);
            setGraphic(null);
        } else {
            loadRoot();
            if (root == null) {
                setText(item.toString());
            } else {
                setGraphic(root);
                controller.setListItem(item);
                controller.setListView(getListView());
            }
        }
    }
}