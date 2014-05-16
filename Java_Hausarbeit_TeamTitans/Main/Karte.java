package Main;

import java.util.ArrayList;

import korridore.Korridor;
import orte.Ort;

/**
 * Die Karte ist
 * 
 * -zunaechst baut die karte ein Netz in sternform auf, indem die Relevanzgrade
 * 
 * -bekommt eine Methode, die das Netz erzeugt. Eine uebergabe an den
 * Datenhandler speichert das Netz als Textdatei.
 */
public class Karte {
	/* {author=HandritschkP} */

	/**
	 * Wird per JOptionPane abgefragt.
	 */
	public static double budget;

	public ArrayList<Ort> orte;

	public ArrayList<Korridor> eingerichteteKorridore;

	/**
	 * gibt die Breite der Karte an, sodass die Positionen 0 bis kartenGroesseX
	 * moeglich sind.
	 */
	public static final int KARTE_GROESSE_X = 199;

	public static final int KARTE_GROESSE_Y = 99;

	public int baukosten;

	/**
	 * speichert den Namen der KartendateiHandler, auf deren Basis das Netz
	 * erzeugt wird beziehungsweise wurde. der Name der NetzdateiHandler soll
	 * KartendateiHandler_net sein, was ein solches Vorgehen notwendig macht.
	 */
	public String nameKartendateiHandler;

	// Konstruktor der Karte. Nichts wird gesetzt. Weitere Aenderungen ueber
	// setters.
	public Karte() {
		super();
	}

	public static double getBudget() {
		return budget;
	}

	public static void setBudget(double budget) {
		Karte.budget = budget;
	}

	public String getNameKartendateiHandler() {
		return nameKartendateiHandler;
	}

	public void setNameKartendateiHandler(String nameKartendateiHandler) {
		this.nameKartendateiHandler = nameKartendateiHandler;
	}

	// Methode missverständlich. name geaendert. Vllt variable auch aendern.
	public ArrayList<Ort> getListeAllerOrte() {
		return orte;
	}

	public ArrayList<Korridor> getEingerichteteKorridore() {
		return eingerichteteKorridore;
	}

	public int getBaukosten() {
		return baukosten;
	}

	public double ermittleOrtsdistanz() {
		return 0.0;
	}

	/**
	 * nach Aufsuchen des Ortes mit dem hoechsten Relevanzgrad werden
	 * Sicherheitskorridore zu allen anderen Orten aufgebaut (Sternloesung).
	 * spaeter: bessere Sternloesung: Geld sparen: Alle Korridorarten, die
	 * zwischen Punkt A und Punkt B moeglich sind, erzeugen und vergleichen. Die
	 * Karte muss auf ortA und ortB ort.angebundeneKorridore.add(Korridor)
	 * hinzufügen.
	 */
	public void erzeugeNetz() {
	}

	public Ort sucheOrtMitHoechstenRelevanzGrad() {
		return null;
	}

	public void erzeugeKorridor() {
	}

	public double ermittleGesamteBaukosten() {
		return 0.0;
	}

	/**
	 * uebergabe der Liste Auswertung der Anzahl nach Art (uebergabeparameter
	 * ist Art des zu analysierenden Korridors)
	 */
	public int ermittleAnzahlKorridore() {
		return 0;
	}

}