package main;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import korridore.Korridor;
import orte.Ort;
import dateihandler.KartendateiHandler;
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
		// TODO Einzelne Abschnitte gegen Fehler absichern
		Benutzerinterface gui = new Benutzerinterface();
		File datei = gui.frageNachKartendatei();
		Karte de = new Karte();
		KartendateiHandler verarbeiter = new KartendateiHandler(de, datei);
		verarbeiter.verarbeiteKartendatei();
		System.out.println(de.orte.size() + " erzeugte Orte.");
		//TODO nach budget fragen und bei Netz beruecksichtigen!
		//gui.frageNachBudget schreiben und an Netzerstellung uebergeben...
		de.erstelleNetz();
		
		Simulator sim;
		File simdatei;
		TestdateiHandler testverarbeiter;
		//TODO Speichern der Dateien nur auf Anweisung durchfuehren
		int entscheidung = 3;
		do{
			sim = new Simulator();
			simdatei = gui.frageNachTestdatei();
			testverarbeiter = new TestdateiHandler(simdatei, sim, de);
			testverarbeiter.verarbeiteTestdatei();
			sim.simuliere();
			entscheidung = gui.frageNachEndoption();
		}
		while(entscheidung == 1);
		if (entscheidung == 0){
			//speichern wir nun die Karte und fertig is'
			System.out.println("Sie haben abspeichern und schliessen gewaehlt...");
		}
		else{
			System.out.println("Sie haben programm schliessen gewaehlt.");
			
		}
	}
}