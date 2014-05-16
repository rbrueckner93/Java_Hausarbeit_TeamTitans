package orte;

public class Nebenort extends Ort {
	/* {author=TolleN, HandritschkP} */

	public int anzahlEinwohner;

	public double relevanzGrad;

	public static final String BESCHREIBUNG = "Nebenort";

	public static final String KENNUNG = "NBN";

	public Nebenort(int koordX, int koordY, String name, int anzahlEinwohner) {
		super(koordX, koordY, name);
		this.anzahlEinwohner = anzahlEinwohner;
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
	 * unterscheidet: E < 101 => 0 E > 100 => teilt Einwohner durch 25000
	 */
	public void berechneRelevanzGrad() {
		if (anzahlEinwohner < 101) {
			setRelevanzGrad(0);
		} else {
			setRelevanzGrad((anzahlEinwohner / 25000));
		}
	}

}