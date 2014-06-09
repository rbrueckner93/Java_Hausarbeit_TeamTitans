package orte;

public class Auslandsverbindung extends Ort {
	private int passagierAufkommen;
	private double umschlagVolumen;
	public static final String BESCHREIBUNG = "Auslandsverbindung";

	/**
	 * Dient lediglich der Ermittlung des Relevanzgrades und erbt hauptsaechlich
	 * von Klasse Ort.
	 * 
	 * @author TolleN, HandritschkP
	 */
	public Auslandsverbindung(int koordX, int koordY, String name,
			int passagierAufkommen, double umschlagVolumen) {
		super(koordX, koordY, name);
		this.passagierAufkommen = passagierAufkommen;
		this.umschlagVolumen = umschlagVolumen;
		berechneRelevanzGrad();
		setKennung(KENNUNG_AUSLANDSVERBINDUNG);
	}

	/**
	 * Ermittelt den Relevanzgrad auf Basis der in der Aufgabenstellung
	 * vorgegebenen Rechenvorschrift und schreibt den Relevanzgrad in das
	 * Attribut der uebergeordneten Klasse Ort.
	 * relevanzGrad=(umschlagVolumen+3*passagierAufkommen)/1000000
	 */
	private void berechneRelevanzGrad() {
		setRelevanzGrad(((umschlagVolumen + (3 * passagierAufkommen)) / 1000000));
	}
}