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
	public static final int MAX_KOORD_Y = 99;

	public Karte kartenInstanz;

	public File aktuelleKartendatei;

	/**
	 * Konstruktor der Datei und ein Objekt Karte benoetigt.
	 * @param kartenInstanz          Zulesende Datei
	 * @param aktuelleKartendatei    Kartenobjekt, das gefuellt wird.
	 * @author Nils
	 */
	public KartendateiHandler(Karte kartenInstanz, File aktuelleKartendatei) {
		super();
		this.kartenInstanz = kartenInstanz;
		this.aktuelleKartendatei = aktuelleKartendatei;
	}

	/**
	 * Methode liesst eine Datei ein und wertet diese Zeilenweise aus.
	 * @author Nils
	 */
	public void verarbeiteKartendatei() {
		ArrayList<String> geleseneDaten = Datei.leseDatei(aktuelleKartendatei);
		kartenInstanz.nameKartendatei = getDateiNamen(aktuelleKartendatei);
		int dateiAnfang = findeDateiBeginnMarker(aktuelleZeile, geleseneDaten);
		if (dateiAnfang == -1) {
			JOptionPane.showMessageDialog(null, "Fehlender Datei Beginn Marker");
			System.exit(0);
		}
		int dateiEnde = findeDateiEndeMarker(dateiAnfang, geleseneDaten);
		if (dateiEnde == -1){
			JOptionPane.showMessageDialog(null, "Fehlender Datei Ende Marker");
			System.exit(0);
		}
		while (DatensatzBeginnMarkerVorhanden(aktuelleZeile, geleseneDaten)
				&& aktuelleZeile < dateiEnde) {
			int datensatzBeginn = findeDatensatzBeginnMarker(aktuelleZeile,
					geleseneDaten);
			int datensatzEnde = findeDatensatzEndeMarker(datensatzBeginn,
					geleseneDaten);
			aktuelleZeile = datensatzBeginn;
			werteDatensatzAus(datensatzBeginn, datensatzEnde, geleseneDaten);
			aktuelleZeile = datensatzEnde;
			if (datensatzBeginn == datensatzEnde) {
				aktuelleZeile += 1;
				System.out.println(1);
			}
		}
	}


	/**
	 * Sucht einen DateibeginnMarker.
	 * @param beginn Zeile ab der Gesucht werden soll
	 * @param text ArrayList von Strings, in der gesucht werden soll.
	 * @return Integer der Zeile mit Befund. Sonst -1
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
	 * Sucht nach einem Dateiende Marker. Beachtet dabei, dass im laufenden Text welche auftauchen können.
	 * @param beginn Zeile ab der gesucht werden soll.
	 * @param text ArrayList in der gesucht werden soll.
	 * @return Integer der Zeile mit Befund, sonst -1
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
						"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde.  In Zeile: "
								+ (beginn+1));
				System.exit(0);
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
							"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde.  In Zeile: "
									+ (beginn+1));
					System.exit(0);
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
		return -1;
	}

	/**
	 * Sucht nach vorhandenem Datensatzbeginn. Kriterium um weiter auszuwerten.
	 * 
	 * @param beginn Zeile ab der gesucht werden soll.
	 * @param text ArrayList String in denen gesucht wird.
	 * @return	true bei Fund sonst false.
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

	/**
	 * Findet DatensatzBeginn Marker im Text
	 * 
	 * @param beginn Zeile ab der gesucht werden soll.
	 * @param text List von Strings in denen gesucht werden soll.
	 * @return Integer der Zeile. Sonst -1
	 */
	public int findeDatensatzBeginnMarker(int beginn, ArrayList<String> text) {
		while (beginn < text.size() - 1) {
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
	 * Findet einen DatensatzEnde Marker. Ueberprueft dabei auf moegliche DatensatzBeginnMarker.
	 * @param beginn Zeile in der begonnen werden soll.
	 * @param text List von Strings in denen gesucht werden soll.
	 * @return Integer der Zeile mit Befunf, sonst beginn Zeile.
	 */
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
						"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde.  In Zeile: "
								+ (beginn+1));
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
		// Schritt in nächste Zeile.
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
							"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde.  In Zeile: "
									+ (beginn+1));
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

	/**
	 * Wertet ein Datensatz nach einem spez. Merkmal aus.
	 * 
	 * @param wertBezeichner Spezifischer Bezeichner des Wertes.
	 * @param zeile String in dem Merkmal stehen muss.
	 * @return Wert des gesuchten Merkmals als String
	 */
	public String getMerkmal(String wertBezeichner, String zeile)
			throws MerkmalMissing {
		int anfang = zeile.indexOf(MERKMAL_BEGINN + wertBezeichner, 0);
		if (anfang == -1) {
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		int ende = zeile.indexOf(MERKMAL_ENDE, anfang);
		String inhaltMerkmal = zeile.substring(anfang, ende);
		String[] merkmalsplit = inhaltMerkmal.split("\\"
				+ BEZEICHNER_WERT_TRENNER);
		if (merkmalsplit[0].equals(MERKMAL_BEGINN + wertBezeichner)) {
			return merkmalsplit[1];
		} else {
			JOptionPane.showMessageDialog(null, "Fehler im Merkmal "
					+ wertBezeichner + " in Datensatz der in Zeile "
					+ (aktuelleZeile+1) + " beginnt");
		}
		return null;
	}

	/**
	 * Wertet einen Datensatz aus und erstellt mögliches Objekt.
	 * 
	 * @param beginnZeile Startzeile des Datensatzes
	 * @param endeZeile Endzeile des Datensatzes
	 * @param text Liste aus Strings in dem der Datensatz steht.
	 */
	public void werteDatensatzAus(int beginnZeile, int endeZeile,
			ArrayList<String> text) {
		// Erstellt einen zusammenhängenden String.
		String datensatz = "";
		for (int i = beginnZeile; i <= endeZeile; i++) {
			datensatz += text.get(i);
		}
		System.out.println(datensatz);
		try {
			int xkoord = Integer.parseInt(getMerkmal(BEZEICHNER_X_KOORDINATE,
					datensatz));
			int ykoord = Integer.parseInt(getMerkmal(BEZEICHNER_Y_KOORDINATE,
					datensatz));
			// Ueberoruefen der Orte auf ihre Position.
			koordinateCheckenX(xkoord);
			koordinateCheckenY(ykoord);
			if (!mindestabstandEingehalten(xkoord, ykoord)) {
				JOptionPane.showMessageDialog(null,
						"Orte liegen zu dicht beeinander. Datensatz beginn in Zeile"
								+ (aktuelleZeile+1));
				System.exit(0);
			}
			// Restliche Auswertung.
			String name = getMerkmal(BEZEICHNER_NAME, datensatz);
			String kennung = getMerkmal(BEZEICHNER_KENNUNG, datensatz);

			switch (kennung) {
			case Ort.KENNUNG_HAUPTORT:
				int einwohnerZahl = Integer.parseInt(getMerkmal(
						BEZEICHNER_EINWOHNERZAHL, datensatz));
				erzeugeHauptort(xkoord, ykoord, name, einwohnerZahl);
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
			}
		} catch (MerkmalMissing e) {
			e.erzeugeMeldung();
			System.exit(0);
		} catch (NumberFormatException f) {
			JOptionPane.showMessageDialog(null,
					"Fehler in Merkmalen. Zahlen sind keine Zahlen. Datensatz ab Zeile "
							+ (aktuelleZeile+1));
			System.exit(0);
		}
	}

	/**
	 * Methode ueberprueft ob Orte den angegeben Abstand von 3 eingehalten haben.
	 * @param x
	 * @param y
	 * @return true, sonst false
	 */
	public boolean mindestabstandEingehalten(int x, int y) {
		if (kartenInstanz.orte.size() != 0) {
			for (Ort ortB : kartenInstanz.orte) {
				double distanz = Math.sqrt((Math.pow((x - ortB.koordX), 2))
						+ Math.pow((y - ortB.koordY), 2));
				// Hier steht der Mindestabstand.
				if (distanz < 3) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Ueberorueft,ob X-Koordinate im Bereich der Karte liegt.
	 * @param koord Integer der Koordinate.
	 */
	public void koordinateCheckenX(int koord) {
		if (koord > MAX_KOORD_X || koord < MIN_KOORD_X) {
			JOptionPane
					.showMessageDialog(
							null,
							"Ort liegt ausserhalb der erlaubten Karte - X Wert außerhalb \n In Datensatz ab Zeile "
									+ (aktuelleZeile+1));
			System.exit(0);
		}
	}

	/**
	 * Ueberorueft,ob  Y-Koordinate im Bereich der Karte liegt.
	 * @param koord Integer der Koordinate.
	 */
	public void koordinateCheckenY(int koord) {
		if (koord > MAX_KOORD_Y || koord < MIN_KOORD_Y) {
			JOptionPane
					.showMessageDialog(
							null,
							"Ort liegt ausserhalb der erlaubten Karte - Y Wert außerhalb \n In Datensatz ab Zeile "
									+ (aktuelleZeile+1));
			System.exit(0);
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
	/**
	 * Methode erzeugt einen String des aktuellen Dateinamens von der momentan ausgewerteten Datei.
	 * Prueft zusaetzlich, ob es sich um eine txt Datei handelt.
	 * @param Datei, die gerade ausgewertet wird.
	 * @return String des Dateinamens ohne suffix.
	 */
	public String getDateiNamen(File datei){
		int dateiEndung = datei.getName().indexOf(".txt");
		if (dateiEndung == -1){
			JOptionPane.showMessageDialog(null, "Falsche Dateiendung der Datei");
			System.exit(0);
		}
		String dateiName = datei.getName().substring(0, dateiEndung);
		return dateiName;
	}
}