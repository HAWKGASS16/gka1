package algorithmen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import application.GraphController;
import util.MeasureObject;

public class FordFulk {

	private Graph graph;
	private final String attrEdgeFluss = "FordfulkersonKantenFluss";
	private final String attrEdgeGewicht = GraphController.EdgeAttributeWeight;
	private final String attrNodeVorgaenger = "FordFulkersonVorgaenger";
	private final String attrNodeInspiziert = "FordFulkersonInspiziert";
	private final String attrNodeDelta = "FordFulkersonFluss";

	// BTS attribute
	private final String attNodeBTSMarkiert = GraphController.NodeAttributVisited;
	private final String attNodeBTSGewicht = "KnotenGewichtBTS";
	private final String attNodeBTSvorgaenger = "KnotenvorgaengerBTS";

	private HashSet<Node> markierteKnoten = new HashSet<Node>();
	private HashSet<Node> inspizierterKnoten = new HashSet<Node>();
	private Queue<Node> btsQueue = new LinkedList<Node>();

	private MeasureObject messObjekt = new MeasureObject();

	private boolean edmondsUndKarp = false;

	private Double doubleEpsilon = 0.05;

	private Integer delta = 0;

	public FordFulk(Graph graph) {
		this.graph = graph;
	}

	public FordFulk(Graph graph, boolean modusEdmondsKarp) {
		this.graph = graph;
		edmondsUndKarp = modusEdmondsKarp;
	}

	private boolean initialisierung() {
		// jede kannte bekommt den initialfluss von 0

		for (Edge kante : graph.getEachEdge()) {

			messObjekt.read("initialisierung()", 1);
			messObjekt.write("initialisierung()", 1);

			kante.setAttribute(attrEdgeFluss, new Double(0));
			kantenLabel(kante, 0.0);

		}
		for (Node knoten : graph.getEachNode()) {
			knoten.setAttribute(attrNodeDelta, 0.0);
			knoten.setAttribute(attrNodeVorgaenger, null);
		}

		Node quelle = graph.getNode("q");
		messObjekt.read("initialisierung()", 1);
		Node senke = graph.getNode("s");

		if (quelle == null || senke == null) {
			return false;
		}

		quelle.setAttribute(attrNodeDelta, Double.POSITIVE_INFINITY);

		messObjekt.write("initialisierung()", 3);

		markierteKnoten.add(quelle);

		breitensuche();

		return true;
	}

	private void kantenLabel(Edge kante, double fluss) {
		messObjekt.read("kantenLabel()", 1);
		messObjekt.write("kantenLabel()", 1);

		Double kantengewicht = (Double) kante.getAttribute(attrEdgeGewicht);

		kante.setAttribute("ui.label", "" + fluss + " / " + kantengewicht);

	}

	public void maxflow() {

		if (edmondsUndKarp) {
			messObjekt.startMeasure("EdmondsKarp");
		} else {
			messObjekt.startMeasure("FordFulkerson");
		}

		Node senke = graph.getNode("s");

		// wenn die initialisierung erfolgreich war (impliziert das es eine
		// quelle namens 'q' und Senke namens 's' gibt), dann erst kann weiter
		// gemacht werden
		if (initialisierung()) {

			// das ist falsch
			while (!markierteKnoten.containsAll(inspizierterKnoten)) {

				Node knotenI = getNextKnoten();

				inspiziere(knotenI);

				Node senkenVorgaenger = (Node) senke.getAttribute(attrNodeVorgaenger);

				// wenn die Senke markiert ist
				if (senkenVorgaenger != null) {
					vergroessereWeg();

					if (edmondsUndKarp) {
						breitensuche();
					}
				}

			}

		}

	}

	private void vergroessereWeg() {

		String methodenname = "vergroessereWeg()";

		Node quelle = graph.getNode("q");
		Node senke = graph.getNode("s");
		Double deltaSenke = (Double) senke.getAttribute(attrNodeDelta);

		Node knotenMomentan;
		Node knotenMomentanNachfolger = senke;
		do {
			knotenMomentan = (Node) knotenMomentanNachfolger.getAttribute(attrNodeVorgaenger);
			Edge kanteMomentan = knotenMomentanNachfolger.getEdgeBetween(knotenMomentan);

			// ist eine kante die in richtung senke gerichtet ist
			if (kanteMomentan.getSourceNode().equals(kanteMomentan)) {
				kanteMomentan.setAttribute(attrEdgeFluss, deltaSenke);
			} else {
				// kannte ist in richtung quelle gerichtet

				Double kantenFluss = fluss(kanteMomentan);

				kanteMomentan.setAttribute(attrEdgeFluss, (kantenFluss - deltaSenke));

			}
			knotenMomentanNachfolger = knotenMomentan;

		} while (!knotenMomentan.equals(quelle));

		senke.setAttribute(attrNodeVorgaenger, null);

		markierteKnoten.clear();
		markierteKnoten.add(quelle);
		btsQueue.clear();
		inspizierterKnoten.clear();

	}

	private Node getNextKnoten() {

		if (edmondsUndKarp) {
			return btsQueue.poll();
		}

		ArrayList<Node> temp = new ArrayList<Node>();

		temp.addAll(markierteKnoten);
		temp.removeAll(inspizierterKnoten);

		int randomNum = ThreadLocalRandom.current().nextInt(0, temp.size() + 1);

		return temp.get(randomNum);

	}

	private void inspiziere(Node knotenI) {

		for (Edge kante : knotenI.getEachEdge()) {
			Node knotenJ = kante.getOpposite(knotenI);
			if (markierteKnoten.contains(knotenJ)) {
				continue;
			}

			Double deltaI = (Double) knotenI.getAttribute(attrNodeDelta);
			Double deltaJ = (Double) knotenJ.getAttribute(attrNodeDelta);
			Double fluss = Math.min(deltaJ, (kapazitaet(kante) - fluss(kante)));

			if (kante.getSourceNode().equals(knotenI) && (!satoriert(kante))) {
				// dann ist die Kante eine ausgehende Kante

				knotenJ.setAttribute(attrNodeVorgaenger, knotenI);

				knotenJ.setAttribute(attrEdgeFluss, fluss);

				markierteKnoten.add(knotenJ);

			} else if (kante.getSourceNode().equals(knotenJ) && (fluss(kante) > 0)) {
				// Kante ist eine eingehende Kante
				knotenJ.setAttribute(attrNodeVorgaenger, knotenI);
				knotenJ.setAttribute(attrEdgeFluss, fluss);
				markierteKnoten.add(knotenJ);
			}

		}
		inspizierterKnoten.add(knotenI);

	}

	private Double fluss(Edge kante) {

		return (Double) kante.getAttribute(attrEdgeFluss);
	}

	private Double kapazitaet(Edge kante) {
		return (Double) kante.getAttribute(attrEdgeGewicht);

	}

	private boolean satoriert(Edge kante) {

		Double fluss = (Double) kante.getAttribute(attrEdgeFluss);
		Double kapazitaet = (Double) kante.getAttribute(attrEdgeGewicht);

		if ((kapazitaet - fluss) < doubleEpsilon) {
			return true;
		}

		return false;
	}

	private void breitensuche() {
		// initialisierung

		Node quelle = graph.getNode("q");
		Node senke = graph.getNode("s");

		for (Node knoten : graph.getEachNode()) {

			knoten.setAttribute(attNodeBTSMarkiert, false);
			knoten.setAttribute(attNodeBTSGewicht, 0);
			knoten.setAttribute(attNodeBTSvorgaenger, null);

		}
		quelle.setAttribute(attNodeBTSMarkiert, true);

		// eigentliche BTS
		Queue<Node> btsTemp = new LinkedList<Node>();

		btsTemp.add(quelle);

		while (!btsTemp.isEmpty()) {

			Node tempKnoten = btsTemp.poll();
			int tempKnotenGewicht = (int) tempKnoten.getAttribute(attNodeBTSGewicht);

			for (Edge kante : tempKnoten.getEachEdge()) {
				// wenn die kannte voll ist, oder die Quelle der Kante nicht der
				// momentane Knoten ist
				if (satoriert(kante) || kante.getTargetNode().equals(tempKnoten)) {
					continue;
				}

				Node tempNachbar = kante.getOpposite(tempKnoten);
				boolean markiert = (boolean) tempNachbar.getAttribute(attNodeBTSMarkiert);

				if (!markiert) {

					int gewichtNachbar = (int) tempNachbar.getAttribute(attNodeBTSGewicht);
					if ((tempKnotenGewicht + 1) < gewichtNachbar) {

						tempNachbar.setAttribute(attNodeBTSvorgaenger, tempKnoten);
						tempNachbar.setAttribute(attNodeBTSGewicht, tempKnotenGewicht + 1);

					}
					btsTemp.add(tempNachbar);

				}

			}
			tempKnoten.setAttribute(attNodeBTSMarkiert, true);
		}
		// ab hier ist das errechnen der wenigsten hops beendet und es kann der
		// kürzeste weg berechnet werden

		Node aktuellFuerKuerzestenWeg = senke;
		ArrayList<Node> weg = new ArrayList<Node>();
		while (!aktuellFuerKuerzestenWeg.equals(quelle)) {

			weg.add(aktuellFuerKuerzestenWeg);
			aktuellFuerKuerzestenWeg = (Node) aktuellFuerKuerzestenWeg.getAttribute(attNodeBTSvorgaenger);
		}

		for (int i = weg.size() - 1; i >= 0; i--) {
			System.out.println("kuerzester weg ist: "+weg.get(i).getId());
			btsQueue.add(weg.get(i));
		}

	}
}
