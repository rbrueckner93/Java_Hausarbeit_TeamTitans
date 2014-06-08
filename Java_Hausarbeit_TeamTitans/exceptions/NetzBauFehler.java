package exceptions;

import javax.swing.JOptionPane;

import netz.Korridor;

public class NetzBauFehler extends Exception {
	
	private Korridor fehlerhafteVerbindung;

	public NetzBauFehler(Korridor fehlerhafteVerbindung) {
		super("Fehler im Netzbau entdeckt");
		this.fehlerhafteVerbindung = fehlerhafteVerbindung;
		// TODO Auto-generated constructor stub
	}

	public void zeigeFehlernachrichtVerbindungsProblem() {
		JOptionPane.showMessageDialog(null, "Fehler im Netz zwischen Ort \""
				+ fehlerhafteVerbindung.getOrtA() + "\" und Ort \""
				+ fehlerhafteVerbindung.getOrtB() + ".\nBitte den Service kontaktieren");
	}

}
