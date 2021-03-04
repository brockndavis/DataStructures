import api.Tree;
import implementations.BST;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BSTTests {

    private Tree<Integer> testTree;

    @BeforeEach
    void setUp() {
        testTree = new BST<>();
        List<Integer> addList = List.of(56, 17, 84, 15, 14, 13, 12, 11, 18, 85, 86);
        testTree.addAll(addList);
    }

    @Test
    void addTest() {
        assertTrue(testTree.add(89));
        assertTrue(testTree.contains(86));
    }

    @Test
    void removeTest() {
        assertTrue(testTree.remove(56));
        assertFalse(testTree.contains(56));
    }

    @Test
    void duplicateTest() {
        assertEquals(11, testTree.size());
        assertFalse(testTree.add(86));
        assertEquals(11, testTree.size());
    }

    @Test
    void preOrderTest() {
        List<Integer> expected = List.of(56, 17, 15, 14, 13, 12, 11, 18, 84, 85, 86);
        Iterator<Integer> itr = testTree.preOrderIterator();
        List<Integer> actual = new ArrayList<>(testTree.size());
        while (itr.hasNext()) actual.add(itr.next());
        assertEquals(expected, actual);
    }

    @Test
    void inOrderTest() {
        List<Integer> expected = List.of(11, 12, 13, 14, 15, 17, 18, 56, 84, 85, 86);
        Iterator<Integer> itr = testTree.inOrderIterator();
        List<Integer> actual = new ArrayList<>(testTree.size());
        while (itr.hasNext()) actual.add(itr.next());
        assertEquals(expected, actual);
    }

    @Test
    void postOrderTest() {
        List<Integer> expected = List.of(11, 12, 13, 14, 15, 18, 17, 86, 85, 84, 56);
        Iterator<Integer> itr = testTree.postOrderIterator();
        List<Integer> actual = new ArrayList<>(testTree.size());
        while (itr.hasNext()) actual.add(itr.next());
        assertEquals(expected, actual);
    }

    @Test
    void containsTest() {
        assertTrue(testTree.contains(86));
        assertFalse(testTree.contains(97));
        assertTrue(testTree.contains(11));
    }

    @Test
    void sizeTest() {
        assertEquals(11, testTree.size());
        assertTrue(testTree.add(95));
        assertEquals(12, testTree.size());
        assertTrue(testTree.remove(95));
        assertEquals(11, testTree.size());
    }

    @Test
    void clearTest() {
        assertFalse(testTree.isEmpty());
        testTree.clear();
        assertTrue(testTree.isEmpty());
        assertEquals(0, testTree.size());
    }

    @Test
    void heightTest() {
        assertEquals(6, testTree.height());
        assertTrue(testTree.add(10));
        assertEquals(7, testTree.height());
    }

    @Test
    void findMaxTest() {
        assertEquals(86, testTree.findMax());
        assertTrue(testTree.add(95));
        assertEquals(95, testTree.findMax());
    }

    @Test
    void sortedObjectArrayTest() {
        Object[] sortedArray = testTree.toSortedArray();
        Integer prev = Integer.MIN_VALUE;
        for (Object obj : sortedArray) {
            Integer i = (Integer) obj;
            assertTrue(i.compareTo(prev) > 0);
            prev = i;
        }
    }

    @Test
    void sortedIntegerArrayTest() {
        Integer[] array = new Integer[0];
        Integer[] sortedArray = testTree.toSortedArray(array);
        assertNotEquals(array, sortedArray);
        assertEquals(testTree.size(), sortedArray.length);
        int prev = Integer.MIN_VALUE;
        for (Object obj : sortedArray) {
            Integer i = (Integer) obj;
            assertTrue(i.compareTo(prev) > 0);
            prev = i;
        }
        array = new Integer[testTree.size()];
        sortedArray = testTree.toSortedArray(array);
        assertEquals(array, sortedArray);
        assertEquals(testTree.size(), sortedArray.length);
        prev = Integer.MIN_VALUE;
        for (Object obj : sortedArray) {
            Integer i = (Integer) obj;
            assertTrue(i.compareTo(prev) > 0);
            prev = i;
        }
    }

    @Test
    void containsAllTest() {
        List<Integer> containsList = List.of(56, 17, 84, 15, 14, 13, 12, 11, 18, 85, 86);
        assertTrue(testTree.containsAll(containsList));
    }
}
