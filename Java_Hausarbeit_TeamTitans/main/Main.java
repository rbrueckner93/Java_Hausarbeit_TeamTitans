package main;

import java.io.File;

import simulation.Simulator;
import netz.Karte;
import dateihandler.KartendateiHandler;
import dateihandler.NetzdateiHandler;
import dateihandler.SimulationsdateiHandler;
import dateihandler.TestdateiHandler;
import exceptions.DateiSyntaxFehler;
import exceptions.NetzBauFehler;
import exceptions.UngueltigerOrt;

/**
 * Diese Klasse beinhaltet den Gesamtablauf des Simulators inklusive aller
 * Interaktionen mit dem User. Ermoeglicht das Einlesen einer Karte und deren
 * Netzerstellung. Ermoeglicht, nur die Netzdatei zu speichern der Netzdatei.
 * Alternativ kann eine Testdatei eingelesen und Fluege auf deren Basis
 * simuliert werden. Das Speichern des Ergebnisses der Simulation_en ist ebenso
 * moeglich wie eine grobe Abschaetzung ueber die Qualitaet des Netzes (durch
 * die Ausgabe von eingerichteten Korridoren und den damit verbundenen
 * Baukosten).
 * 
 * @author TolleN
 */
public class Main {
	public static void main(String[] args) {
		try {
			boolean dateiLesenErfolgreich = true;
			// Erzeugung des Benutzerinterfaces.
			Benutzerinterface gui = new Benutzerinterface();
			// Erzeugung einer Karte.
			Karte karte = new Karte();
			// Erzeugung eines Simulators.
			Simulator sim = new Simulator();
			// Erzeugung eines Simulationsdateiverarbeiters.
			SimulationsdateiHandler simSchreiber = new SimulationsdateiHandler(
					sim);
			// User wird begruesst.
			gui.begruessung();
			// Frage nach dem Budget
			double budget = gui.abfrageBudget();
			karte.setBudget(budget);
			/*
			 * Fragen nach einer gueltigen Kartendatei, die dem vorgegebenen
			 * Format entspricht. Dies wird solange getan bis der User den
			 * Vorgang abbricht oder eine korrekte Datei eingelesen wurde.
			 */
			do {
				try {
					File kartenDatei = gui.frageNachKartendatei();
					KartendateiHandler kartenVerarbeiter = new KartendateiHandler(
							karte, kartenDatei);
					kartenVerarbeiter.verarbeiteKartendatei();
					dateiLesenErfolgreich = true;
				} catch (DateiSyntaxFehler e) {
					// Leeren der Ortsliste, um Orte, die bereits erstellt
					// wurden, zu entfernen.
					karte.getOrte().clear();
					dateiLesenErfolgreich = false;
				}
			} while (!dateiLesenErfolgreich);
			// Erstellung eines Netzes anhand der Karte und Budget.
			karte.erstelleNetz();
			// Meldung ueber die zu erwartenden Baukosten.
			gui.zeigeBaukosten(karte);
			// Abfrage ob nur das Netz gespeichert werden soll.
			int entscheidungNetzspeichern = gui.abfrageNetzSpeichern();
			if (entscheidungNetzspeichern == 0) {
				NetzdateiHandler kartenSchreiber = new NetzdateiHandler(karte);
				kartenSchreiber.schreibeNetzdatei();
				System.exit(0);
			}
			// Einlesen einer Testdatei. Gleiche Kriterien wie bei der
			// Kartendatei.
			do {
				try {
					File aktuelleTestdatei = gui.frageNachTestdatei();
					TestdateiHandler testVerarbeiter = new TestdateiHandler(
							aktuelleTestdatei, sim, karte);
					testVerarbeiter.verarbeiteTestdatei();
					dateiLesenErfolgreich = true;
				} catch (DateiSyntaxFehler e) {
					sim.getRouten().clear();
					dateiLesenErfolgreich = false;
				}
			} while (!dateiLesenErfolgreich);
			// Simulation der eingelesen Flugrouten über auf dem erstellten
			// Netz.
			sim.simuliere();
			// Anzeigen der Nutzkosten, inklusive der Budgetueberschreitung.
			gui.zeigeNutzkosten(sim, karte);
			// Fragen des User, wie weiter verfahren werden soll. Details in
			// Methode "frageNachEndoption".
			int endOption = gui.frageNachEndoption();
			// Speichern von Netz und Simulation, frage nach weiterer Testdatei.
			if (endOption == 1) {
				NetzdateiHandler kartenSchreiber = new NetzdateiHandler(karte);
				kartenSchreiber.schreibeNetzdatei();
				simSchreiber.schreibeSimulationsDatei();
				// Leeren der Liste eingelesener Flugrouten, um neue Flugrouten
				// aufzunehmen.
				sim.getRouten().clear();
				// Speichern von beiden Dateien und Beendigung des Programms.
			} else if (endOption == 0) {
				NetzdateiHandler kartenSchreiber = new NetzdateiHandler(karte);
				kartenSchreiber.schreibeNetzdatei();
				simSchreiber.schreibeSimulationsDatei();
				System.exit(0);
				// Programm wird nur beendet.
			} else if (endOption == 2) {
				System.exit(0);
			}
			/*
			 * Hier geht es nach EndOption = 1 weiter, mit der erneuten Frage
			 * nach einer Testdatei.
			 */
			while (endOption == 1) {
				do {
					try {
						File aktuelleTestdatei = gui.frageNachTestdatei();
						TestdateiHandler testVerarbeiter = new TestdateiHandler(
								aktuelleTestdatei, sim, karte);
						testVerarbeiter.verarbeiteTestdatei();
						dateiLesenErfolgreich = true;
					} catch (DateiSyntaxFehler e) {
						sim.getRouten().clear();
						dateiLesenErfolgreich = false;
					}
					/*
					 * Schleife ermoeglicht das wiederholte Einlesen und
					 * Simulieren von Testdateien.
					 */
				} while (!dateiLesenErfolgreich);
				// Erneute Simulation und Anzeigen der Nutzkosten.
				sim.simuliere();
				gui.zeigeNutzkosten(sim, karte);
				// Es folgt erneut die Frage, wie weiter verfahren werden soll.
				endOption = gui.frageNachEndoption();
				if (endOption == 0) {
					simSchreiber.schreibeSimulationsDatei();
					System.exit(0);
				}
				if (endOption == 1) {
					simSchreiber.schreibeSimulationsDatei();
					sim.getRouten().clear();
				}
				if (endOption == 2) {
					System.exit(0);
				}
			}
		} catch (NetzBauFehler e) {
			e.zeigeFehlernachrichtVerbindungsProblem();
			System.exit(0);
		} catch (UngueltigerOrt e){
			e.zeigeFehlernachricht();
			System.exit(0);
		}
	}
}