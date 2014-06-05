package korridore;

import java.util.ArrayList;

import exceptions.UngueltigerOrt;
import orte.Auslandsverbindung;
import orte.Hauptort;
import orte.Nebenort;
import orte.Ort;
import orte.Umschlagpunkt;

/**
 * die den Unterklasse zugeordnete Methode ueberpruefeOrtart ueberprueft, ob die
 * Orte A und B fuer die jeweiligen Orte zulaessig ist, wenn nicht, gibt die
 * Methode ein false zurueck. wird aufgerufen durch die initialisierung eines
 * jeden korridors.
 */
public class Korridor {
	/* {author=TolleN} */

	public Ort ortA;
	public Ort ortB;
	public double laenge;
	private String kennung;

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
	 * Konstruktor für einen allgemeinen Korridor.
	 * 
	 * @param ortA
	 * @param ortB
	 */
	public Korridor(Ort ortA, Ort ortB, String kennung) throws UngueltigerOrt {
		super();
		this.ortA = ortA;
		this.ortB = ortB;
		this.kennung = kennung;
		ermittleLaenge();
		ueberpruefeOrtart(ortA);
		ueberpruefeOrtart(ortB);
		ueberpruefeOrtUngleichheit(ortA, ortB);
		ortA.angebundeneKorridore.add(this);
		ortB.angebundeneKorridore.add(this);
	}

	public double getLaenge() {
		return laenge;
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

	public int getKorridorArtRang(String kennung) throws IllegalArgumentException{
		if(kennung == KENNUNG_HLST || kennung == KENNUNG_SICH) throw new IllegalArgumentException();
		switch (kennung) {
		case KENNUNG_ENFC:
			return 1;
		case KENNUNG_STND:
			return 2;
		}
		return 0;
	}

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

	public Ort bestimmeAnderenOrt(Ort bekanntesEnde) {
		Ort ermittelterOrt;
		if (ortA == bekanntesEnde) {
			ermittelterOrt = ortB;
		} else if (ortB == bekanntesEnde) {
			ermittelterOrt = ortA;
		} else {
			ermittelterOrt = null;
		}
		// hier vielleicht auch ne Exception oder sowas?

		return ermittelterOrt;
	}

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

	public void ermittleLaenge() {
		double laengeQuadrat = Math.pow(ortA.koordX - ortB.koordX, 2)
				+ Math.pow(ortA.koordY - ortB.koordY, 2);
		laenge = (Math.sqrt(laengeQuadrat));
	}

	public void ueberpruefeOrtart(Ort ortA) throws UngueltigerOrt {
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

	public void ueberpruefeOrtUngleichheit(Ort ortA, Ort ortB)
			throws UngueltigerOrt {
		if (ortA == ortB) {
			throw new UngueltigerOrt(ortA, ortB, "OrtsGleichheit");
		}
	}

}