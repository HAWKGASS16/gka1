package util;


public class Edge {
	
	private String name;
	private String startNode;
	private String endNode;
	private Double weight=null;
	private boolean directed=false;
	
	public Edge(String name, String sartNode, String endNode) {
		
		this.name=name;
		this.startNode=startNode;
		this.endNode=endNode;
		
		
	}
	public Edge(String name, String startNode, String endNode, Double weight, boolean directed) {
		
		this.name=name;
		this.startNode=startNode;
		this.endNode=endNode;
		this.weight=weight;
		this.directed=directed;
		
		
	}
	
	public String getName(){return name;}
	public String getStartNode(){return startNode;}
	public String getEndNode(){return endNode;}
	public Double getWeight(){return weight;}
	public boolean getDirected(){return directed;};
	public String toString(){
		
		return " von "+startNode+" zu "+endNode;
	}

}
