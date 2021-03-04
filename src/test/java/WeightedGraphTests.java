import api.Graph;
import api.Path;
import api.WeightedGraph;
import implementations.DirectionlessWeightedGraph;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WeightedGraphTests {

    private WeightedGraph<String> friendGraph;
    private WeightedGraph<String> computerNetwork;

    @BeforeEach
    void initFriendGraph() {
        friendGraph = new DirectionlessWeightedGraph<>();
        friendGraph.addVertex("Steve");
        friendGraph.addVertex("Dave");
        friendGraph.addVertex("Dylan");
        friendGraph.addVertex("Bob");
        friendGraph.addVertex("Jane");
        friendGraph.addVertex("Mike");
        friendGraph.addVertex("Kate");
        friendGraph.addEdge("Steve", "Dylan", 2);
        friendGraph.addEdge("Steve", "Dave", 5);
        friendGraph.addEdge("Dave", "Bob", 4);
        friendGraph.addEdge("Dave", "Jane", 21);
        friendGraph.addEdge("Bob", "Jane", 7);
        friendGraph.addEdge("Bob", "Mike", 17);
        friendGraph.addEdge("Jane", "Kate", 5);
        friendGraph.addEdge("Mike", "Kate", 4);
        friendGraph.addEdge("Dylan", "Jane", 1);
    }

    @BeforeEach
    void initComputerNetwork() {
        computerNetwork = new DirectionlessWeightedGraph<>();
        computerNetwork.addVertex("ComputerA");
        computerNetwork.addVertex("ComputerB");
        computerNetwork.addVertex("ComputerC");
        computerNetwork.addVertex("ComputerD");
        computerNetwork.addVertex("ComputerE");
        computerNetwork.addVertex("ComputerA");
        computerNetwork.addVertex("SwitchA");
        computerNetwork.addVertex("ISP");
        computerNetwork.addVertex("ServerA");
        computerNetwork.addVertex("ServerB");
        computerNetwork.addVertex("ServerC");
        computerNetwork.addEdge("ComputerA", "ComputerC", 10);
        computerNetwork.addEdge("ComputerA", "ComputerD", 16);
        computerNetwork.addEdge("ComputerA", "SwitchA", 5);
        computerNetwork.addEdge("ComputerD", "ComputerE", 120);
        computerNetwork.addEdge("ComputerC", "ISP", 5000);
        computerNetwork.addEdge("ComputerE", "ServerC", 5);
        computerNetwork.addEdge("ISP", "ComputerB", 8);
        computerNetwork.addEdge("ISP", "SwitchA", 1);
        computerNetwork.addEdge("ISP", "ServerC", 1000);
        computerNetwork.addEdge("ISP", "ServerA", 100);
        computerNetwork.addEdge("SwitchA", "ServerB", 2);
        computerNetwork.addEdge("ComputerB", "SwitchA", 10);
    }

    @Test
    void bfsTests() {
        Iterator<String> bfs = friendGraph.breadthFirstIterator("Steve");
        assertEquals("Steve", bfs.next());
        assertEquals("Dylan", bfs.next());
        assertEquals("Dave", bfs.next());
        assertEquals(bfs.next(), "Jane");
        assertEquals("Bob", bfs.next());
        assertEquals("Kate", bfs.next());
        assertEquals("Mike", bfs.next());
        assertFalse(bfs.hasNext());
        bfs = computerNetwork.breadthFirstIterator("ComputerA");
        assertEquals("ComputerA", bfs.next());
        assertEquals("ComputerC", bfs.next());
        assertEquals("ComputerD", bfs.next());
        assertEquals("SwitchA", bfs.next());
        assertEquals("ISP", bfs.next());
        assertEquals("ComputerE", bfs.next());
        assertEquals("ServerB", bfs.next());
        assertEquals("ComputerB", bfs.next());
        assertEquals("ServerC", bfs.next());
        assertEquals("ServerA", bfs.next());
        assertFalse(bfs.hasNext());
    }

    @Test
    void dfsTests() {
        Iterator<String> dfs = friendGraph.depthFirstIterator("Steve");
        assertEquals("Steve", dfs.next());
        assertEquals("Dave", dfs.next());
        assertEquals("Jane", dfs.next());
        assertEquals(dfs.next(), "Dylan");
        assertEquals("Kate", dfs.next());
        assertEquals("Mike", dfs.next());
        assertEquals("Bob", dfs.next());
        assertFalse(dfs.hasNext());
        dfs = computerNetwork.depthFirstIterator("ComputerA");
        assertEquals("ComputerA", dfs.next());
        assertEquals("SwitchA", dfs.next());
        assertEquals("ComputerB", dfs.next());
        assertEquals("ISP", dfs.next());
        assertEquals("ServerA", dfs.next());
        assertEquals("ServerC", dfs.next());
        assertEquals("ComputerE", dfs.next());
        assertEquals("ComputerD", dfs.next());
        assertEquals("ComputerC", dfs.next());
        assertEquals("ServerB", dfs.next());
        assertFalse(dfs.hasNext());
    }

    @Test
    void friendshipsVertexTests() {
        assertTrue(friendGraph.containsVertex("Steve"));
        assertTrue(friendGraph.containsVertex("Dave"));
        assertTrue(friendGraph.containsVertex("Dylan"));
        assertTrue(friendGraph.containsVertex("Bob"));
        assertTrue(friendGraph.containsVertex("Jane"));
        assertTrue(friendGraph.containsVertex("Mike"));
        assertTrue(friendGraph.containsVertex("Kate"));
        assertFalse(friendGraph.containsVertex("Brock"));
    }

    @Test
    void computerNetworkVertexTest() {
        assertTrue(computerNetwork.containsVertex("ComputerA"));
        assertTrue(computerNetwork.containsVertex("ComputerB"));
        assertTrue(computerNetwork.containsVertex("ComputerC"));
        assertTrue(computerNetwork.containsVertex("ComputerD"));
        assertTrue(computerNetwork.containsVertex("ComputerE"));
        assertTrue(computerNetwork.containsVertex("ServerA"));
        assertTrue(computerNetwork.containsVertex("ServerB"));
        assertTrue(computerNetwork.containsVertex("ServerC"));
        assertTrue(computerNetwork.containsVertex("SwitchA"));
        assertTrue(computerNetwork.containsVertex("ISP"));
        assertFalse(computerNetwork.containsVertex("SwitchB"));
    }

    @Test
    void vertexCountTests() {
        assertEquals(7, friendGraph.vertexCount());
        assertEquals(10, computerNetwork.vertexCount());
    }

    @Test
    void friendshipsEdgeTests() {
        assertTrue(friendGraph.containsEdge("Steve", "Dylan"));
        assertTrue(friendGraph.containsEdge("Steve", "Dave"));
        assertTrue(friendGraph.containsEdge("Dylan", "Jane"));
        assertTrue(friendGraph.containsEdge("Dave", "Jane"));
        assertTrue(friendGraph.containsEdge("Dave", "Bob"));
        assertTrue(friendGraph.containsEdge("Bob", "Jane"));
        assertTrue(friendGraph.containsEdge("Kate", "Jane"));
        assertTrue(friendGraph.containsEdge("Bob", "Mike"));
        assertTrue(friendGraph.containsEdge("Mike", "Kate"));
        assertFalse(friendGraph.containsEdge("Dylan", "Kate"));
    }

    @Test
    void edgeCountTests() {
        assertEquals(9, friendGraph.edgeCount());
        friendGraph.addEdge("Kate", "Dylan", 100);
        assertEquals(10, friendGraph.edgeCount());
        friendGraph.removeEdge("Kate", "Dylan");
        assertEquals(9, friendGraph.edgeCount());
        assertEquals(12, computerNetwork.edgeCount());
        computerNetwork.addEdge("ComputerA", "ServerA", 100000);
        assertEquals(13, computerNetwork.edgeCount());
        computerNetwork.removeEdge("ComputerA", "ServerA");
        assertEquals(12, computerNetwork.edgeCount());
    }

    @Test
    void edgeWeightModificationTests() {
        assertEquals(2, friendGraph.getEdgeWeight("Steve", "Dylan"));
        friendGraph.setEdgeWeight("Steve", "Dylan", 5);
        friendGraph.setEdgeWeight("Steve", "Dylan", (i) -> i * 2);
        assertEquals(10, friendGraph.getEdgeWeight("Steve", "Dylan"));
        friendGraph.setEdgeWeight("Steve", "Dylan", 2);
        assertEquals(2, friendGraph.getEdgeWeight("Steve", "Dylan"));
        assertEquals(10, computerNetwork.getEdgeWeight("ComputerA", "ComputerC"));
        computerNetwork.setEdgeWeight("ComputerA", "ComputerC", 100);
        computerNetwork.setEdgeWeight("ComputerA", "ComputerC", (i) -> i * 100);
        assertEquals(10_000, computerNetwork.getEdgeWeight("ComputerA", "ComputerC"));
        computerNetwork.setEdgeWeight("ComputerA", "ComputerC", 10);
        assertEquals(10, computerNetwork.getEdgeWeight("ComputerA", "ComputerC"));
    }

    @Test
    void completenessTest() {
        assertFalse(friendGraph.isComplete());
        friendGraph.addEdge("Steve", "Bob", 10);
        friendGraph.addEdge("Steve", "Jane", 10);
        friendGraph.addEdge("Steve", "Mike", 10);
        friendGraph.addEdge("Steve", "Kate", 10);
        friendGraph.addEdge("Dylan", "Bob", 10);
        friendGraph.addEdge("Dylan", "Dave", 10);
        friendGraph.addEdge("Dylan", "Kate", 10);
        friendGraph.addEdge("Dylan", "Mike", 10);
        friendGraph.addEdge("Dave", "Mike", 10);
        friendGraph.addEdge("Dave", "Kate", 10);
        friendGraph.addEdge("Bob", "Kate", 10);
        friendGraph.addEdge("Mike", "Jane", 10);
        assertTrue(friendGraph.isComplete());
        friendGraph.removeEdge("Steve", "Bob");
        friendGraph.removeEdge("Steve", "Jane");
        friendGraph.removeEdge("Steve", "Mike");
        friendGraph.removeEdge("Steve", "Kate");
        friendGraph.removeEdge("Dylan", "Bob");
        friendGraph.removeEdge("Dylan", "Dave");
        friendGraph.removeEdge("Dylan", "Kate");
        friendGraph.removeEdge("Dylan", "Mike");
        friendGraph.removeEdge("Dave", "Mike");
        friendGraph.removeEdge("Dave", "Kate");
        friendGraph.removeEdge("Bob", "Kate");
        friendGraph.removeEdge("Mike", "Jane");
        assertFalse(friendGraph.isComplete());
        assertFalse(computerNetwork.isComplete());
    }

    @Test
    void bipartiteTests() {
        assertFalse(friendGraph.isBipartite());
        assertFalse(computerNetwork.isBipartite());
        computerNetwork.removeVertex("ComputerB");
        assertTrue(computerNetwork.isBipartite());
        computerNetwork.addVertex("ComputerB");
        computerNetwork.addEdge("ComputerB", "SwitchA", 10);
        computerNetwork.addEdge("ComputerB", "ISP", 8);
        assertEquals(12, computerNetwork.edgeCount());
        assertEquals(10, computerNetwork.vertexCount());
    }

    @Test
    void connectedComponentTest() {
        assertEquals(1, friendGraph.connectedComponentCount());
        friendGraph.addVertex("Kyle");
        assertEquals(2, friendGraph.connectedComponentCount());
        friendGraph.removeVertex("Kyle");
        assertEquals(1, friendGraph.connectedComponentCount());
        assertEquals(1, computerNetwork.connectedComponentCount());
        computerNetwork.addVertex("SwitchB");
        assertEquals(2, computerNetwork.connectedComponentCount());
        computerNetwork.removeVertex("SwitchB");
        assertEquals(1, computerNetwork.connectedComponentCount());
    }

    @Test
    void cycleTests() {
        assertTrue(friendGraph.containsCycle());
        assertTrue(computerNetwork.containsCycle());
        computerNetwork.removeEdge("ComputerA", "SwitchA");
        computerNetwork.removeEdge("ComputerC", "ISP");
        computerNetwork.removeEdge("ComputerB", "SwitchA");
        assertFalse(computerNetwork.containsCycle());
        computerNetwork.addEdge("ComputerA", "SwitchA", 5);
        computerNetwork.addEdge("ComputerC", "ISP", 5000);
        computerNetwork.addEdge("ComputerB", "SwitchA", 10);
        assertEquals(12, computerNetwork.edgeCount());
    }

    @Test
    void inDegreeTest() {
        assertEquals(2, friendGraph.inDegree("Steve"));
        assertEquals(3, friendGraph.inDegree("Dave"));
        assertEquals(2, friendGraph.inDegree("Dylan"));
        assertEquals(3, friendGraph.inDegree("Bob"));
        assertEquals(2, friendGraph.inDegree("Mike"));
        assertEquals(2, friendGraph.inDegree("Kate"));
        assertEquals(3, computerNetwork.inDegree("ComputerA"));
        assertEquals(2, computerNetwork.inDegree("ComputerD"));
        assertEquals(2, computerNetwork.inDegree("ComputerE"));
        assertEquals(2, computerNetwork.inDegree("ServerC"));
        assertEquals(5, computerNetwork.inDegree("ISP"));
        assertEquals(1, computerNetwork.inDegree("ServerA"));
        assertEquals(4, computerNetwork.inDegree("SwitchA"));
        assertEquals(1, computerNetwork.inDegree("ServerB"));
        assertEquals(2, computerNetwork.inDegree("ComputerB"));
        assertEquals(2, computerNetwork.inDegree("ComputerC"));
    }

    @Test
    void outDegreeTest() {
        assertEquals(2, friendGraph.outDegree("Steve"));
        assertEquals(3, friendGraph.outDegree("Dave"));
        assertEquals(2, friendGraph.outDegree("Dylan"));
        assertEquals(3, friendGraph.outDegree("Bob"));
        assertEquals(2, friendGraph.outDegree("Mike"));
        assertEquals(2, friendGraph.outDegree("Kate"));
        assertEquals(3, computerNetwork.outDegree("ComputerA"));
        assertEquals(2, computerNetwork.outDegree("ComputerD"));
        assertEquals(2, computerNetwork.outDegree("ComputerE"));
        assertEquals(2, computerNetwork.outDegree("ServerC"));
        assertEquals(5, computerNetwork.outDegree("ISP"));
        assertEquals(1, computerNetwork.outDegree("ServerA"));
        assertEquals(4, computerNetwork.outDegree("SwitchA"));
        assertEquals(1, computerNetwork.outDegree("ServerB"));
        assertEquals(2, computerNetwork.outDegree("ComputerB"));
        assertEquals(2, computerNetwork.outDegree("ComputerC"));
    }

    @Test
    void adjacencyTest() {
        Set<String> adj = Set.of("Dylan", "Dave");
        Set<String> actual = new HashSet<>(friendGraph.getAdjacencies("Steve"));
        assertEquals(adj, actual);
        adj = Set.of("ComputerC", "ComputerD", "SwitchA");
        actual = new HashSet<>(computerNetwork.getAdjacencies("ComputerA"));
        assertEquals(adj, actual);
    }

    @Test
    void pathExistenceTests() {
        assertTrue(friendGraph.pathExists("Steve", "Kate"));
        assertTrue(computerNetwork.pathExists("ComputerA", "ServerA"));
        computerNetwork.removeEdge("ISP", "ServerA");
        assertFalse(computerNetwork.pathExists("ComputerA", "ServerA"));
        computerNetwork.addEdge("ISP", "ServerA", 100);
    }

    @Test
    void distanceTests() {
        assertEquals(8, friendGraph.getMinimumDistance("Steve", "Kate"));
        assertEquals(6, friendGraph.getMinimumDistance("Dylan", "Kate"));
        assertEquals(8, friendGraph.getMinimumDistance("Dave", "Jane"));
        assertEquals(12, friendGraph.getMinimumDistance("Kate", "Bob"));
        assertEquals(106, computerNetwork.getMinimumDistance("ComputerA", "ServerA"));
        assertEquals(16, computerNetwork.getMinimumDistance("ISP", "ComputerC"));
        assertEquals(147, computerNetwork.getMinimumDistance("ISP", "ServerC"));
    }

    @Test
    void pathTests() {
        Iterator<String> path = friendGraph.shortestPath("Steve", "Kate").iterator();
        assertEquals("Steve", path.next());
        assertEquals("Dylan", path.next());
        assertEquals("Jane", path.next());
        assertEquals("Kate", path.next());
        assertFalse(path.hasNext());
        path = friendGraph.shortestPath("Dave", "Jane").iterator();
        assertEquals("Dave", path.next());
        assertEquals("Steve", path.next());
        assertEquals("Dylan", path.next());
        assertEquals("Jane", path.next());
        assertFalse(path.hasNext());
        path = friendGraph.shortestPath("Kate", "Bob").iterator();
        assertEquals("Kate", path.next());
        assertEquals("Jane", path.next());
        assertEquals("Bob", path.next());
        assertFalse(path.hasNext());
    }

    @Test
    void minimumSpanningTreeTests() {
        Graph<String> mst = friendGraph.toMinimumSpanningTree("Steve");
        assertEquals(7, mst.vertexCount());
        assertEquals(6, mst.edgeCount());
        assertTrue(mst.containsEdge("Steve", "Dylan"));
        assertTrue(mst.containsEdge("Steve", "Dave"));
        assertTrue(mst.containsEdge("Dave", "Bob"));
        assertTrue(mst.containsEdge("Dylan", "Jane"));
        assertTrue(mst.containsEdge("Jane", "Kate"));
        assertTrue(mst.containsEdge("Kate", "Mike"));
        mst = computerNetwork.toMinimumSpanningTree("ComputerA");
        assertEquals(10, mst.vertexCount());
        assertEquals(9, mst.edgeCount());
        assertTrue(mst.containsEdge("ComputerA", "ComputerC"));
        assertTrue(mst.containsEdge("ComputerA", "SwitchA"));
        assertTrue(mst.containsEdge("ComputerA", "ComputerD"));
        assertTrue(mst.containsEdge("ComputerD", "ComputerE"));
        assertTrue(mst.containsEdge("ComputerE", "ServerC"));
        assertTrue(mst.containsEdge("SwitchA", "ServerB"));
        assertTrue(mst.containsEdge("SwitchA", "ISP"));
        assertTrue(mst.containsEdge("ISP", "ComputerB"));
        assertTrue(mst.containsEdge("ISP", "ServerA"));
    }

    @Test
    void clearTest() {
        friendGraph.clear();
        assertTrue(friendGraph.isEmpty());
        assertEquals(0, friendGraph.edgeCount());
        assertEquals(0, friendGraph.edgeCount());
    }
}
