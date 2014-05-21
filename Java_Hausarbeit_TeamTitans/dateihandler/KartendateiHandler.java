package dateihandler;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.MerkmalMissing;
import main.Karte;
import orte.Auslandsverbindung;
import orte.Hauptort;
import orte.Nebenort;
import orte.Ort;
import orte.Umschlagpunkt;

/**
 * 
 * @author TolleN
 *
 */

public class KartendateiHandler extends Datei {
	public static final String DATEI_BEGINN_MARKER = ">->map";
	public static final String DATEI_ENDE_MARKER = "<-<map";
	public static final String DATENSATZ_BEGINN_MARKER = ">->loc";
	public static final String DATENSATZ_ENDE_MARKER = "<-<loc";
	public static ArrayList<String> GUELTIGE_BEZEICHNER;
	public static final String BEZEICHNER_X_KOORDINATE = "x";
	public static final String BEZEICHNER_Y_KOORDINATE = "y";
	public static final String BEZEICHNER_NAME = "name";
	public static final String BEZEICHNER_KENNUNG = "kind";
	public static final String BEZEICHNER_EINWOHNERZAHL = "population";
	public static final String BEZEICHNER_UMSCHLAGVOLUMEN = "turnover";
	public static final String BEZEICHNER_PASSAGIERAUFKOMMEN = "passengers";

	public int aktuelleZeile = 0;

	/**
	 * 0
	 */
	public static final int MIN_KOORD_X = 0;

	/**
	 * 199
	 */
	public static final int MAX_KOORD_X = 199;

	/**
	 * 0
	 */
	public static final int MIN_KOORD_Y = 0;

	/**
	 * 99
	 */
	public static final int MAY_KOORD_Y = 99;

	public Karte kartenInstanz;

	public File aktuelleKartendatei;

	/**
	 * Konstruktor der Datei und ein Objekt Karte benoetigt.
	 * 
	 * @param kartenInstanz
	 *            Zulesende Datei
	 * @param aktuelleKartendatei
	 *            Kartenobjekt, das gefuellt wird.
	 */
	public KartendateiHandler(Karte kartenInstanz, File aktuelleKartendatei) {
		super();
		this.kartenInstanz = kartenInstanz;
		this.aktuelleKartendatei = aktuelleKartendatei;
	}

	public void verarbeiteKartendatei() {
		ArrayList<String> geleseneDaten = Datei.leseDatei(aktuelleKartendatei);
		while (DatensatzBeginnMarkerVorhanden(aktuelleZeile, geleseneDaten)) {
			int datensatzBeginn = findeDatensatzBeginnMarker(aktuelleZeile,
					geleseneDaten, DATENSATZ_BEGINN_MARKER);
			int datensatzEnde = findeDatensatzEndeMarker(datensatzBeginn,
					geleseneDaten);
			werteDatensatzAus(aktuelleZeile, datensatzEnde, geleseneDaten);
			aktuelleZeile = datensatzEnde;
			if (datensatzBeginn == datensatzEnde) {
				aktuelleZeile += 1;
			}
		}
	}
	/**
	 * 
	 * @param beginn
	 * @param text
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
	 * 
	 * @param beginn
	 * @param text
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
	 * Sucht nach vorhandenem Datensatzbeginn. Kriterium um weiter auszuwerten.
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
	/**
	 * Findet Datensatz beginn
	 * 
	 * @param beginn
	 * @param text
	 * @param marker
	 * @return
	 */
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

	/**
	 * Wertet ein Datensatz nach einem spez. Merkmal aus.
	 * 
	 * @param wertBezeichner
	 * @param zeile
	 * @return
	 */
	public String getMerkmal(String wertBezeichner, String zeile) throws MerkmalMissing {
		int anfang = zeile.indexOf(MERKMAL_BEGINN+wertBezeichner, 0);
		if ( anfang == -1){
			throw new MerkmalMissing();
		}
		int ende = zeile.indexOf(MERKMAL_ENDE, anfang);
		String inhaltMerkmal = zeile.substring(anfang, ende);
		String[] merkmalsplit = inhaltMerkmal.split("\\"+BEZEICHNER_WERT_TRENNER);
		if (merkmalsplit[0].equals(MERKMAL_BEGINN+wertBezeichner)) {
			return merkmalsplit[1];
		} else {
			JOptionPane.showMessageDialog(null, "Fehler im Merkmal "+aktuelleZeile
					+ wertBezeichner);
		}
		return null;
	}

	/**
	 * Wertet einen Datensatz aus und erstellt m�gliches Objekt.
	 * 
	 * @param beginnZeile
	 * @param endeZeile
	 * @param text
	 */
	public void werteDatensatzAus(int beginnZeile, int endeZeile,
			ArrayList<String> text) {
		// Erstellt einen zusammenh�ngenden String.
		String datensatz = "";
		for (int i = beginnZeile; i <= endeZeile; i++) {
			datensatz += text.get(i);
		}
		try{
		int xkoord = Integer
				.parseInt(getMerkmal(BEZEICHNER_X_KOORDINATE, datensatz));
		int ykoord = Integer
				.parseInt(getMerkmal(BEZEICHNER_Y_KOORDINATE, datensatz));
		String name = getMerkmal(BEZEICHNER_NAME, datensatz);
		String kennung = getMerkmal(BEZEICHNER_KENNUNG, datensatz);

		switch (kennung) {
		case Ort.KENNUNG_HAUPTORT:
			int einwohnerZahl = Integer.parseInt(getMerkmal(
					BEZEICHNER_EINWOHNERZAHL, datensatz));
			erzeugeHauptort(xkoord, ykoord, name,einwohnerZahl);
			break;
		case Ort.KENNUNG_NEBENORT:
			int einwohnerZahlnbn = Integer.parseInt(getMerkmal(
					BEZEICHNER_EINWOHNERZAHL, datensatz));
			erzeugeNebenort(xkoord, ykoord, name, einwohnerZahlnbn);
			break;
		case Ort.KENNUNG_UMSCHLAGPUNKT:
			double umschlagVolumen = Double.parseDouble(getMerkmal(
					BEZEICHNER_UMSCHLAGVOLUMEN, datensatz));
			erzeugeUmschlagpunkt(xkoord, ykoord, name, umschlagVolumen);
			break;
		case Ort.KENNUNG_AUSLANDSVERBINDUNG:
			double umschlagVolumenASL = Double.parseDouble(getMerkmal(
					BEZEICHNER_UMSCHLAGVOLUMEN, datensatz));
			int passagierAufkommen = Integer.parseInt(getMerkmal(
					BEZEICHNER_PASSAGIERAUFKOMMEN, datensatz));
			erzeugeAuslandsverbindung(xkoord, ykoord, name,
					umschlagVolumenASL, passagierAufkommen);
		}} catch (MerkmalMissing e){
			JOptionPane.showMessageDialog(null, "Fehlendes Merkmal");
		} catch (NumberFormatException f){
			JOptionPane.showMessageDialog(null, "Fehler in Mermalen. Zahlen sind keine Zahlen");
		}
	}

	// Hier stehen die 4 Methoden zur Erzeugung der 4 verschiedenen Orte.
	// @author Nils
	public void erzeugeHauptort(int koordX, int koordY, String name,
			 int anzahlEinwohner) {
		kartenInstanz.orte.add(new Hauptort(koordX, koordY, name,
				anzahlEinwohner));
	}

	public void erzeugeNebenort(int koordX, int koordY, String name,
			 int anzahlEinwohner) {
		kartenInstanz.orte.add(new Nebenort(koordX, koordY, name,
				anzahlEinwohner));
	}

	public void erzeugeUmschlagpunkt(int koordX, int koordY, String name,
			double umschlagVolumen) {
		kartenInstanz.orte.add(new Umschlagpunkt(koordX, koordY, name,
				umschlagVolumen));
	}

	public void erzeugeAuslandsverbindung(int koordX, int koordY, String name,
			double umschlagVolumen, int passagierAufkommen) {
		kartenInstanz.orte.add(new Auslandsverbindung(koordX, koordY, name,
				 passagierAufkommen, umschlagVolumen));
	}
	
}