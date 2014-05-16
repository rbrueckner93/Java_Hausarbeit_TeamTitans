package Exceptions;

/*Diese Exceptions soll bei Orten geworfen werden, 
wenn ein unerlaubter Ort bei einem Korridor genutzt wird.
*/

public class UngueltigerOrt extends Exception {

	public UngueltigerOrt() {
		super("FalscherOrt_benutzt");
		// TODO Auto-generated constructor stub
	}
	
}
