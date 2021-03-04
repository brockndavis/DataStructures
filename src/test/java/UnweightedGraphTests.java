import api.Graph;
import api.UndirectedUnweightedGraph;
import implementations.BasicGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.hamcrest.core.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UnweightedGraphTests {

    private static UndirectedUnweightedGraph<String> friendships;
    private static UndirectedUnweightedGraph<String> intersections;

    @BeforeAll
    public static void initAll() {
        friendships = new BasicGraph<>();
        friendships.addVertex("Brock");
        friendships.addVertex("Savannah");
        friendships.addVertex("Kaley");
        friendships.addVertex("Caleb");
        friendships.addVertex("Isabelle");
        friendships.addVertex("Jacob");
        friendships.addVertex("Danielle");
        friendships.addVertex("Jose");
        friendships.addEdge("Brock", "Jacob");
        friendships.addEdge("Brock", "Savannah");
        friendships.addEdge("Brock", "Kaley");
        friendships.addEdge("Brock", "Caleb");
        friendships.addEdge("Brock", "Isabelle");
        friendships.addEdge("Caleb", "Jacob");
        friendships.addEdge("Isabelle", "Danielle");
        friendships.addEdge("Jacob", "Danielle");
        friendships.addEdge("Brock", "Jose");
        friendships.addEdge("Savannah", "Kaley");

        intersections = new BasicGraph<>();
        intersections.addVertex("N/W");
        intersections.addVertex("S/W");
        intersections.addVertex("S/E");
        intersections.addVertex("N/E");
        intersections.addEdge("N/W", "N/E");
        intersections.addEdge("N/W", "S/W");
        intersections.addEdge("S/W", "S/E");
        intersections.addEdge("S/E", "N/E");
        // main street
        intersections.addEdge("N/W", "S/E");

    }

    @Test
    public void vertexCountTest() {
        assertEquals(8, friendships.vertexCount());
        assertEquals(4, intersections.vertexCount());
    }

    @Test
    void edgeCountTest() {
        assertEquals(10, friendships.edgeCount());
        assertEquals(5, intersections.edgeCount());
    }

    @Test
    public void completenessTest() {
        assertFalse(friendships.isComplete());
        assertFalse(intersections.isComplete());
        intersections.addEdge("N/E", "S/W");
        assertTrue(intersections.isComplete());
        intersections.removeEdge("N/E", "S/W");
    }

    @Test
    void nonEmptyTest() {
        assertFalse(friendships.isEmpty());
        assertFalse(intersections.isEmpty());
    }

    @Test
    void nonBipartiteTest() {
        assertFalse(friendships.isBipartite());
        assertFalse(intersections.isBipartite());
        intersections.removeEdge("N/W", "S/E");
        assertTrue(intersections.isBipartite());
        intersections.addEdge("N/W", "S/E");
    }

    @Test
    void containsCycleTest() {
        assertTrue(friendships.containsCycle());
        assertTrue(intersections.containsCycle());
    }

    @Test
    void adjacencyTests() {
        List<String> actual = friendships.getAdjacencies("Brock");
        List<String> expected = List.of("Jacob", "Savannah", "Kaley", "Caleb", "Isabelle", "Jose");
        assertEquals(actual.size(), expected.size());
        assertTrue(actual.containsAll(expected));
    }

    @Test
    void inDegreeTest() {
        assertEquals(6, friendships.inDegree("Brock"));
        assertEquals(3, friendships.inDegree("Jacob"));
        assertEquals(2, friendships.inDegree("Savannah"));
        assertEquals(2, friendships.inDegree("Caleb"));
        assertEquals(2, friendships.inDegree("Danielle"));
        assertEquals(3, intersections.inDegree("N/W"));
        assertEquals(2,intersections.inDegree("S/W"));
    }


    @Test
    void outDegreeTest() {
        assertEquals(6, friendships.outDegree("Brock"));
        assertEquals(3, intersections.outDegree("N/W"));
        assertEquals(2,intersections.outDegree("S/W"));
    }

    @Test
    void connectedTest() {
        assertTrue(friendships.isConnected());
        assertTrue(intersections.isConnected());
    }

    @Test
    void componentTest() {
        assertEquals(1, friendships.connectedComponentCount());
        assertEquals(1, intersections.connectedComponentCount());
    }

    @Test
    void edgeTest() {
        assertTrue(friendships.containsEdge("Brock", "Jacob"));
        assertTrue(friendships.containsEdge("Brock", "Caleb"));
        assertTrue(friendships.containsEdge("Brock", "Savannah"));
        assertTrue(friendships.containsEdge("Brock", "Kaley"));
        assertTrue(friendships.containsEdge("Brock", "Isabelle"));
        assertTrue(friendships.containsEdge("Brock", "Jose"));
        assertFalse(friendships.containsEdge("Brock", "Danielle"));
        assertTrue(friendships.containsEdge("Danielle", "Isabelle"));
        assertFalse(friendships.containsEdge("Sebastiam", "Caleb"));
        assertTrue(friendships.containsEdge("Savannah", "Kaley"));
    }

    @Test
    void vertexTest() {
        assertFalse(friendships.containsVertex("brock"));
        assertTrue(friendships.containsVertex("Brock"));
        assertTrue(friendships.containsVertex("Jacob"));
        assertTrue(friendships.containsVertex("Jose"));
        assertTrue(friendships.containsVertex("Caleb"));
        assertTrue(friendships.containsVertex("Kaley"));
        assertTrue(friendships.containsVertex("Savannah"));
        assertTrue(friendships.containsVertex("Danielle"));
        assertTrue(friendships.containsVertex("Isabelle"));
    }

    @Test
    void bfsTest() {
        Iterator<String> bfs = friendships.breadthFirstIterator("Brock");
        assertTrue(bfs.hasNext());
        assertEquals(bfs.next(), "Brock");
        List<String> adjacencies = new ArrayList<>(6);
        for (int i = 0; i < 6; i++)
            adjacencies.add(bfs.next());
        List<String> expected = List.of("Jacob", "Savannah", "Kaley", "Caleb", "Isabelle", "Jose");
        assertTrue(adjacencies.containsAll(expected));
        assertEquals(bfs.next(), "Danielle");
        bfs = intersections.breadthFirstIterator("N/W");
        assertTrue(bfs.hasNext());
        assertEquals(bfs.next(), "N/W");
        assertEquals(bfs.next(), "N/E");
        assertEquals(bfs.next(), "S/W");
        assertEquals(bfs.next(), "S/E");
        assertFalse(bfs.hasNext());
    }

    @Test
    void dfsTest() {
        Iterator<String> dfs = friendships.depthFirstIterator("Brock");
        assertTrue(dfs.hasNext());
        assertEquals(dfs.next(), "Brock");
        assertEquals(dfs.next(), "Jose");
        assertEquals(dfs.next(), "Isabelle");
        assertEquals(dfs.next(), "Danielle");
        assertEquals(dfs.next(), "Jacob");
        assertEquals(dfs.next(), "Caleb");
        assertEquals(dfs.next(), "Kaley");
        assertEquals(dfs.next(), "Savannah");
        assertFalse(dfs.hasNext());
        dfs = intersections.depthFirstIterator("N/W");
        assertTrue(dfs.hasNext());
        assertEquals(dfs.next(), "N/W");
        assertEquals(dfs.next(), "S/E");
        assertEquals(dfs.next(), "N/E");
        assertEquals(dfs.next(), "S/W");

    }

    @Test
    void bipartiteTest() {
        assertFalse(friendships.isBipartite());
        assertFalse(intersections.isBipartite());
        intersections.removeEdge("S/E", "N/W");
        assertTrue(intersections.isBipartite());
        intersections.addEdge("S/E", "N/W");
    }

    @Test
    void mstTest() {
        Graph<String> mst = friendships.toMinimumSpanningTree("Brock");
        assertEquals(7, mst.edgeCount());
        assertEquals(8, mst.vertexCount());
        assertTrue(mst.isConnected());
        assertFalse(mst.isComplete());
    }

    @Test
    void pathTest() {
        assertTrue(friendships.pathExists("Brock", "Danielle"));
        assertTrue(intersections.pathExists("N/W", "S/E"));
    }

    @Test
    void distanceTest() {
        assertEquals(0, friendships.getMinimumDistance("Brock", "Savannah"));
        assertEquals(0, intersections.getMinimumDistance("S/E", "N/E"));
    }
}
