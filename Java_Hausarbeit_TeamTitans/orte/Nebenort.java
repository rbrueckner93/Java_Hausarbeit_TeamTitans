package orte;

public class Nebenort extends Ort {
	private int anzahlEinwohner;
	public static final String BESCHREIBUNG = "Nebenort";

	/**
	 * Dient lediglich der Ermittlung des Relevanzgrades und erbt hauptsaechlich
	 * von Klasse Ort.
	 * 
	 * @author TolleN, HandritschkP
	 */
	public Nebenort(int koordX, int koordY, String name, int anzahlEinwohner) {
		super(koordX, koordY, name);
		this.anzahlEinwohner = anzahlEinwohner;
		berechneRelevanzGrad();
		setKennung(KENNUNG_NEBENORT);
	}

	/**
	 * Ermittelt den Relevanzgrad auf Basis der in der Aufgabenstellung
	 * vorgegebenen Rechenvorschrift und schreibt den Relevanzgrad in das
	 * Attribut der uebergeordneten Klasse Ort.
	 * 
	 * Unterscheidet: E < 101 => 0 E > 100 => teilt Einwohner durch 25000
	 */
	private void berechneRelevanzGrad() {
		if (anzahlEinwohner < 101) {
			setRelevanzGrad(0);
		} else {
			setRelevanzGrad((anzahlEinwohner / 25000));
		}
	}
}
