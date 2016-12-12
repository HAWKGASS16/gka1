package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import algorithmen.Dijkstra;
import application.GraphController;

public class BigNetGraph {

	private Graph graph;
	private Integer anzahlNodes;
	private Integer anzahlEdges;
	private Integer maxAnzahlEdges;
	private Integer maxKantenGewicht = 100;
	private List<Node> sourceList = new ArrayList<Node>();
	private List<Node> sinkList = new ArrayList<Node>();
	private List<Edge> edgeList = new ArrayList<Edge>();

	public BigNetGraph(Graph graph, Integer nodes, Integer edges) {

		this.graph = graph;
		anzahlNodes = nodes;
		anzahlEdges = edges;
		maxAnzahlEdges = anzahlNodes * (anzahlNodes - 1);

		if (anzahlEdges < 3 || anzahlEdges < 0 || anzahlEdges < (anzahlEdges - 1)) {
			throw new IllegalArgumentException("Geben Sie gültige Werte ein.");

		}
		if (anzahlEdges > maxAnzahlEdges) {
			throw new IllegalArgumentException(
					"Die Anzahl der Kanten muss kleiner-gleich " + (maxAnzahlEdges) + " sein");
		}
		graph.addAttribute(GraphController.GraphAttributeDirected, true);

	}

	public void generateBigNet() {

	
//		System.out.println("anzahlEdges: " + anzahlEdges);
		anzahlEdges = Math.min(((anzahlNodes - 2) * (anzahlNodes - 3)), anzahlEdges);
//		System.out.println("anzahlEdges: " + anzahlEdges);

		for (int i = 1; i < anzahlNodes - 1; i++) {

//			System.out.println("füge Node hinzu" + i);
			graph.addNode("Node" + i);
			Node temp = graph.getNode("Node" + i);
			temp.addAttribute("ui.label", "Node" + i);
			sourceList.add(temp);
			sinkList.add(temp);
//			System.out.println("Liste aller Node:" + sourceList);
//			System.out.println("Liste aller Node:" + sinkList);

		}

		if (anzahlEdges < maxAnzahlEdges) {
//			System.out.println("adde Edges zu graph");
			addEdges(anzahlEdges);
		} else {
//			System.out.println("Anzahl Edges ist nicht maximal");
			addEdges(maxAnzahlEdges);
		}
		// Erzeugung der Quelle und der Senke
		graph.addNode("q");
		Node tempNode = graph.getNode("q");
		tempNode.addAttribute("ui.label", "q");

		graph.addNode("s");
		tempNode = graph.getNode("s");
		tempNode.addAttribute("ui.label", "s");

//		System.out.println("fange an zu checken");
		checkSourceSink(graph.getNode("q"), graph.getNode("s"));

	}

	private void checkSourceSink(Node quelle, Node senke) {
//		System.out.println("checke");

		Dijkstra d = new Dijkstra(graph, quelle.getId(), senke.getId());

		while (quelle.getOutDegree() == 0) {
//			System.out.println("quelle hat keine Verbindungen");

			addSource(3);

		}
//		System.out.println("quelle hat Verbindungen");

		while (senke.getInDegree() == 0) {
//			System.out.println("senke hat keine Verbindungen");

			addSink(3);

		}
//		System.out.println("senke hat Verbindungen");

		// d.faerben(false);
//		while (!d.suche()) {
//			System.out.println("Quelle und Senke sind nicht verbunden");
//		}
	}

	private void addSource(Integer temp) {
//		System.out.println("adde Source Knoten");

		Node node1 = graph.getNode("q");

		for (int i = 1; i < temp; i++) {
			if (!sourceList.isEmpty()) {
				if (graph.getEdge(i) != null) {
					graph.removeEdge(i);
				}
				Random randomWeight = new Random();

				Node node2 = getRandomNode();
				if (sourceList.contains(node2)) {
					sourceList.remove(node2);

//					System.out.println("Liste aller Nodes derzeit:" + sourceList);
					Edge edge = graph.getEdge(node1.getId() + node2.getId());
//					System.out.println("Gibt es die Edge schon?:" + graph.getEdge(node1.getId() + node2.getId()));

					if (edge == null) {

						graph.addEdge(node1.getId() + node2.getId(), node1, node2, true);
//						System.out.println("neue Edge:" + graph.getEdge(node1.getId() + node2.getId()));

						edge = graph.getEdge(node1.getId() + node2.getId());

						Double gewicht = (randomWeight.nextDouble() * maxKantenGewicht) + 5;

						gewicht = (double) ((Math.round(gewicht * 100)) / 100);

						edge.setAttribute(GraphController.EdgeAttributeWeight, gewicht);
						edge.setAttribute("ui.label", gewicht);
						i++;

					}
				}
			}
		}

	}

	private void addSink(Integer temp) {
//		System.out.println("adde Sink Knoten");
		Node node1 = graph.getNode("s");
		for (int i = 1; i < temp + 5; i++) {
			if (!sinkList.isEmpty()) {
				graph.removeEdge(i);
				Random randomWeight = new Random();

				Node node2 = getRandomNode();
				if (sinkList.contains(node2)) {
					sinkList.remove(node2);
//					System.out.println("Liste aller Nodes derzeit:" + sourceList);
					Edge edge = graph.getEdge(node2.getId() + node1.getId());
//					System.out.println("Gibt es die Edge schon?:" + graph.getEdge(node2.getId() + node1.getId()));

					if (edge == null) {

						graph.addEdge(node2.getId() + node1.getId(), node2, node1, true);
//						System.out.println("neue Edge:" + graph.getEdge(node2.getId() + node1.getId()));

						edge = graph.getEdge(node2.getId() + node1.getId());

						Double gewicht = (randomWeight.nextDouble() * maxKantenGewicht) + 5;

						gewicht = (double) ((Math.round(gewicht * 100)) / 100);

						edge.setAttribute(GraphController.EdgeAttributeWeight, gewicht);
						edge.setAttribute("ui.label", gewicht);
						i++;

					}
				}
			}
		}
	}

	private void addEdges(Integer temp) {

		Random randomWeight = new Random();
		int edges = 1;

		while (edges < temp - 1) {
//			System.out.println("füge Edge hinzu: " + edges);
			Node node1 = getRandomNode();
//			System.out.println("Hole Random Node 1: " + node1.getId());
			Node node2 = getRandomNode(node1);
//			System.out.println("Hole Random Node 2: " + node2.getId());

			Edge edge = graph.getEdge(node1.getId() + node2.getId());

			if (edge == null) {

				graph.addEdge(node1.getId() + node2.getId(), node1, node2, true);
//				System.out.println("Edge hinzugefügt:" + graph.getEdge(node1.getId() + node2.getId()));
				edge = graph.getEdge(node1.getId() + node2.getId());
				edgeList.add(edge);
//				System.out.println("aktuelle edgeList: " + edgeList);
				if (edgeList.contains(edge)) {

					Double gewicht = (randomWeight.nextDouble() * maxKantenGewicht) + 5;
					gewicht = (double) ((Math.round(gewicht * 100)) / 100);
					edge.setAttribute(GraphController.EdgeAttributeWeight, gewicht);
					edge.setAttribute("ui.label", gewicht);
				}
				edges++;
//				System.out.println("Nächste Runde");

			} else {
//				System.out.println("Gibt es schon. Mach noch mal");
			}
		}
	}

	private Node getRandomNode() {

		Random rand = new Random();

		Integer zufall = rand.nextInt(anzahlNodes + 1);

		Node node = graph.getNode("Node" + zufall);

		if (node == null) {
			node = getRandomNode();
		}
		return node;

	}

	private Node getRandomNode(Node node1) {

		Node node2 = getRandomNode();

		if (node1.getId().equals(node2.getId())) {
			node2 = getRandomNode(node1);
		}
		return node2;

	}

}
