package Main;

import java.io.File;

/**
 * @author BruecknerR, FechnerL, HandritschkP, TolleN
 * erzeugt Karteninstanz, laesst sie samt der eingelesenen Kartendatei im
 * KartenDateiHandler manipulieren, erzeugt Simulationsinstanz, laesst sie samt
 * der eingelesenen Testdatei im TestDateiHandler extern manipulieren
 */
public class Main {
	public static void main(String[] args) {
		Benutzerinterface gui = new Benutzerinterface();
		// Begruessung des Benutzers durch einen freundlichen Dialog
		gui.begruessung();
		// Instanziierung des Kartenobjektes fuer diesen Programmablauf
		Karte map = new Karte();
		// Abfrage einer zu nutzenden Datei mit Kartendaten
		File kartenDatei = gui.frageNachKartendatei();
		// Instanziierung des KartendateiHandlers, der die Karte fuellt
		dateihandler.KartendateiHandler maphandler = new dateihandler.KartendateiHandler(
				map, kartenDatei);
		/**
		 * erzeugeOrte könnte die Aufgabe übernehmen, alle anderen Aktionen, die
		 * vor dem eigentlichen erzeugen der Orte geschehen: ueberpruefen der
		 * Karte etc.
		 */
		maphandler.erzeugeOrte();
		/**
		 * erzeugen der verschiedenen Korridore mit Hilfe von erzeugeKorridor()
		 */
		map.erzeugeNetz();
	}
}