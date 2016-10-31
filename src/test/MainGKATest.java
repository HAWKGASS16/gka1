package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.junit.Before;
import org.junit.Test;

import gka1gc.MainGKA;

public class MainGKATest {

	static String fp;
	static String fp2;
	static MainGKA main01;
	static MainGKA main02;
	static String start01;
	static String start02;
	static String start03;
	static String start04;
	static String ende01;
	static String ende02;
	static String ende03;
	static String ende04;
	static String ende05;
	static org.graphstream.graph.Edge edge01;
	static org.graphstream.graph.Edge edge02;
	static org.graphstream.graph.Edge edge03;
	static org.graphstream.graph.Edge edge04;
	static Node node01;
	static Node node02;
	static Node node03;
	static Node node04;
	static Node node05;
	static Graph graph01;
	static Graph graph03;
	static String EdgeAttributeWeight;
	static ArrayList<org.graphstream.graph.Edge> testEdgeList = new ArrayList<org.graphstream.graph.Edge>();
	static ArrayList<org.graphstream.graph.Edge> mainEdgeList= new ArrayList<org.graphstream.graph.Edge>();
	static HashSet<gka1gc.Edge> edgeSet;

	@Before
	public void initialize() throws Exception {

		graph01 = new MultiGraph("graph01");
		node01 = graph01.addNode("a");
		node02 = graph01.addNode("b");
		node03 = graph01.addNode("l");
		node04 = graph01.addNode("f");
		node05 = graph01.addNode("i");

		graph01.addEdge("ef", node03, node04, true);
		graph01.addEdge("be", node02, node03, true);
		graph01.addEdge("ab", node01, node02, true);
		
		edge03 = graph01.getEdge("ef");
		edge02 = graph01.getEdge("be"); 
		edge01 =  graph01.getEdge("ab");


	}

	@Test
	public void testBtsSuche() {
		fp = "H:\\Files\\Dropbox\\Dropbox\\Uni\\Semester 3\\GKA\\Hölings\\bspGraphen\\graph01.gka";
		main01 = new MainGKA(fp);
		start01 = "a";
		ende01 = "f";
		start02 = "b";
		ende02 = "i";

		main01.btsSuche(start01, ende01);
		testEdgeList.add(edge03);
		testEdgeList.add(edge02);
		testEdgeList.add(edge01);

		System.out.println(testEdgeList);
		System.out.println(main01.getShortestWay());
		
		assertEquals(testEdgeList, main01.getShortestWay());
		assertTrue(main01.btsSuche(start01, ende01));
		assertFalse(main01.btsSuche(start02, ende02));
	}

}


