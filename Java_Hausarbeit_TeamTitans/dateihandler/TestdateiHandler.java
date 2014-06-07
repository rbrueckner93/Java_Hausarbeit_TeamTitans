package dateihandler;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import netz.Karte;
import exceptions.DateiSyntaxFehler;
import exceptions.MerkmalMissing;
import orte.Ort;
import simulation.Flugroute;
import simulation.Simulator;

/**
 *Erhaelt eine Datei (die Informationen ueber
 *Flugrouten enthaelt), erhaelt ein Objekt vom Typ Simulator. schreibt
 *die in der Datei befindlichen Informationen nach einer Pruefung durch
 *eine methode pruefeDatei von TestdateiHandler in die ArrayList routen
 *des uebergebenen Simulator-Objektes.
 *@author TolleN
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
	public static final int DEFAULT_FAKTOR = 1;
	private File aktuelleTestdatei;
	private Simulator aktuelleSimulation;
	private Karte aktuelleKarte;

	private int aktuelleZeile = 0;

	private int ausgewerteteDatensaetze = 0;

	private int mitFaktorDefault = 0;

	/**
	 * Konstruktor. Bekommt von der Mainmethode eine lesbare Testdatei vom typ
	 * File und ein Objekt der Klasse Simulator.
	 * 
	 * @param aktuelleTestdatei Testdatei die ausgewertet wird
	 * @param aktuelleSimulation Simulation, die Routen eingeschrieben bekommt.
	 * @param aktuelleKarte Karte, auf der die Simulation ausgefuehrt wird.
	 */

	public TestdateiHandler(File aktuelleTestdatei,
			Simulator aktuelleSimulation, Karte aktuelleKarte) {
		super();
		this.aktuelleTestdatei = aktuelleTestdatei;
		this.aktuelleSimulation = aktuelleSimulation;
		this.aktuelleKarte = aktuelleKarte;
	}

	public void verarbeiteTestdatei() throws DateiSyntaxFehler {
		ausgewerteteDatensaetze = 0;
		mitFaktorDefault = 0;
		// Einlesen der Datei. Liefert pro Zeile ein StringObjekt im Array.
		ArrayList<String> geleseneDaten = Datei.leseDatei(aktuelleTestdatei);
		aktuelleSimulation.setNameTestdatei(getDateiNamen(aktuelleTestdatei));
		// Checken ob Marker vorhanden.
		int dateiAnfang = findeDateiBeginnMarker(aktuelleZeile, geleseneDaten);
		if (dateiAnfang == -1) {
			JOptionPane
					.showMessageDialog(null, "Fehlender Datei Beginn Marker");
			throw new DateiSyntaxFehler();
		}
		int dateiEnde = findeDateiEndeMarker(dateiAnfang, geleseneDaten);
		if (dateiEnde == -1) {
			JOptionPane.showMessageDialog(null, "Fehlender Datei Ende Marker");
			throw new DateiSyntaxFehler();
		}
		checkLeeresDateiende(dateiEnde, geleseneDaten);
		if (!datensatzBeginnMarkerVorhanden(aktuelleZeile, geleseneDaten)) {
			JOptionPane.showMessageDialog(null,
					"Kein auswertbarer Datensatz in der Datei gefunden");
			throw new DateiSyntaxFehler();
		}
		if (!datensatzMarkiererGleichwertig(dateiAnfang, geleseneDaten)) {
			JOptionPane
					.showMessageDialog(
							null,
							"Achtung! - Es fehlen Datensatzmarkierer zur korrekten Auswertung der Datei.\nOder es stehen 2 identsiche Marker in einer Zeile.");
		}
		// Eigentliche Auswertung des gefundenen Datensatzes.
		while (datensatzBeginnMarkerVorhanden(aktuelleZeile, geleseneDaten)
				&& aktuelleZeile < dateiEnde) {
			int datensatzBeginn = findeDatensatzBeginnMarker(aktuelleZeile,
					geleseneDaten);
			int datensatzEnde = findeDatensatzEndeMarker(datensatzBeginn,
					geleseneDaten);
			aktuelleZeile = datensatzBeginn;
			werteDatensatzAus(datensatzBeginn, datensatzEnde, geleseneDaten);
			aktuelleZeile = datensatzEnde;
			if (aktuelleZeile == datensatzEnde) {
				aktuelleZeile += 1;
			}
		}
		JOptionPane.showMessageDialog(null, "Es wurde/en "
				+ ausgewerteteDatensaetze
				+ " Datensatz/Datensaetze erfolgreich eingelesen. \n" + mitFaktorDefault
				+ " davon mit Faktor 1");
	}

	/**
	 * Findet einen DateiBeginnmarker.
	 * 
	 * @param beginn
	 * @param text
	 * @param marker
	 * @return
	 */
	private  int findeDateiBeginnMarker(int beginn, ArrayList<String> text) {
		while (beginn < text.size()) {
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
			int anfang = text.get(beginn).indexOf(DATEI_BEGINN_MARKER);
			if (anfang == -1) {
				beginn++;
				continue;
			}
			int ende = text.get(beginn).indexOf(DATEI_BEGINN_MARKER)
					+ DATEI_BEGINN_MARKER.length();
			if (anfang != 0){
				return -1;
			}
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
	 * 
	 * @param beginn
	 * @param text
	 * @param marker
	 * @return
	 */
	private int findeDateiEndeMarker(int beginn, ArrayList<String> text)
			throws DateiSyntaxFehler {
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
				JOptionPane
						.showMessageDialog(
								null,
								"Weiterer Datei Beginn Marker gefunden. In Zeile: "
										+ (beginn + 1));
				throw new DateiSyntaxFehler();
			}
		}
		int anfangZeile11 = text.get(beginn).indexOf(DATEI_ENDE_MARKER);
		if (anfangZeile11 != -1) {
			int endeZeile11 = anfangZeile11 + DATEI_ENDE_MARKER.length();
			if (anfangZeile11 != 0){
				return -1;
			}
			String test = text.get(beginn)
					.substring(anfangZeile11, endeZeile11);
			if (test.equals(DATEI_ENDE_MARKER)) {
				return beginn;
			}
		}
		beginn++;
		while (beginn < text.size()) {
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
			int anfangAktuelleZeile = text.get(beginn).indexOf(
					DATEI_BEGINN_MARKER);
			if (anfangAktuelleZeile != -1) {
				int endeAktuelleZeile = anfangAktuelleZeile
						+ DATEI_BEGINN_MARKER.length();
				String moeglicherstart = text.get(beginn).substring(
						anfangAktuelleZeile, endeAktuelleZeile);
				if (moeglicherstart.equals(DATEI_BEGINN_MARKER)) {
					JOptionPane
							.showMessageDialog(
									null,
									"Weiterer Datei Beginn Marker gefunden. In Zeile: "
											+ (beginn + 1));
					throw new DateiSyntaxFehler();
				}
			}
			int anfangAktuelleZeile1 = text.get(beginn).indexOf(
					DATEI_ENDE_MARKER);
			if (anfangAktuelleZeile1 != -1) {
				int endeAktuelleZeile1 = anfangAktuelleZeile1
						+ DATEI_ENDE_MARKER.length();
				if (anfangAktuelleZeile1 != 0){
					return -1;
				}
				String zeile = text.get(beginn).substring(anfangAktuelleZeile1,
						endeAktuelleZeile1);
				if (zeile.equals(DATEI_ENDE_MARKER)) {
					return beginn;
				}
			}
			beginn++;
			continue;
		}
		return -1;
	}
	
	/**
	 * Guckt, ob die Anzahl gestarteter und geschlossener Datensaetze identsich ist.
	 * @param startZeile
	 * @param text Text der ausgewertet werden soll
	 * @return true oder false 
	 */
	private boolean datensatzMarkiererGleichwertig(int startZeile,
			ArrayList<String> text) {
		int anzahlDatensatzBeginnMarker = 0;
		int anzahlDatensatzEndeMarker = 0;
		int durchlaufBeginnMarker = startZeile;
		int durchlaufEndeMarker = startZeile;
		while (durchlaufBeginnMarker < text.size()) {
			int datensatzbeginn = text.get(durchlaufBeginnMarker).indexOf(
					DATENSATZ_BEGINN_MARKER);
			if (datensatzbeginn == -1) {
				durchlaufBeginnMarker++;
				continue;
			}
			if (datensatzbeginn >= 0) {
				anzahlDatensatzBeginnMarker++;
				durchlaufBeginnMarker++;
			}
		}
		while (durchlaufEndeMarker < text.size()) {
			int datensatzEnde = text.get(durchlaufEndeMarker).indexOf(
					DATENSATZ_ENDE_MARKER);
			if (datensatzEnde == -1) {
				durchlaufEndeMarker++;
				continue;
			}
			if (datensatzEnde >= 0) {
				anzahlDatensatzEndeMarker++;
				durchlaufEndeMarker++;
			}
		}
		if (anzahlDatensatzBeginnMarker == anzahlDatensatzEndeMarker) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Liefert true zur�ck wenn noch ein auswertbarer Datensatz vorhanden ist.
	 * 
	 * @param beginn
	 * @param text
	 * @return
	 */
	private boolean datensatzBeginnMarkerVorhanden(int beginn,
			ArrayList<String> text) {
		while (beginn < text.size()) {
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
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

	/**
	 * Findet die Zeile mit einem Datensatzbeginnmarker. Beruecksichtigt moegliche Datensatzendemarker.
	 * @param beginn Zeile, ab der gesucht werden soll.
	 * @param text Text, der ausgewertet werden soll.
	 * @return Zeile mit Fund.
	 */
	private int findeDatensatzBeginnMarker(int beginn, ArrayList<String> text) {
		while (beginn < text.size() - 1) {
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
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

	/**
	 * Findet die Zeile, in der ein Datensatzendemarker steht. Beachtet auftretende Beginnmarker.
	 * @param beginn Zeile, ab der gesucht werden soll.
	 * @param text Text, der ausgewertet werden soll.
	 * @return Zeile mit Befund.
	 */
	private int findeDatensatzEndeMarker(int beginn,
			ArrayList<String> text) throws DateiSyntaxFehler {
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
				JOptionPane
						.showMessageDialog(
								null,
								"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde.  In Datensatz ab Zeile: "
										+ (beginn + 1));
				throw new DateiSyntaxFehler();
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
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
			int anfangAktuelleZeile = text.get(beginn).indexOf(
					DATENSATZ_BEGINN_MARKER);
			if (anfangAktuelleZeile != -1) {
				int endeAktuelleZeile = anfangAktuelleZeile
						+ DATENSATZ_BEGINN_MARKER.length();
				String moeglicherstart = text.get(beginn).substring(
						anfangAktuelleZeile, endeAktuelleZeile);
				if (moeglicherstart.equals(DATENSATZ_BEGINN_MARKER)) {
					JOptionPane
							.showMessageDialog(
									null,
									"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde.  In Datensatz ab Zeile: "
											+ (beginn + 1));
					throw new DateiSyntaxFehler();
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
	
	/**
	 * * Wertet ein Datensatz nach einem spez. Merkmal aus.
	 * 
	 * @param wertBezeichner
	 *            Spezifischer Bezeichner des Wertes.
	 * @param zeile
	 *            String in dem Merkmal stehen muss. Muss der komplette
	 *            Datensatz sein.
	 * @return Wert des gesuchten Merkmals als String
	 * @throws MerkmalMissing
	 *             Fehler bei fehlendem oder defektem merkmal
	 * @throws DateiSyntaxFehler
	 */
	private String getMerkmal(String wertBezeichner, String zeile)
			throws MerkmalMissing, DateiSyntaxFehler {
		// Index des Merkmalbeginns. Check mit "[" vorran gestellt um
		// Leerzeichen dazwischen auszuschliessen.
		int anfang = zeile.indexOf(MERKMAL_BEGINN + wertBezeichner, 0);
		// Check ob Merkmal gefunden wurde.
		if (anfang == -1) {
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		// Index des Ende des Merkmals
		int ende = zeile.indexOf(MERKMAL_ENDE, anfang);
		// Check, ob das Merkmal einzigartig im Datensatz ist
		if (zeile.indexOf(MERKMAL_BEGINN + wertBezeichner, ende) != -1) {
			JOptionPane.showMessageDialog(null, "Merkmal \"" + wertBezeichner
					+ "\" mehrfach im Datensatz ab Zeile "
					+ (aktuelleZeile + 1) + " vorhanden.");
			throw new DateiSyntaxFehler();
		}
		// Extrahieren des spezifischen Merkmals
		String inhaltMerkmal = zeile.substring(anfang, ende);
		// Ausplitten des Merkmals in Bezeichner und Wert
		String[] merkmalsplit = inhaltMerkmal.split("\\"
				+ BEZEICHNER_WERT_TRENNER);
		// Check ob Merkmal Inhalt besitzt
		if (merkmalsplit.length == 1) {
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		if (merkmalsplit[1].isEmpty()) {
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		// Check auf Korrektes Merkmal und anschlie�ende R�ckgabe.
		if (merkmalsplit[0].equals(MERKMAL_BEGINN + wertBezeichner)) {
			return merkmalsplit[1].trim();
		} else {
			JOptionPane.showMessageDialog(null, "Fehler im Merkmal "
					+ wertBezeichner + " in Datensatz der in Zeile "
					+ (aktuelleZeile + 1) + " beginnt");
		}
		return null;
	}

	/**
	 * Erzeugt eine Flugroute.
	 * Ueberprueft dabei die existenz der Orte und den Faktor.
	 * @param nameHerkunft Ort des Abflugs
	 * @param nameZiel Ort des Ziels
	 * @param faktor Haeufigkeit der Nutzung
	 */
	private void erzeugeFlugrouten(String nameHerkunft, String nameZiel,
			int faktor) {
		ArrayList<Ort> erstellteOrte = aktuelleKarte.getListeAllerOrte();
		Ort ortHerkunft = null;
		Ort ortZiel = null;
		//Auswahl welche Ort Ziel welcher Ort Start ist.
		for (Ort gewaelterOrt : erstellteOrte) {
			if (gewaelterOrt.getName().equals(nameHerkunft)) {
				ortHerkunft = gewaelterOrt;

			}
			if (gewaelterOrt.getName().equals(nameZiel)) {
				ortZiel = gewaelterOrt;
			}
		}
		//Check, ob Orte der zuerstellenden Flugroute auf Karte vorhanden sind.
		if (ortHerkunft == null) {
			JOptionPane.showMessageDialog(null, "Flugroute von " + nameHerkunft
					+ " nach " + nameZiel + " nicht erzeugbar - "
					+ nameHerkunft + " nicht auf Karte");
		} else if (ortZiel == null) {
			JOptionPane.showMessageDialog(null, "Flugroute von " + nameHerkunft
					+ " nach " + nameZiel + " nicht erzeugbar - " + nameZiel
					+ " nicht auf Karte");
		} else {
			//Erstellung der Flugroute und adden zur Liste im aktuellen Simulator.
			aktuelleSimulation.getRouten().add(new Flugroute(ortZiel, ortHerkunft,
					faktor));
		}
	}

	/**
	 * Wertet einen Datensatz nach allen Merkmalen aus und erstellt dann ein Objekt anhand der Daten.
	 * @param beginnZeile Beginn des Datensatzes
	 * @param endeZeile Ende des Datensatzes
	 * @param text Text in dem der Datensatz steht
	 * @throws DateiSyntaxFehler Fehler der auftritt, wenn der Syntax verletzt wurde.
	 */
	private void werteDatensatzAus(int beginnZeile, int endeZeile,
			ArrayList<String> text) throws DateiSyntaxFehler {
		// Erstellt einen zusammenh�ngenden String des gesamten Datensatzes.
		String datensatz = "";
		for (int i = beginnZeile; i <= endeZeile; i++) {
			if (istKommentarZeile(text.get(i))) {
				continue;
			}
			datensatz += text.get(i);
		}
		//Auswerten der 3 Merkmale
		try {
			String nameOrtHerkunft = getMerkmal(BEZEICHNER_START, datensatz);
			String nameOrtZiel = getMerkmal(BEZEICHNER_ZIEL, datensatz);
			try {
				int faktor = Integer.parseInt(getMerkmal(BEZEICHNER_FAKTOR,
						datensatz));
				erzeugeFlugrouten(nameOrtHerkunft, nameOrtZiel, faktor);
				ausgewerteteDatensaetze++;
			} catch (MerkmalMissing e) {
				//Hier wird die Flugroute mit dem Daefault Faktoe erstellt, weil kein gueltiger Faktor gefunden wurde.
				erzeugeFlugrouten(nameOrtHerkunft, nameOrtZiel, DEFAULT_FAKTOR);
				//Hochzaehlen, um den User Info zu geben.
				mitFaktorDefault++;
			}
			//Fehler bei Merkmalauswertung
		} catch (MerkmalMissing e) {
			e.erzeugeMeldung();
			//Fehler bei Umwandlung der Zahlen
		} catch (NumberFormatException f) {
			JOptionPane.showMessageDialog(null,
					"Merkmale enthalten keine Zahlen. Im Datensatz ab "
							+ (aktuelleZeile + 1));
			throw new DateiSyntaxFehler();
		}

	}
}