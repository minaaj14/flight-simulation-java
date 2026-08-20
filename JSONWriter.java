package projekat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class JSONWriter {

	private Aplikacija ap;
	private ArrayList<String>greska;
	
	public JSONWriter(Aplikacija ap) {
		this.ap=ap;
		greska=new ArrayList<String>();
	}
	
	void sacuvajFajl(File fajl, InfoLetovi info) {
		
		greska=new ArrayList<String>();
		
		try (PrintWriter pw = new PrintWriter(fajl)) {

	        pw.println("{");

	        pw.println("\"airports\":[");
	        
	        for (int i = 0; i < info.getAerodromi().size(); i++) {
	            Aerodrom a = info.getAerodromi().get(i);

	            pw.print("{");
	            pw.print("\"code\":\"" + a.getKod() + "\",");
	            pw.print("\"name\":\"" + a.getNaziv() + "\",");
	            pw.print("\"x\":" + a.getX() + ",");
	            pw.print("\"y\":" + a.getY());
	            pw.print("}");

	            if (i < info.getAerodromi().size() - 1)
	                pw.println(",");
	            else
	                pw.println();
	        }

	        pw.println("],");

	        pw.println("\"flights\":[");
	        
	        for (int i = 0; i < info.getLetovi().size(); i++) {
	            Let l = info.getLetovi().get(i);

	            int sati = l.vremePoletanja / 60;
	            int minuti = l.vremePoletanja % 60;
	            String vreme = String.format("%02d:%02d", sati, minuti);

	            pw.print("{");
	            pw.print("\"from\":\"" + l.a1.getKod() + "\",");
	            pw.print("\"to\":\"" + l.a2.getKod() + "\",");
	            pw.print("\"departure\":\"" + vreme + "\",");
	            pw.print("\"duration\":" + l.trajanje);
	            pw.print("}");

	            if (i < info.getLetovi().size() - 1)
	                pw.println(",");
	            else
	                pw.println();
	        }

	        pw.println("]");

	        pw.println("}");

	    } catch (IOException e) {
	    	greska.add("Greska pri cuvanju fajla.");
	        new FajlErrorWindow(greska);
	    } catch(NullPointerException e) {
	        greska.add("Greska: nedostaju podaci za cuvanje fajla.");
	        new FajlErrorWindow(greska);
	    }
	}
}
