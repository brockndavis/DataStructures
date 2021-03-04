package api;

import java.util.Collection;

public interface BloomFilter<E> {

    void add(E element);

    boolean contains(E element);

    int size();

    double expectedFalsePositiveRate();

    void addAll(Collection<? extends E> collection);

    void clear();

    boolean isEmpty();

    boolean containsAll(Collection<? extends E> collection);

    int getMemoryFootprintEstimate();

    int getHashFunctionCount();
}
