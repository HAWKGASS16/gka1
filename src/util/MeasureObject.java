package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import scala.collection.mutable.StringBuilder;

public class MeasureObject {

	private String vorgang;
	private String vorgangZeitpunkt;
	private String dateiNameZusatz;
	private ArrayList<String> typ;
	private ArrayList<Integer[]> typWerte;
	private ArrayList<String> sonstiges;
	private boolean writeToDisk = false;

	public MeasureObject() {
		// TODO Auto-generated constructor stub
	}

	public void startMeasure(String massnahme) {
		// z.b. Dijkstra von Husum nach Hannover:

		vorgang = massnahme;
		vorgangZeitpunkt = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss").format(new java.util.Date());
		dateiNameZusatz = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new java.util.Date());

		this.typ = new ArrayList<String>();
		this.typWerte = new ArrayList<Integer[]>();
		this.sonstiges=new ArrayList<String>();

	}

	public void read(String typ, int anzahl) {

		addTyp(typ);

		int indexOfTyp = this.typ.indexOf(typ);

		Integer[] wertAry = typWerte.get(indexOfTyp);
		int wert;

		wertAry[0] = wertAry[0] + 1;

	}

	public void write(String typ, int anzahl) {

		addTyp(typ);

		int indexOfTyp;
		int wert;

		indexOfTyp = this.typ.indexOf(typ);
		wert = ((Integer[]) typWerte.get(indexOfTyp))[1];
		typWerte.get(indexOfTyp)[1] = wert + 1;

	}

	public void stopMeasure() {

		ArrayList<String> stringB = new ArrayList<String>();

		stringB.add(vorgangZeitpunkt);
		
		stringB.add(vorgang);
		stringB.add(System.lineSeparator());
		
		stringB.addAll(sonstiges);
		
		stringB.add(System.lineSeparator());
		
		
		

		int gesamtLesen = 0;

		stringB.add("Lesende Zugriffe:");
		

		Integer[] ary;

		for (int i = 0; i <= typWerte.size() - 1; i++) {

			ary = typWerte.get(i);

			stringB.add(typ.get(i) + ":	" + ary[0]);
			
			gesamtLesen += ary[0];

		}
		stringB.add("Gesamt:			" + gesamtLesen);
		
		stringB.add(System.lineSeparator());

		int gesamtSchreiben = 0;
		stringB.add("Schreibende Zugriffe:");
		
		for (int i = 0; i <= typWerte.size() - 1; i++) {

			stringB.add(typ.get(i) + ":	" + typWerte.get(i)[1]);
			
			gesamtSchreiben += typWerte.get(i)[1];

		}
		stringB.add("Gesamt:			" + gesamtSchreiben);
		stringB.add(System.lineSeparator());
		stringB.add("Alle Zugriffe:		" + (gesamtLesen + gesamtSchreiben));
		
		stringB.add("Ende des Vorgangs");

//		System.out.println(stringB.toString());
		saveToDisk(stringB);

	}
	public void log(String message){
		sonstiges.add(message);
	}

	private void saveToDisk(ArrayList<String> outputString) {
		
		if(!writeToDisk){
			return;
		}
		
		Writer fw = null;
		String line = "";

		try {
			fw = new FileWriter(vorgang+ " " + dateiNameZusatz + ".gka");
			Iterator outputStringIterator = outputString.iterator();
			while (outputStringIterator.hasNext()) {
				line = (String) outputStringIterator.next();

				fw.write(line + "\n");
				fw.write(System.lineSeparator());

			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// TODO: handle finally clause
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void addTyp(String typName) {

		Integer[] ary = new Integer[2];
		ary[0] = 0;
		ary[1] = 0;

		if (!typ.contains(typName)) {
			typ.add(typName);
			typWerte.add(ary);

		}

	}

}
