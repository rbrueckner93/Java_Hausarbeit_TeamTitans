package orte;

import java.util.ArrayList;

import netz.Korridor;

public class Ort {
	/* {author=TolleN, HandritschkP} */
	
	public static final String KENNUNG_HAUPTORT = "HPT";
	public static final String KENNUNG_NEBENORT = "NBN";
	public static final String KENNUNG_UMSCHLAGPUNKT = "UMS";
	public static final String KENNUNG_AUSLANDSVERBINDUNG = "ASL";

	private int koordX;

	private int koordY;

	private String name;

	private String kennung;
	
	private double relevanzGrad;
	
	/**
	 * ein erstellter Korridor wird in den beiden beteiligten Orten in ihrer
	 * Liste angebundeneKorridore referenziert. in dieser Liste befinden sich
	 * alle an den Ort angebundenen Korridore, sobald diese erstellt sind (
	 * sie sind referenziert, nicht mit new Korridor() definiert!
	 */
	
	private ArrayList<Korridor> angebundeneKorridore;

	public Ort(int koordX, int koordY, String name) {
		super();
		this.koordX = koordX;
		this.koordY = koordY;
		this.name = name;
		angebundeneKorridore = new ArrayList<Korridor>();
	}

	public int getKoordX() {
		return koordX;
	}

	public void setKoordX(int koordX) {
		this.koordX = koordX;
	}

	public int getKoordY() {
		return koordY;
	}

	public void setKoordY(int koordY) {
		this.koordY = koordY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKennung() {
		return kennung;
	}

	public void setKennung(String kennung) {
		this.kennung = kennung;
	}

	public double getRelevanzGrad() {
		return relevanzGrad;
	}

	public void setRelevanzGrad(double relevanzGrad) {
		this.relevanzGrad = relevanzGrad;
	}

	public ArrayList<Korridor> getAngebundeneKorridore() {
		return angebundeneKorridore;
	}

	public void setAngebundeneKorridore(ArrayList<Korridor> angebundeneKorridore) {
		this.angebundeneKorridore = angebundeneKorridore;
	}
	


}
