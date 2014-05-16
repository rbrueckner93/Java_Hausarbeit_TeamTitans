package orte;

import java.util.ArrayList;

import korridore.Korridor;

public class Ort {
	/* {author=TolleN, HandritschkP} */

	public int koordX;

	public int koordY;

	public String name;

	public Ort(int koordX, int koordY, String name) {
		super();
		this.koordX = koordX;
		this.koordY = koordY;
		this.name = name;
	}

	/**
	 * ein erstellter Korridor wird in den beiden beteiligten Orten in ihrer
	 * Liste angebundeneKorridore referenziert. in dieser Liste befinden sich
	 * also alle an den ort angebundenen korridore, sobald diese erstellt sind (
	 * sie sind referenziert, nicht mit new Korridor() definiert!
	 */
	public ArrayList<Korridor> angebundeneKorridore;

}