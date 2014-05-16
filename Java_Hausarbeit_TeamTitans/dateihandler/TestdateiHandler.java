package dateihandler;

import java.io.File;
import java.util.ArrayList;

import Main.Simulator;

/**
 * erhaelt eine Datei (die Informationen ueber Flugrouten enthaelt), erhaelt ein
 * Objekt vom Typ Simulator. schreibt die in der Datei befindlichen
 * Informationen nach einer Pruefung durch eine methode pruefeDatei von
 * TestdateiHandler in die ArrayList routen des uebergebenen Simulator-Objektes.
 */
public class TestdateiHandler extends Datei {
	/* {author=TolleN} */

	public static final String DATEI_BEGINN_MARKER = ">->test";

	public static final String DATEI_ENDE_MARKER = "<-<test";

	public static final String DATENSATZ_BEGINN_MARKER = ">->route";

	public static final String DATENSATZ_ENDE_MARKER = "<-<route";

	public static ArrayList<String> GUELTIGE_BEZEICHNER;

	public static final String BEZEICHNER_START = "origin";

	public static final String BEZEICHNER_ZIEL = "destination";

	public static final String BEZEICHNER_FAKTOR = "factor";

	public File aktuelleTestdatei;

	public Simulator aktuelleSimulation;

	/**
	 * Konstruktor. Bekommt von der Mainmethode eine lesbare Testdatei vom typ
	 * File und ein Objekt der Klasse Simulator.
	 * 
	 * @param aktuelleTestdatei
	 * @param aktuelleSimulation
	 */

	public TestdateiHandler(File aktuelleTestdatei, Simulator aktuelleSimulation) {
		super();
		this.aktuelleTestdatei = aktuelleTestdatei;
		this.aktuelleSimulation = aktuelleSimulation;
	}

	public boolean pruefeDatei() {
		return false;
	}

	public void werteAus() {
	}

	public void erzeugeFlugrouten() {
	}

}