package implementations;

import api.*;

import java.util.*;
import java.util.function.Consumer;

public class DirectedGraph<V> implements Diagraph<V> {

    private DirectedWeightedGraph<V> graph;

    public DirectedGraph() {
        this.graph = new WeightedDiagraph<>();
    }

    @Override
    public TopologicalPath<V> topologicalSort() {
        return graph.topologicalSort();
    }

    @Override
    public void clear() {
        graph.clear();
    }

    @Override
    public boolean addVertex(V vertex) {
        return graph.addVertex(vertex);
    }

    @Override
    public boolean containsVertex(V vertex) {
        return graph.containsVertex(vertex);
    }

    @Override
    public boolean containsEdge(V startVertex, V endVertex) {
        return graph.containsEdge(startVertex, endVertex);
    }

    @Override
    public List<V> getAdjacencies(V vertex) {
        return graph.getAdjacencies(vertex);
    }

    @Override
    public boolean isEmpty() {
        return graph.isEmpty();
    }

    @Override
    public boolean removeVertex(V vertex) {
        return graph.removeVertex(vertex);
    }

    @Override
    public boolean removeEdge(V startVertex, V endVertex) {
        return graph.removeEdge(startVertex, endVertex);
    }

    @Override
    public int vertexCount() {
        return graph.vertexCount();
    }

    @Override
    public int edgeCount() {
        return graph.edgeCount();
    }

    @Override
    public Graph<V> toMinimumSpanningTree() {
        return graph.toMinimumSpanningTree();
    }

    @Override
    public Graph<V> toMinimumSpanningTree(V rootVertex) {
        return graph.toMinimumSpanningTree(rootVertex);
    }

    @Override
    public int inDegree(V vertex) {
        return graph.inDegree(vertex);
    }

    @Override
    public int outDegree(V vertex) {
        return graph.outDegree(vertex);
    }

    @Override
    public Iterator<V> breadthFirstIterator() {
        return graph.breadthFirstIterator();
    }

    @Override
    public Iterator<V> breadthFirstIterator(V startVertex) {
        return graph.breadthFirstIterator(startVertex);
    }

    @Override
    public Iterator<V> depthFirstIterator() {
        return graph.depthFirstIterator();
    }

    @Override
    public Iterator<V> depthFirstIterator(V startVertex) {
        return graph.depthFirstIterator(startVertex);
    }

    @Override
    public boolean isComplete() {
        return graph.isComplete();
    }

    @Override
    public boolean isBipartite() {
        return graph.isBipartite();
    }

    @Override
    public boolean containsCycle() {
        return graph.containsCycle();
    }

    @Override
    public int connectedComponentCount() {
        return graph.connectedComponentCount();
    }

    @Override
    public boolean isConnected() {
        return graph.isConnected();
    }

    @Override
    public void forEachVertex(Consumer<? super V> action) {
        graph.forEachVertex(action);
    }

    @Override
    public Long getMinimumDistance(V startVertex, V endVertex) {
        return graph.getMinimumDistance(startVertex, endVertex);
    }

    @Override
    public Path<V> shortestPath(V startVertex, V endVertex) {
        return graph.shortestPath(startVertex, endVertex);
    }

    @Override
    public boolean pathExists(V startVertex, V endVertex) {
        return graph.pathExists(startVertex, endVertex);
    }

    @Override
    public Iterator<V> iterator() {
        return graph.iterator();
    }

    @Override
    public boolean addEdge(V startVertex, V endVertex) {
        return graph.addEdge(startVertex, endVertex, 0);
    }
}
