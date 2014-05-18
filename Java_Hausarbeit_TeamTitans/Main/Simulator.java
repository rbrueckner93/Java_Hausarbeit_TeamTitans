package Main;

import java.util.ArrayList;

/**
 * @author BruecknerR Das berechnete Netz wird anhand einer Testdatei berechnet
 *         hinsichtlich der anfallenden Kosten.
 */
public class Simulator {

	// Alle angelegten Routen werden in einer Liste gespeichert
	public ArrayList<Flugroute> routen;

	/**
	 * speichert die Information ueber die verwendete Testdatei fuer die
	 * Simulation.
	 */
	public String nameTestdatei;

	// Leerer Konstruktor der Klasse.
	public Simulator() {
		super();
	}

	public String getNameTestdatei() {
		return nameTestdatei;
	}

	public void setNameTestdateiHandler(String nameTestdatei) {
		this.nameTestdatei = nameTestdatei;
	}

	/**
	 * fuehrt auf jeder Route, sich in seiner liste routen befindet,
	 * route.ermittleBesteRoute() aus.
	 */
	public void simuliere() {
		for (Flugroute route : routen) {
			route.ermittleBesteRoute();
		}
	}

	/**
	 * mit dieser Methode werden die Nutzkosten aller Routen aufsummiert.
	 */
	public double ermittleNutzkosten() {
		double nutzkosten = 0.0;
		for (Flugroute route : routen) {
			nutzkosten += route.ermittleRoutennutzkosten();
		}
		return nutzkosten;
	}

}