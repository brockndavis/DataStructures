package implementations;

import api.TopologicalPath;

import java.util.Iterator;
import java.util.List;

public class TopologicalPathImpl<V> implements TopologicalPath<V> {

    private List<V> list;

    public TopologicalPathImpl(List<V> list) {
        this.list = list;
    }

    @Override
    public Iterator<V> iterator() {
        return list.iterator();
    }
}
