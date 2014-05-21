package main;

import java.io.File;

import korridore.Korridor;
import orte.Ort;
import dateihandler.KartendateiHandler;

/**
 * @author BruecknerR, FechnerL, HandritschkP, TolleN erzeugt Karteninstanz,
 *         laesst sie samt der eingelesenen Kartendatei im KartenDateiHandler
 *         manipulieren, erzeugt Simulationsinstanz, laesst sie samt der
 *         eingelesenen Testdatei im TestDateiHandler extern manipulieren
 */
public class Main {
	public static void main(String[] args) {
		// TODO Einzelne Abschnitte gegen Fehler absichern
		
		Karte de = new Karte();
		
//		System.out.println(de.orte);
//		for (Ort b : de.orte){
//			System.out.println(b.name);
//			System.out.println(b.kennung);
//		}
		
		System.out.println(de.berechneNetzMittelpunkt()); 
		
		
		
	}
}