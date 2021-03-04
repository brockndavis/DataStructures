package api;

public interface UndirectedUnweightedGraph<V> extends Graph<V>{

    boolean addEdge(V vertexA, V vertexB);
}
