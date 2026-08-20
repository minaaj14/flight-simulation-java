package projekat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class JSONreader {

	private Aplikacija aplikacija;
	private ArrayList<String>errorList;
	
	public JSONreader(Aplikacija a) {
		aplikacija=a;
	}
	public void ucitajFajl(File file, InfoLetovi info)  {
		info.restartError();
		errorList=new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			errorList.add("Greska: Fajl ne postoji ili mu nije moguce pristupiti");
			new FajlErrorWindow(new ArrayList<String>(errorList));
			return;
		}
		String red;
		boolean citajAerodrome=false;
		boolean citajLetove=false;
		try {
			while((red = br.readLine()) != null){
				red=red.trim();
				if(red.isEmpty()) continue;
				if (red.contains("\"airports\"")) {
				    citajAerodrome = true;
				    citajLetove=false;
				    continue;
				}

				if (red.contains("\"flights\"")) {
				    citajAerodrome = false;
				    citajLetove = true;
				    continue;
				}
				if(citajAerodrome) {
					if(red.startsWith("{")) {
						parsirajRedA(info,red);
					}
					else continue;
				}
				if(citajLetove) {
					if(red.startsWith("{")) {
						parsirajRedL(info,red);
					}
					else continue;
				}
				
			}
		} catch (IOException e) {
			errorList.add("Greska pri otvaranju fajla");
			new FajlErrorWindow(new ArrayList<String>(errorList));
			return;
		}
		if(!errorList.isEmpty()) {
			new FajlErrorWindow(new ArrayList<String>(errorList));
		}
		
	}
	
	
	void parsirajRedA(InfoLetovi info,String s) {
		s = s.trim();
		if (!s.contains(":")) return;
	    if (s.length() == 0) return;
	   
		if (s.endsWith(",")) {
	        s = s.substring(0, s.length() - 1);
	    }
	    s = s.substring(1, s.length() - 1);

	    String[] polja = s.split(",");
	    if(polja.length!=4) {
	    	 errorList.add("Pogresan format");
			 return;
	    }

	    String code = polja[0].split(":")[1].replace("\"", "");
	    String name = polja[1].split(":")[1].replace("\"", "");
	    int x = Integer.parseInt(polja[2].split(":")[1]);
	    int y = Integer.parseInt(polja[3].split(":")[1]);
	    
	    if(info.validateAerodrom(name, code,polja[2].split(":")[1] , polja[3].split(":")[1])) {
	    	 info.dodajAerodrom(new Aerodrom(code,name,x,y));
	 	    aplikacija.dodajAerodromPodatke(name, code, polja[2].split(":")[1], polja[3].split(":")[1]);
	    }
	    else {
			  errorList.add(info.getErrorMessage());
			  info.restartError();
		  }
		
	}
	
	
	void parsirajRedL(InfoLetovi info,String s) {
		s = s.trim();
	
		if (s.endsWith(",")) {
		    s = s.substring(0, s.length() - 1);  
		}
		s = s.substring(1, s.length() - 1);
		
		String[] polja = s.split(",");
		if(polja.length!=4) {
	    	 errorList.add("Pogresan format");
			 return;
	    }
	    String from = polja[0].split(":")[1].replace("\"", "");
	    String to = polja[1].split(":")[1].replace("\"", "");
	    String departure = polja[2].split(":",2)[1].replace("\"", "");
	    int duration = Integer.parseInt(polja[3].split(":")[1]);
	    
	    if(info.validateLet(from, to, departure,polja[3].split(":")[1])) {
	    	Aerodrom a1=info.pronadjiAerodrom(from);
			Aerodrom a2=info.pronadjiAerodrom(to);
	    	info.dodajLet(new Let(a1,a2,DodavanjeLeta.parsirajVreme(departure),duration));
    	    aplikacija.dodajLetPodatke(from,to,departure,polja[3].split(":")[1]);
	    }
	    else {
			  errorList.add(info.getErrorMessage());
			  info.restartError();
		}
}
	
	
}
