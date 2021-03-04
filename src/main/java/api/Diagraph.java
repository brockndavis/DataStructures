package api;

public interface Diagraph<V> extends Graph<V> {

    TopologicalPath<V> topologicalSort();

    boolean addEdge(V startVertex, V endVertex);


}
