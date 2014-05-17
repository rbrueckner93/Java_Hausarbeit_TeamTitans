package Main;

import java.util.ArrayList;

import korridore.Korridor;
import orte.Ort;

/**
 * ... wird erst bei Nutzung erstellt mit Start, Ziel, Faktor. Sucht sich nach
 * Aufforderung durch ermittleBesteRoute (nicht schon bei Initialisierung) ihren
 * eigenen optimalen Weg, und schreibt diesen in der reiseListe fest und stellt
 * die Nutzkosten in ermittleRoutennutzkosten fest.
 */
public class Flugroute {
	/* {author=BruecknerR} */

	public Ort ziel;
	public Ort herkunft;

	/**
	 * ArrayList mit Flurkorridoren, die mit einer Flugroute genutzt werden.
	 */
	public ArrayList<Korridor> reiseListe;

	/**
	 * Angabe, welche Intensitaet der Nutzung vermutet wird. (Haeufigkeit der
	 * Fluege)
	 * 
	 */
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

		for (Korridor verbindung : herkunft.angebundeneKorridore) {
			flugroutenInArbeit.add(new Flugroute(herkunft, bestimmeAnderenOrt(
					verbindung, herkunft), faktor));
		}
		// solange in der "In Arbeit"-Liste noch Flugrouten stehen:
		while (flugroutenInArbeit.size() > 0) {
			for (Flugroute weg : flugroutenInArbeit) {
				for (Korridor verbindung : weg.ziel.angebundeneKorridore) {
					/**
					 * wenn ermittleAnderenOrt(verbindung, weg.ziel) nicht in
					 * weg.erzeugeOrtsListe vorkommt und
					 * ermittleAnderenOrt(verbindung, weg.ziel) nicht gleich
					 * ziel ist, dann schreibe in flugroutenInArbeit.add()
					 */
					Ort neuesZiel = bestimmeAnderenOrt(verbindung, weg.ziel);
					boolean bereitsDagewesen = (kommtOrtInOrtslisteVor(
							neuesZiel, weg.erzeugeOrtsListe()));
					if ((bereitsDagewesen == false) && (neuesZiel != ziel)) {
						flugroutenInArbeit.add(new Flugroute(neuesZiel,
								herkunft, faktor));
					}
					if ((bereitsDagewesen == false) && (neuesZiel == ziel)) {
						moeglicheFlugrouten.add(new Flugroute(ziel, herkunft,
								faktor));
					}
					/**
					 * wenn ermittleAnderenOrt(verbindung, weg.ziel nicht in
					 * weg.erzeugeOrtsListe vorkommt und
					 * ermittleAnderenOrt(verbindung, weg.ziel) gleich ziel ist,
					 * dann schreibe in moeglicheFlugrouten
					 */
					flugroutenInArbeit.remove(weg);
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
			// ändere die ReiseListe des aktuellen Flugroutenobjekts
			reiseListe = moeglicheFlugrouten.get(0).reiseListe;
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
		for (Korridor verbindung : reiseListe) {
			if (verbindung.ortA.equals(ortsListe.get(ortsListe.size() - 1))) {
				ortsListe.add(verbindung.ortB);
			} else if (verbindung.ortB
					.equals(ortsListe.get(ortsListe.size() - 1))) {
				ortsListe.add(verbindung.ortA);
			}
		}

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
