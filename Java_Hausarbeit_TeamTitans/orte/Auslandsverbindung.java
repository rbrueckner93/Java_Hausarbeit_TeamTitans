package orte;

/**
 * Klasse hat bis auf die Berechnung der Relvanzgrades keine eigenen Methoden,
 * sie dient vor allem der Aufnahme der Informationen aus der
 */
public class Auslandsverbindung extends Ort {
	/* {author=TolleN, HandritschkP} */

	public int passagierAufkommen;

	public double umschlagVolumen;

	public double relevanzGrad;

	public static final String BESCHREIBUNG = "Auslandsverbindung";

	public static final String KENNUNG = "ASL";

	public Auslandsverbindung(int koordX, int koordY, String name,
			int passagierAufkommen, double umschlagVolumen) {
		super(koordX, koordY, name);
		this.passagierAufkommen = passagierAufkommen;
		this.umschlagVolumen = umschlagVolumen;
		berechneRelevanzGrad();
	}

	public double getRelevanzGrad() {
		return relevanzGrad;
	}

	public void setRelevanzGrad(double relevanzGrad) {
		this.relevanzGrad = relevanzGrad;
	}

	public static String getKennung() {
		return KENNUNG;
	}

	/**
	 * relevanzGrad=(umschlagVolumen+3*passagierAufkommen)/1000000
	 */
	public void berechneRelevanzGrad() {
		setRelevanzGrad(((umschlagVolumen + (3 * passagierAufkommen)) / 1000000));
	}

}