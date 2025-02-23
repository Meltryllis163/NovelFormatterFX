package cc.meltryllis.nf.ui.common;

import javafx.scene.control.ListView;
import lombok.Setter;

/**
 * @author Zachary W
 * @date 2025/2/20
 */
@Setter
public abstract class FXMLListCellController<T> {

    private ListView<T> listView;

    protected abstract void setListItem(T item);
}
