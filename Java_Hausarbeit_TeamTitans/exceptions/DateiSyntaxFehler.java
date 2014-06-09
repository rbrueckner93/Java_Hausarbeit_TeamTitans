package exceptions;

public class DateiSyntaxFehler extends Exception{
	
	/**
	 * Exception die einen Fehler im Dateiformat anzeigt.
	 * @author TolleN
	 */
	public DateiSyntaxFehler() {
		super("Fehler im Dateiformat");
		/*
		 * Hier sind Details zur Erweiterung der Exception auskommentiert.
		 * Moeglichkeit zur erzeugung von Fehlermeldungen anhand von Fehlercodes.
		 */
//		int fehlercode, int zeile
//		super();
//		this.fehlercode = fehlercode;
//		this.zeile = zeile;
	}

	//public void zeigeFehlernachricht(){
	//}
}
