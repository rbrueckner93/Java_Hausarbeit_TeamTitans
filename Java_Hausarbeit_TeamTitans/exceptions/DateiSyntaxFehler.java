package exceptions;

public class DateiSyntaxFehler extends Exception{
	
	int fehlercode;
	
	int zeile;

	public DateiSyntaxFehler() {
//		int fehlercode, int zeile
//		super();
//		this.fehlercode = fehlercode;
//		this.zeile = zeile;
	}

	public void zeigeFehlernachricht(){
	}
}
