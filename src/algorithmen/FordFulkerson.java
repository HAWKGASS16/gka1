package algorithmen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import application.GraphController;
import util.MeasureObject;

public class FordFulkerson {

	private Graph graph;
	private final String attrEdgeFluss = "FordfulkersonKantenFluss";
	private final String attrNodeFluss = "FordFulkersonFluss";
	private final String attrEdgeGewicht = GraphController.EdgeAttributeWeight;
	private final String attrNodeVorgaenger = "FordFulkersonVorgaenger";
	private final String attrNodeInspiziert = "FordFulkersonInspiziert";

	private final String attNodeBTSMarkiert = GraphController.NodeAttributVisited;
	private final String attNodeBTSGewicht = "KnotenGewichtBTS";
	private final String attEdgeBTSgewicht = "BTSKantengewicht";

	private final String attrNodeMarkierung = "FordFulkersonMarkiert";

	private boolean fordFulkerson = true;

	private HashSet<Node> markierteKnoten = new HashSet<Node>();
	private HashSet<Node> inspizierterKnoten = new HashSet<Node>();

	private double maximalerFluss = 0;
	
	private MeasureObject messObjekt = new MeasureObject();

	/**
	 * Bestimmt den maximalen Fluss in einem Graphen. Vorraussetzungen: es gibt
	 * einen Knoten namens "q" es gibt einen Knoten namens "s" Es gibt einen web
	 * zwischen "q" und "s"
	 * 
	 * @param prahp
	 */
	public FordFulkerson(Graph prahp, boolean fordFulkersonOderNicht) {
		fordFulkerson=fordFulkersonOderNicht;
		graph = prahp;
		
	}

	/**
	 * Initialisiert den Graphen mit dem leeren Fluss, auf dem weiter operiert
	 * werden kann. In Diesem Fall ist der Initialfluss 0.An den Knoten kommt
	 * also auch en Fluss von 0 an und es gibt keinen Vorgänger.
	 */
	private void initialisierung() {
		String methodenname = "initialisierung()";

		//EXPERIMENTELL konvertiert ein ungerichtetes Netzwerk in ein gerichtetes Netzwerk
		convertGraphToNetwork();

		for (Iterator iterator = graph.getEdgeIterator(); iterator.hasNext();) {
			Edge kante = (Edge) iterator.next();

			// der anfangsfluss ist 0
			kante.setAttribute(attrEdgeFluss, 0.0);
			kantenLabel(kante, 0.0);
			
			messObjekt.read(methodenname, 1);
			messObjekt.write(methodenname, 1);

		}

		Object[] object = { "pos", null, Double.POSITIVE_INFINITY };

		for (Iterator iterator = graph.getNodeIterator(); iterator.hasNext();) {
			Node knoten = (Node) iterator.next();
			messObjekt.read(methodenname, 1);
			messObjekt.write(methodenname, 4);

			// der vorgänger des Knotens ist zunächst undefiniert
			knoten.addAttribute(attrNodeVorgaenger, object);

			// der moemantane fluss des Knotens ist +unendlich
			knoten.addAttribute(attrNodeFluss, Double.POSITIVE_INFINITY);

			// die Knoten sind initial nicht markiert
			knoten.addAttribute(attrNodeMarkierung, false);

			knoten.addAttribute(attrNodeInspiziert, false);

		}

		messObjekt.read(methodenname, 1);
		messObjekt.write(methodenname, 3);
		
		Node q = graph.getNode("q");
System.out.println("q ist vorhanden: "+(q==null));
		q.setAttribute(attrNodeMarkierung, true);

		q.setAttribute(attrNodeVorgaenger, object);
		q.setAttribute(attrNodeFluss, Double.POSITIVE_INFINITY);

		markierteKnoten.add(q);

	}

	private void initialisiereBTS() {
		
//System.out.println("initialoisiere BTS");
		Object[] object = { "pos", null, Double.POSITIVE_INFINITY };
		for (Iterator nodeIterator = graph.getNodeIterator(); nodeIterator.hasNext();) {
			// jede entfernung wird auf 0 gesetzt
			Node knoten = (Node) nodeIterator.next();

			knoten.setAttribute(attrNodeVorgaenger, object);
			knoten.setAttribute(attNodeBTSMarkiert, false);
			knoten.setAttribute(attrNodeMarkierung, false);
			knoten.setAttribute(attNodeBTSGewicht, Double.POSITIVE_INFINITY);

		}

		for (Iterator edgeIterator = graph.getEdgeIterator(); edgeIterator.hasNext();) {
			// jedes kantengewicht wird auf 0 gesetzt
			Edge kante = (Edge) edgeIterator.next();

			kante.setAttribute(attEdgeBTSgewicht, 0);
			kante.setAttribute(attrEdgeFluss, 0.0);

		}
		graph.getNode("q").addAttribute(attNodeBTSGewicht, 0.0);

	}

	private void convertGraphToNetwork() {

		ArrayList<Edge> zuloeschen = new ArrayList<Edge>();

		for (Iterator alleKanten = graph.getEdgeIterator(); alleKanten.hasNext();) {

			messObjekt.read("ConvertGraphtoNetwork()", 1);
			
			Edge kante = (Edge) alleKanten.next();

			if (!kante.isDirected()) {

				messObjekt.read("ConvertGraphtoNetwork()", 5);
				messObjekt.write("ConvertGraphtoNetwork()", 2);
				
				Node node1 = kante.getNode0();
				Node node2 = kante.getOpposite(node1);

				Double gewicht = (Double) kante.getAttribute(attrEdgeGewicht);

				// die nummer wird an den Kantennamen angefügt
				int nummer = getEdgeNummer(node1.getId(), node2.getId());

				graph.addEdge(node1.getId() + node2.getId() + nummer, node1, node2, true);
				Edge neueKante1 = graph.getEdge(node1.getId() + node2.getId() + nummer);
				neueKante1.setAttribute(attrEdgeGewicht, (gewicht / 2));

				nummer = getEdgeNummer(node2.getId(), node1.getId());
				graph.addEdge(node2.getId() + node1.getId() + nummer, node2, node1, true);
				Edge neueKante2 = graph.getEdge(node2.getId() + node1.getId() + nummer);
				neueKante2.setAttribute(attrEdgeGewicht, (gewicht / 2));

				zuloeschen.add(kante);

				// graph.removeEdge(kante);

			}

		}
		for (Iterator iterator = zuloeschen.iterator(); iterator.hasNext();) {
			Edge edge = (Edge) iterator.next();

			graph.removeEdge(edge);
			
			messObjekt.read("ConvertGraphToNetwork", 1);

		}

	}

	private int getEdgeNummer(String node1, String node2) {
		Edge kante = graph.getEdge(node1 + node2);
		messObjekt.read("getEdgeNummer()", 1);

		int i = 0;
		while (kante != null) {
			i++;
			kante = graph.getEdge(node1 + node2 + i);
			messObjekt.read("getEdgeNummer()", 1);

		}

		return i;
	}

	private void kantenLabel(Edge kante, Double fluss) {

		messObjekt.read("kantenLabel()", 1);
		messObjekt.write("kantenLabel()", 1);
		
		Double kantengewicht = (Double) kante.getAttribute(attrEdgeGewicht);
		// Double kantengewicht = new
		// Double(kante.getAttribute(attrEdgeGewicht));
		kante.setAttribute("ui.label", "" + fluss + " / " + kantengewicht);

	}

	public void maxflow() {

//		System.out.println("starte initialisierung");

		

//		System.out.println("initialisierung beendet");

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

		if (fordFulkerson) {
			
			messObjekt.startMeasure("FordFulkerson von der Quelle zur Senke");
			initialisierung();
			
			do {
				
				
				
				

//				System.out.println(
//						"----------------------------------------------------------------------------------------------------");

				Node knotenMomentan = getNextNode();
				// do {
				// while (knotenMomentan != null) {
				//
				// inspiziere(knotenMomentan);
				//
				// knotenMomentan = getNextNode();
				// }
				// if (senkeIstMarkiert()) {
				// vergroessereWeg();
				// }
				// } while (knotenMomentan != null);

				if (knotenMomentan != null) {
//					System.out.println("\tuntersuche knoten: " + knotenMomentan.getId());

					inspiziere(knotenMomentan);

					
				} else {
					// es gibt keinen vergrößernden weg. Der maximale Fluss
					// ist
					// der
					// der an der Senke ankommt
					 
					break;
				}
				
				
				if (senkeIstMarkiert()) {

//					System.out.println("\t\tsenke ist markiert");
					// der weg wird vergrößert und dann wird ein neuer
					// vergrößernder weg gesucht
					vergroessereWeg();
					

				}


			} while (!alleSindInspiziert());
			
			messObjekt.stopMeasure();

		}else{
			//ab hier ist EdmondsKarp
			messObjekt.startMeasure("EdmondsKarp");
			initialisiereBTS();
		
			breitensuche();
			
			ArrayList<Edge> kuerzesterWeg;
			do{
			kuerzesterWeg= getBTSweg();
			
			for (int i= kuerzesterWeg.size()-1;i>=0;i--) {
				Edge edge = kuerzesterWeg.get(i);
				
//System.out.println("Kante von: "+edge.getSourceNode().getId()+" nach "+edge.getTargetNode().getId());

				markiereKnotenAnKante(edge.getNode0(), edge);
				
			}
			//hier resetten
			
			
			
			
			}while(kuerzesterWeg.size()>0);
			
			messObjekt.stopMeasure();
			
		}
//		System.out.println("der maximalse Fluss ist: " + maximalerFluss);
		// initialisiereBTS();
		// breitensuche();

	}

	/**
	 * Von der Senke angefangen wird der weg bis zur quelle durchgelaufen und
	 * auf dem weg wird auf jede Kante der Fluss des "mitgeschleiften" deltas
	 * gelegt. Da durch das "mitschleifen" des geringsten deltas immer
	 * gewährleistet ist, das nie die Kapazität über- (oder bei Rückwärtskanten)
	 * unterschritten wird.
	 */
	private void vergroessereWeg() {
		String methodenname = "vergroessereWeg()";
		messObjekt.read(methodenname, 3);
		messObjekt.write(methodenname, 1);

//		System.out.println("vergroessere weg beginnt");

		Node senke = graph.getNode("s");
		Node quelle = graph.getNode("q");

		Object[] attributeSenke = (Object[]) senke.getAttribute(attrNodeVorgaenger);
		Double deltaSenke = (Double) attributeSenke[2];

		maximalerFluss += deltaSenke;
//		System.out.println("Delta der Senke: " + deltaSenke);

		Node momentanNode = senke;
		Node vorgaenger;
		while (!momentanNode.equals(quelle)) {
			
			messObjekt.read(methodenname, 2);
			messObjekt.write(methodenname, 2);

			Object[] temp = (Object[]) momentanNode.getAttribute(attrNodeVorgaenger);
			vorgaenger = (Node) temp[1];
			String vorzeichen = (String) temp[0];

//			System.out.println("\tmomentanNode: " + momentanNode.getId());
//			System.out.println("\tvorgaenger: " + vorgaenger.getId());
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
			momentanNode.setAttribute(attrNodeInspiziert, false);

			momentanNode = vorgaenger;

		}

		for (Iterator nodeIterator = graph.getNodeIterator(); nodeIterator.hasNext();) {
			Node knoten = (Node) nodeIterator.next();
			
			messObjekt.read(methodenname, 1);
			messObjekt.write(methodenname, 2);

			knoten.setAttribute(attrNodeMarkierung, false);
			knoten.setAttribute(attrNodeInspiziert, false);

		}
		quelle.setAttribute(attrNodeMarkierung, true);

//		System.out.println("vergroessere weg beendet");
	}

	private void addiereKantenfluss(Edge kante, Double delta, String vorzeichen) {

		messObjekt.read("addiereKantenfluss()", 1);
		messObjekt.write("addiereKantenFluss()", 1);
		
		
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

		messObjekt.read("alleSindInspiziert()", 2);
		
		Node quelle = graph.getNode("q");
		Node senke = graph.getNode("s");

		// Iterator alleKnoten = graph.getNodeIterator();

		for (Node node : graph) {
			messObjekt.read("alleSindInspiziert()", 3);

			// if (node.equals(quelle) || node.equals(senke)) {
			// continue;
			// }

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
		messObjekt.read("senkeIstMarkiert()", 1);
		boolean sIstMarkiert = (boolean) graph.getNode("s").getAttribute(attrNodeMarkierung);
		return sIstMarkiert;
	}

	private Double getF(Edge kante) {

		// Double fluss = (Double)kante.getAttribute(attrEdgeFluss);

		messObjekt.read("getF(edge)", 1);
		
		return (Double) kante.getAttribute(attrEdgeFluss);

	}

	private Double getC(Edge kante) {
		messObjekt.read("getC(kante)", 1);
		return (Double) kante.getAttribute(attrEdgeGewicht);
	}

	private boolean isRueckwaertsKante(String naeherAnQuelle, String naeherAnSenke) {

		messObjekt.read("isRueckwaertsKante()", 4);
		
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
		
		String methodenname = "getNextNode()";
		
		
		
		// hier ein hashset verwenden..
		Node temp = null;
		for (Iterator iterator = graph.getNodeIterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();

			boolean markiert = (boolean) node.getAttribute(attrNodeMarkierung);
			boolean inspiziert = (boolean) node.getAttribute(attrNodeInspiziert);

			messObjekt.read(methodenname, 3);
			
			if (markiert && !inspiziert) {

				temp = node;
				break;
			}

		}

		// if(markierteKnoten.)
		// for(Iterator setIterator =
		// markierteKnoten.iterator();setIterator.hasNext();){
		//
		// }

		return temp;

	}

	private void inspiziere(Node knoten) {
		
		

		// Node vj;

//		System.out.println("inspiziere knoten " + knoten.getId());

		for (Edge edge : knoten) {
//			System.out.println("\tuntersuche kante von " + knoten.getId() + " nach " + edge.getOpposite(knoten).getId());
			markiereKnotenAnKante(knoten, edge);
			messObjekt.read("inspiziere()", 1);

		}

		messObjekt.write("inspiziere()", 1);
		knoten.setAttribute(attrNodeInspiziert, true);
		inspizierterKnoten.add(knoten);

	}

	private void markiereKnotenAnKante(Node knoten, Edge edge) {
		String methodenname = "markiereKnotenAnKante()";
		Node vj = edge.getOpposite(knoten);
		boolean vjMarkiert = (boolean) vj.getAttribute(attrNodeMarkierung);
		
		messObjekt.read(methodenname, 1);

		// Wenn der Knoten vj nicht inspiziert ist, wird er markiert. Wenn er
		// schon markiert ist, wird er nicht weiter beachtet
		if (!vjMarkiert) {
//			System.out.println("der Nachbar " + vj.getId() + " von " + knoten.getId());
			if (!isRueckwaertsKante(knoten.getId(), vj.getId())) {
//				System.out.println("ist eine keine Rückwärtskante");
				double kantenfluss = getF(edge);
				double kantenKapazitaet = getC(edge);

				if (kantenfluss < kantenKapazitaet) {
					Double knotenflussVi = getF(knoten);
					Double delta = Math.min((kantenKapazitaet - kantenfluss), knotenflussVi);

//					System.out.println("Kantenfluss ist kleiner als die kantenkapazitaet");
//					System.out.println("also wird " + vj.getId() + " mit dem neuen Vorgaenger markiert");

					Object[] vorgaenger = new Object[3];

					vorgaenger[0] = "pos";
					vorgaenger[1] = knoten;
					vorgaenger[2] = delta;

					markiere(vj, vorgaenger);

				}

			} else {
				// die kante ist eine Rückwärtskante
				Double kantenfluss = getF(edge);

				if (kantenfluss > 0.0) {

					Double knotenflussVi = getF(knoten);
					System.out.println("knotenflussVi "+knotenflussVi);
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

	private void inspiziereEdmondsKarp(Node knoten) {

		Node vj;

		// um einen weg zu bauen, wird eine breitensuche gemacht. der weg mit
		// den

		breitensuche();
		// dann den kürzesten weg als liste geben lassen. die liste enthält alle
		// kanten des weges
		ArrayList<Edge> einWeg;
		do{
		einWeg = getBTSweg();

		// dann für jede kante (bei q beginnend) markiereKnotenAnKante()
		// aufrufen bis nach s
		
		
		
		for (int i= einWeg.size()-1;i>=0;i--) {
			Edge edge = einWeg.get(i);
			
//System.out.println("Kante von: "+edge.getSourceNode().getId()+" nach "+edge.getTargetNode().getId());

			markiereKnotenAnKante(edge.getNode0(), edge);
			
		}
		}while(einWeg.size()>0);
		

	}

	private ArrayList<Edge> getBTSweg() {
		Node s = graph.getNode("s");
		Node q = graph.getNode("q");

//		System.out.println("\tGetBTSWEG");
		
		Edge kanteZuVorgaenger;// = getBTSVorgaenger(s);
		Node aktuell = s;

		ArrayList<Edge> wegListe = new ArrayList<Edge>();
		ArrayList<Node> besuchteKnoten = new ArrayList<Node>();

		while (!aktuell.equals(q)) {
System.out.println("aktuell: "+aktuell.getId());
//			besuchteKnoten.add(aktuell);
			kanteZuVorgaenger = getBTSVorgaenger(aktuell,wegListe);

//			Node vorgaenger = kanteZuVorgaenger.getSourceNode();
			Node vorgaenger = kanteZuVorgaenger.getOpposite(aktuell);
System.out.println("vorgaenger: "+vorgaenger.getId());			
			wegListe.add(kanteZuVorgaenger);
System.out.println("liste des weges laenge: "+wegListe.size());			
			aktuell=vorgaenger;

		}

		return wegListe;
	}

	/**
	 * liefert den vorgänger des übergebenen Knotens. die Kante darf nich voll
	 * sein ( f(eij) < c(eij) )
	 * 
	 * @param s
	 * @return
	 */
	private Edge getBTSVorgaenger(Node s, ArrayList weg) {
System.out.println("\t\tgetBTSVorgaenger mit knoten "+s.getId());
		Node vorgaenger = null;
		Double vorgaengerGewicht = Double.POSITIVE_INFINITY;

		Node minimum = null;
		Double minimumGewicht = Double.POSITIVE_INFINITY;
		
		Edge minimalKante=null;
		
		Double minimalerKantenFluss = null;

		for (Edge kante : s) {

//			vorgaenger = kante.getSourceNode();
			vorgaenger = kante.getOpposite(s);
			vorgaengerGewicht = (Double) vorgaenger.getAttribute(attNodeBTSGewicht);
//			minimalKante=kante;
			Double kantenfluss = getF(kante);

			if(weg.contains(kante)){
				System.out.println("\tkante ist schon im weg enthalten");
				continue;
				
			}
			
			
			//wenn die kante noch nicht voll ist UND die quelle der Kante auch der Vorgaenger ist
			if (!kanteIsFull(kante)) {
				System.out.println("\tkante ist nicht voll");
				if (vorgaengerGewicht < minimumGewicht) {

					
					if(minimalKante!=null && (kantenfluss<getF(minimalKante))){
	System.out.println("Die kannte ist nicht null UND der kluss ist kleiner als die minimalKante");
					}
					
					minimum = vorgaenger;
					minimumGewicht = vorgaengerGewicht;
					minimalKante = kante;

				}
				if(minimum==null){
					minimum=vorgaenger;
					minimalKante=kante;
				}

			}

		}
System.out.println("kleinster vorgaenger ist: "+minimum.getId());
		return minimalKante;
	}

	private void breitensuche() {

//		System.out.println("initialisiere Breitensuche");
		Node q = graph.getNode("q");

		LinkedList<Node> suchSchlange = new LinkedList<Node>();
		suchSchlange.add(q);

		while (!suchSchlange.isEmpty()) {

			Node tempNode = suchSchlange.poll();

			for (Edge edge : tempNode) {

				Node gegenueber = edge.getOpposite(tempNode);

				if (!isRueckwaertsKante(tempNode.getId(), gegenueber.getId())) {

					Double kostenAktuell = (Double) gegenueber.getAttribute(attNodeBTSGewicht);
					Double kostenBerechnet = (Double) tempNode.getAttribute(attNodeBTSGewicht) + 1;

					// distanzupdate
					gegenueber.setAttribute(attNodeBTSGewicht, Math.min(kostenBerechnet, kostenAktuell));

					boolean gegenueberIstMarkiert = (boolean) gegenueber.getAttribute(attNodeBTSMarkiert);

					// Wenn der Knoten noch NICHT besucht wurde UND er noch
					// nicht in der schlange ist,
					// dann wird er hinzugefügt
					if (!gegenueberIstMarkiert && !suchSchlange.contains(gegenueber)) {
						suchSchlange.add(gegenueber);
					}

				}

			}

			tempNode.setAttribute(attNodeBTSMarkiert, true);

		}

	}

	private boolean kanteIsFull(Edge edge) {

		Double fluss = getF(edge);
		System.out.println("Fluss: "+fluss);
		Double kapazitaet = getC(edge);
		System.out.println("kapazitaet: "+kapazitaet);

		return !(fluss < kapazitaet);
	}

	private void markiere(Node vj, Object[] vorgaenger) {

		messObjekt.write("markiere()", 2);
		
		vj.setAttribute(attrNodeVorgaenger, vorgaenger);
		vj.setAttribute(attrNodeMarkierung, true);
		markierteKnoten.add(vj);

	}

	private Double getF(Node knoten) {
messObjekt.read("getF(knoten)", 1);
		
		Object[] object = (Object[]) knoten.getAttribute(attrNodeVorgaenger);

		Double fluss = (Double) object[2];

		return fluss;
	}

	public Double getMaxFlow() {
		return maximalerFluss;
	}

}
