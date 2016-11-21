package util;

import java.io.File;

import org.graphstream.graph.Graph;

public class FileHandler {

	FileParser fileparser;
	FileSaver filesaver = new FileSaver();
	
	/**
	 * Ein handler, der das Auslesen von .gka Datein verwaltet und der die graphen auch speichert 
	 * @param pfadQuelle ist der Pfad zur Quelldatei eines Graphen im .gka Format.
	 */
	public FileHandler(Graph graph, String pfadQuelle) {

//		pfadQuelle.replace("\\", "\\\\");
		
		if(!pfadQuelle.endsWith(".gka")){
			raiseError("Die angegebene Datei muss eine .gka Datei sein!");
		}
		fileparser = new FileParser(graph, pfadQuelle);
		fileparser.parsefile();
		filesaver = new FileSaver(); 
		
		
		
	}
	
	public FileHandler(){
		
		
		
	}
	
	public FileHandler(File file) {
		fileparser = new FileParser(file);
	}
	/**
	 * Liefert einen FileParser zurück
	 * @return fileparser
	 */
	public FileParser getFileParser(){
		return fileparser;
	}
	private void raiseError(String message){
		throw new IllegalArgumentException(message);
	}
	/**
	 * Speichert die übergebenen Kanten in einer Datei als neuen Graph
	 * @param edgeCollection die Kanten die als Graph gespeichert werden sollen
	 */
	public void saveGraph(Graph graph, File file){
		if(graph==null){
			raiseError("Die übergebene Liste darf nicht null sein!");
		}
		
		filesaver.saveToFile(graph, file);
	}
	public static void loadFile(Graph graph, String Pfad){
		
		
		
	}
}
