package algorithmen;

import org.graphstream.graph.Graph;

public class BTS {
	
	private String startKnoten;
	private String endKnoten;
	private Graph graph;
	Dijkstra dijkstra;
	
	
	public BTS(Graph g, String start, String end) {
		graph=g;
		startKnoten=start;
		endKnoten=end;
		
		dijkstra = new Dijkstra(g, start, end, true);
	}
	
	public boolean suche(){
		
		
//		zugriffsZaehler.startMeasure("BFS-Suche von " + start + " nach " + ende);
//		zugriffsZaehler.log("Knotenanzahl: " + graph.getNodeCount());
//		zugriffsZaehler.log("Kantenanzahl: " + graph.getEdgeCount());
//
//		// Breitensuche einschalten. Das bedeutet Dijkstra arbeitet mit einem
//		// Kantengewicht von 1.0
//		btsSuche = true;
//
//		boolean erfolgreich = dijkstra(start, ende);
//		
//		
//		btsSuche = false;
//
//		zugriffsZaehler.stopMeasure();
//
//		return erfolgreich;
		return dijkstra.suche();
		
	}
	public Double getKosten(){
		return dijkstra.getKosten(startKnoten, endKnoten);
	}

}
