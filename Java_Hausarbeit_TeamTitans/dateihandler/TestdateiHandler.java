package dateihandler;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import orte.Ort;
import main.Flugroute;
import main.Karte;
import main.Simulator;

/**
 * @author TolleN Beschreibung erhaelt eine Datei (die Informationen ueber
 *         Flugrouten enthaelt), erhaelt ein Objekt vom Typ Simulator. schreibt
 *         die in der Datei befindlichen Informationen nach einer Pruefung durch
 *         eine methode pruefeDatei von TestdateiHandler in die ArrayList routen
 *         des uebergebenen Simulator-Objektes.
 */
public class TestdateiHandler extends Datei {
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
	public Karte aktuelleKarte;

	int aktuelleZeile = 0;

	/**
	 * Konstruktor. Bekommt von der Mainmethode eine lesbare Testdatei vom typ
	 * File und ein Objekt der Klasse Simulator.
	 * 
	 * @param aktuelleTestdatei
	 * @param aktuelleSimulation
	 */

	public TestdateiHandler(File aktuelleTestdatei,
			Simulator aktuelleSimulation, Karte aktuelleKarte) {
		super();
		this.aktuelleTestdatei = aktuelleTestdatei;
		this.aktuelleSimulation = aktuelleSimulation;
		this.aktuelleKarte = aktuelleKarte;
	}

	public void verarbeiteTestdatei() {
		ArrayList<String> geleseneDaten = Datei.leseDatei(aktuelleTestdatei);
		while (DatensatzBeginnMarkerVorhanden(aktuelleZeile, geleseneDaten)) {
			int datensatzbeginn = findeDatensatzBeginnMarker(aktuelleZeile,
					geleseneDaten, DATENSATZ_BEGINN_MARKER);
			int endeDatensatz = findeDatensatzEndeMarker(datensatzbeginn,
					geleseneDaten, DATENSATZ_ENDE_MARKER);
			werteDatensatzAus(aktuelleZeile, endeDatensatz, geleseneDaten);
			aktuelleZeile = endeDatensatz;
			if (aktuelleZeile == endeDatensatz) {
				aktuelleZeile += 1;
			}
		}
	}

	public boolean DatensatzBeginnMarkerVorhanden(int beginn,
			ArrayList<String> text) {
		while (beginn < text.size()) {
			int ueberpruefteZeile = beginn + 1;
			if (ueberpruefteZeile < text.size()) {
				try {
					String test = text.get(ueberpruefteZeile).substring(
							text.get(ueberpruefteZeile).indexOf(DATENSATZ_BEGINN_MARKER),
							text.get(ueberpruefteZeile).indexOf(DATENSATZ_BEGINN_MARKER)
									+ DATENSATZ_BEGINN_MARKER.length());
					if (test.equals(DATENSATZ_BEGINN_MARKER)) {
						return true;
					} else {
						beginn++;
					}
				} catch (StringIndexOutOfBoundsException e) {
					beginn++;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	public int findeDatensatzBeginnMarker(int beginn, ArrayList<String> text,
			String marker) {
		while (beginn < (text.size() - 1)) {
			try {
				String test = text.get(beginn).substring(
						text.get(beginn).indexOf(marker),
						text.get(beginn).indexOf(marker) + marker.length());
				if (test.equals(marker)) {
					return beginn;
				} else {
					beginn++;
				}
			} catch (StringIndexOutOfBoundsException e) {
				beginn++;
			}
		}
		return beginn;
	}

	public int findeDatensatzEndeMarker(int beginn, ArrayList<String> text,
			String marker) {
		while (beginn < (text.size() - 1)) {
			int zuueberpruefendeZeile = beginn + 1;
			try {
				String moeglicherstart = text.get(zuueberpruefendeZeile)
						.substring(
								text.get(zuueberpruefendeZeile).indexOf(
										DATENSATZ_BEGINN_MARKER),
								text.get(zuueberpruefendeZeile).indexOf(
										DATENSATZ_BEGINN_MARKER)
										+ DATENSATZ_BEGINN_MARKER.length());
				if (moeglicherstart.equals("DATENSATZ_BEGINN_MARKER")) {
					JOptionPane.showMessageDialog(null,
							"Start gefunden ohne Ende");
					return 0;
				}
			} catch (StringIndexOutOfBoundsException e) {
			}
			try {
				String test = text.get(beginn).substring(
						text.get(beginn).indexOf(marker),
						text.get(beginn).indexOf(marker) + marker.length());
				if (test.equals(marker)) {
					return beginn;
				} else {
					beginn++;
				}
			} catch (StringIndexOutOfBoundsException e) {
				beginn++;
			}
		}
		return beginn;
	}

	public String getMerkmal(String wertBezeichner, String zeile) {
		int anfang = zeile.indexOf(MERKMAL_BEGINN + wertBezeichner, 0);
		int ende = zeile.indexOf(MERKMAL_ENDE, anfang);
		String inhaltMerkmal = zeile.substring(anfang, ende);
		String[] merkmalsplit = inhaltMerkmal.split("\\"
				+ BEZEICHNER_WERT_TRENNER);
		if (merkmalsplit[0].equals(MERKMAL_BEGINN + wertBezeichner)) {
			return merkmalsplit[1];
		} else {
			JOptionPane.showMessageDialog(null, "Fehler im Merkmal "
					+ aktuelleZeile + wertBezeichner);
		}
		return null;
	}

	/**
	 * Erzeugt eine Flugroute.
	 * 
	 * @param nameHerkunft
	 * @param nameZiel
	 * @param faktor
	 */
	public void erzeugeFlugrouten(String nameHerkunft, String nameZiel,
			int faktor) {
		ArrayList<Ort> erstellteOrte = aktuelleKarte.orte;
		Ort ortHerkunft = null;
		Ort ortZiel = null;
		for (Ort gewaelterOrt : erstellteOrte) {
			if (gewaelterOrt.name.equals(nameHerkunft)) {
				ortHerkunft = gewaelterOrt;

			}
			if (gewaelterOrt.name.equals(nameZiel)) {
				ortZiel = gewaelterOrt;
			}
		}
		if (ortHerkunft == null || ortZiel == null) {
			JOptionPane.showMessageDialog(null,
					"Flugroute nicht erzeugbar - Ort nicht auf Karte");
		} else {
			aktuelleSimulation.routen.add(new Flugroute(ortZiel, ortHerkunft,
					faktor));
		}
	}

	public void werteDatensatzAus(int beginnZeile, int endeZeile,
			ArrayList<String> text) {
		// Erstellt einen zusammenhängenden String.
		String datensatz = "";
		for (int i = beginnZeile; i <= endeZeile; i++) {
			datensatz += text.get(i);
		}
		String nameOrtHerkunft = getMerkmal(BEZEICHNER_START, datensatz);
		String nameOrtZiel = getMerkmal(BEZEICHNER_ZIEL, datensatz);
		int faktor = Integer.parseInt(getMerkmal(BEZEICHNER_FAKTOR, datensatz));
		erzeugeFlugrouten(nameOrtHerkunft, nameOrtZiel, faktor);
	}
}