package projekat;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CSVWriter {
	
	private Aplikacija ap;
	private ArrayList<String>greska;

	public CSVWriter(Aplikacija ap) {
		this.ap=ap;
		greska=new ArrayList<String>();
	}
	void sacuvajFajl(File fajl, InfoLetovi info) {
		
		greska=new ArrayList<String>();
		
		try (PrintWriter pw = new PrintWriter(fajl)) {

	        pw.println("# AIRPORTS");
	        pw.println("CODE,NAME,X,Y");

	        for (Aerodrom a : info.getAerodromi()) {
	            pw.println(a.getKod() + "," +
	                       a.getNaziv() + "," +
	                       a.getX() + "," +
	                       a.getY());
	        }

	        pw.println("# FLIGHTS");
	        pw.println("FROM,TO,DEPARTURE,DURATION");

	        for (Let l : info.getLetovi()) {
	            int h = l.vremePoletanja / 60;
	            int m = l.vremePoletanja % 60;
	            String vreme = String.format("%02d:%02d", h, m);

	            pw.println(l.a1.getKod() + "," +
	                       l.a2.getKod() + "," +
	                       vreme + "," +
	                       l.trajanje);
	        }

	    } catch (IOException e) {
	    	greska.add("Greska pri cuvanju fajla.");
	        new FajlErrorWindow(greska);
	    } catch(NullPointerException e) {
	        greska.add("Greska: nedostaju podaci za cuvanje fajla.");
	        new FajlErrorWindow(greska);
	    }
	}
}
