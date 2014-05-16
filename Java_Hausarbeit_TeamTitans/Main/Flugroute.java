package Main;
import java.util.ArrayList;

import korridore.Korridor;
import orte.Ort;

/** 
 *  ... wird erst bei Nutzung erstellt mit Start, Ziel, Faktor.
 *  Sucht sich nach Aufforderung durch ermittleBesteRoute (nicht schon bei Initialisierung) ihren eigenen optimalen Weg, und schreibt diesen in der reiseListe fest und stellt die Nutzkosten in ermittleRoutennutzkosten fest.
 */
public class Flugroute {
  /* {author=BruecknerR}*/


  public Ort ziel;

  public Ort herkunft;

  /** 
   *  ArrayList mit Flurkorridoren, die mit einer Flugroute genutzt werden.
   */
  public ArrayList<Korridor> reiseListe;

  /** 
   *  Angabe, welche Intensitaet der Nutzung vermutet wird. (Haeufigkeit der Fluege)
   *  
   */
  public int faktor;
  
  /**
   * Konstruktor der Klasse Flugroute. Parameter werden von TestdateiHanlder uebergeben.
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
   *  besteRoute anhand unterschiedlicher Korridorkombinationen ermitteln
   */
  public void ermittleBesteRoute() {
  }

  /** 
   *  Multipliziert Nutzungshaeufigkeit einer Route mit den Nutzungskosten der beteiligten Korridore. gibt ein double-wert zurueck.
   *  
   *  Die einmaligen Nutzungskosten muessen nicht separat ermittelt werden, da sowohl bei der Wahl der Korridore als auch der Ermittlung der Gesamtkosten nur die Gesamtkosten der Nutzung einer Route.
   *  
   *  dabei werden korridor.ermittleLaenge(); und korridor.NUTZUNGSKOSTEN herangezogen.
   */
  public double ermittleRoutennutzkosten() {
  return 0.0;
  }

  public String erzeugeTextausgabeReiseroute() {
  return null;
  }

  /** 
   *  gibt eine ArrayList von Orten zurück, die die Reihenfolge von Reise-Hops ausgibt. aus der ArrayList<Korridor> mit ortA und ortB muss also nach dem Ort des ersten Korridors gesucht werden, der herkunft entspricht. im zweiten korridor wird festgestellt, welcher ort nicht dem zweiten ort des ersten korridors entspricht usw., sodass der letzte ort in der reiseliste ziel entspricht. eine ueberpruefung muss natuerlich stattfinden. sie ist eine direkte ableitung aus reiseListe ArrayList<Korridor>
   */
  public ArrayList<Ort> erzeugeOrtsListe() {
  return null;
  }

}