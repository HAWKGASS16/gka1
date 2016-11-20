package algorithmen;

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

		Iterator iterator = graph.getNodeIterator();
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

	public void suche() {

		zugriffsZaehler.startMeasure("FloydWarschall von " + startKnoten + " nach " + endKnoten);
		zugriffsZaehler.log("Knotenanzahl: " + graph.getNodeCount());
		zugriffsZaehler.log("Kantenanzahl: " + graph.getEdgeCount());

		initialize();
		
		
		showDistanzmatrix();
		showTransitMatrix();

		for (int j = 0; j < matrixIndizes.length; j++) {

			for (int i = 0; i < matrixIndizes.length; i++) {

				if (i != j) {

					for (int k = 0; k < matrixIndizes.length; k++) {
						if(k!=j){
							Double momentangewicht = distanzMatrix[i][k];
							Double neuesGewicht = Math.min(momentangewicht, (distanzMatrix[i][j]+distanzMatrix[j][k]));
							if(momentangewicht!=neuesGewicht){
								transitMatrix[i][k]=j;
							}
							distanzMatrix[i][k]=neuesGewicht;
							
							
						
							
						}
						
					}
					if(distanzMatrix[i][i]<0){
						return;
					}

				}

			}

		}
		showDistanzmatrix();
		showTransitMatrix();

	}

	// ------------------------------------------------------------------------------------
	/**
	 * Initialisiert die Suche - also setzt alle distanzen aud default un so
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
					transitMatrix[i][j]= -1;
				}

			}

		}

		// zugriffsZaehler.read("initialize()", 1);
		Iterator edgeIterator = graph.getEdgeIterator();

		while (edgeIterator.hasNext()) {
			Edge object = (Edge) edgeIterator.next();
			// zugriffsZaehler.read("initialize()", 1);

			Node node1 = object.getSourceNode();

			Integer indexNode1 = getIndex(node1.getId());
			Integer indexNode2 = getIndex(object.getOpposite(node1).getId());

			Double gewicht = (Double) object.getAttribute(GraphController.EdgeAttributeWeight);

			if (gewicht != null) {

				distanzMatrix[indexNode1][indexNode2] = gewicht;
				transitMatrix[indexNode1][indexNode2]= indexNode1;
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

	private void showDistanzmatrix() {
		// ausgabe der Matrix
		System.out.println("-----------------------------------------------");
		System.out.println("Distanzmatrix:");
		for (int i = 0; i < distanzMatrix.length; i++) {
			System.out.print(matrixIndizes[i] + ": ");
			for (int j = 0; j < distanzMatrix.length; j++) {
				System.out.print(distanzMatrix[i][j] + " ");
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

}
