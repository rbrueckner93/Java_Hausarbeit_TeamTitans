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
			Benutzerinterface gui = new Benutzerinterface();
			Karte karte = new Karte();
			Simulator sim = new Simulator();
			SimulationsdateiHandler simSchreiber = new SimulationsdateiHandler(sim);
			gui.begruessung();
			File kartenDatei = gui.frageNachKartendatei();
			KartendateiHandler kartenVerarbeiter = new KartendateiHandler(karte, kartenDatei);
			double budget = gui.abfrageBudget();
			karte.setBudget(budget);
			kartenVerarbeiter.verarbeiteKartendatei();
		//	karte.erstelleNetz();
			NetzdateiHandler kartenSchreiber = new NetzdateiHandler(karte);
			kartenSchreiber.schreibeNetzdatei();
			do {
				File aktuelleTestdatei = gui.frageNachTestdatei();
				TestdateiHandler testVerarbeiter = new TestdateiHandler(aktuelleTestdatei, sim, karte);
				testVerarbeiter.verarbeiteTestdatei();
				sim.simuliere();
				simSchreiber.schreibeSimulationsDatei();
				sim.routen.clear();
			} while (gui.frageNachEndoption() == 1);
		}
	}