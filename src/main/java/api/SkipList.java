package api;

import java.util.Collection;

public interface SkipList<E> extends Iterable<E> {

    boolean add(E e);

    boolean addAll(Collection<? extends E> collection);

    boolean contains(E e);

    boolean containsAll(Collection<? extends E> collection);

    boolean remove(E e);

    boolean removeAll(Collection<? extends E> collection);

    void clear();

    int size();

    boolean isEmpty();

    Object[] toArray();

    <T> T[] toArray(T[] array);

    int getHeight();
}
