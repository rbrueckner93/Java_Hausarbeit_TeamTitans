package Main;

import java.util.ArrayList;

import Exceptions.UngueltigerOrt;
import korridore.Korridor;
import orte.Hauptort;
import orte.Nebenort;
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
	 * Wird per JOptionPane abgefragt. von Lukas
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
		orte = new ArrayList<Ort>();
		eingerichteteKorridore = new ArrayList<Korridor>();

		// nur zum Testen
		Hauptort test1 = new Hauptort(10, 10, "Presdorf", "HPT", 23000);
		Nebenort test2 = new Nebenort(170, 65, "dahme", "NBN", 56000);
		orte.add(test1);
		orte.add(test2);

	}

	// Ab hier Getters und Setters

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



	// ab hier andere Methoden

	/**
	 * Ermittelt Ortsdistanz fuer andere Methoden. Rundet die Distanz.
	 * 
	 * @param orteins
	 * @param ortzwei
	 * @return
	 */
	public double ermittleOrtsdistanz(Ort orteins, Ort ortzwei) {
		double laengeQuadrat = Math.pow(orteins.koordX - ortzwei.koordX, 2)
				+ Math.pow(orteins.koordY - ortzwei.koordY, 2);
		double laenge = (Math.sqrt(laengeQuadrat));
		laenge = ((Math.round(laenge * 1000)) / 1000);
		return laenge;
	}

	/**
	 * nach Aufsuchen des Ortes mit dem hoechsten Relevanzgrad werden
	 * Sicherheitskorridore zu allen anderen Orten aufgebaut (Sternloesung).
	 * spaeter: bessere Sternloesung: Geld sparen: Alle Korridorarten, die
	 * zwischen Punkt A und Punkt B moeglich sind, erzeugen und vergleichen. Die
	 * Karte muss auf ortA und ortB ort.angebundeneKorridore.add(Korridor)
	 * hinzufügen.
	 */
	public void erzeugeNetz() throws UngueltigerOrt {
		erzeugeKorridor();
		System.out.println(eingerichteteKorridore.get(0).getBaukosten());

	}

	/**
	 * ArrayList orte wird von Dateihandler gefüllt. Pruefe auch ob gleicher
	 * hoechster Relevanzgrad vorhanden ist. Dann wird der Ort weitergegeben der
	 * am naechsten am Mittelpunkt der Karte liegt.
	 * 
	 * @return
	 */
	public Ort sucheOrtMitHoechstenRelevanzGrad() {

		Ort hoechster = orte.get(0);
		for (int i = 1; i < orte.size(); i++) {

			if (hoechster.getRelevanzGrad() < orte.get(i).getRelevanzGrad()) {
				hoechster = orte.get(i);

			}
		}
		for (int i = 0; i < orte.size(); i++) {
			if (hoechster.getRelevanzGrad() == orte.get(i).getRelevanzGrad()
					&& hoechster != orte.get(i)) {

				Ort NullOrt = new Ort(100, 50, "NullOrt", "0");

				double entfernung1 = ermittleOrtsdistanz(NullOrt, hoechster);
				double entfernung2 = ermittleOrtsdistanz(NullOrt, orte.get(i));
				if (entfernung1 > entfernung2) {
					hoechster = orte.get(i);
				}
			}
		}
		return hoechster;
	}

	/**
	 * Erzeugt Sicherheitskorridore im Stern. Schreibt erzeugte Korridore in
	 * ArrayList eingerichteteKorridore. Schreibt erzeugte Korridore in
	 * ArrayList angebundeneKorridore der Orte.
	 * 
	 * @throws UngueltigerOrt
	 */
	public void erzeugeKorridor() throws UngueltigerOrt {

		for (int i = 0; i < orte.size(); i++) {
			
			if (sucheOrtMitHoechstenRelevanzGrad() != orte.get(i)) {

				eingerichteteKorridore
						.add(new Korridor(sucheOrtMitHoechstenRelevanzGrad(),
								orte.get(i), "SICH"));
			}
			
			sucheOrtMitHoechstenRelevanzGrad().angebundeneKorridore
					.add(eingerichteteKorridore.get(i));
			orte.get(i).angebundeneKorridore.add(eingerichteteKorridore.get(i));
		}
	}

	public double ermittleGesamteBaukosten() {
		int gesamtKosten = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {
			gesamtKosten += eingerichteteKorridore.get(i).getBaukosten();
		}
		return gesamtKosten;
	}

	/**
	 * uebergabe der Liste Auswertung der Anzahl nach Art (uebergabeparameter
	 * ist Art des zu analysierenden Korridors)
	 */
	
	public int ermittleAnzahlHLSTKorridore() {
		int anzahlHLST = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {

			if (eingerichteteKorridore.get(i).getKennung().equals("HLST")) {
				anzahlHLST += 1; }		
		} return anzahlHLST;

	}
	
	public int ermittleAnzahlSICHKorridore() {
		int anzahlSICH = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {

			if (eingerichteteKorridore.get(i).getKennung().equals("SICH")) {
				anzahlSICH += 1; }		
		} return anzahlSICH;

	}
	public int ermittleAnzahlENFCKorridore() {
		int anzahlENFC = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {

			if (eingerichteteKorridore.get(i).getKennung().equals("ENFC")) {
				anzahlENFC += 1; }		
		} return anzahlENFC;

	}
	
	public int ermittleAnzahlSTNDKorridore() {
		int anzahlSTND = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {

			if (eingerichteteKorridore.get(i).getKennung().equals("STND")) {
				anzahlSTND += 1; }		
		} return anzahlSTND;

	}
	
	
}