package orte;

/**
 * Klasse hat bis auf die Berechnung der Relvanzgrades keine eigenen Methoden,
 * sie dient vor allem der Aufnahme der Informationen aus der
 */
public class Auslandsverbindung extends Ort {
	/* {author=TolleN, HandritschkP} */

	private int passagierAufkommen;

	private double umschlagVolumen;

	public static final String BESCHREIBUNG = "Auslandsverbindung";
	
	public Auslandsverbindung(int koordX, int koordY, String name,
			 int passagierAufkommen, double umschlagVolumen) {
		super(koordX, koordY, name);
		this.passagierAufkommen = passagierAufkommen;
		this.umschlagVolumen = umschlagVolumen;
		berechneRelevanzGrad();
		setKennung(KENNUNG_AUSLANDSVERBINDUNG);
	}

	/**
	 * relevanzGrad=(umschlagVolumen+3*passagierAufkommen)/1000000
	 */
	private void berechneRelevanzGrad() {
		setRelevanzGrad(((umschlagVolumen + (3 * passagierAufkommen)) / 1000000));
	}
}