package orte;

public class Nebenort extends Ort {
	/* {author=TolleN, HandritschkP} */

	private int anzahlEinwohner;

	public static final String BESCHREIBUNG = "Nebenort";
	
	/**
	 * anzahlEinwohner wird aus Datei uebergeben relevanzGrad wird mit
	 * berechneRelevanzGrad berechnet 
	 * KENNUNG ist fuer Nebenort: NBN
	 */
	
	public Nebenort(int koordX, int koordY, String name,
			int anzahlEinwohner) {
		super(koordX, koordY, name);
		this.anzahlEinwohner = anzahlEinwohner;
		berechneRelevanzGrad();
		setKennung(KENNUNG_NEBENORT);
	}

	/**
	 * unterscheidet: E < 101 => 0 E > 100 => teilt Einwohner durch 25000
	 */
	
	private void berechneRelevanzGrad() {
		if (anzahlEinwohner < 101) {
			setRelevanzGrad(0);
		} else {
			setRelevanzGrad((anzahlEinwohner / 25000));
		}
	}
}
