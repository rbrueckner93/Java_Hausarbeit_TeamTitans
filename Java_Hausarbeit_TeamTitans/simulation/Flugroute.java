package simulation;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import netz.Korridor;
import exceptions.OrtNichtVorhanden;
import orte.Ort;

/**
 * Wird erst bei Nutzung, dem Testdateieinlesen erstellt mit Start, Ziel und
 * Faktor. Sucht sich nach Aufforderung durch ermittleBesteRoute (nicht schon
 * bei Initialisierung) ihren eigenen optimalen Weg, und schreibt diesen in der
 * reiseListe fest und stellt die Nutzkosten in ermittleRoutennutzkosten fest.
 * 
 * @author BruecknerR
 */
public class Flugroute {
	private Ort ziel;
	private Ort herkunft;
	// ArrayList mit Flurkorridoren, die mit einer Flugroute genutzt werden.
	private ArrayList<Korridor> reiseListe;
	// Haeufigkeit der Fluege auf Route
	private int faktor;

	/**
	 * Konstruktor der Klasse Flugroute. Parameter werden von TestdateiHanlder
	 * uebergeben.
	 * 
	 * @param ziel Ort des Ziels
	 * @param herkunft Ort der Herkunft
	 * @param faktor Haeufigkeit der Nutzung
	 */
	public Flugroute(Ort ziel, Ort herkunft, int faktor) {
		super();
		this.ziel = ziel;
		this.herkunft = herkunft;
		this.faktor = faktor;
		reiseListe = new ArrayList<Korridor>();
	}

	public Ort getZiel() {
		return ziel;
	}

	public void setZiel(Ort ziel) {
		this.ziel = ziel;
	}

	public Ort getHerkunft() {
		return herkunft;
	}

	public void setHerkunft(Ort herkunft) {
		this.herkunft = herkunft;
	}

	public ArrayList<Korridor> getReiseListe() {
		return reiseListe;
	}

	public void setReiseListe(ArrayList<Korridor> reiseListe) {
		this.reiseListe = reiseListe;
	}

	public int getFaktor() {
		return faktor;
	}

	public void setFaktor(int faktor) {
		this.faktor = faktor;
	}

	/**
	 * besteRoute wird auf einer Flugroute ausgefuehrt. Sie dient dazu, den
	 * besten Weg von A nach B zu finden, wobei lediglich die entstehenden
	 * Nutzungskosten zugrunde gelegt werden.
	 * 
	 * Beste Route anhand unterschiedlicher Korridorkombinationen ermitteln.
	 */

	public void ermittleBesteRoute() {
		/*
		 * flugroutenInArbeit speichert nur solange Elemente, wie sie nicht am
		 * Ende der zu optimierenden Flugroute angekommen sind.
		 */
		ArrayList<Flugroute> flugroutenInArbeit = new ArrayList<Flugroute>();
		/*
		 * moeglicheFlugrouten speichert diejenigen Routen ab, ueber die ohne
		 * doppelt angeflogene Orte das ziel erreicht werden kann
		 */
		ArrayList<Flugroute> moeglicheFlugrouten = new ArrayList<Flugroute>();

		// von Start ausgehend alle angebundenen Korridore als
		// flugroutenInArbeit anlegen.
		for (int i = 0; i < herkunft.getAngebundeneKorridore().size(); i++) {
			// ueberpruefen, ob mit dem ersten Hop bereits das Ziel erreicht
			// wurde, wenn ja als moeglicheFlugroute abspeichern.
			Ort erstesZiel = herkunft.getAngebundeneKorridore().get(i)
					.bestimmeAnderenOrt(herkunft);
			Flugroute ersteRoute = new Flugroute(erstesZiel, herkunft, faktor);
			ersteRoute.reiseListe.add(herkunft.getAngebundeneKorridore().get(i));
			if (erstesZiel == ziel) {
				moeglicheFlugrouten.add(ersteRoute);
			}
			// wenn Ziel mit einem Hop noch nicht erreicht, in
			// flugroutenInArbeit speichern.
			else {
				flugroutenInArbeit.add(ersteRoute);
			}
		}

		// solange in der "In Arbeit"-Liste noch Flugrouten stehen:
		while (flugroutenInArbeit.size() > 0) {
			for (int routen = 0; routen < flugroutenInArbeit.size(); routen++) {
				Flugroute weg = flugroutenInArbeit.get(routen);
				for (int i = 0; i < weg.ziel.getAngebundeneKorridore().size(); i++) {
					Korridor verbindung = weg.ziel.getAngebundeneKorridore().get(i);
					/*
					 * wenn ermittleAnderenOrt(verbindung, weg.ziel) nicht in
					 * weg.erzeugeOrtsListe vorkommt und
					 * ermittleAnderenOrt(verbindung, weg.ziel) nicht gleich
					 * Ziel ist, dann schreibe in flugroutenInArbeit.add()
					 */
					Ort neuesZiel = verbindung.bestimmeAnderenOrt(weg.ziel);
					try {
						if (weg.erzeugeOrtsListe().size() > 0) {
							if (!kommtOrtInOrtslisteVor(neuesZiel,
									weg.erzeugeOrtsListe())) {
								Flugroute neueFlugroute = new Flugroute(
										neuesZiel, herkunft, faktor);
								for (Korridor add : weg.reiseListe) {
									neueFlugroute.reiseListe.add(add);
								}
								neueFlugroute.reiseListe.add(verbindung);
								if (neuesZiel != ziel) {
									flugroutenInArbeit.add(neueFlugroute);
								}
								if (neuesZiel == ziel) {
									moeglicheFlugrouten.add(neueFlugroute);
								}
							}
						}
					} catch (OrtNichtVorhanden e) {
						JOptionPane
								.showMessageDialog(null,
										"Fehler aufgetreten. Programm wird abgebrochen.");
						System.exit(0);
					}

					/*
					 * Wenn ermittleAnderenOrt(verbindung, weg.ziel nicht in
					 * weg.erzeugeOrtsListe vorkommt und
					 * ermittleAnderenOrt(verbindung, weg.ziel) gleich Ziel ist,
					 * wird er nirgendwo hingeschrieben.
					 * 
					 * Alle Flugrouten, die uebrig sind, werden geloescht.
					 * Alsbald wird der zaehler um 1 verringert, um kein Elt. zu
					 * ueberspringen.
					 */
				}
				flugroutenInArbeit.remove(routen);
				routen--;

			}
		}

		/*
		 * Nimm die guenstigste Flugroute und gib sie zurueck.
		 */
		while (moeglicheFlugrouten.size() > 1) {
			for (int i = 0; i < moeglicheFlugrouten.size(); i++) {
				if (moeglicheFlugrouten.get(0).ermittleRoutennutzkosten() < moeglicheFlugrouten
						.get(1).ermittleRoutennutzkosten()) {
					moeglicheFlugrouten.remove(1);
				} else {
					moeglicheFlugrouten.remove(0);
				}
			}

		}

		if (moeglicheFlugrouten.size() == 1) {
			reiseListe = moeglicheFlugrouten.get(0).reiseListe;
		}
	}

	/**
	 * Multipliziert Nutzungshaeufigkeit einer Route mit den Nutzungskosten der
	 * beteiligten Korridore. Gibt ein double-wert zurueck.
	 * 
	 * Die einmaligen Nutzungskosten muessen nicht separat ermittelt werden, da
	 * sowohl bei der Wahl der Korridore als auch der Ermittlung der
	 * Gesamtkosten nur die Gesamtkosten der Nutzung einer Route von Bedeutung
	 * sind. Dabei werden korridor.ermittleLaenge(); und korridor.NutzungskostenProKm
	 * herangezogen.
	 * @return Kosten fuer alle Durchfluege
	 */

	public double ermittleRoutennutzkosten() {
		double kosten = 0.0;
		for (Korridor relation : reiseListe) {
			kosten = kosten
					+ ((relation.getNutzungskostenProKm() * relation.getLaenge()) * faktor);
		}
		return kosten;
	}

	/**
	 * Methode wertet die Reiseliste einer Flugroute aus und schreibt 
	 * die Ortsnamen mit ";" getrennt in eine String
	 * @author BruecknerR
	 * @return String mit Semikolon-getrennten Ortsnamen wird von Dateihandler
	 *         verwendet
	 */
	public String erzeugeTextausgabeReiseroute() {
		String ausgabe = "";
		try {
			for (Ort ort : erzeugeOrtsListe()) {
				ausgabe += ort.getName() + ";";
			}
			ausgabe = ausgabe.substring(0, ausgabe.length() - 1);
		} catch (OrtNichtVorhanden e) {
			JOptionPane.showMessageDialog(null,
					"Achtung, Programm jetzt beendet.");
			System.exit(0);
		}
		return ausgabe;
	}

	/**
	 * Gibt eine ArrayList von Orten zurueck, die die Reihenfolge von Reise-Hops
	 * ausgibt. Aus der ArrayList<Korridor> mit ortA und ortB muss also nach dem
	 * Ort des ersten Korridors gesucht werden, der Herkunft entspricht. Im
	 * zweiten Korridor wird festgestellt, welcher Ort nicht dem zweiten Ort des
	 * ersten Korridors entspricht usw., so dass der letzte Ort in der reiseliste
	 * dem Ziel entspricht. Eine Ueberpruefung muss natuerlich stattfinden. Sie
	 * ist eine direkte Ableitung aus reiseListe ArrayList<Korridor>
	 * @return Liste aller Orte die Durchflogen wurden
	 */
	private ArrayList<Ort> erzeugeOrtsListe() throws OrtNichtVorhanden {
		ArrayList<Ort> ortsListe = new ArrayList<Ort>();
		ortsListe.add(herkunft);
		if (reiseListe.size() > 0) {
			for (Korridor verbindung : reiseListe) {
				if (verbindung.getOrtA() == ortsListe.get(ortsListe.size() - 1)) {
					ortsListe.add(verbindung.getOrtB());
				} else if (verbindung.getOrtB() == ortsListe
						.get(ortsListe.size() - 1)) {
					ortsListe.add(verbindung.getOrtA());
				} else {
					throw new OrtNichtVorhanden();
				}
			}
		}
		return ortsListe;
	}

	/**
	 * Ueberprueft eine ArrayList von Orten auf Vorkommen eines bestimmten
	 * Ortes.
	 * 
	 * @param ort
	 *            : Ein beliebiger Ort
	 * @param ortsliste
	 *            : Eine ArrayList mit Orten
	 * @return true: ort kommt in der ortsliste vor.
	 * @author: BruecknerR
	 */
	private boolean kommtOrtInOrtslisteVor(Ort ort, ArrayList<Ort> ortsliste) {
		for (Ort testort : ortsliste) {
			if (testort == ort) {
				return true;
			}
		}
		return false;
	}
}
