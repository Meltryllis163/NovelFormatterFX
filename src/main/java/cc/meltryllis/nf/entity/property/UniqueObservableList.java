package cc.meltryllis.nf.entity.property;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * 为JavaFX设计的带有一些监控属性的列表。
 * 具有特性：<p>
 * <ul>
 *     <li>元素不允许重复</li>
 *     <li>为列表的长度 <i>size</i> 新增监听属性</li>
 * </ul>
 *
 * @author Zachary W
 * @date 2025/2/17
 */
@Getter(AccessLevel.PRIVATE)
public class UniqueObservableList<E> {

    private final Comparator<? super E> comparator;

    private final ObservableList<E> observableList;

    @Getter(AccessLevel.PUBLIC)
    private final SimpleIntegerProperty sizeProperty;

    public UniqueObservableList(@NotNull List<E> elements) {
        this(elements, null);
    }

    public UniqueObservableList(@NotNull List<E> elements, Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.observableList = FXCollections.observableList(distinctElements(elements));
        this.sizeProperty = new SimpleIntegerProperty(observableList.size());
        getObservableList().addListener((ListChangeListener<E>) c -> sizeProperty.setValue(getObservableList().size()));
    }

    public boolean add(E element) {
        if (!getObservableList().contains(element)) {
            getObservableList().add(element);
            sort();
            return true;
        }
        return false;
    }

    public boolean remove(E element) {
        return getObservableList().remove(element);
    }

    public E remove(int index) {
        return getObservableList().remove(index);
    }

    public void asItems(TableView<E> tableView) {
        tableView.setItems(getObservableList());
    }

    public void asItems(ListView<E> listView) {
        listView.setItems(getObservableList());
    }

    public void asItems(ComboBox<E> comboBox) {
        comboBox.setItems(getObservableList());
    }

    private void sort() {
        if (getComparator() != null) {
            getObservableList().sort(getComparator());
        }
    }

    /**
     * 过滤传入列表中的重复项，使其仅保留一次。
     *
     * @param elements 被过滤的列表
     * @return 过滤后的列表
     */
    private List<E> distinctElements(List<E> elements) {
        HashSet<E> set = new HashSet<>();
        elements.removeIf(element -> !set.add(element));
        return elements;
    }
}
