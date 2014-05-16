package korridore;

import Exceptions.UngueltigerOrt;
import orte.Auslandsverbindung;
import orte.Hauptort;
import orte.Nebenort;
import orte.Ort;
import orte.Umschlagpunkt;

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
	 * 
	 * @param ortA
	 * @param ortB
	 */

	/**
	 * Wirft Exception wenn Orte verwedet wurden, die nicht fuer die Korridorart
	 * gültig sind. Fehler wird bei der Erzeugung von Korridoren in der Karte
	 * abgefangen
	 * 
	 * @param ortA
	 * @param ortB
	 * @throws UngueltigerOrt
	 */
	public SicherheitsKorridor(Ort ortA, Ort ortB) throws UngueltigerOrt {
		super(ortA, ortB);
		ueberpruefeOrtart(ortA);
		ueberpruefeOrtart(ortB);
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
	 * (Ort.ortArt) * wirft sonst Exception (UngueltigerOrt) aus
	 */
	public void ueberpruefeOrtart(Ort ortA) throws UngueltigerOrt {

		if (ortA.getClass() != Hauptort.class
				|| ortA.getClass() != Nebenort.class
				|| ortA.getClass() != Umschlagpunkt.class
				|| ortA.getClass() != Auslandsverbindung.class) {
			throw new UngueltigerOrt();
		}
	}
}