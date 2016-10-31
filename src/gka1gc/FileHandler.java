package gka1gc;

import java.util.Collection;
import org.graphstream.graph.Edge;

public class FileHandler {

	FileParser fileparser;
	FileSaver filesaver;
	
	/**
	 * Ein handler, der das Auslesen von .gka Datein verwaltet und der die graphen auch speichert 
	 * @param pfadQuelle ist der Pfad zur Quelldatei eines Graphen im .gka Format.
	 */
	public FileHandler(String pfadQuelle) {

		if(!pfadQuelle.endsWith(".gka")){
			raiseError("Die angegebene Datei muss eine .gka Datei sein!");
		}
		fileparser = new FileParser(pfadQuelle);
		filesaver = new FileSaver(); 
		
		
		
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
	public void saveGraph(Collection<Edge> edgeCollection){
		if(edgeCollection==null){
			raiseError("Die übergebene Liste darf nicht null sein!");
		}
		
		filesaver.saveToFile(edgeCollection);
	}
	
}
