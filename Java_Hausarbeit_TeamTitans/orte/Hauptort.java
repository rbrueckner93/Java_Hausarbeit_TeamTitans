package orte;

public class Hauptort extends Ort {
	/* {author=TolleN, HandritschkP} */

	public int anzahlEinwohner;

	public double relevanzGrad;

	public static final String BESCHREIBUNG = "Hauptort";

	public static final String KENNUNG = "HPT";

	/**
	 * anzahlEinwohner wird aus Datei übergeben relevanzGrad wird mit
	 * berechneRelevanzGrad berechnet BESCHREIBUNG ist Hauptort
	 */

	public Hauptort(int koordX, int koordY, String name, int anzahlEinwohner) {
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
	 * teilt die Einwohner durch 15000
	 */
	public void berechneRelevanzGrad() {
		setRelevanzGrad((anzahlEinwohner / 15000));
	}

}