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
	public static int findeDateiBeginnMarker(int beginn, ArrayList<String> text) {
		while (beginn < text.size()) {
			int anfang = text.get(beginn).indexOf(DATEI_BEGINN_MARKER);
			if (anfang == -1) {
				beginn++;
				continue;
			}
			int ende = text.get(beginn).indexOf(DATEI_BEGINN_MARKER)
					+ DATEI_BEGINN_MARKER.length();
			String zeile = text.get(beginn).substring(anfang, ende);
			if (zeile.equals(DATEI_BEGINN_MARKER)) {
				return beginn;
			} else {
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
		int endeDateiBeginnMarker = text.get(beginn).indexOf(
				DATEI_BEGINN_MARKER)
				+ DATEI_BEGINN_MARKER.length();
		// Beginn der Suche nach DateiBeginnMarker.
		int anfangZeile1 = text.get(beginn).indexOf(DATEI_BEGINN_MARKER,
				endeDateiBeginnMarker);
		if (anfangZeile1 != -1) {
			int endeZeile1 = anfangZeile1 + DATEI_BEGINN_MARKER.length();
			String zutesten = text.get(beginn).substring(anfangZeile1,
					endeZeile1);
			if (zutesten.equals(DATEI_BEGINN_MARKER)) {
				JOptionPane.showMessageDialog(null,
						"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde. Zeile: "
								+ beginn);
				return 0;
			}
		}
		int anfangZeile11 = text.get(beginn).indexOf(DATEI_ENDE_MARKER);
		if (anfangZeile11 != -1) {
			int endeZeile11 = anfangZeile11 + DATEI_ENDE_MARKER.length();
			String test = text.get(beginn)
					.substring(anfangZeile11, endeZeile11);
			if (test.equals(DATEI_ENDE_MARKER)) {
				return beginn;
			}
		}
		beginn++;
		while (beginn < text.size()) {
			int anfangAktuelleZeile = text.get(beginn).indexOf(
					DATEI_BEGINN_MARKER);
			if (anfangAktuelleZeile != -1) {
				int endeAktuelleZeile = anfangAktuelleZeile
						+ DATEI_BEGINN_MARKER.length();
				String moeglicherstart = text.get(beginn).substring(
						anfangAktuelleZeile, endeAktuelleZeile);
				if (moeglicherstart.equals(DATEI_BEGINN_MARKER)) {
					JOptionPane.showMessageDialog(null,
							"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde. Zeile: "
									+ beginn);
					return 0;
				}
			}
			int anfangAktuelleZeile1 = text.get(beginn).indexOf(
					DATEI_ENDE_MARKER);
			if (anfangAktuelleZeile1 != -1) {
				int endeAktuelleZeile1 = anfangAktuelleZeile1
						+ DATEI_ENDE_MARKER.length();
				String zeile = text.get(beginn).substring(anfangAktuelleZeile1,
						endeAktuelleZeile1);
				if (zeile.equals(DATEI_ENDE_MARKER)) {
					return beginn;
				}
			}
			beginn++;
			continue;
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
			int anfang = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER);
			if (anfang == -1) {
				beginn++;
				continue;
			}
			int ende = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER)
					+ DATENSATZ_BEGINN_MARKER.length();
			String zeile = text.get(beginn).substring(anfang, ende);
			if (zeile.equals(DATENSATZ_BEGINN_MARKER)) {
				return true;
			} else {
				beginn++;
			}
		}
		return false;
	}

	public int findeDatensatzBeginnMarker(int beginn, ArrayList<String> text,
			String marker) {
		while (beginn < text.size() -1) {
			int anfang = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER);
			if (anfang == -1) {
				beginn++;
				continue;
			}
			int ende = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER)
					+ DATENSATZ_BEGINN_MARKER.length();
			String zeile = text.get(beginn).substring(anfang, ende);
			if (zeile.equals(DATENSATZ_BEGINN_MARKER)) {
				return beginn;
			} else {
				beginn++;
			}
		}
		return -1;

	}

	public static int findeDatensatzEndeMarker(int beginn,
			ArrayList<String> text) {
		// Ueberprueft die Zeile, in der bereits ein BeginnMarker gefunden
		// wurde, auf einen weiteren.
		int endeDateiBeginnMarker = text.get(beginn).indexOf(
				DATENSATZ_BEGINN_MARKER)
				+ DATENSATZ_BEGINN_MARKER.length();
		// Beginn der Suche nach DateiBeginnMarker.
		int anfangZeile1 = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER,
				endeDateiBeginnMarker);
		if (anfangZeile1 != -1) {
			int endeZeile1 = anfangZeile1 + DATENSATZ_BEGINN_MARKER.length();
			String zutesten = text.get(beginn).substring(anfangZeile1,
					endeZeile1);
			if (zutesten.equals(DATENSATZ_BEGINN_MARKER)) {
				JOptionPane.showMessageDialog(null,
						"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde. Zeile: "
								+ beginn);
				return 0;
			}
		}
		int anfangZeile11 = text.get(beginn).indexOf(DATENSATZ_ENDE_MARKER);
		if (anfangZeile11 != -1) {
			int endeZeile11 = anfangZeile11 + DATENSATZ_ENDE_MARKER.length();
			String test = text.get(beginn)
					.substring(anfangZeile11, endeZeile11);
			if (test.equals(DATENSATZ_ENDE_MARKER)) {
				return beginn;
			}
		}
		beginn++;
		while (beginn < text.size()) {
			int anfangAktuelleZeile = text.get(beginn).indexOf(
					DATENSATZ_BEGINN_MARKER);
			if (anfangAktuelleZeile != -1) {
				int endeAktuelleZeile = anfangAktuelleZeile
						+ DATENSATZ_BEGINN_MARKER.length();
				String moeglicherstart = text.get(beginn).substring(
						anfangAktuelleZeile, endeAktuelleZeile);
				if (moeglicherstart.equals(DATENSATZ_BEGINN_MARKER)) {
					JOptionPane.showMessageDialog(null,
							"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde. Zeile: "
									+ beginn);
					return 0;
				}
			}
			int anfangAktuelleZeile1 = text.get(beginn).indexOf(
					DATENSATZ_ENDE_MARKER);
			if (anfangAktuelleZeile1 != -1) {
				int endeAktuelleZeile1 = anfangAktuelleZeile1
						+ DATENSATZ_ENDE_MARKER.length();
				String zeile = text.get(beginn).substring(anfangAktuelleZeile1,
						endeAktuelleZeile1);
				if (zeile.equals(DATENSATZ_ENDE_MARKER)) {
					return beginn;
				}
			}
			beginn++;
			continue;
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