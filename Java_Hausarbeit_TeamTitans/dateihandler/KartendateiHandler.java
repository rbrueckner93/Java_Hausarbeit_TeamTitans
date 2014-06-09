package dateihandler;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import netz.Karte;
import exceptions.DateiSyntaxFehler;
import exceptions.MerkmalMissing;
import orte.Auslandsverbindung;
import orte.Hauptort;
import orte.Nebenort;
import orte.Ort;
import orte.Umschlagpunkt;

/**
 * @author TolleN
 */

public class KartendateiHandler extends Datei {
	public static final String DATEI_BEGINN_MARKER = ">->map";
	public static final String DATEI_ENDE_MARKER = "<-<map";
	public static final String DATENSATZ_BEGINN_MARKER = ">->loc";
	public static final String DATENSATZ_ENDE_MARKER = "<-<loc";
	public static final String BEZEICHNER_X_KOORDINATE = "x";
	public static final String BEZEICHNER_Y_KOORDINATE = "y";
	public static final String BEZEICHNER_NAME = "name";
	public static final String BEZEICHNER_KENNUNG = "kind";
	public static final String BEZEICHNER_EINWOHNERZAHL = "population";
	public static final String BEZEICHNER_UMSCHLAGVOLUMEN = "turnover";
	public static final String BEZEICHNER_PASSAGIERAUFKOMMEN = "passengers";
	public static final int MIN_ORTABSTAND = 3;
	public static final int MIN_KOORD_X = 0;
	public static final int MAX_KOORD_X = 199;
	public static final int MIN_KOORD_Y = 0;
	public static final int MAX_KOORD_Y = 99;

	private int anzahlAusgewerteteDatensaetze = 0;

	private Karte kartenInstanz;

	private File aktuelleKartendatei;

	/**
	 * Konstruktor, der eine Datei und ein Objekt der Karte benoetigt.
	 * 
	 * @param kartenInstanz
	 *            Zulesende Datei.
	 * @param aktuelleKartendatei
	 *            Kartenobjekt, das gefuellt wird.
	 * @author TolleN
	 */
	public KartendateiHandler(Karte kartenInstanz, File aktuelleKartendatei) {
		super();
		this.kartenInstanz = kartenInstanz;
		this.aktuelleKartendatei = aktuelleKartendatei;
	}

	/**
	 * Methode liesst eine Datei ein und wertet diese Zeilenweise aus.
	 * @author Nils
	 */
	public void verarbeiteKartendatei() throws DateiSyntaxFehler {
		anzahlAusgewerteteDatensaetze = 0;
		// Erstellt eine ArrayList mit dem Zeileninhalt der Datei.
		ArrayList<String> geleseneDaten = Datei.leseDatei(aktuelleKartendatei);
		aktuelleZeile = 0;
		// Schreibt den Dateinamen in die aktuelle Kartendatei.
		kartenInstanz.setNameKartendatei(getDateiNamen(aktuelleKartendatei));
		// Prueft auf einen vorhandenen Dateibeginn Marker.
		int dateiAnfang = findeDateiBeginnMarker(aktuelleZeile, geleseneDaten);
		if (dateiAnfang == -1) {
			JOptionPane.showMessageDialog(null, "Fehlender Dateibeginn Marker");
			// Exception meldet Fehler der Syntax.
			throw new DateiSyntaxFehler();
		}
		// Check auf ein Dateiende.
		int dateiEnde = findeDateiEndeMarker(dateiAnfang, geleseneDaten);
		if (dateiEnde == -1) {
			JOptionPane.showMessageDialog(null, "Fehlender Dateiende Marker");
			throw new DateiSyntaxFehler();
		}
		checkLeeresDateiende(dateiEnde, geleseneDaten);
		// Ueberpruefung, ob auswertbare Datensaetze vorhanden sind.
		if (!datensatzBeginnMarkerVorhanden(aktuelleZeile, geleseneDaten)) {
			JOptionPane.showMessageDialog(null,
					"Kein auswertbarer Datensatz in der Datei gefunden");
			throw new DateiSyntaxFehler();
		}
		// Prueft hier auf alle Datensaetze korrekt ausgewertet werden koennen.
		if (!datensatzMarkiererGleichwertig(dateiAnfang, geleseneDaten)) {
			JOptionPane
					.showMessageDialog(
							null,
							"Achtung! - Es fehlen Datensatzmarkierer zur korrekten Auswertung der Datei.\nOder es stehen 2 identische Marker in einer Zeile.");
		}
		/*
		 * In dieser Schleife, wird wiederholt nach Datensaetzen gesucht und
		 * diese dann ausgewertet. Dies passiert so lange, bis keine Datensaetze
		 * mehr vorhanden sind oder der DateiendeMarker erreicht wurde.
		 */
		while (datensatzBeginnMarkerVorhanden(aktuelleZeile, geleseneDaten)
				&& aktuelleZeile < dateiEnde) {
			// Zeile des Beginns.
			int datensatzBeginn = findeDatensatzBeginnMarker(aktuelleZeile,
					geleseneDaten);
			// Zeile des Endes.
			int datensatzEnde = findeDatensatzEndeMarker(datensatzBeginn,
					geleseneDaten);
			aktuelleZeile = datensatzBeginn;
			// Eigentliche Auswertung.
			werteDatensatzAus(datensatzBeginn, datensatzEnde, geleseneDaten);
			// Sprung an das Ende des Datensatzes.
			aktuelleZeile = datensatzEnde;
			// Sprung in naechste Zeile, wenn Datensatz in genau einer Zeile
			// war.
			if (datensatzBeginn == datensatzEnde) {
				aktuelleZeile += 1;
			}
		}
		// Meldung an den User, wie viele Datensaetze erfolgreich ausgewertet
		// wurden.
		JOptionPane.showMessageDialog(null, "Es wurde/n "
				+ anzahlAusgewerteteDatensaetze
				+ " Datensatz/Datensaetze erfolgreich eingelesen.");
	}

	/**
	 * Sucht einen DateibeginnMarker.
	 * 
	 * @param beginn
	 *            Zeile ab der Gesucht werden soll.
	 * @param text
	 *            ArrayList von Strings, in der gesucht werden soll.
	 * @return 
	 * 		 	  Integer der Zeile mit Befund, sonst -1.
	 */
	private static int findeDateiBeginnMarker(int beginn, ArrayList<String> text) {
		while (beginn < text.size()) {
			// Sprung in naechste Zeile bei Kommentarzeile.
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
			// Pruefen ob der Marker in der Zeile ist. Sonst Sprung in naechste
			// Zeile.
			int anfang = text.get(beginn).indexOf(DATEI_BEGINN_MARKER);
			if (anfang == -1) {
				beginn++;
				continue;
			}
			int ende = text.get(beginn).indexOf(DATEI_BEGINN_MARKER)
					+ DATEI_BEGINN_MARKER.length();
			if (anfang != 0) {
				return -1;
			}
			// Pruefen, ob Marker korrekt ist und Rueckgabe der Zeile.
			String zeile = text.get(beginn).substring(anfang, ende);
			if (zeile.equals(DATEI_BEGINN_MARKER)) {
				return beginn;
			} else {
				beginn++;
			}
		}
		return -1;
	}

	/**
	 * Sucht nach einem DateiendeMarker. Beachtet dabei, dass im laufenden Text
	 * Datei Beginn Marker auftauchen koennen.
	 * 
	 * @param beginn
	 *            Zeile ab der gesucht werden soll.
	 * @param text
	 *            ArrayList in der gesucht werden soll.
	 * @return Integer 
	 *            der Zeile mit Befund, sonst -1.
	 */
	private static int findeDateiEndeMarker(int beginn, ArrayList<String> text)
			throws DateiSyntaxFehler {
		// Ueberprueft die Zeile, in der bereits ein BeginnMarker gefunden
		// wurde, auf einen weiteren.
		int endeDateiBeginnMarker = text.get(beginn).indexOf(
				DATEI_BEGINN_MARKER)
				+ DATEI_BEGINN_MARKER.length();
		// Beginn der Suche nach DateiBeginnMarker.
		int anfangZeile1 = text.get(beginn).indexOf(DATEI_BEGINN_MARKER,
				endeDateiBeginnMarker);
		if (anfangZeile1 != -1) {
			int endeZeile1 = anfangZeile1 + DATEI_BEGINN_MARKER.length();
			String zutesten = text.get(beginn).substring(anfangZeile1,
					endeZeile1);
			if (zutesten.equals(DATEI_BEGINN_MARKER)) {
				// Fall, wenn 2 Dateibeginn Marker gefunden wurden.
				JOptionPane.showMessageDialog(null,
						"Weiterer Dateibeginn Marker gefunden. In Zeile: "
								+ (beginn + 1));
				throw new DateiSyntaxFehler();
			}
		}
		// Check der momentanen Zeile auf einen DateiendeMarker.
		int anfangZeile11 = text.get(beginn).indexOf(DATEI_ENDE_MARKER);
		if (anfangZeile11 != -1) {
			int endeZeile11 = anfangZeile11 + DATEI_ENDE_MARKER.length();
			if (anfangZeile11 != 0) {
				return -1;
			}
			String test = text.get(beginn)
					.substring(anfangZeile11, endeZeile11);
			if (test.equals(DATEI_ENDE_MARKER)) {
				return beginn;
			}
		}
		// Sprung in die naechste Zeile.
		beginn++;
		// Ueberpruefen der restlichen Zeilen bis zum Dateiende.
		while (beginn < text.size()) {
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
			// Check, ob nicht ein 2. Dateibeginn Marker gefunden wird.
			int anfangAktuelleZeile = text.get(beginn).indexOf(
					DATEI_BEGINN_MARKER);
			if (anfangAktuelleZeile != -1) {
				int endeAktuelleZeile = anfangAktuelleZeile
						+ DATEI_BEGINN_MARKER.length();
				String moeglicherstart = text.get(beginn).substring(
						anfangAktuelleZeile, endeAktuelleZeile);
				if (moeglicherstart.equals(DATEI_BEGINN_MARKER)) {
					JOptionPane.showMessageDialog(null,
							"Weiterer Datei Beginn Marker gefunden. In Zeile: "
									+ (beginn + 1));
					throw new DateiSyntaxFehler();
				}
			}
			// Pruefen auf Dateiende Marker.
			int anfangAktuelleZeile1 = text.get(beginn).indexOf(
					DATEI_ENDE_MARKER);
			if (anfangAktuelleZeile1 != -1) {
				int endeAktuelleZeile1 = anfangAktuelleZeile1
						+ DATEI_ENDE_MARKER.length();
				if (anfangAktuelleZeile1 != 0) {
					return -1;
				}
				String zeile = text.get(beginn).substring(anfangAktuelleZeile1,
						endeAktuelleZeile1);
				// Rueckgabe der Zeile bei positiven Befund.
				if (zeile.equals(DATEI_ENDE_MARKER)) {
					return beginn;
				}
			}
			beginn++;
			continue;
		}
		return -1;
	}

	/**
	 * Guckt, ob die Anzahl gestarteter und geschlossener Datensaetze identisch
	 * ist.
	 * 
	 * @param startZeile
	 * @param text
	 *            Text der ausgewertet werden soll.
	 * @return true oder false
	 */
	private boolean datensatzMarkiererGleichwertig(int startZeile,
			ArrayList<String> text) throws DateiSyntaxFehler {
		int anzahlDatensatzBeginnMarker = 0;
		int anzahlDatensatzEndeMarker = 0;
		int durchlaufBeginnMarker = startZeile;
		int durchlaufEndeMarker = startZeile;
		// Zaehlen aller Datensatzbeginn Marker bis Dateiende.
		while (durchlaufBeginnMarker < text.size()) {
			int datensatzbeginn = text.get(durchlaufBeginnMarker).indexOf(
					DATENSATZ_BEGINN_MARKER);
			if (datensatzbeginn == -1) {
				durchlaufBeginnMarker++;
				continue;
			}
			if (datensatzbeginn >= 0) {
				anzahlDatensatzBeginnMarker++;
				durchlaufBeginnMarker++;
			}
		}
		// Zaehlen aller Datensatzende Marker bis Dateiende.
		while (durchlaufEndeMarker < text.size()) {
			int datensatzEnde = text.get(durchlaufEndeMarker).indexOf(
					DATENSATZ_ENDE_MARKER);
			if (datensatzEnde == -1) {
				durchlaufEndeMarker++;
				continue;
			}
			if (datensatzEnde >= 0) {
				anzahlDatensatzEndeMarker++;
				durchlaufEndeMarker++;
			}
		}
		// Vergleichen der beiden Anzahlen und entsprechende Rueckgabe.
		if (anzahlDatensatzBeginnMarker == anzahlDatensatzEndeMarker) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sucht nach vorhandenem Datensatzbeginn. Kriterium um weiter auszuwerten.
	 * 
	 * @param beginn
	 *            Zeile ab der gesucht werden soll.
	 * @param text
	 *            ArrayList String, in denen gesucht wird.
	 * @return true 
	 *            bei Fund sonst false.
	 */
	private boolean datensatzBeginnMarkerVorhanden(int beginn,
			ArrayList<String> text) {
		/*
		 * Durchsuchen der Datei nach Datensatzbeginn Marker bis Dateiende.
		 * Ablauf wie bei Dateibeginn.
		 */
		while (beginn < text.size()) {
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
			int anfang = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER);
			if (anfang == -1) {
				beginn++;
				continue;
			}
			int ende = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER)
					+ DATENSATZ_BEGINN_MARKER.length();
			String zeile = text.get(beginn).substring(anfang, ende);
			// Rueckgabe von true, wenn ein Marker gefunden wurde.
			if (zeile.equals(DATENSATZ_BEGINN_MARKER)) {
				return true;
			} else {
				beginn++;
			}
		}
		return false;
	}

	/**
	 * Findet DatensatzBeginn Marker im Text.
	 * 
	 * @param beginn
	 *            Zeile, ab der gesucht werden soll.
	 * @param text
	 *            List von Strings, in denen gesucht werden soll.
	 * @return 
	 * 			  Integer der Zeile, sonst -1.
	 */
	private int findeDatensatzBeginnMarker(int beginn, ArrayList<String> text) {
		/*
		 * Schleife arbeitet wie bei Dateibeginn.
		 */
		while (beginn < text.size() - 1) {
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
			int anfang = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER);
			if (anfang == -1) {
				beginn++;
				continue;
			}
			int ende = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER)
					+ DATENSATZ_BEGINN_MARKER.length();
			String zeile = text.get(beginn).substring(anfang, ende);
			if (zeile.equals(DATENSATZ_BEGINN_MARKER)) {
				return beginn;
			} else {
				beginn++;
			}
		}
		return -1;

	}

	/**
	 * Findet einen DatensatzEnde Marker. Ueberprueft dabei auf moegliche
	 * DatensatzBeginnMarker.
	 * 
	 * @param beginn
	 *            Zeile, in der begonnen werden soll.
	 * @param text
	 *            List von Strings, in denen gesucht werden soll.
	 * @return 
	 *            Integer der Zeile mit Befund, sonst beginn Zeile.
	 */
	private static int findeDatensatzEndeMarker(int beginn,
			ArrayList<String> text) throws DateiSyntaxFehler {
		// Ueberprueft die Zeile, in der bereits ein BeginnMarker gefunden
		// wurde, auf einen weiteren.
		int endeDateiBeginnMarker = text.get(beginn).indexOf(
				DATENSATZ_BEGINN_MARKER)
				+ DATENSATZ_BEGINN_MARKER.length();
		// Beginn der Suche nach DateiBeginnMarker.
		int anfangZeile1 = text.get(beginn).indexOf(DATENSATZ_BEGINN_MARKER,
				endeDateiBeginnMarker);
		// Check, ob in Zeile ein Marker gefunden wurde.
		if (anfangZeile1 != -1) {
			int endeZeile1 = anfangZeile1 + DATENSATZ_BEGINN_MARKER.length();
			String zutesten = text.get(beginn).substring(anfangZeile1,
					endeZeile1);
			if (zutesten.equals(DATENSATZ_BEGINN_MARKER)) {
				JOptionPane
						.showMessageDialog(
								null,
								"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde.  In Datensatz ab Zeile: "
										+ (beginn + 1));
				throw new DateiSyntaxFehler();
			}
		}
		// Check, ob in der Zeile ein Dateiende Marker steht. Rueckgabe der Zeile
		// bei Fund.
		int anfangZeile11 = text.get(beginn).indexOf(DATENSATZ_ENDE_MARKER);
		if (anfangZeile11 != -1) {
			int endeZeile11 = anfangZeile11 + DATENSATZ_ENDE_MARKER.length();
			String test = text.get(beginn)
					.substring(anfangZeile11, endeZeile11);
			if (test.equals(DATENSATZ_ENDE_MARKER)) {
				return beginn;
			}
		}
		// Schritt in naechste Zeile.
		beginn++;
		// Schleife ueberprueft die restlichen Zeilen nach dem Schema wie oben.
		while (beginn < text.size()) {
			if (istKommentarZeile(text.get(beginn))) {
				beginn++;
				continue;
			}
			int anfangAktuelleZeile = text.get(beginn).indexOf(
					DATENSATZ_BEGINN_MARKER);
			if (anfangAktuelleZeile != -1) {
				int endeAktuelleZeile = anfangAktuelleZeile
						+ DATENSATZ_BEGINN_MARKER.length();
				String moeglicherstart = text.get(beginn).substring(
						anfangAktuelleZeile, endeAktuelleZeile);
				if (moeglicherstart.equals(DATENSATZ_BEGINN_MARKER)) {
					JOptionPane
							.showMessageDialog(
									null,
									"Datensatzbeginn gefunden, ohne das Vorheriger beendet wurde.  In Datensatz ab Zeile: "
											+ (beginn + 1));
					throw new DateiSyntaxFehler();
				}
			}
			int anfangAktuelleZeile1 = text.get(beginn).indexOf(
					DATENSATZ_ENDE_MARKER);
			if (anfangAktuelleZeile1 != -1) {
				int endeAktuelleZeile1 = anfangAktuelleZeile1
						+ DATENSATZ_ENDE_MARKER.length();
				String zeile = text.get(beginn).substring(anfangAktuelleZeile1,
						endeAktuelleZeile1);
				if (zeile.equals(DATENSATZ_ENDE_MARKER)) {
					return beginn;
				}
			}
			beginn++;
			continue;
		}
		return beginn;
	}

	/**
	 * * Wertet einen Datensatz aus und erstellt moegliches Objekt.
	 * 
	 * @param beginnZeile
	 *            Startzeile des Datensatzes.
	 * @param endeZeile
	 *            Endzeile des Datensatzes.
	 * @param text
	 *            Liste aus Strings, in dem der Datensatz steht.
	 * @throws DateiSyntaxFehler
	 */
	private void werteDatensatzAus(int beginnZeile, int endeZeile,
			ArrayList<String> text) throws DateiSyntaxFehler {
		// Erstellt einen zusammenhaengenden String, des gesamten Datensatzes von
		// Beginn- bis Endezeile.
		String datensatz = "";
		for (int i = beginnZeile; i <= endeZeile; i++) {
			if (istKommentarZeile(text.get(i))) {
				continue;
			}
			datensatz += text.get(i);
		}
		// Auswertung des Datensatzes.
		try {
			int xkoord = Integer.parseInt(ermittleMerkmal(
					BEZEICHNER_X_KOORDINATE, datensatz));
			int ykoord = Integer.parseInt(ermittleMerkmal(
					BEZEICHNER_Y_KOORDINATE, datensatz));
			// Ueberpruefen der Orte auf ihre Position.
			koordinateCheckenX(xkoord);
			koordinateCheckenY(ykoord);
			if (!mindestabstandEingehalten(xkoord, ykoord)) {
				JOptionPane.showMessageDialog(null,
						"Orte liegen zu dicht beeinander. In Datensatz ab Zeile: "
								+ (aktuelleZeile + 1));
				throw new DateiSyntaxFehler();
			}
			// Restliche Auswertung.
			String name = ermittleMerkmal(BEZEICHNER_NAME, datensatz);
			// Check ob Name einzigartig ist.
			boolean nameEinzigartig = true;
			for (Ort ort : kartenInstanz.getOrte()) {
				if (name.equals(ort.getName())) {
					nameEinzigartig = false;
				}
			}
			if (!nameEinzigartig) {
				JOptionPane.showMessageDialog(null, "Name des Ortes \"" + name
						+ "\" schon vorhanden. In Datensatz ab Zeile: "
						+ (aktuelleZeile + 1));
				throw new DateiSyntaxFehler();
			}
			// Auswertung der Kennung.
			String kennung = ermittleMerkmal(BEZEICHNER_KENNUNG, datensatz);
			// Je nach Kennung, wird der Rest ausgewertet und dieser Ort
			// erstellt.
			switch (kennung) {
			case Ort.KENNUNG_HAUPTORT:
				int einwohnerZahl = Integer.parseInt(ermittleMerkmal(
						BEZEICHNER_EINWOHNERZAHL, datensatz));
				erzeugeHauptort(xkoord, ykoord, name, einwohnerZahl);
				break;
			case Ort.KENNUNG_NEBENORT:
				int einwohnerZahlnbn = Integer.parseInt(ermittleMerkmal(
						BEZEICHNER_EINWOHNERZAHL, datensatz));
				erzeugeNebenort(xkoord, ykoord, name, einwohnerZahlnbn);
				break;
			case Ort.KENNUNG_UMSCHLAGPUNKT:
				double umschlagVolumen = Double.parseDouble(ermittleMerkmal(
						BEZEICHNER_UMSCHLAGVOLUMEN, datensatz));
				erzeugeUmschlagpunkt(xkoord, ykoord, name, umschlagVolumen);
				break;
			case Ort.KENNUNG_AUSLANDSVERBINDUNG:
				double umschlagVolumenASL = Double.parseDouble(ermittleMerkmal(
						BEZEICHNER_UMSCHLAGVOLUMEN, datensatz));
				int passagierAufkommen = Integer.parseInt(ermittleMerkmal(
						BEZEICHNER_PASSAGIERAUFKOMMEN, datensatz));
				erzeugeAuslandsverbindung(xkoord, ykoord, name,
						umschlagVolumenASL, passagierAufkommen);
				break;
			// Wenn eine unbekannte Kennung benutzt wurde.
			default:
				JOptionPane.showMessageDialog(null,
						"Ort besitzt unbekannte Kennung. In Datensatz ab Zeile: "
								+ (aktuelleZeile + 1));
				throw new DateiSyntaxFehler();
			}
			anzahlAusgewerteteDatensaetze++;
			// Fehler bei Auswertung der Merkmale.
		} catch (MerkmalMissing e) {
			e.erzeugeMeldung();
			// Diese exception faengt ungueltige Werte bei x und y Koordinaten
			// oder spezifischen Kennzahlen der Orte.
		} catch (NumberFormatException f) {
			JOptionPane.showMessageDialog(null,
					"Fehler in Merkmalen. Zahlen sind keine Zahlen. Datensatz ab Zeile: "
							+ (aktuelleZeile + 1));
			throw new DateiSyntaxFehler();
		}
	}

	/**
	 * Methode ueberprueftm, ob Orte den angegebenen Abstand von 3km eingehalten
	 * haben.
	 * 
	 * @param x
	 *            X-Koordinate.
	 * @param y
	 *            Y-Koordinate.
	 * @return true
	 * 		      ,sonst false.
	 */
	private boolean mindestabstandEingehalten(int x, int y) {
		if (kartenInstanz.getOrte().size() != 0) {
			for (Ort ortB : kartenInstanz.getOrte()) {
				// Berechnung der Distanz, zu jedem anderen Ort.
				double distanz = Math
						.sqrt((Math.pow((x - ortB.getKoordX()), 2))
								+ Math.pow((y - ortB.getKoordY()), 2));
				// Anschliessender Vergleich des Abstandes mit Kennwert.
				if (distanz < MIN_ORTABSTAND) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Ueberorueft,ob X-Koordinate im Bereich der Karte liegt.
	 * 
	 * @param koord
	 *            Integer der Koordinate.
	 */
	private void koordinateCheckenX(int koord) throws DateiSyntaxFehler {
		if (koord > MAX_KOORD_X || koord < MIN_KOORD_X) {
			JOptionPane
					.showMessageDialog(
							null,
							"Ort liegt ausserhalb der erlaubten Karte - X Wert ausserhalb \n In Datensatz ab Zeile: "
									+ (aktuelleZeile + 1));
			throw new DateiSyntaxFehler();
		}
	}

	/**
	 * Ueberorueft, ob Y-Koordinate im Bereich der Karte liegt.
	 * 
	 * @param koord
	 *            Integer der Koordinate.
	 */
	private void koordinateCheckenY(int koord) throws DateiSyntaxFehler {
		if (koord > MAX_KOORD_Y || koord < MIN_KOORD_Y) {
			JOptionPane
					.showMessageDialog(
							null,
							"Ort liegt ausserhalb der erlaubten Karte - Y Wert ausserhalb \n In Datensatz ab Zeile: "
									+ (aktuelleZeile + 1));
			throw new DateiSyntaxFehler();
		}
	}

	// Hier stehen die 4 Methoden, zur Erzeugung der 4 verschiedenen Orte, die
	// dann der spezifischen Liste von Orten der Karte hinzugefuegt werden.
	// @author TolleN
	private void erzeugeHauptort(int koordX, int koordY, String name,
			int anzahlEinwohner) {
		kartenInstanz.getOrte().add(
				new Hauptort(koordX, koordY, name, anzahlEinwohner));
	}

	private void erzeugeNebenort(int koordX, int koordY, String name,
			int anzahlEinwohner) {
		kartenInstanz.getOrte().add(
				new Nebenort(koordX, koordY, name, anzahlEinwohner));
	}

	private void erzeugeUmschlagpunkt(int koordX, int koordY, String name,
			double umschlagVolumen) {
		kartenInstanz.getOrte().add(
				new Umschlagpunkt(koordX, koordY, name, umschlagVolumen));
	}

	private void erzeugeAuslandsverbindung(int koordX, int koordY, String name,
			double umschlagVolumen, int passagierAufkommen) {
		kartenInstanz.getOrte().add(
				new Auslandsverbindung(koordX, koordY, name,
						passagierAufkommen, umschlagVolumen));
	}

}