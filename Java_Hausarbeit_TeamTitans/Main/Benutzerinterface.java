package Main;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * lukas erzeugt in einigen Methoden Instanzen von JFileChooser, auf dieser
 * waehlt er eine Datei aus, faengt exceptions und gibt ein objekt vom Typ File
 * (frage nach kartendatei, frage nach Testdatei ) zurueck.
 */
public class Benutzerinterface {
	/* {author=FechnerL} */

	/**
	 * durch system.getproperties("User.Home") am Beginn des Programmes gesetzt.
	 */
	public static final String standardpfad = System.getProperty("user.home");

	/**
	 * Begruessung des Anwenders bei Start des Programms
	 */
	public void begruessung() {
		JOptionPane.showMessageDialog(null,
				"Hallo Anwender, \nTeam Titans wuenscht viel Spaß!");
	}

	/**
	 * Einlesen und ueberpruefen des Budgets: ganzzahlig und positiv
	 */
	public int abfrageBudget() {
		String budgetEingabe;
		int budgetInt = 0;
		while (true) {
			try {

				budgetEingabe = JOptionPane
						.showInputDialog("Wie hoch ist das Budget für den Netzbau?");

				if (budgetEingabe == null) {
					int beenden;
					beenden = JOptionPane.showConfirmDialog(null,
							"Wirklich beenden?", "Abbruch",
							JOptionPane.YES_NO_OPTION);
					if (beenden == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}
				budgetInt = Integer.parseInt(budgetEingabe);

				if (budgetInt > 0) {
					System.out.println(budgetInt);
					return budgetInt;
				} else {
					JOptionPane.showMessageDialog(null, "Das Budget sollte besser positiv sein!");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						"Das Budget sollte eine natürliche Zahl sein.");
			}
		}
	}

	/**
	 * fragt nach Testdatei, faengt alle Exceptions,
	 */
	public File frageNachTestdatei() {
		

			File testdateiFile ;
			JFileChooser chooser = new JFileChooser();

			while (true) {

				int status = chooser.showOpenDialog(null);

				if (status == JFileChooser.APPROVE_OPTION) {
					testdateiFile = chooser.getSelectedFile();
					if (testdateiFile.isFile() && testdateiFile.canRead()) {
						return testdateiFile;
					} else {
						JOptionPane.showMessageDialog(null,	"Sie sollten eine Kartendatei auswählen");
					}				
				} else { 	
					int beenden;
					beenden = JOptionPane.showConfirmDialog(null,
							"Wirklich beenden?", "Abbruch",
							JOptionPane.YES_NO_OPTION);
					if (beenden == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
					
				}
			}
	}

	/**
	 * Nimmt Nutzkosten von aktuellem Simulationsobjekt und gibt diese dem
	 * Anwender aus.
	 * 
	 * @param aktuelleSimulation
	 */
	public void zeigeNutzkosten(Simulator aktuelleSimulation) {

		double kosten = aktuelleSimulation.nutzkosten;
		JOptionPane.showMessageDialog(null, "Die Nutzkosten betragen " + kosten
				+ " Geldeinheiten.");
	}

	/**
	 * Ermittelt Baukosten der aktuellen Karte und zeigt dem Anwender diese.
	 * 
	 * @param aktuelleKarte
	 */
	public void zeigeBaukosten(Karte aktuelleKarte) {
		double baukosten = aktuelleKarte.ermittleGesamteBaukosten();
		JOptionPane.showMessageDialog(null, "Die Baukosten betragen "
				+ baukosten + " Geldeinheiten.");

	}

	public Benutzerinterface() {
	}

	/**
	 * 1) Abspeichern und schliessen 2) Simulation speichern und Karte mit
	 * anderen Testparametern simulieren 3) Programmabbruch
	 */
	public boolean frageNachEndoption() {
		return false;
	}

	/**
	 * soll alle Exceptions abfangen, falls keine Datei uebergeben wird, sich
	 * selbst neu aufrufen, falls abgebrochen wird, system.exit(0);
	 */
	public File frageNachKartendatei() {

		File kartenfile ;
		JFileChooser chooser = new JFileChooser();

		while (true) {

			int status = chooser.showOpenDialog(null);

			if (status == JFileChooser.APPROVE_OPTION) {
				kartenfile = chooser.getSelectedFile();
				if (kartenfile.isFile() && kartenfile.canRead()) {
					return kartenfile;
				} else {
					JOptionPane.showMessageDialog(null,	"Sie sollten eine Kartendatei auswählen");
				}				
			} else { 	
				int beenden;
				beenden = JOptionPane.showConfirmDialog(null,
						"Wirklich beenden?", "Abbruch",
						JOptionPane.YES_NO_OPTION);
				if (beenden == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
				
			}
		}
	}

}
