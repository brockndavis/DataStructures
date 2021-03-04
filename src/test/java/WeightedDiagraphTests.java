import api.DirectedWeightedGraph;
import api.Graph;
import api.Path;
import implementations.WeightedDiagraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WeightedDiagraphTests {

    private DirectedWeightedGraph<String> airports;

    @BeforeEach
    void setUp() {
        airports = new WeightedDiagraph<>();
        airports.addVertex("MCO");
        airports.addVertex("ATL");
        airports.addVertex("JFK");
        airports.addVertex("MSP");
        airports.addVertex("SLC");
        airports.addVertex("DEN");
        airports.addVertex("SEA");
        airports.addVertex("SFO");
        airports.addVertex("LAX");
        airports.addVertex("LAS");
        airports.addVertex("HND");
        airports.addVertex("ICN");
        airports.addVertex("PVG");
        airports.addVertex("PEK");
        airports.addEdge("MCO", "ATL", 500);
        airports.addEdge("ATL", "JFK", 1500);
        airports.addEdge("ATL", "MSP", 900);
        airports.addEdge("MCO", "MSP", 1400);
        airports.addEdge("MCO", "SLC", 2500);
        airports.addEdge("ATL", "SLC", 2100);
        airports.addEdge("MSP", "SLC", 1100);
        airports.addEdge("ATL", "DEN", 2100);
        airports.addEdge("MCO", "SEA", 3000);
        airports.addEdge("ATL", "SEA", 2600);
        airports.addEdge("MSP", "SEA", 1400);
        airports.addEdge("MCO", "LAX", 2900);
        airports.addEdge("ATL", "LAX", 3000);
        airports.addEdge("SLC", "SEA", 600);
        airports.addEdge("SLC", "LAX", 1100);
        airports.addEdge("ATL", "LAS", 2400);
        airports.addEdge("SLC", "LAS", 1100);
        airports.addEdge("ATL", "HND", 7100);
        airports.addEdge("SEA", "HND", 5000);
        airports.addEdge("ATL", "ICN", 6900);
        airports.addEdge("SEA", "ICN", 4800);
        airports.addEdge("MSP", "ICN", 5800);
        airports.addEdge("ATL", "PVG", 7000);
        airports.addEdge("SEA", "PVG", 4900);
        airports.addEdge("LAX", "PVG", 5200);
        airports.addEdge("SEA", "PEK", 5000);
        airports.addEdge("ATL", "SFO", 2400);
        airports.addEdge("JFK", "SFO", 2900);
    }

    @Test
    void vertexCountTest() {
        assertEquals(14, airports.vertexCount());
        airports.addVertex("DXB");
        assertEquals(15, airports.vertexCount());
        airports.removeVertex("DXB");
        assertEquals(14, airports.vertexCount());
    }

    @Test
    void edgeCountTest() {
        assertEquals(28, airports.edgeCount());
        airports.addEdge("ATL", "PEK", 8000);
        assertEquals(29, airports.edgeCount());
        airports.removeEdge("ATL", "PEK");
        assertEquals(28, airports.edgeCount());
    }

    @Test
    void vertexTests() {
        assertFalse(airports.containsVertex("DXB"));
        assertTrue(airports.containsVertex("MCO"));
        assertTrue(airports.containsVertex("ATL"));
        assertTrue(airports.containsVertex("JFK"));
        assertTrue(airports.containsVertex("MSP"));
        assertTrue(airports.containsVertex("SLC"));
        assertTrue(airports.containsVertex("DEN"));
        assertTrue(airports.containsVertex("SEA"));
        assertTrue(airports.containsVertex("SFO"));
        assertTrue(airports.containsVertex("LAX"));
        assertTrue(airports.containsVertex("LAS"));
        assertTrue(airports.containsVertex("HND"));
        assertTrue(airports.containsVertex("ICN"));
        assertTrue(airports.containsVertex("PVG"));
        assertTrue(airports.containsVertex("PEK"));
    }

    @Test
    void edgeTests() {
        assertFalse(airports.containsEdge("ATL", "PEK"));
        assertTrue(airports.containsEdge("MCO", "ATL"));
        assertTrue(airports.containsEdge("ATL", "JFK"));
        assertTrue(airports.containsEdge("ATL", "MSP"));
        assertTrue(airports.containsEdge("MCO", "MSP"));
        assertTrue(airports.containsEdge("MCO", "SLC"));
        assertTrue(airports.containsEdge("ATL", "SLC"));
        assertTrue(airports.containsEdge("MSP", "SLC"));
        assertTrue(airports.containsEdge("ATL", "DEN"));
        assertTrue(airports.containsEdge("MCO", "SEA"));
        assertTrue(airports.containsEdge("MSP", "SEA"));
        assertTrue(airports.containsEdge("MCO", "LAX"));
        assertTrue(airports.containsEdge("ATL", "LAX"));
        assertTrue(airports.containsEdge("MSP", "SEA"));
        assertTrue(airports.containsEdge("SLC", "SEA"));
        assertTrue(airports.containsEdge("SLC", "LAX"));
        assertTrue(airports.containsEdge("ATL", "LAS"));
        assertTrue(airports.containsEdge("SLC", "LAS"));
        assertTrue(airports.containsEdge("ATL", "HND"));
        assertTrue(airports.containsEdge("SEA", "HND"));
        assertTrue(airports.containsEdge("ATL", "ICN"));
        assertTrue(airports.containsEdge("SEA", "ICN"));
        assertTrue(airports.containsEdge("MSP", "ICN"));
        assertTrue(airports.containsEdge("ATL", "PVG"));
        assertTrue(airports.containsEdge("SEA", "PVG"));
        assertTrue(airports.containsEdge("LAX", "PVG"));
        assertTrue(airports.containsEdge("SEA", "PEK"));
        assertTrue(airports.containsEdge("ATL", "SFO"));
        assertTrue(airports.containsEdge("JFK", "SFO"));
    }

    @Test
    void edgeWeightTest() {
        assertEquals(2900, airports.getEdgeWeight("JFK", "SFO"));
        airports.setEdgeWeight("JFK", "SFO", 5000);
        assertEquals(5000, airports.getEdgeWeight("JFK", "SFO"));
    }

    @Test
    void edgeRemovalTest() {
        assertTrue(airports.containsEdge("ATL", "SFO"));
        assertTrue(airports.removeEdge("ATL", "SFO"));
        assertFalse(airports.containsEdge("ATL", "SFO"));
    }

    @Test
    void edgeWeightFunctionTest() {
        assertEquals(2900, airports.getEdgeWeight("JFK", "SFO"));
        airports.setEdgeWeight("JFK", "SFO", (w) -> w * 2);
        assertEquals(5800, airports.getEdgeWeight("JFK", "SFO"));
    }

    @Test
    void vertexRemovalTest() {
        airports.addVertex("DXB");
        airports.addEdge("ATL", "DXB", 8000);
        assertTrue(airports.containsVertex("DXB"));
        assertEquals(29, airports.edgeCount());
        assertEquals(15, airports.vertexCount());
        assertTrue(airports.removeVertex("DXB"));
        assertEquals(28, airports.edgeCount());
        assertEquals(14, airports.vertexCount());
        assertFalse(airports.containsVertex("DXB"));
        assertFalse(airports.containsEdge("ATL", "DXB"));
    }

    @Test
    void cycleTest() {
        assertFalse(airports.containsCycle());
        assertTrue(airports.addEdge("LAX", "MCO", 2850));
        assertTrue(airports.containsCycle());
    }

    @Test
    void completeTest() {
        assertFalse(airports.isComplete());
        DirectedWeightedGraph<Integer> testGraph = new WeightedDiagraph<>();
        assertTrue(testGraph.addVertex(1));
        assertTrue(testGraph.addVertex(2));
        assertTrue(testGraph.addVertex(3));
        assertTrue(testGraph.addEdge(1, 2, 1));
        assertTrue(testGraph.addEdge(2, 3, 1));
        assertTrue(testGraph.addEdge(3, 1, 1));
        assertTrue(testGraph.addEdge(1, 3, 1));
        assertTrue(testGraph.addEdge(3, 2, 1));
        assertTrue(testGraph.addEdge(2, 1, 1));
        assertTrue(testGraph.isComplete());
    }

    @Test
    void componentTest() {
        assertEquals(1, airports.connectedComponentCount());
        assertTrue(airports.addVertex("DXB"));
        assertEquals(2, airports.connectedComponentCount());
        assertTrue(airports.removeVertex("DXB"));
        assertEquals(1, airports.connectedComponentCount());
    }

    @Test
    void pathExistenceTests() {
        assertTrue(airports.pathExists("MCO", "PEK"));
        assertFalse(airports.pathExists("JFK", "MCO"));
        assertTrue(airports.addEdge("PEK", "MCO", 9000));
        assertTrue(airports.pathExists("ATL", "MCO"));
    }

    @Test
    void shortestPathTests() {
        Path<String> flightPath = airports.shortestPath("MCO", "PEK");
        assertNotNull(flightPath);
        assertEquals(7800, flightPath.pathWeight());
        assertEquals(flightPath.getStartVertex(), "MCO");
        assertEquals(flightPath.getEndVertex(), "PEK");
        Iterator<String> itr = flightPath.iterator();
        assertEquals("MCO", itr.next());
        assertEquals("MSP", itr.next());
        assertEquals("SEA", itr.next());
        assertEquals("PEK", itr.next());
        assertFalse(itr.hasNext());
    }

    @Test
    void smallestWeightTests() {
        assertNull(airports.getMinimumDistance("JFK", "MCO"));
        assertEquals(7800, airports.getMinimumDistance("MCO", "PEK"));
        assertEquals(2900, airports.getMinimumDistance("MCO", "LAX"));
    }

    @Test
    void inDegreeTest() {
        assertEquals(0, airports.inDegree("MCO"));
        assertEquals(1, airports.inDegree("JFK"));
        assertEquals(2, airports.inDegree("HND"));
    }

    @Test
    void outDegreeTests() {
        assertEquals(5, airports.outDegree("MCO"));
        assertEquals(4, airports.outDegree("SEA"));
        assertEquals(0, airports.outDegree("HND"));
    }

    @Test
    void adjacencyTests() {
        Set<String> expected = Set.of("ATL", "LAX", "SEA", "SLC",  "MSP");
        Set<String> actual = new HashSet<>(airports.getAdjacencies("MCO"));
        assertEquals(expected, actual);
        expected = Set.of();
        actual = new HashSet<>(airports.getAdjacencies("HND"));
        assertEquals(expected, actual);
    }

    @Test
    void topologicalTest() {
        Iterator<String> itr = airports.topologicalSort().iterator();
        assertEquals("MCO", itr.next());
        assertEquals("ATL", itr.next());
        assertEquals("JFK", itr.next());
        assertEquals("MSP", itr.next());
        assertEquals("DEN", itr.next());
        assertEquals("SFO", itr.next());
        assertEquals("SLC", itr.next());
        assertEquals("SEA", itr.next());
        assertEquals("LAX", itr.next());
        assertEquals("LAS", itr.next());
        assertEquals("HND", itr.next());
        assertEquals("ICN", itr.next());
        assertEquals("PEK", itr.next());
        assertEquals("PVG", itr.next());
        assertFalse(itr.hasNext());
    }

    @Test
    void bipartiteTests() {
        assertFalse(airports.isBipartite());
        DirectedWeightedGraph<Integer> testGraph = new WeightedDiagraph<>();
        assertTrue(testGraph.addVertex(1));
        assertTrue(testGraph.addVertex(2));
        assertTrue(testGraph.addVertex(3));
        assertTrue(testGraph.addVertex(4));
        assertTrue(testGraph.addEdge(1, 2, 1));
        assertTrue(testGraph.addEdge(2, 3, 1));
        assertTrue(testGraph.addEdge(3, 4, 1));
        assertTrue(testGraph.addEdge(4, 1, 1));
        assertTrue(testGraph.isBipartite());
    }

    @Test
    void minimumSpanningTreeTest() {
        Graph<String> mst = airports.toMinimumSpanningTree("MCO");
        assertEquals(14, mst.vertexCount());
        assertEquals(13, mst.edgeCount());
        assertTrue(mst.containsEdge("MCO", "ATL"));
    }

    @Test
    void dfsTest() {
        Iterator<String> dfs = airports.depthFirstIterator("MCO");
        assertEquals("MCO", dfs.next());
        assertEquals("LAX", dfs.next());
        assertEquals("PVG", dfs.next());
        assertEquals("SEA", dfs.next());
        assertEquals("PEK", dfs.next());
        assertEquals("ICN", dfs.next());
        assertEquals("HND", dfs.next());
        assertEquals("SLC", dfs.next());
        assertEquals("LAS", dfs.next());
        assertEquals("MSP", dfs.next());
        assertEquals("ATL", dfs.next());
        assertEquals("SFO", dfs.next());
        assertEquals("DEN", dfs.next());
        assertEquals("JFK", dfs.next());
        assertFalse(dfs.hasNext());
    }

    @Test
    void bfsTest() {
        Iterator<String> bfs = airports.breadthFirstIterator("MCO");
        assertEquals("MCO", bfs.next());
        assertEquals("ATL", bfs.next());
        assertEquals("MSP", bfs.next());
        assertEquals("SLC", bfs.next());
        assertEquals("SEA", bfs.next());
        assertEquals("LAX", bfs.next());
        assertEquals("JFK", bfs.next());
        assertEquals("DEN", bfs.next());
        assertEquals("LAS", bfs.next());
        assertEquals("HND", bfs.next());
        assertEquals("ICN", bfs.next());
        assertEquals("PVG", bfs.next());
        assertEquals("SFO", bfs.next());
        assertEquals("PEK", bfs.next());
    }

    @Test
    void clearTest() {
        airports.clear();
        assertTrue(airports.isEmpty());
        assertEquals(0, airports.vertexCount());
        assertEquals(0, airports.edgeCount());
    }
}
