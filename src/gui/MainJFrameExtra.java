package gui;

import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import gka1gc.FileParser;

public class MainJFrameExtra {

	
	
	public static void main(String[] args) {
	
		JFrame frame = new JFrame("hallo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.add(new JTextArea("hallo"));
		frame.setVisible(true);
		Graph graph = new MultiGraph("hallo");
		graph.display();
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		HashSet<String> nodes = new HashSet<String>();
		
		
//		nodes = fp.getNodes();
		
		Thread thread =	new Thread(){
			
//			public HashSet<String> nodes;
			
			public void run(){
				
				HashSet<String> nodes;
				
				FileParser fp = new FileParser("H:\\Files\\Dropbox\\Dropbox\\Uni\\Semester 3\\GKA\\Hölings\\bspGraphen\\graph03.gka");
				fp.parsefile();
				
				nodes=fp.getNodes();
				
				for(String string : nodes){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					graph.addNode(string);
					
				}
				
				
				
			}
		};
		thread.start();
		
		
		
		
		
				
		
		
	}
	
}
