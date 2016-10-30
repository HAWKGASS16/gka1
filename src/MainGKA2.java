package gka1gc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class MainGKA2 extends JFrame{
	

	private static JTextArea logWindow;
	
	
	public static void main(String[] args) {
		
		//blah
		JFrame frame = new JFrame("GKAv2");
		frame.setSize(600, 600);
		
		
		JSplitPane splitPaneMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		
		Graph graph = new MultiGraph("ff");
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		viewer.enableAutoLayout();
//		Viewer viewer = graph.display();
		View view = viewer.addDefaultView(true);
//		View view = viewer.getDefaultView();
		graph.addNode("n1");
		graph.addNode("n2");
		graph.addEdge("ab","n1", "n2");
		//graph.display();
		
		frame.add((Component) view);
		JScrollPane scroller = new JScrollPane();
		
		//splitPaneMain.setLeftComponent(applet.getContentPane());
		splitPaneMain.setLeftComponent(scroller);
		
		logWindow = new JTextArea();
		logWindow.setEditable(false);
		JScrollPane logScollPane = new JScrollPane(logWindow);
		
		JSplitPane jSplitDown = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JTextField eingabeZeile = new JTextField(1);
		jSplitDown.setLeftComponent(logScollPane);
		
		JButton sendTextField = new JButton("send");
		
		JSplitPane textAndButton = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,eingabeZeile,sendTextField);
		
		jSplitDown.setRightComponent(textAndButton);
		
		jSplitDown.setDividerSize(1);
		
		
		
		splitPaneMain.setRightComponent(jSplitDown);
		
		
		sendTextField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//logWindow.append("\n"+eingabeZeile.getText());
				scanInput(eingabeZeile.getText());
				eingabeZeile.setText("");
				
			}
		});
		
		
		eingabeZeile.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()==KeyEvent.VK_ENTER){
					scanInput(eingabeZeile.getText());
					eingabeZeile.setText("");
					
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		frame.add(splitPaneMain);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//jSplitDown.setMinimumSize(new Dimension(50,50));
		jSplitDown.getRightComponent().setMinimumSize(new Dimension(30,30));
		//jSplitDown.getRightComponent().setMaximumSize(new Dimension(1024,40));
		jSplitDown.setResizeWeight(1.0);
		jSplitDown.setDividerLocation(0.9);
		splitPaneMain.setResizeWeight(1.0);
		splitPaneMain.setDividerLocation(0.7);
		splitPaneMain.setDividerSize(1);
		splitPaneMain.setEnabled(false);
		jSplitDown.setEnabled(false);
		textAndButton.setDividerLocation(0.8);
		textAndButton.setResizeWeight(1.0);
		textAndButton.setEnabled(false);
		textAndButton.setDividerSize(0);
		
		
		
		
		
	}
	
	
	/*
	private static void positionVertexAt( Object vertex, int x, int y ) {
        DefaultGraphCell cell = m_adapter.getVertexCell( vertex );
        System.out.println(m_adapter.getVertexCell(vertex).toString());
        
        Map              attr = cell.getAttributes(  );
        Rectangle2D        b    = GraphConstants.getBounds( attr );
        GraphConstants.setBounds( attr, new Rectangle( x, y, (int)b.getWidth(),(int) b.getHeight() ) );
        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        m_adapter.edit( cellAttr, null, null, null );
    }
    */

	public static void log(String zeile){
		logWindow.append(zeile+"\n");
	}
	public static void scanInput(String order){
		
		
		String temp[] = order.split(" ");
		if(Pattern.matches("addVertex.*", order)||Pattern.matches("addKante.*", order)){
			
			
			if(temp.length>=4){
				log("addVertex-->Name: \""+temp[1]+"\" at x="+temp[2]+" y="+temp[3]);
				//applet.addVertex(temp[1], Integer.parseInt(temp[2]), Integer.parseInt(temp[3]));
			}else{
				log("---ERROR--->addVertex benötigt mindestens 3 Parameter!");
			}
			//log(temp[0]+"<- 1 "+temp[1]+"<-2 "+temp[2]+"<-3 "+temp[3]+"<-4 ");
			
		}else if(Pattern.matches("addEdge.*", order)||Pattern.matches("addKnoten", order)){
			if(temp.length>=3){
				log("addEdge--->Edge zwischen: \""+temp[1]+"\" und "+temp[2]);
				//applet.addEdge(temp[1], temp[2]);
				
			}else{
				log("---ERROR--->addEdge benoetigt mindestens 2 Parameter!");
			}
			
		}
		
	}
	
}