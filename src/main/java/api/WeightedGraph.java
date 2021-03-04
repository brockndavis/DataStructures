package api;

import java.util.function.Function;

public interface WeightedGraph<V> extends Graph<V> {

    boolean addEdge(V startVertex, V endVertex, int weight);

    int getEdgeWeight(V startVertex, V endVertex);

    void setEdgeWeight(V startVertex, V endVertex, int newWeight);

    void setEdgeWeight(V startVertex, V endVertex, Function<Integer, Integer> function);
}
