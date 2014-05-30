package main;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import korridore.Korridor;
import orte.Ort;
import dateihandler.KartendateiHandler;
import dateihandler.NetzdateiHandler;
import dateihandler.SimulationsdateiHandler;
import dateihandler.TestdateiHandler;
import exceptions.DateiSyntaxFehler;
import exceptions.OrtNichtVorhanden;
import exceptions.UngueltigerOrt;

/**
 * @author BruecknerR, FechnerL, HandritschkP, TolleN erzeugt Karteninstanz,
 *         laesst sie samt der eingelesenen Kartendatei im KartenDateiHandler
 *         manipulieren, erzeugt Simulationsinstanz, laesst sie samt der
 *         eingelesenen Testdatei im TestDateiHandler extern manipulieren
 */
public class Main {
	public static void main(String[] args) {
		boolean dateiLesenErfolgreich = true;
		Benutzerinterface gui = new Benutzerinterface();
		Karte karte = new Karte();
		Simulator sim = new Simulator();
		SimulationsdateiHandler simSchreiber = new SimulationsdateiHandler(sim);
		gui.begruessung();
		double budget = gui.abfrageBudget();
		karte.setBudget(budget);
		do {
			try {
				File kartenDatei = gui.frageNachKartendatei();
				KartendateiHandler kartenVerarbeiter = new KartendateiHandler(
						karte, kartenDatei);
				kartenVerarbeiter.verarbeiteKartendatei();
				dateiLesenErfolgreich = true;
			} catch (DateiSyntaxFehler e) {
				e.zeigeFehlernachricht();
				dateiLesenErfolgreich = false;
			}
		} while (!dateiLesenErfolgreich);
		karte.erstelleNetz();
		gui.zeigeBaukosten(karte);
		int entscheidungNetzspeichern = gui.abfrageNetzSpeichern();
		if (entscheidungNetzspeichern == 0){
			NetzdateiHandler kartenSchreiber = new NetzdateiHandler(karte);
			kartenSchreiber.schreibeNetzdatei();
			System.exit(0);
		}
		do {
			try {
				File aktuelleTestdatei = gui.frageNachTestdatei();
				TestdateiHandler testVerarbeiter = new TestdateiHandler(
						aktuelleTestdatei, sim, karte);
				testVerarbeiter.verarbeiteTestdatei();
				dateiLesenErfolgreich = true;
			} catch (DateiSyntaxFehler e) {
				e.zeigeFehlernachricht();
				dateiLesenErfolgreich = false;
			}
		} while (!dateiLesenErfolgreich);
		sim.simuliere();
		gui.zeigeNutzkosten(sim);
		int endOption = gui.frageNachEndoption();
		if (endOption == 1) {
			NetzdateiHandler kartenSchreiber = new NetzdateiHandler(karte);
			kartenSchreiber.schreibeNetzdatei();
			simSchreiber.schreibeSimulationsDatei();
			sim.routen.clear();
		} else if (endOption == 0) {
			NetzdateiHandler kartenSchreiber = new NetzdateiHandler(karte);
			kartenSchreiber.schreibeNetzdatei();
			simSchreiber.schreibeSimulationsDatei();
			sim.routen.clear();
			System.exit(0);
		} else if (endOption == 2) {
			System.exit(0);
		}
		while (endOption == 1) {
			do {
				try {
					File aktuelleTestdatei = gui.frageNachTestdatei();
					TestdateiHandler testVerarbeiter = new TestdateiHandler(
							aktuelleTestdatei, sim, karte);
					testVerarbeiter.verarbeiteTestdatei();
					dateiLesenErfolgreich = true;
				} catch (DateiSyntaxFehler e) {
					e.zeigeFehlernachricht();
					dateiLesenErfolgreich = false;
				}
			} while (!dateiLesenErfolgreich);
			sim.simuliere();
			gui.zeigeNutzkosten(sim);
			endOption = gui.frageNachEndoption();
			if (endOption == 0) {
				simSchreiber.schreibeSimulationsDatei();
				sim.routen.clear();
				System.exit(0);
			}
			if (endOption == 1) {
				simSchreiber.schreibeSimulationsDatei();
				sim.routen.clear();
			}
			if (endOption == 2) {
				System.exit(0);
			}
		}

	}
}