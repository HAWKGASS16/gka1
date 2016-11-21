package generator;

import java.util.Random;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import application.GraphController;

public class BIG {

	private Graph graph;
	private Integer anzahlNodes;
	private Integer anzahlEdges;
	private boolean big=false;
	private Double maxKantenGewicht = 300.0;
	
	
	public BIG(Graph graph, Integer nodes, Integer edges) {
		
		this.graph=graph;
		anzahlNodes=nodes;
		anzahlEdges=edges;
		
		
	}
	
	
	
	public void generate(){
		
		
		for(int i=0; i < anzahlNodes;i++){
			
			graph.addNode("Node"+i);
			Node temp = graph.getNode("Node"+i);
			temp.addAttribute("ui.label", "Node"+i);
			
			
		}
		
		
		Random randomWeight = new Random();
		int edges=0;
		
		
		while(edges < anzahlEdges){
			Node node1 = getRandomNode();
			Node node2 = getRandomNode(node1);
			
			Edge edge = graph.getEdge(node1.getId()+node2.getId());
			
			if(edge==null){

					graph.addEdge(node1.getId()+node2.getId(), node1, node2,true);



				edge = graph.getEdge(node1.getId()+node2.getId());
				
				Double gewicht = (randomWeight.nextDouble()*maxKantenGewicht)+5;
				
				gewicht= (double) ((Math.round(gewicht*100))/100);
				
				edge.setAttribute(GraphController.EdgeAttributeWeight, gewicht);
				edge.setAttribute("ui.label", gewicht);
				
				edges++;
				
			}
			
			
			
		}
		graph.addAttribute(GraphController.GraphAttributeDirected, true);
		
		if(big){
			
			Node node1 = graph.getNode("Node1");
			Node node2 = getRandomNode(node1);
			graph.addNode("v1");
			Node node3 = graph.getNode("v1");
			node3.addAttribute("ui.label", "v1");
			
			graph.addEdge("kurz1", node1, node2, true);
			Edge edge = graph.getEdge("kurz1");
			edge.setAttribute(GraphController.EdgeAttributeWeight, 2.0);
			
			graph.addEdge("kurz2", node2, node3, true);
			edge = graph.getEdge("kurz2");
			edge.setAttribute(GraphController.EdgeAttributeWeight, 5.0);
			
			
			
			
		}
		
		
	}
	
	private Node getRandomNode(){
		
		Random rand = new Random();
		
		Integer zufall=rand.nextInt(anzahlNodes+1);
		
		Node node = graph.getNode("Node"+zufall);
		
		if(node==null){
			node=getRandomNode();
		}
		return node;
		
		
	}
	private Node getRandomNode(Node node1){
		
		Node node2 = getRandomNode();
		
		if(node1.getId().equals(node2.getId())){
			node2 = getRandomNode(node1);
		}
		return node2;
		
		
	}
	
	public void setBIG(boolean bool){
		big=bool;
		
	}
	
	
	
	
	
}
