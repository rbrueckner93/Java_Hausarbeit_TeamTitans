package exceptions;

import javax.swing.JOptionPane;

/**
 * Exception die ein Fehlendes Merkmal meldet. Benötigt zur korrekten erzeugung
 * einer Fehlermeldung die Merkmalbezeichnung und das aktuelle Element der
 * ArrayList.
 * 
 * @author Nils
 * 
 */
public class MerkmalMissing extends Exception {

	String merkmal;
	int zeile;

	public MerkmalMissing(String merkmal, int zeile) {
		super("Merkmal nicht gefunden im Datensatz");
		this.merkmal = merkmal;
		this.zeile = zeile;
	}

	/**
	 * Erzeugt eine Fehlermeldung fuer den User.
	 * @throws DateiSyntaxFehler
	 */
	public void erzeugeMeldung() throws DateiSyntaxFehler {
		JOptionPane.showMessageDialog(null,
				"Fehlendes oder defektes Merkmal \"" + merkmal
						+ "\"\nIn Datensatz ab Zeile: " + (zeile + 1));
		throw new DateiSyntaxFehler();
	}
}
