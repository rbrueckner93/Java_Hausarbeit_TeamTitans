package orte;

public class Umschlagpunkt extends Ort {
	/* {author=TolleN, HandritschkP} */

	public double umschlagVolumen;

	public static final String BESCHREIBUNG = "Umschlagpunkt";
	
	public static final String KENNUNG = "UMS";

	/**
	 * umschlagVolumen wird aus Datei übergeben relevanzGrad wird aus
	 * umschlagVolumen durch berechneRelevanzGrad() berechnet beschreibung ist
	 * "Umschlagpunkt"
	 */
	public Umschlagpunkt(int koordX, int koordY, String name,
			double umschlagVolumen) {
		super(koordX, koordY, name);
		this.umschlagVolumen = umschlagVolumen;
		berechneRelevanzGrad();
		setKennung();
	}

	/**
	 * teilt Umschlagvolumen durch 200000
	 */
	public void berechneRelevanzGrad() {
		setRelevanzGrad((umschlagVolumen / 200000));
	}
	/**
	 * Setzt die Kennung Klassenspezifisch.
	 */
	public void setKennung() {
		kennung = KENNUNG;
	}

}