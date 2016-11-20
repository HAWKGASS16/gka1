package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import application.GraphController;

/**
 * Soll eine Liste aller Kanten erhalten und sie dann in eine Datei *.gka
 * speichern
 * 
 * @author Höling
 *
 */
public class FileSaver {

	String pfad = "result.gka";
	
	
	/**
	 * Liefert einen FileSaver der im aktuellen Verzeichnis des Programms die
	 * Dateien erstellt
	 */
	public FileSaver() {
		
	}

	public void saveToFile(Graph graph, File file) {

		
		Collection edgeListe = graph.getEdgeSet();
		
		StringBuilder output = new StringBuilder();
		ArrayList<String> outputString = new ArrayList<String>();
		
		Edge edgeElem;
		Iterator edgeIterator = edgeListe.iterator();
		while (edgeIterator.hasNext()) {
			String result = "";
			
			edgeElem = (Edge) edgeIterator.next();
			
			String node1Name=edgeElem.getSourceNode().getId();
			String node2Name=edgeElem.getTargetNode().getId();
			String direction;
			String edgeName = (String) edgeElem.getAttribute(GraphController.EdgeAttributeName);
			Double gewicht = (Double) edgeElem.getAttribute(GraphController.EdgeAttributeWeight);
			
			if(edgeElem.isDirected()){
				direction = "->";
			}else{
				direction = "--";
			}
			
			if(edgeName==null){
				edgeName="";
			}else{
				edgeName="("+edgeName+")";
			}
			
			result=node1Name+" "+direction+" "+node2Name+" "+edgeName;
			
			if(gewicht!=null&&gewicht!=Double.POSITIVE_INFINITY){
				result=result+" : "+gewicht;
			}
			
			result=result+";\n";
			output.append(result);
			outputString.add(result);
			
			
		}
		
		
		Iterator nodeIterator = graph.getNodeIterator();
		
		while (nodeIterator.hasNext()) {
			Node object = (Node) nodeIterator.next();
			
			outputString.add(object.getId());
			
			
		}
		
		saveToDisk(outputString, file);
		
	}
	private void saveToDisk(ArrayList<String> outputString, File file){
		
		Writer fw = null;
		String line="";
		
		try {
			fw = new FileWriter(file.getAbsolutePath());
			Iterator outputStringIterator = outputString.iterator();
			while(outputStringIterator.hasNext()){
				line = (String)outputStringIterator.next();
				
				fw.write(line+"\n");
				fw.write(System.lineSeparator());
//				fw.append(System.getProperty("line.seperator"));
				
				
			}
			
			
//			fw.write(outputString);
			
			
		}catch (Exception e) {
			// TODO: handle exception
		} 
		finally {
			// TODO: handle finally clause
			if(fw!= null){
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
	}

}
