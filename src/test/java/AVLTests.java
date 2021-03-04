import api.Tree;
import implementations.AVLTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AVLTests {

    private Tree<Integer> testTree;

    @BeforeEach
    void setUp() {
        testTree = new AVLTree<>();
        List<Integer> addList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 19, 21, 26);
        testTree.addAll(addList);
    }

    @Test
    void inOrderTest() {
        List<Integer> expected = List.of(1, 2, 3, 4, 5, 6, 7, 8, 19, 21, 26);
        Iterator<Integer> itr = testTree.inOrderIterator();
        List<Integer> actual = new ArrayList<>(testTree.size());
        while (itr.hasNext()) actual.add(itr.next());
        assertEquals(expected, actual);
    }

    @Test
    void preOrderTest() {
        List<Integer> expected = List.of(4, 2, 1, 3, 8, 6, 5, 7, 21, 19, 26);
        Iterator<Integer> itr = testTree.preOrderIterator();
        List<Integer> actual = new ArrayList<>(testTree.size());
        while (itr.hasNext()) actual.add(itr.next());
        assertEquals(expected, actual);
    }

    @Test
    void postOrderTest() {
        List<Integer> expected = List.of(1, 3, 2, 5, 7, 6, 19, 26, 21, 8, 4);
        Iterator<Integer> itr = testTree.postOrderIterator();
        List<Integer> actual = new ArrayList<>(testTree.size());
        while (itr.hasNext()) actual.add(itr.next());
        assertEquals(expected, actual);
    }

    @Test
    void sizeTest() {
        assertEquals(11, testTree.size());
        assertTrue(testTree.remove(4));
        assertEquals(10, testTree.size());
        assertTrue(testTree.add(4));
        assertEquals(11, testTree.size());
    }

    @Test
    void duplicateValueTest() {
        assertEquals(11, testTree.size());
        assertFalse(testTree.add(26));
        assertEquals(11, testTree.size());
    }

    @Test
    void clearTest() {
        testTree.clear();
        assertTrue(testTree.isEmpty());
    }

    @Test
    void findMaxTest() {
        assertEquals(26, testTree.findMax());
        assertTrue(testTree.add(27));
        assertEquals(27, testTree.findMax());
    }

    @Test
    void containsALlTest() {
        List<Integer> containsList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 19, 21, 26);
        assertTrue(testTree.containsAll(containsList));
    }

    @Test
    void heightTest() {
        assertEquals(3, testTree.height());
        assertTrue(testTree.add(0));
        assertTrue(testTree.add(-1));
        assertTrue(testTree.add(-2));
        assertTrue(testTree.add(-3));
        assertEquals(3, testTree.height());
        assertTrue(testTree.add(-4));
        assertEquals(4, testTree.height());
    }

    @Test
    void removeTest() {
        assertTrue(testTree.remove(26));
        assertTrue(testTree.remove(1));
        assertFalse(testTree.remove(0));
    }

    @Test
    void sortedArrayTest() {
        Object[] sortedArray = testTree.toSortedArray();
        assertEquals(testTree.size(), sortedArray.length);
        Integer prev = Integer.MIN_VALUE;
        for (Object i : sortedArray) {
            Integer current = (Integer) i;
            assertTrue(current.compareTo(prev) > 0);
            prev = current;
        }
    }

    @Test
    void otherSortedArrayTest() {
        Integer[] array = {};
        Integer[] sortedArray = testTree.toSortedArray(array);
        assertNotEquals(array, sortedArray);
        assertEquals(testTree.size(), sortedArray.length);
        Integer prev = Integer.MIN_VALUE;
        for (Integer i : sortedArray) {
            assertTrue(i.compareTo(prev) > 0);
            prev = i;
        }
        array = new Integer[testTree.size()];
        sortedArray = testTree.toSortedArray(array);
        assertEquals(array, sortedArray);
    }

    @Test
    void containsTest() {
        assertTrue(testTree.contains(26));
        assertTrue(testTree.contains(4));
        assertFalse(testTree.contains(-1));
    }
}
