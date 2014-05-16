package orte;

public class Umschlagpunkt extends Ort {
	/* {author=TolleN, HandritschkP} */

	public double umschlagVolumen;

	public double relevanzGrad;

	public static final String BESCHREIBUNG = "Umschlagpunkt";

	public static final String KENNUNG = "UMS";

	public Umschlagpunkt(int koordX, int koordY, String name,
			double umschlagVolumen) {
		super(koordX, koordY, name);
		this.umschlagVolumen = umschlagVolumen;
		berechneRelevanzGrad();
	}

	public double getRelevanzGrad() {
		return relevanzGrad;
	}

	public void setRelevanzGrad(double relevanzGrad) {
		this.relevanzGrad = relevanzGrad;
	}

	public static String getKennung() {
		return KENNUNG;
	}

	/**
	 * teilt Umschlagvolumen durch 200000
	 */
	public void berechneRelevanzGrad() {
		setRelevanzGrad((umschlagVolumen/200000));
	}

	/**
	 * umschlagVolumen wird aus Datei übergeben relevanzGrad wird aus
	 * umschlagVolumen durch berechneRelevanzGrad() berechnet beschreibung ist
	 * "Umschlagpunkt"
	 */
}