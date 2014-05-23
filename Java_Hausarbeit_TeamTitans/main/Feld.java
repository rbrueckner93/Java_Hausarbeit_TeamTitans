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
	 * @author BruecknerR
	 * @return eine ArrayList mit Orten, die sich in diesem Feld befinden
	 */
	public ArrayList<Ort> bestimmeOrteAusserASLImFeld() {
		ArrayList<Ort> bestimmteOrte = new ArrayList<Ort>();
		for (Ort o : kartenInstanz.orte) {
			if (o.koordX < endX && o.koordX > startX && o.koordY < endY
					&& o.koordY > startY)
				if(o.kennung != Ort.KENNUNG_AUSLANDSVERBINDUNG) bestimmteOrte.add(o);
		}
		//System.out.println(bestimmteOrte.size()+" lokalisiert."+startX+"|"+startY);
		return bestimmteOrte;
	}

	/**
	 * @author BruecknerR
	 * @param kantenlaenge
	 * @param kantenlaenge
	 * @param minOrte
	 * @return eine ArrayList<ArrayList<Integer>>. Ein jedes Element der
	 *         ArrayList stellt ein Feld dar. Es ist sichergestellt, dass die
	 *         Felder sich nicht ueberlappen. In den einzelnen Feldern sind die
	 *         Informationen wie folgt angeordnet: ArrayList mit int: (0):
	 *         x-Beginn (1):x-Ende (2):y-Beginn (3):y-Ende
	 */
	

	/**
	 * @author BruecknerR
	 * @param feldB
	 *            irgendein Feld.
	 * @return true wenn es eine Schnittmenge der angegebenen Flaechen gibt
	 */
		
	public boolean ueberschneidungMitFeld(Feld feldB) {
		// pruefe, ob die Felder einen x-Abschnitt gemeinsam haben
		
		boolean feldB_schneidetUnten = (startY < feldB.endY && startY > feldB.startY);
		boolean feldB_schneidetOben = (endY < feldB.endY && endY > feldB.startY);
		boolean feldB_schneidetLinks = (startX < feldB.endX && startX > feldB.startX);
		boolean feldB_schneidetRechts = (endX < feldB.endX && endX > feldB.startX);
		boolean direktUebereinander = (startX == feldB.startX);
		boolean direktNebeneinander = (startY == feldB.startY);
		boolean schneidenSich = ((feldB_schneidetOben || feldB_schneidetUnten || direktNebeneinander)&&(feldB_schneidetLinks || feldB_schneidetRechts || direktUebereinander));
		
		if(schneidenSich)
		{	System.out.println("Ueberschneidung festgestellt.");
				return true;
			}
		return false;
	}

}
