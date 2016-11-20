package application;

import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import algorithmen.Dijkstra;
import algorithmen.FloydWarshall;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import util.FileHandler;

public class GraphController implements Initializable {

	@FXML
	private TextArea logWindow = new TextArea();

	@FXML
	private TextField commandLine = new TextField();

	@FXML
	private Button enter = new Button();
	@FXML
	private MenuItem miOeffnen = new MenuItem();
	@FXML
	private MenuItem miClose = new MenuItem();
	
	@FXML
	private MenuItem miSpeichern = new MenuItem();
	

	private Graph graph;

	public static final String EdgeAttributeName = "Name";
	public static final String EdgeAttributeWeight = "Gewicht";
	public static final String NodeAttributVisited = "visited";
	public static final String NodeAttributdistance = "Distanz";
	private FileHandler fileHandler;
	protected static String stylesheet = "node {fill-color: black; size: 15px, 15px; stroke-mode: plain; stroke-color: blue;} node.marked{ fill-color: red;}node.start{fill-color: green;} node.shortest{ fill-color:green; }edge { fill-color: grey;} edge.shortest{fill-color: green; stroke-width:2px;}";

	public static String LOG_TAG_USER = "user";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		graph = new MultiGraph("test");
		graph.display();
		graph.addAttribute("ui.stylesheet", stylesheet);
		
		fileHandler = new FileHandler();

		// der listener für den button
		enter.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

			@Override
			public void handle(javafx.event.ActionEvent event) {
				String eingabe = commandLine.getText();

				log(LOG_TAG_USER+"> " + eingabe);

				commandLine.setText("");

				parseCommand(eingabe);

			}
		});

		// der lsitener für den Menüpunkt zum schliessen - geht noch nicht
		miClose.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				System.exit(0);
			}
		});

		//offnen action Listener
		miOeffnen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				openFile();
			}
		});
		
		//speichern unter actionlistener
		miSpeichern.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				saveFile();
				
			}
		});
		

	}

	private void addNode(String name) {

		if (graph.getNode(name) == null && name != "") {
			log("s> füge Knoten " + name + " hinzu");
			graph.addNode(name);
			graph.getNode(name).addAttribute("ui.label", name);
		}

	}

	private void addEdge(String node1, String node2) {

		
		graph.addEdge(node1+node2,node1, node2);
		
	}

	// hier wird auf bestimmte Befehle geprüft und die dann weiter geschliffen
	private void parseCommand(String eingabe) {

		if (eingabe != "") {

			if (eingabe.startsWith("addNode ")) {
				eingabe = eingabe.substring(8);
				addNode(eingabe);
			} else if (eingabe.startsWith("addEdge ")) {
				// hier bei " " splitten und dann rausfinden was da passieren
				// soll
				String[] temp = eingabe.split(" ");
				
				addEdge(temp[1], temp[2]);
				
			}else if(eingabe.startsWith("dijkstra ")){
			
				String[] temp = eingabe.split(" ");
				
				Dijkstra dijkstra = new Dijkstra(graph, temp[1], temp[2]);
				
				dijkstra.suche();
				
				//addEdge(temp[1], temp[2]);
			
			}else if(eingabe.startsWith("floyd")){
				//muss nochmal verbessert werden
				
				String[] temp = eingabe.split(" ");
				
				FloydWarshall fw = new FloydWarshall(graph, temp[1], temp[2]);
				
				
				
			}

		}

	}

	private void log(String message) {
		logWindow.appendText(message + "\n");
	}

	private void openFile() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Öffne ...");
		File file = fc.showOpenDialog(null);
		
		
		
		if(file != null){
//			initializeGraph();
			log(file.getAbsolutePath());
			fileHandler = new FileHandler(graph, file.getAbsolutePath());
		}
		
		//den Pfad dann an den fileparser der dann die datei ausliest
		
	}
	private void initializeGraph(){
		graph=new MultiGraph("graph");
	}
	
	
	private void saveFile(){
		FileChooser fc = new FileChooser();
		fc.setTitle("Speichern unter...");
		
		
				
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GKA Datein (*.gka)", "*.gka");
        fc.getExtensionFilters().add(extFilter);
		
        File file = fc.showSaveDialog(null);
		
		if(file!=null){
			
			Set temp = new HashSet();
			
			Iterator iterator = graph.getEdgeIterator();
			
			while (iterator.hasNext()) {
				Edge object = (Edge) iterator.next();
				temp.add(object);
				
			}
			
			
			System.out.println("edgeset size: "+temp.size());
			System.out.println("filepath: "+file.getAbsolutePath());
			fileHandler.saveGraph(temp, file);
			
		}
		
		
	}

}
