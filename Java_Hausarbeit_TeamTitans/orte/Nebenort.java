package orte;

public class Nebenort extends Ort {
	/* {author=TolleN, HandritschkP} */

	public int anzahlEinwohner;

	public static final String BESCHREIBUNG = "Nebenort";

	public Nebenort(int koordX, int koordY, String name, String kennung,
			int anzahlEinwohner) {
		super(koordX, koordY, name, kennung);
		this.anzahlEinwohner = anzahlEinwohner;
		berechneRelevanzGrad();
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