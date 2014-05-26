package main;

import java.io.File;

import javax.annotation.processing.FilerException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author FechnerL
 * lukas erzeugt in einigen Methoden Instanzen von JFileChooser, auf dieser
 * waehlt er eine Datei aus, faengt exceptions und gibt ein objekt vom Typ File
 * (frage nach kartendatei, frage nach Testdatei ) zurueck.
 */
public class Benutzerinterface {

	/**
	 * durch system.getproperties("User.Home") am Beginn des Programmes gesetzt.
	 */
	public static final String standardpfad = System.getProperty("user.home");

	/**
	 * Begruessung des Anwenders bei Start des Programms
	 */
	public void begruessung() {
		JOptionPane.showMessageDialog(null,
				"Hallo Anwender, \nTeam Titans wuenscht viel Spass!");
	}

	/**
	 * Einlesen und ueberpruefen des Budgets: ganzzahlig und positiv
	 */
	public double abfrageBudget() {
		String budgetEingabe;
		double budget = 0;
		while (true) {
			try {

				budgetEingabe = JOptionPane
						.showInputDialog("Wie hoch ist das Budget fuer den Netzbau?");

				if (budgetEingabe == null) {
					int beenden;
					beenden = JOptionPane.showConfirmDialog(null,
							"Wirklich beenden?", "Abbruch",
							JOptionPane.YES_NO_OPTION);
					if (beenden == JOptionPane.YES_OPTION) {
						System.exit(0);

					} else if (beenden == JOptionPane.NO_OPTION) {
						continue;
					}
				}
				budget = Double.parseDouble(budgetEingabe);

				if (budget > 0) {
					System.out.println(budget);
					return budget;
				} else {
					JOptionPane.showMessageDialog(null,
							"Das Budget sollte besser positiv sein!");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						"Das Budget sollte eine natuerliche Zahl sein.");
			}
		}
	}

	/**
	 * fragt nach Testdatei, faengt alle Exceptions,
	 */
	public File frageNachTestdatei() {
		JOptionPane.showMessageDialog(null,
				"Bitte waehlen Sie nun eine Testdatei aus.");
		File testdateiFile;
		FileFilter txtfilter = new FileNameExtensionFilter("Testdatei", "txt");
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(txtfilter);
		chooser.setFileFilter(txtfilter);

		while (true) {

			int status = chooser.showOpenDialog(null);

			if (status == JFileChooser.APPROVE_OPTION) {
				testdateiFile = chooser.getSelectedFile();
				int dateiEndung = testdateiFile.getName().indexOf(".txt");
				if (dateiEndung == -1){
					JOptionPane.showMessageDialog(null, "Falsche Dateiendung der Datei");
					continue;
				}
				if (testdateiFile.isFile() && testdateiFile.canRead()) {
					return testdateiFile;
				} else {
					JOptionPane.showMessageDialog(null,
							"Sie sollten eine Kartendatei auswaehlen");
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

		double kosten = aktuelleSimulation.ermittleNutzkosten();
		JOptionPane.showMessageDialog(null, "Die Nutzkosten betragen " + kosten
				+ " Geldeinheiten.");
	}

	/**
	 * Ermittelt Baukosten der aktuellen Karte und zeigt dem Anwender diese. Zusaetzlich wird dem Anwender
	 * eine Auflistung der gebauten Korridore, gruppiert nach ihrer Art, ausgegeben.
	 * 
	 * @param aktuelleKarte
	 */
	public void zeigeBaukosten(Karte aktuelleKarte) {
		double baukosten = aktuelleKarte.ermittleGesamteBaukosten();
		JOptionPane.showMessageDialog(null, "Die Baukosten betragen "
				+ baukosten + " Geldeinheiten. \nFolgende Korridore wurden gebaut: \nEinfache Korridore: " +
		aktuelleKarte.ermittleAnzahlENFCKorridore() + "\nStandardkorridore: " + aktuelleKarte.ermittleAnzahlSTNDKorridore() 
		+ "\nHochleistungskorridore: " + aktuelleKarte.ermittleAnzahlHLSTKorridore() +
		"\nSicherheitskorridore: " + 	aktuelleKarte.ermittleAnzahlSICHKorridore());
		
	
		

	}

	public Benutzerinterface() {
	}

	/**
	 * uebergibt 0 fuer Abspeichern und schliessen, 1 fuer Simulation speichern und
	 * Karte mit anderen Testparametern simulieren und 2 fuer Programmabbruch
	 */
	public int frageNachEndoption() {
		int entscheidung;
		while (true) {
			String[] buttons = new String[] { "Abspeichern und schliessen",
					"Speichern und neue Simulation", "Programm schliessen" };
			entscheidung = JOptionPane.showOptionDialog(null,
					"Wie soll es weitergehen?", "Und nun?",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, buttons, buttons[0]);
			if (entscheidung == -1) {
				{
					int beenden;
					beenden = JOptionPane.showConfirmDialog(null,
							"Wirklich beenden?", "Abbruch",
							JOptionPane.YES_NO_OPTION);
					if (beenden == JOptionPane.YES_OPTION) {
						System.exit(0);
					}

				}
			} else {
				return entscheidung;
			}
		}
	}

	/**
	 * soll alle Exceptions abfangen, falls keine Datei uebergeben wird, sich
	 * selbst neu aufrufen, falls abgebrochen wird, system.exit(0);
	 */
	public File frageNachKartendatei() {

		JOptionPane.showMessageDialog(null,
				"Bitte waehlen Sie eine Kartendatei aus.");
		File kartenfile;
		FileFilter txtfilter = new FileNameExtensionFilter("Kartendatei", "txt");
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(txtfilter);
		chooser.setFileFilter(txtfilter);

		while (true) {

			int status = chooser.showOpenDialog(null);

			if (status == JFileChooser.APPROVE_OPTION) {
				kartenfile = chooser.getSelectedFile();
				int dateiEndung = kartenfile.getName().indexOf(".txt");
				if (dateiEndung == -1){
					JOptionPane.showMessageDialog(null, "Falsche Dateiendung der Datei");
					continue;
				}
				if (kartenfile.isFile() && kartenfile.canRead()) {
					return kartenfile;
				} else {
					JOptionPane.showMessageDialog(null,
							"Sie sollten eine Kartendatei auswaehlen");
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