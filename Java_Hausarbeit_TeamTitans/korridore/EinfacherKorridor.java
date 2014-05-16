package korridore;

import Exceptions.UngueltigerOrt;
import orte.Hauptort;
import orte.Nebenort;
import orte.Ort;
import orte.Umschlagpunkt;

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

	/**
	 * Wirft Exception wenn Orte verwedet wurden, die nicht fuer die Korridorart
	 * gültig sind. Fehler wird bei der Erzeugung von Korridoren in der Karte
	 * abgefangen
	 * 
	 * @param ortA
	 * @param ortB
	 * @throws UngueltigerOrt
	 */
	public EinfacherKorridor(Ort ortA, Ort ortB) throws UngueltigerOrt{
		super(ortA, ortB);
	
		 ueberpruefeOrtart(ortA);
		 ueberpruefeOrtart(ortB);
		 
		 

	}

	/**
	 * Ueberpruefung, ob ortA, ortB aus den Ortsklassen Nebenort, Hauptort oder
	 * Umschlagpunkt stammen. (Ort.ortArt)
	 * wirft sonst Exception (UngueltigerOrt) aus 
	 * 
	 */
	public void ueberpruefeOrtart(Ort ortA) throws UngueltigerOrt {

		if (ortA.getClass() != Hauptort.class
				|| ortA.getClass() != Nebenort.class
				|| ortA.getClass() != Umschlagpunkt.class) {
			throw new UngueltigerOrt();
		} 

		

	}
}