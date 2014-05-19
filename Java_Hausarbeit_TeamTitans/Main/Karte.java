package Main;

import java.util.ArrayList;

import Exceptions.UngueltigerOrt;
import korridore.Korridor;
import orte.Auslandsverbindung;
import orte.Hauptort;
import orte.Nebenort;
import orte.Ort;
import orte.Umschlagpunkt;

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
		// Hauptort test1 = new Hauptort(10, 10, "Presdorf", 23000);
		// Nebenort test2 = new Nebenort(170, 65, "dahme", 56000);
		// Auslandsverbindung test3 = new Auslandsverbindung(23, 34, "Luckau",
		// 3544, 455);
		// Umschlagpunkt test4 = new Umschlagpunkt(67, 123, "Hambur", 75653);
		// Hauptort test5 = new Hauptort(60, 80, "Bremen", 2340);
		// Nebenort test6 = new Nebenort(90, 95, "muenchen", 5700);
		// orte.add(test1);
		// orte.add(test2);
		// orte.add(test3);
		// orte.add(test4);
		// orte.add(test5);
		// orte.add(test6);
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

	public void erzeugeNetz() {
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

				Ort NullOrt = new Ort(100, 50, "NullOrt");

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

	public void erzeugeKorridor() {
		ArrayList<Korridor> moeglicheKorridore = new ArrayList<Korridor>();
		for (int i = 0; i < orte.size(); i++) {

			for (int k = 0; k < moeglicheKorridore.size(); k++) {
				moeglicheKorridore.remove(moeglicheKorridore.size() - 1 - k);
			}

			if (sucheOrtMitHoechstenRelevanzGrad() != orte.get(i)) {

				try {
					moeglicheKorridore.add(new Korridor(sucheOrtMitHoechstenRelevanzGrad(),
							orte.get(i), korridore.Korridor.KENNUNG_SICH));
				} catch (UngueltigerOrt e) {
				}
				try {
					moeglicheKorridore.add(new Korridor(sucheOrtMitHoechstenRelevanzGrad(),
							orte.get(i), korridore.Korridor.KENNUNG_HLST));
				} catch (UngueltigerOrt e) {
				}
				try {
					moeglicheKorridore.add(new Korridor(sucheOrtMitHoechstenRelevanzGrad(),
							orte.get(i), korridore.Korridor.KENNUNG_ENFC));
				} catch (UngueltigerOrt e) {
				}
				try {
					moeglicheKorridore.add(new Korridor(sucheOrtMitHoechstenRelevanzGrad(),
							orte.get(i), korridore.Korridor.KENNUNG_STND));
				} catch (UngueltigerOrt e) {
				}
			} else {
				continue;
			}
			Korridor beste = moeglicheKorridore.get(0);

			for (int j = 1; j < moeglicheKorridore.size(); j++) {

				if (beste.getBaukosten() > moeglicheKorridore.get(j).getBaukosten()) {
					beste = moeglicheKorridore.get(j);
				}
			}
			eingerichteteKorridore.add(beste);
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
				anzahlHLST += 1;
			}
		}
		return anzahlHLST;
	}

	public int ermittleAnzahlSICHKorridore() {
		int anzahlSICH = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {

			if (eingerichteteKorridore.get(i).getKennung().equals("SICH")) {
				anzahlSICH += 1;
			}
		}
		return anzahlSICH;
	}

	public int ermittleAnzahlENFCKorridore() {
		int anzahlENFC = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {

			if (eingerichteteKorridore.get(i).getKennung().equals("ENFC")) {
				anzahlENFC += 1;
			}
		}
		return anzahlENFC;
	}

	public int ermittleAnzahlSTNDKorridore() {
		int anzahlSTND = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {

			if (eingerichteteKorridore.get(i).getKennung().equals("STND")) {
				anzahlSTND += 1;
			}
		}
		return anzahlSTND;
	}
}