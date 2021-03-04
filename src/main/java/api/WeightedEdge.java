package api;

import java.util.Objects;

public class WeightedEdge<V> {

    private V endpoint;

    private int weight;

    public WeightedEdge(V endpoint, int weight) {
        this.endpoint = endpoint;
        this.weight = weight;
    }

    public V getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(V endpoint) {
        this.endpoint = endpoint;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeightedEdge)) return false;
        WeightedEdge<?> that = (WeightedEdge<?>) o;
        return Objects.equals(endpoint, that.endpoint);
    }

    @Override
    public int hashCode() {
        return endpoint.hashCode();
    }
}
