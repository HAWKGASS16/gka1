package generator;

import java.util.Random;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class BIG {

	private Graph graph;
	private Integer anzahlNodes;
	private Integer anzahlEdges;
	private boolean big=false;
	
	
	public BIG(Graph graph, Integer nodes, Integer edges) {
		
		this.graph=graph;
		anzahlNodes=nodes;
		anzahlEdges=edges;
		
		
	}
	
	
	
	public void generate(){
		
		
		for(int i=0; i<=anzahlNodes;i++){
			
			graph.addNode("Node"+i);
			Node temp = graph.getNode("Node"+i);
			temp.addAttribute("ui.label", "Node"+i);
			
			
		}
		
		
		Random rand = new Random();
		int edges=0;
		
		
		while(edges<=anzahlEdges){
			Node node1 = getRandomNode();
			Node node2 = getRandomNode(node1);
			
			Edge edge = graph.getEdge(node1.getId()+node2.getId());
			
			if(edge==null){
				
				Double richtung =Math.random();
				
				if(richtung < 0.5){
					graph.addEdge(node1.getId()+node2.getId(), node1, node2,true);
				}else{
					graph.addEdge(node1.getId()+node2.getId(), node1, node2);
				}
				
				
				edges++;
				
			}
			
			
			
		}
		
//		graph.addEdge("Node1Node"+anzahlNodes, "Node1", "Node"+anzahlNodes);
		
		
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
