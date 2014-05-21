package main;

import java.util.ArrayList;

import exceptions.UngueltigerOrt;
import korridore.Korridor;
//import orte.Auslandsverbindung;
//import orte.Hauptort;
//import orte.Nebenort;
import orte.Ort;
//import orte.Umschlagpunkt;
//import sun.awt.UngrabEvent;

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
	public String nameKartendatei;

	// Konstruktor der Karte. Nichts wird gesetzt. Weitere Aenderungen ueber
	// setters.

	public Karte() {
		super();
		orte = new ArrayList<Ort>();
		eingerichteteKorridore = new ArrayList<Korridor>();

		// nur zum Testen
//		Hauptort test1 = new Hauptort(1, 1, "Presdorf", 23000);
//		Nebenort test2 = new Nebenort(198, 98, "dahme", 56000);
//		Auslandsverbindung test3 = new Auslandsverbindung(50, 70, "Luckau",3544, 455);
//		Umschlagpunkt test4 = new Umschlagpunkt(67, 123, "Hamburg", 75653);
//		Hauptort test5 = new Hauptort(50, 80, "Bremen", 2340);
//		Nebenort test6 = new Nebenort(90, 95, "muenchen", 5700);
//		orte.add(test1);
//		orte.add(test2);
//		orte.add(test3);
//		orte.add(test4);
//		orte.add(test5);
//		orte.add(test6);
	}

	public static double getBudget() {
		return budget;
	}

	public static void setBudget(double budget) {
		Karte.budget = budget;
	}

	public String getNameKartendateiHandler() {
		return nameKartendatei;
	}

	public void setNameKartendateiHandler(String nameKartendateiHandler) {
		this.nameKartendatei = nameKartendateiHandler;

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
	//	laenge = ((Math.round(laenge * 1000)) / 1000);
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

	// Ab hier verschiedene Möglichkeiten der Netzerstellung, zum einen immer
	// weiter optimiert, zum anderen zum testen

	// Alle Orte mit jeden anderen verbunden
	// public void erzeugeKorridor() {
	// ArrayList<Korridor> moeglicheKorridore = new ArrayList<Korridor>();
	//
	// for (int x = 0; x < orte.size(); x++) {
	//
	// for (int y = x + 1; y < orte.size(); y++) {
	//
	// for (int k = 0; k < moeglicheKorridore.size(); k++) {
	// moeglicheKorridore.remove(moeglicheKorridore.size() - 1 - k);
	// }
	//
	// try {
	// moeglicheKorridore.add(new Korridor(orte.get(x), orte
	// .get(y), korridore.Korridor.KENNUNG_SICH));
	// } catch (UngueltigerOrt e) {
	// }
	// try {
	// moeglicheKorridore.add(new Korridor(orte.get(x), orte
	// .get(y), korridore.Korridor.KENNUNG_HLST));
	// } catch (UngueltigerOrt e) {
	// }
	// try {
	// moeglicheKorridore.add(new Korridor(orte.get(x), orte
	// .get(y), korridore.Korridor.KENNUNG_ENFC));
	// } catch (UngueltigerOrt e) {
	// }
	// try {
	// moeglicheKorridore.add(new Korridor(orte.get(x), orte
	// .get(y), korridore.Korridor.KENNUNG_STND));
	// } catch (UngueltigerOrt e) {
	// }
	//
	//
	//
	// Korridor beste = moeglicheKorridore.get(0);
	//
	// for (int j = 1; j < moeglicheKorridore.size(); j++) {
	//
	// if (beste.getBaukosten() > moeglicheKorridore.get(j)
	// .getBaukosten()) {
	// beste = moeglicheKorridore.get(j);
	// }
	// }
	// eingerichteteKorridore.add(beste);
	// }
	// }
	// }

	// //Sternvariante
	public void erzeugeKorridor() {
		ArrayList<Korridor> moeglicheKorridore = new ArrayList<Korridor>();
		for (int i = 0; i < orte.size(); i++) {

			for (int k = 0; k < moeglicheKorridore.size(); k++) {
				moeglicheKorridore.remove(moeglicheKorridore.size() - 1 - k);
			}

			if (sucheOrtMitHoechstenRelevanzGrad() != orte.get(i)) {

				try {
					moeglicheKorridore.add(new Korridor(
							sucheOrtMitHoechstenRelevanzGrad(), orte.get(i),
							korridore.Korridor.KENNUNG_SICH));
				} catch (UngueltigerOrt e) {
				}
				try {
					moeglicheKorridore.add(new Korridor(
							sucheOrtMitHoechstenRelevanzGrad(), orte.get(i),
							korridore.Korridor.KENNUNG_HLST));
				} catch (UngueltigerOrt e) {
				}
				try {
					moeglicheKorridore.add(new Korridor(
							sucheOrtMitHoechstenRelevanzGrad(), orte.get(i),
							korridore.Korridor.KENNUNG_ENFC));
				} catch (UngueltigerOrt e) {
				}
				try {
					moeglicheKorridore.add(new Korridor(
							sucheOrtMitHoechstenRelevanzGrad(), orte.get(i),
							korridore.Korridor.KENNUNG_STND));
				} catch (UngueltigerOrt e) {
				}
			} else {
				continue;
			}
			Korridor beste = moeglicheKorridore.get(0);

			for (int j = 1; j < moeglicheKorridore.size(); j++) {

				if (beste.getBaukosten() > moeglicheKorridore.get(j)
						.getBaukosten()) {
					beste = moeglicheKorridore.get(j);
				}
			}
			eingerichteteKorridore.add(beste);
		}
	}

	// // Anfang 2 Sterne Variante (zusätzlich muss Methode erzeugeNetz auch
	// auskommentiert werden
	//
	// public Ort sucheOrtMitHoechstenRelevanzGrad(ArrayList<Ort> liste) {
	//
	// Ort hoechster = liste.get(0);
	// for (int i = 1; i < liste.size(); i++) {
	//
	// if (hoechster.getRelevanzGrad() < liste.get(i).getRelevanzGrad()) {
	// hoechster = liste.get(i);
	// }
	// }
	// for (int i = 0; i < liste.size(); i++) {
	// if (hoechster.getRelevanzGrad() == liste.get(i).getRelevanzGrad()
	// && hoechster != liste.get(i)) {
	//
	// Ort NullOrt = new Ort(100, 50, "NullOrt");
	//
	// double entfernung1 = ermittleOrtsdistanz(NullOrt, hoechster);
	// double entfernung2 = ermittleOrtsdistanz(NullOrt, orte.get(i));
	// if (entfernung1 > entfernung2) {
	// hoechster = orte.get(i);
	// }
	// }
	// }
	// return hoechster;
	// }
	//
	// public ArrayList<Ort> linkeTeilkarte (ArrayList<Ort> liste){
	//
	// ArrayList<Ort> orteLinks = new ArrayList<>();
	// for (int x = 0 ; x < orte.size(); x++ ){
	// if(orte.get(x).koordX < 99.5){
	// orteLinks.add(orte.get(x));
	// }
	// }
	// return orteLinks;
	// }
	//
	// public ArrayList<Ort> rechteTeilkarte (ArrayList<Ort> liste){
	//
	// ArrayList<Ort> orteRechts = new ArrayList<>();
	// for (int x = 0 ; x < orte.size(); x++ ){
	// if(orte.get(x).koordX < 99.5){
	// orteRechts.add(orte.get(x));
	// }
	// }
	// return orteRechts;
	// }
	//
	// public void erzeugeKorridor( ArrayList<Ort> liste) {
	//
	// ArrayList<Korridor> moeglicheKorridore = new ArrayList<Korridor>();
	//
	// for (int i = 0; i < liste.size(); i++) {
	//
	// for (int k = 0; k < moeglicheKorridore.size(); k++) {
	// moeglicheKorridore.remove(moeglicheKorridore.size() - 1 - k);
	// }
	//
	// if (sucheOrtMitHoechstenRelevanzGrad(liste) != liste.get(i)) {
	//
	// try {
	// moeglicheKorridore.add(new
	// Korridor(sucheOrtMitHoechstenRelevanzGrad(liste),
	// liste.get(i), korridore.Korridor.KENNUNG_SICH));
	// } catch (UngueltigerOrt e) {
	// }
	// try {
	// moeglicheKorridore.add(new
	// Korridor(sucheOrtMitHoechstenRelevanzGrad(liste),
	// liste.get(i), korridore.Korridor.KENNUNG_HLST));
	// } catch (UngueltigerOrt e) {
	// }
	// try {
	// moeglicheKorridore.add(new
	// Korridor(sucheOrtMitHoechstenRelevanzGrad(liste),
	// liste.get(i), korridore.Korridor.KENNUNG_ENFC));
	// } catch (UngueltigerOrt e) {
	// }
	// try {
	// moeglicheKorridore.add(new
	// Korridor(sucheOrtMitHoechstenRelevanzGrad(liste),
	// liste.get(i), korridore.Korridor.KENNUNG_STND));
	// } catch (UngueltigerOrt e) {
	// }
	// } else {
	// continue;
	// }
	// Korridor beste = moeglicheKorridore.get(0);
	//
	// for (int j = 1; j < moeglicheKorridore.size(); j++) {
	//
	// if (beste.getBaukosten() > moeglicheKorridore.get(j).getBaukosten()) {
	// beste = moeglicheKorridore.get(j);
	// }
	// }
	// eingerichteteKorridore.add(beste);
	// }
	// }
	//
	// public void erstelleNetz(){
	//
	// erzeugeKorridor(rechteTeilkarte(orte));
	// erzeugeKorridor(linkeTeilkarte(orte));
	// try{
	// eingerichteteKorridore.add(new
	// Korridor(sucheOrtMitHoechstenRelevanzGrad(linkeTeilkarte(orte)),
	// sucheOrtMitHoechstenRelevanzGrad(rechteTeilkarte(orte)),"HLST"));
	// } catch (UngueltigerOrt e){
	// try{
	// eingerichteteKorridore.add(new
	// Korridor(sucheOrtMitHoechstenRelevanzGrad(linkeTeilkarte(orte)),
	// sucheOrtMitHoechstenRelevanzGrad(rechteTeilkarte(orte)),"SICH"));
	// } catch (UngueltigerOrt f){
	// System.out.println("Kann gar nicht passieren!");
	// }
	//
	// }
	//
	// };
	//
	// //Ende 2 Sterne Variante

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

	/**
	 * Verbindet alle Auslandsverbindungen mit dem nächstliegenden,
	 * Nichtauslandsverbindungsort mit einen Sicherheitskorridor Fügt die
	 * erstllten Korridore der ArrayList eingerichtete Korridore zu.
	 */
	public void auslandsverbindungAnsNetzAnbienden() {
		ArrayList<Ort> listeAuslandsverbindungen = new ArrayList<Ort>();
		Ort naechsterOrt;
		do {
			naechsterOrt = orte.get(0);
		} while (!(naechsterOrt.kennung.equals("ALS")));

		for (int i = 0; i < orte.size(); i++) {
			if (orte.get(i).kennung.equals("ASL")) {
				listeAuslandsverbindungen.add(orte.get(i));
			}
			for (int j = 0; j < listeAuslandsverbindungen.size(); j++) {

				for (int k = 0; k < orte.size(); k++) {

					if (!(orte.get(k).kennung.equals("SICH"))) {
						if (ermittleOrtsdistanz(
								listeAuslandsverbindungen.get(j), orte.get(k)) < ermittleOrtsdistanz(
								listeAuslandsverbindungen.get(j), naechsterOrt)) {
							naechsterOrt = orte.get(k);
						}
						try {
							eingerichteteKorridore.add(new Korridor(
									listeAuslandsverbindungen.get(j),
									naechsterOrt, "SICH"));
						} catch (UngueltigerOrt e) {
							continue;
						}
					}
				}
			}
		}
	}

	/**
	 * Berechnet den Mittelpunt (Punkt, wo die Summe der Abstände am kleinsten
	 * ist) des Netzes. Gibt ArrayList zurück. Element0 = X-Koordinate, Element1
	 * = Y-Koordinate des Mittelpunktes.
	 * 
	 * @return
	 */

	public ArrayList<Integer> berechneNetzMittelpunkt() {

		ArrayList<Integer> punkt = new ArrayList<Integer>();
		punkt.add(0);
		punkt.add(0);

		double kleinsteSumme = 222000 * orte.size(); // 222 ist der maximale
													// Abstand der auf der Karte
													// möglich wäre

		for (int i = 0; i < 200; i++) {

			for (int j = 0; j < 100; j++) {

				Ort fiktivOrt = new Ort(i, j, "fiktivOrt");

					if (summeAbstandermitteln(fiktivOrt) < kleinsteSumme) {
						kleinsteSumme = summeAbstandermitteln(fiktivOrt);
						punkt.set(0, i);
						punkt.set(1, j);
					}
				}
			}

		return punkt;
	}

	public double summeAbstandermitteln(Ort orta) {
		double summeAbstand = 0; 
		for(int i = 0; i < orte.size(); i++){
		summeAbstand = summeAbstand + (1000*ermittleOrtsdistanz(orta, orte.get(i)));
		}
		return summeAbstand; 
		
	}
	
	
}