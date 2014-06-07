package exceptions;

import orte.Ort;

/*Diese Exceptions soll bei Orten geworfen werden, 
wenn ein unerlaubter Ort bei einem Korridor genutzt wird.
*/



public class UngueltigerOrt extends Exception {
	Ort ortA;
	Ort ortB;
	String korridorArt;
	
	public UngueltigerOrt(Ort ortA, Ort ortB, String korridorArt) {
		super("FalscherOrt_benutzt:"+ortA.getKennung()+", "+ortB.getKennung()+" sollten fuer einen "+korridorArt+" genutzt werden.");
		this.ortA = ortA;
		this.ortB = ortB;
		this.korridorArt = korridorArt;
		// TODO Auto-generated constructor stub
	}
	
}
