package korridore;

import orte.Ort;

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
	 * Kontruktor. Ueberpruefung der Orte hier.
	 * 
	 * @param ortA
	 * @param ortB
	 */
	public StandardKorridor(Ort ortA, Ort ortB) {
		super(ortA, ortB);
		ueberpruefeOrtart();
	}

	/**
	 * Ueberpruefung, ob ortA, ortB aus den Ortsklassen Hauptort, Umschlagpunkt
	 * oder Nebenort stammen. (Ort.ortArt)
	 */
	public void ueberpruefeOrtart() {
	}
}