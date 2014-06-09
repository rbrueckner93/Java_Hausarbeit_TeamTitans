package orte;

public class Umschlagpunkt extends Ort {
	public double umschlagVolumen;
	public static final String BESCHREIBUNG = "Umschlagpunkt";

	/**
	 * Dient lediglich der Ermittlung des Relevanzgrades und erbt hauptsaechlich
	 * von Klasse Ort.
	 * 
	 * @author TolleN, HandritschkP
	 */
	public Umschlagpunkt(int koordX, int koordY, String name,
			double umschlagVolumen) {
		super(koordX, koordY, name);
		this.umschlagVolumen = umschlagVolumen;
		berechneRelevanzGrad();
		setKennung(KENNUNG_UMSCHLAGPUNKT);
	}

	/**
	 * Ermittelt den Relevanzgrad auf Basis der in der Aufgabenstellung
	 * vorgegebenen Rechenvorschrift Umschlagvolumen / 200000 und schreibt den
	 * Relevanzgrad in das Attribut der uebergeordneten Klasse Ort.
	 */
	private void berechneRelevanzGrad() {
		setRelevanzGrad((umschlagVolumen / 200000));
	}
}
