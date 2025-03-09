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

/**
 * 具有不重复元素的 {@code ObservableList}。
 *
 * @author Zachary W
 * @date 2025/2/17
 */
@Slf4j
@Getter(AccessLevel.PRIVATE)
public class UniqueObservableList<E> extends ModifiableObservableListBase<E> {

    private final List<E>               elements;
    @Getter
    @JsonIgnore
    private final SimpleIntegerProperty sizeProperty;
    @Setter
    @JsonIgnore
    private       Comparator<E>         comparator;

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
    public E get(int index) {
        return getElements().get(index);
    }

    @Override
    public int size() {
        return getElements().size();
    }

    @Override
    protected void doAdd(int index, E element) {
        if (!contains(element)) {
            getElements().add(index, element);
            if (getComparator() != null) {
                getElements().sort(getComparator());
            }
            return;
        }
        throw new IllegalArgumentException(StrUtil.format("Add failed. Element {0} already exists.", element));
    }

    @Override
    protected E doSet(int index, E element) {
        if (!contains(element)) {
            return getElements().set(index, element);
        }
        throw new IllegalArgumentException(StrUtil.format("Set failed. Element {0} already exists.", element));
    }

    @Override
    protected E doRemove(int index) {
        return getElements().remove(index);
    }

}
