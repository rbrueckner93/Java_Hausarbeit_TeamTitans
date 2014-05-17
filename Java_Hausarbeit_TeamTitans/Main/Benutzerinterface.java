package Main;

import java.io.File;

import javax.swing.JOptionPane;

import Exceptions.BudgetKleinerNull;

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
		int budgetInt=0;
		while (true) {
			try {
				
				budgetEingabe = JOptionPane.showInputDialog("Wie hoch ist das Budget für den Netzbau?");
				if (budgetEingabe == null) {
					int beenden;
					beenden = JOptionPane.showConfirmDialog(null,"Wirklich beenden?" , "Abbruch", JOptionPane.YES_NO_OPTION );
					if(beenden == JOptionPane.YES_OPTION) { System.exit(0);
					} 				
				}
				budgetInt = Integer.parseInt(budgetEingabe);
				System.out.println("hallo");
				if (budgetInt > 0) {
					System.out.println(budgetInt);
					return budgetInt;
				} else {
					JOptionPane.showMessageDialog(null, "negativ");
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
		return null;
	}

	public String zeigeNutzkosten() {
		return null;
	}

	public String zeigeBaukosten() {
		return null;
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
		return null;
	}

}