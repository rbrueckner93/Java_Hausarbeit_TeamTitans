package dateihandler;

import java.util.ArrayList;
import Main.Simulator;

/**
 * aufgrund eines ihr uebergebenen Simulators erstellt SimulationsdateiHandler
 * eine Datei, die Informationen ueber die durchgefuehrte Simulation enthaelt.
 */
public class SimulationsdateiHandler extends Datei {
	/* {author=TolleN} */

	public static final String DATEI_BEGINN_MARKER = ">->sim";

	public static final String DATEI_ENDE_MARKER = "<-<sim";

	public static final String DATENSATZ_BEGINN_MARKER = ">->route";

	public static final String DATENSATZ_ENDE_MARKER = "<-<route";

	public static final String DATEI_SUFFIX = "_sim";

	public static ArrayList<String> GUELTIGE_BEZEICHNER;

	public static final String BEZEICHNER_START = "start";

	public static final String BEZEICHNER_ZIEL = "end";
	
	public static final char REISELISTE_TEILER = ';';

	/**
	 * Aneinanderreihung aller Orte auf dem Weg der Flugroute
	 */
	public static final String BEZEICHNER_REISELISTE = "travellist";

	public static final String BEZEICHNER_FAKTOR = "factor";

	/**
	 * Uebergabe der Informationen, die durch den Simulator gesammelt wurden.
	 */
	public Simulator fertigeSimulation;
	
	/**
	 * Konstruktor. Bekommt ein fertiges Objekt der Klasse Simulator uebergeben.
	 * @param fertigeSimulation
	 */

	public SimulationsdateiHandler(Simulator fertigeSimulation) {
		super();
		this.fertigeSimulation = fertigeSimulation;
	}

	/**
	 * benutzt unter anderem erzeugeOrtsListe, um reiseListe zu erstellen.
	 */
	public String erstelleOutputStream() {
		return null;
	}

	public String erstelleDateiName() {
		return null;
	}

}