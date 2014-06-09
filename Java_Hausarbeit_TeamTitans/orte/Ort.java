package orte;

import java.util.ArrayList;

import netz.Korridor;

public class Ort {
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
	 * Ein erstellter Korridor wird in den beiden beteiligten Orten in ihrer
	 * Liste angebundeneKorridore referenziert. In dieser Liste befinden sich
	 * Verweise auf alle an den Ort angebundenen Korridore, sofern diese mit
	 * endgueltig = true erstellt oder im Nachhinein aktiviert() wurden.
	 */
	private ArrayList<Korridor> angebundeneKorridore;

	/**
	 * Die Klasse Ort stellt in erster Linie eine Datenhaltung in der
	 * Programmstruktur dar und dient mit Bereitstellung von Ortskennungen,
	 * Koordinaten und angebundenenKorridoren.
	 * 
	 * @param koordX
	 * @param koordY
	 * @param name
	 * @author TolleN, HandritschkP
	 */
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

	public int getKoordY() {
		return koordY;
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

	public void addAngebundenenKorridor(Korridor korridor) {
		this.angebundeneKorridore.add(korridor);
	}
}
