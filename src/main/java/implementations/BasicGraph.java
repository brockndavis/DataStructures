package implementations;

import api.Path;
import api.UndirectedUnweightedGraph;

import java.util.*;
import java.util.function.Consumer;

public class BasicGraph<V> implements UndirectedUnweightedGraph<V> {

    private Map<V, Set<V>> adjacencyMap;
    private int vertexCount;
    private int edgeCount;

    public BasicGraph() {
        adjacencyMap = new LinkedHashMap<>();
    }

    @Override
    public boolean addVertex(V vertex) {
        Objects.requireNonNull(vertex);
        if (adjacencyMap.containsKey(vertex)) return false;

        adjacencyMap.put(vertex, new LinkedHashSet<>());
        vertexCount++;
        return true;
    }

    @Override
    public boolean addEdge(V vertexA, V vertexB) {
        if (!adjacencyMap.containsKey(vertexA) || !adjacencyMap.containsKey(vertexB)) return false;
        adjacencyMap.get(vertexA).add(vertexB);
        adjacencyMap.get(vertexB).add(vertexA);
        edgeCount++;
        return true;
    }

    @Override
    public void clear() {
        adjacencyMap.clear();
        vertexCount = 0;
        edgeCount = 0;
    }

    @Override
    public UndirectedUnweightedGraph<V> toMinimumSpanningTree() {
        if (vertexCount == 0)
            return new BasicGraph<>();

        return toMinimumSpanningTree(adjacencyMap.keySet().stream().findFirst().get());
    }

    @Override
    public UndirectedUnweightedGraph<V> toMinimumSpanningTree(V rootVertex) {
        if (!isConnected())
            return null;
        UndirectedUnweightedGraph<V> returnTree = new BasicGraph<>();
        Queue<V> queue = new ArrayDeque<>();
        Set<V> visitedSet = new HashSet<>();
        queue.offer(rootVertex);
        visitedSet.add(rootVertex);
        returnTree.addVertex(rootVertex);
        while (!queue.isEmpty()) {
            V parent = queue.poll();
            for (V vertex : adjacencyMap.get(parent)) {
                if (!visitedSet.contains(vertex)) {
                    queue.add(vertex);
                    visitedSet.add(vertex);
                    returnTree.addVertex(vertex);
                    returnTree.addEdge(parent, vertex);
                }
            }
        }
        return returnTree;
    }

    @Override
    public int inDegree(V vertex) {
        return outDegree(vertex);
    }

    @Override
    public int outDegree(V vertex) {
        if (!adjacencyMap.containsKey(vertex))
            throw new NoSuchElementException("The specified vertex is not contained in the graph");
        return adjacencyMap.get(vertex).size();
    }

    @Override
    public Iterator<V> breadthFirstIterator() {
        return new BreadthFirstIterator();
    }

    @Override
    public Iterator<V> depthFirstIterator() {
        return new DepthFirstIterator();
    }

    @Override
    public Iterator<V> breadthFirstIterator(V startVertex) {
        return new BreadthFirstIterator(startVertex);
    }

    @Override
    public Iterator<V> depthFirstIterator(V startVertex) {
        return new DepthFirstIterator(startVertex);
    }

    @Override
    public Iterator<V> iterator() {
        return breadthFirstIterator();
    }

    @Override
    public boolean containsVertex(V vertex) {
        return adjacencyMap.containsKey(vertex);
    }

    @Override
    public boolean containsEdge(V startVertex, V endVertex) {
        if (!adjacencyMap.containsKey(startVertex)) return false;

        return adjacencyMap.get(startVertex).contains(endVertex);
    }

    @Override
    public List<V> getAdjacencies(V vertex) {

        if (!adjacencyMap.containsKey(vertex))
            throw new NoSuchElementException("The specified vertex is not contained within the graph");
        return new ArrayList<>(adjacencyMap.get(vertex));
    }

    @Override
    public boolean isEmpty() {
        return vertexCount == 0;
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (!adjacencyMap.containsKey(vertex)) return true;
        adjacencyMap.remove(vertex);
        for (V graphVertex : adjacencyMap.keySet()) {
            boolean edgeFound = adjacencyMap.get(graphVertex).remove(vertex);
            if (edgeFound) edgeCount--;
        }
        vertexCount--;
        return true;
    }

    @Override
    public boolean removeEdge(V startVertex, V endVertex) {

        if (adjacencyMap.containsKey(startVertex) && adjacencyMap.containsKey(endVertex)) {
            boolean edgeFound = adjacencyMap.get(startVertex).remove(endVertex);
            if (edgeFound) {
                adjacencyMap.get(endVertex).remove(startVertex);
                edgeCount--;
                return true;
            }
        }
        return false;
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
    public boolean isComplete() {
        return edgeCount == ((vertexCount * (vertexCount - 1)) / 2);
    }

    @Override
    public boolean isConnected() {
        Iterator<V> bfs = breadthFirstIterator();
        int vertexCount = 0;
        while (bfs.hasNext()) {
            bfs.next();
            vertexCount++;
        }
        return vertexCount == this.vertexCount;
    }

    @Override
    public boolean isBipartite() {
        if (vertexCount <= 2) return true;
        Set<V> redSet = new HashSet<>();
        Set<V> blueSet = new HashSet<>();
        Queue<V> queue = new ArrayDeque<>();
        Set<V> visitedSet = new HashSet<>();
        V startVertex = adjacencyMap.keySet().stream().findFirst().get();
        queue.add(startVertex);
        redSet.add(startVertex);
        while (!queue.isEmpty()) {
            V current = queue.poll();
            visitedSet.add(current);
            boolean isNeighborRed = redSet.contains(current);
            for (V vertex : adjacencyMap.get(current)) {
                    if (!redSet.contains(vertex) && !blueSet.contains(vertex)) {
                        if (isNeighborRed)
                            blueSet.add(vertex);
                        else
                            redSet.add(vertex);
                        queue.offer(vertex);
                    }
                    else if ((isNeighborRed && redSet.contains(vertex)) || (!isNeighborRed && blueSet.contains(vertex)))
                        return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsCycle() {
        Set<V> visitedSet = new HashSet<>();
        // In case the graph isn't connected
        for (V vertex : adjacencyMap.keySet())
            if (!visitedSet.contains(vertex))
                if (containsCycleHelper(vertex, visitedSet, null))
                    return true;
        return false;
    }

    @Override
    public void forEachVertex(Consumer<? super V> action) {
        adjacencyMap.keySet().forEach(action);
    }

    private boolean containsCycleHelper(V current, Set<V> visitedSet, V parent) {
        visitedSet.add(current);

        for (V vertex : adjacencyMap.get(current)) {
            if (!visitedSet.contains(vertex))
                if (containsCycleHelper(vertex, visitedSet, current))
                    return true;
            else
                if (!vertex.equals(parent))
                    return true;
        }
        return false;
    }

    @Override
    public int connectedComponentCount() {
        Set<V> visitedSet = new HashSet<>();
        int componentCount = 0;
        for (V vertex : adjacencyMap.keySet()) {
            if (!visitedSet.contains(vertex)) {
                Iterator<V> bfs = new BreadthFirstIterator(vertex);
                while (bfs.hasNext()) {
                    visitedSet.add(bfs.next());
                }
                componentCount++;
            }
        }
        return componentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicGraph)) return false;
        BasicGraph<?> that = (BasicGraph<?>) o;
        return vertexCount == that.vertexCount &&
                edgeCount == that.edgeCount &&
                adjacencyMap.equals(that.adjacencyMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexCount, edgeCount);
    }

    @Override
    public Long getMinimumDistance(V startVertex, V endVertex) {
        if (!pathExists(startVertex, endVertex))
            throw new NoSuchElementException("A path doesn't exist between the vertices in the graph");
        return 0L;
    }

    @Override
    public Path<V> shortestPath(V startVertex, V endVertex) {

        if (!containsVertex(startVertex) || !containsVertex(endVertex))
            throw new IllegalArgumentException("One or both of the specified vertices are not contained in the graph");

        Iterator<V> bfs = breadthFirstIterator(startVertex);
        PathImpl<V> path = new PathImpl<>(0);
        while (bfs.hasNext()) {
            V current = bfs.next();
            path.addVertexTail(current);
            if (current.equals(endVertex))
                return path;
        }
        // A path doesn't exist between the two vertices
        return null;
    }


    @Override
    public boolean pathExists(V startVertex, V endVertex) {
        if (!containsVertex(startVertex) || !containsVertex(endVertex))
            return false;
        Iterator<V> bfs = breadthFirstIterator(startVertex);
        while (bfs.hasNext())
            if (bfs.next().equals(endVertex))
                return true;
        return false;
    }

    private class BreadthFirstIterator implements Iterator<V> {

        private final Queue<V> q;
        private final Set<V> visitedSet;

        public BreadthFirstIterator() {
             this(null);
        }

        @SuppressWarnings("unchecked")
        public BreadthFirstIterator(V startVertex) {
            if (startVertex == null && vertexCount > 0)
                startVertex = (V) adjacencyMap.keySet().toArray()[0];
            q = new LinkedList<>();
            visitedSet = new HashSet<>();
            if (vertexCount > 0) {
                q.offer(startVertex);
                visitedSet.add(startVertex);
            }
        }

        @Override
        public boolean hasNext() {
            return !q.isEmpty();
        }

        @Override
        public V next() {
            if (!hasNext())
                throw new NoSuchElementException();
            V vertex = q.poll();
            for (V graphVertex : adjacencyMap.get(vertex)) {
                if (!visitedSet.contains(graphVertex)) {
                    q.offer(graphVertex);
                    visitedSet.add(graphVertex);
                }
            }
            return vertex;
        }
    }

    private class DepthFirstIterator implements Iterator<V> {

        private final Stack<V> stack;
        private final Set<V> visitedSet;


        public DepthFirstIterator() {
            this(null);
        }

        public DepthFirstIterator(V startVertex) {
            if (startVertex == null && vertexCount > 0)
                startVertex = adjacencyMap.keySet().stream().findFirst().get();
            stack = new Stack<>();
            visitedSet = new HashSet<>();
            if (vertexCount > 0) {
                stack.push(startVertex);
            }
        }

        @Override
        public boolean hasNext() {
            while (!stack.isEmpty() && visitedSet.contains(stack.peek())) stack.pop();
            return !stack.isEmpty();
        }

        @Override
        public V next() {
            if (stack.isEmpty())
                throw new NoSuchElementException();
            V vertex = stack.pop();
            while(visitedSet.contains(vertex)) vertex = stack.pop();
            for (V graphVertex : adjacencyMap.get(vertex)) {
                if (!visitedSet.contains(graphVertex)) {
                    stack.push(graphVertex);
                }
            }
            visitedSet.add(vertex);
            return vertex;
        }
    }
}
