package api;

public interface DirectedWeightedGraph<V> extends WeightedGraph<V>{

    TopologicalPath<V> topologicalSort();
}
