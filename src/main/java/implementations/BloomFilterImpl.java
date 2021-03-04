package implementations;

import api.BloomFilter;

import java.util.BitSet;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class BloomFilterImpl<E> implements BloomFilter<E> {

    private BitSet filter;
    private int memoryFootprintEstimate;
    private int numHashes;
    private int size;

    public BloomFilterImpl() {
        this(100_000L, 1e-7);
    }

    public BloomFilterImpl(long expectedElementCount) {
        this(expectedElementCount, 1e-7);
    }

    public BloomFilterImpl(long expectedElementCount, double desiredFalsePositiveRate) {
        while (true) {
            memoryFootprintEstimate = (int) Math.ceil((expectedElementCount * Math.log(desiredFalsePositiveRate)) / Math.log(1 / Math.pow(2, Math.log(2))));
            // attempt to meet desired false positive rate, but give precedence to system memory constraints
            if (memoryFootprintEstimate > (Runtime.getRuntime().freeMemory() * 7)) {
                desiredFalsePositiveRate *= 2;
            }
            else
                break;
        }
        filter = new BitSet(memoryFootprintEstimate);
        numHashes = (int) Math.round((double)(memoryFootprintEstimate / expectedElementCount) * Math.log(2D));
    }

    @Override
    public void add(E element) {
        Objects.requireNonNull(element);
        int[] hashCodes = generateHashCodes(element);
        for (int i : hashCodes)
            filter.set(Math.floorMod(i, filter.size()));
        size++;
    }

    @Override
    public boolean contains(E element) {
        Objects.requireNonNull(element);
        int[] hashCodes = generateHashCodes(element);
        for (int i : hashCodes) {
            if (!filter.get(Math.floorMod(hashCodes[i], filter.size())))
                return false;
        }
        return true;
    }

    private int[] generateHashCodes(E element) {
        int[] hashCodes = new int[numHashes];
        int hashVal = element.hashCode();
        for (int i = 0; i < numHashes; i++) {
            hashCodes[i] = Integer.hashCode(hashVal);
            hashVal = hashCodes[i];
        }
        return hashCodes;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public double expectedFalsePositiveRate() {
        // Uses Goel and Gupta's formula for an upper bound on the false positive rate of the bloom filter
        return Math.pow(1 - Math.pow(Math.E, ((numHashes * (size + 0.5)) / (filter.length() - 1))  * -1), numHashes);
    }

    @Override
    public void addAll(Collection<? extends E> collection) {
        collection.forEach(this::add);
    }

    @Override
    public void clear() {
        filter.clear();
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<? extends E> collection) {
        return collection.stream().allMatch(this::contains);
    }

    @Override
    public int getMemoryFootprintEstimate() {
        // returns the memory footprint in bytes
        return memoryFootprintEstimate / 8;
    }

    @Override
    public int getHashFunctionCount() {
        return numHashes;
    }
}
