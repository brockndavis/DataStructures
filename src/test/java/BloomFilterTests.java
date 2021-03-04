import api.BloomFilter;
import implementations.BloomFilterImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class BloomFilterTests {

    private BloomFilter<String> filter;

    @BeforeEach
    void setUp() {
        filter = new BloomFilterImpl<>(1_000_000, 1e-7);
    }

    @Test
    void addTest() {
        assertFalse(filter.contains("Brock"));
        filter.add("Brock");
        assertTrue(filter.contains("Brock"));
        assertEquals(1, filter.size());
        filter.add("Brock");
        assertEquals(2, filter.size());
    }

    @Test
    void containsTest() {
        assertEquals(0, filter.size());
        filter.addAll(List.of("Brock", "Steve", "Bill", "Kate", "William", "Henry", "Kim"));
        assertTrue(filter.contains("Brock"));
        assertTrue(filter.contains("Steve"));
        assertTrue(filter.contains("Bill"));
        assertTrue(filter.contains("Kate"));
        assertTrue(filter.contains("William"));
        assertTrue(filter.contains("Henry"));
        assertTrue(filter.contains("Kim"));
        assertEquals(7, filter.size());
    }

    @Test
    void containsAllTest() {
        List<String> collection = List.of("Michael", "Jack", "Ashley", "Sydney", "Eric", "Peter");
        filter.addAll(collection);
        assertTrue(filter.containsAll(collection));
    }

    @Test
    void clearTest() {
        List<String> addList = List.of("Michael", "Jack", "Ashley", "Sydney", "Eric", "Peter", "Brock", "Steve", "Bill", "Kate", "William", "Henry", "Kim");
        assertTrue(filter.isEmpty());
        filter.addAll(addList);
        assertEquals(addList.size(), filter.size());
        filter.clear();
        assertTrue(filter.isEmpty());
        assertEquals(0, filter.size());
    }

    @Test
    void memoryTest() {
        assertTrue((filter.getMemoryFootprintEstimate() > 0) && (filter.getMemoryFootprintEstimate() < Runtime.getRuntime().freeMemory()));
    }

    @Test
    void falsePositiveTest() {
        BloomFilter<Integer> bloomFilter = new BloomFilterImpl<>(1_000_000, 1e-7);
        new Random().ints(1_000_000).forEach(bloomFilter::add);
        DecimalFormat format = new DecimalFormat("0E0");
        double actualFalsePositive = Double.parseDouble(format.format(bloomFilter.expectedFalsePositiveRate()));
        assertEquals(1e-7, actualFalsePositive);
    }
}
