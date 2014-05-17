package orte;

public class Umschlagpunkt extends Ort {
	/* {author=TolleN, HandritschkP} */

	public double umschlagVolumen;

	public static final String BESCHREIBUNG = "Umschlagpunkt";

	/**
	 * umschlagVolumen wird aus Datei übergeben relevanzGrad wird aus
	 * umschlagVolumen durch berechneRelevanzGrad() berechnet beschreibung ist
	 * "Umschlagpunkt"
	 */
	public Umschlagpunkt(int koordX, int koordY, String name, String kennung,
			double umschlagVolumen) {
		super(koordX, koordY, name, kennung);
		this.umschlagVolumen = umschlagVolumen;
		berechneRelevanzGrad();
	}

	/**
	 * teilt Umschlagvolumen durch 200000
	 */
	public void berechneRelevanzGrad() {
		setRelevanzGrad((umschlagVolumen / 200000));
	}


}