package orte;

public class Hauptort extends Ort {
	/* {author=TolleN, HandritschkP} */

	public int anzahlEinwohner;

	public static final String BESCHREIBUNG = "Hauptort";

	/**
	 * anzahlEinwohner wird aus Datei übergeben relevanzGrad wird mit
	 * berechneRelevanzGrad berechnet BESCHREIBUNG ist Hauptort
	 */

	public Hauptort(int koordX, int koordY, String name, String kennung,
			int anzahlEinwohner) {
		super(koordX, koordY, name, kennung);
		this.anzahlEinwohner = anzahlEinwohner;
		berechneRelevanzGrad();
	}

	/**
	 * teilt die Einwohner durch 15000
	 */
	public void berechneRelevanzGrad() {
		setRelevanzGrad((anzahlEinwohner / 15000));
	}

}