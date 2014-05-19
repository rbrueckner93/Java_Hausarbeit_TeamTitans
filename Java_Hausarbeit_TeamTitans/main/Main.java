package main;

import java.io.File;

import korridore.Korridor;
import orte.Ort;
import dateihandler.KartendateiHandler;
import dateihandler.TestdateiHandler;

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
		de.erzeugeNetz();
		Simulator sim = new Simulator();
		File simdatei = gui.frageNachTestdatei();
		TestdateiHandler testverarbeiter = new TestdateiHandler(simdatei, sim, de);
		testverarbeiter.verarbeiteTestdatei();
		for (Flugroute b : sim.routen){
			System.out.println(b.herkunft);
		}
//		Flugroute ab = new Flugroute(de.orte.get(6),de.orte.get(2),243);
//		ab.ermittleBesteRoute();
//		System.out.println(ab.erzeugeTextausgabeReiseroute());
	}
}