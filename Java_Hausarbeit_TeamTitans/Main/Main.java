package Main;

import java.io.File;

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
		// Begruessung des Benutzers durch einen freundlichen Dialog
		gui.begruessung();
		// Instanziierung des Kartenobjektes fuer diesen Programmablauf
		Karte map = new Karte();
		// Abfrage einer zu nutzenden Datei mit Kartendaten
		File kartenDatei = gui.frageNachKartendatei();
		// Instanziierung des KartendateiHandlers, der die Karte fuellt
		dateihandler.KartendateiHandler maphandler = new dateihandler.KartendateiHandler(
				map, kartenDatei);
		// TODO muss leseDatei hier ausgefuehrt werden?

		/**
		 * in diesem Abschnitt muss auf der Karte alles durchgefuehrt werden,
		 * Netzerstellung etc.
		 */

		// NACH ERSTELLUNG DES NETZES... SIMULIEREN

		// Abfrage einer zu nutzenden Datei mit Flugdaten=Testdaten
		File testDatei = gui.frageNachTestdatei();
		Simulator sim = new Simulator();
		dateihandler.TestdateiHandler testhandler = new dateihandler.TestdateiHandler(
				testDatei, sim);
		//schreibe die Flugrouten aus Datei in den Simulator
		testhandler.erzeugeFlugrouten();
		sim.simuliere();
		
		
		gui.zeigeNutzkosten(sim.ermittleNutzkosten());

	}
}