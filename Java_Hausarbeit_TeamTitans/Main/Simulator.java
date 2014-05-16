package Main;
import java.util.ArrayList;

/**
 * Das berechnete Netz wird anhand einer TestdateiHandler berechnet hinsichtlich
 * der anfallenden Kosten.
 */
public class Simulator {
	/* {author=BruecknerR} */

	/**
	 * in diesem Attribut werden die Nutzkosten aller Routen aufsummiert. Dies tut die KLasse selber.
	 */
	public static double nutzkosten;

	public ArrayList<Flugroute> routen;

	/**
	 * speichert die Information ueber die verwendete TestdateiHandler fuer die
	 * Simulation.
	 */
	public String nameTestdateiHandler;

	// Leerer Konstruktor der Klasse.
	public Simulator() {
		super();
	}

	public String getNameTestdateiHandler() {
		return nameTestdateiHandler;
	}

	public void setNameTestdateiHandler(String nameTestdateiHandler) {
		this.nameTestdateiHandler = nameTestdateiHandler;
	}

	public double getNutzkosten() {
		return nutzkosten;
	}

	/**
	 * fuehrt auf jeder Route, sich in seiner liste routen befindet,
	 * route.ermittleBesteRoute() aus.
	 */
	public void simuliere() {
	}

}