package implementations;

import api.Tree;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class BST<E extends Comparable<E>> implements Tree<E> {

    private TreeNode<E> root;
    private boolean flag;
    private int size;

    public BST(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    public BST() {
    }

    public boolean add(E element) {
        flag = false;
        root = addHelper(element, root);
        return flag;
    }

    private TreeNode<E> addHelper(E element, TreeNode<E> root) {
        if (root == null) {
            size++;
            flag = true;
            return new TreeNode<>(element);
        }
        else if (element.compareTo(root.data) < 0)
            root.left = addHelper(element, root.left);
        else if (element.compareTo(root.data) > 0)
            root.right = addHelper(element, root.right);
        else {
            flag = false;
            return root;
        }
        return root;
    }

    public boolean remove(E element) {
        flag = false;
        root = removeHelper(element, root);
        return flag;
    }

    private TreeNode<E> removeHelper(E element, TreeNode<E> root) {
        if (root == null) {
            flag = true;
            return null;
        }
        else if (element.compareTo(root.data) < 0)
            root.left = removeHelper(element, root.left);
        else if (element.compareTo(root.data) > 0)
            root.right = removeHelper(element, root.right);
        else {
            flag = true;
            size--;
            if (root.left == null && root.right == null)
                return null;
            else if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            else {
                root.data = findMaxHelper(root.left);
                root.left = removeHelper(root.data, root.left);
            }
        }
        return root;
    }

    public E findMax() {
        return findMaxHelper(root);
    }

    private E findMaxHelper(TreeNode<E> root) {
        if (root == null)
            return null;
        else if (root.right == null)
            return root.data;
        else
            return findMaxHelper(root.right);
    }

    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(TreeNode<E> root) {
        if (root == null)
            return -1;
        return 1 + Math.max(heightHelper(root.left), heightHelper(root.right));
    }

    public boolean contains(E element) {
        return containsHelper(element, root);
    }

    private boolean containsHelper(E element, TreeNode<E> root) {
        if (root == null)
            return false;
        else if (element.compareTo(root.data) < 0)
            return containsHelper(element, root.left);
        else if (element.compareTo(root.data) > 0)
            return containsHelper(element, root.right);
        else
            return true;
    }

    public boolean isEmpty() {
        return (root == null);
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return inOrderIterator();
    }

    public void addAll(Collection<? extends E> collection) {
        collection.forEach(this::add);
    }

    public boolean containsAll(Collection<? extends E> collection) {
        return collection.stream().allMatch(this::contains);
    }

    @Override
    public Iterator<E> inOrderIterator() {
        return new InorderItr();
    }

    @Override
    public Iterator<E> preOrderIterator() {
        return new preOrderIterator();
    }

    @Override
    public Iterator<E> postOrderIterator() {
        return new postOrderIterator();
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        Iterator<E> itr = new InorderItr();
        while (itr.hasNext()) action.accept(itr.next());
    }

    @Override
    public Object[] toSortedArray() {
        InorderItr itr = new InorderItr();
        Object[] retArray = new Object[size];
        for (int i = 0; itr.hasNext(); i++)
            retArray[i] = itr.next();
        return retArray;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toSortedArray(T[] array) {
        Iterator<E> itr = new InorderItr();
        T[] retArray = (array.length >= size) ? array : (T[]) Array.newInstance(array.getClass().getComponentType(), size);
        for (int i = 0; itr.hasNext(); i++)
            retArray[i] = (T) itr.next();
        return retArray;
    }

    private class InorderItr implements Iterator<E> {

        private final Stack<TreeNode<E>> stack;

        InorderItr() {
            stack = new Stack<>();
            pushLeftNodes(root);
        }

        private void pushLeftNodes(TreeNode<E> current) {
            while(current != null) {
                stack.push(current);
                current = current.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            if (stack.isEmpty())
                throw new NoSuchElementException();
            TreeNode<E> current = stack.pop();
            pushLeftNodes(current.right);
            return current.data;
        }
    }

    private class preOrderIterator implements Iterator<E> {

        private final Stack<TreeNode<E>> stack;

        public preOrderIterator() {
            stack = new Stack<>();
            stack.push(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            if (stack.isEmpty())
                throw new NoSuchElementException();
            TreeNode<E> current = stack.pop();
            if (current.right != null)
                stack.push(current.right);
            if (current.left != null)
                stack.push(current.left);
            return current.data;
        }
    }

    private class postOrderIterator implements Iterator<E> {

        private final Stack<TreeNode<E>> stack;

        public postOrderIterator() {
            stack = new Stack<>();
            pushLeftRightNodes(root);
        }

        private void pushLeftRightNodes(TreeNode<E> current) {
            while(current != null) {
                if (current.right != null)
                    stack.push(current.right);
                stack.push(current);
                current = current.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            if (stack.isEmpty())
                throw new NoSuchElementException();
            TreeNode<E> rightChild;
            TreeNode<E> current = stack.pop();
            if (!stack.isEmpty() && stack.peek() == current.right) {
                rightChild = stack.pop();
                stack.push(current);
                pushLeftRightNodes(rightChild);
                return next();
            }
            return current.data;
        }
    }
    private static class TreeNode<E> {
        E data;
        TreeNode<E> left;
        TreeNode<E> right;

        public TreeNode(E data) {
            this.data = data;
        }

        public TreeNode(E data, TreeNode<E> left, TreeNode<E> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }


    }
}
