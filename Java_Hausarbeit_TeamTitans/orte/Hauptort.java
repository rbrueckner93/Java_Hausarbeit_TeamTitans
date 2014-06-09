package orte;

public class Hauptort extends Ort {
	private int anzahlEinwohner;
	public static final String BESCHREIBUNG = "Hauptort";

	/**
	 * Dient lediglich der Ermittlung des Relevanzgrades und erbt hauptsächlich
	 * von Klasse Ort.
	 * 
	 * @author TolleN, HandritschkP
	 */
	public Hauptort(int koordX, int koordY, String name, int anzahlEinwohner) {
		super(koordX, koordY, name);
		this.anzahlEinwohner = anzahlEinwohner;
		berechneRelevanzGrad();
		setKennung(KENNUNG_HAUPTORT);
	}

	/**
	 * Ermittelt den Relevanzgrad auf Basis der in der Aufgabenstellung
	 * vorgegebenen Rechenvorschrift und schreibt den Relevanzgrad in das
	 * Attribut der uebergeordneten Klasse Ort.
	 * 
	 * Der Relevanzgrad ist definiert durch Einwohner geteilt durch 15000.
	 */
	private void berechneRelevanzGrad() {
		setRelevanzGrad((anzahlEinwohner / 15000));
	}
}
