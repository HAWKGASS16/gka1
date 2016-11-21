package algorithmen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import application.GraphController;
import util.MeasureObject;

public class Dijkstra {
	
	private boolean bts = false;
	private MeasureObject zugriffsZaehler = new MeasureObject();
	private String startKnoten;
	private String endKnoten;
	private Graph graph;
	private ArrayList<Edge> shortestWay = new ArrayList<Edge>();
	
	
	public Dijkstra(Graph graph, String start, String end) {
		this.graph=graph;
		startKnoten=start;
		endKnoten=end;
	}
	
	protected Dijkstra(Graph graph, String start, String end, boolean btsSuche) {
		bts=btsSuche;
		this.graph=graph;
		startKnoten=start;
		endKnoten=end;
		
	}
	
	private void initialize(){
		
		
		
		zugriffsZaehler.read("initialisierung", 2);

		Node temp;
		Iterator<Node> nodeIterator = graph.getNodeIterator();
		while (nodeIterator.hasNext()) {
			temp = (Node) nodeIterator.next();

			temp.setAttribute(GraphController.NodeAttributdistance, Double.POSITIVE_INFINITY);
			temp.setAttribute("ui.class", "unmarked");
			temp.setAttribute(GraphController.NodeAttributVisited, false);

			zugriffsZaehler.write("initialisierung", 3);

		}

		Edge tempEdge;

		Iterator<Edge> edgeIterator = graph.getEdgeIterator();
		while (edgeIterator.hasNext()) {
			tempEdge = (Edge) edgeIterator.next();

			tempEdge.setAttribute("ui.class", "unmarked");

			// String edgeName = (String)
			// tempEdge.getAttribute(EdgeAttributeName);

			// das label ggf wieder entfernen
			// if(edgeName.equals("")||edgeName==null){
			//
			// tempEdge.setAttribute("ui.label", 1.0);
			// }else{
			//
			// }

			zugriffsZaehler.write("initialisierung", 1);
			zugriffsZaehler.read("initialisierung", 1);

		}
		
		
	}

	
	public boolean suche(){
		
		
		
		if(!bts){
			
			zugriffsZaehler.startMeasure("Dijkstra von " + startKnoten + " nach " + endKnoten);
			zugriffsZaehler.log("Knotenanzahl: " + graph.getNodeCount());
			zugriffsZaehler.log("Kantenanzahl: " + graph.getEdgeCount());
			
		}
		boolean erfolgreich = false;
		
		initialize();
		
		
		
		
		
		Node start = graph.getNode(startKnoten);
		Node ende = graph.getNode(endKnoten);
		
		if(start==null){
			return false;
		}
		if(ende == null){
			return false;
		}	
			
		
		
		start.setAttribute(GraphController.NodeAttributdistance, 0.0);
		LinkedList<Node> queue = new LinkedList<Node>();
		
		queue.add(start);
		Node tempNode;
		Edge tempEdge;

		// die Zugriffe auf den Graphen zählen
		zugriffsZaehler.read("Dijkstra", 2);
		zugriffsZaehler.write("Dijkstra", 1);

		while (!(queue.isEmpty())) {
			
			tempNode = getMinimumNode(queue);
//			log("dijstra---momentane tempNode: " + tempNode.getId());
//			log("dijstra---queue:" + queue.toString());

			tempNode.setAttribute(GraphController.NodeAttributVisited, true);
			tempNode.setAttribute("ui.class", "marked");
//			log("dijstra---der Knoten " + tempNode.getId() + " wird als besucht markiert");

			zugriffsZaehler.read("Dijkstra", 2);
			zugriffsZaehler.write("Dijkstra", 2);

			// Iteriert über alle Kanten die an dem Knoten anliegen, der
			// momentan untersucht wird.
			Iterator<?> edgeIterator = tempNode.getEdgeIterator();
			while (edgeIterator.hasNext()) {

				zugriffsZaehler.read("Dijkstra", 4);

				tempEdge = (Edge) edgeIterator.next();

				distanzUpdate(tempNode, tempEdge);

				Node tempNachbar = tempEdge.getOpposite(tempNode);
				boolean besucht = (boolean) tempNachbar.getAttribute(GraphController.NodeAttributVisited);

				boolean gerichteteKante = tempEdge.isDirected();

				if ((gerichteteKante && (tempEdge.getTargetNode() == tempNode))) {
					// continue;
				} else {
					if ((!besucht) && (!queue.contains(tempNachbar))) {
						// Wenn der gegenüberliegende Knoten noch nicht besucht
						// ist, und noch nicht
						// in der Queue, dann wird er zur queue hinzugefügt
						queue.add(tempNachbar);

//						log("dijstra---nachbar " + tempNachbar.getId() + " wird zur queue hinzugefuegt");

						zugriffsZaehler.write("Dijkstra", 1);

					}
				}

			}

			// wenn diese if-Bedingung aktiv ist, hoert der Algorithmus auf,
			// denn wenn der zuletzt gefundene Knoten der Zielknoten ist,
			// ist das schon der kürzeste weg da immer der aktuell kürzeste weg
			// untersucht wird.
//---------------------------------------------------------------------------den codeblock nochmal verbesser, da ist der fehler
			if (tempNode == ende) {
				erfolgreich = true;
				getShortestWay(graph.getNode(startKnoten), graph.getNode(endKnoten));
//				break;
			}

			


		}

		if(erfolgreich){
//			logShortestWay(start, getShortestWay(start, ende));
		}


		if (!bts) {
			zugriffsZaehler.stopMeasure();
		}
		
		return erfolgreich;
		
		
	}

	private void distanzUpdate(Node knoten, Edge kante) {
		zugriffsZaehler.read("distanzUpdate()", 1);
		

		// die Variable "nachbar" wird mit dem übergebenen knoten initialisiert
		// einfach nur damit sie initialisiert ist^^
		Node nachbar;

		Double edgeGewicht;

		if (bts) {
			edgeGewicht = 1.0;
		} else {
			edgeGewicht = (Double) kante.getAttribute(GraphController.EdgeAttributeWeight);
			zugriffsZaehler.read("distanzUpdate()", 1);
		}
		Double knotenGewicht = (Double) knoten.getAttribute(GraphController.NodeAttributdistance);

		
		
		Double distanz = knotenGewicht + edgeGewicht;

		if (kante.isDirected()) {
			if (kante.getSourceNode() == knoten) {
				nachbar = kante.getTargetNode();
			} else {
				// Wenn der übergebene Knoten NICHT die Quelle einer gerichteten
				// Kante ist
				// wird die Distanz natürlich nicht angepasst und deshalb
				// returnt
				return;
			}
		} else {
			nachbar = kante.getOpposite(knoten);
			zugriffsZaehler.read("distanzUpdate()", 1);
		}

		zugriffsZaehler.read("distanzUpdate()", 1);
		zugriffsZaehler.write("distanzUpdate()", 1);

		Double nachbargewicht = nachbar.getAttribute(GraphController.NodeAttributdistance);
		Double minGewicht = Math.min(distanz, nachbargewicht);
		// log("dijstra-distanzUpdate-- nachbargewicht: "+nachbargewicht);
		nachbar.addAttribute(GraphController.NodeAttributdistance, minGewicht);
		addNodeWeightLabel(nachbar, minGewicht);

		
	}

	private void addNodeWeightLabel(Node node, Double weight) {
		String nodeName = node.getId();
		node.addAttribute("ui.label", nodeName + " : " + weight);
		
	}

	private Node getMinimumNode(LinkedList<Node> queue) {
		zugriffsZaehler.read("getMinimumNode()", 4);
		zugriffsZaehler.write("getMinimumNode()", 1);

		Node minimalNode = queue.getFirst();
		Node temp = null;
		// iteriert über alle Einträge der queue und merkt sich
		// immer das kleinste Element also den Knoten mit der geringsten
		// entfernung
		Iterator<Node> queueIterator = queue.iterator();
		while (queueIterator.hasNext()) {

			zugriffsZaehler.read("getMinimumNode()", 4);

			temp = (Node) queueIterator.next();
//			log("dijstra-getMini-- iteriere ueber die queue mit dem aktuellen knoten: " + temp.getId());
			Double minimalNodeDistance = (Double) minimalNode.getAttribute(GraphController.NodeAttributdistance);
			Double tempNodeDistance = (Double) temp.getAttribute(GraphController.NodeAttributdistance);
			if (tempNodeDistance < minimalNodeDistance) {
				minimalNode = temp;
			}
		}

//		log("dijstra-getMini-- es wird der Knoten " + minimalNode.getId() + " aus der queue entfernt");
		queue.remove(minimalNode);
		return minimalNode;
	}
	private void markEdgeVisual(Edge tempEdge) {

		tempEdge.getTargetNode().addAttribute("ui.class", "shortest");
		tempEdge.addAttribute("ui.class", "shortest");
		tempEdge.getSourceNode().addAttribute("ui.class", "shortest");

	}
	private ArrayList<Edge> getShortestWay(Node startKnoten, Node endknoten) {

//		ArrayList<Edge> shortestWay = new ArrayList<Edge>();

		Node tempNode = endknoten;

		while (tempNode != startKnoten) {

			// temporäre liste aller edges aus denen das Minumum - also der
			// nächst kürzeste Knoten - extrahiert wird
			ArrayList<Edge> tempList = new ArrayList<Edge>();
			Edge tempEdge;

			zugriffsZaehler.read("getShortestWay()", 1);

			Iterator<?> edgeIterator = tempNode.getEdgeIterator();
			while (edgeIterator.hasNext()) {
				tempEdge = (Edge) edgeIterator.next();
				zugriffsZaehler.read("getShortestWay()", 2);

				if (tempEdge.isDirected()) {

					if (tempEdge.getTargetNode() == tempNode) {
						zugriffsZaehler.read("getShortestWay()", 1);
						tempList.add(tempEdge);
					}

				} else {
					tempList.add(tempEdge);
				}

			}
			tempEdge = getMinimumEdge(tempNode, tempList);
			
			if(tempEdge==null){
				
				return null;
				
			}
			
			shortestWay.add(tempEdge);
			markEdgeVisual(tempEdge);
			tempNode = tempEdge.getOpposite(tempNode);
		}

//		this.shortestWay = shortestWay;
		return shortestWay;

	}
	private Edge getMinimumEdge(Node tempNode, ArrayList<Edge> tempList) {

		// tempNode ist der Knoten der näher am ziel liegt - also der der zum
		// Startpunkt guckt
		zugriffsZaehler.read("getMinimumEdge()", 2);

		Node vergleichsNode;
		Double vergleichsGewicht;
		Edge tempEdge;
		Edge minimumEdge=null;
		
		if(tempList.size()!=0){
			minimumEdge = tempList.get(0);
		}else{
			return null;
		}
		Double minimumWeight = (Double) minimumEdge.getOpposite(tempNode).getAttribute(GraphController.NodeAttributdistance);

		Iterator<Edge> listIterator = tempList.iterator();
		while (listIterator.hasNext()) {
			zugriffsZaehler.read("getMinimumEdge()", 3);
			tempEdge = (Edge) listIterator.next();

			vergleichsNode = tempEdge.getOpposite(tempNode);
			vergleichsGewicht = (Double) vergleichsNode.getAttribute(GraphController.NodeAttributdistance);

			if (vergleichsGewicht < minimumWeight) {
				minimumEdge = tempEdge;
				minimumWeight = vergleichsGewicht;

			}

		}

		return minimumEdge;
	}
	public Double getKosten(String start, String ziel){
		
		Node node2 = graph.getNode(ziel);
		
		if(node2==null){
			return null;
		}
		
		Double kosten = node2.getAttribute(GraphController.NodeAttributdistance);
		
		return kosten;
		
	}
	
}
