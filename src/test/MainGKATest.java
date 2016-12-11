//package test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashSet;
//
//import org.graphstream.graph.Graph;
//import org.graphstream.graph.Node;
//import org.graphstream.graph.implementations.MultiGraph;
//import org.junit.Before;
//import org.junit.Test;
//
//import interfaces.MainGKA;
//
//public class MainGKATest {
//	//TODO Interfaces für die benötigten Methoden
//	//TODO Weitere Tests für Grenzfälle
//	//TODO Speichern und laden testen
//	//TODO Set von Methoden als String speichern und vergleichen
//
//	static String fp;
//	//Test für das Speichern
//	static String fp2;
//	static String fp3;
//	//
//	static String target;
//	static String source;
//	//Test für das Speichern
//	static MainGKA main03;
//	static String start01;
//	static String start02;
//	static String start03;
//	static String start04;
//	static String ende01;
//	static String ende02;
//	static String ende03;
//	static String ende04;
//	static String ende05;
//	//Edges für BTS Test
//	static org.graphstream.graph.Edge edge01;
//	static org.graphstream.graph.Edge edge02;
//	static org.graphstream.graph.Edge edge03;
//	static org.graphstream.graph.Edge edge04;
//	//Edges für Speicherungstest
//	static org.graphstream.graph.Edge edge05;
//	static org.graphstream.graph.Edge edge06;
//	static org.graphstream.graph.Edge edge07;
//	static org.graphstream.graph.Edge edge08;
//	static org.graphstream.graph.Edge edge09;
//	static org.graphstream.graph.Edge edge10;
//	//Weitere Edges
//	
//	//Nodes für den Bts Test
//	static Node node01;
//	static Node node02;
//	static Node node03;
//	static Node node04;
//	static Node node05;
//	//Nodes für den Speicherungstest
//	static Node node06;
//	static Node node07;
//	static Node node08;
//	static Node node09;
//	static Node node10;
//	// Weitere Nodes
//	static Node node11;
//	static Node node12;
//	static Node node13;
//	static Node node14;
//	static Node node15;
//	static Node node16;
//	static Node node17;
//	static Node node18;
//	static Node node19;
//	static Node node20;
//	static Graph graph01;
//	static Graph graph02;
//	static Graph graph03;
//	static String EdgeAttributeWeight;
//	static ArrayList<org.graphstream.graph.Edge> testEdgeList = new ArrayList<org.graphstream.graph.Edge>();
//	static ArrayList<org.graphstream.graph.Edge> mainEdgeList= new ArrayList<org.graphstream.graph.Edge>();
//	static HashSet<util.Edge> edgeSet;
//
//	@Before
//	public void initialize() throws Exception {
//		//Initialisierung für die BTS Suche
//		graph01 = new MultiGraph("graph01");
//		node01 = graph01.addNode("a");
//		node02 = graph01.addNode("b");
//		node03 = graph01.addNode("l");
//		node04 = graph01.addNode("f");
//		node05 = graph01.addNode("i");
//
//		graph01.addEdge("lf", node03, node04, true);
//		graph01.addEdge("bl", node02, node03, true);
//		graph01.addEdge("ab", node01, node02, true);
//		
//		edge03 = graph01.getEdge("lf");
//		edge02 = graph01.getEdge("bl"); 
//		edge01 =  graph01.getEdge("ab");
//		
//		//Initialisierung für die Speicherung
//		graph02 = new MultiGraph("graph02");
//		node06 = graph02.addNode("1");
//		node07 = graph02.addNode("2");
//		node08 = graph02.addNode("3");
//		node09 = graph02.addNode("4");
//		node10 = graph02.addNode("5");
//		
//		graph02.addEdge("12", "1", "2");
//		graph02.addEdge("13", "1", "3");
//		graph02.addEdge("23", "2", "3");
//		graph02.addEdge("24", "2", "4");
//		graph02.addEdge("35", "3", "5");
//		graph02.addEdge("45", "4", "5");
//		
//		edge05 = graph02.getEdge("12");
//		edge06 = graph02.getEdge("13");
//		edge07 = graph02.getEdge("23");
//		edge08 = graph02.getEdge("24");
//		edge09 = graph02.getEdge("35");
//		edge10 = graph02.getEdge("45");
//
//
//	}
//	
//
////	@Test
////	public void testBtsSuche() {
////		fp = "src/data/graph01.gka";
////		main01 = new MainGKA(fp);
////		start01 = "a";
////		ende01 = "f";
////		start02 = "b";
////		ende02 = "i";
////
////		main01.btsSuche(start01, ende01);
////		testEdgeList.add(edge03);
////		testEdgeList.add(edge02);
////		testEdgeList.add(edge01);
////
////		System.out.println(testEdgeList);
////		System.out.println(main01.getShortestWay());
////		
////		assertEquals(testEdgeList.toString(), main01.getShortestWay().toString());
////		assertTrue(main01.btsSuche(start01, ende01));
////		assertFalse(main01.btsSuche(start02, ende02));
////	}
//	@Test
//	public void testDijkstra(){
//		
//	}
//
//	
//	
//
//}
//
//
