package main;

import java.util.ArrayList;

import javax.swing.JOptionPane;

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
 * 
 * @author handritschkp, tollen, bruecknerr, fechnerl
 */

public class Karte {
	public static double budget;
	public ArrayList<Ort> orte;
	public ArrayList<Korridor> eingerichteteKorridore;

	/**
	 * gibt die Breite der Karte an, sodass die Positionen 0 bis kartenGroesseX
	 * moeglich sind.
	 */

	public static final int KARTE_GROESSE_X = 199;
	public static final int MIN_ORTE_IM_FELD = 3;
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

	public void erstelleTrigonRing(ArrayList<Ort> ringOrte) {
		try {
			System.out.println(ringOrte.size());
			if (ringOrte.size() > 2) {
				int mitteX = berechneNetzMittelpunkt(ringOrte)[0];
				int mitteY = berechneNetzMittelpunkt(ringOrte)[1];

				ArrayList<Ort> nichtVerbunden = new ArrayList<Ort>();
				ArrayList<Ort> schonVerbunden = new ArrayList<Ort>();
				for (Ort a : ringOrte) {
					nichtVerbunden.add(a);
				}
				Ort partnerSucher = ringOrte.get(0);
				Ort ersterOrt = ringOrte.get(0);
				nichtVerbunden.remove(0);
				Ort winkelPartner = null;
				double winkelMin;
				double neuesDelta;

				while (nichtVerbunden.size() > 1) {
					winkelMin = Double.MAX_VALUE;
					for (Ort moeglicherPartner : ringOrte) {
						if ((moeglicherPartner != partnerSucher)
								&& (!schonVerbunden.contains(moeglicherPartner))) {
							neuesDelta = Math
									.min(Math.abs(ermittleWinkel(mitteX,
											mitteY, partnerSucher)
											+ 360.0
											- ermittleWinkel(mitteX, mitteY,
													moeglicherPartner)),
											Math.min(
													Math.abs(ermittleWinkel(
															mitteX, mitteY,
															partnerSucher)
															- ermittleWinkel(
																	mitteX,
																	mitteY,
																	moeglicherPartner)
															+ 360.0),
													Math.abs(ermittleWinkel(
															mitteX, mitteY,
															partnerSucher)
															- ermittleWinkel(
																	mitteX,
																	mitteY,
																	moeglicherPartner))));
							if (neuesDelta < winkelMin) {
								winkelMin = neuesDelta;
								winkelPartner = moeglicherPartner;
							}
						}
					}
					if (nichtVerbunden.size() > 1) {
						eingerichteteKorridore.add(new Korridor(partnerSucher,
								winkelPartner, Korridor.KENNUNG_ENFC));
						schonVerbunden.add(partnerSucher);
						nichtVerbunden.remove(partnerSucher);
						partnerSucher = winkelPartner;
						System.out.println(partnerSucher.name + " > "
								+ winkelPartner + "d=" + winkelMin);
					}
				}
				eingerichteteKorridore.add(new Korridor(nichtVerbunden.get(0),
						ersterOrt, Korridor.KENNUNG_ENFC));
			} else if (ringOrte.size() == 2) {
				eingerichteteKorridore.add(new Korridor(ringOrte.get(0),
						ringOrte.get(1), Korridor.KENNUNG_ENFC));
			}
		} catch (UngueltigerOrt e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double ermittleWinkel(int bezugX, int bezugY, Ort ort) {
		int punktY = ort.koordY;
		int punktX = ort.koordX;
		double winkel = Math.atan2(Math.abs(punktY - bezugY),
				Math.abs(punktX - bezugX));
		// II. Quadrant
		if (punktX < bezugX && punktY > bezugY)
			winkel = Math.PI - winkel;
		// III. Quadrant
		if (punktX < bezugX && punktY < bezugY)
			winkel = Math.PI + winkel;
		// IV. Quadrant
		if (punktX > bezugX && punktY < bezugY)
			winkel = 2 * Math.PI - winkel;
		// genau ueber Bezug
		if (punktX == bezugX && punktY > bezugY)
			winkel = 0.5 * Math.PI;
		// genau unter Bezug
		if (punktX == bezugX && punktY < bezugY)
			winkel = 1.5 * Math.PI;
		// genau rechts Bezug
		if (punktX > bezugX && punktY == bezugY)
			winkel = 0;
		// genau links Bezug
		if (punktX < bezugX && punktY == bezugY)
			winkel = Math.PI;
		return Math.toDegrees(winkel);
	}

	/**
	 * Hauptmethode der Klasse Karte. Hier werden alle notwendigen Schritte zur
	 * Netzerstellung ausgeführt.
	 */
	public void erstelleNetz() {
		/*
		 * Sonderfall1 Wenn kein einziger Ort in eingelesen wurde, wird Benutzer
		 * informiert und Programm beendet.
		 */
		if (orte.size() == 0) {
			JOptionPane
					.showMessageDialog(null,
							"Leider war kein Ort in Ihrer Kartendatei! /n Das Programm wird beendet.");
			System.exit(0);
		}
		/*
		 * Sonderfall2 Wenn genau nur ein Ort eingelesen wurde, wird der
		 * Benutzer informiert, eine Netzerstellung macht keinen Sinn und das
		 * Programm beendet
		 */
		if (orte.size() == 1) {
			JOptionPane
					.showMessageDialog(
							null,
							"Leider war nur ein Ort in Ihrer Kartendatei! /n Ein Netz zu erstellen macht keinen Sinn./n Das Programm wird beendet.");
			System.exit(0);
		}

		/*
		 * Sonderfall2 Wenn ausschliesslich Auslandsverbindungen eingelesen
		 * wurden, wird ein Stern aus Sicherheitskorridoren erstellt.
		 */
		boolean alleASLOrt = true;
		for (Ort ort : orte) {
			if (!ort.getKennung().equals(Ort.KENNUNG_AUSLANDSVERBINDUNG)) {
				alleASLOrt = false;
			}
		}
		if (alleASLOrt) {
			erstelleSternSICH(orte);
		}

		// Ablauf bei "gewoehnlicher" Karte.
		verbindeAuslandsorte();
		erstelleRingStruktur(ermittleRelevanteKonzentration(35, 75, 5, 3),
				entferneOrtTyp(Ort.KENNUNG_AUSLANDSVERBINDUNG, orte));
		netzUpgrade();
	}

	public double getAbsKorridorRang(Korridor korridor) {
		return korridor.laenge * korridor.ortA.getRelevanzGrad()
				* korridor.ortB.getRelevanzGrad();
	}

	public void netzUpgrade() {
		ArrayList<Korridor> nochUpgradebareK = new ArrayList<Korridor>();
		for (Korridor k : eingerichteteKorridore) {
			if (isUpgradeable(k)) {
				nochUpgradebareK.add(k);
			}
		}
		while (ermittleGesamteBaukosten() < budget) {
			if (nochUpgradebareK.size() > 0) {
				Korridor upgradeKandidat = nochUpgradebareK.get(0);
				for (Korridor upgradebarerKorridor : nochUpgradebareK) {
					if (getAbsKorridorRang(upgradebarerKorridor) > getAbsKorridorRang(upgradeKandidat)) {
						upgradeKandidat = upgradebarerKorridor;
						getAbsKorridorRang(upgradeKandidat);
					}
				}
				upgradeKandidat.setKennung(upgradeKandidat.getNextKennung());
				if (!isUpgradeable(upgradeKandidat))
					nochUpgradebareK.remove(upgradeKandidat);
			} else
				break;
		}

	}

	/**
	 * Methode zum erstellen eines Ringes im Netz. Weitere Optimierung folgt.
	 * 
	 * @param ohneASL
	 */
	private void erstelleRingStruktur(ArrayList<Feld> relevanzFelder,
			ArrayList<Ort> ohneASLOrte) {
		ArrayList<Ort> ringOrte = new ArrayList<Ort>();
		double hoechsterRG = 0;
		// Aufsuchen der hoechsten Relevanz.
		for (Ort ort : ohneASLOrte) {
			double aktuellerRG = relevanzGradOrtmitASL(ort);
			if (aktuellerRG > hoechsterRG) {
				hoechsterRG = aktuellerRG;
			}
		}
		// Erstellen einer Liste der vorhanden Ringorte.
		head: while (ringOrte.size() < 4) {
			for (double abwertFaktor = 1; abwertFaktor >= 0.1; abwertFaktor = (abwertFaktor - 0.05)) {

				for (Ort ort : ohneASLOrte) {
					if (relevanzGradOrtmitASL(ort) >= (hoechsterRG * abwertFaktor)
							&& !ringOrte.contains(ort)) {
						ringOrte.add(ort);
					}
					if (abwertFaktor < 0.15 || ringOrte.size() == orte.size()) {
						System.out.println("Sehr wenige RingOrte Anzahl: "
								+ ringOrte.size());
						break head;
					}
				}
				if (ringOrte.size() > 3) {
					break head;
				}
			}
		}
		// Check der Relevanzfelder. Ist aus jedem Feld ein Ort enthalten. Sonst
		// besten Ort adden.
		ArrayList<Ort> hinzuzufuegendeOrte = new ArrayList<Ort>();
		if (relevanzFelder.size() > 0) {
			for (Feld aktuellesFeld : relevanzFelder) {
				boolean enthalten = false;
				for (Ort ort : aktuellesFeld.bestimmeOrteImFeld()) {
					if (ringOrte.contains(ort)) {
						enthalten = true;
					}
				}
				if (!enthalten) {
					Ort besterOrt = null;
					double besterRG = 0;
					for (Ort ortAusFeld : aktuellesFeld.bestimmeOrteImFeld()) {
						if (relevanzGradOrtmitASL(ortAusFeld) > besterRG) {
							besterRG = relevanzGradOrtmitASL(ortAusFeld);
							besterOrt = ortAusFeld;
						}
					}
					hinzuzufuegendeOrte.add(besterOrt);
				}
			}
			for (Ort add : hinzuzufuegendeOrte) {
				ringOrte.add(add);
			}
		}

		// Erstellen des Ringes.
		erstelleTrigonRing(ringOrte);
		// Hier erstellung der Liste verbleibenderOrte.
		try {
			ArrayList<Ort> verbleibendeOrte = new ArrayList<Ort>();
			verbleibendeOrte = ohneASLOrte;
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
	 * Erstellt einen Stern vom mittigsten Ort des Netztes mit Sicherheits
	 * Korridoren.
	 * 
	 * @param liste
	 *            Liste aller Orte
	 * 
	 * @author Patrick
	 */
	public void erstelleSternSICH(ArrayList<Ort> liste) {
		Ort sternMitte = sucheOrtMitHoechstenRelevanzGrad(liste);
		try {
			for (Ort ortA : liste) {
				if (ortA != sternMitte) {
					eingerichteteKorridore.add(new Korridor(sternMitte, ortA,
							Korridor.KENNUNG_SICH));
				}
			}
		} catch (UngueltigerOrt e) {
			JOptionPane
					.showMessageDialog(
							null,
							"Erstellung des Sicherheitskorridor nicht moeglich /n Das Programm wird beendet");
			System.exit(0);
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

	public boolean isUpgradeable(Korridor k) {
		String neueKorridorart = k.getNextKennung();
		if (neueKorridorart != "")
			return istEinrichtbarerKorridor(k.ortA, k.ortB, neueKorridorart);
		return false;
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
			String korridorTyp) throws IllegalArgumentException {
		if (!(korridorTyp == Korridor.KENNUNG_ENFC
				|| korridorTyp == Korridor.KENNUNG_SICH
				|| korridorTyp == Korridor.KENNUNG_HLST || korridorTyp == Korridor.KENNUNG_STND))
			throw new IllegalArgumentException();
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
	 * 
	 * @param gewaehlterOrt
	 * @return
	 */
	public double relevanzGradOrtmitASL(Ort gewaehlterOrt) {
		double gesamtRelevanzGrad = gewaehlterOrt.relevanzGrad;
		for (Korridor korridor : gewaehlterOrt.angebundeneKorridore) {
			Ort ort = korridor.bestimmeAnderenOrt(gewaehlterOrt);
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

	// karte.ermittleRelevanteKonzentration(35, 75, 5, 3); eignet sich.
	/**
	 * 
	 * @param vonLaenge
	 *            Beginn der Schleife, die sich alle Felder mit mind. dieser
	 *            Kantenlaenge sucht
	 * @param bisLaenge
	 *            Ende der Schleife
	 * @param schrittweite
	 *            Groesse in km, um die die beiden Kanten eines Suchfeldes pro
	 *            Iteration vergroessert werden.
	 * @param abbruchBedingung
	 *            Schwellenwert, der bei Uebersteigen fuer einen Abbruch der
	 *            Schleife sorgt: Anzahl von Feldern.
	 * @return eine Liste von Feldern, die den o.g. Kriterien entsprechen
	 * @throws IllegalArgumentException
	 *             Im Fall von ungueltigen Zahlenangaben fuer o.g. Werte.
	 * @author bruecknerr
	 */
	public ArrayList<Feld> ermittleRelevanteKonzentration(int vonLaenge,
			int bisLaenge, int schrittweite, int abbruchBedingung)
			throws IllegalArgumentException {
		/**
		 * Da wichtige Teile der Schleife und ihrem Erfolg von den mitgegebenen
		 * Argumenten abhaengen, wird mit dem boolschen Ausdruck illegalArgument
		 * festgestellt, ob ein grober Fehler vorliegt:
		 * 
		 * Es waere unverhaeltnismaessig, Felder zu untersuchen, deren Laenge
		 * groesser als die Abmasse der Karte sind.
		 */
		boolean illegalArgument = ((bisLaenge <= vonLaenge)
				|| (schrittweite < 1 || schrittweite > Math.min(
						KARTE_GROESSE_X, KARTE_GROESSE_Y))
				|| (vonLaenge > Math.max(KARTE_GROESSE_X, KARTE_GROESSE_Y)) || (bisLaenge > Math
				.max(KARTE_GROESSE_X, KARTE_GROESSE_Y)));
		if (illegalArgument)
			throw new IllegalArgumentException();

		ArrayList<Feld> relevanteKonzentrationsFelder = new ArrayList<Feld>();
		/**
		 * Fuer alle Feldgroessen wird nun ermittelt, welche Anzahl an
		 * Konzentrationsfeldern zu ermitteln ist. Sobald eine Anzahl von
		 * abbruchBedingung ueberschritten ist, werden die Felder, die diese
		 * Bedingung ueberschritten haben, zurueckgegeben.
		 */
		for (int kantenlaenge = vonLaenge; kantenlaenge < bisLaenge; kantenlaenge += schrittweite) {
			if (ermittleKonzentrationsfelder(kantenlaenge).size() > relevanteKonzentrationsFelder
					.size()) {
				relevanteKonzentrationsFelder = ermittleKonzentrationsfelder(kantenlaenge);
				/**
				 * sobald der als Argument uebergebene Schwellenwert
				 * ueberschritten ist, soll die Schleife abgebrochen werden.
				 */
				if (relevanteKonzentrationsFelder.size() > abbruchBedingung)
					break;
			}
		}
		return relevanteKonzentrationsFelder;
	}

	public ArrayList<Feld> ermittleKonzentrationsfelder(
			int erstellerKantenlaenge) {
		/**
		 * in felderOhneUeberschneidung werden diejenigen Felder geschrieben,
		 * die letzlich auch zurueckgegeben werden. felderOhneUeberschneidung
		 * ist eine Teilliste von felderListe, bereinigt um diejenigen Felder,
		 * die Orte mit anderen Felder gemeinsam haben
		 * (Unabhaenigkeitskriterium)
		 */
		ArrayList<Feld> felderOhneUeberschneidung = new ArrayList<Feld>();
		/**
		 * in felderListe werden weiter unten die Felder geschrieben, die den
		 * Kriterien entsprechen. (bisher: sie beschreiben ein Quadrat, in
		 * dessen Gebiet sich mindestens MIN_ORTE_IM_FELD Orte befinden.)
		 */
		ArrayList<Feld> felderListe = new ArrayList<Feld>();

		/**
		 * fuege alle erstellbaren Felder mit der gegebenen Kantenlaenge der
		 * ArrayList hinzu, wenn diese das Kriterium erfuellen.
		 */
		for (int x = 0; (x + erstellerKantenlaenge) <= KARTE_GROESSE_X; x++) {
			for (int y = 0; (y + erstellerKantenlaenge) <= KARTE_GROESSE_Y; y++) {
				Feld neuesFeld = new Feld(this, x, y, erstellerKantenlaenge);
				if (neuesFeld.bestimmeOrteImFeld().size() >= MIN_ORTE_IM_FELD)
					felderListe.add(neuesFeld);
			}
		}

		/**
		 * loesche alle Felder aus der Liste, die sich mit dem aktuell am
		 * dichtesten besiedelten Ort ueberschneiden
		 */
		while (felderListe.size() > 0) {
			int max = 0;
			/**
			 * Ermittlung desjenigen Feldes der felderListe, das am meisten Orte
			 * enthaelt. Felder, auf die dies zugetroffen hat, werden am Ende
			 * eines Schleifendurchlaufs in die felderOhneUberlappung
			 * aufgenommen.
			 */
			for (int index = 0; index < felderListe.size(); index++) {
				if (felderListe.get(max).bestimmeOrteImFeld().size() < felderListe
						.get(index).bestimmeOrteImFeld().size())
					max = index;
			}
			/**
			 * Loeschen derjenigen Felder aus felderListe, die eine Schnittmenge
			 * mit dem ueberlegenen Feld haben.
			 */
			for (int i = 0; i < felderListe.size(); i++) {
				if ((i != max)
						&& (felderListe.get(max)
								.hatOrtsSchnittmengeMit(felderListe.get(i)))) {
					felderListe.remove(i);
					i--;
					/**
					 * da die indizes beim loeschen eines Elt. aus einer
					 * ArrayList veraendert werden, muss sichergestellt werden,
					 * dass kein Element uebersprungen wird. der Zeiger auf das
					 * feld mit den meisten Elementen muss nur dann berichtigt
					 * werden, wenn max > i und damit um 1 nach vorn rutscht
					 */
					if (i < max)
						max--;
				}
			}
			// Hinzufuegen des ermittelten Feldes zur Rueckgabeliste
			felderOhneUeberschneidung.add(felderListe.get(max));
			// Bereinigung der felderListe um das bereits akzeptierte Feld
			felderListe.remove(max);
		}
		return felderOhneUeberschneidung;
	}
}