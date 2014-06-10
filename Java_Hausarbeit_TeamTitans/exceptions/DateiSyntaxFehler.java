package exceptions;

public class DateiSyntaxFehler extends Exception{
	
	/**
	 * Exception die einen Fehler im Dateiformat anzeigt.
	 * @author TolleN
	 */
	public DateiSyntaxFehler() {
		super("Fehler im Dateiformat");
	}
}
