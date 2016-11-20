package test;

import static org.junit.Assert.*;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.Before;
import org.junit.Test;

import algorithmen.Dijkstra;
import algorithmen.FloydWarshall;
import util.FileParser;

public class MainTest {
	//File Parser für Dijktsra und für Floyd Warshall 
	static String fp01;
	static FileParser fp02;
	//File Parser für BIG
	static String fp03;
	static FileParser fp04;
	//Dijkstra
	static Dijkstra dijkstra01;
	static Dijkstra dijkstra02;
	static Dijkstra dijkstra03;
	static Dijkstra dijkstra04;
	static Dijkstra dijkstra05;
	static Dijkstra dijkstra06;
	//Floyd Warshall
	static FloydWarshall fw01;
	static FloydWarshall fw02;
	static FloydWarshall fw03;
	static FloydWarshall fw04;
	static FloydWarshall fw05;
	static FloydWarshall fw06;
	//Edges für Dijkstra
	static org.graphstream.graph.Edge edge01;
	static org.graphstream.graph.Edge edge02;
	static org.graphstream.graph.Edge edge03;
	static org.graphstream.graph.Edge edge04;
	static org.graphstream.graph.Edge edge05;
	static org.graphstream.graph.Edge edge06;
	//Edges für Floyd Warshall
	static org.graphstream.graph.Edge edge07;
	static org.graphstream.graph.Edge edge08;
	static org.graphstream.graph.Edge edge09;
	static org.graphstream.graph.Edge edge10;
	static org.graphstream.graph.Edge edge11;
	static org.graphstream.graph.Edge edge12;
	//Nodes für Dijkstra
	static Node node01;
	static Node node02;
	static Node node03;
	static Node node04;
	static Node node05;
	static Node node06;
	// Nodes für Floyd Warshall
	static Node node07;
	static Node node08;
	static Node node09;
	static Node node10;
	static Node node11;
	static Node node12;
	//Graphen für Dijktsra und für Floyd Warshall
	static Graph graph01;
	static Graph graph02;
	static Graph graph03;
	//Graphen für BIG
	static Graph graph04;

	
	@Before
	public void setUp() throws Exception {
		graph01 = new MultiGraph("graph01");
		node01 = graph01.addNode("1");
		node02 = graph01.addNode("2");
		node03 = graph01.addNode("3");
		node04 = graph01.addNode("4");
		node05 = graph01.addNode("5");
		node06 = graph01.addNode("6");
		
		graph01.addEdge("12", "1", "2");
		graph01.getEdge("12").addAttribute("Gewicht", 1.0);
		graph01.addEdge("13", "1", "3");
		graph01.getEdge("13").addAttribute("Gewicht", 5.3);
		graph01.addEdge("23", "2", "3");
		graph01.getEdge("23").addAttribute("Gewicht", 4.5);
		graph01.addEdge("24", "2", "4");
		graph01.getEdge("24").addAttribute("Gewicht", 2.0);
		graph01.addEdge("35", "3", "5");
		graph01.getEdge("35").addAttribute("Gewicht", 3.4);
		graph01.addEdge("45", "4", "5");
		graph01.getEdge("45").addAttribute("Gewicht", 600.0);
		
		edge01 = graph01.getEdge("12");
		edge02 = graph01.getEdge("13");
		edge03 = graph01.getEdge("23");
		edge04 = graph01.getEdge("24");
		edge05 = graph01.getEdge("35");
		edge06 = graph01.getEdge("45");
		
		graph02 = new MultiGraph("graph02");
		fp01 = "src/data/graph03.gka";
		fp02 = new FileParser(graph02, fp01);
		fp02.parsefile();
		
		graph04 = new MultiGraph("graph04");
		fp03 = "src/data/SMALL01.gka";
		fp04 = new FileParser(graph04, fp03);
		fp04.parsefile();
	}

	@Test
	public void testDijkstra() {
		dijkstra01 = new Dijkstra(graph01, "1", "4");
		dijkstra02 = new Dijkstra(graph01, "1", "6");
		
		assertTrue(dijkstra01.suche());
		assertFalse(dijkstra02.suche());
		
//------------------------------------------------------
		
		dijkstra03 = new Dijkstra(graph02, "Hamburg", "Husum");
		dijkstra04 = new Dijkstra(graph02, "Hamburg", "Berlin");
		
		assertTrue(dijkstra03.suche());
		assertFalse(dijkstra04.suche());
		
		
	}
	@Test
	public void testFloydWarshall() {
		fw01 = new FloydWarshall(graph01, "1", "4");
		fw02 = new FloydWarshall(graph01, "1", "6");
		
//		assertTrue(fW01.suche());
//		assertFalse(fW02.suche());

//--------------------------------------------------------
		
		fw03 = new FloydWarshall(graph02,"Hamburg","Husum");
		fw04 = new FloydWarshall(graph02,"Hamburg","Berlin");
		
//		assertTrue(fW03.suche());
//		assertFalse(fW04.suche());
		
	}
	
	@Test
	public void testBIG(){
		
		dijkstra05 = new Dijkstra(graph04,"node7","node5");
		dijkstra06 = new Dijkstra(graph04, "node9", "node2");
		
		fw05 = new FloydWarshall(graph04,"node7","node5");
		fw06 = new FloydWarshall(graph04, "node9","node2");
		
		
		assertTrue(dijkstra05.suche());
		assertFalse(dijkstra06.suche());
//		assertTrue(fw05.suche());
//		assertFalse(fw06.suche());
	}
}
