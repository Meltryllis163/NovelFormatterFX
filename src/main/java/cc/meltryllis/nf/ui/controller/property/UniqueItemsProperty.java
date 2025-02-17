package cc.meltryllis.nf.ui.controller.property;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * {@code javafx.scene.control.ComboBox#items} 属性的监听增强类。
 * 新增以下特性：<p>
 * <ul>
 *     <li>元素不允许重复</li>
 *     <li>为列表的长度 <i>size</i> 新增监听属性</li>
 * </ul>
 *
 * @author Zachary W
 * @date 2025/2/17
 */
@Getter
public class UniqueItemsProperty<E> {
    private final SimpleObjectProperty<ObservableList<E>> items;
    @NotNull
    @Getter(AccessLevel.PRIVATE)
    private final ObservableList<E> observableList;
    private final SimpleIntegerProperty sizeProperty;

    public UniqueItemsProperty(@NotNull List<E> elements) {
        // 去重
        this.observableList = FXCollections.observableList(new ArrayList<>(new LinkedHashSet<>(elements)));
        items = new SimpleObjectProperty<>(getObservableList());
        sizeProperty = new SimpleIntegerProperty(getObservableList().size());
        observableList.addListener((ListChangeListener<E>) c -> sizeProperty.setValue(getObservableList().size()));
    }

    public boolean add(E element) {
        if (!getObservableList().contains(element)) {
            return getObservableList().add(element);
        }
        return false;
    }

    public boolean remove(E element) {
        return getObservableList().remove(element);
    }

}
