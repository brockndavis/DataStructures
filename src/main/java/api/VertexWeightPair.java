package api;

import implementations.WeightedDiagraph;

public class VertexWeightPair<V> implements Comparable<VertexWeightPair<V>> {

    public static final int oo = (int) 1e9;
    private V vertex;
    private Integer weight;

    public VertexWeightPair() {
    }

    public VertexWeightPair(V vertex) {
        this.vertex = vertex;
        this.weight = oo;
    }

    public VertexWeightPair(V vertex, Integer weight) {
        this.vertex = vertex;
        this.weight = weight;
    }

    @Override
    public int compareTo(VertexWeightPair<V> o) {
        return this.weight - o.weight;
    }

    public V getVertex() {
        return vertex;
    }

    public void setVertex(V vertex) {
        this.vertex = vertex;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
