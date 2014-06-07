package main;

import java.util.ArrayList;

/**
 * Berechnet ein gegebenes Netz anhand einer Datei mit voraussichtlichen
 * Belastungen der Relationen hinsichtlich der anfallenden Kosten.
 * 
 * @author BruecknerR
 */
public class Simulator {
	// Alle angelegten Routen werden in einer Liste gespeichert
	public ArrayList<Flugroute> routen;
	public String nameTestdatei;

	public Simulator() {
		super();
		routen = new ArrayList<Flugroute>();
	}

	public String getNameTestdatei() {
		return nameTestdatei;
	}

	public void setNameTestdatei(String nameTestdatei) {
		this.nameTestdatei = nameTestdatei;
	}

	/**
	 * fuehrt auf jeder Route, sich in der Liste routen der Instanz befindet,
	 * route.ermittleBesteRoute() aus.
	 */
	public void simuliere() {
		for (Flugroute route : routen) {
			route.ermittleBesteRoute();
		}
	}

	/**
	 * @return Summe aller Routennutzungskostenkosten
	 */
	public double ermittleNutzkosten() {
		double nutzkosten = 0.0;
		for (Flugroute route : routen) {
			nutzkosten += route.ermittleRoutennutzkosten();
		}
		return nutzkosten;
	}

}
