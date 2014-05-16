package dateihandler;

import java.util.ArrayList;

import Main.Karte;

public class NetzdateiHandler extends Datei {
	/* {author=TolleN} */

	public static final String DATEI_BEGINN_MARKER = ">->net";

	public static final String DATEI_ENDE_MARKER = "<-<net";

	public static final String DATENSATZ_BEGINN_MARKER = ">->connection";

	public static final String DATENSATZ_ENDE_MARKER = "<-<connection";

	public static final String DATEI_SUFFIX = "_net";

	public static ArrayList<String> GUELTIGE_BEZEICHNER;

	/**
	 * start
	 */
	public static final String BEZEICHNER_START = "start";

	/**
	 * end
	 */
	public static final String BEZEICHNER_ZIEL = "end";

	/**
	 * kind
	 */
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

	public String erstelleOutputStream() {
		return null;
	}

	public String erstelleDateiName() {
		return null;
	}

}