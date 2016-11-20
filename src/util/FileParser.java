package util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.graphstream.graph.Graph;

import application.GraphController;




public class FileParser {
	Graph graph;
	HashSet<String> nodes = new HashSet<>();
	HashSet<Edge> edges = new HashSet<>();
	HashSet<Edge> fehlerhafteEdges = new HashSet<>();

	boolean undirected = true;
	boolean weighted = false;

	String path;

//	alt: Pattern directionPattern = Pattern.compile("[a-zA-Z0-9]*[--].|[a-zA-Z]*[->]");
	Pattern directionPattern = Pattern.compile("(((.*?)([ ^-])))");
//	Pattern nodePattern = Pattern.compile("([a-zA-Z0-9]([^ -.*]||[^-.*]))");
	Pattern nodePattern = Pattern.compile("(.*?)(( -.*)|(-.*))");
	Pattern node2Pattern = Pattern.compile("([->]|[--])(\\s*)([a-zA-Z0-9öäüÖÄÜ]+)");
	Pattern weightPattern = Pattern.compile("(.+: +)([0-9]+)");
	Pattern edgeNamePattern = Pattern.compile("([(])([a-zA-Z])([)])");
	//((- .*?[^:])|(> .*?[^:])|(-.*?[^:])|(>.*?[^:]))(.*[:]) gannz gut bisher

//	public static void main(String[] args) {
//		FileParser fp = new FileParser("H:\\Files\\Dropbox\\Dropbox\\Uni\\Semester 3\\GKA\\Hölings\\bspGraphen\\alle Graphen.gka");
//		fp.parsefile();
//	}

	public FileParser(Graph graphen, String path) {
		graph=graphen;
		setPath(path);
	}

	public FileParser(File file) {
		setPath(file.getAbsolutePath());
	}

	public void setPath(String path) {
		if (path.equals("")) {
			raiseException("Empty strings are not allowed");
		} else {

			this.path = path;
		}

	}

	public boolean isUndirected() {

		return undirected;
	}

	public boolean isDirected() {
		return !undirected;
	}

	public boolean isWeighted() {
		return weighted;
	}

	public boolean isUnWeighted() {
		return !weighted;
	}

	
	/**
	 * Liest die im Konstruktor übergebene Datei ein und extrahiert alle Knoten und Kanten.
	 * Anschließend sind die Knoten über getNodes() und die Kanten über getEdges() zu erreichen
	 */
	public void parsefile() {

		

		try {
			//der Reader um die Datei zeilenweise einzulesen
			BufferedReader bf = new BufferedReader(new FileReader(new File(path)));
			for (String line; (line = bf.readLine()) != null;) {
				
//				System.out.println(line.replaceAll(" ", ""));
				
				String temp;
				String[] splitSemikolon;
				String node1;
				String node2;
				String direction;
				Double weight;
				String name;
				boolean directed=true;
				
				
//				System.out.println("--------------------------------");
				
//				für den Fall das in 1 Zeile nach dem Semikolon noch etwas steht.
//				was wird dann ignoriert
				splitSemikolon = line.split(";");
				
				if(!(splitSemikolon[0].contains("->")||splitSemikolon[0].contains("--"))){
					//wenn die datei keine edges enthält, oder fehlerhaftes format hat, dann
					//fehler werfen oder ignorieren
					continue;
				}

				if (splitSemikolon[0].contains("->")) {
					undirected = false;
				}
				if (splitSemikolon[0].contains(":")) {
					weighted = true;
				}
				temp = splitSemikolon[0].replaceAll(" ", "");
//				System.out.println(temp);

				node1 = getFirstNode(splitSemikolon[0]);
//				System.out.println("node1: "+node1);
				
				direction = getDirection(splitSemikolon[0]);
//				System.out.println("direction: "+direction);
				
				
				node2 = getSecondNode(splitSemikolon[0]);
//				System.out.println("node2: "+node2);
				
				
				weight = getWeight(splitSemikolon[0]);
//				System.out.println("gewicht: "+weight);
				
				name = getName(splitSemikolon[0]);
//				System.out.println("name: "+name);
				
				
				if(graph.getNode(node1)==null){
					graph.addNode(node1);
					graph.getNode(node1).addAttribute("ui.label", node1);
				}
				
				
				if(graph.getNode(node2)==null){
					graph.addNode(node2);
					graph.getNode(node2).addAttribute("ui.label", node2);
				}
				
				
				if(direction.contains("--")){directed=false;}
				
				graph.addEdge(node1+node2, node1, node2);
				org.graphstream.graph.Edge graphstreamEdge = graph.getEdge(node1+node2);
				
				
				
				Edge edge =new Edge(name, node1, node2, weight, directed);
				//wenn im gewichteten graphen eine kante kein gewicht hat (quelle ist fehlerhaft)
				//dann wird die kante zu den fehlerhaften kanten hinzugefügt.
				//wenn die kante im gewichteteten graphen auch ein gewicht hat, wird sie zu den 
				//"heilen" kanten hinzugefügt
				if(weighted&&weight!=null){
					graphstreamEdge.addAttribute(GraphController.EdgeAttributeWeight, weight);
					graphstreamEdge.addAttribute("ui.label", weight);
					
				}else if(!weighted){
					
				}
				else{
					fehlerhafteEdges.add(edge);
				}

//				System.out.println(node1 + " " + direction + " " + node2);
				// anschliessend beide nodes erstellen und dann zu den sets hinzufügen

			}

//			log("es gibt "+fehlerhafteEdges.size()+" fehlerhafte kanten");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Liefert ein HashSet von Knoten zurück. Zuvor muss natürlich erst eine Datei geparst werden. Wenn nicht ist der/die/das HashSet leer 
	 * @return ein HashSet mit Knoten gefüllt - oder eine leere Liste
	 */
	public HashSet<String> getNodes(){
		return nodes;
	}
	
	/**
	 * Liefert ein HashSet von Kanten zurück, die - wenn zuvor geparst wurde - mit ausgelesenen Kanten
	 * @return wenn vorher geparst enthält es die ausgelesenen Kanten.
	 */
	public HashSet<Edge> getEdges(){
		return edges;
	}
	

	private void raiseException(String message) {

	}

	private String getFirstNode(String string) {
		List<String> results = new ArrayList<String>();
		String temp = "";
		Matcher matcher = nodePattern.matcher(string);

		while (matcher.find()) {
			results.add(matcher.group(1));
			// System.out.println(matcher.group());
			// temp=matcher.group();
		}
		 
		 if(results.isEmpty()){
			 return null;
		 }
//		 System.out.println(results.get(0));
		return results.get(0);
	}
	
	
	

	private String getSecondNode(String string) {
//		
//		String[] temp;
//		String stringkopie;
//		
//		if(string.contains("->")){
//		temp = string.split("->");
//		}else{
//			temp=string.split("--");
//		}
//		int positionDurch=0;
//		
////		System.out.println("laenge ist: "+temp.length+" "+temp[0]+"<-- pos 0 pos1-> "+temp[1]);
//		
//		if(string.contains(":")){
//			positionDurch=temp[1].indexOf(":");
//			
//			
////			System.out.println(temp[1].substring(1, positionDurch));
//			return temp[1].substring(1, positionDurch);
//			
//		}else if(string.contains("(")){
//			
//			positionDurch=temp[1].indexOf("(");
////			System.out.println(temp[1].substring(1, positionDurch));
//			return temp[1].substring(1, positionDurch);
//			
//		}else{
////			System.out.println(temp[1]);
//			return temp[1].substring(1, temp[1].length());
//		}
		
		List<String> results = new ArrayList<String>();
		String temp = "";
		Matcher matcher = node2Pattern.matcher(string);

		while (matcher.find()) {
			results.add(matcher.group(3));
			// System.out.println(matcher.group());
			// temp=matcher.group();
		}
		 
		 if(results.isEmpty()){
			 return null;
		 }
//		 System.out.println(results.get(0));
		return results.get(0);
		
		
		
		
	}
	
	
	
	
	
	

	private String getDirection(String string) {
		
		/*
		String temp = "";
		Matcher matcher = directionPattern.matcher(string);

		while (matcher.find()) {
			// System.out.println(matcher.group());
			temp = matcher.group();
		}


		 */
		
		if(string.contains("--")){
			return "--";
		}else if(string.contains("->")){
			return "->";
		}
		
		return "";
	}

	private Double getWeight(String string) {
		
		
//		string.replaceAll(" ", "");
		
//		System.out.println("--------------");
//		System.out.println("der sitring ist: "+string);
		
		
		
		int position =0;
		
		if(string.contains(":")){
			
			String[] tempSplit = string.split(":");
//			System.out.println("inhalt nach dem :Split "+tempSplit[1].toString());
			
			//wenn im gewichteten graphen kein gewicht aufteucht, ist die Quelle fehlerhaft
			//und es wird mit null rausgesprungen
//			if((tempSplit[1].matches("[0-9]+"))){
////				return null;
//			}else{
//				return null;
//			}
			
			
			List<String> results = new ArrayList<String>();
			String temp1 = "";
			Matcher matcher = weightPattern.matcher(string);

			while (matcher.find()) {
				results.add(matcher.group(2));
//				 System.out.println(matcher.group(2));
				temp1=matcher.group();
			}
			 
			 if(!results.isEmpty()){
//				 System.out.println("gefundenes weightpattern: "+results.get(0));
			 }else{
				 return null;
			 }
			
			
			
			
			
//			
//			position = string.indexOf(":");
//			String temp=string.substring(position+1);
//			temp.replace("\t", "");
//			temp.replace("\n", "");
//			System.out.println(temp.substring(1)+"ohne leerzeichen");
//			
//			return Integer.parseInt(temp.substring(1));
			 
			 return Double.parseDouble(results.get(0));
		}
		
		return null;
	}

	private String getName(String string) {
		
		
		//wenn der String Klammern enthält, wird der pattern matcher angewendet
		if(string.contains("(")&&string.contains(")")){
//			System.out.println("der string "+string+" matched dem pattern");
			List<String> results = new ArrayList<String>();
			String temp1 = "";
			Matcher matcher = edgeNamePattern.matcher(string);

			while (matcher.find()) {
				results.add(matcher.group(2));
//				 System.out.println(matcher.group(2));
				temp1=matcher.group();
			}
			 
			 if(!results.isEmpty()){
				 return results.get(0);
			 }else{
				 return null;
			 }
		}else{
			return null;
		}
		
		
		
//		
//		int start=0;
//		int ende=0;
//		
//		if(string.contains("(")&&string.contains("")){
//			start=string.indexOf("(")+1;
//			ende=string.indexOf(")");
//		}else{
//			return "";
//		}
//		
//		
//		return string.substring(start, ende);
	}
	private void log(String log){
		
	}

}
