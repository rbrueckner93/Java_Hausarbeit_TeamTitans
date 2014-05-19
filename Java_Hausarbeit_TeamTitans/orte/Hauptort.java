package orte;

public class Hauptort extends Ort {
	/* {author=TolleN, HandritschkP} */

	public int anzahlEinwohner;

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
		setKennung();
	}

	/**
	 * teilt die Einwohner durch 15000
	 */
	public void berechneRelevanzGrad() {
		setRelevanzGrad((anzahlEinwohner / 15000));
	}

	/**
	 * Setzt die Kennung Klassenspezifisch.
	 */
	public void setKennung() {
		kennung = KENNUNG;
	}

}