package cc.meltryllis.nf.entity.property;

import cn.hutool.core.util.StrUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
public class UniqueListProperty<E> extends SimpleObjectProperty<ObservableList<E>> implements Iterable<E> {

    private final Comparator<? super E> comparator;

    @Getter(AccessLevel.PUBLIC)
    private final SimpleIntegerProperty sizeProperty;

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
    public @NotNull Iterator<E> iterator() {
        return new UniqueListIterator();
    }

    /**
     * {@link UniqueListProperty}迭代器。
     *
     * @author Zachary W
     * @date 2025/2/23
     */
    @Getter(AccessLevel.PRIVATE)
    private class UniqueListIterator implements Iterator<E> {

        private int lastSet;
        private int index;

        public UniqueListIterator() {
            this.lastSet = -1;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return getIndex() < size();
        }

        @Override
        public E next() {
            int i = getIndex();
            if (i >= size()) {
                throw new NoSuchElementException(
                        StrUtil.format("Cannot get element at index {}, only {} elements.", index,
                                size()));
            }
            index = i + 1;
            return get(lastSet = i);
        }

        @Override
        public void remove() {
            if (lastSet < 0) {
                throw new IllegalStateException();
            }
            UniqueListProperty.this.remove(lastSet);
            index = lastSet;
            lastSet = -1;
        }
    }
}
