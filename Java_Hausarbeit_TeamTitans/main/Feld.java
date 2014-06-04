package main;

import java.util.ArrayList;
import orte.Ort;

public class Feld {
	public int startX;
	public int endX;
	public int startY;
	public int endY;
	public int kantenlaenge;
	public Karte kartenInstanz;

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
		for (Ort o : kartenInstanz.orte) {
			// wenn der gefundene Ort tatsaechlich in dem Feld oder auf seiner
			// Grenze liegt
			if (o.koordX < endX && o.koordX > startX && o.koordY < endY
					&& o.koordY > startY)
				// fuege ihn den gefundenen Orten hinzu, die zurueckgegeben
				// werden (s.u.)
				bestimmteOrte.add(o);
		}
		// TODO REMOVE DEBUG ONLY CODE
		// System.out.println(bestimmteOrte.size() + " im Feld " + this
		// + " lokalisiert." + startX + "|" + startY);
		// END DEBUG ONLY CODE
		return bestimmteOrte;
	}

	/**
	 * Fragt jeden Ort ob Relevanzgrad groesser als bislang gefundener Ort mit
	 * hoechstem Relevanzgrad ist.
	 * 
	 * @author bruecknerr
	 * @return Ort mit hoechstem Relevanzgrad im aktuellen Feld
	 */
	public Ort wichtigsterOrtImFeld() {
		Ort wichtigsterOrt = null;
		if (!feldIstLeer()) {
			System.out
					.println("Da das Feld nicht leer ist, bestimme ich nun den...");
			// setze initial wichtigsterOrt als das erste Elt. der Liste
			wichtigsterOrt = bestimmeOrteImFeld().get(0);
			for (Ort ort : bestimmeOrteImFeld()) {
				// ueberschreibe jedes Mal, wenn ein Ort mit hoeherem RG
				// gefunden
				// wurde.
				System.out.println("In der Liste stehen "
						+ bestimmeOrteImFeld().size() + " Orte.");
				System.out.println(ort.name + " wird untersucht.");
				if (ort.getRelevanzGrad() > ermittleWichtigstenFeldOrt()
						.getRelevanzGrad())
					wichtigsterOrt = ort;
			}
		}
		// TODO REMOVE DEBUG ONLY CODE
		System.out.println("Ich werde " + wichtigsterOrt.name
				+ " als wichtigsten Ort an den Commander melden.");
		// END DEBUG ONLY CODE
		return wichtigsterOrt;
	}

	// import aus Karte,

	/**
	 * 
	 * @return Hauptort oder Umschlagpunkt eines Feldes mit hoechstem RG
	 * @author bruecknerr
	 */
	public Ort ermittleWichtigstenFeldOrt() {
		Ort wichtigsterOrt = null;
		double revGradWichtigsterOrt = Double.MIN_VALUE;
		for (Ort ort : bestimmeOrteImFeld()) {
			if (((ort.kennung == Ort.KENNUNG_HAUPTORT) || (ort.kennung == Ort.KENNUNG_UMSCHLAGPUNKT))
					&& (ort.getRelevanzGrad() > revGradWichtigsterOrt)) {
				wichtigsterOrt = ort;
				revGradWichtigsterOrt = ort.getRelevanzGrad();
			}
		}
		return wichtigsterOrt;
	}

	public boolean feldIstLeer() {
		return (bestimmeOrteImFeld().size() == 0);
	}

	/**
	 * Eine Ueberpruefung findet auf
	 * 
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
