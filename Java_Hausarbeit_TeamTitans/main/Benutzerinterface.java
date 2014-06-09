package main;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulation.Simulator;
import netz.Karte;
import dateihandler.Datei;

/**
 *  	   Die Klasse Benutzerinterface ist fuer die Kommunikation des
 *         Programms mit dem Anwender zustaendig. Hierzu gehoert einerseits das
 *         Fragen nach Dateien oder bestimmten Informationen, deren
 *         Ueberpruefung und die Weitergabe. Andererseits sind auch die Methoden
 *         fuer die Ausgabe an den Anwender beinhaltet.
 *         @author FechnerL
 */
public class Benutzerinterface {

	/**
	 * Hier wird der Standardpfad gesetzt, um mit diesem spaeter die Dateiauswahl
	 * durchzufuehren.
	 */
	public static final String STANDARDPFAD = System.getProperty("user.home")
			+ "\\deerone";
	public static final String WAEHRUNG = "EUR";

	public Benutzerinterface() {
	}

	/**
	 * Nach dem Start des Programms wird der Anwender begruesst.
	 */
	public void begruessung() {
		JOptionPane.showMessageDialog(null,
				"Hallo Anwender, \nTeam Titans wuenscht viel Spass!");
	}

	/**
	 * Fragt den Anwender nach einer Kartendatei, um diese zur Auswertung
	 * weiterzugeben. Prueft, ob es sich tatsaechlich um eine Textatei handelt
	 * und ob auf die Datei zugegriffen werden kann.
	 */
	public File frageNachKartendatei() {

		JOptionPane.showMessageDialog(null,
				"Bitte waehlen Sie eine Kartendatei aus.");
		File kartenfile;
		FileFilter txtfilter = new FileNameExtensionFilter("Kartendatei", "txt");
		JFileChooser chooser = new JFileChooser(STANDARDPFAD);
		chooser.addChoosableFileFilter(txtfilter);
		chooser.setFileFilter(txtfilter);

		while (true) {

			int status = chooser.showOpenDialog(null);

			if (status == JFileChooser.APPROVE_OPTION) {
				kartenfile = chooser.getSelectedFile();
				int dateiEndung = kartenfile.getName().indexOf(
						Datei.STANDARD_DATEITYP);
				if (dateiEndung == -1) {
					JOptionPane.showMessageDialog(null,
							"Falsche Dateiendung der Datei");
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

	/**
	 * Der Anwender wird nach dem zur Verfuegung stehenden Budget gefragt. nach
	 * der Eingabe wird geprueft, ob dieses ganzzahlig und positiv ist.
	 */
	public double abfrageBudget() {
		String budgetEingabe;
		double budget = 0;
		while (true) {
			try {

				budgetEingabe = JOptionPane
						.showInputDialog("Wie hoch ist das Budget fuer den Netzbau in "
								+ WAEHRUNG + " ?");

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
	 * Ermittelt die Baukosten des Netzes zur aktuellen Karte und zeigt dem
	 * Anwender diese. Zusaetzlich wird dem Anwender eine Auflistung der
	 * gebauten Korridore, gruppiert nach ihrer Art, ausgegeben.
	 * 
	 * @param aktuelleKarte
	 */
	public void zeigeBaukosten(Karte aktuelleKarte) {
		double baukosten = aktuelleKarte.ermittleGesamteBaukosten();

		// Runden von baukosten auf 2 Nachkommastellen
		baukosten = baukosten * 100;
		baukosten = Math.round(baukosten);
		baukosten = baukosten / 100;
		if (baukosten - aktuelleKarte.getBudget() > 350000) {
			JOptionPane.showMessageDialog(null,
					"Das Budget war leider nicht ausreichend, "
							+ "um ein wirtschaftsliches Netz zu erstellen!\nEs wurde ein minimal wirtschaftliches Netz erstellt.");
		}
		JOptionPane
				.showMessageDialog(
						null,
						"Die Baukosten betragen "
								+ baukosten
								+ " "
								+ WAEHRUNG
								+ " . \nFolgende Korridore wurden gebaut: \nEinfachekorridore: "
								+ aktuelleKarte.ermittleAnzahlENFCKorridore()
								+ "\nStandardkorridore: "
								+ aktuelleKarte.ermittleAnzahlSTNDKorridore()
								+ "\nHochleistungskorridore: "
								+ aktuelleKarte.ermittleAnzahlHLSTKorridore()
								+ "\nSicherheitskorridore: "
								+ aktuelleKarte.ermittleAnzahlSICHKorridore());
	}

	/**
	 * Nach der Erstellung des Netzes durch Korridore wird der Anwender gefragt,
	 * ob das Netz gespeichert und das Programm beendet werden soll. Bei Ja wird
	 * Karte gespeichert und das Programm geschlossen. Bei Nein fährt das
	 * Programm fort und fragt nach einer Testdatei.
	 * 
	 * @return
	 */
	// Ja uebergibt 0 und Nein 1
	public int abfrageNetzSpeichern() {
		int entscheidung;

		while (true) {
			entscheidung = JOptionPane.showConfirmDialog(null,
					"Wollen Sie das Netz speichern und beenden?",
					"Speichern und beenden?", JOptionPane.YES_NO_OPTION);
			if (entscheidung == -1) {

				int beenden;
				beenden = JOptionPane.showConfirmDialog(null,
						"Wirklich beenden?", "Abbruch",
						JOptionPane.YES_NO_OPTION);
				if (beenden == JOptionPane.YES_OPTION) {
					System.exit(0);
				}

			} else {

				return entscheidung;
			}

		}
	}

	/**
	 * Fragt den Anwender nach einer Testdatei, um diese zur Auswertung
	 * weiterzugeben. Prueft, ob es sich tatsaechlich um eine Textdatei handelt
	 * und ob auf die Datei zugegriffen werden kann.
	 */
	public File frageNachTestdatei() {
		JOptionPane.showMessageDialog(null,
				"Bitte waehlen Sie eine Testdatei aus.");
		File testdateiFile;
		FileFilter txtfilter = new FileNameExtensionFilter("Testdatei", "txt");
		JFileChooser chooser = new JFileChooser(STANDARDPFAD);
		chooser.addChoosableFileFilter(txtfilter);
		chooser.setFileFilter(txtfilter);

		while (true) {

			int status = chooser.showOpenDialog(null);

			if (status == JFileChooser.APPROVE_OPTION) {
				testdateiFile = chooser.getSelectedFile();
				int dateiEndung = testdateiFile.getName().indexOf(
						Datei.STANDARD_DATEITYP);
				if (dateiEndung == -1) {
					JOptionPane.showMessageDialog(null,
							"Falsche Dateiendung der Datei");
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
	public void zeigeNutzkosten(Simulator aktuelleSimulation,
			Karte aktuelleKarte) {

		double kosten = aktuelleSimulation.ermittleNutzkosten();
		if (aktuelleKarte.ermittleGesamteBaukosten() > aktuelleKarte
				.getBudget()) {
			double budgetUeberschreitung = (aktuelleKarte
					.ermittleGesamteBaukosten() - aktuelleKarte.getBudget());
			kosten += budgetUeberschreitung;
		}
		// kosten auf 2 Nachkommastellen runden
		kosten = kosten * 100;
		kosten = Math.round(kosten);
		kosten = kosten / 100;

		JOptionPane.showMessageDialog(null, "Die Nutzkosten betragen " + kosten
				+ WAEHRUNG + ".");
	}

	/**
	 * Fragt den Anwender nach dem weiteren Vorgehen, wobei es drei
	 * Moeglichkeiten gibt. Uebergibt: 0, Wenn die aktuelle Simulation als Datei
	 * gespeichert werden soll. 1, um die aktuelle Simulation zu speichern und
	 * zusaetzlich eine neue Simulation mit anderen Testparametern zu starten
	 * und 2, um das Programm zu beenden.
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

}