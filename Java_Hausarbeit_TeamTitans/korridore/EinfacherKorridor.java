package korridore;

import orte.Ort;

public class EinfacherKorridor extends Korridor {
	/* {author=TolleN, HandritschkP} */

	/**
	 * Mit Erzeugung eines Korridors wird unweigerlich geprueft werden, ob die
	 * hinzuzufuegenden Orte auch den Anforderungen der bestimmten Korridorart
	 * entsprechen.
	 */

	public static final double BAUKOSTEN = 50000;

	public static final double NUTZUNGSKOSTEN = 0.03;

	/**
	 * wird fuer jeden EinfacherKorridor als "Einfacher Korridor" festgelegt.
	 */
	public static final String BESCHREIBUNG = "Einfacher Korridor";

	public static final String KENNUNG = "ENFC";

	public EinfacherKorridor(Ort ortA, Ort ortB) {
		super(ortA, ortB);
		// Hier müssen die beiden Orte auf Nutzbarkeit geprueft werden.
		ueberpruefeOrtart();
	}

	/**
	 * Ueberpruefung, ob ortA, ortB aus den Ortsklassen Nebenort, Hauptort oder
	 * Umschlagpunkt stammen. (Ort.ortArt)
	 */
	public void ueberpruefeOrtart() {

	}
}