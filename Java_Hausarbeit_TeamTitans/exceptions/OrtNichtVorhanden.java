package exceptions;

/**
 * @author BruecknerR
 * 
 * Fehler wird geworfen, wenn eine Ortsliste aus nicht zusammenhaengenden
 * Korridoren einer ReiseListe erzeugt werden soll.
*/

public class OrtNichtVorhanden extends Exception {

	public OrtNichtVorhanden() {
		super("ReiseListe_nicht_verbunden");

	}

}
