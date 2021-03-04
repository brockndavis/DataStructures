import api.Diagraph;
import api.Graph;
import api.Path;
import implementations.DirectedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DiagraphTests {

    private Diagraph<String> friendships;

    @BeforeEach
    void setUp() {
        friendships = new DirectedGraph<>();
        friendships.addVertex("Steve");
        friendships.addVertex("Jessica");
        friendships.addVertex("Justin");
        friendships.addVertex("David");
        friendships.addVertex("Kevin");
        friendships.addVertex("Kim");
        friendships.addVertex("Barrack");
        friendships.addEdge("Steve", "Jessica");
        friendships.addEdge("Jessica", "Steve");
        friendships.addEdge("Steve", "David");
        friendships.addEdge("Justin", "Jessica");
        friendships.addEdge("Justin", "David");
        friendships.addEdge("Justin", "Kim");
        friendships.addEdge("Justin", "Barrack");
        friendships.addEdge("David", "Kevin");
        friendships.addEdge("David", "Kim");
        friendships.addEdge("David", "Barrack");
        friendships.addEdge("Kim", "Barrack");
        friendships.addEdge("Kevin", "Barrack");
    }

    @Test
    void vertexCountTest() {
        assertEquals(7, friendships.vertexCount());
        friendships.addVertex("Bob");
        assertEquals(8, friendships.vertexCount());
        friendships.removeVertex("Bob");
        assertEquals(7, friendships.vertexCount());
    }

    @Test
    void edgeCountTest() {
        assertEquals(12, friendships.edgeCount());
        friendships.addEdge("Kim", "Steve");
        assertEquals(13, friendships.edgeCount());
        friendships.removeEdge("Kim", "Steve");
        assertEquals(12, friendships.edgeCount());
    }

    @Test
    void bfsTest() {
        Iterator<String> bfs = friendships.breadthFirstIterator("Justin");
        assertEquals("Justin", bfs.next());
        assertEquals("Jessica", bfs.next());
        assertEquals("David", bfs.next());
        assertEquals("Kim", bfs.next());
        assertEquals("Barrack", bfs.next());
        assertEquals("Steve", bfs.next());
        assertEquals("Kevin", bfs.next());
        assertFalse(bfs.hasNext());
    }

    @Test
    void dfsTest() {
        Iterator<String> dfs = friendships.depthFirstIterator("Justin");
        assertEquals("Justin", dfs.next());
        assertEquals("Barrack", dfs.next());
        assertEquals("Kim", dfs.next());
        assertEquals("David", dfs.next());
        assertEquals("Kevin", dfs.next());
        assertEquals("Jessica", dfs.next());
        assertEquals("Steve", dfs.next());
        assertFalse(dfs.hasNext());
    }

    @Test
    void cycleTest() {
        assertTrue(friendships.containsCycle());
        friendships.removeEdge("Steve", "Jessica");
        assertFalse(friendships.containsCycle());
        friendships.addEdge("Barrack", "Steve");
        assertTrue(friendships.containsCycle());
    }

    @Test
    void vertexTests() {
        assertTrue(friendships.containsVertex("Steve"));
        assertTrue(friendships.containsVertex("David"));
        assertTrue(friendships.containsVertex("Jessica"));
        assertTrue(friendships.containsVertex("Justin"));
        assertTrue(friendships.containsVertex("Kevin"));
        assertTrue(friendships.containsVertex("Kim"));
        assertTrue(friendships.containsVertex("Barrack"));
        assertFalse(friendships.containsVertex("Brock"));
    }

    @Test
    void edgeTests() {
        assertTrue(friendships.containsEdge("Steve", "Jessica"));
        assertTrue(friendships.containsEdge("Steve", "David"));
        assertTrue(friendships.containsEdge("Jessica", "Steve"));
        assertTrue(friendships.containsEdge("Justin", "Jessica"));
        assertTrue(friendships.containsEdge("Justin", "David"));
        assertTrue(friendships.containsEdge("Justin", "Kim"));
        assertTrue(friendships.containsEdge("Justin", "Barrack"));
        assertTrue(friendships.containsEdge("David", "Kevin"));
        assertTrue(friendships.containsEdge("Kevin", "Barrack"));
        assertTrue(friendships.containsEdge("Kim", "Barrack"));
        assertTrue(friendships.containsEdge("David", "Barrack"));
        assertTrue(friendships.containsEdge("David", "Kim"));
        assertFalse(friendships.containsEdge("Steve", "Barrack"));
    }

    @Test
    void connectedTest() {
        assertFalse(friendships.isConnected());
        friendships.addEdge("Barrack", "Steve");
        friendships.addEdge("David", "Justin");
        assertTrue(friendships.isConnected());
    }

    @Test
    void isCompleteTest() {
        assertFalse(friendships.isComplete());
    }

    @Test
    void bipartiteTest() {
        assertFalse(friendships.isBipartite());
        assertTrue(friendships.removeEdge("Kim", "Barrack"));
        assertTrue(friendships.removeEdge("Justin", "Kim"));
        assertTrue(friendships.removeEdge("Justin", "Barrack"));
        assertTrue(friendships.removeEdge("Kevin", "Barrack"));
        assertTrue(friendships.isBipartite());
    }

    @Test
    void clearTest() {
        friendships.clear();
        assertTrue(friendships.isEmpty());
        assertEquals(0, friendships.vertexCount());
        assertEquals(0, friendships.edgeCount());
    }

    @Test
    void inDegreeTests() {
        assertEquals(1, friendships.inDegree("Steve"));
        assertEquals(2, friendships.inDegree("Jessica"));
        assertEquals(0, friendships.inDegree("Justin"));
        assertEquals(2, friendships.inDegree("David"));
        assertEquals(2, friendships.inDegree("Kim"));
        assertEquals(4, friendships.inDegree("Barrack"));
        assertEquals(1, friendships.inDegree("Kevin"));
    }

    @Test
    void outDegreeTests() {
        assertEquals(2, friendships.outDegree("Steve"));
        assertEquals(1, friendships.outDegree("Jessica"));
        assertEquals(3, friendships.outDegree("David"));
        assertEquals(4, friendships.outDegree("Justin"));
        assertEquals(1, friendships.outDegree("Kevin"));
        assertEquals(1, friendships.outDegree("Kim"));
        assertEquals(0, friendships.outDegree("Barrack"));
    }

    @Test
    void pathExistenceTests() {
        friendships.addEdge("David", "Justin");
        assertFalse(friendships.pathExists("Barrack", "Steve"));
        assertFalse(friendships.pathExists("Barrack", "David"));
        assertTrue(friendships.pathExists("Jessica", "Justin"));
        friendships.addEdge("Barrack", "Steve");
        assertTrue(friendships.pathExists("Barrack", "Kevin"));
        assertTrue(friendships.pathExists("Barrack", "Justin"));
        assertTrue(friendships.pathExists("Kim", "Kevin"));
    }

    @Test
    void pathWeightTest() {
        assertEquals(0, friendships.getMinimumDistance("Steve", "Kevin"));
        assertNull(friendships.getMinimumDistance("Barrack", "Justin"));
    }

    @Test
    void pathTest() {
        friendships.addEdge("Barrack", "Steve");
        friendships.addEdge("David", "Justin");
        Path<String> path = friendships.shortestPath("Barrack", "Kevin");
        assertEquals(0, path.pathWeight());
        assertEquals(3, path.edgeCount());
        assertEquals("Barrack", path.getStartVertex());
        assertEquals("Kevin", path.getEndVertex());
        Iterator<String> itr = path.iterator();
        assertEquals("Barrack", itr.next());
        assertEquals("Steve", itr.next());
        assertEquals("David", itr.next());
        assertEquals("Kevin", itr.next());
        assertFalse(itr.hasNext());
    }

    @Test
    void mstTest() {
        assertNull(friendships.toMinimumSpanningTree("David"));
        friendships.addEdge("David", "Justin");
        Graph<String> mst = friendships.toMinimumSpanningTree("David");
        assertEquals(7, mst.vertexCount());
        assertEquals(6, mst.edgeCount());
    }

    @Test
    void adjacenciesTest() {
        Set<String> adj = Set.of("Jessica", "David");
        Set<String> actual = new HashSet<>(friendships.getAdjacencies("Steve"));
        assertEquals(adj, actual);
        adj = Set.of();
        actual = new HashSet<>(friendships.getAdjacencies("Barrack"));
        assertEquals(adj, actual);
        adj = Set.of("Jessica", "David", "Kim", "Barrack");
        actual = new HashSet<>(friendships.getAdjacencies("Justin"));
        assertEquals(adj, actual);
    }
}
