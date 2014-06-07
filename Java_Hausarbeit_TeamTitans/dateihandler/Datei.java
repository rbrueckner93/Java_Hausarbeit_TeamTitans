package dateihandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
			if (!neuerOrdner.exists()) {
				int entscheidung = JOptionPane
						.showConfirmDialog(
								null,
								"Achtung - Ordner \""
										+ Benutzerinterface.STANDARDPFAD
										+ "\" nicht vorhanden.\nSoll dieser erstellt werden?",
								"Ornder erstellen?", JOptionPane.YES_NO_OPTION);
				if (entscheidung == JOptionPane.YES_OPTION) {
					neuerOrdner.mkdir();
				}
				if (entscheidung == JOptionPane.NO_OPTION) {
					return;
				}
			}
		File neueDatei = new File(Benutzerinterface.STANDARDPFAD + "\\"
				+ neuerDateiname +"."+ STANDARD_DATEITYP);
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
				return;
			}
			if (entscheidung == JOptionPane.YES_OPTION) {
			}
		}
		try {
			neueDatei.createNewFile();
			PrintStream writer = new PrintStream(neueDatei);
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
}