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
		erstelleSternENFC(entferneOrtTyp(Ort.KENNUNG_AUSLANDSVERBINDUNG,orte));
		//erstelleRingStruktur(entferneOrtTyp(Ort.KENNUNG_AUSLANDSVERBINDUNG,
		//		orte));
	}

	/**
	 * Methode zum erstellen eines Ringes im Netz. Weitere Optimierung folgt.
	 * 
	 * @param ohneASL
	 */
	public void erstelleRingStruktur(ArrayList<Ort> ohneASL) {
		try {
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
			// Erstellung des Ringes
			ArrayList<Ort> ringOrte = new ArrayList<Ort>();
			for (Ort ort : ohneASL) {
				double distanzOrt = Math.sqrt(Math.pow(
						(mittelpunkt[0] - ort.koordX), 2)
						+ Math.pow((mittelpunkt[1] - ort.koordY), 2));
				if (distanzOrt < (4.0 * distanz)) {
					ringOrte.add(ort);
				}
			}
			// Sortieren der Liste RingOrte aufsteigend nach ihrer Distanz
			Ort mittigsterOrt = getMittigstenOrt(ohneASL);
			ringOrte = sortiereListeNachAbstand(mittigsterOrt, ringOrte);
			// Sortierung abgeschlossen.
			Ort ringstart = ringOrte.get(0);
			ArrayList<Ort> verbleibendeRingOrte = new ArrayList<Ort>();
			for (Ort ort : ringOrte) {
				verbleibendeRingOrte.add(ort);
			}
			// Erstellung des Rings.
			for (int index = 0; index < ringOrte.size(); index++) {
				if (index == (ringOrte.size() - 1)) {
					// eingerichteteKorridore.add(new Korridor(
					// ringOrte.get(index), ringstart,
					// Korridor.KENNUNG_ENFC));
					// verbleibendeRingOrte.remove(ringOrte.get(index));
					break;
				} else {
					eingerichteteKorridore.add(new Korridor(
							ringOrte.get(index), findeDichtestenOrtzuDiesem(
									ringOrte.get(index), verbleibendeRingOrte),
							Korridor.KENNUNG_ENFC));
					verbleibendeRingOrte.remove(ringOrte.get(index));
				}

			}
			// Hier erstellung der Liste verbleibenderOrte.
			ArrayList<Ort> verbleibendeOrte = ohneASL;
			for (Ort ortA : ringOrte) {
				verbleibendeOrte.remove(ortA);
			}
			for (Ort ortA : verbleibendeOrte) {
				eingerichteteKorridore.add(new Korridor(ortA,
						findeDichtestenOrtzuDiesem(ortA, ringOrte),
						Korridor.KENNUNG_ENFC));
			}
		} catch (UngueltigerOrt e) {
		}
	}

	/**
	 * Erstellt einen Stern vom mittigsten Ort des Netztes mit Einfachen
	 * Korridoren.
	 * 
	 * @param ohneASL
	 *            Liste mit Orten, aber ohne Auslandsorten.
	 * @author Nils
	 */
	public void erstelleSternENFC(ArrayList<Ort> ohneASL) {
		try {
			double kennwertMitte = Double.MAX_VALUE;
			Ort sternMitte = getMittigstenOrt(ohneASL);
			for (Ort moeglicheMitte : ohneASL) {
				double distanzSumme = 0;
				for (Ort andererOrt : ohneASL) {
					distanzSumme += (ermittleOrtsdistanz(moeglicheMitte,
							andererOrt) / relevanzGradOrtmitASL(andererOrt));
				}
				double aktuellerKennwertMitte = (distanzSumme / relevanzGradOrtmitASL(moeglicheMitte));
				if (aktuellerKennwertMitte < kennwertMitte){
					kennwertMitte = aktuellerKennwertMitte;
					sternMitte = moeglicheMitte;
				}
			}
			// Aufbau des Sterns.
			for (Ort ortA : ohneASL) {
				if (ortA != sternMitte) {
					eingerichteteKorridore.add(new Korridor(sternMitte,
							ortA, Korridor.KENNUNG_ENFC));
				}
			}
			// Upgrade einzelner Korridore.
		} catch (UngueltigerOrt e) {
		}
	}

	/**
	 * Diese Methode entfernt einen ganzen OrtTyp aus einer Liste.
	 * 
	 * @param kennung
	 *            Kennung des OrtTypes
	 * @param liste
	 *            Liste von Orten, aus der entfernt werden soll.
	 * @return Liste ohne diesen OrtTyp.
	 * @author Nils
	 */
	public ArrayList<Ort> entferneOrtTyp(String kennung, ArrayList<Ort> liste) {
		ArrayList<Ort> ohneOrte = new ArrayList<Ort>();
		for (Ort ort : liste) {
			if (!ort.kennung.equals(kennung)) {
				ohneOrte.add(ort);
			}
		}
		return ohneOrte;
	}

	/**
	 * Diese Methode liefert den Ort zurueck, der am dichtesten zum Angegeben
	 * Ort liegt.
	 * 
	 * @param bezugsOrt
	 *            Ort, von dem aus gesucht wird.
	 * @param listeOrte
	 *            Liste mit Orten, in der der dichteste gesucht werden soll.
	 * @return Objekt des Typs Ort, der am dichtesten liegt.
	 */
	public Ort findeDichtestenOrtzuDiesem(Ort bezugsOrt,
			ArrayList<Ort> listeOrte) {
		Ort dichtesterOrt = null;
		double distanz = Double.MAX_VALUE;
		for (Ort ort : listeOrte) {
			if (bezugsOrt == ort) {
				continue;
			}
			double distanzOrt = Math.sqrt(Math.pow(
					(bezugsOrt.koordX - ort.koordX), 2)
					+ Math.pow((bezugsOrt.koordY - ort.koordY), 2));
			if (distanzOrt < distanz) {
				distanz = distanzOrt;
				dichtesterOrt = ort;
			}
		}
		return dichtesterOrt;
	}

	/**
	 * Diese Methode verbindet alle ASL Orte mit dem nächsten nicht ASL Ort.
	 * 
	 * @author Nils
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

	/**
	 * Diese Methode prüft, ob der angegebene KorridorTyp zwischen den Orten
	 * eingerichtet werden kann.
	 * 
	 * @param ortA
	 * @param ortB
	 * @param Typ
	 *            des einzurichtenden Korridors
	 * @return true, wenn möglich. Sonst false.
	 * @author Nils
	 */
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
	 * @param Ort
	 *            eins
	 * @param ort
	 *            zwei
	 * @return Laenge im Wert double
	 */

	public double ermittleOrtsdistanz(Ort orteins, Ort ortzwei) {
		double laengeQuadrat = Math.pow(orteins.koordX - ortzwei.koordX, 2)
				+ Math.pow(orteins.koordY - ortzwei.koordY, 2);
		double laenge = (Math.sqrt(laengeQuadrat));
		return laenge;
	}

	/**
	 * Bekommt eine Liste mit orten und gibt den mit hoechsten Relevanzgrad
	 * zurück. Ort mit hoechsten Relevanzgrad aber nei Auslandsverbindung.
	 * 
	 * @param Liste
	 *            aller zu ueberoruefenden Orte
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
	 * @return Summe aller Baukosten.
	 */
	public double ermittleGesamteBaukosten() {
		int gesamtKosten = 0;
		for (Korridor korridor : eingerichteteKorridore) {
			gesamtKosten += korridor.getBaukosten();
		}
		return gesamtKosten;
	}

	/**
	 * Berechnet die fiktiven x und y Koordinaten des Punktes mit geringster
	 * Abstandssumme zu allen Orten. Im Bereich x(0-199) und y(0-99)
	 * 
	 * @param vorhandeneOrte
	 *            Liste der vorhandenen Orte.
	 * @return Integer Array der Laenge 2 || Pos 0 = x-Wert. Pos 1 = y-Wert.
	 * @author Nils
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
	 * Löscht einen bestimmten Ort aus einer uebergeben Liste. Verändert Liste
	 * nicht, wenn Ort nicht in Liste.
	 * 
	 * @param zuLöschenOrt
	 *            Ort der gelöscht werden soll.
	 * @param listeDerOrte
	 *            Liste aus der der Ort entfernt werden soll.
	 * @return Ursprungsliste ohne den angegeben Ort.
	 * @author Nils
	 */
	public ArrayList<Ort> loescheOrtAusListe(Ort zuLoeschenOrt,
			ArrayList<Ort> listeDerOrte) {
		ArrayList<Ort> saubereListe = new ArrayList<Ort>();
		for (Ort ort : listeDerOrte) {
			if (zuLoeschenOrt != ort) {
				saubereListe.add(ort);
			}
		}
		return saubereListe;
	}

	/**
	 * Diese Methode loescht den angegebenen Korridor aus der uebergebenen
	 * Liste.
	 * 
	 * @param zuLoeschenderKorridor
	 * @param listeKorridore
	 * @return
	 */
	public ArrayList<Korridor> loescheKorriorAusListe(
			Korridor zuLoeschenderKorridor, ArrayList<Korridor> listeKorridore) {
		ArrayList<Korridor> saubereListe = new ArrayList<Korridor>();
		for (Korridor korridor : listeKorridore) {
			if (zuLoeschenderKorridor != korridor) {
				saubereListe.add(korridor);
			}
		}
		return saubereListe;
	}

	/**
	 * Methode liefert den Ort eines Netztes zurück, der am dichtestens am
	 * fiktiven Mittelpunkt des Netzes liegt.
	 * 
	 * @param orteDesNetzes
	 *            Liste alle Orte im Netz.
	 * @return Objekt des Types Ort, liegt am dichtesten am fiktiven
	 *         Mittelpunkt.
	 * @author Nils
	 */
	public Ort getMittigstenOrt(ArrayList<Ort> orteDesNetzes) {
		Integer[] mittelpunkt = berechneNetzMittelpunkt(orteDesNetzes);
		Ort netzMittelpunkt = null;
		double distanz = Double.MAX_VALUE;
		for (Ort ort : orteDesNetzes) {
			double distanzZuOrt = Math.sqrt(Math.pow(
					(mittelpunkt[0] - ort.koordX), 2)
					+ Math.pow((mittelpunkt[1] - ort.koordY), 2));
			if (distanzZuOrt < distanz) {
				distanz = distanzZuOrt;
				netzMittelpunkt = ort;
			}
		}
		return netzMittelpunkt;
	}

	/**
	 * Methode sortiert die Liste aufsteigend nach ihren Abstaenden.
	 * 
	 * @param listeOrte
	 * @param bezugsOrt
	 *            Ort zu dem der Abstand gemessen wird.
	 * @return
	 */
	public ArrayList<Ort> sortiereListeNachAbstand(Ort bezugsOrt,
			ArrayList<Ort> listeOrte) {
		ArrayList<Ort> sortierteOrte = new ArrayList<Ort>();
		int anfangsSize = listeOrte.size();
		while (sortierteOrte.size() < anfangsSize) {
			double distanz = Double.MAX_VALUE;
			Ort verglOrt = null;
			for (Ort ort : listeOrte) {
				double verglDistanz = ermittleOrtsdistanz(bezugsOrt, ort);
				if (verglDistanz < distanz) {
					distanz = verglDistanz;
					verglOrt = ort;
				}
			}
			sortierteOrte.add(verglOrt);
			listeOrte.remove(verglOrt);
		}
		return sortierteOrte;
	}

	/**
	 * Methode versucht einen Korridor definiert durch 2 Orte auf einen best.
	 * Typ zu setzten.
	 * 
	 * @param ortA
	 *            Ort des Korridors
	 * @param ortB
	 *            Ort des Korridors
	 * @param korridorTyp
	 *            Angabe welcher Typ eingerichtet werden soll.
	 * @return 1 bei Erfolg, 0 wenn Korridor nicht gefunden. -1 bei
	 *         Nichtupgrade.
	 * @author Nils
	 */
	public int upgradeKorridor(Ort ortA, Ort ortB, String korridorTyp) {
		for (Korridor korridor : ortA.angebundeneKorridore) {
			if (korridor.ortB == ortB) {
				if (istEinrichtbarerKorridor(ortA, ortB, korridorTyp)) {
					korridor.setKennung(korridorTyp);
					return 1;
				}
				return -1;
			}
		}
		return 0;
	}

	/**
	 * Methode noch nicht fertig. NICHT BENUTZEN!!
	 * 
	 * @param ausgangsOrt
	 * @param ringOrte
	 * @return
	 */
	public double getRelevanzgradZweig(Ort ausgangsOrt, ArrayList<Ort> ringOrte) {
		double gesamtRelevanzgrad = ausgangsOrt.relevanzGrad;
		ArrayList<Ort> ersteZweigOrte = new ArrayList<Ort>();
		for (Korridor korridor : ausgangsOrt.angebundeneKorridore) {
			if (!ringOrte.contains(korridor.bestimmeAnderenOrt(ausgangsOrt))) {
				ersteZweigOrte.add(korridor.bestimmeAnderenOrt(ausgangsOrt));
			}
		}
		return gesamtRelevanzgrad;
	}

	/**
	 * Methode addiert die angebundenen ASL Ort mit ihrer Relevanz drauf.
	 * @param gewaelterOrt
	 * @return
	 */
	public double relevanzGradOrtmitASL(Ort gewaelterOrt) {
		double gesamtRelevanzGrad = gewaelterOrt.relevanzGrad;
		for (Korridor korridor : gewaelterOrt.angebundeneKorridore) {
			Ort ort = korridor.bestimmeAnderenOrt(gewaelterOrt);
			if (ort.kennung == Ort.KENNUNG_AUSLANDSVERBINDUNG) {
				gesamtRelevanzGrad += ort.relevanzGrad;
			}
		}
		return gesamtRelevanzGrad;
	}

	// Hier folgen 4 Methoden zur Ermittlung der Anzahl von eingerichteten
	// Korridortypen.
	public int ermittleAnzahlHLSTKorridore() {
		int anzahlHLST = ermittleAnzahlKorridore("HLST");
		return anzahlHLST;
	}

	public int ermittleAnzahlSICHKorridore() {
		int anzahlSICH = ermittleAnzahlKorridore("SICH");
		return anzahlSICH;
	}

	public int ermittleAnzahlENFCKorridore() {
		int anzahlENFC = ermittleAnzahlKorridore("ENFC");
		return anzahlENFC;
	}

	public int ermittleAnzahlSTNDKorridore() {
		int anzahlSTND = ermittleAnzahlKorridore("STND");
		return anzahlSTND;
	}

	public int ermittleAnzahlKorridore(String kennung) {
		int anzahl = 0;
		for (Korridor korridor : eingerichteteKorridore) {
			if (korridor.getKennung().equals(kennung)) {
				anzahl += 1;
			}
		}
		return anzahl;
	}

	// Ab hier folgen die Methoden zur Feldgestützten Analyse der Karte.

	public int anzahlOrteInFelderListe(ArrayList<Feld> felderLi) {
		int anzahl = 0;
		for (Feld felda : felderLi) {
			anzahl += felda.bestimmeOrteAusserASLImFeld().size();
		}
		return anzahl;
	}

	public ArrayList<Feld> ermittleKonzentrationsfelder(int minOrte,
			int erstellerKantenlaenge) {
		// in felderListe werden die Felder geschrieben, die den Kriterien
		// entsprechen.
		ArrayList<Feld> felderListe = new ArrayList<Feld>();
		for (int x = 0; (x + erstellerKantenlaenge) <= KARTE_GROESSE_X; x++) {
			boolean etwasInX_Gefunden = false;
			for (int y = 0; (y + erstellerKantenlaenge) <= KARTE_GROESSE_Y; y++) {
				Feld neuesFeld = new Feld(this, x, y, erstellerKantenlaenge);
				if (neuesFeld.bestimmeOrteAusserASLImFeld().size() >= minOrte)
					felderListe.add(neuesFeld);
				// System.out.println("Ich habe ("+neuesFeld.startX+"|"+neuesFeld.startY+"), ("+neuesFeld.endX+"|"+neuesFeld.endY+") hinzugefuegt. Dort befinden sich "+neuesFeld.bestimmeOrteImFeld().size()+" Orte.");
				if (neuesFeld.bestimmeOrteAusserASLImFeld().size() == 0)
					y += (erstellerKantenlaenge - 1);
				etwasInX_Gefunden = true;
			}
			if (etwasInX_Gefunden = false)
				x += erstellerKantenlaenge;
		}

		// durchsuche die ArrayList nach dem Maximum, mit dem begonnen wird
		ArrayList<Feld> felderOhneUeberschneidung = new ArrayList<Feld>();

		// loesche alle Felder aus der Liste, die sich mit dem aktuell am
		// dichtesten besiedelten Ort ueberschneiden
		while (felderListe.size() > 0) {
			int max = 0;
			// jedes Feld nach der Anzahl der Orte fragen, um Feld mit
			// dichtester besiedlung zu ermitteln
			for (int index = 0; index < felderListe.size(); index++) {
				if (felderListe.get(max).bestimmeOrteAusserASLImFeld().size() < felderListe
						.get(index).bestimmeOrteAusserASLImFeld().size())
					max = index;
			}
			// loesche jetzt alle felder in der Liste, deren flaeche sich mit
			// der flaeche von der flaeche mit den meisten orten schneidet.
			for (int i = 0; i < felderListe.size(); i++) {
				if ((i != max)
						&& (felderListe.get(max)
								.ueberschneidungMitFeld(felderListe.get(i)))) {
					System.out.println("Es wird gelÃ¶scht:"
							+ felderListe.get(i));
					felderListe.remove(i);
					i--;
					if (i < max)
						max--;
				}
			}
			felderOhneUeberschneidung.add(felderListe.get(max));
			felderListe.remove(max);
		}

		return felderOhneUeberschneidung;
	}
}