import api.SkipList;
import implementations.SkipListImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class SkipListTests {

    private SkipList<String> skipList;

    @BeforeEach
    void setUp() {
        skipList = new SkipListImpl<>();
        skipList.addAll(List.of("Ben", "Katy", "Evan", "Jessica", "Bob", "Ashley"));
        System.out.println(skipList);
    }

    @RepeatedTest(10)
    void addTest() {
        System.out.println(skipList);
        assertEquals(6, skipList.size());
        assertTrue(skipList.add("Brock"));
        assertEquals(7, skipList.size());
    }

    @RepeatedTest(10)
    void addAllTest() {
        assertEquals(6, skipList.size());
        skipList.addAll(List.of("Dylan", "Mike", "Hannah"));
        assertTrue(skipList.contains("Dylan"));
        assertTrue(skipList.contains("Mike"));
        assertTrue(skipList.contains("Hannah"));
        assertEquals(9, skipList.size());
    }

    @RepeatedTest(10)
    void duplicateTest() {
        assertEquals(6, skipList.size());
        assertFalse(skipList.add("Katy"));
        assertEquals(6, skipList.size());
    }

    @RepeatedTest(10)
    void removeTest() {
        assertEquals(6, skipList.size());
        assertTrue(skipList.remove("Ben"));
        assertEquals(5, skipList.size());
        assertTrue(skipList.remove("Katy"));
        assertEquals(4, skipList.size());
        assertTrue(skipList.remove("Evan"));
        assertEquals(3, skipList.size());
        assertTrue(skipList.remove("Jessica"));
        assertEquals(2, skipList.size());
        assertTrue(skipList.remove("Bob"));
        assertEquals(1, skipList.size());
        assertTrue(skipList.remove("Ashley"));
        assertEquals(0, skipList.size());
        assertTrue(skipList.isEmpty());
    }

    @RepeatedTest(10)
    void removeAllTest() {
        assertEquals(6, skipList.size());
        List<String> removeList = List.of("Ben", "Katy", "Evan", "Jessica", "Bob", "Ashley");
        assertTrue(skipList.removeAll(removeList));
        assertTrue(skipList.isEmpty());
        assertEquals(0, skipList.size());
    }

    @RepeatedTest(10)
    void containsTest() {
        assertTrue(skipList.contains("Ben"));
        assertTrue(skipList.contains("Katy"));
        assertTrue(skipList.contains("Evan"));
        assertTrue(skipList.contains("Jessica"));
        assertTrue(skipList.contains("Bob"));
        assertTrue(skipList.contains("Ashley"));
        assertFalse(skipList.contains("ben"));
        assertFalse(skipList.contains("Brock"));
    }

    @RepeatedTest(10)
    void containsAllTest() {
        List<String> containsList = List.of("Ben", "Katy", "Evan", "Jessica", "Bob", "Ashley");
        assertTrue(skipList.containsAll(containsList));
        containsList = new ArrayList<>(containsList);
        containsList.add("Brock");
        assertFalse(skipList.containsAll(containsList));
    }

    @RepeatedTest(10)
    void clearTest() {
        skipList.clear();
        assertTrue(skipList.isEmpty());
        assertEquals(0, skipList.size());
    }

    @RepeatedTest(10)
    void toObjectArrayTest() {
        Set<String> expected = new HashSet<>(List.of("Ben", "Katy", "Evan", "Jessica", "Bob", "Ashley"));
        Set<String> actual = new HashSet<>(skipList.size());
        Object[] array = skipList.toArray();
        assertEquals(6, array.length);
        for (Object o : array) {
            actual.add((String)o);
        }
        assertEquals(expected, actual);
    }

    @RepeatedTest(10)
    void toTypeArrayTest() {
        Set<String> expected = new HashSet<>(List.of("Ben", "Katy", "Evan", "Jessica", "Bob", "Ashley"));
        Set<String> actual = new HashSet<>(skipList.size());
        String[] a = new String[0];
        String[] array = skipList.toArray(a);
        assertEquals(6, array.length);
        assertNotEquals(a, array);
        for (Object o : array) {
            actual.add((String)o);
        }
        assertEquals(expected, actual);
        a = new String[skipList.size()];
        array = skipList.toArray(a);
        assertEquals(a, array);
    }

    @RepeatedTest(10)
    void heightTest() {
        SkipList<Integer> integerSkipList = new SkipListImpl<>();
        new Random().ints(80_000).forEach(integerSkipList::add);
        assertEquals(17, integerSkipList.getHeight());
    }
}
