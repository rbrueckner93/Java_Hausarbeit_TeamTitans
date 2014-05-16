package korridore;

import orte.Ort;

public class HochlastKorridor extends Korridor {
	/* {author=TolleN, HandritschkP} */

	/**
	 * Mit Erzeugung eines Korridors wird unweigerlich geprueft werden, ob die
	 * hinzuzufuegenden Orte auch den Anforderungen der bestimmten Korridorart
	 * entsprechen.
	 */

	public static final double BAUKOSTEN = 350000;

	public static final double NUTZUNGSKOSTEN = 0.01;

	/**
	 * wird fuer jeden HauptlastKorridor als "Hauptlastkorridor" festgelegt.
	 */
	public static final String BESCHREIBUNG = "Hochlastkorridor";

	public static final String KENNUNG = "HLST";

	public HochlastKorridor(Ort ortA, Ort ortB) {
		super(ortA, ortB);
		ueberpruefeOrtart();
	}

	public static double getBaukosten() {
		return BAUKOSTEN;
	}

	public static double getNutzungskosten() {
		return NUTZUNGSKOSTEN;
	}

	public static String getBeschreibung() {
		return BESCHREIBUNG;
	}

	public static String getKennung() {
		return KENNUNG;
	}

	/**
	 * Ueberpruefung, ob ortA, ortB aus den Ortsklassen Hauptort oder
	 * Umschlagpunkt stammen. (Ort.ortArt)
	 */
	public void ueberpruefeOrtart() {
	}

}