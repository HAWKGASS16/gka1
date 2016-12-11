package algorithmen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import application.GraphController;

public class FordFulkerson {

	private Graph graph;
	private final String attrEdgeFluss = "FordfulkersonKantenFluss";
	private final String attrNodeFluss = "FordFulkersonFluss";
	private final String attrEdgeGewicht = GraphController.EdgeAttributeWeight;
	private final String attrNodeVorgaenger = "FordFulkersonVorgaenger";
	private final String attrNodeInspiziert = "FordFulkersonInspiziert";

	private final String attrNodeMarkierung = "FordFulkersonMarkiert";
	private HashSet<Node> markierteKnoten = new HashSet<Node>();
	private HashSet<Node> inspizierterKnoten = new HashSet<Node>();
	
	private double maximalerFluss=0;

	/**
	 * Bestimmt den maximalen Fluss in einem Graphen. Vorraussetzungen: es gibt
	 * einen Knoten namens "q" es gibt einen Knoten namens "s" Es gibt einen web
	 * zwischen "q" und "s"
	 * 
	 * @param prahp
	 */
	public FordFulkerson(Graph prahp) {

		graph = prahp;
	}

	/**
	 * Initialisiert den Graphen mit dem leeren Fluss, auf dem weiter operiert
	 * werden kann. In Diesem Fall ist der Initialfluss 0.An den Knoten kommt
	 * also auch en Fluss von 0 an und es gibt keinen Vorgänger.
	 */
	private void initialisierung() {

		convertGraphToNetwork();
		
		for (Iterator iterator = graph.getEdgeIterator(); iterator.hasNext();) {
			Edge kante = (Edge) iterator.next();

			// der anfangsfluss ist 0
			kante.setAttribute(attrEdgeFluss, 0.0);
			kantenLabel(kante, 0.0);

		}

		Object[] object = { "pos", null, Double.POSITIVE_INFINITY };

		for (Iterator iterator = graph.getNodeIterator(); iterator.hasNext();) {
			Node knoten = (Node) iterator.next();

			// der vorgänger des Knotens ist zunächst undefiniert
			knoten.addAttribute(attrNodeVorgaenger, object);

			// der moemantane fluss des Knotens ist +unendlich
			knoten.addAttribute(attrNodeFluss, Double.POSITIVE_INFINITY);

			// die Knoten sind initial nicht markiert
			knoten.addAttribute(attrNodeMarkierung, false);

			knoten.addAttribute(attrNodeInspiziert, false);

		}

		Node q = graph.getNode("q");

		q.setAttribute(attrNodeMarkierung, true);

		q.setAttribute(attrNodeVorgaenger, object);
		q.setAttribute(attrNodeFluss, Double.POSITIVE_INFINITY);

		markierteKnoten.add(q);

	}

	private void convertGraphToNetwork() {
		
		ArrayList<Edge> zuloeschen = new ArrayList<Edge>();
		
		
		
		for (Iterator alleKanten=graph.getEdgeIterator();alleKanten.hasNext();) {
			
			Edge kante = (Edge) alleKanten.next();
			
			if(!kante.isDirected()){
				
				Node node1 = kante.getNode0();
				Node node2 = kante.getOpposite(node1);
				
				Double gewicht = (Double)kante.getAttribute(attrEdgeGewicht);
				
				int nummer = getEdgeNummer(node1.getId(), node2.getId());
				
				graph.addEdge(node1.getId()+node2.getId()+nummer, node1, node2, true);
				Edge neueKante1 = graph.getEdge(node1.getId()+node2.getId()+nummer);
				neueKante1.setAttribute(attrEdgeGewicht, (gewicht/2));
				
				
				nummer = getEdgeNummer(node2.getId(), node1.getId());
				graph.addEdge(node2.getId()+node1.getId()+nummer, node2, node1, true);
				Edge neueKante2 = graph.getEdge(node2.getId()+node1.getId()+nummer);
				neueKante2.setAttribute(attrEdgeGewicht, (gewicht/2));
				
				zuloeschen.add(kante);
				
//				graph.removeEdge(kante);
				
				
				
			}
			
			
		}
		for (Iterator iterator = zuloeschen.iterator(); iterator.hasNext();) {
			Edge edge = (Edge) iterator.next();
			
			graph.removeEdge(edge);
			
		}
		
	}

	private int getEdgeNummer(String node1, String node2) {
		Edge kante=graph.getEdge(node1+node2);
		
		int i=0;
		while(kante!=null){
			i++;
			kante=graph.getEdge(node1+node2+i);
			
			
		}
		
		return i;
	}

	private void kantenLabel(Edge kante, Double fluss) {

		Double kantengewicht = (Double) kante.getAttribute(attrEdgeGewicht);
		kante.setAttribute("ui.label", "" + fluss + " / " + kantengewicht);

	}

	public void maxflow() {

		System.out.println("starte initialisierung");

		initialisierung();

		System.out.println("initialisierung beendet");

		// wähle einen beliebigen aber noch nicht inspizierten Knoten

		/*
		 * do
		 * 
		 * inspiziere(markierten aber nicht inspizierten Knoten)
		 * 
		 * 
		 * wenn die Senke markiert ist, vergößere den fluss else gehe zu
		 * inspiziere();
		 * 
		 * wenn alle Knoten inspiziert wurden, break
		 * 
		 * 
		 * 
		 */
		// while(esGibtErweiterndenWeg()){
		//
		// //erhöhe f längs des erweiternden weges
		//
		// }

		Node q = graph.getNode("q");
		Node s = graph.getNode("s");

		do {

			Node knotenMomentan = getNextNode();

			if (knotenMomentan != null) {
				System.out.println("\tuntersuche knoten: " + knotenMomentan.getId());

				inspiziere(knotenMomentan);

				if (!alleSindInspiziert()) {

					System.out.println("\t\tes sicht nicht alle knoten ispiziert");

					if (senkeIstMarkiert()) {

						System.out.println("\t\tsenke ist markiert");
						// der weg wird vergrößert und dann wird ein neuer
						// vergrößernder weg gesucht
						vergroessereWeg();

					}

				} else {
					// es gibt keinen vergrößernden weg. Der maximale Fluss ist
					// der
					// der an der Senke ankommt
					break;
				}
			}else{
				break;
			}

		} while (true);

		System.out.println("der maximalse Fluss ist: " + maximalerFluss);

	}

	/**
	 * Von der Senke angefangen wird der weg bis zur quelle durchgelaufen und
	 * auf dem weg wird auf jede Kante der Fluss des "mitgeschleiften" deltas
	 * gelegt. Da durch das "mitschleifen" des geringsten deltas immer
	 * gewährleistet ist, das nie die Kapazität über- (oder bei Rückwärtskanten)
	 * unterschritten wird.
	 */
	private void vergroessereWeg() {
		
System.out.println("vergroessere weg beginnt");
		
		Node senke = graph.getNode("s");
		Node quelle = graph.getNode("q");

		Object[] attributeSenke = (Object[]) senke.getAttribute(attrNodeVorgaenger);
		Double deltaSenke = (Double) attributeSenke[2];

		maximalerFluss+=deltaSenke;
		
		Node momentanNode = senke;
		Node vorgaenger;
		while (!momentanNode.equals(quelle)) {

			
			Object[] temp = (Object[]) momentanNode.getAttribute(attrNodeVorgaenger);
			vorgaenger = (Node) temp[1];
			String vorzeichen = (String) temp[0];

			
System.out.println("\tmomentanNode: "+momentanNode.getId());
System.out.println("\tvorgaenger: "+vorgaenger.getId());
			// die kante zwischen dem momentanen Knoten und seinem Vorgänger
			Edge kanteZwischenMomentanUndVorgaenger = momentanNode.getEdgeBetween(vorgaenger);

			/*
			 * wenn das Vorzeichen negativ ist (es handelt sich um eine
			 * rückwärtskante) dann wird der negative wert des deltas auf zu dem
			 * aktuellen Fluss addiert
			 */
			addiereKantenfluss(kanteZwischenMomentanUndVorgaenger, deltaSenke, vorzeichen);

			// entfernt die markierung
			momentanNode.setAttribute(attrNodeMarkierung, false);

			momentanNode = vorgaenger;

		}
		System.out.println("vergroessere weg beendet");
	}

	private void addiereKantenfluss(Edge kante, Double delta, String vorzeichen) {

		Double kantenfluss = (Double) kante.getAttribute(attrEdgeFluss);

		Double neuesDelta = delta;
		if (vorzeichen.equals("neg")) {
			neuesDelta = neuesDelta * (-1);
		}

		kante.setAttribute(attrEdgeFluss, kantenfluss + neuesDelta);
		kantenLabel(kante, kantenfluss + neuesDelta);

	}

	/**
	 * gibt den fluss des Knotens zurück (das Delta)
	 * 
	 * @param s
	 * @return
	 */
	private Double getFluss(Node s) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean alleSindInspiziert() {

		Node quelle = graph.getNode("q");
		Node senke = graph.getNode("s");

		// Iterator alleKnoten = graph.getNodeIterator();

		for (Node node : graph) {

//			if (node.equals(quelle) || node.equals(senke)) {
//				continue;
//			}

			boolean istMarkiert = (boolean) node.getAttribute(attrNodeMarkierung);
			boolean istInspiziert = (boolean) node.getAttribute(attrNodeInspiziert);

			// wenn ein knoten markiert UND inspiziert ist, ist seine
			// untersuchung auf maximalen fluss beendet.
			// wenn dies NICHT der fall ist, muss weiterhin nach einem maximalen
			// fluss gesucht werden
			if (!(istMarkiert && istInspiziert)) {
				return false;
			}

		}

		return true;
	}

	private boolean senkeIstMarkiert() {
		boolean sIstMarkiert = (boolean) graph.getNode("s").getAttribute(attrNodeMarkierung);
		return sIstMarkiert;
	}

	private Double getF(Edge kante) {

		// Double fluss = (Double)kante.getAttribute(attrEdgeFluss);

		return (Double) kante.getAttribute(attrEdgeFluss);

	}

	private Double getC(Edge kante) {
		return (Double) kante.getAttribute(attrEdgeGewicht);
	}

	private boolean isRueckwaertsKante(String naeherAnQuelle, String naeherAnSenke) {

		Node node1 = graph.getNode(naeherAnQuelle);
		Node node2 = graph.getNode(naeherAnSenke);

		Edge kante = node2.getEdgeBetween(node1);

		if (kante.getSourceNode().equals(node1)) {
			return false;
		} else {
			return true;
		}

	}

	private Node getNextNode() {
		Node temp = null;
		for (Iterator iterator = graph.getNodeIterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();

			boolean markiert = (boolean) node.getAttribute(attrNodeMarkierung);
			boolean inspiziert = (boolean) node.getAttribute(attrNodeInspiziert);

			if (markiert && (!inspiziert)) {

				temp = node;
				break;
			}

		}
		return temp;

	}

	private void inspiziere(Node knoten) {

		Node vj;

		for (Edge edge : knoten) {

			vj = edge.getOpposite(knoten);
			boolean vjMarkierung = (boolean) vj.getAttribute(attrNodeMarkierung);

			// Wenn der Knoten vj nicht markiert ist, wird er markiert. Wenn er
			// schon markiert ist, wird er nicht weiter beachtet
			if (!vjMarkierung) {

				if (!isRueckwaertsKante(knoten.getId(), vj.getId())) {

					double kantenfluss = getF(edge);
					double kantenKapazitaet = getC(edge);

					if (kantenfluss < kantenKapazitaet) {
						Double knotenflussVi = getF(knoten);
						Double minimum = Math.min((kantenKapazitaet - kantenfluss), knotenflussVi);

						Object[] vorgaenger = new Object[3];

						vorgaenger[0] = "pos";
						vorgaenger[1] = knoten;
						vorgaenger[2] = minimum;

						markiere(vj, vorgaenger);

					}

				} else {
					// die kante ist eine Rückwärtskante
					Double kantenfluss = getF(edge);

					if (kantenfluss > 0.0) {

						Double knotenflussVi = getF(knoten);
						Double minimum = Math.min(kantenfluss, knotenflussVi);

						Object[] vorgaenger = new Object[3];

						vorgaenger[0] = "neg";
						vorgaenger[0] = knoten;
						vorgaenger[0] = minimum;

						markiere(vj, vorgaenger);

					}
				}

			}
		}

		knoten.setAttribute(attrNodeInspiziert, true);
		inspizierterKnoten.add(knoten);

	}

	private void markiere(Node vj, Object[] vorgaenger) {

		vj.setAttribute(attrNodeVorgaenger, vorgaenger);
		vj.setAttribute(attrNodeMarkierung, true);

	}

	private Double getF(Node knoten) {

		Object[] object = (Object[]) knoten.getAttribute(attrNodeVorgaenger);

		Double fluss = (Double) object[2];

		return fluss;
	}
	public Double getMaxFlow(){
		return maximalerFluss;
	}

}
