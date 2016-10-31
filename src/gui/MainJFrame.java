package gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import com.sun.glass.events.KeyEvent;

import gka1gc.FileHandler;
import gka1gc.FileParser;

public class MainJFrame {

	private Graph graph;
	private JTextArea logWindow;
	private JTextField consoleInput;
	private long ANIMATION_SHORT = 200;
	private long ANIMATION_MIDDLE = 500;
	private long ANIMATION_LONG = 1000;
	private boolean animated = true;

	public MainJFrame() throws InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

				graph = new MultiGraph("Tutorial 1");

				String styleSheet = "node {\n" + "fill-color: black;\n" + "}\n" + "node.marked {\n" + "fill-color: red;"
						+ "size: 25px;\n" + "}";

				// graph.addNode("A");
				// graph.addNode("B");
				// graph.addNode("C");
				// graph.addNode("D");
				// graph.addNode("E");
				// graph.addNode("F");
				// graph.addEdge("AB", "A", "B");
				// graph.addEdge("BC", "B", "C");
				// graph.addEdge("CA", "C", "A");
				// graph.addEdge("CE", "C", "E", true);
				// graph.addEdge("EF", "E", "F", true);
				// graph.addEdge("AD", "A", "D");
				// graph.addAttribute("ui.stylesheet", styleSheet);
				// graph.getNode("A").addAttribute("ui.label", "A Node");
				// graph.getNode("A").addAttribute("ui.class", "marked");
				//
				//
				// graph.getNode("B").addAttribute("ui.clicked", "marked");
				graph.setAutoCreate(true);

				Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
				viewer.enableAutoLayout();
				View view = viewer.addDefaultView(false);/**/ // false indicates
																// "no JFrame".
				Component defaultView = viewer.getDefaultView();

				JFrame frame = new JFrame("Graph");

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(600, 600);

				JScrollPane graphPane = new JScrollPane(defaultView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

				JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

				logWindow = new JTextArea();

				JSplitPane consolePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				consolePane.setResizeWeight(1.0);
				consolePane.setDividerSize(2);

				consoleInput = new JTextField();
				consoleInput.addKeyListener(new KeyListener() {

					@Override
					public void keyPressed(java.awt.event.KeyEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void keyReleased(java.awt.event.KeyEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void keyTyped(java.awt.event.KeyEvent e) {
						// TODO Auto-generated method stub

						if (e.getKeyChar() == KeyEvent.VK_ENTER) {
							scanConsole(consoleInput.getText());
							consoleInput.setText("");

						}

					}
				});

				consolePane.setLeftComponent(new JScrollPane(logWindow));
				consolePane.setRightComponent(consoleInput);

				logWindow.setEditable(false);

				mainSplitPane.setLeftComponent(graphPane);
				mainSplitPane.setRightComponent(consolePane);
				mainSplitPane.setResizeWeight(1.0);
				mainSplitPane.setDividerSize(2);

				JMenuBar menuBar = new JMenuBar();
				JMenu menuNeu = new JMenu("Neu");
				menuNeu.setMnemonic(KeyEvent.VK_A);
				menuNeu.getAccessibleContext().setAccessibleDescription("test discription");

				JMenuItem itemNeu = new JMenuItem("Neuer leerer Graph");
				JMenuItem itemOeffnen = new JMenuItem("Öffnen");
				itemOeffnen.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						log("Öffnen geklickt");
						openFileMenu();

					}
				});

				JMenuItem itemBeenden = new JMenuItem("Beenden");
				itemBeenden.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);

					}
				});
				menuNeu.add(itemNeu);
				menuNeu.add(itemOeffnen);
				menuNeu.addSeparator();
				menuNeu.add(itemBeenden);

				JMenu menuOptionen = new JMenu("Optionen");
				JMenuItem itemSettings = new JMenuItem("Einstellungen");
				JMenuItem itemAbout = new JMenuItem("About");
				menuOptionen.add(itemSettings);
				menuOptionen.add(itemAbout);

				menuBar.add(menuNeu);
				menuBar.add(menuOptionen);

				frame.setJMenuBar(menuBar);

				frame.add(mainSplitPane);
				frame.setVisible(true);

				consolePane.setDividerLocation(0.9);
				mainSplitPane.setDividerLocation(0.7);

			}
		});

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			MainJFrame gh = new MainJFrame();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void log(String message) {
		logWindow.append(message + "\n");
	}

	private void openFileMenu() {
		JFileChooser chooser = new JFileChooser();

		chooser.setFileFilter(new FileNameExtensionFilter("GKA Dateien", "gka", "dot"));

		// chooser.s

		int rueckgabe = chooser.showOpenDialog(null);

		// wenn auf datei Öffnen geklickt wurde
		if (rueckgabe == JFileChooser.APPROVE_OPTION) {
			parseFile(chooser.getSelectedFile());
		} else {
			// hier wurde abbrechen gedrückt
		}
	}

	private void parseFile(File file) {
		log("Lade Datei: " + file.getAbsolutePath());

		FileHandler fh = new FileHandler(file);
		FileParser fp = fh.getFileParser();
		fp.parsefile();

		HashSet<String> nodes = fp.getNodes();

		if (nodes.size() != 0) {
			addNodes(nodes);
		}else{
			log("Datei "+file.getName()+" war leer oder das Parsen schlug fehl");
		}

	}

	private void addNodes(Collection<String> nodes) {
//		log("adde Nodes" + nodes);

		for (String string : nodes) {

			if (animated) {
				sleep(ANIMATION_LONG);
			}

			graph.addNode(string);

		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void scanConsole(String string) {
		// log(string);

		if (string.contains("parseFile")) {

		}

	}

}
