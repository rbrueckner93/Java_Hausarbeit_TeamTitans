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
		ArrayList<Flugroute> flugroutenInArbeit = new ArrayList<Flugroute>();
		ArrayList<Flugroute> moeglicheFlugrouten = new ArrayList<Flugroute>();
		ArrayList<Double> kostenVergleich = new ArrayList<Double>();
		for (Korridor verbindung : herkunft.angebundeneKorridore) {
			flugroutenInArbeit.add(new Flugroute(herkunft, bestimmeAnderenOrt(
					verbindung, herkunft), 0));
		}
		// solange in der "In Arbeit"-Liste noch Elt. stehen:
		while (flugroutenInArbeit.size() > 0) {
			for (Flugroute weg : flugroutenInArbeit) {
				//TODO implement Algorithm
			}
		}
		/**
		 * Nimm die g¸nstigste und gib sie zur¸ck.
		 */
		while (moeglicheFlugrouten.size() > 1) {
			for (int i = 0; i < moeglicheFlugrouten.size(); i++) {
				if (moeglicheFlugrouten.get(i).ermittleRoutennutzkosten() < moeglicheFlugrouten
						.get(i + 1).ermittleRoutennutzkosten()) {
					moeglicheFlugrouten.remove(i + 1);
				}
			}
		}
		// ‰ndere die ReiseListe des aktuellen Flugroutenobjekts
		reiseListe = moeglicheFlugrouten.get(0).reiseListe;

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
	 * gibt eine ArrayList von Orten zur¸ck, die die Reihenfolge von Reise-Hops
	 * ausgibt. aus der ArrayList<Korridor> mit ortA und ortB muss also nach dem
	 * Ort des ersten Korridors gesucht werden, der herkunft entspricht. im
	 * zweiten korridor wird festgestellt, welcher ort nicht dem zweiten ort des
	 * ersten korridors entspricht usw., sodass der letzte ort in der reiseliste
	 * ziel entspricht. eine ueberpruefung muss natuerlich stattfinden. sie ist
	 * eine direkte ableitung aus reiseListe ArrayList<Korridor>
	 * 
	 * ungetestet! mit Vorsicht zu genieﬂen!
	 */
	public ArrayList<Ort> erzeugeOrtsListe() {
		ArrayList<Ort> ortsListe = new ArrayList<Ort>();
		ortsListe.add(herkunft);
		for (Korridor verbindung : reiseListe) {
			if (verbindung.ortA.equals(ortsListe.get(ortsListe.size()-1))) {
				ortsListe.add(verbindung.ortB);
			} else if (verbindung.ortB
					.equals(ortsListe.get(ortsListe.size() - 1))) {
				ortsListe.add(verbindung.ortA);
			}
		}

		return ortsListe;
	}
}