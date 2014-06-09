package dateihandler;

import java.util.ArrayList;

import netz.Karte;
import netz.Korridor;

/**
 * Schreibt alle Korridore einer uebergebenen Karte in eine Textdatei.
 * 
 * @author TolleN
 */

public class NetzdateiHandler extends Datei {
	public static final String DATEI_BEGINN_MARKER = ">->net";
	public static final String DATEI_ENDE_MARKER = "<-<net";
	public static final String DATENSATZ_BEGINN_MARKER = ">->connection";
	public static final String DATENSATZ_ENDE_MARKER = "<-<connection";
	public static final String DATEI_SUFFIX = "_net";
	public static final String BEZEICHNER_START = "start";
	public static final String BEZEICHNER_ZIEL = "end";
	public static final String BEZEICHNER_KORRIDOR_KENNUNG = "kind";

	private Karte fertigesNetz;

	/**
	 * Konstruktor. Muss ein Objekt des Types Karte uebergeben bekommen.
	 * 
	 * @param fertigesNetz
	 */
	public NetzdateiHandler(Karte fertigesNetz) {
		super();
		this.fertigesNetz = fertigesNetz;
	}

	/**
	 * Methode erzeugt einen schreibbaren Stream und schreibt diesen in eine
	 * Datei.
	 */
	public void schreibeNetzdatei() {
		Datei.schreibeDatei(erstelleOutputStreamNet(), erstelleDateiNameNet());
	}

	/**
	 * Methode schreibt alle ausgewerteten Korridore in ein String Array. Haelt
	 * sich dabei an vorgegebene Syntax und Markierer.
	 * 
	 * @return ArrayList von Strings.
	 */
	private ArrayList<String> erstelleOutputStreamNet() {
		ArrayList<String> fertigerText = new ArrayList<String>();
		fertigerText.add(DATEI_BEGINN_MARKER);
		fertigerText.add("");
		/*
		 * Durchlaufen aller Korridore, um sie mit allen Merkmalen zu schreiben.
		 */
		for (Korridor aktuellerKorridor : fertigesNetz
				.getEingerichteteKorridore()) {
			fertigerText.add(DATENSATZ_BEGINN_MARKER);
			fertigerText.add(MERKMAL_BEGINN + BEZEICHNER_START
					+ BEZEICHNER_WERT_TRENNER
					+ aktuellerKorridor.getOrtA().getName() + MERKMAL_ENDE);
			fertigerText.add(MERKMAL_BEGINN + BEZEICHNER_ZIEL
					+ BEZEICHNER_WERT_TRENNER
					+ aktuellerKorridor.getOrtB().getName() + MERKMAL_ENDE);
			fertigerText.add(MERKMAL_BEGINN + BEZEICHNER_KORRIDOR_KENNUNG
					+ BEZEICHNER_WERT_TRENNER + aktuellerKorridor.getKennung()
					+ MERKMAL_ENDE);
			fertigerText.add(DATENSATZ_ENDE_MARKER);
		}
		fertigerText.add("");
		fertigerText.add(DATEI_ENDE_MARKER);
		return fertigerText;
	}

	/**
	 * Haengt an den Dateinamen den spezifischen Suffix dran. Nimmt den
	 * Kartendatei Namen aus der aktuellen Karte.
	 * 
	 * @return Ein String aus Dateiname + Suffix.
	 */
	private String erstelleDateiNameNet() {
		String dateiName = fertigesNetz.getNameKartendatei() + DATEI_SUFFIX;
		return dateiName;
	}

}