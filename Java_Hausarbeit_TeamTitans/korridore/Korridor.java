package korridore;

import orte.Ort;

/**
 * die den Unterklasse zugeordnete Methode ueberpruefeOrtart ueberprueft, ob die
 * Orte A und B fuer die jeweiligen Orte zulaessig ist, wenn nicht, gibt die
 * Methode ein false zurueck. wird aufgerufen durch die initialisierung eines
 * jeden korridors.
 */
public class Korridor {
	/* {author=TolleN, HandritschkP} */

	public Ort ortA;

	public Ort ortB;

	public double laenge;
	/**
	 * Konstruktor für einen allgemeinen Korridor.
	 * @param ortA
	 * @param ortB
	 */
	public Korridor(Ort ortA, Ort ortB) {
		super();
		this.ortA = ortA;
		this.ortB = ortB;
		ermittleLaenge();
	}

	public double getLaenge() {
		return laenge;
	}

	public void setLaenge(double laenge) {
		this.laenge = laenge;
	}

	public Ort getOrtA() {
		return ortA;
	}

	public Ort getOrtB() {
		return ortB;
	}

	public void setOrtA(Ort ortA) {
		this.ortA = ortA;
	}

	public void setOrtB(Ort ortB) {
		this.ortB = ortB;
	}

	public void ermittleLaenge() {
		double laengeQuadrat = Math.pow(ortA.koordX - ortB.koordX, 2)
				+ Math.pow(ortA.koordY - ortB.koordY, 2);
		setLaenge(Math.sqrt(laengeQuadrat));
	}
}