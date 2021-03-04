package api;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public interface Graph<V> extends Iterable<V> {

    void clear();

    boolean addVertex(V vertex);

    boolean containsVertex(V vertex);

    boolean containsEdge(V startVertex, V endVertex);

    boolean equals(Object other);

    List<V> getAdjacencies(V vertex);

    boolean isEmpty();

    boolean removeVertex(V vertex);

    boolean removeEdge(V startVertex, V endVertex);

    int vertexCount();

    int edgeCount();

    Graph<V> toMinimumSpanningTree();

    Graph<V> toMinimumSpanningTree(V rootVertex);

    int inDegree(V vertex);

    int outDegree(V vertex);

    Iterator<V> breadthFirstIterator();

    Iterator<V> breadthFirstIterator(V startVertex);

    Iterator<V> depthFirstIterator();

    Iterator<V> depthFirstIterator(V startVertex);

    boolean isComplete();

    boolean isBipartite();

    boolean containsCycle();

    int connectedComponentCount();

    boolean isConnected();

    void forEachVertex(Consumer<? super V> action);

    Long getMinimumDistance(V startVertex, V endVertex);

    Path<V> shortestPath(V startVertex, V endVertex);

    boolean pathExists(V startVertex, V endVertex);
}
