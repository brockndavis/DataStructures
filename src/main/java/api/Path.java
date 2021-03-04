package api;

public interface Path<V> extends Iterable<V> {

    V getStartVertex();

    V getEndVertex();

    int edgeCount();

    long pathWeight();
}
