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
	public static final int SCHWELLFAKTOR_QUERVERBINDUNG = 3;
	public static final int BEGINN_FELDABTASTUNG = 35;
	public static final int END_FELDABTASTUNG = 75;
	public static final int SCHRITTE_FELDABTASTUNG = 5;
	public static final int MINORT_FELDABTASTUNG = 3;
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
	 * Erstellt einen Ring auf Basis der Informationen mitgegebener Orte: Mit
	 * dem Mittelpunkt der Ortsmenge werden Steigungsdreiecke gebildet. Mit
	 * einer Unterscheidung der Quadranten in ermittleWinkel ist es moeglich,
	 * alle Winkel miteinander zu vergleichen. Ziel ist die moeglichst optimale
	 * Erstellung eines Kreisrings.
	 * 
	 * @param ringOrte
	 * @author bruecknerr
	 */
	private void erstelleTrigonRing(ArrayList<Ort> ringOrte) {
		try {
			System.out.println(ringOrte.size());
			if (ringOrte.size() > 2) {
				int mitteX = berechneNetzMittelpunkt(ringOrte)[0];
				int mitteY = berechneNetzMittelpunkt(ringOrte)[1];

				ArrayList<Ort> nichtVerbunden = new ArrayList<Ort>();
				ArrayList<Ort> schonVerbunden = new ArrayList<Ort>();
				ArrayList<Korridor> ringKorridore = new ArrayList<Korridor>();
				for (Ort a : ringOrte) {
					nichtVerbunden.add(a);
				}
				Ort partnerSucher = ringOrte.get(0);
				// Damit der Polygonzug am Ende geschlossen wird, muss der
				// Anfang gemerkt werden
				Ort ersterOrt = ringOrte.get(0);

				nichtVerbunden.remove(0);
				Ort winkelPartner = null;
				double winkelMin;
				double neuesDelta;
				/*
				 * Aufsuchen einer minimalen Winkelabweichung
				 */
				while (nichtVerbunden.size() > 1) {
					winkelMin = Double.MAX_VALUE;
					for (Ort moeglicherPartner : ringOrte) {
						if ((moeglicherPartner != partnerSucher)
								&& (!schonVerbunden.contains(moeglicherPartner))) {
							/*
							 * mehrere Bedingungen, um dem Bereich um die 0 Grad
							 * herum gerecht zu werden
							 */
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
						Korridor naechstesStueck = new Korridor(partnerSucher,
								winkelPartner, Korridor.KENNUNG_ENFC);
						eingerichteteKorridore.add(naechstesStueck);
						ringKorridore.add(naechstesStueck);
						schonVerbunden.add(partnerSucher);
						nichtVerbunden.remove(partnerSucher);
						partnerSucher = winkelPartner;
					}
				}
				/*
				 * Ring schliessen.
				 */
				Korridor letztesStueck = new Korridor(nichtVerbunden.get(0),
						ersterOrt, Korridor.KENNUNG_ENFC);
				eingerichteteKorridore.add(letztesStueck);
				ringKorridore.add(letztesStueck);

				/*
				 * Ueberpruefen, ob der Polygonzug sehr lang wurde und damit
				 * Querverbindungen notwendig werden, um ein sinnvolles Netz zu
				 * erhalten.
				 */
				double ringLaenge = 0.0;
				for (Korridor k : ringKorridore) {
					ringLaenge += k.getLaenge();
				}
				/*
				 * an dieser Stelle im Programm ist ohnehin garantiert, dass
				 * mind. 3 ringOrte vorhanden sind. es wird jetzt die groesste
				 * Wegersparnis ausgerechnet, die mit dem hinzufuegen eines
				 * zusaetzlichen Korridores erzielt werden kann.
				 */
				while (ringLaenge > Math.min(KARTE_GROESSE_X, KARTE_GROESSE_Y) * 2) {
					Korridor hoechsterErsparniskorridor = new Korridor(
							ringOrte.get(0), ringOrte.get(1),
							Korridor.KENNUNG_ENFC);
					double besteErsparnis = 0.0;
					for (Ort r1 : ringOrte) {
						for (Ort r2 : ringOrte) {
							if (r1 != r2) {
								Korridor moeglicheQuerK = new Korridor(r1, r2,
										Korridor.KENNUNG_ENFC);
								double ringZug = 0;
								Ort hop = r1;
								Ort lastHop = null;
								Korridor move = null;
								while (true) {
									for (Korridor k : hop
											.getAngebundeneKorridore()) {
										/*
										 * ASL_ausschliesen
										 */
										if (k.bestimmeAnderenOrt(hop)
												.getKennung() != Ort.KENNUNG_AUSLANDSVERBINDUNG
												&& k.bestimmeAnderenOrt(hop) != lastHop) {
											move = k;
											break;
										}
									}
									ringZug += move.getLaenge();
									lastHop = hop;
									hop = move.bestimmeAnderenOrt(hop);
									if (hop == r2)
										break;
								}
								if (ringZug > 0.5 * ringLaenge)
									ringZug = ringLaenge - ringZug;

								double moeglicheErsparnis = ringZug
										- moeglicheQuerK.getLaenge();
								if (moeglicheErsparnis > besteErsparnis
										&& !korridorExistent(moeglicheQuerK)) {
									besteErsparnis = moeglicheErsparnis;
									hoechsterErsparniskorridor = moeglicheQuerK;
								}
							}
						}
					}
					if ((besteErsparnis / hoechsterErsparniskorridor
							.getLaenge()) > SCHWELLFAKTOR_QUERVERBINDUNG) {
						eingerichteteKorridore.add(hoechsterErsparniskorridor);
					} else {
						break;
					}
				}

			} else if (ringOrte.size() == 2) {
				/*
				 * kein Ring moeglich, es entsteht ein Korridor
				 */
				eingerichteteKorridore.add(new Korridor(ringOrte.get(0),
						ringOrte.get(1), Korridor.KENNUNG_ENFC));
			}
		} catch (UngueltigerOrt e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean korridorExistent(Korridor k) {
		for (Korridor q : eingerichteteKorridore) {
			if ((q.ortA == k.ortA && q.ortB == k.ortB)
					|| (q.ortA == k.ortB && q.ortB == k.ortA))
				return true;
		}
		return false;
	}

	/**
	 * @param bezugX
	 *            Beginn des Steigungsdreiecks X
	 * @param bezugY
	 *            Beginn des Steigungsdreiecks Y
	 * @param ort
	 *            dessen Winkel bestimmt werden soll
	 * @return Winkel eines durch einen Punkt und einen Ort beschriebenen
	 *         Steigungsdreiecks in Grad
	 * @author bruecknerr
	 */
	private double ermittleWinkel(int bezugX, int bezugY, Ort ort) {
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
							"Es befindet sich kein Ort in Ihrer Kartendatei! /n Das Programm wird beendet.");
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
							"Es befindet sich nur ein Ort in Ihrer Kartendatei! /n Eine Netzplanung ist nicht sinnvoll./n Das Programm wird beendet.");
			System.exit(0);
		}

		/*
		 * Sonderfall2 Wenn ausschliesslich Auslandsverbindungen eingelesen
		 * wurden, wird ein Stern aus Sicherheitskorridoren erstellt.
		 */
		boolean nur_ASL_Vorhanden = true;
		for (Ort ort : orte) {
			if (!ort.getKennung().equals(Ort.KENNUNG_AUSLANDSVERBINDUNG)) {
				nur_ASL_Vorhanden = false;
			}
		}
		if (nur_ASL_Vorhanden) {
			erstelleSternSICH(orte);
			netzUpgrade();
			return;
		}

		// Ablauf bei "gewoehnlicher" Karte.
		verbindeAuslandsorte();
		erstelleRingStruktur(
				ermittleRelevanteKonzentration(BEGINN_FELDABTASTUNG,
						END_FELDABTASTUNG, SCHRITTE_FELDABTASTUNG,
						MIN_ORTE_IM_FELD),
				entferneOrtTyp(Ort.KENNUNG_AUSLANDSVERBINDUNG, orte));
		netzUpgrade();
	}

	private double getAbsKorridorRang(Korridor korridor) {
		return korridor.laenge * korridor.ortA.getRelevanzGrad()
				* korridor.ortB.getRelevanzGrad();
	}

	private void netzUpgrade() {
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
	private void erstelleSternSICH(ArrayList<Ort> liste) {
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
	private ArrayList<Ort> entferneOrtTyp(String kennung, ArrayList<Ort> liste) {
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
	private Ort findeDichtestenOrtzuDiesem(Ort bezugsOrt,
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
	 * Verbinden aller ASL Orte mit dem naechsten nicht ASL Ort.
	 * @author Nils
	 */
	private void verbindeAuslandsorte() {
		try {
			ArrayList<Ort> aslOrte = new ArrayList<Ort>();
			Ort naechsterOrt = null;
			for (Ort ortA : orte) {
				if (ortA.kennung.equals(Ort.KENNUNG_AUSLANDSVERBINDUNG)) {
					aslOrte.add(ortA);
				}
			}
			for (Ort ortASL : aslOrte) {
				double minDistanz = Double.MAX_VALUE;
				for (Ort nichtASL_Ort : orte) {
					if (nichtASL_Ort.kennung
							.equals(Ort.KENNUNG_AUSLANDSVERBINDUNG)) {
						continue;
					}
					double distanzEinOrt = ermittleOrtsdistanz(ortASL,
							nichtASL_Ort);
					if (distanzEinOrt < minDistanz) {
						minDistanz = distanzEinOrt;
						naechsterOrt = nichtASL_Ort;
					}
				}
				eingerichteteKorridore.add(new Korridor(ortASL, naechsterOrt,
						Korridor.KENNUNG_SICH));
			}
		} catch (UngueltigerOrt e) {
		}
	}

	/**
	 * @param k	zu Korridor
	 * @return
	 */
	private boolean isUpgradeable(Korridor k) {
		String neueKorridorart = k.getNextKennung();
		if (neueKorridorart != "")
			return istEinrichtbarerKorridor(k.ortA, k.ortB, neueKorridorart);
		return false;
	}

	/**
	 * @param ortA
	 * @param ortB
	 * @param korridorKennung
	 *            des einzurichtenden Korridors
	 * @return Einrichtbarkeit als boolschen Wert.

	 * @throws IllegalArgumentException
	 *             falls die Kennung nicht zulaessig ist
	 * @author tollen
	 */
	private boolean istEinrichtbarerKorridor(Ort ortA, Ort ortB,
			String korridorKennung) throws IllegalArgumentException {
		if (!(korridorKennung == Korridor.KENNUNG_ENFC
				|| korridorKennung == Korridor.KENNUNG_SICH
				|| korridorKennung == Korridor.KENNUNG_HLST || korridorKennung == Korridor.KENNUNG_STND))
			throw new IllegalArgumentException();
		if (korridorKennung.equals(Korridor.KENNUNG_ENFC)) {
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
		if (korridorKennung.equals(Korridor.KENNUNG_HLST)) {
			if ((ortA.getKennung().equals(Ort.KENNUNG_HAUPTORT) || ortA
					.getKennung().equals(Ort.KENNUNG_UMSCHLAGPUNKT))
					&& (ortB.getKennung().equals(Ort.KENNUNG_HAUPTORT) || ortB
							.getKennung().equals(Ort.KENNUNG_UMSCHLAGPUNKT))) {
				return true;
			} else {
				return false;
			}
		}
		if (korridorKennung.equals(Korridor.KENNUNG_SICH)) {
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
		if (korridorKennung.equals(Korridor.KENNUNG_STND)) {
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
	 * @return Distanz der angegebenen Orte
	 */

	private double ermittleOrtsdistanz(Ort orteins, Ort ortzwei) {
		double laengeQuadrat = Math.pow(orteins.koordX - ortzwei.koordX, 2)
				+ Math.pow(orteins.koordY - ortzwei.koordY, 2);
		double laenge = (Math.sqrt(laengeQuadrat));
		return laenge;
	}

	/**
	 * @param liste
	 *            der zu untersuchenden Orte
	 * @return Ort mit maximalem Relevanzgrad der keine Auslandsverbindung ist
	 */
	private Ort sucheOrtMitHoechstenRelevanzGrad(ArrayList<Ort> liste) {

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
	 * @return Summe aller Baukosten der Korridore dieser Karteninstanz.
	 */
	public double ermittleGesamteBaukosten() {
		int gesamtKosten = 0;
		for (Korridor korridor : eingerichteteKorridore) {
			gesamtKosten += korridor.getBaukosten();
		}
		return gesamtKosten;
	}

	/**
	 * Berechnung der x- und y-Koordinate des Punktes mit geringster
	 * Abstandssumme zu allen Orten. Im Bereich x(0-KARTE_GROESSE_X) und
	 * y(0-KARTE_GROESSE_Y)
	 * 
	 * @param ortsListe
	 *            Liste der vorhandenen Orte.
	 * @return Integer[] mit Laenge 2 || [0] = x-Wert. [1] = y-Wert.
	 * @author tollen, bruecknerr
	 */
	private Integer[] berechneNetzMittelpunkt(ArrayList<Ort> ortsListe) {
		Integer[] mittelpunkt = new Integer[2];
		double distanz = Double.MAX_VALUE;
		for (int xKoordinate = 0; xKoordinate <= KARTE_GROESSE_X; xKoordinate++) {
			for (int yKoordinate = 0; yKoordinate <= KARTE_GROESSE_Y; yKoordinate++) {
				double distanzSumme = 0;
				for (Ort ort : ortsListe) {
					double distanzSchleife = Math.sqrt(Math.pow(
							(xKoordinate - ort.koordX), 2)
							+ Math.pow((yKoordinate - ort.koordY), 2));
					distanzSumme += distanzSchleife;
				}
				if (distanzSumme < distanz) {
					distanz = distanzSumme;
					mittelpunkt[0] = xKoordinate;
					mittelpunkt[1] = yKoordinate;
				}
			}
		}
		return mittelpunkt;
	}

	/**
	 * @param gewaehlterOrt
	 * @return Summe der Relevanzgrade von gewahlterOrt und dessen angebundenen
	 *         Auslandsverbindungen
	 */
	private double relevanzGradOrtmitASL(Ort gewaehlterOrt) {
		double gesamtRelevanzGrad = gewaehlterOrt.relevanzGrad;
		for (Korridor korridor : gewaehlterOrt.angebundeneKorridore) {
			Ort ort = korridor.bestimmeAnderenOrt(gewaehlterOrt);
			if (ort.kennung == Ort.KENNUNG_AUSLANDSVERBINDUNG) {
				gesamtRelevanzGrad += ort.relevanzGrad;
			}
		}
		return gesamtRelevanzGrad;
	}

	/**
	 * @return Anzahl der Korridore mit dem Typ Hochleistungskorridor in dieser
	 *         Karteninstanz
	 */
	public int ermittleAnzahlHLSTKorridore() {
		int anzahlHLST = ermittleAnzahlKorridore(Korridor.KENNUNG_HLST);
		return anzahlHLST;
	}

	/**
	 * @return Anzahl der Korridore mit dem Typ Sicherheitskorridor in dieser
	 *         Karteninstanz
	 */
	public int ermittleAnzahlSICHKorridore() {
		int anzahlSICH = ermittleAnzahlKorridore(Korridor.KENNUNG_SICH);
		return anzahlSICH;
	}

	/**
	 * @return Anzahl der Korridore mit dem Typ Einfacher Korridor in dieser
	 *         Karteninstanz
	 */
	public int ermittleAnzahlENFCKorridore() {
		int anzahlENFC = ermittleAnzahlKorridore(Korridor.KENNUNG_ENFC);
		return anzahlENFC;
	}

	/**
	 * @return Anzahl der Korridore mit dem Typ Standardkorridor in dieser
	 *         Karteninstanz
	 */
	public int ermittleAnzahlSTNDKorridore() {
		int anzahlSTND = ermittleAnzahlKorridore(Korridor.KENNUNG_STND);
		return anzahlSTND;
	}

	/**
	 * @param kennung
	 *            Kennung der Korridorart
	 * @return Anzahl der Korridore mit angebebenem Typ in dieser Karteninstanz
	 */
	private int ermittleAnzahlKorridore(String kennung) {
		int anzahl = 0;
		for (Korridor korridor : eingerichteteKorridore) {
			if (korridor.getKennung().equals(kennung)) {
				anzahl += 1;
			}
		}
		return anzahl;
	}

	/**
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
	private ArrayList<Feld> ermittleRelevanteKonzentration(int vonLaenge,
			int bisLaenge, int schrittweite, int abbruchBedingung)
			throws IllegalArgumentException {
		/*
		 * Da wichtige Teile der Schleife und ihrem Erfolg von den mitgegebenen
		 * Argumenten abhaengen, wird festgestellt, ob ein grober Fehler
		 * vorliegt:
		 * 
		 * Es ist nicht vorgesehen, Felder zu untersuchen, deren Laenge groesser
		 * als die Karte an sich sind.
		 */
		if (schrittweite > Math.min(KARTE_GROESSE_X, KARTE_GROESSE_Y)
				|| vonLaenge > Math.max(KARTE_GROESSE_X, KARTE_GROESSE_Y)
				|| bisLaenge <= vonLaenge || schrittweite < 1
				|| bisLaenge > Math.max(KARTE_GROESSE_X, KARTE_GROESSE_Y))
			throw new IllegalArgumentException();

		ArrayList<Feld> relevanteKonzentrationsFelder = new ArrayList<Feld>();
		/*
		 * Fuer alle Feldgroessen wird ermittelt, welche Anzahl an
		 * Konzentrationsfeldern ermittelt werden kann. Sobald eine Anzahl von
		 * abbruchBedingung ueberschritten ist, werden die Felder, die zum
		 * Ueberschreiten dieser Bedingung gefuehrt haben, zurueckgegeben.
		 */
		for (int kantenlaenge = vonLaenge; kantenlaenge < bisLaenge; kantenlaenge += schrittweite) {
			if (ermittleKonzentrationsfelder(kantenlaenge).size() > relevanteKonzentrationsFelder
					.size()) {
				relevanteKonzentrationsFelder = ermittleKonzentrationsfelder(kantenlaenge);
				if (relevanteKonzentrationsFelder.size() > abbruchBedingung)
					break;
			}
		}
		return relevanteKonzentrationsFelder;
	}

	/**
	 * 
	 * @param erstellerKantenlaenge
	 * @return Eine Teilliste von felderListe, bereinigt um diejenigen Felder,
	 *         die Orte mit ueberlegenen Feldern gemeinsam haben
	 *         (Unabhaenigkeitskriterium).
	 * @author bruecknerr
	 */
	private ArrayList<Feld> ermittleKonzentrationsfelder(
			int erstellerKantenlaenge) {
		/*
		 * In felderListe werden weiter unten diejenigen Felder geschrieben, die
		 * den Kriterien entsprechen. Sie beschreiben ein quadratische Flaeche,
		 * in dessen Gebiet sich mindestens MIN_ORTE_IM_FELD Orte befinden.
		 */
		ArrayList<Feld> felderListe = new ArrayList<Feld>();
		ArrayList<Feld> unabhaengigeFelder = new ArrayList<Feld>();
		/*
		 * Fuege alle erstellbaren Felder mit der gegebenen Kantenlaenge der
		 * ArrayList hinzu, wenn diese das Kriterium erfuellen.
		 */
		for (int x = 0; (x + erstellerKantenlaenge) <= KARTE_GROESSE_X; x++) {
			for (int y = 0; (y + erstellerKantenlaenge) <= KARTE_GROESSE_Y; y++) {
				Feld neuesFeld = new Feld(this, x, y, erstellerKantenlaenge);
				if (neuesFeld.bestimmeOrteImFeld().size() >= MIN_ORTE_IM_FELD)
					felderListe.add(neuesFeld);
			}
		}

		/*
		 * loesche alle Felder aus der Liste, die sich mit dem aktuell am
		 * dichtesten besiedelten Feld Orte teilen
		 */
		while (felderListe.size() > 0) {
			int max = 0;
			/*
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
			/*
			 * Loeschen derjenigen Felder aus felderListe, deren Ortslisten eine
			 * Schnittmenge mit der Ortsliste des ueberlegenen Feldes haben.
			 */
			for (int i = 0; i < felderListe.size(); i++) {
				if ((i != max)
						&& (felderListe.get(max)
								.hatOrtsSchnittmengeMit(felderListe.get(i)))) {
					/*
					 * da die indizes beim loeschen eines Elementes aus einer
					 * ArrayList veraendert werden, muss sichergestellt werden,
					 * dass kein Element uebersprungen wird. Der Zeiger auf das
					 * Feld mit den meisten Elementen muss nur dann berichtigt
					 * werden, wenn max > i und damit um 1 nach vorn rutscht
					 */
					felderListe.remove(i);
					i--;
					if (i < max)
						max--;
				}
			}
			// Hinzufuegen des ermittelten Feldes zur Rueckgabeliste
			unabhaengigeFelder.add(felderListe.get(max));
			// Bereinigung der felderListe um das bereits akzeptierte Feld
			felderListe.remove(max);
		}
		return unabhaengigeFelder;
	}
}