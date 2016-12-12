package test;

import static org.junit.Assert.assertEquals;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.Before;
import org.junit.Test;

//import algorithmen.EdmondKarp;
import algorithmen.FordFulkerson;
import application.GraphController;
import generator.BigNetGraph;
import util.FileParser;

public class FordEdmondTest {
	// File Parser für Ford-Fulkerson und für Edmond-Karp
	static String fp01;
	static FileParser fp02;
	// File Parser für BigNet
	static String fp03;
	static FileParser fp04;
	// Weitere FileParser
	static String fp05;
	static FileParser fp06;

	static String fp07;
	static FileParser fp08;

	static String fp09;
	static FileParser fp10;
	// Ford-Fulkerson
	static FordFulkerson ff01;
	static FordFulkerson ff02;
	static FordFulkerson ff03;
	static FordFulkerson ff04;
	static FordFulkerson ff05;
	static FordFulkerson ff06;
	static FordFulkerson ff07;
	static FordFulkerson ff08;
	static FordFulkerson ff09;
	// Der BigNet Graph
	static BigNetGraph bigNet01;
	static BigNetGraph bigNet02;
	static BigNetGraph bigNet03;
	// Edmond-Karp

	// Edges für den Test Graphen
	static org.graphstream.graph.Edge edge01;
	static org.graphstream.graph.Edge edge02;
	static org.graphstream.graph.Edge edge03;
	static org.graphstream.graph.Edge edge04;
	static org.graphstream.graph.Edge edge05;
	static org.graphstream.graph.Edge edge06;
	static org.graphstream.graph.Edge edge07;
	static org.graphstream.graph.Edge edge08;
	// Weitere Edges
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
	// Graphen für Ford-Fulkerson und für Edmond-Karp
	static Graph graph01;
	static Graph graph02;
	static Graph graph03;

	// Graphen für BIG
	static Graph graph04;
	static Graph graph05;
	static Graph graph06;
	static Graph graph07;

	// Weitere Graphen
	static Graph graph08;
	static Graph graph09;
	static Graph graph10;
	static Graph graph11;
	static Graph graph12;

	@Before
	public void setUp() throws Exception {
		graph01 = new MultiGraph("graph01");
		node01 = graph01.addNode("q");
		graph01.getNode("q");
		node02 = graph01.addNode("v1");
		graph01.getNode("v1");
		node03 = graph01.addNode("v2");
		graph01.getNode("v2");
		node04 = graph01.addNode("v3");
		graph01.getNode("v3");
		node05 = graph01.addNode("v5");
		graph01.getNode("v5");
		node06 = graph01.addNode("s");
		graph01.getNode("s");
		// Nicht verbundene Node
		// node07 = graph01.addNode("v7");

		// Netzwerk aus der Vorlesung (thm05 Seite 31)
		graph01.addAttribute(GraphController.GraphAttributeDirected, true);
		graph01.addEdge("qv2", "q", "v2", true);
		graph01.getEdge("qv2").addAttribute("Gewicht", 4.0);

		graph01.addEdge("qv1", "q", "v1", true);
		graph01.getEdge("qv1").addAttribute("Gewicht", 5.0);

		graph01.addEdge("qv5", "q", "v5", true);
		graph01.getEdge("qv5").addAttribute("Gewicht", 1.0);

		graph01.addEdge("v1s", "v1", "s", true);
		graph01.getEdge("v1s").addAttribute("Gewicht", 3.0);

		graph01.addEdge("v1v5", "v1", "v5", true);
		graph01.getEdge("v1v5").addAttribute("Gewicht", 1.0);

		graph01.addEdge("v1v3", "v1", "v3", true);
		graph01.getEdge("v1v3").addAttribute("Gewicht", 1.0);

		graph01.addEdge("v2v3", "v2", "v3", true);
		graph01.getEdge("v2v3").addAttribute("Gewicht", 2.0);

		graph01.addEdge("v3s", "v3", "s", true);
		graph01.getEdge("v3s").addAttribute("Gewicht", 3.0);

		graph01.addEdge("v5s", "v5", "s", true);
		graph01.getEdge("v5s").addAttribute("Gewicht", 3.0);

		graph02 = new MultiGraph("graph02");
		fp01 = "src/data/graph04.gka";
		fp02 = new FileParser(graph02, fp01);
		fp02.parsefile();

//		graph03 = new MultiGraph("graph03");
//		fp03 = "src/data/BigNet_Gruppe2_6.gka";
//		fp04 = new FileParser(graph03, fp03);
//		fp04.parsefile();

		graph04 = new MultiGraph("graph04");
		fp05 = "src/data/testgraph02.gka";
		fp06 = new FileParser(graph04, fp05);
		fp06.parsefile();

		graph05 = new MultiGraph("graph05");
		fp07 = "src/data/testgraph03.gka";
		fp08 = new FileParser(graph05, fp07);
		fp08.parsefile();

		graph09 = new MultiGraph("graph09");
		bigNet01 = new BigNetGraph(graph09, 80, 300);
		bigNet01.generateBigNet();

		graph06 = new MultiGraph("graph06");
		bigNet02 = new BigNetGraph(graph06, 250, 2000);
		bigNet02.generateBigNet();
	}

	@Test
	public void testFordFulkerson() {

		ff01 = new FordFulkerson(graph01, true);
		// ff02 = new FordFulkerson(graph02, true);

		ff03 = new FordFulkerson(graph04, true);
		ff04 = new FordFulkerson(graph05, true);

		ff01.maxflow();
		assertEquals(8.0, ff01.getMaxFlow(), 0.001);
//		ff02.maxflow();
//		assertEquals(12.0, ff02.getMaxFlow(), 0.001);
		ff03.maxflow();
		assertEquals(1.0, ff03.getMaxFlow(), 0.001);
		ff04.maxflow();
		assertEquals(3.0, ff04.getMaxFlow(), 0.001);

	}

	@Test
	public void testEdmondKarp() {
		ff07 = new FordFulkerson(graph01, false);
		ff08 = new FordFulkerson(graph04, false);
		ff09 = new FordFulkerson(graph05, false);
		
		ff07.maxflow();
		assertEquals(8.0, ff07.getMaxFlow(), 0.001);
		ff08.maxflow();
		assertEquals(1.0, ff08.getMaxFlow(), 0.001);
		ff09.maxflow();
		assertEquals(0.0, ff09.getMaxFlow(), 0.001);
	}

//	@Test
//	public void testFordEdmund100TimesDouble() {
//		long alpha = 0;
//		long beta = 0;
//
////		graph07 = new MultiGraph ("graph07");
////		bigNet03 = new BigNetGraph(graph07, 80, 2300);
////		bigNet03.generateBigNet();
//
//
//		ff05 = new FordFulkerson(graph05, true);
//		ff06 = new FordFulkerson(graph05, false);
//
//		for (int i = 0; i <= 99; i++) {
//
//			long startford = System.nanoTime();
//
//			ff05.maxflow();
//			ff05.getMaxFlow();
//			long endford = System.nanoTime();
//			alpha += endford - startford;
//			System.out.println(ff05.getMaxFlow());
//
//			long startedmond = System.nanoTime();
//
//			ff06.maxflow();
//			ff06.getMaxFlow();
//			long endedmond = System.nanoTime();
//			beta += endedmond - startedmond;
//			System.out.println(ff06.getMaxFlow());
//
//			assertEquals(ff05.getMaxFlow(), ff06.getMaxFlow(), 0.001);
//		}
//		System.out.println("Laufzeit für 100 mal ausführen von Ford: " + alpha * ((1.0 / 0.06) * Math.pow(10, -10)));
//		System.out.println("Laufzeit für 100 mal ausführen von Edmond: " + beta * ((1.0 / 0.06) * Math.pow(10, -10)));
//
//	}

	@Test
	public void testBigNet() {
		assertEquals(80.0, graph09.getNodeCount(), 0.001);
		assertEquals(298.0, graph09.getEdgeCount(), 0.001);
		assertEquals(250.0, graph06.getNodeCount(), 0.001);
		assertEquals(1998.0, graph06.getEdgeCount(), 0.001);

	}
}
