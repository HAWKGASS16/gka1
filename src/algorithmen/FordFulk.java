package algorithmen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import application.GraphController;
import util.MeasureObject;

public class FordFulk {

	private Graph graph;
	private final String attrEdgeFluss = "FordfulkersonKantenFluss";
	private final String attrEdgeGewicht = GraphController.EdgeAttributeWeight;
	private final String attrNodeVorgaenger = "FordFulkersonVorgaenger";
	private final String attrNodeInspiziert = "FordFulkersonInspiziert";
	private final String attrNodeDelta = "FordFulkersonFluss";
	private final String attrNodeFordFulk = "FordFulkersonAttribut";

	// BTS attribute
	private final String attNodeBTSMarkiert = GraphController.NodeAttributVisited;
	private final String attNodeBTSGewicht = "KnotenGewichtBTS";
	private final String attNodeBTSvorgaenger = "KnotenvorgaengerBTS";

	private HashSet<Node> markierteKnoten = new HashSet<Node>();
	private HashSet<Node> inspizierterKnoten = new HashSet<Node>();
	private Queue<Node> btsQueue = new LinkedList<Node>();

	private MeasureObject messObjekt = new MeasureObject();

	private boolean edmondsUndKarp = false;

	private Double doubleEpsilon = 0.05;

	private Double maxFluss = 0.0;
	private boolean senkeMarkiert = false;

	public FordFulk(Graph graph) {
		this.graph = graph;
	}

	public FordFulk(Graph graph, boolean modusEdmondsKarp) {
		this.graph = graph;
		edmondsUndKarp = modusEdmondsKarp;
	}

	private boolean initialisierung() {
		// jede kannte bekommt den initialfluss von 0

		for (Edge kante : graph.getEachEdge()) {

			messObjekt.read("initialisierung()", 1);
			messObjekt.write("initialisierung()", 1);

			kante.setAttribute(attrEdgeFluss, new Double(0.0));
			kantenLabel(kante, 0.0);

		}
		
		Object[] tempObjekt = {1, null,Double.POSITIVE_INFINITY};
		
		
		for (Node knoten : graph.getEachNode()) {
			messObjekt.read("initialisierung()", 1);
			messObjekt.write("initialisierung()", 2);
			
			knoten.setAttribute(attrNodeDelta, Double.POSITIVE_INFINITY);
//			knoten.setAttribute(attrNodeVorgaenger, null);
			knoten.setAttribute(attrNodeFordFulk, tempObjekt);
		}

		Node quelle = graph.getNode("q");
		Node senke = graph.getNode("s");
		
		messObjekt.read("initialisierung()", 2);

		if (quelle == null || senke == null) {
			return false;
		}

		quelle.setAttribute(attrNodeDelta, Double.POSITIVE_INFINITY);

		senkeMarkiert=false;
		
		messObjekt.write("initialisierung()", 1);

		
		markierteKnoten.clear();
		inspizierterKnoten.clear();
		markierteKnoten.add(quelle);

		if(edmondsUndKarp){
			breitensuche();
		}

		return true;
	}

	private void kantenLabel(Edge kante, double fluss) {
		messObjekt.read("kantenLabel()", 1);
		messObjekt.write("kantenLabel()", 1);

		Double kantengewicht = (Double) kante.getAttribute(attrEdgeGewicht);

		kante.setAttribute("ui.label", "" + fluss + " / " + kantengewicht);

	}

	public void maxflow() {

		if (edmondsUndKarp) {
			messObjekt.startMeasure("EdmondsKarp");
		} else {
			messObjekt.startMeasure("FordFulkerson");
		}

		Node senke = graph.getNode("s");

		// wenn die initialisierung erfolgreich war (impliziert das es eine
		// quelle namens 'q' und Senke namens 's' gibt), dann erst kann weiter
		// gemacht werden
		if (initialisierung()) {
System.out.println("initialisierung");

			// das ist falsch
//			while (!inspizierterKnoten.containsAll(markierteKnoten)) {
int i = 0;
			while (!alleSindInspiziert()) {
				

				Node knotenI = getNextKnoten();

				inspiziere(knotenI);

//				Node senkenVorgaenger = (Node) senke.getAttribute(attrNodeVorgaenger);

				// wenn die Senke markiert ist
				if (senkeMarkiert) {
					vergroessereWeg();
					i++;

					if (edmondsUndKarp) {
						breitensuche();
					}
				}

			}
System.out.println("am ende der schleife");

		}
		messObjekt.stopMeasure();

	}

	private void vergroessereWeg() {
System.out.println("vergroessere Weg");
		String methodenname = "vergroessereWeg()";

		Node quelle = graph.getNode("q");
		Node senke = graph.getNode("s");
		Double deltaSenke = getDelta(senke);
		maxFluss+=deltaSenke;
		
		messObjekt.read(methodenname, 2);


		
		Node knotenMomentan = senke;
		Node knotenMomentanVorgaenger;
System.out.println("Delta an der Senke: "+deltaSenke);		
		do{
			knotenMomentanVorgaenger=getVorgaenger(knotenMomentan);
			Edge kanteMomentan = knotenMomentanVorgaenger.getEdgeBetween(knotenMomentan);
//			Edge kanteMomentan = knotenMomentan.getEdgeBetween(knotenMomentanVorgaenger);
			messObjekt.read(methodenname, 3);
			messObjekt.write(methodenname, 1);
			
System.out.println("----------------------------------------");
System.out.println("KnotenMomentan: "+knotenMomentan.getId());
System.out.println("KnotenVorgaenger: "+knotenMomentanVorgaenger.getId());
			
			if(kanteMomentan.getSourceNode()==knotenMomentanVorgaenger){
				//kante ist eine vorwätskante
				
				kanteMomentan.setAttribute(attrEdgeFluss, fluss(kanteMomentan)+deltaSenke);
				kantenLabel(kanteMomentan, fluss(kanteMomentan));
				
			}else{
				Double kantenfluss = fluss(kanteMomentan);
				kanteMomentan.setAttribute(attrEdgeFluss, (kantenfluss-deltaSenke));
				kantenLabel(kanteMomentan, (kantenfluss-deltaSenke));
			}


			knotenMomentan=knotenMomentanVorgaenger;

		}while(knotenMomentanVorgaenger!=quelle);
		

//		senke.setAttribute(attrNodeVorgaenger, null);
		//setzt die vorgänger und deltas zurück, sowie leert die listen für markierte
		//und inspiierte knoten
		reset();
				
		markierteKnoten.add(quelle);
		

	}

	/**
	 * Die vorgänger aller Knoten und die Deltas aller Knoten zurücksetzten damit ein neuer
	 * Vergrößender weg gefunden werden kann
	 */
	private void reset() {
		String methodenname = "reset()";
		
		
		
		for(Node knoten : graph.getEachNode()){
			messObjekt.read(methodenname, 1);
//			setVorgaenger(knoten, null);
			setDelta(knoten, Double.POSITIVE_INFINITY);
		}
		senkeMarkiert=false;
		markierteKnoten.clear();
		inspizierterKnoten.clear();
		btsQueue.clear();
		
		
	}

	private Node getNextKnoten() {

		System.out.println("get nextKnoten");
		
		if (edmondsUndKarp) {
			return btsQueue.poll();
		}

		ArrayList<Node> temp = new ArrayList<Node>();

		temp.addAll(markierteKnoten);
		temp.removeAll(inspizierterKnoten);

		int randomNum = ThreadLocalRandom.current().nextInt(0, temp.size());

		Node zufallsKnoten = temp.get(randomNum);
		System.out.println("name des neuen Knotens: "+zufallsKnoten.getId());
		
		return zufallsKnoten;

	}

	private void inspiziere(Node knotenI) {
		System.out.println("\tinspiziere: "+knotenI.getId());

		for (Edge kante : knotenI.getEachEdge()) {
			messObjekt.read("inspiziere()", 2);
			Node knotenJ = kante.getOpposite(knotenI);
			if (markierteKnoten.contains(knotenJ)) {
				continue;
			}
			
			//Damit sich nur die kanten auf dem kürzesten weg angesehen werden, bzw die kanten zwischen den Knoten auf dem
			//kürzesten weg
			Node naechterInDerQueue = btsQueue.peek();
			if(edmondsUndKarp && !(knotenI.getEdgeBetween(naechterInDerQueue)==kante)){
				messObjekt.read("inspiziere()", 1);
				continue;
			}
			
			
System.out.println("KnotenJ = "+knotenJ.getId());

			Double deltaI = getDelta(knotenI);
			Double deltaJ = getDelta(knotenJ);
			Double fluss = 0.0;
			
System.out.println("deltaI: "+deltaI);
System.out.println("DeltaJ: "+deltaJ);


			if (kante.getSourceNode().equals(knotenI) && (!satoriert(kante))) {
				messObjekt.read("inspiziere()", 1);
				// dann ist die Kante eine ausgehende Kante

//				knotenJ.setAttribute(attrNodeVorgaenger, knotenI);
				setVorgaenger(knotenJ, knotenI);

				fluss = Math.min(deltaJ, (kapazitaet(kante) - fluss(kante)));
				setDelta(knotenJ, fluss);

				markierteKnoten.add(knotenJ);
				
				
				
//TODO hier nachbessern und rückwärtskanten besser berechnen..
			} else if (kante.getSourceNode().equals(knotenJ) && (fluss(kante) > 0)) {
				
				messObjekt.read("inspiziere()", 1);
				
				// Kante ist eine eingehende Kante

				setVorgaenger(knotenJ, knotenI);
				
				fluss= Math.min(fluss(kante)-kapazitaet(kante), deltaJ);
				setDelta(knotenJ, fluss);
				markierteKnoten.add(knotenJ);
				
				
			}
System.out.println("neues Delta: "+fluss);			
			if(knotenJ.equals(graph.getNode("s"))){
				messObjekt.read("inspiziere()", 1);
				senkeMarkiert=true;
			}

		}
		inspizierterKnoten.add(knotenI);

	}

	private Double fluss(Edge kante) {
		messObjekt.read("fluss(Kante)", 1);

		return (Double) kante.getAttribute(attrEdgeFluss);
	}

	private Double kapazitaet(Edge kante) {
		messObjekt.read("kapazitaet(Kante)", 1);
		return (Double) kante.getAttribute(attrEdgeGewicht);

	}

	private boolean satoriert(Edge kante) {
		messObjekt.read("satoriert()", 2);

		Double fluss = (Double) kante.getAttribute(attrEdgeFluss);
		Double kapazitaet = (Double) kante.getAttribute(attrEdgeGewicht);

		if ((kapazitaet - fluss) < doubleEpsilon) {
			return true;
		}

		return false;
	}

	private void breitensuche() {
		// initialisierung
		messObjekt.read("breitensuche()", 2);

System.out.println("breitensuche");
		initBTS();
		Node quelle = graph.getNode("q");
		Node senke = graph.getNode("s");

		
		// eigentliche BTS
		Queue<Node> btsTemp = new LinkedList<Node>();

		btsTemp.add(quelle);

		while (!btsTemp.isEmpty()) {

			Node tempKnoten = btsTemp.poll();
System.out.println("Breitensuche: momentan untersuchter Knoten: "+tempKnoten.getId());
			int tempKnotenGewicht = (int) tempKnoten.getAttribute(attNodeBTSGewicht);

			for (Edge kante : tempKnoten.getEachEdge()) {
				messObjekt.read("breitensuche()", 1);
				// wenn die kannte voll ist, oder die Quelle der Kante nicht der
				// momentane Knoten ist
				if (satoriert(kante) || kante.getTargetNode().equals(tempKnoten)) {
					continue;
				}

				Node tempNachbar = kante.getOpposite(tempKnoten);
				boolean markiert = (boolean) tempNachbar.getAttribute(attNodeBTSMarkiert);

				if (!markiert) {

					int gewichtNachbar = (int) tempNachbar.getAttribute(attNodeBTSGewicht);
					if ((tempKnotenGewicht + 1) < gewichtNachbar) {
						
						messObjekt.write("breitensuche()", 2);
						messObjekt.read("breitensuche()", 1);

						tempNachbar.setAttribute(attNodeBTSvorgaenger, tempKnoten);
						tempNachbar.setAttribute(attNodeBTSGewicht, tempKnotenGewicht + 1);

					}
					if(!btsTemp.contains(tempNachbar)){
						btsTemp.add(tempNachbar);
					}

				}

			}
			tempKnoten.setAttribute(attNodeBTSMarkiert, true);
			messObjekt.write("breitensuche()", 1);
			messObjekt.read("breitensuche()", 3);
		}
		// ab hier ist das errechnen der wenigsten hops beendet und es kann der
		// kürzeste weg berechnet werden

		Node aktuellFuerKuerzestenWeg = graph.getNode("s");
		messObjekt.read("breitensuche()", 1);
System.out.println("aktuellFuerKuerzestenWeg: "+aktuellFuerKuerzestenWeg.getId());
System.out.println("quelle: "+quelle.getId());
		ArrayList<Node> weg = new ArrayList<Node>();
		while(aktuellFuerKuerzestenWeg!=quelle) {
System.out.println("groesse der queue: "+weg.size());
			weg.add(aktuellFuerKuerzestenWeg);
			aktuellFuerKuerzestenWeg = (Node) aktuellFuerKuerzestenWeg.getAttribute(attNodeBTSvorgaenger);
		}

		btsQueue.add(quelle);
		for (int i = weg.size() - 1; i >= 0; i--) {
			System.out.println("kuerzester weg ist: "+weg.get(i).getId());
			btsQueue.add(weg.get(i));
		}
		

	}
	private void initBTS() {
		System.out.println("breitensuche init");
		Node quelle = graph.getNode("q");


		for (Node knoten : graph.getEachNode()) {

			knoten.setAttribute(attNodeBTSMarkiert, false);
			knoten.setAttribute(attNodeBTSGewicht, Integer.MAX_VALUE);

		}
		quelle.setAttribute(attNodeBTSMarkiert, true);
		
	}

	private boolean alleSindInspiziert(){
		if(markierteKnoten.size()!=inspizierterKnoten.size()){
			return false;
		}else if(markierteKnoten.containsAll(inspizierterKnoten)){
			return true;
		}
		return false;
	}
	//------------------------------------------------------------------------------------
	private Node getVorgaenger(Node knoten){

		messObjekt.read("getVorgaenger(Knoten)", 1);
		Node tmp = (Node)knoten.getAttribute(attrNodeVorgaenger);
System.out.println("vorgaenger von knoten: "+knoten.getId());
System.out.println("ist: "+tmp.getId());
		return tmp;
		
	}
	private void setVorgaenger(Node knoten, Node vorgaenger){
		messObjekt.write("setVorgaenger(Knoten, Vorgaenger)", 1);
System.out.println("setze vorgaenger von: "+knoten.getId()+" zu: "+vorgaenger.getId());
		
		knoten.setAttribute(attrNodeVorgaenger, vorgaenger);
		
		
	}
	private Double getDelta(Node knoten){
		messObjekt.read("getDelta(Node)", 1);
		Object[] temp = (Object[]) knoten.getAttribute(attrNodeFordFulk);
		return (Double)temp[2];
		
	}
	private void setDelta(Node knoten, Double delta){
		messObjekt.write("setDelta(Knoten, Delta)", 1);
		Object[] temp = (Object[]) knoten.getAttribute(attrNodeFordFulk);
		temp[2]=delta;
	}
	public Double getMaxflow(){
		return maxFluss;
	}
	
}
