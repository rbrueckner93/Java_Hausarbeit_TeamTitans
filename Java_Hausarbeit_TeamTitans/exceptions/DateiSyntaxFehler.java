package exceptions;

public class DateiSyntaxFehler extends Exception{
	
	int fehlercode;
	
	int zeile;
	/**
	 * Exception die einen fehler im Dateiformat anzeigt.
	 * @author Nils
	 */
	public DateiSyntaxFehler() {
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
