package orte;

public class Hauptort extends Ort {
	/* {author=TolleN, HandritschkP} */

	public int anzahlEinwohner;

	public static final String BESCHREIBUNG = "Hauptort";
	
	/**
	 * anzahlEinwohner wird aus Datei uebergeben relevanzGrad wird mit
	 * berechneRelevanzGrad berechnet 
	 * KENNUNG ist fuer Hauptort: HPT
	 */

	public Hauptort(int koordX, int koordY, String name, int anzahlEinwohner) {
		super(koordX, koordY, name);
		this.anzahlEinwohner = anzahlEinwohner;
		berechneRelevanzGrad();
		setKennung();
	}

	/**
	 * Relevanzgrad für HPT: teilt die Einwohner durch 15000
	 */
	
	private void berechneRelevanzGrad() {
		setRelevanzGrad((anzahlEinwohner / 15000));
	}

	/**
	 * Setzt die Kennung Klassenspezifisch.
	 */
	
	public void setKennung() {
		kennung = KENNUNG_HAUPTORT;
	}
}
