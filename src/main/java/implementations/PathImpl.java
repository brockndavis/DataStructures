package implementations;

import api.Path;
import api.VertexWeightPair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PathImpl<V> implements Path<V> {

    private final LinkedList<V> list;
    private long pathWeight;

    public PathImpl(LinkedList<V> list) {
        this.list = list;
    }

    public PathImpl() {
        this.list = new LinkedList<>();
    }

    public PathImpl(long pathWeight) {
        this.list = new LinkedList<>();
        this.pathWeight = pathWeight;
    }

    @Override
    public V getStartVertex() {
        return list.getFirst();
    }

    @Override
    public V getEndVertex() {
        return list.getLast();
    }

    @Override
    public int edgeCount() {
        return list.size() - 1;
    }

    @Override
    public long pathWeight() {
        return pathWeight;
    }

    @Override
    public Iterator<V> iterator() {
        return list.iterator();
    }

    public void addVertex(V vertex) {
        list.addFirst(vertex);
    }

    public void addVertexTail(V vertex) {
        list.addLast(vertex);
    }

    public void setPathWeight(long pathWeight) {
        this.pathWeight = pathWeight;
    }
}
