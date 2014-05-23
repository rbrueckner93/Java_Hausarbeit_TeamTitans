package main;

import java.util.ArrayList;

import exceptions.UngueltigerOrt;
import korridore.Korridor;
import orte.Auslandsverbindung;
import orte.Hauptort;
import orte.Nebenort;
//import orte.Auslandsverbindung;
//import orte.Hauptort;
//import orte.Nebenort;
import orte.Ort;
//import orte.Umschlagpunkt;
//import sun.awt.UngrabEvent;
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
	public String nameKartendatei;

	// Konstruktor der Karte. Nichts wird gesetzt. Weitere Aenderungen ueber
	// setters.

	public Karte() {
		super();
		orte = new ArrayList<Ort>();
		eingerichteteKorridore = new ArrayList<Korridor>();
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

	public ArrayList<Ort> getListeAllerOrte() {
		return orte;
	}

	public ArrayList<Korridor> getEingerichteteKorridore() {
		return eingerichteteKorridore;
	}

	/**
	 * Hauptmethode der Klasse Karte. Hier werden alle notwendigen Schritte zur
	 * Netzerstellung ausgeführt.
	 */
	public void erstelleNetz() {
		verbindeAuslandsorte();
		//erstelleSternENFC(entferneOrtTyp(Ort.KENNUNG_AUSLANDSVERBINDUNG));
		//erstelleRingStruktur(entferneOrtTyp(Ort.KENNUNG_AUSLANDSVERBINDUNG));
		erstelleSternSTND(entferneOrtTyp(Ort.KENNUNG_AUSLANDSVERBINDUNG));
	}

	public void erstelleRingStruktur(ArrayList<Ort> ohneASL) {
		try{
		Integer[] mittelpunkt = berechneNetzMittelpunkt(ohneASL);
		double distanz = Double.MAX_VALUE;
		for (Ort ort : ohneASL) {
			double distanzOrt = Math.sqrt(Math.pow(
					(mittelpunkt[0] - ort.koordX), 2)
					+ Math.pow((mittelpunkt[1] - ort.koordY), 2));
			if (distanzOrt < distanz) {
				distanz = distanzOrt;
			}
		}
		//Erstellung des Ringes
		ArrayList<Ort> ringOrte = new ArrayList<Ort>();
		for (Ort ort : ohneASL) {
			double distanzOrt = Math.sqrt(Math.pow(
					(mittelpunkt[0] - ort.koordX), 2)
					+ Math.pow((mittelpunkt[1] - ort.koordY), 2));
			if (distanzOrt < (4.0 * distanz)) {
				ringOrte.add(ort);
			}
		}
		Ort ringstart = ringOrte.get(0);
		for (int startOrt = 0; startOrt < ringOrte.size(); startOrt++) {
			if (startOrt != (ringOrte.size() - 1)) {
				eingerichteteKorridore.add(new Korridor(ringOrte.get(startOrt),
						ringOrte.get((startOrt + 1)), Korridor.KENNUNG_ENFC));
			} else {eingerichteteKorridore.add(new Korridor(ringOrte.get(startOrt),ringOrte.get(0), Korridor.KENNUNG_ENFC));}
		}
		// Hier erstellung der Liste verbleibenderOrte.
		ArrayList<Ort> verbleibendeOrte = ohneASL;
		for (Ort ortA : ringOrte) {
			verbleibendeOrte.remove(ortA);
		}
		for (Ort ortA : verbleibendeOrte){
		eingerichteteKorridore.add(new Korridor(ortA, findeDichtestenOrtzuDiesem(ortA, ringOrte), Korridor.KENNUNG_ENFC));
		}
		} catch (UngueltigerOrt e){}
	}
	
	public Ort findeDichtestenOrtzuDiesem(Ort bezugsOrt, ArrayList<Ort> listeOrte){
		Ort dichtesterOrt = null;
		double distanz = Double.MAX_VALUE;
		for (Ort ort : listeOrte){
			if (bezugsOrt == ort){
				continue;
			}
			double distanzOrt = Math.sqrt(Math.pow((bezugsOrt.koordX - ort.koordX), 2)+ Math.pow((bezugsOrt.koordY - ort.koordY), 2));
			if (distanzOrt < distanz){
				distanz = distanzOrt;
				dichtesterOrt = ort;
			}
		}
		return dichtesterOrt;
	}

	public void erstelleSternENFC(ArrayList<Ort> ohneASL) {
		try {
			Integer[] mittelpunkt = berechneNetzMittelpunkt(ohneASL);
			Ort sternMittelpunkt = null;
			double distanz = Double.MAX_VALUE;
			for (Ort ort : ohneASL) {
				double distanzOrt = Math.sqrt(Math.pow(
						(mittelpunkt[0] - ort.koordX), 2)
						+ Math.pow((mittelpunkt[1] - ort.koordY), 2));
				if (distanzOrt < distanz) {
					distanz = distanzOrt;
					sternMittelpunkt = ort;
				}
			}
			for (Ort ortA : ohneASL) {
				if (ortA != sternMittelpunkt) {
					eingerichteteKorridore.add(new Korridor(sternMittelpunkt,
							ortA, Korridor.KENNUNG_ENFC));
				}
			}
		} catch (UngueltigerOrt e) {
		}
	}
	
	public void erstelleSternSTND(ArrayList<Ort> ohneASL) {
		try {
			Integer[] mittelpunkt = berechneNetzMittelpunkt(ohneASL);
			Ort sternMittelpunkt = null;
			double distanz = Double.MAX_VALUE;
			for (Ort ort : ohneASL) {
				double distanzOrt = Math.sqrt(Math.pow(
						(mittelpunkt[0] - ort.koordX), 2)
						+ Math.pow((mittelpunkt[1] - ort.koordY), 2));
				if (distanzOrt < distanz) {
					distanz = distanzOrt;
					sternMittelpunkt = ort;
				}
			}
			for (Ort ortA : ohneASL) {
				if (ortA != sternMittelpunkt) {
					eingerichteteKorridore.add(new Korridor(sternMittelpunkt,
							ortA, Korridor.KENNUNG_STND));
				}
			}
		} catch (UngueltigerOrt e) {
		}
	}

	public ArrayList<Ort> entferneOrtTyp(String kennung) {
		ArrayList<Ort> ohneOrte = new ArrayList<Ort>();
		for (Ort ort : orte) {
			if (!ort.kennung.equals(kennung)) {
				ohneOrte.add(ort);
			}
		}
		return ohneOrte;
	}

	/**
	 * Diese Methode verbindet alle ASL Orte mit ihrem nächstgelegen Nicht ASL
	 * Ort.
	 */

	public void verbindeAuslandsorte() {
		try {
			ArrayList<Ort> aslOrte = new ArrayList<Ort>();
			Ort ortVB = null;
			for (Ort ortA : orte) {
				if (ortA.kennung.equals(Ort.KENNUNG_AUSLANDSVERBINDUNG)) {
					aslOrte.add(ortA);
				}
			}
			for (Ort ortASL : aslOrte) {
				double distanz = Double.MAX_VALUE;
				for (Ort ortNOTasl : orte) {
					if (ortNOTasl.kennung
							.equals(Ort.KENNUNG_AUSLANDSVERBINDUNG)) {
						continue;
					}
					double distanzVergleich = ermittleOrtsdistanz(ortASL,
							ortNOTasl);
					if (distanzVergleich < distanz) {
						distanz = distanzVergleich;
						ortVB = ortNOTasl;
					}
				}
				eingerichteteKorridore.add(new Korridor(ortASL, ortVB,
						Korridor.KENNUNG_SICH));
			}
		} catch (UngueltigerOrt e) {
		}
	}

	public boolean istEinrichtbarerKorridor(Ort ortA, Ort ortB,
			String korridorTyp) {
		if (korridorTyp.equals(Korridor.KENNUNG_ENFC)) {
			if ((ortA.getKennung().equals(Ort.KENNUNG_HAUPTORT)
					|| ortA.getKennung().equals(Ort.KENNUNG_NEBENORT) || ortA
					.getKennung().equals(Ort.KENNUNG_UMSCHLAGPUNKT))
					&& (ortB.getKennung().equals(Ort.KENNUNG_HAUPTORT)
							|| ortB.getKennung().equals(Ort.KENNUNG_NEBENORT) || ortB
							.getKennung().equals(Ort.KENNUNG_UMSCHLAGPUNKT))) {
				return true;
			} else {
				return false;
			}
		}
		if (korridorTyp.equals(Korridor.KENNUNG_HLST)) {
			if ((ortA.getKennung().equals(Ort.KENNUNG_HAUPTORT) || ortA
					.getKennung().equals(Ort.KENNUNG_UMSCHLAGPUNKT))
					&& (ortB.getKennung().equals(Ort.KENNUNG_HAUPTORT) || ortB
							.getKennung().equals(Ort.KENNUNG_UMSCHLAGPUNKT))) {
				return true;
			} else {
				return false;
			}
		}
		if (korridorTyp.equals(Korridor.KENNUNG_SICH)) {
			if ((ortA.getKennung().equals(Ort.KENNUNG_HAUPTORT)
					|| ortA.getKennung().equals(Ort.KENNUNG_NEBENORT)
					|| ortA.getKennung().equals(Ort.KENNUNG_UMSCHLAGPUNKT) || ortA
					.getKennung().equals(Ort.KENNUNG_AUSLANDSVERBINDUNG))
					&& (ortB.getKennung().equals(Ort.KENNUNG_HAUPTORT)
							|| ortB.getKennung().equals(Ort.KENNUNG_NEBENORT)
							|| ortB.getKennung().equals(
									Ort.KENNUNG_UMSCHLAGPUNKT) || ortB
							.getKennung()
							.equals(Ort.KENNUNG_AUSLANDSVERBINDUNG))) {
				return true;
			} else {
				return false;
			}
		}
		if (korridorTyp.equals(Korridor.KENNUNG_STND)) {
			if ((ortA.getKennung().equals(Ort.KENNUNG_HAUPTORT)
					|| ortA.getKennung().equals(Ort.KENNUNG_NEBENORT) || ortA
					.getKennung().equals(Ort.KENNUNG_UMSCHLAGPUNKT))
					&& (ortB.getKennung().equals(Ort.KENNUNG_HAUPTORT)
							|| ortB.getKennung().equals(Ort.KENNUNG_NEBENORT) || ortB
							.getKennung().equals(Ort.KENNUNG_UMSCHLAGPUNKT))) {
				return true;
			} else {
				return false;
			}
		}
		return false;
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
		return laenge;
	}

	/**
	 * ArrayList orte wird von Dateihandler gefüllt. Pruefe auch ob gleicher
	 * hoechster Relevanzgrad vorhanden ist. Dann wird der Ort weitergegeben der
	 * am naechsten am Mittelpunkt der Karte liegt.
	 * 
	 * @return
	 */

	/**
	 * Bekommt eine Liste mit orten und gibt den mit hoechsten Relevanzgrad
	 * zurück. Ort mit hoechsten Relevanzgrad aber nei Auslandsverbindung.
	 * 
	 * @param liste
	 * @return
	 */
	public Ort sucheOrtMitHoechstenRelevanzGrad(ArrayList<Ort> liste) {

		Ort hoechster = liste.get(0);
		for (int i = 1; i < liste.size(); i++) {

			if (hoechster.getRelevanzGrad() < liste.get(i).getRelevanzGrad()
					&& !(hoechster.kennung.equals("ASL"))) {
				hoechster = liste.get(i);
			}
		}
		for (int i = 0; i < liste.size(); i++) {
			if (hoechster.getRelevanzGrad() == liste.get(i).getRelevanzGrad()
					&& hoechster != liste.get(i)) {

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
	 * Geht Liste der der eingerichteten Korridore durch und summiert die
	 * Baukosten und gibt diese aus.
	 * 
	 * @return
	 */

	public double ermittleGesamteBaukosten() {
		int gesamtKosten = 0;
		for (Korridor korridor : eingerichteteKorridore) {
			gesamtKosten += korridor.getBaukosten();
		}
		return gesamtKosten;
	}

	/**
	 * Berechnet den Mittelpunt (Punkt, wo die Summe der Abstände am kleinsten
	 * ist) des Netzes. Gibt ArrayList zurück. Element0 = X-Koordinate, Element1
	 * = Y-Koordinate des Mittelpunktes.
	 * 
	 * @return
	 */

	public Integer[] berechneNetzMittelpunkt(ArrayList<Ort> vorhandeneOrte) {
		Integer[] koord = new Integer[2];
		double distanz = Double.MAX_VALUE;
		for (int xkoord = 0; xkoord < 200; xkoord++) {
			for (int ykoord = 0; ykoord < 100; ykoord++) {
				double gesamtdistanz = 0;
				for (Ort ort : vorhandeneOrte) {
					double distanzSchleife = Math.sqrt(Math.pow(
							(xkoord - ort.koordX), 2)
							+ Math.pow((ykoord - ort.koordY), 2));
					gesamtdistanz += distanzSchleife;
				}
				if (gesamtdistanz < distanz) {
					distanz = gesamtdistanz;
					koord[0] = xkoord;
					koord[1] = ykoord;
				}
			}
		}
		return koord;
	}

	/**
	 * uebergabe der Liste Auswertung der Anzahl nach Art (uebergabeparameter
	 * ist Art des zu analysierenden Korridors)
	 */

	public int ermittleAnzahlHLSTKorridore() {
		int anzahlHLST = 0;
		for (Korridor korridor : eingerichteteKorridore) {
			if (korridor.getKennung().equals("HLST")) {
				anzahlHLST += 1;
			}
		}
		return anzahlHLST;
	}

	public int ermittleAnzahlSICHKorridore() {
		int anzahlSICH = 0;
		for (Korridor korridor : eingerichteteKorridore) {

			if (korridor.getKennung().equals("SICH")) {
				anzahlSICH += 1;
			}
		}
		return anzahlSICH;
	}

	public int ermittleAnzahlENFCKorridore() {
		int anzahlENFC = 0;
		for (Korridor korridor : eingerichteteKorridore) {

			if (korridor.getKennung().equals("ENFC")) {
				anzahlENFC += 1;
			}
		}
		return anzahlENFC;
	}

	public int ermittleAnzahlSTNDKorridore() {
		int anzahlSTND = 0;
		for (Korridor korridor : eingerichteteKorridore) {

			if (korridor.getKennung().equals("STND")) {
				anzahlSTND += 1;
			}
		}
		return anzahlSTND;
	}
}