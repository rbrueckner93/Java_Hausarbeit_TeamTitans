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

	// Methode missverst�ndlich. name geaendert. Vllt variable auch aendern.
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
		return laenge;
	}

	/**
	 * ArrayList orte wird von Dateihandler gef�llt. Pruefe auch ob gleicher
	 * hoechster Relevanzgrad vorhanden ist. Dann wird der Ort weitergegeben der
	 * am naechsten am Mittelpunkt der Karte liegt.
	 * 
	 * @return
	 */

	/**
	 * Bekommt eine Liste mit orten und gibt den mit hoechsten Relevanzgrad
	 * zur�ck. Ort mit hoechsten Relevanzgrad aber nei Auslandsverbindung.
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
 * Bekommt Liste mit Orten und gibt Liste mit allen Orten, die in der linken Kartenh�lfte liegen zur�ck.
 * @param liste
 * @return
 */
	public ArrayList<Ort> linkeTeilkarte(ArrayList<Ort> liste) {

		ArrayList<Ort> orteLinks = new ArrayList<>();
		for (int x = 0; x < orte.size(); x++) {
			if (orte.get(x).koordX < 99.5
					&& !(orte.get(x).kennung.equals("ASL"))) {
				orteLinks.add(orte.get(x));
			}
		}
		return orteLinks;
	}

	/**
	 * Bekommt Liste mit allen Orten und gibt Liste mit allen Orten, die in der rechten Kartenh�lfte liegen zur�ck. 
	 * @param liste
	 * @return
	 */
	public ArrayList<Ort> rechteTeilkarte(ArrayList<Ort> liste) {

		ArrayList<Ort> orteRechts = new ArrayList<>();
		for (int x = 0; x < orte.size(); x++) {
			if (orte.get(x).koordX > 99.5
					&& !(orte.get(x).kennung.equals("ASL"))) {
				orteRechts.add(orte.get(x));
			}
		}
		return orteRechts;
	}

	/**
	 * Bekommt Liste mit Orten und erstellt Sternvariante.
	 * Erstellt immer alle m�glichen Korridorarten und gibt nur den aus, der die niedrigsten Baukosten hat.
	 * @param liste
	 */
	public void erzeugeKorridor(ArrayList<Ort> liste) {

		ArrayList<Korridor> moeglicheKorridore = new ArrayList<Korridor>();

		for (int i = 0; i < liste.size(); i++) {

			for (int k = 0; k < moeglicheKorridore.size(); k++) {
				moeglicheKorridore.remove(moeglicheKorridore.size() - 1 - k);
			}

			if (sucheOrtMitHoechstenRelevanzGrad(liste) != liste.get(i)) {

				try {
					moeglicheKorridore.add(new Korridor(
							sucheOrtMitHoechstenRelevanzGrad(liste), liste
									.get(i), korridore.Korridor.KENNUNG_SICH));
				} catch (UngueltigerOrt e) {
				}
				try {
					moeglicheKorridore.add(new Korridor(
							sucheOrtMitHoechstenRelevanzGrad(liste), liste
									.get(i), korridore.Korridor.KENNUNG_HLST));
				} catch (UngueltigerOrt e) {
				}
				try {
					moeglicheKorridore.add(new Korridor(
							sucheOrtMitHoechstenRelevanzGrad(liste), liste
									.get(i), korridore.Korridor.KENNUNG_ENFC));
				} catch (UngueltigerOrt e) {
				}
				try {
					moeglicheKorridore.add(new Korridor(
							sucheOrtMitHoechstenRelevanzGrad(liste), liste
									.get(i), korridore.Korridor.KENNUNG_STND));
				} catch (UngueltigerOrt e) {
				}
			} else {
				continue;
			}
			System.out.println(moeglicheKorridore);
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

	/**
	 * Erzeugt das Netz, indem die Sterne fuer beide Kartenhaelften gebildet werden und die Orte mit hoechsten Relevanzgrad dann verbunden werden.
	 * Auslandsverbindungen vorher an angebunden mit externen Methode.
	 */
	public void erstelleNetz() {
	//	auslandsverbindungAnsNetzAnbienden();
		erzeugeKorridor(rechteTeilkarte(orte));
		erzeugeKorridor(linkeTeilkarte(orte));
		try {
			eingerichteteKorridore.add(new Korridor(
					sucheOrtMitHoechstenRelevanzGrad(linkeTeilkarte(orte)),
					sucheOrtMitHoechstenRelevanzGrad(rechteTeilkarte(orte)),
					"HLST"));
		} catch (UngueltigerOrt e) {
			try {
				eingerichteteKorridore
						.add(new Korridor(
								sucheOrtMitHoechstenRelevanzGrad(linkeTeilkarte(orte)),
								sucheOrtMitHoechstenRelevanzGrad(rechteTeilkarte(orte)),
								"SICH"));
			} catch (UngueltigerOrt f) {
				System.out.println("Kann gar nicht passieren!");
			}

		}

	};

	/**
	 * Geht Liste der der eingerichteten Korridore durch und summiert die Baukosten und gibt diese aus.
	 * @return
	 */

	public double ermittleGesamteBaukosten() {
		int gesamtKosten = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {
			gesamtKosten += eingerichteteKorridore.get(i).getBaukosten();
		}
		return gesamtKosten;
	}



	/**
	 * Verbindet alle Auslandsverbindungen mit dem n�chstliegenden,
	 * Nichtauslandsverbindungsort mit einen Sicherheitskorridor F�gt die
	 * erstllten Korridore der ArrayList eingerichtete Korridore zu.
	 */
	
	public void auslandsverbindungAnsNetzAnbienden() {
		
		ArrayList<Ort> listeAuslandsverbindungen = new ArrayList<Ort>();
		Ort naechsterOrt = orte.get(0);

		for (int i = 1; i < orte.size() && naechsterOrt.kennung.equals("ASL"); i++) {
			naechsterOrt = orte.get(i);
		}

		for (int i = 0; i < orte.size(); i++) {
			
			if (orte.get(i).kennung.equals("ASL")) {
				listeAuslandsverbindungen.add(orte.get(i));
			}
		}
	
			
			for (int j = 0; j < listeAuslandsverbindungen.size(); j++) {

				for (int k = 0; k < orte.size(); k++) {

					if (!(orte.get(k).kennung.equals("SICH"))) {
						if (ermittleOrtsdistanz(
								listeAuslandsverbindungen.get(j), orte.get(k)) < ermittleOrtsdistanz(
								listeAuslandsverbindungen.get(j), naechsterOrt)) {
							naechsterOrt = orte.get(k);
						}

					}
					
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

	/**
	 * Berechnet den Mittelpunt (Punkt, wo die Summe der Abst�nde am kleinsten
	 * ist) des Netzes. Gibt ArrayList zur�ck. Element0 = X-Koordinate, Element1
	 * = Y-Koordinate des Mittelpunktes.
	 * 
	 * @return
	 */

	public ArrayList<Integer> berechneNetzMittelpunkt() {

		ArrayList<Integer> punkt = new ArrayList<Integer>();
		punkt.add(0);
		punkt.add(0);

		double kleinsteSumme = 222000 * orte.size(); // 222 ist der maximale
														// Abstand der auf der
														// Karte
														// m�glich w�re

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
		for (int i = 0; i < orte.size(); i++) {
			summeAbstand = summeAbstand
					+ (1000 * ermittleOrtsdistanz(orta, orte.get(i)));
		}
		return summeAbstand;
	}
	
	/**
	 * uebergabe der Liste Auswertung der Anzahl nach Art (uebergabeparameter
	 * ist Art des zu analysierenden Korridors)
	 */

	public int ermittleAnzahlHLSTKorridore() {
		return ermittleKorridoranzahlNachArt(Korridor.KENNUNG_HLST);
	}

	public int ermittleAnzahlSICHKorridore() {
		return ermittleKorridoranzahlNachArt(Korridor.KENNUNG_SICH);
	}

	public int ermittleAnzahlENFCKorridore() {
		return ermittleKorridoranzahlNachArt(Korridor.KENNUNG_ENFC);
	}

	public int ermittleAnzahlSTNDKorridore() {
		return ermittleKorridoranzahlNachArt(Korridor.KENNUNG_STND);
	}
	
	public int ermittleKorridoranzahlNachArt(String kennung){
		int anzahl = 0;
		for (int i = 0; i < eingerichteteKorridore.size(); i++) {

			if (eingerichteteKorridore.get(i).getKennung().equals(kennung)) {
				anzahl += 1;
			}
		}
		return anzahl;
		
	}
	
	public int anzahlOrteInFelderListe(ArrayList<Feld> felderLi){
		int anzahl = 0;
		for(Feld felda:felderLi)
		{
			anzahl += felda.bestimmeOrteAusserASLImFeld().size();
		}
		return anzahl;
	}
	
	public ArrayList<Feld> ermittleKonzentrationsfelder(int minOrte, int erstellerKantenlaenge) {
		// in felderListe werden die Felder geschrieben, die den Kriterien
		// entsprechen.
		ArrayList<Feld> felderListe = new ArrayList<Feld>();
		for (int x = 0; (x + erstellerKantenlaenge) <= KARTE_GROESSE_X; x++) {
			boolean etwasInX_Gefunden = false;
			for (int y = 0; (y + erstellerKantenlaenge) <= KARTE_GROESSE_Y; y++) {
				Feld neuesFeld = new Feld(this, x, y,
						erstellerKantenlaenge);
				if(neuesFeld.bestimmeOrteAusserASLImFeld().size()>=minOrte) felderListe.add(neuesFeld);
			//	System.out.println("Ich habe ("+neuesFeld.startX+"|"+neuesFeld.startY+"), ("+neuesFeld.endX+"|"+neuesFeld.endY+") hinzugefuegt. Dort befinden sich "+neuesFeld.bestimmeOrteImFeld().size()+" Orte.");
				if (neuesFeld.bestimmeOrteAusserASLImFeld().size() == 0)
					y += (erstellerKantenlaenge - 1);
				etwasInX_Gefunden = true;
			}
			if (etwasInX_Gefunden = false)
				x += erstellerKantenlaenge;
		}

		// durchsuche die ArrayList nach dem Maximum, mit dem begonnen wird
		ArrayList<Feld> felderOhneUeberschneidung = new ArrayList<Feld>();

		//loesche alle Felder aus der Liste, die sich mit dem aktuell am dichtesten besiedelten Ort ueberschneiden
		while (felderListe.size() > 0) {
			int max = 0;
			// jedes Feld nach der Anzahl der Orte fragen, um Feld mit
			// dichtester besiedlung zu ermitteln
			for (int index = 0; index < felderListe.size(); index++) {
				if (felderListe.get(max).bestimmeOrteAusserASLImFeld().size() < felderListe.get(index).bestimmeOrteAusserASLImFeld().size())
					max = index;
			}
			// loesche jetzt alle felder in der Liste, deren flaeche sich mit
			// der flaeche von der flaeche mit den meisten orten schneidet.
			for (int i = 0; i < felderListe.size(); i++) {
				if ((i != max) && (felderListe.get(max).ueberschneidungMitFeld(felderListe.get(i)))) {
					System.out.println("Es wird gelöscht:"+felderListe.get(i));
					felderListe.remove(i);
					i--;
					if (i < max) max--;
				}
			}
			felderOhneUeberschneidung.add(felderListe.get(max));
			felderListe.remove(max);
		}

		return felderOhneUeberschneidung;
	}
}