package dateihandler;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import orte.Auslandsverbindung;
import orte.Hauptort;
import orte.Nebenort;
import orte.Umschlagpunkt;
import Main.Karte;

public class KartendateiHandler extends Datei {
	/* {author=TolleN} */

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
		while (!dateiEndemarkererreicht(aktuelleZeile, geleseneDaten)) {
			aktuelleZeile = findeDatensatzBeginnMarker(aktuelleZeile,
					geleseneDaten, DATENSATZ_BEGINN_MARKER);
			int endeDatensatz = findeDatensatzEndeMarker(aktuelleZeile,
					geleseneDaten, DATENSATZ_ENDE_MARKER);
			werteDatensatzAus(aktuelleZeile, endeDatensatz, geleseneDaten);
			System.out.println(endeDatensatz+" ende datensatz");
			aktuelleZeile = endeDatensatz;
			System.out.println(aktuelleZeile+"  aktuelleZeile");
		}
	}
	/**
	 * 
	 * @param beginn
	 * @param text
	 * @return
	 */
	public boolean dateiEndemarkererreicht(int beginn,
			ArrayList<String> text) {
		while (true) {
			//beginn+1 , da ich eine Zeile vorraus schaue.
			if (text.get(beginn+1).equals("") || text.get(beginn+1).equals("\n")) {
				beginn++;
				continue;
			}
			if (text.get(beginn+1).substring(0, DATEI_ENDE_MARKER.length()).equals(DATEI_ENDE_MARKER)){
				return true;
			} else {
				return false;
			}
		}
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

	/**
	 * Findet einen Datensatz Ende markierer.
	 * 
	 * @param beginn
	 * @param text
	 * @param marker
	 * @return
	 */
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

	/**
	 * Wertet ein Datensatz nach einem spez. Merkmal aus.
	 * 
	 * @param wertBezeichner
	 * @param zeile
	 * @return
	 */
	public String getMerkmal(String wertBezeichner, String zeile) {
		int anfang = zeile.indexOf(Datei.MERKMAL_BEGINN+wertBezeichner, 0);
		int ende = zeile.indexOf("]", anfang);
		String inhaltMerkmal = zeile.substring(anfang, ende);
		String[] merkmalsplit = inhaltMerkmal.split("\\|");
		if (merkmalsplit[0].equals(Datei.MERKMAL_BEGINN+wertBezeichner)) {
			return merkmalsplit[1];
		} else {
			JOptionPane.showMessageDialog(null, "Fehler im Merkmal "
					+ wertBezeichner);
		}
		return null;
	}

	/**
	 * Wertet einen Datensatz aus und erstellt mögliches Objekt.
	 * 
	 * @param beginnZeile
	 * @param endeZeile
	 * @param text
	 */
	public void werteDatensatzAus(int beginnZeile, int endeZeile,
			ArrayList<String> text) {
		// Erstellt einen zusammenhängenden String.
		String datensatz = "";
		for (int i = beginnZeile; i <= endeZeile; i++) {
			datensatz += text.get(i);
		}
		int xkoord = Integer
				.parseInt(getMerkmal(BEZEICHNER_X_KOORDINATE, datensatz));
		int ykoord = Integer
				.parseInt(getMerkmal(BEZEICHNER_Y_KOORDINATE, datensatz));
		String name = getMerkmal(BEZEICHNER_NAME, datensatz);
		String kennung = getMerkmal(BEZEICHNER_KENNUNG, datensatz);

		switch (kennung) {
		case "HPT":
			int einwohnerZahl = Integer.parseInt(getMerkmal(
					BEZEICHNER_EINWOHNERZAHL, datensatz));
			erzeugeHauptort(xkoord, ykoord, name, kennung, einwohnerZahl);
			break;
		case "NBN":
			int einwohnerZahlnbn = Integer.parseInt(getMerkmal(
					BEZEICHNER_EINWOHNERZAHL, datensatz));
			erzeugeNebenort(xkoord, ykoord, name, kennung, einwohnerZahlnbn);
			break;
		case "UMS":
			double umschlagVolumen = Double.parseDouble(getMerkmal(
					BEZEICHNER_UMSCHLAGVOLUMEN, datensatz));
			erzeugeUmschlagpunkt(xkoord, ykoord, name, kennung, umschlagVolumen);
			break;
		case "ASL":
			double umschlagVolumenASL = Double.parseDouble(getMerkmal(
					BEZEICHNER_UMSCHLAGVOLUMEN, datensatz));
			int passagierAufkommen = Integer.parseInt(getMerkmal(
					BEZEICHNER_PASSAGIERAUFKOMMEN, datensatz));
			erzeugeAuslandsverbindung(xkoord, ykoord, name, kennung,
					umschlagVolumenASL, passagierAufkommen);
		}
	}

	// Hier stehen die 4 Methoden zur Erzeugung der 4 verschiedenen Orte.
	// @author Nils
	public void erzeugeHauptort(int koordX, int koordY, String name,
			String kennung, int anzahlEinwohner) {
		kartenInstanz.orte.add(new Hauptort(koordX, koordY, name, kennung,
				anzahlEinwohner));
	}

	public void erzeugeNebenort(int koordX, int koordY, String name,
			String kennung, int anzahlEinwohner) {
		kartenInstanz.orte.add(new Nebenort(koordX, koordY, name, kennung,
				anzahlEinwohner));
	}

	public void erzeugeUmschlagpunkt(int koordX, int koordY, String name,
			String kennung, double umschlagVolumen) {
		kartenInstanz.orte.add(new Umschlagpunkt(koordX, koordY, name, kennung,
				umschlagVolumen));
	}

	public void erzeugeAuslandsverbindung(int koordX, int koordY, String name,
			String kennung, double umschlagVolumen, int passagierAufkommen) {
		kartenInstanz.orte.add(new Auslandsverbindung(koordX, koordY, name,
				kennung, passagierAufkommen, umschlagVolumen));
	}
	
}