package korridore;

import orte.Ort;

public class SicherheitsKorridor extends Korridor {
	/* {author=TolleN, HandritschkP} */

	public static final double BAUKOSTEN = 250000;

	public static final double NUTZUNGSKOSTEN = 0.04;

	/**
	 * wird fuer jeden SicherheitsKorridor als "Sicherheitskorridor" festgelegt.
	 */
	public static final String BESCHREIBUNG = "Sicherheitskorridor";

	public static final String KENNUNG = "SICH";
	/**
	 * Konstruktor. Hier ueberpruefung der Orte.
	 * @param ortA
	 * @param ortB
	 */
	public SicherheitsKorridor(Ort ortA, Ort ortB) {
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
	 * ueberpruefung, ob ortA, ortB eine Ortsart besitzen (hier hinreichend.)
	 * (Ort.ortArt)
	 */
	public void ueberpruefeOrtart() {
	}
}