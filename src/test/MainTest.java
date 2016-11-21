package test;

import static org.junit.Assert.*;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.Before;
import org.junit.Test;

import algorithmen.Dijkstra;
import algorithmen.FloydWarshall;
import application.GraphController;
import generator.BIG;
import util.FileParser;

public class MainTest {
	// File Parser für Dijktsra und für Floyd Warshall
	static String fp01;
	static FileParser fp02;
	// File Parser für BIG
	static String fp03;
	static FileParser fp04;
	// Dijkstra
	static Dijkstra dijkstra01;
	static Dijkstra dijkstra02;
	static Dijkstra dijkstra03;
	static Dijkstra dijkstra04;
	static Dijkstra dijkstra05;
	static Dijkstra dijkstra06;
	//Der BIG Graph
	static BIG big;
	// Floyd Warshall
	static FloydWarshall fw01;
	static FloydWarshall fw02;
	static FloydWarshall fw03;
	static FloydWarshall fw04;
	static FloydWarshall fw05;
	static FloydWarshall fw06;
	// Edges für den Test Graphen
	static org.graphstream.graph.Edge edge01;
	static org.graphstream.graph.Edge edge02;
	static org.graphstream.graph.Edge edge03;
	static org.graphstream.graph.Edge edge04;
	static org.graphstream.graph.Edge edge05;
	static org.graphstream.graph.Edge edge06;	
	static org.graphstream.graph.Edge edge07;
	static org.graphstream.graph.Edge edge08;
	//Weitere Edges
	static org.graphstream.graph.Edge edge09;
	static org.graphstream.graph.Edge edge10;
	static org.graphstream.graph.Edge edge11;
	static org.graphstream.graph.Edge edge12;
	// Nodes für den Test Graphen
	static Node node01;
	static Node node02;
	static Node node03;
	static Node node04;
	static Node node05;
	static Node node06;
	static Node node07;
	static Node node08;
	static Node node09;
	static Node node10;
	static Node node11;
	static Node node12;
	// Graphen für Dijktsra und für Floyd Warshall
	static Graph graph01;
	static Graph graph02;
	static Graph graph03;
	// Graphen für BIG
	static Graph graph04;
	static Graph graph05;


	@Before
	public void setUp() throws Exception {
		graph01 = new MultiGraph("graph01");
		node01 = graph01.addNode("1");
		node02 = graph01.addNode("2");
		node03 = graph01.addNode("3");
		node04 = graph01.addNode("4");
		node05 = graph01.addNode("5");
		node06 = graph01.addNode("7");

		graph01.addAttribute(GraphController.GraphAttributeDirected, true);
		graph01.addEdge("12", "1", "2", true);
		graph01.getEdge("12").addAttribute("Gewicht", 1.0);

		graph01.addEdge("13", "1", "3", true);
		graph01.getEdge("13").addAttribute("Gewicht", 5.3);

		graph01.addEdge("23", "2", "3", true);
		graph01.getEdge("23").addAttribute("Gewicht", 4.5);

		graph01.addEdge("24", "2", "4", true);
		graph01.getEdge("24").addAttribute("Gewicht", 2.0);

		graph01.addEdge("35", "3", "5", true);
		graph01.getEdge("35").addAttribute("Gewicht", 3.4);

		graph01.addEdge("45", "4", "5", true);
		graph01.getEdge("45").addAttribute("Gewicht", 600.0);

		graph01.addEdge("25", "2", "5", true);
		graph01.getEdge("25").addAttribute("Gewicht", -5.0);

		graph01.addEdge("14", "1", "4", true);
		graph01.getEdge("14").addAttribute("Gewicht", 600.0);

		edge01 = graph01.getEdge("12");
		edge02 = graph01.getEdge("13");
		edge03 = graph01.getEdge("23");
		edge04 = graph01.getEdge("24");
		edge05 = graph01.getEdge("35");
		edge06 = graph01.getEdge("45");
		edge07 = graph01.getEdge("25");
		edge08 = graph01.getEdge("14");

		graph02 = new MultiGraph("graph02");
		fp01 = "src/data/graph03.gka";
		fp02 = new FileParser(graph02, fp01);
		fp02.parsefile();

		graph04 = new MultiGraph("graph04");
		fp03 = "src/data/BIG02.gka";
		fp04 = new FileParser(graph04, fp03);
		fp04.parsefile();

		graph05 = new MultiGraph("graph05");
		big = new BIG(graph05, 100, 2500);
		big.generate();
	}

	@Test
	public void testDijkstra() {
		Double vg01 = 3.0;
		Double vg02 = 518.0;
		dijkstra01 = new Dijkstra(graph01, "1", "4");
		dijkstra02 = new Dijkstra(graph01, "1", "7");

		assertTrue(dijkstra01.suche());
		assertEquals(vg01, dijkstra01.getKosten("1", "4"));

		assertFalse(dijkstra02.suche());
		assertEquals(null, dijkstra02.getKosten("1", "6"));
		// ------------------------------------------------------

		dijkstra03 = new Dijkstra(graph02, "Hamburg", "Husum");
		dijkstra04 = new Dijkstra(graph02, "Hamburg", "Berlin");

		assertTrue(dijkstra03.suche());
		assertEquals(vg02, dijkstra03.getKosten("Hamburg", "Husum"));

		assertFalse(dijkstra04.suche());
		assertEquals(null, dijkstra04.getKosten("Hamburg", "Berlin"));
	}

	@Test
	public void testFloydWarshall() {
		Double vg = 3.0;
		Double vg02 = 518.0;

		dijkstra03 = new Dijkstra(graph02, "Hamburg", "Husum");
		dijkstra04 = new Dijkstra(graph02, "Hamburg", "Berlin");

		fw01 = new FloydWarshall(graph01, "1", "4");
		fw02 = new FloydWarshall(graph01, "1", "7");

		assertTrue(fw01.suche());
		assertEquals(vg, fw01.getKosten("1", "4"));

		assertFalse(fw02.suche());
		assertEquals(null, fw02.getKosten("1", "6"));

		// --------------------------------------------------------

		fw03 = new FloydWarshall(graph02, "Hamburg", "Husum");
		fw04 = new FloydWarshall(graph02, "Hamburg", "Berlin");
		
		//------Tests für den Graphen03 mit Flyod Warshall-----------------
		assertTrue(fw03.suche());
		assertEquals(vg02, fw03.getKosten("Hamburg", "Husum"));
		assertFalse(fw04.suche());
		assertEquals(null, fw04.getKosten("Hamburg", "Berlin"));
		
		//------Tests für den Vergleich zwischen Floyd Warshall und Dijkstra-----------------------
		dijkstra03.suche();
		assertEquals(dijkstra03.getKosten("Hamburg", "Husum"), fw03.getKosten("Hamburg", "Husum"));
		dijkstra04.suche();
		assertEquals(dijkstra04.getKosten("Hamburg", "Berlin"), fw04.getKosten("Hamburg", "Berlin"));

	}

	@Test
	public void testBIG() {
		Double vg03 = 7.0;

		assertEquals(100, graph05.getNodeCount());
		assertEquals(2500, graph05.getEdgeCount());

		dijkstra05 = new Dijkstra(graph04, "Node1", "v1");
		dijkstra06 = new Dijkstra(graph04, "Node1", "Node101");

		fw05 = new FloydWarshall(graph04, "Node1", "v1");
		fw06 = new FloydWarshall(graph04, "Node1", "Node101");
		
		//------Tests für den BIG mit Dijkstra-----------------------
		assertTrue(dijkstra05.suche());
		assertEquals(vg03, dijkstra05.getKosten("Node1", "v1"));
		assertFalse(dijkstra06.suche());
		assertEquals(null, dijkstra06.getKosten("Node1", "Node101"));
		
		//------Tests für den BIG mit Flyod Warshall-----------------
		assertTrue(fw05.suche());
		assertEquals(vg03, fw05.getKosten("Node1", "v1"));
		assertFalse(fw06.suche());
		assertEquals(null, fw06.getKosten("Node1", "Node101"));

	}
}
