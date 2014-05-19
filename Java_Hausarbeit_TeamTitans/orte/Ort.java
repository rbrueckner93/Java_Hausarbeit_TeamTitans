package orte;

import java.util.ArrayList;

import korridore.Korridor;

public class Ort {
	/* {author=TolleN, HandritschkP} */

	public int koordX;

	public int koordY;

	public String name;

	public String kennung;
	
	public double relevanzGrad;

	public Ort(int koordX, int koordY, String name) {
		super();
		this.koordX = koordX;
		this.koordY = koordY;
		this.name = name;
		angebundeneKorridore = new ArrayList<Korridor>();
	}

	
	public double getRelevanzGrad() {
		return relevanzGrad;
	}


	public void setRelevanzGrad(double relevanzGrad) {
		this.relevanzGrad = relevanzGrad;
	}


	public String getKennung() {
		return kennung;
	}


	public ArrayList<Korridor> getAngebundeneKorridore() {
		return angebundeneKorridore;
	}


	/**
	 * ein erstellter Korridor wird in den beiden beteiligten Orten in ihrer
	 * Liste angebundeneKorridore referenziert. in dieser Liste befinden sich
	 * also alle an den ort angebundenen korridore, sobald diese erstellt sind (
	 * sie sind referenziert, nicht mit new Korridor() definiert!
	 */
	public ArrayList<Korridor> angebundeneKorridore;

}