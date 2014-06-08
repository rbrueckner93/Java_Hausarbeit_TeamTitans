package dateihandler;

import java.util.ArrayList;

import simulation.Flugroute;
import simulation.Simulator;

/**
 * Erstellt aus einem uebergebenen Simulator eine Textdatei, 
 * die alle Flugrouten mit ihrer spez. Reiseliste enthaelt.
 * @author TolleN
 */
public class SimulationsdateiHandler extends Datei {
	/* {author=TolleN} */

	public static final String DATEI_BEGINN_MARKER = ">->sim";
	public static final String DATEI_ENDE_MARKER = "<-<sim";
	public static final String DATENSATZ_BEGINN_MARKER = ">->route";
	public static final String DATENSATZ_ENDE_MARKER = "<-<route";
	public static final String DATEI_SUFFIX = "_sim";
	public static final String BEZEICHNER_START = "start";
	public static final String BEZEICHNER_ZIEL = "end";
	public static final String BEZEICHNER_REISELISTE = "travellist";
	public static final String BEZEICHNER_FAKTOR = "factor";
	
	private Simulator fertigeSimulation;

	/**
	 * Konstruktor. Bekommt ein fertiges Objekt der Klasse Simulator uebergeben.
	 * @param fertigeSimulation
	 * @author Nils
	 */
	public SimulationsdateiHandler(Simulator fertigeSimulation) {
		super();
		this.fertigeSimulation = fertigeSimulation;
	}
	/**
	 * Methode erzeugt einen schreibbaren Stream und schreibt diesen in eine Datei.
	 */
	public void schreibeSimulationsDatei() {
		Datei.schreibeDatei(erstelleOutputStreamSim(), erstelleDateiNameSim());
	}

	/**
	 * Methode erzeugt einen AusgabeStream der den Regeln folgt. Benoetigt
	 * lediglich eine liste aller erstellten Flugrouten.
	 * @return ArrayList<String> Jedes Eelement entspricht einer Zeile.
	 */
	private ArrayList<String> erstelleOutputStreamSim() {
		ArrayList<String> fertigerText = new ArrayList<String>();
		fertigerText.add(DATEI_BEGINN_MARKER);
		fertigerText.add("");
		/*
		 * Durchlaeuft die Liste aller Flugrouten und schreibt die Merkmale.
		 */
		for (Flugroute aktuelleFlugroute : fertigeSimulation.getRouten()) {
			fertigerText.add(DATENSATZ_BEGINN_MARKER);
			fertigerText.add(MERKMAL_BEGINN + BEZEICHNER_START
					+ BEZEICHNER_WERT_TRENNER + aktuelleFlugroute.getHerkunft().getName()
					+ MERKMAL_ENDE);
			fertigerText.add(MERKMAL_BEGINN + BEZEICHNER_ZIEL
					+ BEZEICHNER_WERT_TRENNER + aktuelleFlugroute.getZiel().getName()
					+ MERKMAL_ENDE);
			fertigerText.add(MERKMAL_BEGINN+BEZEICHNER_REISELISTE+BEZEICHNER_WERT_TRENNER
					+ aktuelleFlugroute.erzeugeTextausgabeReiseroute()
					+ MERKMAL_ENDE);
			fertigerText.add(MERKMAL_BEGINN + BEZEICHNER_FAKTOR
					+ BEZEICHNER_WERT_TRENNER + aktuelleFlugroute.getFaktor()
					+ MERKMAL_ENDE);
			fertigerText.add(DATENSATZ_ENDE_MARKER);
		}
		fertigerText.add("");
		fertigerText.add(DATEI_ENDE_MARKER);
		return fertigerText;
	}
	
	/**
	 * Erstellt den fertigen Dateinamen für die Simulationsdatei.
	 * @return String aus Dateinamen + Suffix
	 */
	private String erstelleDateiNameSim() {
		String dateiname = fertigeSimulation.getNameTestdatei() + DATEI_SUFFIX;
		return dateiname;
	}

}