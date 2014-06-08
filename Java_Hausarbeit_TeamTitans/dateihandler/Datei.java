package dateihandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.DateiSyntaxFehler;
import exceptions.MerkmalMissing;
import main.Benutzerinterface;

/**
 * @author TolleN
 * 
 */
public class Datei {
	public static final String KOMMENTARMARKER = "##";
	public static final String MERKMAL_BEGINN = "[";
	public static final String MERKMAL_ENDE = "]";
	public static final String BEZEICHNER_WERT_TRENNER = "|";
	/**
	 * Dateieindung ohne Punkt
	 */
	public static final String STANDARD_DATEITYP = "txt";
	
	protected int aktuelleZeile = 0;

	/**
	 * Methode die eine gegebene Datei Zeilenweise ausliest und ein Array von
	 * Strings aller Zeilen zurückgibt. Datei muss von UI geprüft worden sein
	 * auf lesbarkeit und isFile().
	 * 
	 * @param neueDatei
	 *            Datei die eingelsen werden soll.
	 * @return ArrayList<String> pro Index eine Zeile.
	 */
	public static ArrayList<String> leseDatei(File aktuelleDatei) {
		ArrayList<String> zeilen = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(aktuelleDatei);
			BufferedReader reader = new BufferedReader(fileReader);
			while (reader.ready()) {
				/*
				 * Liesst die Datei und schreibt eine Zeile in ein Element der ArrayList. 
				 * Dabei werden Leerzeichen vorne und hinten entfernt.
				 */
				zeilen.add(reader.readLine().trim());
			}
			reader.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Datei nicht gefunden");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fehler in Datei");
		}
		return zeilen;
	}

	/**
	 * Schreibt einen Liste von Strings. Jedes Objekt ist eine neue Zeile.
	 * Bekommt Dateinamen[inklusive Suffix!] und OutputStream als String
	 * 
	 * @param zuSchreibenderText
	 *            ArrayList von Strings mit allen Zeilen.
	 * @param neuerDateiname
	 *            String des Namen+Suffix der Datei(kein ".txt") enthält.
	 * @author Nils
	 */
	public static void schreibeDatei(ArrayList<String> zuSchreibenderText,
			String neuerDateiname) {
			File neuerOrdner = new File(Benutzerinterface.STANDARDPFAD + "\\");
			//Check, ob der Ordner in den geschrieben werden soll existiert
			if (!neuerOrdner.exists()) {
				int entscheidung = JOptionPane
						.showConfirmDialog(
								null,
								"Achtung - Ordner \""
										+ Benutzerinterface.STANDARDPFAD
										+ "\" nicht vorhanden.\nSoll dieser erstellt werden?",
								"Ornder erstellen?", JOptionPane.YES_NO_OPTION);
				if (entscheidung == JOptionPane.YES_OPTION) {
					//Erstellt den Ordner nach User-Entscheidung
					neuerOrdner.mkdir();
				}
				if (entscheidung == JOptionPane.NO_OPTION) {
					//Abbruch der Methode
					return;
				}
			}
		File neueDatei = new File(Benutzerinterface.STANDARDPFAD + "\\"
				+ neuerDateiname +"."+ STANDARD_DATEITYP);
		//Prueft ob die Datei bereits existiert
		if (neueDatei.exists()) {
			int entscheidung = JOptionPane
					.showConfirmDialog(
							null,
							"Achtung - Datei \""
									+ Benutzerinterface.STANDARDPFAD
									+ "\\"
									+ neuerDateiname
									+ "."+STANDARD_DATEITYP
									+ "\" existiert bereits.\nSoll sie ueberschrieben werden?",
							"Datei speichern?", JOptionPane.YES_NO_OPTION);
			if (entscheidung == JOptionPane.NO_OPTION) {
				//Abbruch der Methode
				return;
			}
			if (entscheidung == JOptionPane.YES_OPTION) {
				//Springt weiter zur erstellung der Datei und anschliessenden Schreiben.
			}
		}
		try {
			neueDatei.createNewFile();
			PrintStream writer = new PrintStream(neueDatei);
			//Schreibt jede Zeile in die neue Datei.
			for (String zeile : zuSchreibenderText) {
				writer.println(zeile);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Datei nicht gefunden");
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fehler bei Dateierstellung");
			return;
		}
	}
	
	/**
	 * Check ob Zeile eine Kommentarzeile ist.
	 * @param zeile 
	 * @return true bei Kommentarzeile, sonst false.
	 */
	public static boolean istKommentarZeile(String zeile) {
		if (zeile.indexOf(KOMMENTARMARKER) == -1
				|| zeile.indexOf(KOMMENTARMARKER) > 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Guckt, ob nach dem DateiendeMarker noch text steht.
	 * @param zeile Zeile des DateiendeMarker
	 * @param text Text; der ausgewertet werden soll.
	 */
	public void checkLeeresDateiende(int zeile, ArrayList<String> text) {
		zeile++;
		while (zeile < text.size()) {
			if (text.get(zeile).equals("") || text.get(zeile).equals("\n")) {
				zeile++;
				continue;
			}
			JOptionPane.showMessageDialog(null,
					"Achtung! - Weiterer Text nach Datei Ende Marker gefunden");
			return;
		}
	}
	
	/**
	 * Methode erzeugt einen String des aktuellen Dateinamens von der momentan
	 * ausgewerteten Datei. Prueft zusaetzlich, ob es sich um eine txt Datei
	 * handelt.
	 * 
	 * @param Datei
	 *            , die gerade ausgewertet wird.
	 * @return String des Dateinamens ohne suffix.
	 */
	public String getDateiNamen(File datei) throws DateiSyntaxFehler{
		int dateiEndung = datei.getName().indexOf("."+STANDARD_DATEITYP);
		if (dateiEndung == -1) {
			JOptionPane
					.showMessageDialog(null, "Falsche Dateiendung der Datei");
			throw new DateiSyntaxFehler();
		}
		String dateiName = datei.getName().substring(0, dateiEndung);
		return dateiName;
	}
	
	/**
	 * * Wertet ein Datensatz nach einem spez. Merkmal aus.
	 * 
	 * @param wertBezeichner
	 *            Spezifischer Bezeichner des Wertes.
	 * @param zeile
	 *            String in dem Merkmal stehen muss. Muss der komplette
	 *            Datensatz sein.
	 * @return Wert des gesuchten Merkmals als String
	 * @throws MerkmalMissing
	 *             Fehler bei fehlendem oder defektem merkmal
	 * @throws DateiSyntaxFehler
	 */
	public String getMerkmal(String wertBezeichner, String zeile)
			throws MerkmalMissing, DateiSyntaxFehler {
		// Index des Merkmalbeginns. Check mit "[" vorran gestellt um
		// Leerzeichen dazwischen auszuschliessen.
		int anfang = zeile.indexOf(MERKMAL_BEGINN + wertBezeichner, 0);
		// Check ob Merkmal gefunden wurde.
		if (anfang == -1) {
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		// Index des Ende des Merkmals
		int ende = zeile.indexOf(MERKMAL_ENDE, anfang);
		// Check, ob das Merkmal einzigartig im Datensatz ist
		if (zeile.indexOf(MERKMAL_BEGINN + wertBezeichner, ende) != -1) {
			JOptionPane.showMessageDialog(null, "Merkmal \"" + wertBezeichner
					+ "\" mehrfach im Datensatz ab Zeile "
					+ (aktuelleZeile + 1) + " vorhanden.");
			throw new DateiSyntaxFehler();
		}
		// Extrahieren des spezifischen Merkmals
		String inhaltMerkmal = zeile.substring(anfang, ende);
		// Ausplitten des Merkmals in Bezeichner und Wert
		String[] merkmalsplit = inhaltMerkmal.split("\\"
				+ BEZEICHNER_WERT_TRENNER);
		// Check ob Merkmal Inhalt besitzt
		if (merkmalsplit.length == 1) {
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		if (merkmalsplit[1].isEmpty()) {
			throw new MerkmalMissing(wertBezeichner, aktuelleZeile);
		}
		// Check auf Korrektes Merkmal und anschließende Rückgabe.
		if (merkmalsplit[0].equals(MERKMAL_BEGINN + wertBezeichner)) {
			return merkmalsplit[1].trim();
		} else {
			JOptionPane.showMessageDialog(null, "Fehler im Merkmal "
					+ wertBezeichner + " in Datensatz der in Zeile "
					+ (aktuelleZeile + 1) + " beginnt");
		}
		return null;
	}
}