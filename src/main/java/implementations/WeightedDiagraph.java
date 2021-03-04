package implementations;

import api.*;

import javax.naming.OperationNotSupportedException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static api.VertexWeightPair.oo;

public class WeightedDiagraph<V> implements DirectedWeightedGraph<V> {

    private final Map<V, Map<V, Integer>> adjacencyMap;
    private int vertexCount;
    private int edgeCount;

    public WeightedDiagraph() {
        adjacencyMap = new LinkedHashMap<>();
    }

    @Override
    public boolean addEdge(V startVertex, V endVertex, int weight) {
        if (!containsVertex(startVertex) || !containsVertex(endVertex))
            throw new NoSuchElementException("One of the specified vertices doesn't exist in the graph");

        if (adjacencyMap.get(startVertex).containsKey(endVertex))
            return false;

        adjacencyMap.get(startVertex).put(endVertex, weight);
        edgeCount++;
        return true;
    }

    @Override
    public int getEdgeWeight(V startVertex, V endVertex) {
        if (!containsEdge(startVertex, endVertex))
            throw new NoSuchElementException("The specified edge doesn't exist in the graph");

        return adjacencyMap.get(startVertex).get(endVertex);
    }

    @Override
    public void setEdgeWeight(V startVertex, V endVertex, int newWeight) {
        if (!containsEdge(startVertex, endVertex))
            throw new NoSuchElementException("The specified edge doesn't exist in the graph");

        adjacencyMap.get(startVertex).put(endVertex, newWeight);
    }

    @Override
    public void setEdgeWeight(V startVertex, V endVertex, Function<Integer, Integer> function) {
        if (!containsEdge(startVertex, endVertex))
            throw new NoSuchElementException("The specified edge doesn't exist in the graph");

        adjacencyMap.get(startVertex).put(endVertex, function.apply(adjacencyMap.get(startVertex).get(endVertex)));
    }

    @Override
    public void clear() {
        adjacencyMap.clear();
        vertexCount = 0;
        edgeCount = 0;
    }

    @Override
    public boolean addVertex(V vertex) {
        Objects.requireNonNull(vertex);
        if (containsVertex(vertex))
            return false;
        adjacencyMap.put(vertex, new LinkedHashMap<>());
        vertexCount++;
        return true;
    }

    @Override
    public boolean containsVertex(V vertex) {
        return adjacencyMap.containsKey(vertex);
    }

    @Override
    public boolean containsEdge(V startVertex, V endVertex) {
        return containsVertex(startVertex) && adjacencyMap.get(startVertex).containsKey(endVertex);
    }

    @Override
    public List<V> getAdjacencies(V vertex) {
        if (!containsVertex(vertex))
            throw new NoSuchElementException("The specified vertex doesn't exist in the graph");

        return new ArrayList<>(adjacencyMap.get(vertex).keySet());
    }

    @Override
    public boolean isEmpty() {
        return vertexCount == 0;
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (!containsVertex(vertex))
            return false;

        edgeCount -= adjacencyMap.remove(vertex).size();
        vertexCount--;
        adjacencyMap.keySet().stream().filter((v) -> containsEdge(v, vertex)).forEach((v) -> removeEdge(v, vertex));
        return true;
    }

    @Override
    public boolean removeEdge(V startVertex, V endVertex) {
        if (!containsEdge(startVertex, endVertex))
            return false;
        adjacencyMap.get(startVertex).remove(endVertex);
        edgeCount--;
        return true;
    }

    @Override
    public int vertexCount() {
        return vertexCount;
    }

    @Override
    public int edgeCount() {
        return edgeCount;
    }

    @Override
    public Graph<V> toMinimumSpanningTree() {
        if (vertexCount == 0)
            return new WeightedDiagraph<>();

        return toMinimumSpanningTree(adjacencyMap.keySet().stream().findFirst().get());
    }

    @Override
    public Graph<V> toMinimumSpanningTree(V rootVertex) {
        if (!isConnected() || rootVertex == null)
            return null;

        WeightedDiagraph<V> returnGraph = new WeightedDiagraph<>();
        Set<V> mstSet = new HashSet<>();
        PriorityQueue<NeighborWeightPair<V>> minHeap = new PriorityQueue<>();
        minHeap.offer(new NeighborWeightPair<>(null, rootVertex, 0));
        while (mstSet.size() != vertexCount) {
            NeighborWeightPair<V> vertexPair = minHeap.poll();
            while (mstSet.contains(vertexPair.getVertex())) vertexPair = minHeap.poll();
            returnGraph.addVertex(vertexPair.getVertex());
            if (vertexPair.getNeighbor() != null)
                returnGraph.addEdge(vertexPair.getNeighbor(), vertexPair.getVertex(), vertexPair.getWeight());
            for (V vertex : adjacencyMap.get(vertexPair.getVertex()).keySet()) {
                int weight = adjacencyMap.get(vertexPair.getVertex()).get(vertex);
                minHeap.offer(new NeighborWeightPair<>(vertexPair.getVertex(), vertex, weight));
            }
            mstSet.add(vertexPair.getVertex());
        }
        return returnGraph;
    }

    private static class NeighborWeightPair<V> implements Comparable<NeighborWeightPair<V>> {
        V vertex;
        V neighbor;
        Integer weight;

        public NeighborWeightPair(V neighbor, V vertex, int weight) {
            this.neighbor = neighbor;
            this.vertex = vertex;
            this.weight = weight;
        }

        @Override
        public int compareTo(NeighborWeightPair<V> o) {
            if (o == null || o.weight == null) return -1;
            return this.weight - o.weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WeightedDiagraph.NeighborWeightPair)) return false;
            NeighborWeightPair<?> that = (NeighborWeightPair<?>) o;
            return vertex.equals(that.vertex) && weight.equals(that.weight);
        }

        @Override
        public int hashCode() {
            return weight.hashCode();
        }

        public V getVertex() {
            return vertex;
        }

        public V getNeighbor() {
            return neighbor;
        }

        public Integer getWeight() {
            return weight;
        }
    }

    @Override
    public int inDegree(V vertex) {
        if (!containsVertex(vertex))
            throw new NoSuchElementException("The specified vertex is not contained within the graph");
        return (int) adjacencyMap.keySet().stream().filter((v) -> containsEdge(v, vertex)).count();
    }

    @Override
    public int outDegree(V vertex) {
        if (!containsVertex(vertex))
            throw new NoSuchElementException("The specified vertex is not contained within the graph");
        return adjacencyMap.get(vertex).size();
    }

    @Override
    public Iterator<V> breadthFirstIterator() {
        return new BreadthFirstIterator();
    }

    @Override
    public Iterator<V> breadthFirstIterator(V startVertex) {
        return new BreadthFirstIterator(startVertex);
    }

    @Override
    public Iterator<V> depthFirstIterator() {
        return new DepthFirstIterator();
    }

    @Override
    public Iterator<V> depthFirstIterator(V startVertex) {
        return new DepthFirstIterator(startVertex);
    }

    @Override
    public boolean isComplete() {
        return edgeCount == (vertexCount * (vertexCount - 1));
    }

    @Override
    public boolean isBipartite() {
        if (vertexCount == 0) return false;
        if (vertexCount <= 2) return true;
        Set<V> visitedSet = new HashSet<>();
        Set<V> redSet = new HashSet<>();
        Set<V> blueSet = new HashSet<>();
        Queue<V> queue = new ArrayDeque<>();
        V startVertex = adjacencyMap.keySet().stream().findFirst().get();
        queue.offer(startVertex);
        redSet.add(startVertex);
        while (visitedSet.size() != vertexCount) {
            while (!queue.isEmpty()) {
                V current = queue.poll();
                if (!visitedSet.add(current))
                    continue;
                boolean neighborRed = redSet.contains(current);
                for (V vertex : adjacencyMap.get(current).keySet()) {
                    if (!redSet.contains(vertex) && !blueSet.contains(vertex)) {
                        if (neighborRed)
                            blueSet.add(vertex);
                        else
                            redSet.add(vertex);
                        queue.offer(vertex);
                    } else if ((neighborRed && redSet.contains(vertex)) || (!neighborRed && blueSet.contains(vertex)))
                        return false;
                }
            }
            if (visitedSet.size() != vertexCount) {
                startVertex = adjacencyMap.keySet().stream().filter(Predicate.not(visitedSet::contains)).findFirst().get();
                queue.offer(startVertex);
                redSet.add(startVertex);
            }
        }
        return true;
    }

    @Override
    public boolean containsCycle() {
        return containsCycle(false);
    }

    public boolean containsCycle(boolean excludeParent) {
        if (vertexCount == 0)
            return false;
        Set<V> visitedSet = new HashSet<>();
        Set<V> vertexStack = new HashSet<>();
        for (V vertex : adjacencyMap.keySet()) {
            if (!visitedSet.contains(vertex))
                if (containsCycleHelper(vertex, visitedSet, null, excludeParent, vertexStack))
                    return true;
        }
        return false;
    }

    private boolean containsCycleHelper(V current, Set<V> visited, V parent, boolean excludeParent, Set<V> vertexStack) {
        visited.add(current);
        if (!vertexStack.add(current))
            return true;

        boolean retVal =  adjacencyMap.get(current).keySet().stream()
                .filter((v) -> (!excludeParent || !v.equals(parent)))
                .anyMatch((v) -> containsCycleHelper(v, visited, current, excludeParent, vertexStack));

        adjacencyMap.get(current).keySet().stream()
                .filter((v) -> (!excludeParent || !v.equals(parent)))
                .forEach(vertexStack::remove);
        vertexStack.remove(current);
        return retVal;
    }

    @Override
    public int connectedComponentCount() {
        Set<V> visitedSet = new HashSet<>();
        int componentCount = 0;
        for (V vertex : adjacencyMap.keySet()) {
            if (!visitedSet.contains(vertex)) {
                Iterator<V> bfs = breadthFirstIterator(vertex);
                while (bfs.hasNext()) visitedSet.add(bfs.next());
                componentCount++;
            }
        }
        return componentCount;
    }

    @Override
    public boolean isConnected() {
        Iterator<V> bfs = breadthFirstIterator();
        int vertexCount = 0;
        while (bfs.hasNext()) {
            bfs.next();
            vertexCount++;
        }
        return vertexCount == vertexCount();
    }


    @Override
    public void forEachVertex(Consumer<? super V> action) {
        adjacencyMap.keySet().forEach(action);
    }

    @Override
    public Long getMinimumDistance(V startVertex, V endVertex) {

        Path<V> path = shortestPath(startVertex, endVertex);
        return (path == null) ? null : path.pathWeight();
    }

    @Override
    public Path<V> shortestPath(V startVertex, V endVertex) {
        if (!containsVertex(startVertex) || !containsVertex(endVertex))
            throw new NoSuchElementException("One of the specified vertices is not contained in the graph");

        Map<V, VertexPredecessorWeightPair<V>> distances = new LinkedHashMap<>();
        adjacencyMap.keySet().forEach((v) -> distances.put(v, new VertexPredecessorWeightPair<>(v, null)));
        PriorityQueue<VertexWeightPair<V>> minHeap = new PriorityQueue<>();
        Set<V> visitedSet = new HashSet<>();
        VertexPredecessorWeightPair<V> firstPair = new VertexPredecessorWeightPair<>(startVertex, 0, null);
        minHeap.add(firstPair);
        distances.put(startVertex, firstPair);
        while (!minHeap.isEmpty()) {
            VertexWeightPair<V> pair = minHeap.poll();
            boolean exit = false;
            while (visitedSet.contains(pair.getVertex())) {
                pair = minHeap.poll();
                if (pair == null || pair.getVertex().equals(endVertex)) {
                    exit = true;
                    break;
                }
            }
            if (exit) break;

            for (V vertex : adjacencyMap.get(pair.getVertex()).keySet()) {
                int discoveredWeight = distances.get(pair.getVertex()).getWeight() + getEdgeWeight(pair.getVertex(), vertex);
                if (!visitedSet.contains(vertex) && (discoveredWeight < distances.get(vertex).getWeight())) {
                    VertexPredecessorWeightPair<V> newWeight = new VertexPredecessorWeightPair<>(vertex, discoveredWeight, pair.getVertex());
                    distances.put(vertex, newWeight);
                    minHeap.add(newWeight);
                }
            }
            visitedSet.add(pair.getVertex());
        }
        if (distances.get(endVertex).getWeight() == oo)
            return null;

        PathImpl<V> path = new PathImpl<>(distances.get(endVertex).getWeight());
        V current = endVertex;
        while (current != null) {
            VertexPredecessorWeightPair<V> next = distances.get(current);
            path.addVertex(next.getVertex());
            current = next.getPredecessor();
        }
        return path;
    }

    private static class VertexPredecessorWeightPair<V> extends VertexWeightPair<V> {

        V predecessor;

        public VertexPredecessorWeightPair(V vertex, V predecessor) {
            super(vertex);
            this.predecessor = predecessor;
        }

        public VertexPredecessorWeightPair(V vertex, Integer weight, V predecessor) {
            super(vertex, weight);
            this.predecessor = predecessor;
        }

        public V getPredecessor() {
            return predecessor;
        }
    }

    @Override
    public boolean pathExists(V startVertex, V endVertex) {
        if (!containsVertex(startVertex) || !containsVertex(endVertex))
            throw new NoSuchElementException("One of the specified vertices is not contained in the graph");
        Iterator<V> bfs = breadthFirstIterator(startVertex);
        while (bfs.hasNext()) {
            if (bfs.next().equals(endVertex)) return true;
        }
        return false;
    }

    @Override
    public Iterator<V> iterator() {
        return breadthFirstIterator();
    }

    @Override
    public TopologicalPath<V> topologicalSort() {
        if (containsCycle())
            throw new CycleFoundException("A graph with a cycle can't be topologically sorted");

        Map<V, Integer> incoming = new LinkedHashMap<>();
        List<V> topologicalList = new ArrayList<>(vertexCount);
        Queue<V> queue = new ArrayDeque<>();
        for (V vertex : adjacencyMap.keySet()) {
            int inDegree = inDegree(vertex);
            incoming.put(vertex, inDegree);
            if (inDegree == 0) {
                queue.offer(vertex);
            }
        }
        while (!queue.isEmpty()) {
            V current = queue.poll();
            topologicalList.add(current);
            for (V vertex : adjacencyMap.get(current).keySet()) {
                incoming.put(vertex, incoming.get(vertex) - 1);
                if (incoming.get(vertex) == 0) {
                    queue.offer(vertex);
                }
            }
        }
        return new TopologicalPathImpl<>(topologicalList);
    }

    private class BreadthFirstIterator implements Iterator<V> {

        private final Queue<V> queue;
        private final Set<V> visitedSet;

        public BreadthFirstIterator() {
            this(null);
        }

        public BreadthFirstIterator(V startVertex) {
            queue = new ArrayDeque<>();
            visitedSet = new HashSet<>();
            if (vertexCount != 0 && startVertex == null)
                startVertex = adjacencyMap.keySet().stream().findFirst().get();
            if (startVertex != null && containsVertex(startVertex)) {
                queue.offer(startVertex);
                visitedSet.add(startVertex);
            }

        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public V next() {
            if (queue.isEmpty())
                throw new NoSuchElementException();
            V current = queue.poll();
            adjacencyMap.get(current).keySet().stream()
                    .filter(Predicate.not(visitedSet::contains))
                    .forEach((v) -> { queue.offer(v); visitedSet.add(v); });
            return current;
        }
    }

    private class DepthFirstIterator implements Iterator<V> {

        private final Stack<V> stack;
        private final Set<V> visitedSet;

        public DepthFirstIterator() {
            this(null);
        }

        public DepthFirstIterator(V startVertex) {
            stack = new Stack<>();
            visitedSet = new HashSet<>();

            if (vertexCount != 0 && startVertex == null)
                startVertex = adjacencyMap.keySet().stream().findFirst().get();
            if (startVertex != null && containsVertex(startVertex))
                stack.push(startVertex);
        }

        @Override
        public boolean hasNext() {
            while (!stack.isEmpty() && visitedSet.contains(stack.peek())) stack.pop();
            return !stack.isEmpty();
        }

        @Override
        public V next() {
            if (!hasNext())
                throw new NoSuchElementException();
            V current = stack.pop();
            adjacencyMap.get(current).keySet().stream().filter(Predicate.not(visitedSet::contains)).forEach(stack::push);
            visitedSet.add(current);
            return current;
        }
    }
}
