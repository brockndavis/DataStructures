package implementations;

import api.Tree;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class AVLTree<E extends Comparable<? super E>>  implements Tree<E> {

    private TreeNode<E> root;
    private int size;
    private boolean flag;


    @Override
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
        else
            return root;
        root.setHeight();
        root = balance(root);
        return root;
    }

    private TreeNode<E> balance(TreeNode<E> root) {
        Objects.requireNonNull(root);
        int bf = root.getBF();

        if (bf == 2) {
            if (root.left.getBF() < 0)
                root.left = singleLeft(root.left);
            root = singleRight(root);
        }
        else if (bf == -2) {
            if (root.right.getBF() > 0)
                root.right = singleRight(root.right);
            root = singleLeft(root);
        }

        return root;
    }

    private TreeNode<E> singleLeft(TreeNode<E> root) {
        Objects.requireNonNull(root);
        TreeNode<E> temp = root.right;
        root.right = temp.left;
        temp.left = root;
        root.setHeight();
        temp.setHeight();
        return temp;
    }

    private TreeNode<E> singleRight(TreeNode<E> root) {
        Objects.requireNonNull(root);
        TreeNode<E> temp = root.left;
        root.left = temp.right;
        temp.right = root;
        root.setHeight();
        temp.setHeight();
        return temp;
    }

    private E findMaxHelper(TreeNode<E> root) {
        if (root == null)
            return null;
        else if (root.right == null)
            return root.data;
        else
            return findMaxHelper(root.right);
    }

    @Override
    public boolean remove(E element) {
        flag = false;
        root = removeHelper(element, root);
        if (flag) size--;
        return flag;
    }

    private TreeNode<E> removeHelper(E element, TreeNode<E> root) {
        if (root == null) {
            flag = false;
            return null;
        }
        else if (element.compareTo(root.data) < 0)
            root.left = removeHelper(element, root.left);
        else if (element.compareTo(root.data) > 0)
            root.right = removeHelper(element, root.right);
        else {
            flag = true;
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
        root.setHeight();
        root = balance(root);
        return root;
    }

    @Override
    public int height() {
        return (root == null) ? -1 : root.getHeight(root);
    }

    @Override
    public Iterator<E> iterator() {
        return inOrderIterator();
    }

    @Override
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

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addAll(Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        collection.forEach(this::add);
    }

    @Override
    public boolean containsAll(Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        return collection.stream().allMatch(this::contains);
    }

    @Override
    public Iterator<E> inOrderIterator() {
        return new InOrderIterator();
    }

    @Override
    public Iterator<E> preOrderIterator() {
        return new PreOrderIterator();
    }

    @Override
    public Iterator<E> postOrderIterator() {
        return new PostOrderIterator();
    }

    @Override
    public Object[] toSortedArray() {
        Iterator<E> itr = inOrderIterator();
        Object[] retArray = new Object[size];
        for (int i = 0; itr.hasNext(); i++)
            retArray[i] = itr.next();
        return retArray;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toSortedArray(T[] array) {
        T[] retArray = (array.length >= size) ? array : (T[]) Array.newInstance(array.getClass().getComponentType(), size);
        Iterator<E> itr = inOrderIterator();
        for (int i = 0; itr.hasNext(); i++)
            retArray[i] = (T) itr.next();
        return retArray;
    }

    @Override
    public E findMax() {
        return findMaxHelper(root);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Iterator<E> itr = inOrderIterator();
        while (itr.hasNext()) action.accept(itr.next());
    }

    private static class TreeNode<E> {
        E data;
        int height;
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

        public int getHeight(TreeNode<E> root) {
            return (root == null) ? -1 : root.height;
        }

        public void setHeight() {
            this.height = 1 + Math.max(getHeight(left), getHeight(right));
        }

        public int getBF() {
            return getHeight(left) - getHeight(right);
        }
    }

    private class InOrderIterator implements Iterator<E> {

        private final Stack<TreeNode<E>> stack;

        public InOrderIterator() {
            stack = new Stack<>();
            pushLeftNodes(root);
        }

        private void pushLeftNodes(TreeNode<E> current) {
            while (current != null) {
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

    private class PreOrderIterator implements Iterator<E> {

        private final Stack<TreeNode<E>> stack;

        public PreOrderIterator() {
            stack = new Stack<>();
            if (root != null)
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

    private class PostOrderIterator implements Iterator<E> {

        private final Stack<TreeNode<E>> stack;

        public PostOrderIterator() {
            stack = new Stack<>();
            pushLeftRightNodes(root);
        }

        private void pushLeftRightNodes(TreeNode<E> current) {
            while (current != null) {
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
            TreeNode<E> current = stack.pop();
            if (!stack.isEmpty() && stack.peek() == current.right) {
                TreeNode<E> rightChild = stack.pop();
                stack.push(current);
                pushLeftRightNodes(rightChild);
                return next();
            }
            return current.data;
        }
    }
}
