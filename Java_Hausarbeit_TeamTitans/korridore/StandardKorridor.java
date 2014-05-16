package korridore;

import Exceptions.UngueltigerOrt;
import orte.Hauptort;
import orte.Nebenort;
import orte.Ort;
import orte.Umschlagpunkt;

public class StandardKorridor extends Korridor {
	/* {author=TolleN, HandritschkP} */

	public static final double BAUKOSTEN = 200000;

	public static final double NUTZUNGSKOSTEN = 0.02;

	/**
	 * wird fuer jeden StandarfKorridor als "Standardkorridor" festgelegt.
	 */
	public static final String BESCHREIBUNG = "Standardkorridor";

	public static final String KENNUNG = "STND";

	/**
	 * Wirft Exception wenn Orte verwedet wurden, die nicht fuer die Korridorart
	 * gültig sind. Fehler wird bei der Erzeugung von Korridoren in der Karte
	 * abgefangen
	 * 
	 * @param ortA
	 * @param ortB
	 * @throws UngueltigerOrt
	 */
	public StandardKorridor(Ort ortA, Ort ortB) throws UngueltigerOrt {
		super(ortA, ortB);
		ueberpruefeOrtart(ortA);
		ueberpruefeOrtart(ortB);
	}

	/**
	 * Ueberpruefung, ob ortA, ortB aus den Ortsklassen Hauptort, Umschlagpunkt
	 * oder Nebenort stammen. (Ort.ortArt) * wirft sonst Exception
	 * (UngueltigerOrt) aus
	 */
	public void ueberpruefeOrtart(Ort ortA) throws UngueltigerOrt {

		if (ortA.getClass() != Hauptort.class
				|| ortA.getClass() != Nebenort.class
				|| ortA.getClass() != Umschlagpunkt.class) {
			throw new UngueltigerOrt();
		}
	}
}