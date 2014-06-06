package dateihandler;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.DateiSyntaxFehler;
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

	public int ausgewerteteDatensaetze = 0;

	public int mitFaktorDefault = 0;

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

	public void verarbeiteTestdatei() throws DateiSyntaxFehler {
		ausgewerteteDatensaetze = 0;
		mitFaktorDefault = 0;
		// Einlesen der Datei. Liefert pro Zeile ein StringObjekt im Array.
		ArrayList<String> geleseneDaten = Datei.leseDatei(aktuelleTestdatei);
		aktuelleSimulation.nameTestdatei = getDateiNamen(aktuelleTestdatei);
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
		if (!DatensatzBeginnMarkerVorhanden(aktuelleZeile, geleseneDaten)) {
			JOptionPane.showMessageDialog(null,
					"Kein auswertbarer Datensatz in der Datei gefunden");
			throw new DateiSyntaxFehler();
		}
		if (!DatensatzMarkiererGleichwertig(dateiAnfang, geleseneDaten)) {
			JOptionPane
					.showMessageDialog(
							null,
							"Achtung! - Es fehlen Datensatzmarkierer zur korrekten Auswertung der Datei.\nOder es stehen 2 identsiche Marker in einer Zeile.");
			throw new DateiSyntaxFehler();
		}
		// Eigentliche Auswertung des gefundenen Datensatzes.
		while (DatensatzBeginnMarkerVorhanden(aktuelleZeile, geleseneDaten)
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
		JOptionPane.showMessageDialog(null, "Es wurden "
				+ ausgewerteteDatensaetze
				+ " Datensaetze erfolgreich eingelesen. \n" + mitFaktorDefault
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
	public static int findeDateiBeginnMarker(int beginn, ArrayList<String> text) {
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
	public static int findeDateiEndeMarker(int beginn, ArrayList<String> text)
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
								"Weiteren Datei Beginn Marker gefunden. In Zeile: "
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
									"Weiteren Datei Beginn Marker gefunden. In Zeile: "
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
	
	public boolean DatensatzMarkiererGleichwertig(int startZeile,
			ArrayList<String> text) throws DateiSyntaxFehler {
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
	 * Liefert true zurück wenn noch ein Auswertbarer Datensatz vorhanden ist.
	 * 
	 * @param beginn
	 * @param text
	 * @return
	 */
	public boolean DatensatzBeginnMarkerVorhanden(int beginn,
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
	 * 
	 * @param beginn
	 * @param text
	 * @param marker
	 * @return
	 */
	public int findeDatensatzBeginnMarker(int beginn, ArrayList<String> text) {
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
	 * 
	 * @param beginn
	 * @param text
	 * @return
	 */
	public static int findeDatensatzEndeMarker(int beginn,
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
	 * 
	 * @param wertBezeichner
	 * @param zeile
	 * @return
	 * @throws MerkmalMissing
	 */
	public String getMerkmal(String wertBezeichner, String zeile)
			throws MerkmalMissing, DateiSyntaxFehler {
		int anfang = zeile.indexOf(MERKMAL_BEGINN + wertBezeichner, 0);
		if (anfang == -1) {
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		int ende = zeile.indexOf(MERKMAL_ENDE, anfang);
		if (zeile.indexOf(MERKMAL_BEGINN + wertBezeichner, ende) != -1) {
			JOptionPane.showMessageDialog(null, "Merkmal \"" + wertBezeichner
					+ "\" mehrfach im Datensatz ab Zeile "
					+ (aktuelleZeile + 1) + " vorhanden.");
			throw new DateiSyntaxFehler();
		}
		String inhaltMerkmal = zeile.substring(anfang, ende);
		String[] merkmalsplit = inhaltMerkmal.split("\\"
				+ BEZEICHNER_WERT_TRENNER);
		// Check ob Merkmal Inhalt besitzt
		if (merkmalsplit.length == 1){
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		if (merkmalsplit[1].isEmpty()) {
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		if (merkmalsplit[0].equals(MERKMAL_BEGINN + wertBezeichner)) {
			return merkmalsplit[1].trim();
		} else {
			JOptionPane.showMessageDialog(null, "Fehler im Merkmal. "
					+ wertBezeichner + "Im Datensatz ab Zeile "
					+ (aktuelleZeile + 1));
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
		if (ortHerkunft == null) {
			JOptionPane.showMessageDialog(null, "Flugroute von " + nameHerkunft
					+ " nach " + nameZiel + " nicht erzeugbar - "
					+ nameHerkunft + " nicht auf Karte");
		} else if (ortZiel == null) {
			JOptionPane.showMessageDialog(null, "Flugroute von " + nameHerkunft
					+ " nach " + nameZiel + " nicht erzeugbar - " + nameZiel
					+ " nicht auf Karte");
		} else {
			aktuelleSimulation.routen.add(new Flugroute(ortZiel, ortHerkunft,
					faktor));
		}
	}

	public void werteDatensatzAus(int beginnZeile, int endeZeile,
			ArrayList<String> text) throws DateiSyntaxFehler {
		// Erstellt einen zusammenhängenden String.
		String datensatz = "";
		for (int i = beginnZeile; i <= endeZeile; i++) {
			if (istKommentarZeile(text.get(i))) {
				continue;
			}
			datensatz += text.get(i);
		}
		System.out.println(datensatz);
		try {
			int faktorDefault = 1;
			String nameOrtHerkunft = getMerkmal(BEZEICHNER_START, datensatz);
			String nameOrtZiel = getMerkmal(BEZEICHNER_ZIEL, datensatz);
			try {
				int faktor = Integer.parseInt(getMerkmal(BEZEICHNER_FAKTOR,
						datensatz));
				erzeugeFlugrouten(nameOrtHerkunft, nameOrtZiel, faktor);
				ausgewerteteDatensaetze++;
			} catch (MerkmalMissing e) {
				erzeugeFlugrouten(nameOrtHerkunft, nameOrtZiel, faktorDefault);
				mitFaktorDefault++;
			}
		} catch (MerkmalMissing e) {
			e.erzeugeMeldung();
			System.exit(0);
		} catch (NumberFormatException f) {
			JOptionPane.showMessageDialog(null,
					"Merkmale enthalten keine Zahlen. Im Datensatz ab "
							+ (aktuelleZeile + 1));
			throw new DateiSyntaxFehler();
		}

	}

	public String getDateiNamen(File datei) {
		int dateiEndung = datei.getName().indexOf(".txt");
		if (dateiEndung == -1) {
			JOptionPane
					.showMessageDialog(null, "Falsche Dateiendung der Datei");
			System.exit(0);
		}
		String dateiName = datei.getName().substring(0, dateiEndung);
		return dateiName;
	}

	public static boolean istKommentarZeile(String zeile) {
		if (zeile.indexOf(KOMMENTARMARKER) == -1
				|| zeile.indexOf(KOMMENTARMARKER) > 0) {
			return false;
		}
		return true;
	}

	public void checkLeeresDateiende(int zeile, ArrayList<String> text) {
		zeile++;
		while (zeile < text.size()) {
			if (text.get(zeile).equals("") || text.get(zeile).equals("\n")) {
				zeile++;
				continue;
			}
			JOptionPane.showMessageDialog(null,
					"Achtung! - Weiterer Text nach Datei Ende Marker gefunden");
			return;
		}
	}
}