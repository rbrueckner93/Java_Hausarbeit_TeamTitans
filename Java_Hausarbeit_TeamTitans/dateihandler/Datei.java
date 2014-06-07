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
}