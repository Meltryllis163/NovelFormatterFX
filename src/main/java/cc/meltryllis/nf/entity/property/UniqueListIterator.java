package cc.meltryllis.nf.entity.property;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link UniqueListProperty}迭代器。
 *
 * @author Zachary W
 * @date 2025/2/23
 */
@Getter(AccessLevel.PRIVATE)
public class UniqueListIterator<E> implements Iterator<E> {

    private final UniqueListProperty<E> uniqueListProperty;

    private int index;

    public UniqueListIterator(UniqueListProperty<E> uniqueListProperty) {
        this.uniqueListProperty = uniqueListProperty;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return getIndex() < uniqueListProperty.size();
    }

    @Override
    public E next() throws NoSuchElementException {
        if (hasNext()) {
            return getUniqueListProperty().get(index++);
        }
        throw new NoSuchElementException("Only " + getUniqueListProperty().size() + " elements.");
    }
}
