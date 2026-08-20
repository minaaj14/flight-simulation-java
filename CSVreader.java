package projekat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CSVreader {

	private Aplikacija aplikacija;
	private ArrayList<String>errorList;
	
	public CSVreader(Aplikacija a) {
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
				
				if(red.equals("# AIRPORTS")) {
					citajAerodrome=true;
					citajLetove=false;
					continue;
				}
				if(red.equals("# FLIGHTS")) {
					citajLetove=true;
					citajAerodrome=false;
					continue;
				}
				if(citajAerodrome) {
					  if (red.equals("CODE,NAME,X,Y"))continue;
					  else {
						  
						  String[] delovi = red.split(",");
						  if(delovi.length!=4) {
							  errorList.add("Pogresan format");
							  continue;
						  }
						  if(info.validateAerodrom(delovi[1], delovi[0], delovi[2], delovi[3])) {
							  info.dodajAerodrom(new Aerodrom(delovi[0], delovi[1], Integer.parseInt(delovi[2]), Integer.parseInt(delovi[3])));
							  aplikacija.dodajAerodromPodatke(delovi[1], delovi[0], delovi[2], delovi[3]);
						  }
						  else {
							  errorList.add(info.getErrorMessage());
							  info.restartError();
						  }
					  }	  
				}
				if(citajLetove) {
					if(red.equals("FROM,TO,DEPARTURE,DURATION"))continue;
					else {
						String[] delovi = red.split(",");
						if(delovi.length!=4) {
							  errorList.add("Pogresan format");
							  continue;
						  }
						if(info.validateLet(delovi[0], delovi[1], delovi[2], delovi[3])) {
							Aerodrom a1=info.pronadjiAerodrom(delovi[0]);
							Aerodrom a2=info.pronadjiAerodrom(delovi[1]);
							Let novi=new Let(a1,a2,DodavanjeLeta.parsirajVreme(delovi[2]),Integer.parseInt(delovi[3]));
							info.dodajLet(novi);
							aplikacija.dodajLetPodatke(delovi[0], delovi[1], delovi[2], delovi[3]);
						}
						 else {
							  errorList.add(info.getErrorMessage());
							  info.restartError();
						  }

					}
				}
			}
		} catch (NumberFormatException e) {
			
		} catch (IOException e) {
			errorList.add("Greska pri otvaranju fajla");
			new FajlErrorWindow(new ArrayList<String>(errorList));
			return;
		}
		if(!errorList.isEmpty()) {
			new FajlErrorWindow(new ArrayList<String>(errorList));
		}
	}
	
	
	
}