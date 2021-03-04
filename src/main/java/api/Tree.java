package api;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public interface Tree<E> extends Iterable<E> {

    boolean add(E element);

    boolean remove(E element);

    int height();

    boolean contains(E element);

    boolean isEmpty();

    int size();

    void addAll(Collection<? extends E> collection);

    boolean containsAll(Collection<? extends E> collection);

    Iterator<E> inOrderIterator();

    Iterator<E> preOrderIterator();

    Iterator<E> postOrderIterator();

    Object[] toSortedArray();

    <T> T[] toSortedArray(T[] array);

    E findMax();

    void clear();

    void forEach(Consumer<? super E> action);
}
