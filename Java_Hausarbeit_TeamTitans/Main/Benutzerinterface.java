package Main;
import java.io.File;

/** 
 *  lukas erzeugt in einigen Methoden Instanzen von JFileChooser,
 *  auf dieser waehlt er eine Datei aus, faengt exceptions und gibt ein objekt vom Typ File (frage nach kartendatei, frage nach Testdatei ) zurueck.
 */
public class Benutzerinterface {
  /* {author=FechnerL}*/


  /** 
   *  durch system.getproperties("User.Home") am Beginn des Programmes gesetzt.
   */
  public static final String standardpfad = System.getProperty("user.home");

  /** 
   *  Einlesen und ueberpruefen des Budgets:
   *  ganzzahlig und positiv
   */
  public int abfrageBudget() {
  return 0;
  }

  /** 
   *  fragt nach Testdatei, faengt alle Exceptions, 
   */
  public File frageNachTestdatei() {
  return null;
  }

  public String zeigeNutzkosten() {
  return null;
  }

  public String zeigeBaukosten() {
  return null;
  }

  public Benutzerinterface() {
  }

  /** 
   *  1) Abspeichern und schliessen
   *  2) Simulation speichern und Karte mit anderen Testparametern simulieren
   *  3) Programmabbruch
   */
  public boolean frageNachEndoption() {
  return false;
  }

  /** 
   *  soll alle Exceptions abfangen,
   *  falls keine Datei uebergeben wird, sich selbst neu aufrufen,
   *  falls abgebrochen wird, system.exit(0);
   */
  public File frageNachKartendatei() {
  return null;
  }

}