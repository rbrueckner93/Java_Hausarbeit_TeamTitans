package netz;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.NetzBauFehler;
import exceptions.UngueltigerOrt;
import orte.Ort;
import simulation.Flugroute;

/**
 * @author handritschkp, tollen, bruecknerr, fechnerl
 */
public class Karte {
	public static double budget;
	public static final int KARTE_GROESSE_X = 199;
	public static final int KARTE_GROESSE_Y = 99;
	// Wie viele Orte muessen mindestens in einem Feld sein, damit ein Feld als
	// Feld gilt?
	public static final int MIN_ORTE_IM_FELD = 3;
	// Wie oft muss eine Querverbindung ihre eigene Laenge einsparen, um
	// eingerichtet zu werden?
	public static double SCHWELLFAKTOR_QUERVERBINDUNG = 2;
	// Wie lang darf ein Ring max sein, um ohne Querverbindung auszukommen.
	public static final double MAX_RINGLAENGE_OHNE_QUERVERBINDUNG = (KARTE_GROESSE_Y * 2);
	// Schrittgroesse bei der Verringerung des Abwertfaktors um geeignete Ringorte
	// zu finden.
	public static final double SCHRITTWEITE_RING_ABWERTFAKTOR = 0.05;
	// Wert der unterschritten werden muss, um keine weiteren Ringorte mehr
	// aufzunehmen. (Achtung Rundungsprobleme)
	public static final double SCHWELLWERT_ABBRUCH_RINGORTE = 0.15;
	// Mit welcher Kantenlaenge soll der Feldalgorithmus beginnen,
	// Konzentrationsfelder zu suchen?
	public static final int BEGINN_FELDABTASTUNG = 35;
	// Ab welcher Kantenlaenge soll kein Feld mehr anerkannt werden?
	public static final int END_FELDABTASTUNG = 75;
	// Mit welcher Schrittgroesse soll die Kantenlaenge vergroessert werden?
	public static final int SCHRITTE_FELDABTASTUNG = 5;
	// Wie viele Orte muessen innerhalb des Quadrats befindlich sein, damit es
	// als Feld erkannt wird?
	public static final int MINORT_FELDABTASTUNG = 3;
	private ArrayList<Ort> orte;
	private ArrayList<Korridor> eingerichteteKorridore;
	private String nameKartendatei;

	public Karte() {
		super();
		orte = new ArrayList<Ort>();
		eingerichteteKorridore = new ArrayList<Korridor>();
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		Karte.budget = budget;
	}

	public String getNameKartendatei() {
		return nameKartendatei;
	}

	public void setNameKartendatei(String nameKartendatei) {
		this.nameKartendatei = nameKartendatei;
	}

	public ArrayList<Ort> getListeAllerOrte() {
		return orte;
	}

	public ArrayList<Korridor> getEingerichteteKorridore() {
		return eingerichteteKorridore;
	}

	public ArrayList<Ort> getOrte() {
		return orte;
	}

	/**
	 * Erstellt einen Ring auf Basis der Informationen mitgegebener Orte: Mit
	 * dem Mittelpunkt der Ortsmenge werden Steigungsdreiecke gebildet.
	 * 
	 * Ziel ist die moeglichst optimale Erstellung eines Kreisrings.
	 * 
	 * @param ringOrte
	 * @author bruecknerr
	 */
	private void erstelleTrigonRing(ArrayList<Ort> ringOrte) {
		try {
			if (ringOrte.size() > 2) {
				Integer[] mitte = berechneNetzMittelpunkt(ringOrte);

				// Ausgangspunkt der Ringbildung ist eine Liste aller ringOrte
				ArrayList<Ort> nichtVerbunden = new ArrayList<Ort>();
				for (Ort a : ringOrte) {
					nichtVerbunden.add(a);
				}

				ArrayList<Ort> schonVerbunden = new ArrayList<Ort>();
				ArrayList<Korridor> ringKorridore = new ArrayList<Korridor>();
				/*
				 * Damit der Polygonzug am Ende geschlossen wird, muss der
				 * Anfang gespeichert werden
				 */
				Ort ersterOrt = ringOrte.get(0);

				// Der aktuell behandelte Ort, partnerSucher, gilt mit Start des
				// Ablaufs als verbunden.
				schonVerbunden.add(ringOrte.get(0));
				nichtVerbunden.remove(0);
				// Ebenso wird er als erster partnerSucher gesetzt
				Ort partnerSucher = ringOrte.get(0);

				// Vergleichsobjekte / -variablen
				Ort winkelPartner = null;
				double neuesDelta;

				// Aufsuchen einer minimalen Winkelabweichung
				while (!nichtVerbunden.isEmpty()) {
					double winkelMin = Double.MAX_VALUE;
					for (Ort moeglicherPartner : ringOrte) {
						if ((moeglicherPartner != partnerSucher)
								&& (!schonVerbunden.contains(moeglicherPartner))) {

							/*
							 * Dem Bereich um die 360 bzw. 0 Grad herum gerecht
							 * werden
							 */

							double partnerWinkel = ermittleWinkel(mitte[0],
									mitte[1], moeglicherPartner);
							double sucherWinkel = ermittleWinkel(mitte[0],
									mitte[1], partnerSucher);
							neuesDelta = Math.min(Math.abs(sucherWinkel + 360.0
									- partnerWinkel), Math.min(
									Math.abs(sucherWinkel - partnerWinkel
											+ 360.0),
									Math.abs(sucherWinkel - partnerWinkel)));

							if (neuesDelta < winkelMin) {
								winkelMin = neuesDelta;
								winkelPartner = moeglicherPartner;
							}
						}
					}
					if (nichtVerbunden.size() > 1) {
						/*
						 * Ort winkelPartner besitzt den kleinsten
						 * Winkelunterschied. Er wird als Partner im neuen
						 * Korridor verwendet.
						 */
						Korridor naechstesStueck = new Korridor(partnerSucher,
								winkelPartner, Korridor.KENNUNG_ENFC, true);
						eingerichteteKorridore.add(naechstesStueck);
						ringKorridore.add(naechstesStueck);

						schonVerbunden.add(partnerSucher);
						nichtVerbunden.remove(partnerSucher);

						// Im naechsten Durchlauf soll winkelPartner seinen
						// Partner suchen
						partnerSucher = winkelPartner;
					} else {
						// Ring schliessen.
						Korridor letztesStueck = new Korridor(
								nichtVerbunden.get(0), ersterOrt,
								Korridor.KENNUNG_ENFC, true);
						eingerichteteKorridore.add(letztesStueck);
						ringKorridore.add(letztesStueck);
						nichtVerbunden.remove(0);
					}
				}

				/*
				 * QUERVERBINDUNGEN Ueberpruefen, ob der Polygonzug lang wurde:
				 * Sind Querverbindungen notwendig, um ein sinnvolles Netz zu
				 * erhalten?
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
				while (ringLaenge > MAX_RINGLAENGE_OHNE_QUERVERBINDUNG) {
					Korridor hoechsterErsparniskorridor = new Korridor(
							ringOrte.get(0), ringOrte.get(1),
							Korridor.KENNUNG_ENFC, false);
					double besteErsparnis = 0.0;
					for (Ort ortA : ringOrte) {
						for (Ort ortB : ringOrte) {
							if (ortA != ortB) {
								/*
								 * Ermittlung der Wegstrecke zwischen ortA und
								 * ortB auf dem Ring und den bisher
								 * eingerichteten Querverbindungen
								 */
								double routenSum = 0;
								/*
								 * Erzeugen einer temporaeren Route, um vom
								 * Routenfindungsalgorithmus Gebrauch machen zu
								 * koennen
								 */
								Flugroute testRoute = new Flugroute(ortA, ortB,
										1);
								testRoute.ermittleBesteRoute();
								for (Korridor k : testRoute.getReiseListe()) {
									routenSum += k.getLaenge();
								}
								double distanz = ermittleOrtsdistanz(ortA, ortB);
								double moeglicheErsparnis = routenSum - distanz;
								/*
								 * erzielt der Korridor eine gewuenschte
								 * Ersparung gegenueber der Testroute ohne
								 * diesen Korridor, die groesser als bisher
								 * ermittle Einsparungen ist, wird er zu dem am
								 * meisten einsparenden Korridor gekuert.
								 */
								if (moeglicheErsparnis > besteErsparnis) {
									Korridor moeglicherQuerkorridor = new Korridor(
											ortA, ortB, Korridor.KENNUNG_ENFC,
											false);
									besteErsparnis = moeglicheErsparnis;
									hoechsterErsparniskorridor = moeglicherQuerkorridor;
								}
							}
						}
					}
					/*
					 * erzielt der zuletzt als am meisten einsparende Korridor
					 * auch noch eine relative Einsparung wie oben festgelegt,
					 * wird er "aktiviert", d.h. auch in die
					 * angebundenenKorridore der beteiligten Orte geschrieben.
					 */
					if ((besteErsparnis / hoechsterErsparniskorridor
							.getLaenge()) > SCHWELLFAKTOR_QUERVERBINDUNG) {
						hoechsterErsparniskorridor.aktiviere();
						eingerichteteKorridore.add(hoechsterErsparniskorridor);
					} else {
						break;
					}
				}

			} else if (ringOrte.size() == 2) {
				// kein Ring moeglich, es entsteht ein Korridor
				eingerichteteKorridore.add(new Korridor(ringOrte.get(0),
						ringOrte.get(1), Korridor.KENNUNG_ENFC, true));
			}
		} catch (UngueltigerOrt e) {
			JOptionPane
					.showMessageDialog(
							null,
							"Es trat ein ungueltiger Ort bei der Erstellung von Korridoren auf. \n Bitte wenden Sie sich an die Herausgeber des Programmes."
									+ e.getMessage());
			System.exit(0);
		}
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
		int punktY = ort.getKoordY();
		int punktX = ort.getKoordX();
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
	 * Netzerstellung ausgefuehrt.
	 */
	public void erstelleNetz() throws NetzBauFehler {
		if (orte.size() == 1) {
			/*
			 * Wurde ein Ort eingelesen, wird der Benutzer informiert, dass eine
			 * Netzerstellung keinen Sinn macht. Das Programm wird beendet.
			 */
			JOptionPane
					.showMessageDialog(
							null,
							"Es befindet sich nur ein Ort in Ihrer Kartendatei! /n Eine Netzplanung ist nicht sinnvoll./n Das Programm wird beendet.");
			System.exit(0);
		}

		/*
		 * Wenn ausschliesslich Auslandsverbindungen eingelesen wurden, wird ein
		 * Stern aus Sicherheitskorridoren erstellt.
		 */
		boolean nur_ASL_Vorhanden = true;
		for (Ort ort : orte) {
			if (!ort.getKennung().equals(Ort.KENNUNG_AUSLANDSVERBINDUNG)) {
				nur_ASL_Vorhanden = false;
			}
		}
		if (nur_ASL_Vorhanden) {
			erstelleSternSICH(orte);
			pruefeNetzBau(eingerichteteKorridore);
			return;
		}

		// Ablauf bei "gewoehnlicher" Karte.
		verbindeAuslandsorte();
		erstelleRingStruktur(
				ermittleRelevanteKonzentration(BEGINN_FELDABTASTUNG,
						END_FELDABTASTUNG, SCHRITTE_FELDABTASTUNG,
						MIN_ORTE_IM_FELD),
				entferneOrtTyp(Ort.KENNUNG_AUSLANDSVERBINDUNG, orte));
		// Ausbau des Netzes
		netzUpgrade();
		// Pruefung des eingerichteten Netzes
		pruefeNetzBau(eingerichteteKorridore);
	}

	/**
	 * @param korridor
	 * @return Wert eines Korridors aufgrund seiner angebundenen Orte und deren
	 *         Entfernung
	 */
	private double getAbsKorridorRang(Korridor korridor) {
		return korridor.getLaenge() * korridor.getOrtA().getRelevanzGrad()
				* korridor.getOrtB().getRelevanzGrad();
	}

	/**
	 * Solange das Budget nicht ueberschritten wurde, versucht netzUpgrade,
	 * wichtige Korridore in ihren Nutzungskosten zu verbessern.
	 * 
	 * @author bruecknerr
	 */
	private void netzUpgrade() {
		ArrayList<Korridor> nochUpgradebareK = new ArrayList<Korridor>();
		for (Korridor k : eingerichteteKorridore) {
			if (isUpgradeable(k)) {
				// nicht mehr upgradebare Korridore ausschliessen
				nochUpgradebareK.add(k);
			}
		}

		while (ermittleGesamteBaukosten() < budget) {

			if (!nochUpgradebareK.isEmpty()) {
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
	 * 
	 * @param relevanzFelder
	 * @param ohneASLOrte
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
			for (double abwertFaktor = 1; abwertFaktor >= 0.1; abwertFaktor = (abwertFaktor - SCHRITTWEITE_RING_ABWERTFAKTOR)) {

				for (Ort ort : ohneASLOrte) {
					if (relevanzGradOrtmitASL(ort) >= (hoechsterRG * abwertFaktor)
							&& !ringOrte.contains(ort)) {
						ringOrte.add(ort);
					}
					if (abwertFaktor < SCHWELLWERT_ABBRUCH_RINGORTE
							|| ringOrte.size() == orte.size()) {
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
		if (!relevanzFelder.isEmpty()) {
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
			ringOrte.addAll(hinzuzufuegendeOrte);
		}

		// Erstellen des Ringes.
		erstelleTrigonRing(ringOrte);
		// Hier erstellung der Liste verbleibenderOrte.
		try {
			ArrayList<Ort> verbleibendeOrte = new ArrayList<Ort>();
			verbleibendeOrte = ohneASLOrte;
			verbleibendeOrte.removeAll(ringOrte);
			for (Ort ortA : verbleibendeOrte) {
				eingerichteteKorridore.add(new Korridor(ortA,
						findeDichtestenOrtzuDiesem(ortA, ringOrte),
						Korridor.KENNUNG_ENFC, true));
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
							Korridor.KENNUNG_SICH, true));
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
			if (!ort.getKennung().equals(kennung)) {
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
					(bezugsOrt.getKoordX() - ort.getKoordX()), 2)
					+ Math.pow((bezugsOrt.getKoordY() - ort.getKoordY()), 2));
			if (distanzOrt < distanz) {
				distanz = distanzOrt;
				dichtesterOrt = ort;
			}
		}
		return dichtesterOrt;
	}

	/**
	 * Verbinden aller ASL Orte mit dem naechsten nicht ASL Ort.
	 * 
	 * @author Nils
	 */
	private void verbindeAuslandsorte() {
		try {
			// Erstellen einer Liste aller ASL Orte, die sich auf der Karte
			// befinden
			ArrayList<Ort> aslOrte = new ArrayList<Ort>();
			Ort naechsterOrt = null;
			for (Ort ortA : orte) {
				if (ortA.getKennung().equals(Ort.KENNUNG_AUSLANDSVERBINDUNG)) {
					aslOrte.add(ortA);
				}
			}
			// Durchgehen der ASL Orte
			for (Ort ortASL : aslOrte) {
				// Finden des dichtesten Nicht ASL Ortes zu diesem Ort
				double minDistanz = Double.MAX_VALUE;
				// Durchlauf durch alle Orte der Karte.
				for (Ort nichtASL_Ort : orte) {
					// Check, ob der Ort auch kein ASL Ort ist
					if (nichtASL_Ort.getKennung().equals(
							Ort.KENNUNG_AUSLANDSVERBINDUNG)) {
						continue;
					}
					double distanzEinOrt = ermittleOrtsdistanz(ortASL,
							nichtASL_Ort);
					if (distanzEinOrt < minDistanz) {
						minDistanz = distanzEinOrt;
						naechsterOrt = nichtASL_Ort;
					}
				}
				// Bau eines Korridores zwischen dem aktuellen ASL Ort und
				// seinem dichtesten Partner.
				eingerichteteKorridore.add(new Korridor(ortASL, naechsterOrt,
						Korridor.KENNUNG_SICH, true));
			}
		} catch (UngueltigerOrt e) {
		}
	}

	/**
	 * @param k
	 *            zu Korridor
	 * 
	 */
	private boolean isUpgradeable(Korridor k) {
		String neueKorridorart = k.getNextKennung();
		if (neueKorridorart != "")
			return istEinrichtbarerKorridor(k.getOrtA(), k.getOrtB(),
					neueKorridorart);
		return false;
	}

	/**
	 * @param ortA
	 * @param ortB
	 * @param korridorKennung
	 *            des einzurichtenden Korridors
	 * @return Einrichtbarkeit als boolschen Wert.
	 * 
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
		double laengeQuadrat = Math.pow(
				orteins.getKoordX() - ortzwei.getKoordX(), 2)
				+ Math.pow(orteins.getKoordY() - ortzwei.getKoordY(), 2);
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
					&& !(hoechster.getKennung().equals("ASL"))) {
				hoechster = liste.get(i);
			}
		}
		for (int i = 0; i < liste.size(); i++) {
			if (hoechster.getRelevanzGrad() == liste.get(i).getRelevanzGrad()
					&& hoechster != liste.get(i)) {

				Ort NullOrt = new Ort(Math.round(KARTE_GROESSE_X / 2),
						Math.round(KARTE_GROESSE_Y / 2), "NullOrt");

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
							(xKoordinate - ort.getKoordX()), 2)
							+ Math.pow((yKoordinate - ort.getKoordY()), 2));
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
		double gesamtRelevanzGrad = gewaehlterOrt.getRelevanzGrad();
		for (Korridor korridor : gewaehlterOrt.getAngebundeneKorridore()) {
			Ort ort = korridor.bestimmeAnderenOrt(gewaehlterOrt);
			if (ort.getKennung() == Ort.KENNUNG_AUSLANDSVERBINDUNG) {
				gesamtRelevanzGrad += ort.getRelevanzGrad();
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
				boolean nichtASLvorhanden = false;
				if (neuesFeld.bestimmeOrteImFeld().size() >= MIN_ORTE_IM_FELD)
					for (Ort ort : neuesFeld.bestimmeOrteImFeld()) {
						if (ort.getKennung() != Ort.KENNUNG_AUSLANDSVERBINDUNG)
							nichtASLvorhanden = true;
					}
				if (nichtASLvorhanden)
					felderListe.add(neuesFeld);
			}
		}

		/*
		 * loesche alle Felder aus der Liste, die sich mit dem aktuell am
		 * dichtesten besiedelten Feld Orte teilen
		 */
		while (!felderListe.isEmpty()) {
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

	/**
	 * Diese Methode prueft den abschliessenden Netzbau. Bei korrekter
	 * Programmierung sollte kein Fehler auftauchen
	 * 
	 * @param aktuellesNetz
	 *            Netz, dass ueberpreuft werden soll
	 * @throws NetzBauFehler
	 *             spezifischer Fehler bei nicht korrketer einrichtung
	 */
	private void pruefeNetzBau(ArrayList<Korridor> aktuellesNetz)
			throws NetzBauFehler {
		// Check, ob nur endgueltige Korridore eingerichtet wurden
		for (Korridor k : eingerichteteKorridore) {
			if (!k.isEndgueltig()) {
				throw new NetzBauFehler(k, 0);
			}
		}
		// Check, ob in angebundenKorridoren von Orten nur endgueltige Korridore
		// stehen.
		for (Ort ortA : orte) {
			for (Korridor k : ortA.getAngebundeneKorridore()) {
				if (!eingerichteteKorridore.contains(k)) {
					throw new NetzBauFehler(k, 1);
				}
			}
		}
	}
}
