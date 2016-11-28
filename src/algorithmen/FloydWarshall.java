package algorithmen;

import java.util.ArrayList;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import application.GraphController;
import util.MeasureObject;

public class FloydWarshall {

	private MeasureObject zugriffsZaehler = new MeasureObject();
	private String startKnoten;
	private String endKnoten;
	private Graph graph;
	private Double[][] distanzMatrix;// = new Double[1][1];
	private Integer[][] transitMatrix;
	private String[] matrixIndizes;

	public FloydWarshall(Graph graph, String start, String ende) {

		this.graph = graph;
		startKnoten = start;
		endKnoten = ende;

		int matrixgroesse = graph.getNodeCount();

		distanzMatrix = new Double[matrixgroesse][matrixgroesse];
		transitMatrix = new Integer[matrixgroesse][matrixgroesse];
		matrixIndizes = new String[matrixgroesse];

		Iterator<Node> iterator = graph.getNodeIterator();
		int i = 0;
		while (iterator.hasNext()) {
			Node object = (Node) iterator.next();

			matrixIndizes[i] = object.getId();
			i++;

		}

		// System.out.println("die indizes der matrix:");
		// for(String elem : matrixIndizes){
		// System.out.println(elem);
		// }

	}

	public boolean suche() {

		if (startKnoten.equals(endKnoten)) {
			return true;
		}

		zugriffsZaehler.startMeasure("FloydWarschall von " + startKnoten + " nach " + endKnoten);
		zugriffsZaehler.log("Knotenanzahl: " + graph.getNodeCount());
		zugriffsZaehler.log("Kantenanzahl: " + graph.getEdgeCount());

		Node node1 = graph.getNode(startKnoten);
		Node node2 = graph.getNode(endKnoten);

		zugriffsZaehler.read("suche()", 2);

		if (node1 == null || node2 == null) {
			return false;
		}

		initialize();

		showDistanzmatrix();
		showTransitMatrix();
		
		
		for (int k = 0; k < matrixIndizes.length; k++) {
			for (int i = 0; i < matrixIndizes.length; i++) {
				for (int j = 0; j < matrixIndizes.length; j++) {
					//TODO if i=k Abfrage siehe algorithmus
					//wenn es eine kleinre distanze gibt
					if (distanzMatrix[i][j] > (distanzMatrix[i][k] + distanzMatrix[k][j])) {
						distanzMatrix[i][j] = distanzMatrix[i][k] + distanzMatrix[k][j];
						transitMatrix[i][j] = k;
						
//						if (distanzMatrix[i][j] < 0) { // abbruch f�r negative
//														// l�ngen
//							return;
//						}
					}
				}
			}
		}
		
		

		//Für j=1,...,|V|:
//		for (int j = 0; j < matrixIndizes.length; j++) {
//
//			//Für i=1,..|V| i!=j
//			for (int i = 0; i < matrixIndizes.length; i++) {
//
//				if (i != j) {
//
//					//für k=1,...|V| k!=j
//					for (int k = 0; k < matrixIndizes.length; k++) {
//
//						if (j != k) {
//
//							Double alt = distanzMatrix[i][k];
//							//Setzte d[i][k] = min(d[i][k], d[i][j]+d[j][k])
//							distanzMatrix[i][k] = Math.min(distanzMatrix[i][k], (distanzMatrix[i][j]+distanzMatrix[j][k]));
//
//							//wenn die entfernung aktualisiert wurde
//							if (alt!=distanzMatrix[i][k]) {
//
//								transitMatrix[i][k]=j;
//								
//							}
//						}
//
//					}
//				}
//
//			}
//
//		}


		showDistanzmatrix();
		showTransitMatrix();

		Integer indexStart = getIndex(startKnoten);
		Integer indexEnde = getIndex(endKnoten);

		zugriffsZaehler.stopMeasure();

		 ArrayList<String> kuerzesterWegalsInteger = new ArrayList<String>();
		// shortestWay(kuerzesterWegalsInteger, startKnoten, endKnoten);

		if (distanzMatrix[indexStart][indexEnde] != Double.POSITIVE_INFINITY) {
			// System.out.println(startKnoten+" nach "+endKnoten+"ist
			// erreichbar");
			return true;
		} else {
			return false;
		}

	}

	// ------------------------------------------------------------------------------------
	/**
	 * Initialisiert die Suche - also setzt alle distanzen auf default
	 */
	private void initialize() {

		// die matrix initialisieren
		for (int i = 0; i < matrixIndizes.length; i++) {

			for (int j = 0; j < matrixIndizes.length; j++) {

				if (i == j) {
					distanzMatrix[i][j] = 0.0;
					transitMatrix[i][j] = 0;
				} else {
					distanzMatrix[i][j] = Double.POSITIVE_INFINITY;
					transitMatrix[i][j] = -1;
				}

			}

		}

		zugriffsZaehler.read("initialize()", 1);
		Iterator<Edge> edgeIterator = graph.getEdgeIterator();

		while (edgeIterator.hasNext()) {
			Edge object = (Edge) edgeIterator.next();
			zugriffsZaehler.read("initialize()", 4);

			Node node1 = object.getSourceNode();

			Integer indexNode1 = getIndex(node1.getId());
			Integer indexNode2 = getIndex(object.getOpposite(node1).getId());

			Double gewicht = (Double) object.getAttribute(GraphController.EdgeAttributeWeight);

			if (gewicht != null) {

				distanzMatrix[indexNode1][indexNode2] = gewicht;
//				transitMatrix[indexNode1][indexNode2] = indexNode1;

				boolean directedGraph = (boolean) graph.getAttribute(GraphController.GraphAttributeDirected);
				if (!directedGraph) {
					distanzMatrix[indexNode2][indexNode1] = gewicht;
//					transitMatrix[indexNode2][indexNode1] = indexNode2;
				}

			}

		}

	}

	private Integer getIndex(String name) {

		int i = 0;

		for (String elem : matrixIndizes) {
			if (elem.equals(name)) {
				return i;
			}

			i++;
		}
		return null;
	}

	private String getName(Integer index) {
		return matrixIndizes[index];
	}

	private void showDistanzmatrix() {
		// ausgabe der Matrix
		System.out.println("-----------------------------------------------");
		System.out.println("Distanzmatrix:");
		for (int i = 0; i < distanzMatrix.length; i++) {
			System.out.print(matrixIndizes[i] + ":\t");
			for (int j = 0; j < distanzMatrix.length; j++) {
				System.out.print(distanzMatrix[i][j] + "\t ");
			}
			System.out.print("\n");
		}

	}

	private void showTransitMatrix() {

		System.out.println("-----------------------------------------------");
		System.out.println("Transitmatrix:");
		for (int i = 0; i < transitMatrix.length; i++) {
			System.out.print(matrixIndizes[i] + ": ");
			for (int j = 0; j < transitMatrix.length; j++) {
				System.out.print(transitMatrix[i][j] + " ");
			}
			System.out.print("\n");
		}

	}

	/**
	 * 
	 * @param liste
	 *            Liste enthält die jeweiligen Vorgänger aber nur die Indizes
	 * @param u
	 *            startKnoten
	 * @param v
	 *            der jeweilige vorgänger vom Zielknoten
	 */
	private void shortestWay(ArrayList<String> listeWeg, String u, String v) {
		
		int indexStart=getIndex(u);
		int indexEnde=getIndex(v);
		int indexMitte=transitMatrix[indexStart][indexEnde];
		String nameMitte=getName(indexMitte);
		
		if(indexMitte==0){
			//dann sind beide direkt miteinander verbunden
			listeWeg.add(v);
		}else{
			
			//weg von start zur mitte
			shortestWay(listeWeg, u, nameMitte);
			listeWeg.add(nameMitte);
			shortestWay(listeWeg, nameMitte, v);
			
			
		}
		
	}

	public Double getKosten(String start, String ende) {

		Node node1 = graph.getNode(start);
		Node node2 = graph.getNode(ende);
		if (node1 == null || node2 == null) {

			return null;

		}
		Double kosten = distanzMatrix[getIndex(node1.getId())][getIndex(node2.getId())];
		return kosten;

	}

}
