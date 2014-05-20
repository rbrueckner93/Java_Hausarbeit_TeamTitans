package main;

import java.util.ArrayList;
import korridore.Korridor;
import orte.Ort;

/**
 * @author BruecknerR ... wird erst bei Nutzung=Testdateieinlesen erstellt mit
 *         Start, Ziel, Faktor. Sucht sich nach Aufforderung durch
 *         ermittleBesteRoute (nicht schon bei Initialisierung) ihren eigenen
 *         optimalen Weg, und schreibt diesen in der reiseListe fest und stellt
 *         die Nutzkosten in ermittleRoutennutzkosten fest.
 */
public class Flugroute {
	public Ort ziel;
	public Ort herkunft;
	// ArrayList mit Flurkorridoren, die mit einer Flugroute genutzt werden.
	public ArrayList<Korridor> reiseListe;
	// Haeufigkeit der Fluege auf Route
	public int faktor;

	/**
	 * Konstruktor der Klasse Flugroute. Parameter werden von TestdateiHanlder
	 * uebergeben.
	 * 
	 * @param ziel
	 * @param herkunft
	 * @param faktor
	 */
	public Flugroute(Ort ziel, Ort herkunft, int faktor) {
		super();
		this.ziel = ziel;
		this.herkunft = herkunft;
		this.faktor = faktor;
		reiseListe = new ArrayList<Korridor>();
	}

	/**
	 * besteRoute wird auf einer Flugroute ausgefuehrt. sie dient dazu, den
	 * besten Weg von A nach B zu finden, wobei lediglich die entstehenden
	 * Nutzungskosten zugrunde gelegt werden.
	 * 
	 * anhand unterschiedlicher Korridorkombinationen ermitteln
	 */
	public Ort bestimmeAnderenOrt(Korridor verbindung, Ort bekanntesEnde) {
		Ort ermittelterOrt;
		if (verbindung.ortA.equals(bekanntesEnde)) {
			ermittelterOrt = verbindung.ortB;
		}
		if (verbindung.ortB.equals(bekanntesEnde)) {
			ermittelterOrt = verbindung.ortA;
		} else {
			ermittelterOrt = null;
		}
		// hier vielleicht auch ne Exception oder sowas?

		return ermittelterOrt;
	}

	public void ermittleBesteRoute() {
		/**
		 * flugroutenInArbeit speichert nur solange Elemente, wie sie nicht am
		 * Ende der zu optimierenden Flugroute angekommen sind.
		 */
		ArrayList<Flugroute> flugroutenInArbeit = new ArrayList<Flugroute>();
		/**
		 * moeglicheFlugrouten speichert diejenigen Routen ab, über die ohne
		 * doppelt angeflogene Orte das ziel erreicht werden kann
		 */
		ArrayList<Flugroute> moeglicheFlugrouten = new ArrayList<Flugroute>();

		// von Start ausgehend alle angebundenen Korridore als
		// flugroutenInArbeit anlegen.
		for (int i = 0; i < herkunft.angebundeneKorridore.size(); i++) {
			//ueberpruefen, ob mit dem ersten Hop bereits das Ziel erreicht wurde.
			if (bestimmeAnderenOrt(herkunft.angebundeneKorridore.get(i),
					herkunft).equals(ziel)) {
				Flugroute neueFlugroute = new Flugroute(
						herkunft,
						bestimmeAnderenOrt(
								herkunft.angebundeneKorridore.get(i), herkunft),
						faktor);
				neueFlugroute.reiseListe.add(herkunft.angebundeneKorridore
						.get(i));
				moeglicheFlugrouten.add(neueFlugroute);
			} else {
				Flugroute neueFlugroute = new Flugroute(
						herkunft,
						bestimmeAnderenOrt(
								herkunft.angebundeneKorridore.get(i), herkunft),
						faktor);
				neueFlugroute.reiseListe.add(herkunft.angebundeneKorridore
						.get(i));
				flugroutenInArbeit.add(neueFlugroute);
			}
		}
		// solange in der "In Arbeit"-Liste noch Flugrouten stehen:

		if (flugroutenInArbeit.size() > 0) {
			System.out.println("Es ist mindestens eine in Arbeit.");
			for (int routen = 0; routen < flugroutenInArbeit.size(); routen++) {
				Flugroute weg = flugroutenInArbeit.get(routen);
				for (Korridor verbindung : weg.ziel.angebundeneKorridore) {
					/**
					 * wenn ermittleAnderenOrt(verbindung, weg.ziel) nicht in
					 * weg.erzeugeOrtsListe vorkommt und
					 * ermittleAnderenOrt(verbindung, weg.ziel) nicht gleich
					 * ziel ist, dann schreibe in flugroutenInArbeit.add()
					 */
					Ort neuesZiel = bestimmeAnderenOrt(verbindung, weg.ziel);
					if (weg.erzeugeOrtsListe().size() > 0) {
						if (kommtOrtInOrtslisteVor(neuesZiel,
								weg.erzeugeOrtsListe())) {

							if (!neuesZiel.equals(ziel)) {
								flugroutenInArbeit.add(new Flugroute(neuesZiel,
										herkunft, faktor));
							}
							if (neuesZiel.equals(ziel)) {
								moeglicheFlugrouten.add(new Flugroute(ziel,
										herkunft, faktor));
							}
						}
					} else {

						if (neuesZiel != ziel) {
							Flugroute andereNeueFlugroute = new Flugroute(
									neuesZiel, herkunft, faktor);
							andereNeueFlugroute.reiseListe.add(verbindung);
							flugroutenInArbeit.add(andereNeueFlugroute);
							System.out.println("routeInArbeit hinzugefuegt.");
						}
						if (neuesZiel == ziel) {
							Flugroute dritteNeueFlugroute = new Flugroute(ziel,
									herkunft, faktor);
							dritteNeueFlugroute.reiseListe.add(verbindung);
							moeglicheFlugrouten.add(dritteNeueFlugroute);
							System.out.println("moeglicheRoute hinzugefuegt.");
						}
					}
					/**
					 * wenn ermittleAnderenOrt(verbindung, weg.ziel nicht in
					 * weg.erzeugeOrtsListe vorkommt und
					 * ermittleAnderenOrt(verbindung, weg.ziel) gleich ziel ist,
					 * wird er nirgendwo hingeschrieben.
					 * 
					 * alle flugrouten, die nun uebrig sind (d.h. nicht um ein
					 * weiteren Hop bzw. das Ziel erweitert wurden, werden in
					 * eine ArrayList geschrieben, die am Ende gelöscht wird.
					 */
					//

				}
				flugroutenInArbeit.remove(routen);

			}
		}

		/**
		 * Nimm die günstigste und gib sie zurück.
		 */
		while (moeglicheFlugrouten.size() > 1) {
			for (int i = 0; i < moeglicheFlugrouten.size(); i++) {
				if (moeglicheFlugrouten.get(i).ermittleRoutennutzkosten() < moeglicheFlugrouten
						.get(i + 1).ermittleRoutennutzkosten()) {
					moeglicheFlugrouten.remove(i + 1);
				}

			}
		}
		if (moeglicheFlugrouten.size() > 0) {
			System.out.println("Ich schreibe gleich in die Reiseliste!");
			reiseListe = moeglicheFlugrouten.get(0).reiseListe;
			System.out.println(reiseListe);
		}
	}

	/**
	 * Multipliziert Nutzungshaeufigkeit einer Route mit den Nutzungskosten der
	 * beteiligten Korridore. gibt ein double-wert zurueck.
	 * 
	 * Die einmaligen Nutzungskosten muessen nicht separat ermittelt werden, da
	 * sowohl bei der Wahl der Korridore als auch der Ermittlung der
	 * Gesamtkosten nur die Gesamtkosten der Nutzung einer Route.
	 * 
	 * dabei werden korridor.ermittleLaenge(); und korridor.NUTZUNGSKOSTEN
	 * herangezogen.
	 */
	public double ermittleRoutennutzkosten() {
		double kosten = 0.0;
		for (Korridor relation : reiseListe) {
			kosten += relation.getNutzungskosten() * faktor;
		}
		return kosten;
	}

	/**
	 * @author BruecknerR
	 * @return String mit Semikolon-getrennten Ortsnamen @
	 */
	public String erzeugeTextausgabeReiseroute() {
		String ausgabe = "";
		for (Ort ort : erzeugeOrtsListe()) {
			ausgabe.concat(ort.name);
			ausgabe.concat(";");
		}
		return ausgabe;
	}

	/**
	 * gibt eine ArrayList von Orten zurück, die die Reihenfolge von Reise-Hops
	 * ausgibt. aus der ArrayList<Korridor> mit ortA und ortB muss also nach dem
	 * Ort des ersten Korridors gesucht werden, der herkunft entspricht. im
	 * zweiten korridor wird festgestellt, welcher ort nicht dem zweiten ort des
	 * ersten korridors entspricht usw., sodass der letzte ort in der reiseliste
	 * ziel entspricht. eine ueberpruefung muss natuerlich stattfinden. sie ist
	 * eine direkte ableitung aus reiseListe ArrayList<Korridor>
	 * 
	 * ungetestet! mit Vorsicht zu genießen!
	 */
	public ArrayList<Ort> erzeugeOrtsListe() {
		ArrayList<Ort> ortsListe = new ArrayList<Ort>();
		ortsListe.add(herkunft);
		// try {
		// if (reiseListe.size() > 0) {
		// for (Korridor verbindung : reiseListe) {
		// if (verbindung.ortA.equals(ortsListe.get(ortsListe.size()))) {
		// ortsListe.add(verbindung.ortB);
		// } else if (verbindung.ortB.equals(ortsListe.get(ortsListe
		// .size()))) {
		// ortsListe.add(verbindung.ortA);
		// }
		// }
		// }
		// } catch (NullPointerException e) {
		// System.out.println("noch keine Korridore in ReiseListe");
		// }
		return ortsListe;
	}

	/**
	 * Ueberprueft eine ArrayList von Orten auf vorkommen eines bestimmten
	 * Ortes.
	 * 
	 * @param ort
	 *            : Ein beliebiger Ort
	 * @param ortsliste
	 *            : Eine Liste mit Orten
	 * @return true: ort kommt in der ortsliste vor.
	 * @author: BruecknerR
	 */
	public boolean kommtOrtInOrtslisteVor(Ort ort, ArrayList<Ort> ortsliste) {
		for (Ort testort : ortsliste) {
			if (testort.equals(ort)) {
				return true;
			}
		}
		return false;
	}
}
