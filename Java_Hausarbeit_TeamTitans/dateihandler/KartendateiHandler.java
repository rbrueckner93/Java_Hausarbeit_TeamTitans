package dateihandler;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import orte.Auslandsverbindung;
import orte.Hauptort;
import orte.Nebenort;
import orte.Umschlagpunkt;
import Main.Karte;

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

	public boolean pruefeDatei() {
		return false;
	}

	/**
	 * Diese Methode wertet den eingangsStream aus und erstellt entsprechende
	 * Orte
	 */
	public void werteZeileAus() {
		int aktuelleZeile = 0;
		boolean dateisatzBeginnMarkergefunden = false;
		ArrayList<String> eingeleseneZeilen = Datei
				.leseDatei(aktuelleKartendatei);
		while (aktuelleZeile < eingeleseneZeilen.size()){	
		if (eingeleseneZeilen.get(aktuelleZeile).substring(0, 1).equals(Datei.KOMMENTARMARKER)){
			aktuelleZeile += 1;
		} if (eingeleseneZeilen.get(aktuelleZeile).equals(DATEI_BEGINN_MARKER)){
			aktuelleZeile += 1;
			dateisatzBeginnMarkergefunden = true;
		} else {
			if (dateisatzBeginnMarkergefunden){} else {
			JOptionPane.showMessageDialog(null, "Fehlender Dateibeginnmarker");
			System.exit(0);}
			
		}
		
			
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