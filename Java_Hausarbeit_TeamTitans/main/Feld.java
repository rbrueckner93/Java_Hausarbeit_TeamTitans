package main;

import java.util.ArrayList;
import orte.Ort;

public class Feld {
	private int startX;
	private int endX;
	private int startY;
	private int endY;
	private int kantenlaenge;
	private Karte kartenInstanz;

	public Feld(Karte kartenInstanz, int startX, int startY, int kantenlaenge) {
		this.startX = startX;
		this.startY = startY;
		this.endX = startX + kantenlaenge;
		this.endY = startY + kantenlaenge;
		this.kantenlaenge = kantenlaenge;
		this.kartenInstanz = kartenInstanz;
	}

	/**
	 * Alle Orte in diesem Feld sollen ermittelt werden. Dabei werden ausser
	 * passenden Koordinaten keine Anforderungen an einen Ort gestellt.
	 * 
	 * @author BruecknerR
	 * @return eine ArrayList mit Orten, die sich in diesem Feld befinden
	 */
	public ArrayList<Ort> bestimmeOrteImFeld() {
		ArrayList<Ort> bestimmteOrte = new ArrayList<Ort>();
		for (Ort o : kartenInstanz.getOrte()) {
			/**
			 * liegt der gerade untersuchte Ort innerhalb der spezifizierten
			 * Grenzen?
			 */
			if (o.getKoordX() < endX && o.getKoordX() > startX && o.getKoordY() < endY
					&& o.getKoordY() > startY)
				bestimmteOrte.add(o);
		}
		return bestimmteOrte;
	}

	/**
	 * Fragt jeden Ort ob Relevanzgrad groesser als bislang gefundener Ort mit
	 * hoechstem Relevanzgrad ist.
	 * 
	 * @author bruecknerr
	 * @return Ort mit hoechstem Relevanzgrad im aktuellen Feld
	 * @deprecated
	 */
	public Ort wichtigsterOrtImFeld() {
		Ort wichtigsterOrt = null;
		if (bestimmeOrteImFeld().size()>0) {
			// setze initial wichtigsterOrt als das erste Elt. der Liste
			wichtigsterOrt = bestimmeOrteImFeld().get(0);
			for (Ort ort : bestimmeOrteImFeld()) {
				/*
				 * ueberschreibe jedes Mal, wenn ein Ort mit hoeherem RG
				 * gefunden wurde.
				 */
				if (ort.getRelevanzGrad() > wichtigsterOrt.getRelevanzGrad())
					wichtigsterOrt = ort;
			}
		}
		return wichtigsterOrt;
	}

	/**
	 * @author BruecknerR
	 * @param feldB
	 *            irgendein Feld.
	 * @return true wenn es eine Schnittmenge der enthaltenen Orte gibt.
	 */
	public boolean hatOrtsSchnittmengeMit(Feld feldB) {
		/**
		 * Eine Untersuchung der Orte im Feld entfaellt, wenn ein Objekt mit
		 * sich selbst verglichen wird.
		 */
		if (feldB == this)
			return true;
		/**
		 * Untersuchung jedes Ortes des einen Feldes auf Vorkommen in
		 * feldB.bestimmeOrteImFeld() Bei der ersten gefundenen Uebereinstimmung
		 * wird ein true zurueckgegeben.
		 */
		for (Ort ortA : bestimmeOrteImFeld()) {
			if (feldB.bestimmeOrteImFeld().contains(ortA))
				return true;
		}
		return false;
	}

}
