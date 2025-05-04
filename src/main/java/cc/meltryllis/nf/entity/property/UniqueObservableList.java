package cc.meltryllis.nf.entity.property;

import cc.meltryllis.nf.utils.common.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ModifiableObservableListBase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

/**
 * 具有不重复元素的 {@code ObservableList}。
 *
 * @author Zachary W
 * @date 2025/2/17
 */
@Slf4j
@Getter(AccessLevel.PRIVATE)
public class UniqueObservableList<E> extends ModifiableObservableListBase<E> {

    @Setter
    @JsonIgnore
    private DuplicatePolicy duplicatePolicy = DuplicatePolicy.NONE;

    private final List<E>               elements;
    @Getter
    @JsonIgnore
    private final SimpleIntegerProperty sizeProperty;
    @Setter
    @JsonIgnore
    private       Comparator<E>         comparator;

    @Override
    public void addFirst(E e) {
        try {
            super.addFirst(e);
        } catch (IllegalArgumentException ignored) {

        }
    }

    @JsonCreator
    public UniqueObservableList(@JsonProperty("elements") @NotNull List<E> elements) {
        this.elements = distinctElements(elements);
        this.sizeProperty = new SimpleIntegerProperty(elements.size());
        addListener((ListChangeListener<E>) c -> sizeProperty.setValue(size()));
    }

    /**
     * 过滤传入列表中的重复项，使其仅保留一次。
     *
     * @param elements 被过滤的列表
     *
     * @return 过滤后的列表
     */
    private List<E> distinctElements(List<E> elements) {
        HashSet<E> set = new HashSet<>();
        elements.removeIf(element -> !set.add(element));
        return elements;
    }

    @JsonIgnore
    public List<E> toList() {
        return getElements().stream().toList();
    }

    @Override
    public boolean add(E e) {
        try {
            return super.add(e);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    protected void doAdd(int index, E element) {
        int curIndex = getElements().indexOf(element);
        // 不存在
        if (curIndex < 0) {
            getElements().add(index, element);
            if (getComparator() != null) {
                getElements().sort(getComparator());
            }
        } else {
            handleDuplicate(curIndex, element);
            throw new IllegalArgumentException(StrUtil.format("Add failed. This Element {0} already exists.", element));
        }
    }

    @Override
    public E get(int index) {
        return getElements().get(index);
    }

    @Override
    public int size() {
        return getElements().size();
    }

    @Override
    protected E doSet(int index, E element) {
        int curIndex = getElements().indexOf(element);
        if (curIndex < 0) {
            return getElements().set(index, element);
        }
        handleDuplicate(curIndex, element);
        throw new IllegalArgumentException(StrUtil.format("Set failed. Element {0} already exists.", element));
    }

    /**
     * 当添加或设置重复项时，根据 {@code duplicatePolicy} 处理。
     *
     * @param curIndex 重复项当前在列表中的索引。
     * @param element  重复项元素。
     */
    protected void handleDuplicate(int curIndex, E element) {
        if (getDuplicatePolicy() == DuplicatePolicy.SET_TO_FIRST && curIndex > 0) {
            remove(curIndex);
            addFirst(element);
        }
    }

    @Override
    protected E doRemove(int index) {
        return getElements().remove(index);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex > size()) {
            throw new IndexOutOfBoundsException("Index: " + fromIndex);
        }

        // return early if the range is empty
        if (fromIndex == toIndex) {
            return;
        }

        ListIterator<E> it = listIterator(fromIndex);
        for (int i = 0, n = toIndex - fromIndex; i < n; i++) {
            beginChange();
            try {
                E element = it.next();
                it.remove();
                nextRemove(i, element);
            } finally {
                endChange();
            }
        }
    }

    public enum DuplicatePolicy {
        NONE, SET_TO_FIRST
    }

}
