package dateihandler;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.MerkmalMissing;
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
			int datensatzBeginn = findeDatensatzBeginnMarker(aktuelleZeile,
					geleseneDaten, DATENSATZ_BEGINN_MARKER);
			int datensatzEnde = findeDatensatzEndeMarker(datensatzBeginn,
					geleseneDaten);
			werteDatensatzAus(aktuelleZeile, datensatzEnde, geleseneDaten);
			aktuelleZeile = datensatzEnde;
			if (aktuelleZeile == datensatzEnde) {
				aktuelleZeile += 1;
			}
		}
	}
	/**
	 * Findet einen DateiBeginnmarker.
	 * @param beginn
	 * @param text
	 * @param marker
	 * @return
	 */
	public static int findeDateiBeginnMarker(int beginn,
			ArrayList<String> text) {
		while (beginn < text.size()) {
			try {
				String test = text.get(beginn).substring(
						text.get(beginn).indexOf(DATEI_BEGINN_MARKER),
						text.get(beginn).indexOf(DATEI_BEGINN_MARKER) + DATEI_BEGINN_MARKER.length());
				if (test.equals(DATEI_BEGINN_MARKER)) {
					return beginn;
				} else {
					beginn++;
				}
			} catch (StringIndexOutOfBoundsException e) {
				beginn++;
			}
		}
		return -1;
	}
	
	/**
	 * Sucht nach einbem Datei ende Marker
	 * @param beginn
	 * @param text
	 * @param marker
	 * @return
	 */
	public static int findeDateiEndeMarker(int beginn, ArrayList<String> text) {
		// Ueberprueft die Zeile, in der bereits ein BeginnMarker gefunden
		// wurde, auf einen weiteren.
		int endeDateiBeginnMarker = text.get(beginn).indexOf(DATEI_BEGINN_MARKER)
				+ DATEI_BEGINN_MARKER.length();
			try {
			String zutesten = text.get(beginn).substring(
					text.get(beginn).indexOf(DATEI_BEGINN_MARKER, endeDateiBeginnMarker),
					text.get(beginn).indexOf(DATEI_BEGINN_MARKER, endeDateiBeginnMarker)
							+ DATEI_BEGINN_MARKER.length());
			if (zutesten.equals(DATEI_BEGINN_MARKER)) {
				JOptionPane
						.showMessageDialog(null,
								"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde. Zeile: "+beginn);
				return 0;
			}
		} catch (IndexOutOfBoundsException e) {
			try {
				String test = text.get(beginn).substring(
						text.get(beginn).indexOf(DATEI_ENDE_MARKER),
						text.get(beginn).indexOf(DATEI_ENDE_MARKER) + DATEI_ENDE_MARKER.length());
				if (test.equals(DATEI_ENDE_MARKER)) {
					return beginn;
				}
			} catch (StringIndexOutOfBoundsException f) {
				beginn++;
			}
		}
		while (beginn < text.size()) {
			try {
				String moeglicherstart = text.get(beginn).substring(
						text.get(beginn).indexOf(DATEI_BEGINN_MARKER),
						text.get(beginn).indexOf(DATEI_BEGINN_MARKER) + DATEI_BEGINN_MARKER.length());
				if (moeglicherstart.equals(DATEI_BEGINN_MARKER)) {
					JOptionPane
							.showMessageDialog(null,
									"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde. Zeile: "+beginn);
					return 0;
				}
			} catch (StringIndexOutOfBoundsException e) {
			}
			try {
				String test = text.get(beginn).substring(
						text.get(beginn).indexOf(DATEI_ENDE_MARKER),
						text.get(beginn).indexOf(DATEI_ENDE_MARKER) + DATEI_ENDE_MARKER.length());
				if (test.equals(DATEI_ENDE_MARKER)) {
					return beginn;
				}
			} catch (StringIndexOutOfBoundsException e) {
				beginn++;
			}
		}
		return beginn;
	}
	/**
	 * Liefert true zurück wenn noch ein Auswertbarer Datensatz vorhanden ist.
	 * @param beginn
	 * @param text
	 * @return
	 */
	public boolean DatensatzBeginnMarkerVorhanden(int beginn,
			ArrayList<String> text) {
		while (beginn < text.size()) {
			if (beginn < text.size()) {
				try {
					String test = text.get(beginn).substring(
							text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER),
							text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER)
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

	public static int findeDatensatzEndeMarker(int beginn, ArrayList<String> text) {
		// Ueberprueft die Zeile, in der bereits ein BeginnMarker gefunden
		// wurde, auf einen weiteren.
		int endeDateiBeginnMarker = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER)
				+ DATENSATZ_BEGINN_MARKER.length();
			try {
			String zutesten = text.get(beginn).substring(
					text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER, endeDateiBeginnMarker),
					text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER, endeDateiBeginnMarker)
							+ DATENSATZ_BEGINN_MARKER.length());
			if (zutesten.equals(DATENSATZ_BEGINN_MARKER)) {
				JOptionPane
						.showMessageDialog(null,
								"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde. Zeile: "+beginn);
				return 0;
			}
		} catch (IndexOutOfBoundsException e) {
			try {
				String test = text.get(beginn).substring(
						text.get(beginn).indexOf(DATENSATZ_ENDE_MARKER),
						text.get(beginn).indexOf(DATENSATZ_ENDE_MARKER) + DATENSATZ_ENDE_MARKER.length());
				if (test.equals(DATENSATZ_ENDE_MARKER)) {
					return beginn;
				}
			} catch (StringIndexOutOfBoundsException f) {
				beginn++;
			}
		}
		while (beginn < text.size()) {
			try {
				String moeglicherstart = text.get(beginn).substring(
						text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER),
						text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER) + DATENSATZ_BEGINN_MARKER.length());
				if (moeglicherstart.equals(DATENSATZ_BEGINN_MARKER)) {
					JOptionPane
							.showMessageDialog(null,
									"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde. Zeile: "+beginn);
					return 0;
				}
			} catch (StringIndexOutOfBoundsException e) {
			}
			try {
				String test = text.get(beginn).substring(
						text.get(beginn).indexOf(DATENSATZ_ENDE_MARKER),
						text.get(beginn).indexOf(DATENSATZ_ENDE_MARKER) + DATENSATZ_ENDE_MARKER.length());
				if (test.equals(DATENSATZ_ENDE_MARKER)) {
					return beginn;
				}
			} catch (StringIndexOutOfBoundsException e) {
				beginn++;
			}
		}
		return beginn;
	}

	public String getMerkmal(String wertBezeichner, String zeile)
			throws MerkmalMissing {
		int anfang = zeile.indexOf(MERKMAL_BEGINN + wertBezeichner, 0);
		if (anfang == -1){
			throw new MerkmalMissing();
		}
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
		try {
			int faktorDefault = 1;
			String nameOrtHerkunft = getMerkmal(BEZEICHNER_START, datensatz);
			String nameOrtZiel = getMerkmal(BEZEICHNER_ZIEL, datensatz);
			try {
				int faktor = Integer.parseInt(getMerkmal(BEZEICHNER_FAKTOR,
						datensatz));
				erzeugeFlugrouten(nameOrtHerkunft, nameOrtZiel, faktor);
			} catch (MerkmalMissing e) {
				erzeugeFlugrouten(nameOrtHerkunft, nameOrtZiel, faktorDefault);
			}
		} catch (MerkmalMissing e) {
			JOptionPane.showMessageDialog(null, "Datensatz fehlt Merkmal "+aktuelleZeile);
		} catch (NumberFormatException f){
			JOptionPane.showMessageDialog(null, "Merkmale enthalten keine Zahlen "+aktuelleZeile);
		}

	}
}