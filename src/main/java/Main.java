import api.BloomFilter;
import api.Tree;
import implementations.AVLTree;
import implementations.BST;
import implementations.BloomFilterImpl;

import java.security.SecureRandom;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        BloomFilter<Integer> filter = new BloomFilterImpl<>(2_000_000, 10e-10);
        System.out.println("Expected false psitive rate: " + filter.expectedFalsePositiveRate());
        System.out.println("Memory footprint estimation (bytes): " + filter.getMemoryFootprintEstimate());
        System.out.println("Hashy function count: " + filter.getHashFunctionCount());
        System.out.println("Size: " + filter.size());
        System.out.println("Inserting 1,000,000 elements... ");
        new Random().ints(1_000_000).forEach(filter::add);
        System.out.println("Expected false positive rate: " + filter.expectedFalsePositiveRate());
        System.out.println("Size: " + filter.size());
        System.out.println("Inserting 1 million elements....");
        new Random().ints(1_000_000).forEach(filter::add);
        System.out.println("Expected false positive rate: " + filter.expectedFalsePositiveRate());
        System.out.println("Size: " + filter.size());
        Tree<Integer> bst = new BST<>();
        Tree<Integer> myBst = new BST<>();
        Tree<Integer> avl = new AVLTree<>();
        Tree<Integer> myAvl = new AVLTree<>();

        SecureRandom rand = new SecureRandom();
        int[] ints = new int[] {5, 4, 2, 3, 50, 47, 48, 78};
        Arrays.stream(ints).forEach(myAvl::add);
        rand.ints(1_000_000).forEach(bst::add);
        rand.ints(1_000_000).forEach(avl::add);

        System.out.println("BST height: " + bst.height());
        System.out.println("AVL height: " + avl.height());
        System.out.println("BST size:  " + bst.size());
        System.out.println("AVL size: " + avl.size());
        System.out.println("Custom AVL height: " + myAvl.height());
        System.out.println("Custom AVL size: " + myAvl.size());

        Iterator<Integer> itr = myAvl.inOrderIterator();
        while (itr.hasNext()) System.out.println(itr.next());

        Integer[] array = new Integer[myAvl.size()];
        Integer[] elements = myAvl.toSortedArray(array);
        for (Integer integer : myAvl)
            System.out.println(integer);
        System.out.println(elements == array);
    }
}
