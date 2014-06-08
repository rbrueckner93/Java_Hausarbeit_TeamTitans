package exceptions;

import javax.swing.JOptionPane;
import netz.Korridor;

public class NetzBauFehler extends Exception {
	/* {author TolleN} */

	private Korridor fehlerhafteVerbindung;

	/**
	 * Art des Fehler 0 = Ein nicht endgueltiger Korridor wurde entdeckt. 1 =
	 * Angebundene Korridore eines Ortes enthalten unbeaknnten Korridor.
	 */
	private int fehlerTyp;

	/**
	 * Fehler, der bei inkorrektem Netzbau geworfen wird. Erwartet den Korridor
	 * mit spez Fehlercode.
	 * 
	 * @param fehlerhafteVerbindung
	 *            Korridor mit Fehler
	 * @param fehlerTyp
	 *            Art des Fehlers
	 */
	public NetzBauFehler(Korridor fehlerhafteVerbindung, int fehlerTyp) {
		super("Fehler im Netzbau entdeckt");
		this.fehlerhafteVerbindung = fehlerhafteVerbindung;
		this.fehlerTyp = fehlerTyp;
		// TODO Auto-generated constructor stub
	}

	public void zeigeFehlernachrichtVerbindungsProblem() {
		switch (fehlerTyp) {
		case 0:
			JOptionPane
					.showMessageDialog(null,
							"Fehler bei Netzerstellung.\nKorridor zwischen Ort \""
									+ fehlerhafteVerbindung.getOrtA().getName()
									+ "\" und Ort \""
									+ fehlerhafteVerbindung.getOrtB().getName()
									+ "\" ist nicht endgueltig.\nBitte Service kontaktieren.");
			break;
		case 1:
			JOptionPane.showMessageDialog(null,
					"Fehler bei der Netzerstellung.\nKorridor zwischen Ort \""
									+ fehlerhafteVerbindung.getOrtA().getName()
									+ "\" und Ort \""
									+ fehlerhafteVerbindung.getOrtB().getName()
									+ "\" ist dem Netz unbekannt.\nBitte Service kontaktieren");
			break;
		default:
			JOptionPane.showMessageDialog(null,
					"Fehler bei der Netzerstellung zwischen Ort \""
							+ fehlerhafteVerbindung.getOrtA().getName()
							+ "\" und Ort \""
							+ fehlerhafteVerbindung.getOrtB().getName()
							+ "\".\nBitte den Service kontaktieren");
			break;
		}
	}

}
