package algorithmen;

import java.util.Iterator;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import util.MeasureObject;

public class FloydWarshall {
	
	
	private MeasureObject zugriffsZaehler = new MeasureObject();
	private String startKnoten;
	private String endKnoten;
	private Graph graph;
	private Double[][] matrix;// = new Double[1][1];
	private String[] matrixIndizes;
	
	public FloydWarshall(Graph graph, String start, String ende) {

		this.graph=graph;
		startKnoten=start;
		endKnoten=ende;
		
		int matrixgroesse = graph.getNodeCount();
		
		matrix = new Double[matrixgroesse][matrixgroesse];
		matrixIndizes = new String[matrixgroesse];
		
		
		Iterator iterator = graph.getNodeIterator();
		int i = 0;
		while (iterator.hasNext()) {
			Node object = (Node) iterator.next();
			
			matrixIndizes[i]=object.getId();
			i++;
			
		}
		
//		System.out.println("die indizes der matrix:");
//		for(String elem : matrixIndizes){
//			System.out.println(elem);
//		}
		
		
	}
	
	
	public void suche(){
		
		zugriffsZaehler.startMeasure("Dijkstra von " + startKnoten + " nach " + endKnoten);
		zugriffsZaehler.log("Knotenanzahl: " + graph.getNodeCount());
		zugriffsZaehler.log("Kantenanzahl: " + graph.getEdgeCount());
		
		
	}
	
	//------------------------------------------------------------------------------------
	/**
	 * Initialisiert die Suche - also setzt alle distanzen aud default un so
	 */
	private void initialize(){
		
	}
	

}
