package orte;

public class Umschlagpunkt extends Ort {
	/* {author=TolleN, HandritschkP} */

	public double umschlagVolumen;

	public static final String BESCHREIBUNG = "Umschlagpunkt";
	
	/**
	 * umschlagVolumen wird aus Datei uebergeben relevanzGrad wird mit
	 * berechneRelevanzGrad berechnet 
	 * KENNUNG ist fuer Nebenort: UMS
	 */
	public Umschlagpunkt(int koordX, int koordY, String name,
			double umschlagVolumen) {
		super(koordX, koordY, name);
		this.umschlagVolumen = umschlagVolumen;
		berechneRelevanzGrad();
		setKennung(KENNUNG_UMSCHLAGPUNKT);
	}

	/**
	 * teilt Umschlagvolumen durch 200000
	 */
	private void berechneRelevanzGrad() {
		setRelevanzGrad((umschlagVolumen / 200000));
	}
}
