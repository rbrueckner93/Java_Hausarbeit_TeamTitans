package netz;

import exceptions.UngueltigerOrt;
import orte.Auslandsverbindung;
import orte.Hauptort;
import orte.Nebenort;
import orte.Ort;
import orte.Umschlagpunkt;

/**
 * Die den Unterklasse zugeordnete Methode ueberpruefeOrtart ueberprueft, ob die
 * Orte A und B fuer die jeweiligen Orte zulaessig ist, wenn nicht, gibt die
 * Methode ein false zurueck. Wird aufgerufen durch die Initialisierung eines
 * jeden Korridors.
 */

public class Korridor {
	/* {author=TolleN} */

	private Ort ortA;
	private Ort ortB;
	private double laenge;
	private String kennung;
	private boolean endgueltig;

	public static final double BAUKOSTEN_ENFC = 50000;
	public static final double BAUKOSTEN_HLST = 350000;
	public static final double BAUKOSTEN_SICH = 250000;
	public static final double BAUKOSTEN_STND = 200000;
	public static final String KENNUNG_ENFC = "ENFC";
	public static final String KENNUNG_HLST = "HLST";
	public static final String KENNUNG_SICH = "SICH";
	public static final String KENNUNG_STND = "STND";
	public static final double NUTZUNGSKOSTEN_ENFC = 0.03;
	public static final double NUTZUNGSKOSTEN_HLST = 0.01;
	public static final double NUTZUNGSKOSTEN_SICH = 0.04;
	public static final double NUTZUNGSKOSTEN_STND = 0.02;
	public static final String BESCHREIBUNG_ENFC = "Einfacher Korridor";
	public static final String BESCHREIBUNG_HLST = "Hochlastkorridor";
	public static final String BESCHREIBUNG_SICH = "Sicherheitskorridor";
	public static final String BESCHREIBUNG_STND = "Standardkorridor";

	/**
	 * Konstruktor eines allgemeinen Korridors
	 * 
	 * @param ortA
	 * @param ortB
	 * @param kennung
	 * @param endgueltig
	 *            falls false wird der Korridor noch nicht in
	 *            ort.angebundeneKorridore geschrieben.
	 * @throws UngueltigerOrt
	 */
	public Korridor(Ort ortA, Ort ortB, String kennung, boolean endgueltig)
			throws UngueltigerOrt {
		super();
		this.ortA = ortA;
		this.ortB = ortB;
		this.kennung = kennung;
		this.endgueltig = endgueltig;
		ueberpruefeZulaessigkeit();
		ermittleLaenge();
		if (endgueltig)
			aktiviere();
	}

	private void ueberpruefeZulaessigkeit() throws UngueltigerOrt {
		ueberpruefeOrtart(ortA);
		ueberpruefeOrtart(ortB);
		ueberpruefeOrtUngleichheit(ortA, ortB);
	}

	public double getLaenge() {
		return laenge;
	}
	
	public boolean isEndgueltig() {
		return endgueltig;
	}

	public void aktiviere() {
		ortA.addAngebundenenKorridor(this);
		ortB.addAngebundenenKorridor(this);
	}

	public Ort getOrtA() {
		return ortA;
	}

	public Ort getOrtB() {
		return ortB;
	}

	public String getKennung() {
		return kennung;
	}

	public void setKennung(String kennung) {
		this.kennung = kennung;
	}

	/**
	 * Wird aus Karte aufgerufen.
	 * 
	 * @return die naechst hoehere Kennung eine Korridors
	 */

	public String getNextKennung() {
		switch (getKennung()) {
		case Korridor.KENNUNG_HLST: {
			return "";
		}
		case Korridor.KENNUNG_SICH: {
			return "";
		}
		case Korridor.KENNUNG_ENFC: {
			return Korridor.KENNUNG_STND;
		}
		case Korridor.KENNUNG_STND: {
			return Korridor.KENNUNG_HLST;
		}
		}
		return "";
	}

	/**
	 * Kann fuer einen Korridor aufgerufen werden und bekommt einen Ort dazu.
	 * Gibt dann den anderen Ort, der zu dem Korridor gehoert zurueck.
	 * 
	 * @param bekanntesEnde
	 * @return den anderen Ort des Korridors
	 * @throws IllegalArgumentException
	 *             wird geworfen, wenn der uebergebene Ort gar nicht zu dem
	 *             Korr. gehoert.
	 */

	public Ort bestimmeAnderenOrt(Ort bekanntesEnde)
			throws IllegalArgumentException {
		if (ortA == bekanntesEnde) {
			return ortB;
		} else if (ortB == bekanntesEnde) {
			return ortA;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Fuer einen Korr. werden ueber die Kennung die Baukosten zurueckgegeben.
	 * 
	 * @return die Baukosten des Korr.
	 */

	public double getBaukosten() {
		if (kennung.equals(KENNUNG_ENFC)) {
			return BAUKOSTEN_ENFC;
		} else if (kennung.equals(KENNUNG_HLST)) {
			return BAUKOSTEN_HLST;
		} else if (kennung.equals(KENNUNG_SICH)) {
			return BAUKOSTEN_SICH;
		} else if (kennung.equals(KENNUNG_STND)) {
			return BAUKOSTEN_STND;
		} else {
			return 0;
		}
	}

	/**
	 * Fuer einen Korr. werden ueber die Kennung die Nutzkosten pro km
	 * zurueckgegeben.
	 * 
	 * @return die Nutzkosten pro km
	 */

	public double getNutzungskostenProKm() {
		if (kennung.equals(KENNUNG_ENFC)) {
			return NUTZUNGSKOSTEN_ENFC;
		} else if (kennung.equals(KENNUNG_HLST)) {
			return NUTZUNGSKOSTEN_HLST;
		} else if (kennung.equals(KENNUNG_SICH)) {
			return NUTZUNGSKOSTEN_SICH;
		} else if (kennung.equals(KENNUNG_STND)) {
			return NUTZUNGSKOSTEN_STND;
		} else {
			return 0;
		}
	}

	/**
	 * Fuer einen Korr. wird ueber die Kennung die Beschreibung zurueckgegeben.
	 * 
	 * @return die Beschreibung
	 */
	public String getBeschreibung() {
		if (kennung.equals(KENNUNG_ENFC)) {
			return BESCHREIBUNG_ENFC;
		} else if (kennung.equals(KENNUNG_HLST)) {
			return BESCHREIBUNG_HLST;
		} else if (kennung.equals(KENNUNG_SICH)) {
			return BESCHREIBUNG_SICH;
		} else if (kennung.equals(KENNUNG_STND)) {
			return BESCHREIBUNG_STND;
		} else {
			return "";
		}
	}

	/**
	 * Methode nur von Konstruktor des Korridors aufgerufen.
	 */

	private void ermittleLaenge() {
		double laengeQuadrat = Math.pow(ortA.getKoordX() - ortB.getKoordX(), 2)
				+ Math.pow(ortA.getKoordY() - ortB.getKoordY(), 2);
		laenge = (Math.sqrt(laengeQuadrat));
	}

	/**
	 * Methode nur von Konstruktor des Korridors aufgerufen.
	 * 
	 * @param ortA
	 *            Ort der an den Korr. angebunden werden soll.
	 * @throws UngueltigerOrt
	 *             wenn der Ort nicht an die gewaehlte Korridorart agebunden
	 *             werden kann.
	 */

	private void ueberpruefeOrtart(Ort ortA) throws UngueltigerOrt {
		if (kennung.equals(KENNUNG_ENFC)) {
			if (ortA.getClass() == Hauptort.class
					|| ortA.getClass() == Nebenort.class
					|| ortA.getClass() == Umschlagpunkt.class) {
			} else {
				throw new UngueltigerOrt(ortA, ortB, KENNUNG_ENFC);
			}
		} else if (kennung.equals(KENNUNG_HLST)) {
			if (ortA.getClass() == Hauptort.class
					|| ortA.getClass() == Umschlagpunkt.class) {
			} else {
				throw new UngueltigerOrt(ortA, ortB, KENNUNG_HLST);
			}
		} else if (kennung.equals(KENNUNG_SICH)) {
			if (ortA.getClass() == Hauptort.class
					|| ortA.getClass() == Nebenort.class
					|| ortA.getClass() == Umschlagpunkt.class
					|| ortA.getClass() == Auslandsverbindung.class) {
			} else {
				throw new UngueltigerOrt(ortA, ortB, KENNUNG_SICH);
			}
		} else if (kennung.equals(KENNUNG_STND)) {
			if (ortA.getClass() == Hauptort.class
					|| ortA.getClass() == Nebenort.class
					|| ortA.getClass() == Umschlagpunkt.class) {
			} else {
				throw new UngueltigerOrt(ortA, ortB, KENNUNG_STND);
			}
		} else {
			throw new UngueltigerOrt(ortA, ortB, "keineKennung");
		}
	}

	/**
	 * Methode nur aus Konstruktor aufgerufen.
	 * 
	 * @param ortA
	 *            ein Ort des Korr. der eingerichtet werden soll
	 * @param ortB
	 *            andere Ort des Korr. der eingerichtet werden soll
	 * @throws UngueltigerOrt
	 *             wird geworfen, wenn die beiden Orte der gleiche Ort sind
	 */

	private void ueberpruefeOrtUngleichheit(Ort ortA, Ort ortB)
			throws UngueltigerOrt {
		if (ortA == ortB) {
			throw new UngueltigerOrt(ortA, ortB, "OrtsGleichheit");
		}
	}
}
