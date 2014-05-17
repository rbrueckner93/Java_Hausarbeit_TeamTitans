package orte;

/**
 * Klasse hat bis auf die Berechnung der Relvanzgrades keine eigenen Methoden,
 * sie dient vor allem der Aufnahme der Informationen aus der
 */
public class Auslandsverbindung extends Ort {
	/* {author=TolleN, HandritschkP} */

	public int passagierAufkommen;

	public double umschlagVolumen;

	public static final String BESCHREIBUNG = "Auslandsverbindung";
	
	public Auslandsverbindung(int koordX, int koordY, String name,
			String kennung, int passagierAufkommen, double umschlagVolumen) {
		super(koordX, koordY, name, kennung);
		this.passagierAufkommen = passagierAufkommen;
		this.umschlagVolumen = umschlagVolumen;
		berechneRelevanzGrad();
	}

	public double getRelevanzGrad() {
		return relevanzGrad;
	}

	/**
	 * relevanzGrad=(umschlagVolumen+3*passagierAufkommen)/1000000
	 */
	public void berechneRelevanzGrad() {
		setRelevanzGrad(((umschlagVolumen + (3 * passagierAufkommen)) / 1000000));
	}

}