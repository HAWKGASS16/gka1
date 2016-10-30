package gka1gc;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

public class MainGKA {
	
	protected static String stylesheet="node {fill-color: black; size: 15px, 15px; stroke-mode: plain; stroke-color: blue;} node.marked{ fill-color: red;}node.start{fill-color: green;} node.shortest{ fill-color:green; }edge { fill-color: grey;} edge.shortest{fill-color: green; stroke-width:2px;}";
//	protected static String stylesheetEdge="edge { fill-color: grey;} ";
	static Graph graph = new MultiGraph("anzeigegraph");
	static boolean animated=true;
	private boolean log=false;
	private int unreachableNodes=0;
	private static String NodeAttributVisited = "visited";
	private static String NodeAttributdistance = "Distanz";
	private static String EdgeAttributeWeight = "Gewicht";
	private boolean weightedGraph=false;
	private boolean dijkstraShort = true;
	

	public MainGKA(FileParser fp) {
//gsff
		
		
		HashSet<String> nodeSet = fp.getNodes();
		HashSet<gka1gc.Edge> edgeSet = fp.getEdges();
		
		Iterator nodeIterator = nodeSet.iterator();
		while(nodeIterator.hasNext()){
			String nodeName = (String) nodeIterator.next();
			
			graph.addNode(nodeName);
			graph.getNode(nodeName).addAttribute(NodeAttributVisited, false);
		}
		
		//iteriert über das Set indem die aus der datei ausgelesenen edges sind
		//und fügt sie dem Graphen hinzu
		Iterator edgeIterator = edgeSet.iterator();
		while (edgeIterator.hasNext()) {
			gka1gc.Edge object = (gka1gc.Edge) edgeIterator.next();
			String name=object.getName();
			
			
			if((object.getName()=="")||(object.getName()==null)){
				name=object.getStartNode()+object.getEndNode();
				
			}
			
			
			graph.addEdge(name, object.getStartNode(), object.getEndNode(),object.getDirected());
			
			Edge edge = graph.getEdge(name);
			
			if(object.getWeight()!=null){
				edge.addAttribute(EdgeAttributeWeight, object.getWeight());
				edge.setAttribute("ui.label", ""+object.getWeight());
			}else{
				edge.addAttribute(EdgeAttributeWeight, Double.POSITIVE_INFINITY);
			}
			
		}
		
		
		
		for(Node node : graph){
			node.addAttribute("ui.label", node.getId());
		}
		
		if(fp.isWeighted()){
			weightedGraph=true;
		}
		
		
		//graph.getNode("n1").setAttribute("ui.class", "marked");
		//graph.getNode("n1").setAttribute("ui.class", "unmarked");
		
		
		//explore(graph.getNode("n1"));
		
		
		
		//ab hier ist der graf eigentlich soweit fertig gebaut
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	
	public static void main(String[] args) {
		
		
		
//		FileParser fp = new FileParser("H:\\Files\\Dropbox\\Dropbox\\Uni\\Semester 3\\GKA\\Hölings\\bspGraphen\\bsp.gka");
		FileParser fp = new FileParser("H:\\Files\\Dropbox\\Dropbox\\Uni\\Semester 3\\GKA\\Hölings\\bspGraphen\\graph03.gka");
//		FileParser fp = new FileParser("C:\\Users\\Patrick\\Dropbox\\Uni\\Semester 3\\GKA\\Hölings\\bspGraphen\\graph03.gka");
		fp.parsefile();
		MainGKA main = new MainGKA(fp);
		
		graph.addAttribute("ui.stylesheet", stylesheet);
		graph.display();
		
//		sleep();
//		String start="Detmold";
//		String ziel="Husum";
//		boolean erreichbar=main.btsSuche(start, ziel);
//		System.out.println("der Knoten "+ziel+" ist von "+start+" aus zu erreichen: "+erreichbar);
		
		main.dijkstra("Husum", "Hannover");
		
	}
	
	private void addNodes(Set nodes){
		//fügt alle nodes aus einem set hinzu
		
	}
	private void unMarkGraph(){
		//Iteriert über alle nodes und markiert sie als nicht markiert
	}
	private void markNode(String name){
		graph.getNode(name).setAttribute("ui.class", "marked");
		
	}
	private void addAllEdgeLabels(){
		//zeigt die Labels ALLER edges an
		
	}
	
	/**
	 * sorgt dafür das die Kanten (Edges) ein label erhalten
	 * (currently not working)
	 * @param name Name der Kante (Edge) die ein Label bekommen soll
	 */
	private static void addEdgeLabel(String name){
		Edge edge = graph.getEdge(name);
		edge.setAttribute("ui.label", edge.getId());
	}
	public static void explore(Node source){
		System.out.println("explore: "+source.getId());
		
		
		Iterator<? extends Node> k = source.getBreadthFirstIterator();
		System.out.println("k has next: "+k.hasNext());
		while(k.hasNext()){
			
			Node next = k.next();
			
			next.addAttribute("ui.class", "marked");
			sleep();
		}
	}
	private static void sleep() {
		
		sleep(1000);
		
	}
	private static void sleep(long millis) {
		
		if(animated){
		try {Thread.sleep(millis);} catch(Exception e){
			
		}}
		
	}
	
	
	/**
	 * Diese Suche beginnt bei einem Startknoten und traversiert den durch bis entweder der
	 * Zielknoten gefunden wurde, oder ALLE erreichbaren Knoten besucht wurden, ohne den Zielknoten
	 * zu finden
	 * @param start ist der Name des Statknotens
	 * @param ende ist der Name des Endknotens
	 * @return liefert true wenn der Zielknoten vom Startknoten aus erreichbar ist, andernfalls false
	 */
	public boolean btsSuche(String start, String ende){
		
		//variable zum zaehlen der besuchten Knoten
		//wird benötigt um festzustellen ob ALLE knoten überhaupt erreichbar sind
		int visitedNodes=0;
		
		LinkedList<Node> queue = new LinkedList<>();
		
		log("Die breitensuche beginnt bei "+start+" und soll "+ende+" enden");
		
		Node startNode = graph.getNode(start);
		Node endNode = graph.getNode(ende);
		startNode.setAttribute(NodeAttributVisited, true);
		queue.add(startNode);
		Node tempNode;
		startNode.addAttribute("ui.class", "start");
		
		
		while(!queue.isEmpty()){
			
			
			//zur verschönerung den Startnode anders färben als die Besuchten
			startNode.addAttribute("ui.class", "start");
			
			
//			System.out.println(queue.toString());
			
			//der erste Knoten wird aus der Queue entnommen (startNode) 
			tempNode=queue.pollFirst();

			log("Der aktuelle Knoten ist: "+tempNode.getId()+" und wurde als besucht markiert");
			log("Die Queue ist: "+queue.toString());
			
			
			
			//der Aktuelle knoten wird als besucht markiert
			tempNode.setAttribute(NodeAttributVisited, true);
			tempNode.setAttribute("ui.class", "marked");
			
//			System.out.println(tempNode.getId());
			if(tempNode.getId().equals(endNode.getId())){
				return true;
			}
			
			
//			System.out.println("visit: "+tempNode.getId());
			visitedNodes+=1;
			//sleep zum animieren der veränderungen
			sleep();
			
			
			Iterator NodeIterator = tempNode.getEachEdge().iterator();
			//Iteriert über ALLE kanten des aktuellen knotens
			while(NodeIterator.hasNext()){
				Edge edge = (Edge)NodeIterator.next();
					
				
					//wenn die kante gerichtet ist, wird geprüft ob der aktuelle knoten das ziel oder die Quelle 
					//der Kante ist
					if(edge.isDirected()){
						
//						wenn der aktuelle knoten NICHT der Zielknoten der gerichteten Kante ist
						if(edge.getTargetNode()!=tempNode){
							
//							hier wird festgestellt ob der knoten schon enmal besucht war
							if(!isNodeVisited(edge.getTargetNode().getId())){
//								System.out.println("den knoten "+edge.getTargetNode().getId()+" wurde schon besucht");
								//ab hier ist der aktuelle Knoten NICHT das Ziel der gerichteten Kante								
								
								//wenn der knoten noch nicht besucht wurde, wird geprüft ob er schon in der queue ist
								//wenn das der Fall ist, wird er der Queue hinzugefuegt
								if(! (queueContainsNode(queue, edge.getTargetNode().getId())||isNodeVisited(edge.getTargetNode().getId())) ){
									queue.add(edge.getTargetNode());
									log("der Knoten der gerichteten Kante, der noch nicht besuht ist UND noch nicht in der queue ist "+edge.getTargetNode().getId()+" wird der queue hinzugefügt");
								}
								
							}
						}
					}else{
						//wenn im undirekten Graphen der gegenüberliegende Knoten vom aktuellen knoten (auf der selben kante)
						// noch nicht in der queue ist, wird er hinzugefügt
						Node oppositeNode = edge.getOpposite(tempNode);
						if(! (queueContainsNode(queue, oppositeNode.getId())||isNodeVisited(oppositeNode.getId())) ){
							
							queue.add(oppositeNode);
							log("242:Knoten "+edge.getOpposite(tempNode).getId()+" wird der queue hinzugefuegt");
						}
					}
				
			}
		}
		
		int unvisitedNodes = graph.getNodeCount()-visitedNodes;
		if(unvisitedNodes!=0){
			log("es konnten "+unvisitedNodes+" knoten nicht besucht werden");
		}
		return false;
		
		
		
		
	}
	private boolean queueContainsNode(LinkedList<Node>nodeList, String nodeName){
		
		for(Node node: nodeList){
			if(node.getId().equals(nodeName)){
				return true;
			}
		}
		
		
		return false;
	}
	/**
	 * Stellt fest ob ein Knoten/ein Node besucht wurde oder nicht
	 * @param nodename der Knotenname als String
	 * @return true - wenn der Knoten schon bescuht wurde, false wenn nicht
	 */
	private boolean isNodeVisited(String nodename){
		
		//aufgrund von Fehlern beim casten (anscheinend probleme mit dem Framework)
		//muss so umständlich der wert für "besucht" zurückgegeben werden
		
		Node node = graph.getNode(nodename);
		log("Node "+node.getId()+" ist besucht:"+node.getAttribute(NodeAttributVisited));
		return (boolean)node.getAttribute(NodeAttributVisited);
//		System.out.println("knoten isVisited: "+node.getId()+" zustand: "+node.getAttribute("besucht"));
//		String besucht = ""+node.getAttribute("besucht");
		
//		if(besucht.equals("true")){
//			return true;
//		}else{
//			return false;
//		}
//		return besucht;
	}

	private void log(String message){
		if(log){
			System.out.println(message);
		}
	}
	/**
	 * Aktualisiert die Distanz des Knotens der dem übergebenen Knoten auf der übergebenen
	 * Kante gegenüber liegt. 
	 * @param knoten der Knoten von dem die neuberechnung ausgeht
	 * @param kante die kante die das gewicht liefert
	 */
	private void distanzUpdate(Node knoten, Edge kante){
		
		
		//die Variable "nachbar" wird mit dem übergebenen knoten initialisiert
		//einfach nur damit sie initialisiert ist^^ 
		Node nachbar;
		Double edgeGewicht = (Double)kante.getAttribute(EdgeAttributeWeight);
		Double knotenGewicht = (Double)knoten.getAttribute(NodeAttributdistance);
		
		Double distanz = knotenGewicht+edgeGewicht;
		
		if(kante.isDirected()){
			if(kante.getSourceNode()==knoten){
				nachbar=kante.getTargetNode();
			}else{
				//Wenn der übergebene Knoten NICHT die Quelle einer gerichteten Kante ist
				//wird die Distanz natürlich nicht angepasst und deshalb returnt
				return;
			}
		}else{
			nachbar=kante.getOpposite(knoten);
		}
		
		Double nachbargewicht = nachbar.getAttribute(NodeAttributdistance);
		Double minGewicht = Math.min(distanz, nachbargewicht);
//		log("dijstra-distanzUpdate-- nachbargewicht: "+nachbargewicht);
		nachbar.addAttribute(NodeAttributdistance, minGewicht);
		addNodeWeightLabel(nachbar, minGewicht);
		
	}
	public void dijkstra(String startKnoten, String endKnoten){
		initializeDijkstra();
//		//eine neue queue die immer 
//		PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
//
//			@Override
//			public int compare(Node o1, Node o2) {
//				
//				Double node1 = (Double)o1.getAttribute(NodeAttributdistance);
//				Double node2 = (Double)o2.getAttribute(NodeAttributdistance);
//				
//				return node1.compareTo(node2);
//			}
//		});
		
		//eine neue queue aus der immer immer das minimalste element entfernt wird
		
		Node start = graph.getNode(startKnoten);
		Node ende = graph.getNode(endKnoten);
		start.setAttribute(NodeAttributdistance, 0.0);
		LinkedList<Node> queue = new LinkedList<Node>();
		log("dijstra---startnode: "+start.getId());
		queue.add(start);
		Node tempNode;
		Edge tempEdge;
		
		while(!(queue.isEmpty())){
			sleep();
			tempNode=getMinimumNode(queue);
			log("dijstra---momentane tempNode: "+tempNode.getId());
			log("dijstra---queue:"+queue.toString());
			
			
			tempNode.setAttribute(NodeAttributVisited, true);
			tempNode.setAttribute("ui.class", "marked");
			log("dijstra---der Knoten "+tempNode.getId()+" wird als besucht markiert");
			
			
			Iterator edgeIterator = tempNode.getEdgeIterator();
			while(edgeIterator.hasNext()){
				tempEdge=(Edge)edgeIterator.next();
				
				distanzUpdate(tempNode, tempEdge);
				
				Node tempNachbar = tempEdge.getOpposite(tempNode);
				boolean besucht = (boolean)tempNachbar.getAttribute(NodeAttributVisited);
				
				if((!besucht)&&(!queue.contains(tempNachbar))){
					//Wenn der gegenüberliegende Knoten noch nicht besucht ist, und noch nicht 
					//in der Queue, dann wird er zur queue hinzugefügt
					queue.add(tempNachbar);
					log("dijstra---nachbar "+tempNachbar.getId()+" wird zur queue hinzugefuegt");
				}
				
			}
			
			
//			wenn diese if-Bedingung aktiv ist, hoert der Algorithmus auf, denn wenn der zuletzt gefundene Knoten der Zielknoten ist,
//			ist das schon der kürzeste weg da immer der aktuell kürzeste weg untersucht wird.
			if(dijkstraShort&&tempNode==ende){
				break;
			}
			
			
		}
		
		logShortestWay(start, getShortestWay(start, ende));
		log("dijstra---fertig");
	}
	
	private void logShortestWay(Node startNode, ArrayList<Edge> shortestWay) {
		log("dijkstra--shortestWay--start");
		
		Edge temp;
		Node succ=null;
		Node prev = startNode;
		
		for(int i = shortestWay.size()-1;i>=0;i--){
			
			temp=shortestWay.get(i);
			succ=temp.getOpposite(prev);
			log("dijkstra--shortestWay--von "+prev.getId()+" nach "+succ.getId());
			
			prev=succ;
			
		}
		
		log("dijkstra--shortestWay--es waren "+shortestWay.size()+" Kanten (Spruenge)");
		
		if(weightedGraph){
			log("dijkstra--shortestWay--eine Distanz von: "+((Double)succ.getAttribute(NodeAttributdistance)));
		}
			
	}
		
	



	/**
	 * liefert aus der queue das Element mit dem niedrifsten weg zurück
	 * @param queue die Queue aus der das Minimum geliefert wird
	 * @return den Knoten mit dem niedrigsten weg
	 */
	private Node getMinimumNode(LinkedList<Node> queue){
		
		
		Node minimalNode=queue.getFirst();
		Node temp=null;
		Double minimumEntfernung = (Double)minimalNode.getAttribute(NodeAttributdistance);
		
		//iteriert über alle Einträge der queue und merkt sich
		//immer das kleinste Element also den Knoten mit der geringsten entfernung
		Iterator queueIterator = queue.iterator();
		while(queueIterator.hasNext()){
			temp=(Node) queueIterator.next();
			log("dijstra-getMini-- iteriere ueber die queue mit dem aktuellen knoten: "+temp.getId());
			Double minimalNodeDistance = (Double) minimalNode.getAttribute(NodeAttributdistance);
			Double tempNodeDistance = (Double) temp.getAttribute(NodeAttributdistance);
			if(tempNodeDistance<minimalNodeDistance){
				minimalNode=temp;
			}
		}
		
		log("dijstra-getMini-- es wird der Knoten "+minimalNode.getId()+" aus der queue entfernt");
		queue.remove(minimalNode);
		return minimalNode;
		
	}
	/**
	 * bereitet vor, indem alle Knotenentfernungen mit Double.PositivInfinite
	 * initialisiert werden UND alle knoten unmarkiert werden (und ein paar viselle dinge..)
	 */
	private void initializeDijkstra(){
		//wenn der gesamte Graph ungewichtet ist, sollen alle kanten mit einem gewicht von 1.0
		//initialisiert werden
		
		
		
		Node temp;
		Iterator nodeIterator = graph.getNodeIterator();
		while(nodeIterator.hasNext()){
			temp = (Node)nodeIterator.next();
			
			temp.setAttribute(NodeAttributdistance, Double.POSITIVE_INFINITY);
			temp.setAttribute("ui.class", "unmarked");
			temp.setAttribute(NodeAttributVisited, false);
		}
		
		
		if(!weightedGraph){
			Edge tempEdge;
			Iterator edgeIterator = graph.getEdgeIterator();
			while(edgeIterator.hasNext()){
				tempEdge = (Edge)edgeIterator.next();
				
				tempEdge.setAttribute(EdgeAttributeWeight, 1.0);
				
				//das label ggf wieder entfernen
				tempEdge.setAttribute("ui.label", 1.0);
				
			}
		}
		
	}
	/**
	 * Fügt dem Knoten seine aktuelle Entfernung als Beschriftung für  
	 * @param node
	 * @param weight
	 */
	private void addNodeWeightLabel(Node node,Double weight){
		
		String nodeName = node.getId();
		node.addAttribute("ui.label", nodeName+" : "+weight);
		
	}
	/**
	 * Liefert einen kürzesten Weg in einer Liste zurück
	 * Vorbedingung: der Dijkstra-Algorithmus ist einmal durchlaufen UND es gibt (mindestens) 
	 * einen kürzesten weg.
	 * Nachbedingungen: der Graph bleibt unverändert. 
	 * 
	 * @param endknoten
	 * @return enthält einen Kürzesten Weg in umgekehrter Reihenfolge. Also das erste Element ist die letzte Kante die zum Zielknoten führt
	 */
	private ArrayList<Edge> getShortestWay(Node startKnoten, Node endknoten){
		
		
		ArrayList<Edge> shortestWay = new ArrayList<Edge>();
		
		
		Node tempNode = endknoten;
		
		while(tempNode!=startKnoten){
			
			//temporäre liste aller edges aus denen das Minumum - also der nächst kürzeste Knoten - extrahiert wird 
			ArrayList<Edge> tempList = new ArrayList<Edge>();
			Edge tempEdge;
			
			Iterator edgeIterator = tempNode.getEdgeIterator();
			while(edgeIterator.hasNext()){
				tempEdge = (Edge) edgeIterator.next();
				
				if(tempEdge.isDirected()){
					
					if(tempEdge.getTargetNode()==tempNode){
						tempList.add(tempEdge);
					}
					
				}else{
					tempList.add(tempEdge);
				}
				
				
				
			}
			tempEdge = getMinimumEdge(tempNode, tempList);
			shortestWay.add(tempEdge);
			markEdgeVisual(tempEdge);
			tempNode=tempEdge.getOpposite(tempNode);
		}
		
		return shortestWay;
		
	}


	private void markEdgeVisual(Edge tempEdge) {
		
		if(animated){
			sleep(300);
		}
		
		tempEdge.getTargetNode().addAttribute("ui.class", "shortest");
		tempEdge.addAttribute("ui.class", "shortest");
		tempEdge.getSourceNode().addAttribute("ui.class", "shortest");
		
		
	}



	/**
	 * Liefert die Kante mit mit der geringsten Entfernung zum start zurück
	 * 
	 * Vorbedingungen: In der Liste befinden sich nur Kanten, dessen Knoten -vom Startpunkt des Dijkstra aus betrachtet- den
	 * übergebenen Knoten erreichen können.
	 * Nachbedingungen: es wird nur 1 minimales Objekt übergeben
	 * @param tempNode
	 * @param tempList
	 * @return
	 */
	private Edge getMinimumEdge(Node tempNode, ArrayList<Edge> tempList) {
		
		//tempNode ist der Knoten der näher am ziel liegt - also der der zum Startpunkt guckt
		
		Node vergleichsNode;
		Double vergleichsGewicht;
		Edge tempEdge;
		Edge minimumEdge = tempList.get(0);
		Double minimumWeight = (Double) minimumEdge.getOpposite(tempNode).getAttribute(NodeAttributdistance);
		
		
		Iterator listIterator = tempList.iterator();
		while(listIterator.hasNext()){
			tempEdge = (Edge) listIterator.next();
			
			vergleichsNode = tempEdge.getOpposite(tempNode);
			vergleichsGewicht = (Double) vergleichsNode.getAttribute(NodeAttributdistance);
			
			if(vergleichsGewicht<minimumWeight){
				minimumEdge=tempEdge;
				minimumWeight= vergleichsGewicht;
				
			}
			
		}
		
		
		return minimumEdge;
	}
}
