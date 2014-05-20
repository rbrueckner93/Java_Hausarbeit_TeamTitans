package dateihandler;

import java.util.ArrayList;

import korridore.Korridor;
import main.Karte;

/**
 * 
 * @author TolleN
 * Beschreibung
 *
 */

public class NetzdateiHandler extends Datei {
	public static final String DATEI_BEGINN_MARKER = ">->net";
	public static final String DATEI_ENDE_MARKER = "<-<net";
	public static final String DATENSATZ_BEGINN_MARKER = ">->connection";
	public static final String DATENSATZ_ENDE_MARKER = "<-<connection";
	public static final String DATEI_SUFFIX = "_net";
	public static ArrayList<String> GUELTIGE_BEZEICHNER;
	public static final String BEZEICHNER_START = "start";
	public static final String BEZEICHNER_ZIEL = "end";
	public static final String BEZEICHNER_KORRIDOR_KENNUNG = "kind";
	public Karte fertigesNetz;
	/**
	 * Konstruktor. Muss ein Objekt des Types Karte uebergeben bekommen.
	 * @param fertigesNetz
	 */
	public NetzdateiHandler(Karte fertigesNetz) {
		super();
		this.fertigesNetz = fertigesNetz;
	}

	public void schreibeNetzdatei(){
		Datei.schreibeDatei(erstelleOutputStream(), erstelleDateiName());
	}
	
	
	public ArrayList<String> erstelleOutputStream() {
		ArrayList<String> fertigerText = new ArrayList<String>();
		fertigerText.add(DATEI_BEGINN_MARKER);
		fertigerText.add("");
		for (Korridor aktuellerKorridor : fertigesNetz.eingerichteteKorridore){
			fertigerText.add(DATENSATZ_BEGINN_MARKER);
			fertigerText.add(MERKMAL_BEGINN+BEZEICHNER_START+BEZEICHNER_WERT_TRENNER+aktuellerKorridor.getOrtA().name+MERKMAL_ENDE);
			fertigerText.add(MERKMAL_BEGINN+BEZEICHNER_ZIEL+BEZEICHNER_WERT_TRENNER+aktuellerKorridor.getOrtB().name+MERKMAL_ENDE);
			fertigerText.add(MERKMAL_BEGINN+BEZEICHNER_KORRIDOR_KENNUNG+BEZEICHNER_WERT_TRENNER+aktuellerKorridor.getKennung()+MERKMAL_ENDE);
			fertigerText.add(DATENSATZ_ENDE_MARKER);
		}
		fertigerText.add("");
		fertigerText.add(DATEI_ENDE_MARKER);
		return fertigerText;
	}

	public String erstelleDateiName() {
			String dateiName = fertigesNetz.getNameKartendateiHandler()+DATEI_SUFFIX;
		return dateiName;
	}

}