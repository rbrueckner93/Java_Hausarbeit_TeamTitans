package korridore;

import Exceptions.UngueltigerOrt;
import orte.Hauptort;
import orte.Nebenort;
import orte.Ort;
import orte.Umschlagpunkt;

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

	/**
	 * Wirft Exception wenn Orte verwedet wurden, die nicht fuer die Korridorart
	 * gültig sind. Fehler wird bei der Erzeugung von Korridoren in der Karte
	 * abgefangen
	 * 
	 * @param ortA
	 * @param ortB
	 * @throws UngueltigerOrt
	 */
	public HochlastKorridor(Ort ortA, Ort ortB) throws UngueltigerOrt {
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
	 * Ueberpruefung, ob ortA, ortB aus den Ortsklassen Hauptort oder
	 * Umschlagpunkt stammen. (Ort.ortArt) * wirft sonst Exception
	 * (UngueltigerOrt) aus
	 */
	public void ueberpruefeOrtart(Ort ortA) throws UngueltigerOrt {

		if (ortA.getClass() != Hauptort.class
				|| ortA.getClass() != Umschlagpunkt.class) {
			throw new UngueltigerOrt();
		}
	}

}