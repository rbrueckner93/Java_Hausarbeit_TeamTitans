package main;

import java.io.File;

import korridore.Korridor;
import orte.Ort;
import dateihandler.KartendateiHandler;
import dateihandler.NetzdateiHandler;
import dateihandler.SimulationsdateiHandler;
import dateihandler.TestdateiHandler;
import exceptions.OrtNichtVorhanden;

/**
 * @author BruecknerR, FechnerL, HandritschkP, TolleN erzeugt Karteninstanz,
 *         laesst sie samt der eingelesenen Kartendatei im KartenDateiHandler
 *         manipulieren, erzeugt Simulationsinstanz, laesst sie samt der
 *         eingelesenen Testdatei im TestDateiHandler extern manipulieren
 */
public class Main {
	public static void main(String[] args) {
		// TODO Einzelne Abschnitte gegen Fehler absichern
		Benutzerinterface gui = new Benutzerinterface();
		File datei = gui.frageNachKartendatei();
		Karte de = new Karte();
		KartendateiHandler verarbeiter = new KartendateiHandler(de, datei);
		verarbeiter.verarbeiteKartendatei();
		System.out.println(de.orte);
		de.erstelleNetz();
		
		do {
			Simulator sim = new Simulator();
			File simdatei = gui.frageNachTestdatei();
			TestdateiHandler testverarbeiter = new TestdateiHandler(simdatei, sim,
					de);
			testverarbeiter.verarbeiteTestdatei();
			sim.simuliere();

			NetzdateiHandler netzler = new NetzdateiHandler(de);
			netzler.schreibeNetzdatei();
			
			SimulationsdateiHandler simon = new SimulationsdateiHandler(sim);
			simon.schreibeSimulationsDatei();
		} while (gui.frageNachEndoption() == 1);
	}
}