package implementations;

import api.SkipList;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.*;

public class SkipListImpl<E> implements SkipList<E> {

    private final ListNode<E> head;
    private int size;
    private final SecureRandom random;
    private Comparator<E> comparator;
    private int currentMaxHeight;

    public SkipListImpl(Comparator<E> comparator) {
        this();
        this.comparator = comparator;
    }

    public SkipListImpl() {
        currentMaxHeight = 1;
        random = new SecureRandom();
        head = new ListNode<>(null, currentMaxHeight, null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int getHeight() {
        return currentMaxHeight;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        Objects.requireNonNull(o);
        ListNode<E> current = head;
        while (current != null) {
            for (int i = current.getHeight() - 1; i >= 0; i--) {
                int cmpValue = (current.getReference(i) != null) ? compare(o, current.getReference(i).getData()) : -1;
                if (cmpValue == 0)
                    return true;
                if (cmpValue > 0) {
                    current = current.getReference(i);
                    break;
                }
                if (i == 0)
                    return false;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new SkipListIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Iterator<E> itr = iterator();
        for (int i = 0; i < size; i++) {
            array[i] = itr.next();
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        T[] returnArray = (a.length >= size) ? a : (T[])Array.newInstance(a.getClass().componentType(), size);
        Iterator<E> itr = iterator();
        for (int i = 0; i < size; i++) {
            returnArray[i] = (T) itr.next();
        }
        return returnArray;
    }

    @Override
    public boolean add(E e) {
        Objects.requireNonNull(e);
        List<NodeIndexPair<E>> referenceUpdates = new LinkedList<>();
        ListNode<E> current = head;
        boolean insertionPointFound = false;
        while (!insertionPointFound) {
            for (int i = current.getHeight() - 1; i >= 0; i--) {
                int cmpValue = (current.getReference(i) == null) ? -1 : compare(e, current.getReference(i).getData());
                if (cmpValue > 0) {
                    current = current.getReference(i);
                    break;
                }
                else if (cmpValue == 0) {
                    return false;
                }
                else {
                    referenceUpdates.add(new NodeIndexPair<>(current, i));
                    if (i == 0) {
                        insertionPointFound = true;
                        break;
                    }
                }
            }
        }
        size++;
        int newNodeHeight = genNodeHeight();
        ListNode<E> newNode = new ListNode<>(e, newNodeHeight);
        for (NodeIndexPair<E> pair : referenceUpdates) {
            if (pair.index < newNodeHeight)
                newNode.setReference(pair.index, pair.node.setReference(pair.index, newNode));
        }
        boolean heightWasUpdated = setMaxHeight();
        if (heightWasUpdated) {
            updateNodeHeights();
        }
        return true;
    }

    private static class NodeIndexPair<E> {
        ListNode<E> node;
        int index;

        public NodeIndexPair(ListNode<E> node, int index) {
            this.node = node;
            this.index = index;
        }
    }

    private void updateNodeHeights() {
        head.addReference(null);
        ListNode<E> current = head;
        ListNode<E> next = head.getReference(currentMaxHeight - 2);
        while (next != null) {
            if ((random.nextInt(2) == 1)) {
                next.addReference(current.setReference(currentMaxHeight - 1, next));
                current = next;
            }

            next = next.getReference(currentMaxHeight - 2);
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(Object o1, Object o2){
       return (comparator != null) ? comparator.compare((E)o1, (E)o2) : ((Comparable<E>)o1).compareTo((E)o2);
    }

    @Override
    public boolean containsAll(Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        return collection.stream().allMatch(this::contains);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        return collection.stream().allMatch(this::add);
    }

    @Override
    public boolean remove(E e) {
        Objects.requireNonNull(e);
        ListNode<E> current = head;
        List<NodeIndexPair<E>> referenceUpdates = new ArrayList<>();
        boolean nodeFound = false;
        while (!nodeFound) {
            for (int i = current.getHeight() - 1; i >= 0; i--) {
                int cmpValue = (current.getReference(i) == null) ? -1 : compare(e, current.getReference(i).getData());
                if (cmpValue > 0) {
                    current = current.getReference(i);
                    break;
                }
                if (cmpValue == 0) {
                    referenceUpdates.add(new NodeIndexPair<>(current, i));
                    if (i == 0) {
                        nodeFound = true;
                        break;
                    }
                }
                if (i == 0)
                    return false;
            }
        }
        ListNode<E> deletedNode = referenceUpdates.get(referenceUpdates.size() - 1).node;
        for (NodeIndexPair<E> pair : referenceUpdates) {
            if (pair.index < deletedNode.getHeight())
                pair.node.setReference(pair.index, deletedNode.getReference(pair.index));
        }
        size--;
        boolean heightWasUpdated = setMaxHeight();
        if (heightWasUpdated)
            trimSkipList();
        return true;
    }

    @Override
    public boolean removeAll(Collection<? extends E> collection) {
        Objects.requireNonNull(collection);
        return collection.stream().allMatch(this::remove);
    }

    @Override
    public void clear() {
        this.size = 0;
        this.currentMaxHeight = 1;
        head.clearAllReferences();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < head.getHeight(); i++) {
            sb.append("Level ").append(i + 1).append(": ");
            ListNode<E> current = head.getReference(i);
            while (current != null) {
                sb.append(current.getData()).append(" -> ");
                current = (current.getHeight() > i) ? current.getReference(i) : null;
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private int genNodeHeight() {
        int nodeHeight = 1;
        while((nodeHeight < currentMaxHeight) && (random.nextInt(2) == 1)) nodeHeight++;
        return nodeHeight;
    }

    private boolean setMaxHeight() {
        int oldMaxHeight = currentMaxHeight;
        currentMaxHeight = Math.max(1, (int)Math.ceil( Math.log(size) / Math.log(2)));
        return (oldMaxHeight != currentMaxHeight);
    }

    private void trimSkipList() {
        ListNode<E> current = head;
        while (current != null) {
            ListNode<E> next = current.getReference(currentMaxHeight - 1);
            current.trim(currentMaxHeight);
            current = next;
        }
    }

    private static class ListNode<E> {
        private E data;
        private ArrayList<ListNode<E>> references;

        public ListNode() {
        }

        public ListNode(E data, int height) {
            this.data = data;
            this.references = new ArrayList<>(height + 1);
            for (int i = 0; i < height; i++)
                references.add(null);
        }

        public ListNode(E data, int height, ListNode<E> next) {
            this(data, height);
            references.set(0, next);
        }

        public E getData() {
            return data;
        }

        public int getHeight() {
            return references.size();
        }

        public ListNode<E> getReference(int index) {
            return references.get(index);
        }

        public ListNode<E> setReference(int index, ListNode<E> reference) {
            return references.set(index, reference);
        }

        public void addReference(ListNode<E> reference) {
            references.add(reference);
        }
        public void clearAllReferences() {
            references.clear();
        }

        public void trim(int height) {
            while (references.size() > height)
                references.remove(references.size() - 1);
        }
    }
    private class SkipListIterator implements Iterator<E> {

        private ListNode<E> current;

        public SkipListIterator() {
            current = head.getReference(0);
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            E retVal = current.getData();
            current = current.getReference(0);
            return retVal;
        }
    }
}
