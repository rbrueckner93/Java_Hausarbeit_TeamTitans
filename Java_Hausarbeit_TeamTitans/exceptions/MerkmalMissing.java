package exceptions;

import javax.swing.JOptionPane;

public class MerkmalMissing extends Exception {
	
	String merkmal;
	int zeile;

	public MerkmalMissing(String merkmal, int zeile) {
		super("Merkmal nicht gefunden im Datensatz");
		this.merkmal = merkmal;
		this.zeile = zeile;
	}
		
	public void erzeugeMeldung() throws DateiSyntaxFehler{
		JOptionPane.showMessageDialog(null, "Fehlendes oder Defektes Merkmal \"" +merkmal+"\"\nIn Datensatz ab Zeile: "+(zeile+1));
		throw new DateiSyntaxFehler();
	}
}
