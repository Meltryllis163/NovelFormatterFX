package cc.meltryllis.nf.entity.property;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * 为JavaFX设计的可监控 {@link List}。
 * 新增以下特性：<p>
 * <ul>
 *     <li>元素不允许重复</li>
 *     <li>为列表的长度 <i>size</i> 新增监听属性</li>
 * </ul>
 *
 * @author Zachary W
 * @date 2025/2/17
 */
@Getter(AccessLevel.PRIVATE)
public class UniqueListProperty<E> extends SimpleObjectProperty<ObservableList<E>> implements Iterable<E> {

    private final Comparator<? super E> comparator;

    @Getter(AccessLevel.PUBLIC)
    private final SimpleIntegerProperty sizeProperty;

    public UniqueListProperty() {
        this(new ArrayList<>());
    }

    /**
     * @param elements 传入的数据列表，注意这是一个<b>引用拷贝</b>。即编辑操作都直接作用与传入的列表，传入过程中不存在克隆与复制。
     */
    public UniqueListProperty(@NotNull List<E> elements) {
        this(elements, null);
    }

    /**
     * @param elements 传入的数据列表，注意这是一个<b>引用拷贝</b>。即编辑操作都直接作用于传入的列表，传入过程中不存在克隆与复制。
     */
    public UniqueListProperty(@NotNull List<E> elements, Comparator<? super E> comparator) {
        setValue(FXCollections.observableList(distinctElements(elements)));
        this.comparator = comparator;
        this.sizeProperty = new SimpleIntegerProperty(getValue().size());
        getValue().addListener((ListChangeListener<E>) c -> sizeProperty.setValue(getValue().size()));
    }

    public int size() {
        return getSizeProperty().getValue();
    }

    public E get(int index) {
        return getValue().get(index);
    }

    public boolean contains(E element) {
        return getValue().contains(element);
    }

    public boolean add(E element) {
        if (!getValue().contains(element)) {
            getValue().add(element);
            if (getComparator() != null) {
                getValue().sort(comparator);
            }
            return true;
        }
        return false;
    }

    public boolean remove(E element) {
        return getValue().remove(element);
    }

    @SuppressWarnings("UnusedReturnValue")
    public E remove(int index) {
        return getValue().remove(index);
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

    @Override
    public @NotNull UniqueListIterator<E> iterator() {
        return new UniqueListIterator<>(this);
    }
}
