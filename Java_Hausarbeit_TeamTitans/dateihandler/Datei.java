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
	 * Methode die eine gegebene Datei Zeilenweise ausliest und ein Array von
	 * Strings aller Zeilen zurückgibt. Datei muss von UI geprüft worden sein
	 * auf lesbarkeit und isFile().
	 * 
	 * @param neueDatei
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
	 * bekommt Dateinamen[inklusive Suffix!] und OutputStream als String
	 * 
	 * @param zuSchreibenderText
	 *            ArrayList von Strings mit allen Zeilen.
	 * @param neuerDateiname
	 *            String der Namen+Suffix der Datei(kein ".txt") enthält.
	 * @author Nils
	 */
	public static void schreibeDatei(ArrayList<String> zuSchreibenderText,
			String neuerDateiname) {
		System.out.println(Benutzerinterface.standardpfad+"  "+neuerDateiname);
		File neueDatei = new File(Benutzerinterface.standardpfad+ "\\"
				+ neuerDateiname + ".txt");
		try {
			PrintStream writer = new PrintStream(neueDatei);
			for (String zeile : zuSchreibenderText) {
				writer.println(zeile);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Datei nicht gefunden");
			return;
		}

	}

}